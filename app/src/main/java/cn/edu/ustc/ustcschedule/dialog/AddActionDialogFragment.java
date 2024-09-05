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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.timeflow.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.loper7.date_time_picker.DateTimePicker;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.dao.ActionDao;
import cn.edu.ustc.timeflow.util.AlarmReceiver;
import cn.edu.ustc.timeflow.util.DBHelper;

public class AddActionDialogFragment extends BottomSheetDialogFragment {

    private long startTime = 0;
    private long endTime = 0;
    private DBHelper dbHelper;
    private Duration duration = Duration.ZERO;
    private Action action;

    public static AddActionDialogFragment newInstance(Action action) {
        AddActionDialogFragment fragment = new AddActionDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("action", action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
//            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
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

        if (getArguments() != null) {
            action = (Action) getArguments().getSerializable("action");
            if (action != null) {
                actionName.setText(action.getName());
                actionLocation.setText(action.getLocation());
                actionNote.setText(action.getNote());
                actionRemind.setChecked(action.isRemind());
                // Set start and end time if available
                // actionTimePicker.setStartTime(action.getStartTime());
                // actionTimePicker.setEndTime(action.getEndTime());
            }
        }

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
            String name = actionName.getText().toString().trim();
            duration = Duration.ofMillis(endTime - startTime);
            String location = actionLocation.getText().toString().trim();
            String note = actionNote.getText().toString().trim();
            boolean remind = actionRemind.isChecked();

            if (name.isEmpty() || location.isEmpty() || note.isEmpty()) {
                // Show error message
                Toast.makeText(requireContext(), "填完再存呦~", Toast.LENGTH_SHORT).show();
                return;
            }

            if (action == null) {
                action = new Action(
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
            } else {
                action.setName(name);
                action.setLocation(location);
                action.setNote(note);
                action.setRemind(remind);
                action.setDuration(duration);
            }

            saveActionToDatabase(action);

            if (remind) {
                setAlarm(endTime, name);
            }

            dismiss();
        });

        return view;
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

    public interface OnActionSavedListener {
        void onActionSaved();
    }

    private OnActionSavedListener onActionSavedListener;

    /**
     * Create a new instance of AddActionDialogFragment
     * @param action
     * @param listener
     * @return
     */
    public static AddActionDialogFragment newInstance(Action action, OnActionSavedListener listener) {
        AddActionDialogFragment fragment = new AddActionDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("action", action);
        fragment.setArguments(args);
        fragment.setOnActionSavedListener(listener);
        return fragment;
    }

    public void setOnActionSavedListener(OnActionSavedListener listener) {
        this.onActionSavedListener = listener;
    }

    private void saveActionToDatabase(Action action) {
        dbHelper.getActionDao().insert(action);
        if (onActionSavedListener != null) {
            onActionSavedListener.onActionSaved();
        }
    }
}