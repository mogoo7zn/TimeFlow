package cn.edu.ustc.ustcschedule.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.timeflow.R;

public class AddActionDialogFragment extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        // Set the dialog window attributes here
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_action, container, false);

        EditText actionName = view.findViewById(R.id.action_name);
        EditText actionDuration = view.findViewById(R.id.action_duration);
        EditText actionLocation = view.findViewById(R.id.action_location);
        EditText actionNote = view.findViewById(R.id.action_note);
        RadioGroup actionType = view.findViewById(R.id.action_type);
        DatePicker dailyDatePicker = view.findViewById(R.id.daily_date_picker);
        TimePicker startTime = view.findViewById(R.id.start_time);
        TimePicker endTime = view.findViewById(R.id.end_time);
        Button saveActionButton = view.findViewById(R.id.save_action_button);

        saveActionButton.setOnClickListener(v -> {
            // Handle saving the action here
            dismiss();
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Add Action");
        return dialog;
    }
}