package cn.edu.ustc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.edu.ustc.timeflow.bean.Task
import cn.edu.ustc.timeflow.database.TaskDB
import com.example.timeflow.R
import cn.edu.ustc.ui.home.HomeFragment
import java.text.ParseException

class HomeListAdapter : BaseAdapter() {
    private val context: Context? = null
    private val tasks : List<Task> = ArrayList<Task>()

//    val task_DB = TaskDB(context)

    override fun getCount(): Int {
        return 0
    }

    override fun getItem(position: Int): Any {
        return Any()
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder : ViewHolder = ViewHolder()
        if (convertView == null) {
            val view = LayoutInflater.from(context).inflate(0, parent, false)
                holder.checkbox = view.findViewById(R.id.checkbox)
                holder.name = view.findViewById(R.id.name)
                holder.place = view.findViewById(R.id.place)
                holder.startTime = view.findViewById(R.id.start_time)
                holder.endTime = view.findViewById(R.id.end_time)
                holder.tomato = view.findViewById(R.id.tomato)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        var task = tasks[position]
        if (task.finished.equals(true)) {
//            holder.checkbox.isChecked = true
        } else {
//            holder.checkbox.isChecked = false
        }

//      //initialize linking database
        val task_db = context?.let { TaskDB.getDatabase(it) }
        val task_dao = task_db?.taskDao()

        //set the checkbox state of the task
        holder.checkbox?.setOnClickListener { view ->
//            var taskDAO: TaskDao? = TaskDao(context)
            if ((view as CheckBox).isChecked) {
//                task_dao?.finishTask(tasks[position].id)
            } else {
                //task_DB.unfinishTask(tasks[position].id)
            }
        }

        holder.tomato?.setOnClickListener(View.OnClickListener {
//            val intent = Intent(context, TomatoActivity::class.java)
        })

        return convertView!!
    }

    class ViewHolder {
        var checkbox: View? = null
        var name: View? = null
        var place: View? = null
        var startTime: View? = null
        var endTime: View? = null
        var tomato: View? = null
    }




}