package cn.edu.ustc.ustcschedule.dialog;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.timeflow.R;
import com.loper7.date_time_picker.DateTimeConfig;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import cn.edu.ustc.ustcschedule.fragment.DayListFragment;


public class LessonDetailDialog extends DialogFragment {

    private String lessonName;
    private String teacherName;
    private String room;
    private String time;
    private Boolean is_finished;
    private Integer task_id;
    private Fragment fragment;
    private Activity context;



    public LessonDetailDialog(Activity context, String lessonName, String teacherName, String room, String time, Boolean is_finished, Integer task_id, Fragment fragment) {
        this.context = context;
        this.lessonName = lessonName;
        this.teacherName = teacherName;
        this.room = room;
        this.time = time;
        this.is_finished = is_finished;
        this.task_id = task_id;
        this.fragment = fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the dialog window attributes here
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_lesson_detail, container, false);

        TextView lessonNameTextView = view.findViewById(R.id.lesson_detail_name);
        TextView teacherNameTextView = view.findViewById(R.id.lesson_detail_teacher);
        TextView roomTextView = view.findViewById(R.id.lesson_detail_room);
        TextView timeTextView = view.findViewById(R.id.lesson_detail_time);
        CheckBox checkBox = view.findViewById(R.id.lesson_detail_finished);
        Button changeButton = view.findViewById(R.id.lesson_detail_change_time);
        Button deleteButton = view.findViewById(R.id.lesson_detail_delete);
        ImageButton tomatoButton = view.findViewById(R.id.tomato_button);


        lessonNameTextView.setText("任务内容: " + lessonName);
        teacherNameTextView.setText("说明: " + teacherName);
        roomTextView.setText("位置: " + room);
        timeTextView.setText("时间: " + time);
        checkBox.setChecked(is_finished);


        checkBox.setOnClickListener(v -> {
            // 传递信息到DayListFragment
                if (fragment instanceof DayListFragment) {
                    ((DayListFragment) fragment).changeTaskFinished(task_id, checkBox.isChecked());
                }


        });

        changeButton.setOnClickListener(v -> {
            new CardDatePickerDialog.Builder(getContext())
                    .setTitle("选择开始时间")
                    .setDisplayType(DateTimeConfig.HOUR, DateTimeConfig.MIN)
                    .setOnChoose(
                            "确定",

                            time_millisecond -> {
                                // 传递信息到DayListFragment

                                LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(time_millisecond / 1000, 0, ZoneOffset.of("+8"));

                                if (fragment instanceof DayListFragment) {
                                    ((DayListFragment) fragment).changeTaskTime(task_id, localDateTime.toLocalTime());
                                }

                                dismiss();
                                return null;
                            }
                    )
                    .build()
                    .show();
        });

        deleteButton.setOnClickListener(v -> {
            if (fragment instanceof DayListFragment) {
                ((DayListFragment) fragment).deleteTask(task_id);
            }
            dismiss();
        });

        tomatoButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("task_id", task_id);
            // 跳转到番茄钟
            NavController findNavController = Navigation.findNavController(context, R.id.nav_host_fragment_content_main);
            findNavController.navigate(R.id.fragment_tomato, bundle);
            dismiss();
        });
        return view;
    }
}
