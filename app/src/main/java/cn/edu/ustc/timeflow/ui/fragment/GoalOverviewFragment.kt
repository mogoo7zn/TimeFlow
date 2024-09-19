package cn.edu.ustc.timeflow.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

        // Fetch goals from database
        val dbHelper = DBHelper(requireContext())
        goalList = dbHelper.getGoalDao().getAll()

        if(goalList.isNotEmpty()) {
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

        // Set up FAB listener
        val fab: FloatingActionButton = binding.addActionFab
        fab.setOnClickListener {
            showAddActionDialog()
        }

        // Set up item click listeners
        adapter.setOnItemClickListener(object : FragmentGoalOverviewRecyclerItemAdapter.
        OnItemClickListener {
            override fun onItemClick(action: Action) {
                showEditActionDialog(action)
            }

            override fun onItemLongClick(action: Action) {
                showDeleteConfirmationDialog(action)
            }
        })
    }

    private fun showEditActionDialog(action: Action) {
        val dialog = AddActionDialogFragment(action,context)
        dialog.show(parentFragmentManager, "AddActionDialogFragment")
    }

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

    private fun deleteAction(action: Action) {
        val dbHelper = DBHelper(requireContext())
        dbHelper.getActionDao().delete(action)
        refreshData()
    }

    private fun showAddActionDialog() {
        currentGoal?.let { AddActionDialogFragment(it.id,context) }
            ?.show(parentFragmentManager, "AddActionDialogFragment")
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

    private fun fetchAndDisplayActionsForGoal(goal: Goal) {
        val dbHelper = DBHelper(requireContext())
        actionList = dbHelper.getActionDao().getActionsForGoal(goal.id)
        adapter.actionList = actionList
        adapter.notifyDataSetChanged()
        view?.findViewById<TextView>(R.id.current_goal)?.text = goal.content
        view?.findViewById<TextView>(R.id.goal_count)?.text = goalList.size.toString() + getString(R.string.goal_connecting_string) +" " + actionList.size.toString() + getString(R.string.action_count)
        view?.findViewById<TextView>(R.id.goal_description)?.text = goal.reason
        currentGoal = goal
        view?.findViewById<TextView>(R.id.current_goal)?.setOnClickListener {
            val dialog = AddGoalDialogFragment(goal)
            dialog.show(parentFragmentManager, "AddGoalDialogFragment")
        }
    }

    private fun showAddGoalDialog() {
        val addGoalDialogFragment = AddGoalDialogFragment()
        addGoalDialogFragment.show(parentFragmentManager, "AddGoalDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}