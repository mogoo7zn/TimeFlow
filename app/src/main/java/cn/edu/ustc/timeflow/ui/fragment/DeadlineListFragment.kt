package cn.edu.ustc.timeflow.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.ustc.timeflow.ui.adapter.MilestoneAdapter
import com.example.timeflow.R
import com.example.timeflow.databinding.FragmentDeadlineListBinding
import cn.edu.ustc.timeflow.bean.Milestone
import cn.edu.ustc.timeflow.util.DBHelper
import com.loper7.date_time_picker.DateTimePicker
import java.time.LocalDateTime
import java.time.ZoneOffset

class DeadlineListFragment : Fragment() {

    private var _binding: FragmentDeadlineListBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DBHelper
    private lateinit var milestoneList: List<Milestone>
    private var allMilestonesInRange = mutableListOf<Milestone>()
    private lateinit var adapter: MilestoneAdapter
    private lateinit var milestoneTimePicker: DateTimePicker
    private lateinit var timeRange: String

    private val handler = Handler(Looper.getMainLooper())
    private val refreshRunnable = object : Runnable {
        override fun run() {
            fetchMilestones(timeRange)
            handler.postDelayed(this, 60000) // 1 minute delay
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeadlineListBinding.inflate(inflater, container, false)
        dbHelper = DBHelper(requireContext())

        binding.deadlineList.layoutManager = LinearLayoutManager(context)
        adapter = MilestoneAdapter(mutableListOf(), dbHelper)
        binding.deadlineList.adapter = adapter
        binding.deadlineRange.setText(R.string._week)
        fetchMilestones("week")
        binding.deadlineCount.setText(allMilestonesInRange.size.toString())

        binding.fabAddMilestone.setOnClickListener {
            showAddMilestoneDialog()
        }
        timeRange = arguments?.getString("timeRange") ?: "week"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.deadlineRangePicker.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showPopupMenu(binding.deadlineRangePicker)
            }
        }

        val timeRange = arguments?.getString("timeRange") ?: "week"
        fetchMilestones(timeRange)

        // Start the recurring task
        handler.post(refreshRunnable)
    }

    /**
     * Show a popup menu when the user clicks on the deadline range picker.
     */
    private fun showPopupMenu(view: View) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_menu, null)
        val popupWindow = PopupWindow(popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true)

        popupWindow.showAsDropDown(view, 0, 0)

        popupView.findViewById<View>(R.id.menu_week).setOnClickListener {
            onMenuItemSelected("week")
            timeRange = "week"
            binding.deadlineRange.setText(R.string._week)
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.menu_month).setOnClickListener {
            onMenuItemSelected("month")
            timeRange = "month"
            binding.deadlineRange.setText(R.string._month)
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.menu_all_actions).setOnClickListener {
            onMenuItemSelected("all")
            timeRange = "all"
            binding.deadlineRange.setText(R.string.all)
            popupWindow.dismiss()
        }
        popupWindow.setOnDismissListener {
            binding.deadlineRangePicker.isChecked = false
        }
    }

    private fun onMenuItemSelected(timeRange: String) {
        fetchMilestones(timeRange)
    }

    /**
     * Show a dialog to add a new milestone.
     */
    private fun showAddMilestoneDialog() {
        var localDateTime : LocalDateTime = LocalDateTime.now()     //本地时间
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_milestone, null)
        val milestoneName = dialogLayout.findViewById<EditText>(R.id.milestone_name)    //里程碑名称
        val milestoneTimeCheckbox = dialogLayout.findViewById<CheckBox>(R.id.milestone_time_checkbox)
        milestoneTimePicker = dialogLayout.findViewById<DateTimePicker>(R.id.milestone_time_picker)

        milestoneTimeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            milestoneTimePicker.visibility = if (isChecked) View.VISIBLE else View.GONE

            //控制显示内容
            if (isChecked)
            {
                milestoneTimeCheckbox.text = localDateTime.toString()
            }else{
                milestoneTimeCheckbox.text = getString(R.string.set_time)
            }
        }

        milestoneTimePicker.setOnDateTimeChangedListener{
                millisecond ->
            run {
                localDateTime =
                    LocalDateTime.ofEpochSecond(millisecond / 1000,
                        0,
                        ZoneOffset.of("+8"))
                milestoneTimeCheckbox.text = localDateTime.toString()
            }
        }

        milestoneTimePicker.setThemeColor(resources.getColor(R.color.blue_500))
        milestoneTimePicker.setMinMillisecond(System.currentTimeMillis())
        milestoneTimePicker.setMaxMillisecond(LocalDateTime.now().
        plusYears(10).
        toEpochSecond(ZoneOffset.of("+8")) * 1000)


        builder.setView(dialogLayout)
            .setTitle(R.string.add_milestone)
            .setPositiveButton(R.string.save) { dialog, _ ->
                val name = milestoneName.text.toString().trim()

                val milestone = Milestone().apply {
                    content = name
                    time = localDateTime
                }

                if (name.isEmpty() || localDateTime == null) {
                    Toast.makeText(requireContext(), "填完再存呦~", Toast.LENGTH_SHORT).show()
                }

                if (name.isNotEmpty() && localDateTime != null) {
                    dbHelper.getMilestoneDao().insert(milestone)
                    fetchMilestones(timeRange)
                    dialog.dismiss()
                }
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    /**
     * Fetch milestones from the database based on the specified time range.
     */
    private fun fetchMilestones(timeRange: String) {
        milestoneList = when (timeRange) {
            "week" -> dbHelper.getMilestoneDao().getMilestonesForTimeRange(7)
            "month" -> dbHelper.getMilestoneDao().getMilestonesForTimeRange(30)
            "all" -> dbHelper.getMilestoneDao().getAllMilestones()
            else -> emptyList()
        }
        // Store all milestones in range in a List<Milestone>
        allMilestonesInRange.clear()
        allMilestonesInRange.addAll(milestoneList)
        adapter.updateMilestones(milestoneList)
        adapter.notifyDataSetChanged()

        // Update the deadline count
        binding.deadlineCount.text = allMilestonesInRange.size.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Stop the recurring task
        handler.removeCallbacks(refreshRunnable)
    }
}