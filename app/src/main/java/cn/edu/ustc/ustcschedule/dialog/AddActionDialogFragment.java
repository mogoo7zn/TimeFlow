package cn.edu.ustc.ustcschedule.dialog;

import android.app.AlarmManager;
import android.app.AlertDialog;
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
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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
    private String actionType = "once"; // Default action type

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
        Button actionFrequencyButton = view.findViewById(R.id.action_frequency_button);
        actionFrequencyButton.setOnClickListener(v -> AddActionDialogFragment.this.showFrequencyMenu(v));

        view.findViewById(R.id.action_start_time).setRotation(180);

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
            String location = actionLocation.getText().toString().trim();
            String note = actionNote.getText().toString().trim();
            boolean remind = actionRemind.isChecked();
            int goal_id = action.getGoal_id();

            if (name.isEmpty() || location.isEmpty() || note.isEmpty()) {
                // Show error message
                Toast.makeText(requireContext(), "填完再存呦~", Toast.LENGTH_SHORT).show();
                return;
            }

            if (actionType == null || actionType.isEmpty()) {
                // Show error message
                Toast.makeText(requireContext(), "请选择频率", Toast.LENGTH_SHORT).show();
                return;
            }

            duration = Duration.ofMillis(endTime - startTime);

            //TODO:这里的id还不对
            if (action == null) {
                action = new Action(
                        0,
                        goal_id, // Set the appropriate goal_id
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

    /**
     * Show a popup menu to select the frequency of the action
     * @param v
     */
    private void showFrequencyMenu(View v) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.frequency_menu, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.showAsDropDown(v, 0, -80);


        //TODO:连接数据库，同时要把这个menu调到最上面，位置还不对
        popupView.findViewById(R.id.frequency_once).setOnClickListener(v1 -> {
            actionType = "once";
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.frequency_multiple_times).setOnClickListener(v1 -> {
            showMultipleTimesDialog();
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.frequency_fixed_time).setOnClickListener(v1 -> {
            actionType = "fixed";
            popupWindow.dismiss();
        });

        popupWindow.setOnDismissListener(() -> {
            CheckBox actionFrequencyButton = v.findViewById(R.id.action_frequency_button);
            actionFrequencyButton.setChecked(false);
        });
    }

    /**
     * Show a dialog to select multiple days
     */
    private void showMultipleTimesDialog() {
        String[] days = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        boolean[] checkedDays = new boolean[days.length];
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("选择每周星期")
                .setMultiChoiceItems(days, checkedDays, (dialog, which, isChecked) -> checkedDays[which] = isChecked)
                .setPositiveButton("确定", (dialog, which) -> {
                    StringBuilder selectedDays = new StringBuilder();
                    for (int i = 0; i < checkedDays.length; i++) {
                        if (checkedDays[i]) {
                            if (selectedDays.length() > 0) {
                                selectedDays.append(", ");
                            }
                            selectedDays.append(days[i]);
                        }
                    }
                    actionType = "multiple: " + selectedDays.toString();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * Set an alarm for the given end time
     * @param timeInMillis
     * @param actionName
     */
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
        action.setType(actionType); // Set the action type before saving
        dbHelper.getActionDao().insert(action);
        if (onActionSavedListener != null) {
            onActionSavedListener.onActionSaved();
        }
    }
}