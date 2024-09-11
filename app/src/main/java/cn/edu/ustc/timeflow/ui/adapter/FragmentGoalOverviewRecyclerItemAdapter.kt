package cn.edu.ustc.timeflow.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ustc.timeflow.bean.Action
import cn.edu.ustc.timeflow.restriction.TimeRestriction
import com.example.timeflow.R

class FragmentGoalOverviewRecyclerItemAdapter(
    private val context: Context,
    var actionList: List<Action>
) : RecyclerView.Adapter<FragmentGoalOverviewRecyclerItemAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(action: Action)
        fun onItemLongClick(action: Action)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.fragment_goal_overview_recycler_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = actionList[position]
        holder.bind(action)
    }

    override fun getItemCount(): Int {
        return actionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionName: TextView = itemView.findViewById(R.id.action_name)
        private val actionDuration: TextView = itemView.findViewById(R.id.action_time)
        private val actionDescription: TextView = itemView.findViewById(R.id.action_description)
        private val actionPlace: TextView = itemView.findViewById(R.id.action_place)


        @RequiresApi(Build.VERSION_CODES.S)
        fun bind(action: Action) {
            actionName.text = action.name
            var timeString =""
            if (action.duration.toHours()>0){
                timeString += action.duration.toHours().toString() + context.getString(R.string.hour)
            }
            if (action.duration.toMinutesPart()>0){
                timeString += action.duration.toMinutesPart().toString() + context.getString(R.string.min)
            }
            actionDuration.text = timeString

            // 如果note太长，只显示前20个字符，后面用省略号代替
            if (action.note.length > 50) {
                actionDescription.text = action.note.substring(0, 50) + "..."
            } else {
                actionDescription.text = action.note
            }

            actionPlace.text = action.location

            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(action)
            }
            itemView.setOnLongClickListener {
                onItemClickListener?.onItemLongClick(action)
                true
            }
        }
    }
}