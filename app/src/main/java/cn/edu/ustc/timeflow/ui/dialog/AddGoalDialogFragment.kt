package cn.edu.ustc.timeflow.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.util.DBHelper
import com.example.timeflow.R
import java.time.LocalDateTime

class AddGoalDialogFragment : DialogFragment {
    private var goalName: EditText? = null
    private var goalReason: EditText? = null
    private var goalMeasure: EditText? = null
    private var goalPriority: EditText? = null
    private var goalFinished: CheckBox? = null
    private var goalActive: CheckBox? = null
    private var function: () -> Unit = {}
    private val goal: Goal

    constructor() {
        goal = Goal()
    }

    constructor(goal: Goal) {
        this.goal = goal
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_goal, container, false)

        goalName = view.findViewById(R.id.goal_name)
        goalReason = view.findViewById(R.id.goal_reason)
        goalMeasure = view.findViewById(R.id.goal_measure)
        goalPriority = view.findViewById(R.id.goal_priority)
        goalFinished = view.findViewById(R.id.goal_finished)
        goalActive = view.findViewById(R.id.goal_active)
        val saveGoalButton = view.findViewById<Button>(R.id.save_goal_button)
        val cancelGoalButton = view.findViewById<Button>(R.id.cancel_goal_button)

        goalName?.let { it.setText(goal.content) }
        goalReason?.let { it.setText(goal.reason) }
        goalMeasure?.let { it.setText(goal.measure) }
        goalPriority?.let { it.setText(goal.priority.toString()) }
        goalFinished?.let { it.isChecked = goal.isFinished }
        goalActive?.let { it.isChecked = goal.isActive }
        if (goal.id == 0) {
            goalFinished?.isEnabled = false
            goalActive?.isEnabled = false
            cancelGoalButton.text = getString(R.string.cancel)
            cancelGoalButton.setOnClickListener { v: View? -> dismiss() }
        } else {
            goalFinished?.isEnabled = true
            goalActive?.isEnabled = true
            cancelGoalButton.text = getString(R.string.delete)
            cancelGoalButton.setOnClickListener { v: View? ->
                val db = DBHelper(requireContext())
                val goalDao = db.getGoalDao()
                goalDao.delete(goal)
                Toast.makeText(requireContext(), "已删除", Toast.LENGTH_SHORT).show()
                function()
                dismiss()
            }
        }

        saveGoalButton.setOnClickListener { v: View? -> saveGoal() }

        return view
    }

    /**
     * Save the goal to the database
     */
    private fun saveGoal() {
        val name = goalName?.text.toString().trim { it <= ' ' }
        val reason = goalReason?.text.toString().trim { it <= ' ' }
        val measure = goalMeasure?.text.toString().trim { it <= ' ' }
        val priority = goalPriority?.text.toString().trim { it <= ' ' }.toInt()
        val finished = goalFinished?.isChecked ?: false
        val active = goalActive?.isChecked ?: false

        if (name.isEmpty() || reason.isEmpty() || measure.isEmpty()) {
            Toast.makeText(requireContext(), "填写完毕再存呦", Toast.LENGTH_SHORT).show()
            return
        }

        goal.content = name
        goal.reason = reason
        goal.priority = priority
        goal.start = LocalDateTime.now()
        goal.end = LocalDateTime.now()
        goal.measure = measure
        goal.isFinished = finished
        goal.isActive = active

        val db = DBHelper(requireContext())
        val goalDao = db.getGoalDao()
        if (goal.id == 0) {
            goalDao.insert(goal)
        } else {
            goalDao.update(goal)
        }

        Toast.makeText(requireContext(), "已存储", Toast.LENGTH_SHORT).show()
        function()
        dismiss()
    }

    fun setOnDismissListener(function: () -> Unit) {
        if (dialog == null)
            Log.d("AddGDF", "setOnDismissListener: null")
        this.function = function
        dialog?.setOnDismissListener { function() }
    }
}