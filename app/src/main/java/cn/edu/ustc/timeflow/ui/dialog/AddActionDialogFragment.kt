package cn.edu.ustc.timeflow.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.bean.Goal
import cn.edu.ustc.timeflow.restriction.AmountRestriction
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import cn.edu.ustc.timeflow.restriction.IntervalRestriction
import cn.edu.ustc.timeflow.restriction.TimeRestriction
import cn.edu.ustc.timeflow.ui.adapter.ActionTimeAdapter
import cn.edu.ustc.timeflow.ui.adapter.RestrictionAdapter
import cn.edu.ustc.timeflow.util.DBHelper
import com.example.timeflow.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

class AddActionDialogFragment : BottomSheetDialogFragment {
    private var dbHelper: DBHelper

    private var action: Action?
    private var actionType: String = "Fixed" // Default action type
    private var goalId = 0
    private val context: Context

    constructor(goalId: Int, context: Context) {
        action = Action()
        dbHelper = DBHelper(context)
        this.goalId = goalId
        this.context = context
    }

    constructor(action: Action?, context: Context) {
        this.action = action
        dbHelper = DBHelper(context)
        this.context = context
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
        val view = inflater.inflate(R.layout.dialog_add_action, container, false)

        dbHelper = DBHelper(requireContext())
        val actionName = view.findViewById<EditText>(R.id.action_name)
        val actionLocation = view.findViewById<EditText>(R.id.action_location)
        val actionNote = view.findViewById<EditText>(R.id.action_note)
        val actionDuration = view.findViewById<EditText>(R.id.action_duration)
        val actionRemindSwitch = view.findViewById<SwitchMaterial>(R.id.action_remind_switch)
        val actionTypeSwitch = view.findViewById<SwitchMaterial>(R.id.action_type_switch)
        val saveActionButton = view.findViewById<Button>(R.id.save_action_button)

        // TODO: Implement the following features
        val actionAddTimeButton = view.findViewById<ImageButton>(R.id.action_add_time)
        val actionTimeList = view.findViewById<ListView>(R.id.action_time_list)
        val actionAddRestrictionButton = view.findViewById<ImageButton>(R.id.action_add_restriction)
        val actionRestrictionList = view.findViewById<ListView>(R.id.action_restriction_list)


        actionTimeList.adapter = ActionTimeAdapter(context, action!!)
        actionRestrictionList.adapter = RestrictionAdapter(context, action!!)

        if (action != null) {
            actionName.setText(action!!.name)
            actionLocation.setText(action!!.location)
            actionNote.setText(action!!.note)
            actionRemindSwitch.isChecked = action!!.isRemind
            actionDuration.setText(action!!.duration.toString())
        }

        actionAddTimeButton.setOnClickListener { v: View? ->
            action!!.addRestriction(
                FixedTimeRestriction(
                    LocalTime.now().withSecond(0).withNano(0),
                    LocalTime.now().plusHours(1).withSecond(0).withNano(0),
                    FixedTimeRestriction.FixedTimeRestrictionType.DAILY,
                    ArrayList()
                )
            )
            actionTimeList.adapter = ActionTimeAdapter(context, action!!)
        }

        //true->fixed time, false->repeating time
        actionTypeSwitch.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            val actionFixedTime = view.findViewById<View>(R.id.action_FixedTime)
            val actionRepeatingTime = view.findViewById<View>(R.id.action_RepeatingTime)
            if (isChecked) {
                actionFixedTime.visibility = View.VISIBLE
                actionRepeatingTime.visibility = View.VISIBLE
                actionType = "Fixed"
            } else {
                actionFixedTime.visibility = View.GONE
                actionRepeatingTime.visibility = View.VISIBLE
                actionType = "Repeating"
            }
        }
        actionTypeSwitch.isChecked = action!!.type == "Fixed"

        saveActionButton.setOnClickListener { v: View? ->
            val name = actionName.text.toString().trim { it <= ' ' }
            val location = actionLocation.text.toString().trim { it <= ' ' }
            val note = actionNote.text.toString().trim { it <= ' ' }
            val remind = actionRemindSwitch.isChecked
            val duration = Duration.parse(actionDuration.text.toString().trim { it <= ' ' })

            if (name.isEmpty() || location.isEmpty() || note.isEmpty()) {
                Toast.makeText(requireContext(), "填完再存呦~", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (actionType.isEmpty()) {
                Toast.makeText(requireContext(), "请选择频率", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            action!!.goal_id = goalId
            action!!.name = name
            action!!.location = location
            action!!.note = note
            action!!.isRemind = remind
            action!!.duration = duration

            saveActionToDatabase(action)
            dismiss()
        }

        actionAddRestrictionButton.setOnClickListener { v: View? ->
            val popupMenu = PopupMenu(context, v)
            val inflater1 = popupMenu.menuInflater
            inflater1.inflate(R.menu.restriction_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.menu_time_restriction ->                         // Handle Time Restriction
                        action!!.addRestriction(
                            TimeRestriction(
                                LocalDateTime.now(),
                                LocalDateTime.MAX
                            )
                        )

                    R.id.menu_amount_restriction ->                         // Handle Amount Restriction
                        action!!.addRestriction(AmountRestriction(30, 0, 0))

                    R.id.menu_interval_restriction ->                         // Handle Interval Restriction
                        action!!.addRestriction(IntervalRestriction(1, 3))

                    else -> return@setOnMenuItemClickListener false
                }
                actionRestrictionList.adapter = RestrictionAdapter(context, action!!)
                true
            }
            popupMenu.show()
        }

        return view
    }

    private fun saveActionToDatabase(action: Action?) {
        action!!.type = actionType
        val dao = dbHelper.getActionDao()
        val goalDao = dbHelper.getGoalDao()

        if (actionType == "Repeating"){
            action.restrictions = action.getRestrictionsWithout("FixedTimeRestriction")
        }

        if (action.goal_id == -1) {
            if (goalDao.getByContent("Default") == null) {
                goalDao.insert(Goal("Default", LocalDateTime.now(), LocalDateTime.MAX, "", 0))
            }
            action.goal_id = goalDao.getByContent("Default")!!.id
        }

        if (action.id == 0) {
            dao.insert(action)
        } else {
            dao.update(action)
        }
    }
}