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

public class MonthListAdapter extends BaseAdapter{



    /*private static final int TYPE_HOMEWORK = 0;
    private static final int TYPE_READING= 1;
    private static final int TYPE_FITTING= 2;
    private static final int TYPE_DDL= 3;*/



    private static final int TYPE_COUNT=1;
    private Context mContext;
    private List<Task> mData = null;


    public MonthListAdapter(Context mContext, List<Task> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //多布局的核心，通过这个判断类别
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    //类别数目
    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //TODO：Add the layout of the month list item
//        //int type = getItemViewType(position);
//        MySchedule ExampleSchedule=new MySchedule();
//        MyDeadLine ExampleDDL=new MyDeadLine();
//
//        ViewHolder holder = null;
//        BasicSchedule schedule = mData.get(position);
//
//        if(convertView == null){
//
//            holder = new ViewHolder();
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_month_list_item, parent, false);
//            holder.lesson_label = (ImageView) convertView.findViewById(R.id.lesson_label);
//            holder.lesson_text = (TextView) convertView.findViewById(R.id.lesson_text);
//            holder.lesson_room = (TextView) convertView.findViewById(R.id.lesson_room);
//            holder.lesson_teacher = (TextView) convertView.findViewById(R.id.lesson_teacher);
//            holder.lesson_time =(TextView) convertView.findViewById(R.id.lesson_time);
//            convertView.setTag(R.id.Tag_month_list,holder);
//            convertView.setTag(R.id.Tag_id,schedule.getId());
//
//
//
//        }else{
//
//            holder = (ViewHolder) convertView.getTag(R.id.Tag_month_list);
//        }
//
//        //设置下控件的值
//        holder.lesson_text.setText(schedule.getName());
//        holder.lesson_label.setBackgroundResource(R.drawable.blue_label);
//        holder.lesson_room.setText(schedule.getPlace());
//        holder.lesson_teacher.setText(schedule.getDescription());
//
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
//        Date date_start=new Date();
//        date_start.setTime(schedule.getStartingTime());
//        holder.lesson_time.setText(format.format(date_start));
//
//        if(schedule.getClass().isInstance(ExampleSchedule))
//        {
//            convertView.setTag(R.id.Tag_table_name,"SCHEDULE");
//            Date date_end=new Date();
//            date_end.setTime(((MySchedule)schedule).getEndingTime());
//            holder.lesson_time.setText(format.format(date_start)+"-"+format.format(date_end));
//            holder.lesson_text.setTextColor(mContext.getResources().getColor(R.color.blue_label_text));
//            holder.lesson_room.setBackgroundResource(R.drawable.blue_label_small);
//            holder.lesson_label.setBackgroundResource(R.drawable.blue_label);
//        }
//
//        if(schedule.getClass().isInstance(ExampleDDL))
//        {
//            convertView.setTag(R.id.Tag_table_name,"DDL");
//            holder.lesson_time.setText("DDL: "+format.format(date_start));
//            holder.lesson_room.setBackgroundResource(R.drawable.yellow_label_small);
//            holder.lesson_text.setTextColor(mContext.getResources().getColor(R.color.yellow_label_text));
//            holder.lesson_label.setBackgroundResource(R.drawable.yellow_label);
//        }
//
//
//        switch (schedule.getImportance()){
//            case 1:
//
//
//                break;
//            case 2:
//
//
//                break;
//            default:
//                break;
//        }
        return convertView;
    }


    //ViewHolder
    private static class ViewHolder{
        ImageView lesson_label;
        TextView lesson_text;
        TextView lesson_teacher;
        TextView lesson_room;
        TextView lesson_time;
    }

}
