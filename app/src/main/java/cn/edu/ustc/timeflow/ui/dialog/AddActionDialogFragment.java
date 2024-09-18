package cn.edu.ustc.timeflow.ui.dialog;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.timeflow.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.loper7.date_time_picker.DateTimePicker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.bean.Goal;
import cn.edu.ustc.timeflow.dao.ActionDao;
import cn.edu.ustc.timeflow.dao.GoalDao;
import cn.edu.ustc.timeflow.restriction.FixedTimeRestriction;
import cn.edu.ustc.timeflow.ui.adapter.ActionTimeAdapter;
import cn.edu.ustc.timeflow.util.AlarmReceiver;
import cn.edu.ustc.timeflow.util.DBHelper;

public class AddActionDialogFragment extends BottomSheetDialogFragment {

    private DBHelper dbHelper;

    private Action action;
    private String actionType = "Fixed"; // Default action type
    private int goalId;
    private final Context context;

    public AddActionDialogFragment(int goalId, Context context) {
        action = new Action();
        dbHelper= new DBHelper(context);
        this.goalId = goalId;
        this.context = context;
    }

    public AddActionDialogFragment(Action action,Context context) {
        this.action = action;
        dbHelper= new DBHelper(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
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
        EditText actionDuration = view.findViewById(R.id.action_duration);
        SwitchMaterial actionRemindSwitch = view.findViewById(R.id.action_remind_switch);
        SwitchMaterial actionTypeSwitch = view.findViewById(R.id.action_type_switch);
        Button saveActionButton = view.findViewById(R.id.save_action_button);

        // TODO: Implement the following features
        ImageButton actionAddTimeButton = view.findViewById(R.id.action_add_time);
        ListView actionTimeList = view.findViewById(R.id.action_time_list);
        ImageButton actionAddRestrictionButton = view.findViewById(R.id.action_add_restriction);
        ListView actionRestrictionList = view.findViewById(R.id.action_restriction_list);

        actionTimeList.setAdapter(new ActionTimeAdapter(context,action));


        if (action != null) {
            actionName.setText(action.getName());
            actionLocation.setText(action.getLocation());
            actionNote.setText(action.getNote());
            actionRemindSwitch.setChecked(action.isRemind());
            actionDuration.setText(action.getDuration().toString());
        }

        actionAddTimeButton.setOnClickListener(v -> {
            action.addRestriction(new FixedTimeRestriction(
                LocalTime.now().withSecond(0).withNano(0),
                LocalTime.now().plusHours(1).withSecond(0).withNano(0),
                FixedTimeRestriction.FixedTimeRestrictionType.DAILY,
                new ArrayList<>()
            ));
            actionTimeList.setAdapter(new ActionTimeAdapter(context,action));
        });

        actionTypeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            View actionFixedTime = view.findViewById(R.id.action_FixedTime);
            View actionRepeatingTime = view.findViewById(R.id.action_RepeatingTime);
            if (isChecked) {
                actionFixedTime.setVisibility(View.VISIBLE);
                actionRepeatingTime.setVisibility(View.GONE);
            } else {
                actionFixedTime.setVisibility(View.GONE);
                actionRepeatingTime.setVisibility(View.VISIBLE);
            }
        });

        saveActionButton.setOnClickListener(v -> {
            String name = actionName.getText().toString().trim();
            String location = actionLocation.getText().toString().trim();
            String note = actionNote.getText().toString().trim();
            boolean remind = actionRemindSwitch.isChecked();
            Duration duration = Duration.parse(actionDuration.getText().toString().trim());

            if (name.isEmpty() || location.isEmpty() || note.isEmpty()) {
                Toast.makeText(requireContext(), "填完再存呦~", Toast.LENGTH_SHORT).show();
                return;
            }

            if (actionType == null || actionType.isEmpty()) {
                Toast.makeText(requireContext(), "请选择频率", Toast.LENGTH_SHORT).show();
                return;
            }
            action.setGoal_id(goalId);
            action.setName(name);
            action.setLocation(location);
            action.setNote(note);
            action.setRemind(remind);
            action.setDuration(duration);

            saveActionToDatabase(action);


            dismiss();
        });

        return view;
    }

    private void saveActionToDatabase(Action action) {
        action.setType(actionType);
        ActionDao dao= dbHelper.getActionDao();
        GoalDao goalDao = dbHelper.getGoalDao();

        if (action.getGoal_id() == -1) {
            if(goalDao.getByContent("Default")==null){
                goalDao.insert(new Goal("Default", LocalDateTime.now(), LocalDateTime.MAX,"",0));
            }
            action.setGoal_id(goalDao.getByContent("Default").getId());
        }

        if (action.getId() == 0) {
            dao.insert(action);
        } else {
            dao.update(action);
        }

    }

}