package cn.edu.ustc.timeflow.ui.adapter

import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import com.example.timeflow.R
import java.time.LocalTime
import java.util.*

class ActionTimeAdapter(context: Context,val action: Action) :
    ArrayAdapter<FixedTimeRestriction>(context, 0, action.getRestrictions("FixedTimeRestriction") as List<FixedTimeRestriction>)
{
           
    private class ViewHolder(view: View) {
        val startTime: TextView = view.findViewById(R.id.start_time)
        val endTime: TextView = view.findViewById(R.id.end_time)
        val repeatType: TextView = view.findViewById(R.id.repeat_type)
        val repeatParams: EditText = view.findViewById(R.id.repeat_params)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.action_time_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val currentItem = getItem(position)!!
        viewHolder.startTime.text = currentItem.start.toString()
        viewHolder.startTime.setOnClickListener {
            showTimePickerDialog(context, viewHolder.startTime) { selectedTime ->
                currentItem.start = selectedTime
            }
        }

        viewHolder.endTime.text = currentItem.end.toString()
        viewHolder.endTime.setOnClickListener {
            showTimePickerDialog(context, viewHolder.endTime) { selectedTime ->
                currentItem.end = selectedTime
            }
        }
        viewHolder.repeatType.text = when (currentItem.type) {
            0 -> "每天"
            1 -> "每周"
            2 -> "每月"
            3 -> "每年"
            else -> "未知"
        }

        viewHolder.repeatType.setOnClickListener {
            showOptionMenu(context, it, viewHolder.repeatType) { selectedType ->
                currentItem.type = selectedType
                viewHolder.repeatType.text = when (selectedType) {
                    0 -> "每天"
                    1 -> "每周"
                    2 -> "每月"
                    3 -> "每年"
                    -1 -> "不重复"
                    else -> "未知"
                }
            }
        }
        viewHolder.repeatParams.text = Editable.Factory.getInstance().newEditable(currentItem.days.joinToString(separator = ", "))
       viewHolder.repeatParams.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val daysText = viewHolder.repeatParams.text.toString()
                val days = daysText.split(",").mapNotNull {
                    it.trim().toIntOrNull()
                }
                if (days.size == daysText.split(",").size) {
                    currentItem.days = days
                } else {
                    // Handle invalid input, e.g., show an error message
                    viewHolder.repeatParams.error = "Invalid input. Please enter comma-separated integers."
                }

            }
        }
        view.setOnLongClickListener {
            action.removeRestriction(currentItem)
            remove(currentItem)
            notifyDataSetChanged()
            true
        }
        return view
    }

    private fun showTimePickerDialog(context: Context, textView: TextView, onTimeSelected: (LocalTime) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context,android.R.style.Theme_Holo_Dialog, { _, selectedHour, selectedMinute ->
            val selectedTime = LocalTime.of(selectedHour, selectedMinute)
            textView.text = selectedTime.toString()
            onTimeSelected(selectedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun showOptionMenu(context: Context, anchor: View, textView: TextView, onOptionSelected: (Int) -> Unit) {
        val popupMenu = PopupMenu(context, anchor)
        popupMenu.menuInflater.inflate(R.menu.repeat_type_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val selectedType = when (menuItem.itemId) {
                R.id.daily -> 0
                R.id.weekly -> 1
                R.id.monthly -> 2
                R.id.yearly -> 3
                R.id.none -> -1
                else -> 408
            }
            onOptionSelected(selectedType)
            true
        }
        popupMenu.show()
    }
}