package cn.edu.ustc.ustcschedule.fragment

import android.os.Bundle
import android.app.Dialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.ustc.adapter.FragmentGoalOverviewRecyclerItemAdapter
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.util.DBHelper
import com.example.timeflow.R
import com.example.timeflow.databinding.FragmentGoalOverviewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.time.Duration

class GoalOverviewFragment : Fragment() {
    private var _binding: FragmentGoalOverviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FragmentGoalOverviewRecyclerItemAdapter
    private lateinit var actionList: List<Action>
    private lateinit var goalList: List<Goal>
    lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.goalOverviewRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FragmentGoalOverviewRecyclerItemAdapter(requireContext(), emptyList())
        binding.goalOverviewRecyclerView.adapter = adapter

        // Fetch goals from database
        val dbHelper = DBHelper(requireContext())
        goalList = dbHelper.getGoalDao().getAll()

        // Set up CheckBox listener
        binding.currentGoalPicker.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showGoalPickerMenu { selectedGoal ->
                    fetchAndDisplayActionsForGoal(selectedGoal)
                }
            } else {
                adapter.actionList = emptyList()
                adapter.notifyDataSetChanged()
            }
        }

        // Set up FAB listener
        val fab: FloatingActionButton = binding.addActionFab
        fab.setOnClickListener {
            showAddActionDialog()
        }
    }

    private fun showAddActionDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_action)

        val actionName: EditText = dialog.findViewById(R.id.action_name)
        val actionDuration: EditText = dialog.findViewById(R.id.action_duration)
        val actionLocation: EditText = dialog.findViewById(R.id.action_location)
        val actionNote: EditText = dialog.findViewById(R.id.action_note)
        val actionRemind: CheckBox = dialog.findViewById(R.id.action_remind)
        val actionType: RadioGroup = dialog.findViewById(R.id.action_type)
        val weeklyOptions: LinearLayout = dialog.findViewById(R.id.weekly_options)
        val dailyOptions: LinearLayout = dialog.findViewById(R.id.daily_options)
        val saveButton: Button = dialog.findViewById(R.id.save_action_button)

        actionType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.action_type_daily -> {
                    dailyOptions.visibility = View.VISIBLE
                    weeklyOptions.visibility = View.GONE
                }
                R.id.action_type_weekly -> {
                    dailyOptions.visibility = View.GONE
                    weeklyOptions.visibility = View.VISIBLE
                }
                R.id.action_type_once -> {
                    dailyOptions.visibility = View.GONE
                    weeklyOptions.visibility = View.GONE
                }
            }
        }

        saveButton.setOnClickListener {
            val name = actionName.text.toString()
            val duration = actionDuration.text.toString().toLong()
            val location = actionLocation.text.toString()
            val note = actionNote.text.toString()
            val remind = actionRemind.isChecked
            val type = when (actionType.checkedRadioButtonId) {
                R.id.action_type_daily -> "daily"
                R.id.action_type_weekly -> "weekly"
                R.id.action_type_once -> "once"
                else -> ""
            }

            // Create new Action object
//            val newAction = Action(
//                goal_id = 0, // Set the appropriate goal_id
//                name = name,
//                duration = duration,
//                location = location,
//                note = note,
//                remind = remind,
//                type = type,
//                finished = false,
//                restrictions = emptyList() // Set the appropriate restrictions
//            )

//            saveActionToDatabase(newAction)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveActionToDatabase(action: Action) {
        val dbHelper = DBHelper(requireContext())
        dbHelper.getActionDao().insert(action)
        refreshData()
    }

    /**
     * Refresh the data displayed in the RecyclerView.
     */
    private fun refreshData() {
        val dbHelper = DBHelper(requireContext())
        actionList = dbHelper.getActionDao().getAll()
        adapter.actionList = actionList
        adapter.notifyDataSetChanged()
    }

    /**
     * Show a dialog to pick a goal from the list of goals.
     */
    private fun showGoalPickerMenu(onGoalSelected: (Goal) -> Unit) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.goal_picker_menu, null)
        val listView: ListView = view.findViewById(R.id.goal_list_view)

        val goalNames = goalList.map { it.content }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, goalNames)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedGoal = goalList[position]
            onGoalSelected(selectedGoal)
            dialog.dismiss()

        }

        dialog.setOnDismissListener { binding.currentGoalPicker.isChecked = false }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun fetchAndDisplayActionsForGoal(goal: Goal) {
        val dbHelper = DBHelper(requireContext())
        actionList = dbHelper.getActionDao().getActionsForGoal(goal.id)
        adapter.actionList = actionList
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}