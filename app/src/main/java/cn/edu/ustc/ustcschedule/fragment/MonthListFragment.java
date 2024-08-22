package cn.edu.ustc.ustcschedule.fragment;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.timeflow.R;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.util.TimeTable;
import cn.edu.ustc.ustcschedule.adapter.TaskAdapter;

public class MonthListFragment extends Fragment {

    Calendar ca=Calendar.getInstance(Locale.CHINA);
    CalendarView calendarView;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ca.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        view=inflater.inflate(R.layout.fragment_month_list, container, false);


        calendarView = ((View)container.getParent()).findViewById(R.id.calendar);

        updateList();
        return view;
    }

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

    public void updateList() {
        long dateInMillis = calendarView.getDate();

        LocalDate localDate = Instant.ofEpochMilli(dateInMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        TimeTable timeTable=new TimeTable(getContext(),localDate);


        RecyclerView layout = (RecyclerView) view.findViewById(R.id.month_ListView);
        layout.setLayoutManager(new LinearLayoutManager(getContext()));
        layout.setAdapter(new TaskAdapter(timeTable.getTasks()));
    }
}