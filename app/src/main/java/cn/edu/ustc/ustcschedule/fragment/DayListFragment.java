package cn.edu.ustc.ustcschedule.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import com.example.timeflow.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.dao.TaskDao;
import cn.edu.ustc.timeflow.util.DBHelper;
import cn.edu.ustc.timeflow.util.TimeTable;
import cn.edu.ustc.ustcschedule.dialog.DeleteDialog;
import cn.edu.ustc.ustcschedule.util.Alpha;

public class DayListFragment extends Fragment {

    final SimpleDateFormat format_day = new SimpleDateFormat("yyyy/MM/dd",Locale.CHINA);
    final SimpleDateFormat format_time = new SimpleDateFormat("HH:mm",Locale.CHINA);
    Date date=new Date();

    Calendar ca=Calendar.getInstance(Locale.CHINA);
    long day_start=((date.getTime()+8*3600*1000)/(86400*1000))*(86400*1000)-8*3600*1000;//清除小时和分钟
    long day_end=day_start+86400*1000;
    double magnify_ratio=2.63;//default


    LayoutInflater inflater;
    ViewGroup container;
    ConstraintLayout layout;
    TimeTable timeTable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.inflater=inflater;
        this.container=container;

        ca.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        View view= inflater.inflate(R.layout.fragment_day_list, container, false);
        layout= view.findViewById(R.id.day_list_layout);
        magnify_ratio=(float)(layout.getLayoutParams()).height/1226.0;

        //TODO: For Testing, remove later
        // Get the tasks for the current day
        DBHelper dbHelper = new DBHelper(getContext());
        dbHelper.generateTestTaskData();
        timeTable = new TimeTable(getContext(), LocalDate.now());
        show_schedule();
        return view;
    }

    public void clean_schedule()
    {
        // Remove all schedule items from the layout
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child.getId() == -1) {
                layout.removeView(child);
                i--;
            }
        }
    }

    public void show_schedule()
    {
        // Get the tasks for the current day
        List<Task> tasks = timeTable.getTasks();

        for (Task task : tasks) {
            add_schedule(layout, task, inflater, container);
        }
    }

    public void add_schedule(ConstraintLayout layout, Task task, LayoutInflater inflater, ViewGroup container)
    {
        View schedule_view=inflater.inflate(R.layout.fragment_day_list_item, container, false);
        // 计算相关参数
       // Convert start and end times to milliseconds, adjusted for GMT+8 timezone
        long starting_time = task.getStart().toEpochSecond(ZoneOffset.of("+8")) * 1000;
        long ending_time = task.getEnd().toEpochSecond(ZoneOffset.of("+8")) * 1000;

        // Calculate the height of the task item based on its duration
        double height = 1.01 * (Math.abs(ending_time - starting_time)) / 72000;

        // Calculate the start of the day in milliseconds, adjusted for GMT+8 timezone
        long day_start_temp = ((starting_time + 8 * 3600 * 1000) / (86400 * 1000)) * (86400 * 1000) - 8 * 3600 * 1000;

        // Calculate the position of the task item in the layout
        // 6.5 is an offset to account for the layout's top margin
        double pos = 1.01 * (Math.min(starting_time, ending_time) - day_start_temp) / 72000 + 6.5;

        CardView card= schedule_view.findViewById(R.id.lesson_card_day);

        // Set the long click listener for the card
        card.setOnLongClickListener(new View.OnLongClickListener()
            {
                final int event_id= task.getId();
                final String table_name="SCHEDULE";

                @Override
                public boolean onLongClick(View v) {
                    DeleteDialog deleteDialog = new DeleteDialog();
                    deleteDialog.setEvent_id(event_id);
                    deleteDialog.setTable_name(table_name);
                    deleteDialog.show(getActivity().getSupportFragmentManager(), "delete");
                    deleteDialog.setListener(new DeleteDialog.DeleteDialogListener() {
                        @Override
                        public void onDialogPositiveClick(DialogFragment dialog) {
                            // Delete the task item from the database
                            timeTable.deleteTask(event_id);
                            clean_schedule();
                            show_schedule();
                        }

                        @Override
                        public void onDialogNegativeClick(DialogFragment dialog) {

                        }
                    });
                return false;
            }

        });

        // Set the height, top margin, and width of the card
        ConstraintLayout.LayoutParams card_params = (ConstraintLayout.LayoutParams) card.getLayoutParams();
        card_params.height=(int)(magnify_ratio*height);//放大倍数乘值
        card_params.topMargin=(int)(magnify_ratio*pos);


        int margin = 0;

        switch (task.getOverlap()) {
            case 0:
                card.setAlpha(Alpha.ALPHA_FULL);
                break;
            case 1:
                card.setAlpha(Alpha.ALPHA_HIGH);
                margin = 50;
                break;
            case 2:
                card.setAlpha(Alpha.ALPHA_MEDIUM);
                margin = 100;
                break;
            case 3:
                card.setAlpha(Alpha.ALPHA_LOW);
                margin = 30;
                break;
            case 4:
                card.setAlpha(Alpha.ALPHA_VERY_LOW);
                margin = 80;
                break;
            default:
                card.setAlpha(Alpha.ALPHA_MINIMUM);
                margin = 110;
                break;
        }



        card_params.setMarginStart((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                margin,
                getResources().getDisplayMetrics()
        ));

        card_params.setMarginEnd((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (int)(margin * 0.3),
                getResources().getDisplayMetrics()
        ));


        // 写入内容
        ((TextView)card.findViewById(R.id.lesson_text_day)).setText(task.getContent());
//        ((TextView)card.findViewById(R.id.lesson_teacher)).setText(task.getTeacher());
//        ((TextView)card.findViewById(R.id.lesson_place)).setText(task.getPlace());

        ((TextView)schedule_view.findViewById(R.id.start_time_text)).setText(format_time.format(starting_time));
        ((TextView)schedule_view.findViewById(R.id.end_time_text)).setText(format_time.format(ending_time));

        switch(task.getImportance())
        {
            case 1:
            case 2:
                schedule_view.findViewById(R.id.lesson_label_day).setBackgroundResource(R.drawable.green_label);
                ((TextView)schedule_view.findViewById(R.id.lesson_text_day)).setTextColor(ContextCompat.getColor(getContext(), R.color.green_label_text));
                schedule_view.findViewById(R.id.lesson_place).setBackgroundResource(R.drawable.green_label_small);

                break;

            case 3:
                schedule_view.findViewById(R.id.lesson_label_day).setBackgroundResource(R.drawable.yellow_label);
                ((TextView)schedule_view.findViewById(R.id.lesson_text_day)).setTextColor(ContextCompat.getColor(getContext(), R.color.yellow_label_text));
                schedule_view.findViewById(R.id.lesson_place).setBackgroundResource(R.drawable.yellow_label_small);
                break;
        }

        card.setLayoutParams(card_params);


        schedule_view.setTag(R.id.Tag_id,-1);


        layout.addView(schedule_view);

    }


//    public void add_DDL(ConstraintLayout layout,MyDeadLine ddl,LayoutInflater inflater, ViewGroup container) {
//        View ddl_view=inflater.inflate(R.layout.fragment_day_list_item_ddl, container, false);
//
//        long starting_time=ddl.getStartingTime();
//        long day_start_temp=((starting_time+8*3600*1000)/(86400*1000))*(86400*1000)-8*3600*1000;
//        double pos=1.01*(starting_time-day_start_temp)/72000+6.5;//6是line到layout顶部的高度
//
//
//        ConstraintLayout.LayoutParams ddl_params = (ConstraintLayout.LayoutParams) ddl_view.findViewById(R.id.day_deadline).getLayoutParams();
//        //FrameLayout.LayoutParams ddl_params = (FrameLayout.LayoutParams) ddl_view.getLayoutParams();
//        TextView ddl_text=((TextView)ddl_view.findViewById(R.id.day_deadline_label));
//        ddl_text.setText(format_time.format(starting_time));
//        ddl_text.setOnLongClickListener(new View.OnLongClickListener()
//        {
//            final int event_id= ddl.getId();
//            final String table_name="DDL";
//
//            @Override
//            public boolean onLongClick(View v) {
//                DeleteDialog deleteDialog = new DeleteDialog();
//                deleteDialog.setEvent_id(event_id);
//                deleteDialog.setTable_name(table_name);
//                deleteDialog.show(getActivity().getSupportFragmentManager(), "delete");
//
//
//                return false;
//            }
//        });
//
//        ddl_params.topMargin=(int)(magnify_ratio*pos);
//        ddl_view.findViewById(R.id.day_deadline).setLayoutParams(ddl_params);
//        //ddl_view.layout(ddl_view.getLeft(),100,ddl_view.getRight(),170);
//        layout.addView(ddl_view);
//    }
//    public boolean is_today_fun(BasicSchedule schedule)
//    {
//        boolean is_today=false;
//        long starting_time=schedule.getStartingTime();
//        Calendar temp_ca=Calendar.getInstance(Locale.CHINA);
//        temp_ca.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//        temp_ca.setTimeInMillis(starting_time);
//        if(schedule.getPeriod()==1)
//            is_today=true;
//        if(schedule.getPeriod()==7&&(ca.get(Calendar.DAY_OF_WEEK) ==temp_ca.get(Calendar.DAY_OF_WEEK)))
//            is_today=true;
//        if(schedule.getPeriod()==30)
//        {
//            if(ca.get(Calendar.DAY_OF_MONTH) ==temp_ca.get(Calendar.DAY_OF_MONTH))
//                is_today = true;
//            if(ca.getActualMaximum(Calendar.DAY_OF_MONTH) <temp_ca.get(Calendar.DAY_OF_MONTH))//超过一个月最大天数
//            {
//                if(ca.getActualMaximum(Calendar.DAY_OF_MONTH)==ca.get(Calendar.DAY_OF_MONTH))
//                    is_today=true;
//            }
//        }
//        if(schedule.getPeriod()==365)
//        {
//            if((ca.get(Calendar.DAY_OF_MONTH) ==temp_ca.get(Calendar.DAY_OF_MONTH))&&
//                    (ca.get(Calendar.MONTH)==temp_ca.get(Calendar.MONTH)))
//            {
//                is_today=true;
//            }
//            if(ca.getActualMaximum(Calendar.DAY_OF_MONTH) <temp_ca.get(Calendar.DAY_OF_MONTH)&&
//                    (ca.get(Calendar.MONTH)==temp_ca.get(Calendar.MONTH)))//2月29日
//            {
//                is_today=true;
//            }
//        }
//        return is_today;
//    }
}
