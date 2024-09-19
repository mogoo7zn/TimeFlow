package cn.edu.ustc.timeflow.ui.adapter

import android.content.Context
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.restriction.AmountRestriction
import cn.edu.ustc.timeflow.restriction.IntervalRestriction
import cn.edu.ustc.timeflow.restriction.Restriction
import cn.edu.ustc.timeflow.restriction.TimeRestriction
import com.example.timeflow.R
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import java.time.LocalDateTime


class RestrictionAdapter(context: Context, action: Action) :
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
                viewHolder.total?.text = Editable.Factory.getInstance().newEditable(restriction.total.toString())
                viewHolder.total?.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        restriction.total = viewHolder.total.text.toString().toInt()
                    }
                })

                viewHolder.todoLabel?.text = context.getString(R.string.todo)+ " "+restriction.todo.toString()
                viewHolder.doneLabel?.text = context.getString(R.string.finished)+ " "+restriction.finished.toString()
            }
            is IntervalRestriction -> {
                viewHolder.intervalValue?.text = Editable.Factory.getInstance().newEditable(restriction.interval.toString())
                viewHolder.intervalValue?.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        restriction.interval = viewHolder.intervalValue.text.toString().toInt()
                    }
                })
                viewHolder.repeatValue?.text = Editable.Factory.getInstance().newEditable(restriction.repeat_times.toString())
                viewHolder.repeatValue?.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        restriction.repeat_times = viewHolder.repeatValue.text.toString().toInt()
                    }
                })
            }
        }

        return view
    }

    private class ViewHolder(view: View, restriction: Restriction) {
        //TimeRestriction
        val startTime: TextView? = if (restriction is TimeRestriction) view.findViewById(R.id.start_time) else null
        val endTime: TextView? = if (restriction is TimeRestriction) view.findViewById(R.id.end_time) else null
        //AmountRestriction
        val total: EditText? = if (restriction is AmountRestriction) view.findViewById(R.id.total) else null
        val todoLabel: TextView? = if (restriction is AmountRestriction) view.findViewById(R.id.todo_label) else null
        val doneLabel: TextView? = if (restriction is AmountRestriction) view.findViewById(R.id.finished_label) else null
        //IntervalRestriction
        val intervalValue: EditText? = if (restriction is IntervalRestriction) view.findViewById(R.id.interval_value) else null
        val repeatValue: EditText? = if (restriction is IntervalRestriction) view.findViewById(R.id.repeat_times_value) else null
    }

    private fun showDateTimePicker(context: Context ,textView: TextView,onChozen: (LocalDateTime) -> Unit) {
        CardDatePickerDialog.builder(context)
            .setTitle("SET MAX DATE")
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