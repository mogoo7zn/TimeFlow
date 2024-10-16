package cn.edu.ustc.ustcschedule.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;

import cn.edu.ustc.timeflow.bean.Task;
import cn.edu.ustc.timeflow.util.TimeTable;
import cn.edu.ustc.ustcschedule.dialog.DeleteDialog;
import cn.edu.ustc.ustcschedule.dialog.LessonDetailDialog;
import cn.edu.ustc.ustcschedule.util.Alpha;

public class DayListFragment extends Fragment {

    final SimpleDateFormat format_time = new SimpleDateFormat("HH:mm",Locale.CHINA);
    double magnify_ratio=2.63;//default
    LayoutInflater inflater;
    ViewGroup container;
    ConstraintLayout layout;
    TimeTable timeTable;
    LocalDate date;

    public DayListFragment(){
        date = LocalDate.now();
    }
    public DayListFragment(LocalDate date){
        this.date = date;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.inflater=inflater;
        this.container=container;

        View view= inflater.inflate(R.layout.fragment_day_list, container, false);
        layout= view.findViewById(R.id.day_list_layout);
        magnify_ratio=(float)(layout.getLayoutParams()).height/1226.0;

        timeTable = new TimeTable(getContext(), date);
        clean_schedule();
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

        // Calculate the height of the task CourseItem based on its duration
        double height = 1.01 * (Math.abs(ending_time - starting_time)) / 72000;

        // Calculate the start of the day in milliseconds, adjusted for GMT+8 timezone
        long day_start_temp = ((starting_time + 8 * 3600 * 1000) / (86400 * 1000)) * (86400 * 1000) - 8 * 3600 * 1000;

        // Calculate the position of the task CourseItem in the layout
        // 6.5 is an offset to account for the layout's top margin
        double pos = 1.01 * (Math.min(starting_time, ending_time) - day_start_temp) / 72000 + 6.5;

        CardView card= schedule_view.findViewById(R.id.lesson_card_day);

        // Set the long click listener for the card
        card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Show the lesson detail dialog
                        LessonDetailDialog dialog =new LessonDetailDialog(
                                getActivity(),
                                task.getContent(),
                                task.getNote(),task.getLocation(),
                                format_time.format(starting_time) + " - " + format_time.format(ending_time),
                                task.getFinished(),
                                task.getId(),
                                DayListFragment.this
                        );
                        dialog.show(getActivity().getSupportFragmentManager(), "lesson_detail");
                    }
                }
        );
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
                            deleteTask(event_id);
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
        ((TextView)card.findViewById(R.id.lesson_teacher)).setText(task.getNote());
        ((TextView)card.findViewById(R.id.lesson_place)).setText(task.getLocation());

        //TODO: 用这个标记吗？

        card.findViewById(R.id.is_finished).setBackgroundResource(task.getFinished()==null || task.getFinished() ? R.drawable.ic_flag_task_importance_low : R.drawable.ic_flag_task_importance_high);

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

    public void deleteTask(Integer taskId) {
        // Delete the task CourseItem from the database
        timeTable.deleteTask(taskId);
        clean_schedule();
        show_schedule();
    }


    public void changeTaskTime(Integer taskId, LocalTime localTime) {
        // Change the task CourseItem's time in the database
        timeTable.changeTaskTime(taskId, localTime);
        clean_schedule();
        show_schedule();
    }

    public void changeTaskFinished(Integer taskId, boolean checked) {
        // Change the task CourseItem's finished status in the database
        timeTable.changeTaskFinished(taskId, checked);
        clean_schedule();
        show_schedule();
    }
}
