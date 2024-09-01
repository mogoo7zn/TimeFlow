package cn.edu.ustc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.ustc.timeflow.bean.Milestone
import cn.edu.ustc.timeflow.util.DBHelper
import com.example.timeflow.R
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MilestoneAdapter(
    private val milestoneList: List<Milestone>,
    private val dbHelper: DBHelper
) : RecyclerView.Adapter<MilestoneAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val milestoneName: TextView = view.findViewById(R.id.milestone_name)
        val milestoneTime: TextView = view.findViewById(R.id.milestone_time)
        val timeLeft: TextView = view.findViewById(R.id.time_left)
        val milestoneFinished: CheckBox = view.findViewById(R.id.milestone_finished)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_deadline_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val milestone = milestoneList[position]
        holder.milestoneName.text = milestone.content
        holder.milestoneTime.text = milestone.time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

        val now = LocalDateTime.now()
        val duration = Duration.between(now, milestone.time)
        val daysLeft = duration.toDays()
        val hoursLeft = duration.toHours() % 24
        val minutesLeft = duration.toMinutes() % 60

        holder.timeLeft.text = "Time left: ${daysLeft}d ${hoursLeft}h ${minutesLeft}m"
        holder.milestoneFinished.isChecked = milestone.isFinished

        holder.milestoneFinished.setOnCheckedChangeListener { _, isChecked ->
            milestone.isFinished = isChecked
            dbHelper.getMilestoneDao().update(milestone)
        }
    }

    override fun getItemCount() = milestoneList.size
}