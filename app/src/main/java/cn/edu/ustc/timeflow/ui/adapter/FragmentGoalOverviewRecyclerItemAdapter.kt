package cn.edu.ustc.timeflow.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ustc.timeflow.bean.Action
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = actionList[position]
        holder.bind(action)
    }

    override fun getItemCount(): Int {
        return actionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val actionName: TextView = itemView.findViewById(R.id.action_name)

        fun bind(action: Action) {
            actionName.text = action.name
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