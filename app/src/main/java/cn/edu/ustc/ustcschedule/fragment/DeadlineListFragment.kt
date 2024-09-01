package cn.edu.ustc.ustcschedule.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.ustc.adapter.MilestoneAdapter
import com.example.timeflow.R
import com.example.timeflow.databinding.FragmentDeadlineListBinding
import cn.edu.ustc.timeflow.bean.Milestone
import cn.edu.ustc.timeflow.util.DBHelper

class DeadlineListFragment : Fragment() {

    private var _binding: FragmentDeadlineListBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: DBHelper
    private lateinit var milestoneList: List<Milestone>
    private var allMilestonesInRange = mutableListOf<Milestone>()
    private lateinit var adapter: MilestoneAdapter

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
            binding.deadlineRange.setText(R.string._week)
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.menu_month).setOnClickListener {
            onMenuItemSelected("month")
            binding.deadlineRange.setText(R.string._month)
            popupWindow.dismiss()
        }

        popupView.findViewById<View>(R.id.menu_all_actions).setOnClickListener {
            onMenuItemSelected("all")
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}