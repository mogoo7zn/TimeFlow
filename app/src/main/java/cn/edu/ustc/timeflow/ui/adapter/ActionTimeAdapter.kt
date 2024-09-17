package cn.edu.ustc.timeflow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction
import cn.edu.ustc.timeflow.restriction.TimeRestriction
import com.example.timeflow.R

data class ActionTimeItem(
    val startTime: String,
    val endTime: String,
    val repeatType: String,
    val repeatParams: String
)

class ActionTimeAdapter(context: Context, action: Action) :
    ArrayAdapter<ActionTimeItem>(context, 0, action.toActionTimeItems()) {

    private class ViewHolder(view: View) {
        val startTime: TextView = view.findViewById(R.id.start_time)
        val endTime: TextView = view.findViewById(R.id.end_time)
        val repeatType: TextView = view.findViewById(R.id.repeat_type)
        val repeatParams: TextView = view.findViewById(R.id.repeat_params)
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
        viewHolder.startTime.text = currentItem.startTime
        viewHolder.endTime.text = currentItem.endTime
        viewHolder.repeatType.text = currentItem.repeatType
        viewHolder.repeatParams.text = currentItem.repeatParams

        return view
    }
}
fun Action.toActionTimeItems(): List<ActionTimeItem> {
    val items = mutableListOf<ActionTimeItem>()
    val fixedTimeRestrictions = this.getRestrictions("FixedTimeRestriction")
    for (restriction in fixedTimeRestrictions) {
        val fixedTimeRestriction = restriction as FixedTimeRestriction
        /**
         *     LocalTime start;
         *     LocalTime end;
         *     /**
         *      * 0:daily,1:weekly,2:monthly,3:yearly
         *      */
         *     int type;
         *     /**
         *      * 1:Monday,2:Tuesday,3:Wednesday,4:Thursday,5:Friday,6:Saturday,7:Sunday
         *      */
         *     List<Integer> days;
         */
        items.add(
            ActionTimeItem(
                startTime = fixedTimeRestriction.start.toString(),
                endTime = fixedTimeRestriction.end.toString(),
                repeatType = when (fixedTimeRestriction.type) {
                    0 -> "每天"
                    1 -> "每周"
                    2 -> "每月"
                    3 -> "每年"
                    else -> "未知"
                },
                repeatParams = fixedTimeRestriction.days.joinToString(separator = ", ")
            )
        )
    }
    return items
}
