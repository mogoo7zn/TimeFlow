package cn.edu.ustc.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ustc.timeflow.bean.Goal
import com.example.timeflow.R

class GoalPickerMenuAdapter(
    private val context: Context,
    private val goalList: List<Goal>,
    private val onGoalSelected: (Goal) -> Unit
) : RecyclerView.Adapter<GoalPickerMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.goal_picker_menu_item,
            parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = goalList[position]
        holder.goalName.text = goal.content
        holder.itemView.setOnClickListener {
            onGoalSelected(goal)
        }
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalName: TextView = itemView.findViewById(R.id.goal_name)
    }
}