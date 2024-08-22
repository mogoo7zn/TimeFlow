package cn.edu.ustc.ustcschedule.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.edu.ustc.timeflow.bean.Task;
import com.example.timeflow.R;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_month_list_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.title.setText(task.getContent());
        holder.teacher.setText(task.getEvaluation());
        holder.room.setText(String.valueOf(task.getImportance()));
        holder.time.setText(task.getStart().toString() + " - " + task.getEnd().toString());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView teacher;
        TextView room;
        TextView time;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.lesson_text);
            teacher = itemView.findViewById(R.id.lesson_teacher);
            room = itemView.findViewById(R.id.lesson_room);
            time = itemView.findViewById(R.id.lesson_time);
        }
    }
}
