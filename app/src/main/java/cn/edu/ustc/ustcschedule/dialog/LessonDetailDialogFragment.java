package cn.edu.ustc.ustcschedule.dialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timeflow.R;


public class LessonDetailDialogFragment extends DialogFragment {

    private String lessonName;
    private String teacherName;
    private String room;
    private String time;
    private String description;
    private String notes;

    public static LessonDetailDialogFragment newInstance(String lessonName, String teacherName, String room, String time, String description, String notes) {
        LessonDetailDialogFragment fragment = new LessonDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("lessonName", lessonName);
        args.putString("teacherName", teacherName);
        args.putString("room", room);
        args.putString("time", time);
        args.putString("description", description);
        args.putString("notes", notes);
        fragment.setArguments(args);
        return fragment;
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


        if (getArguments() != null) {
            lessonName = getArguments().getString("lessonName");
            teacherName = getArguments().getString("teacherName");
            room = getArguments().getString("room");
            time = getArguments().getString("time");
            description = getArguments().getString("description");
            notes = getArguments().getString("notes");
        }

        TextView lessonNameTextView = view.findViewById(R.id.lesson_detail_name);
        TextView teacherNameTextView = view.findViewById(R.id.lesson_detail_teacher);
        TextView roomTextView = view.findViewById(R.id.lesson_detail_room);
        TextView timeTextView = view.findViewById(R.id.lesson_detail_time);
        TextView descriptionTextView = view.findViewById(R.id.lesson_detail_description);
        TextView notesTextView = view.findViewById(R.id.lesson_detail_notes);

        lessonNameTextView.setText("课程名称: " + lessonName);
        teacherNameTextView.setText("教师: " + teacherName);
        roomTextView.setText("教室: " + room);
        timeTextView.setText("时间: " + time);
        descriptionTextView.setText("描述: " + description);
        notesTextView.setText("备注: " + notes);

        return view;
    }
}
