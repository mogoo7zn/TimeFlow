package cn.edu.ustc.timeflow.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.util.DBHelper
import cn.edu.ustc.timeflow.ui.adapter.FragmentGoalOverviewRecyclerItemAdapter
import cn.edu.ustc.timeflow.ui.dialog.AddActionDialogFragment
import cn.edu.ustc.timeflow.ui.dialog.AddGoalDialogFragment
import com.example.timeflow.R
import com.example.timeflow.databinding.FragmentGoalOverviewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class GoalOverviewFragment : Fragment() {
    private var _binding: FragmentGoalOverviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FragmentGoalOverviewRecyclerItemAdapter
    private lateinit var actionList: List<Action>
    private lateinit var goalList: List<Goal>
    private var currentGoal: Goal? = null

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

        // Refresh goal list
        refreshGoalList()

        // Set up FAB listener
        binding.addActionFab.setOnClickListener {
            showAddActionDialog()
        }

        // Set up item click listeners
        adapter.setOnItemClickListener(object : FragmentGoalOverviewRecyclerItemAdapter.OnItemClickListener {
            override fun onItemClick(action: Action) {
                showEditActionDialog(action)
            }

            override fun onItemLongClick(action: Action) {
                showDeleteConfirmationDialog(action)
            }
        })
    }

    /**
     * Refresh the goal list from the database and update the UI.
     */
    private fun refreshGoalList() {
        val dbHelper = DBHelper(requireContext())
        goalList = dbHelper.getGoalDao().getAll()

        if (goalList.isNotEmpty()) {
            fetchAndDisplayActionsForGoal(goalList[0])
            currentGoal = goalList[0]
        }

        // Set up CheckBox listener
        binding.currentGoalPicker.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showGoalPickerMenu { selectedGoal ->
                    fetchAndDisplayActionsForGoal(selectedGoal)
                }
            }
        }
    }

    /**
     * Show a dialog to edit the selected action.
     */
    private fun showEditActionDialog(action: Action) {
        val dialog = context?.let { AddActionDialogFragment(action, it) }
        dialog?.setOnDismissListener { refreshActionData() }
        dialog?.show(parentFragmentManager, "AddActionDialogFragment")
    }

    /**
     * Show a confirmation dialog to delete the selected action.
     */
    private fun showDeleteConfirmationDialog(action: Action) {
        AlertDialog.Builder(requireContext())
            .setTitle("取消行动")
            .setMessage("确认删除？")
            .setPositiveButton("是") { _, _ ->
                deleteAction(action)
            }
            .setNegativeButton("再想想", null)
            .show()
    }

    /**
     * Delete the selected action from the database and refresh the data.
     */
    private fun deleteAction(action: Action) {
        val dbHelper = DBHelper(requireContext())
        dbHelper.getActionDao().delete(action)
        refreshActionData()
    }

    /**
     * Show a dialog to add a new action.
     */
    private fun showAddActionDialog() {
        currentGoal?.let { goal ->
            context?.let { ctx ->
                AddActionDialogFragment(goal.id, ctx).apply {
                    setOnDismissListener { refreshActionData() }
                }.show(parentFragmentManager, "AddActionDialogFragment")
            }
        }
    }

    /**
     * Refresh the data displayed in the RecyclerView.
     */
    private fun refreshActionData() {
        val dbHelper = DBHelper(requireContext())
        actionList = dbHelper.getActionDao().getActionsForGoal(currentGoal!!.id)
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
        val addGoalButton = view.findViewById<Button>(R.id.add_goal_button)

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedGoal = goalList[position]
            onGoalSelected(selectedGoal)
            dialog.dismiss()
        }

        addGoalButton.setOnClickListener { showAddGoalDialog() }

        dialog.setOnDismissListener { binding.currentGoalPicker.isChecked = false }
        dialog.setContentView(view)
        dialog.show()
    }

    /**
     * Fetch and display actions for the selected goal.
     */
    private fun fetchAndDisplayActionsForGoal(goal: Goal) {
        val dbHelper = DBHelper(requireContext())
        actionList = dbHelper.getActionDao().getActionsForGoal(goal.id)
        adapter.actionList = actionList
        adapter.notifyDataSetChanged()
        view?.findViewById<TextView>(R.id.current_goal)?.text = goal.content
        view?.findViewById<TextView>(R.id.goal_count)?.text = "${goalList.size} ${getString(R.string.goal_connecting_string)} ${actionList.size} ${getString(R.string.action_count)}"
        view?.findViewById<TextView>(R.id.goal_description)?.text = goal.reason
        currentGoal = goal
        view?.findViewById<TextView>(R.id.current_goal)?.setOnClickListener {
            val addGoalDialogFragment = AddGoalDialogFragment(goal)
            addGoalDialogFragment.show(parentFragmentManager, "AddGoalDialogFragment")
            parentFragmentManager.executePendingTransactions() // Ensure the dialog is shown
            addGoalDialogFragment.setOnDismissListener {
                Log.d("GoalOverviewFragment", "onDismissListener")
                refreshGoalList()
                if (goalList.isNotEmpty()) {
                    if (currentGoal == null && goalList.contains(currentGoal)) {
                        fetchAndDisplayActionsForGoal(goalList[0])
                        currentGoal = goalList[0]
                    } else {
                        fetchAndDisplayActionsForGoal(currentGoal!!)
                    }
                }
            }
        }
    }

    /**
     * Show a dialog to add a new goal.
     */
    private fun showAddGoalDialog() {
        val addGoalDialogFragment = AddGoalDialogFragment()
        addGoalDialogFragment.show(parentFragmentManager, "AddGoalDialogFragment")
        parentFragmentManager.executePendingTransactions() // Ensure the dialog is shown
        addGoalDialogFragment.setOnDismissListener {
            Log.d("GoalOverviewFragment", "onDismissListener")
            refreshGoalList()
            if (goalList.isNotEmpty()) {
                if (currentGoal == null && goalList.contains(currentGoal)) {
                    fetchAndDisplayActionsForGoal(goalList[0])
                    currentGoal = goalList[0]
                } else {
                    fetchAndDisplayActionsForGoal(currentGoal!!)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        refreshGoalList()
        if (goalList.isNotEmpty()) {
            if (currentGoal == null && goalList.contains(currentGoal)) {
                fetchAndDisplayActionsForGoal(goalList[0])
                currentGoal = goalList[0]
            } else {
                fetchAndDisplayActionsForGoal(currentGoal!!)
            }
        }
    }

}