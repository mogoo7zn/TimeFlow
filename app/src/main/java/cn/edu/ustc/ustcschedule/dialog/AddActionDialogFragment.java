package cn.edu.ustc.ustcschedule.dialog;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.timeflow.R;
import com.loper7.date_time_picker.DateTimePicker;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.dao.ActionDao;
import cn.edu.ustc.timeflow.util.AlarmReceiver;
import cn.edu.ustc.timeflow.util.DBHelper;

public class AddActionDialogFragment extends DialogFragment {

    private long startTime = 0;
    private long endTime = 0;
    private DBHelper dbHelper;
    private Duration duration = Duration.ZERO;
    private Action action;

    public static AddActionDialogFragment newInstance(Action action) {
        AddActionDialogFragment fragment = new AddActionDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("action", (Serializable) action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_action, container, false);

        dbHelper = new DBHelper(requireContext());

        EditText actionName = view.findViewById(R.id.action_name);
        EditText actionLocation = view.findViewById(R.id.action_location);
        EditText actionNote = view.findViewById(R.id.action_note);
        CheckBox actionRemind = view.findViewById(R.id.action_remind);
        CheckBox actionStartTime = view.findViewById(R.id.action_start_time);
        CheckBox actionEndTime = view.findViewById(R.id.action_end_time);
        DateTimePicker actionTimePicker = view.findViewById(R.id.action_time_picker);
        Button saveActionButton = view.findViewById(R.id.save_action_button);

        actionStartTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                actionTimePicker.setVisibility(View.VISIBLE);
                actionTimePicker.setOnDateTimeChangedListener((millis) -> {
                    startTime = millis;
                    return null;
                });
            } else {
                actionTimePicker.setVisibility(View.GONE);
            }
        });

        actionEndTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                actionTimePicker.setVisibility(View.VISIBLE);
                actionTimePicker.setOnDateTimeChangedListener((millis) -> {
                    endTime = millis;
                    return null;
                });
            } else {
                actionTimePicker.setVisibility(View.GONE);
            }
        });

        saveActionButton.setOnClickListener(v -> {
            String name = actionName.getText().toString();
            duration = Duration.ofMillis(endTime - startTime);
            String location = actionLocation.getText().toString();
            String note = actionNote.getText().toString();
            boolean remind = actionRemind.isChecked();

            Action newAction = new Action(
                    0,
                    0, // Set the appropriate goal_id
                    name,
                    duration,
                    location,
                    note,
                    remind,
                    "once", // Set the appropriate type
                    false,
                    new ArrayList<>() // Set the appropriate restrictions
            );

            saveActionToDatabase(newAction);

            if (remind) {
                setAlarm(endTime, name);
            }

            dismiss();
        });

        return view;
    }

    private void saveActionToDatabase(Action action) {
        dbHelper.getActionDao().insert(action);
        // Optionally, refresh data in the parent fragment
    }

    private void setAlarm(long timeInMillis, String actionName) {
        AlarmManager alarmManager = (AlarmManager) requireContext()
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);
        intent.putExtra("action_name", actionName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Add Action");
        return dialog;
    }
}