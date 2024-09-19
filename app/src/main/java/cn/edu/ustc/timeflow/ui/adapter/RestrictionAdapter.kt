package cn.edu.ustc.timeflow.ui.adapter

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.restriction.AmountRestriction
import cn.edu.ustc.timeflow.restriction.IntervalRestriction
import cn.edu.ustc.timeflow.restriction.Restriction
import cn.edu.ustc.timeflow.restriction.TimeRestriction
import com.example.timeflow.R
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import java.time.LocalDateTime


class RestrictionAdapter(context: Context,val action: Action) :
    ArrayAdapter<Restriction>(context, 0, action.getRestrictionsWithout("FixedTimeRestriction") as List<Restriction>) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val restriction = getItem(position)
        val view: View
        val viewHolder: ViewHolder


        view = when (restriction) {
                is TimeRestriction -> {
                    LayoutInflater.from(context).inflate(R.layout.time_restriction_item, parent, false)
                }
                is AmountRestriction -> {
                    LayoutInflater.from(context).inflate(R.layout.amount_restriction_item, parent, false)
                }
                is IntervalRestriction -> {
                    LayoutInflater.from(context).inflate(R.layout.interval_restriction_item, parent, false)
                }
                else -> {
                    throw IllegalArgumentException("Unknown restriction type")
                }
            }
        viewHolder = ViewHolder(view, restriction)


        when (restriction) {
            is TimeRestriction -> {
                viewHolder.startTime?.text = restriction.start.toString()
                viewHolder.endTime?.text = restriction.end.toString()
                viewHolder.startTime?.setOnClickListener {
                    showDateTimePicker(context, viewHolder.startTime) { selectedTime ->
                        restriction.start = selectedTime
                    }
                }
                viewHolder.endTime?.setOnClickListener {
                    showDateTimePicker(context, viewHolder.endTime) { selectedTime ->
                        restriction.end = selectedTime
                    }
                }

            }
            is AmountRestriction -> {

                viewHolder.total?.minValue = 1
                viewHolder.total?.maxValue = 20
                viewHolder.total?.value = restriction.total
                viewHolder.total?.setOnValueChangedListener { picker, oldVal, newVal ->
                    restriction.total = newVal
                    restriction.todo =  restriction.total - restriction.finished
                    viewHolder.todoLabel?.text = context.getString(R.string.todo)+ " "+restriction.todo.toString()
                }

                viewHolder.todoLabel?.text = context.getString(R.string.todo)+ " "+restriction.todo.toString()
                viewHolder.doneLabel?.text = context.getString(R.string.finished)+ " "+restriction.finished.toString()
            }
            is IntervalRestriction -> {
                viewHolder.intervalValue?.minValue = 1
                viewHolder.intervalValue?.maxValue = 10
                viewHolder.intervalValue?.value = restriction.interval
                viewHolder.intervalValue?.setOnValueChangedListener { picker, oldVal, newVal ->
                    restriction.interval = newVal
                }

                viewHolder.repeatValue?.minValue = 1
                viewHolder.repeatValue?.maxValue = 10
                viewHolder.repeatValue?.value = restriction.repeat_times
                viewHolder.repeatValue?.setOnValueChangedListener { picker, oldVal, newVal ->
                    restriction.repeat_times = newVal
                }


            }
        }

        view.setOnLongClickListener {
            action.removeRestriction(restriction)
            remove(restriction)
            notifyDataSetChanged()
            true
        }

        return view
    }

    private class ViewHolder(view: View, restriction: Restriction) {
        //TimeRestriction
        val startTime: TextView? = if (restriction is TimeRestriction) view.findViewById(R.id.start_time) else null
        val endTime: TextView? = if (restriction is TimeRestriction) view.findViewById(R.id.end_time) else null
        //AmountRestriction
        val total: NumberPicker? = if (restriction is AmountRestriction) view.findViewById(R.id.total) else null
        val todoLabel: TextView? = if (restriction is AmountRestriction) view.findViewById(R.id.todo_label) else null
        val doneLabel: TextView? = if (restriction is AmountRestriction) view.findViewById(R.id.finished_label) else null
        //IntervalRestriction
        val intervalValue: NumberPicker? = if (restriction is IntervalRestriction) view.findViewById(R.id.interval_value) else null
        val repeatValue: NumberPicker? = if (restriction is IntervalRestriction) view.findViewById(R.id.repeat_times_value) else null
    }

    private fun showDateTimePicker(context: Context ,textView: TextView,onChozen: (LocalDateTime) -> Unit) {
        CardDatePickerDialog.builder(context)
            .setTitle(context.getString(R.string.set_time))
            .setOnChoose {millisecond->
                run {
                    val date =
                        LocalDateTime.ofEpochSecond(millisecond / 1000, 0, java.time.ZoneOffset.ofHours(8))
                    textView.text = date.toString()
                    onChozen(date)
                }
            }.build().show()

    }

}