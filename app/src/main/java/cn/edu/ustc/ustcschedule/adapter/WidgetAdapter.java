package cn.edu.ustc.ustcschedule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.timeflow.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.edu.ustc.timeflow.bean.Task;

public class WidgetAdapter extends BaseAdapter {

    private final List<Task> taskList;
    private final LayoutInflater inflater;

    public WidgetAdapter(Context context, List<Task> taskList) {
        this.taskList = taskList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_month_list_item, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.lesson_text);
            holder.teacher = convertView.findViewById(R.id.lesson_teacher);
            holder.room = convertView.findViewById(R.id.lesson_room);
            holder.time = convertView.findViewById(R.id.lesson_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = taskList.get(position);
        holder.title.setText(task.getContent());
        holder.teacher.setText(task.getEvaluation());
        holder.room.setText(String.valueOf(task.getImportance()));
        holder.time.setText(task.getStart().toString() + " - " + task.getEnd().toString());

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView teacher;
        TextView room;
        TextView time;
    }
}
