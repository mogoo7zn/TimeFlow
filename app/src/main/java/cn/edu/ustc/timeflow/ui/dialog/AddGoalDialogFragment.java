package cn.edu.ustc.timeflow.ui.dialog;

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

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.bean.Goal;
import cn.edu.ustc.timeflow.dao.GoalDao;
import cn.edu.ustc.timeflow.util.DBHelper;

public class AddGoalDialogFragment extends DialogFragment {

    private EditText goalName;
    private EditText goalReason;
    private EditText goalMeasure;
    private EditText goalPriority;
    private CheckBox goalFinished;
    private CheckBox goalActive;
    private Button saveGoalButton;

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
        View view = inflater.inflate(R.layout.dialog_add_goal, container, false);

        goalName = view.findViewById(R.id.goal_name);
        goalReason = view.findViewById(R.id.goal_reason);
        goalMeasure = view.findViewById(R.id.goal_measure);
        goalPriority = view.findViewById(R.id.goal_priority);
        goalFinished = view.findViewById(R.id.goal_finished);
        goalActive = view.findViewById(R.id.goal_active);
        saveGoalButton = view.findViewById(R.id.save_goal_button);

        saveGoalButton.setOnClickListener(v -> saveGoal());

        return view;
    }

    private void saveGoal() {
        String name = goalName.getText().toString().trim();
        String reason = goalReason.getText().toString().trim();
        String measure = goalMeasure.getText().toString().trim();
        int priority = Integer.parseInt(goalPriority.getText().toString().trim());
        boolean finished = goalFinished.isChecked();
        boolean active = goalActive.isChecked();

        if (name.isEmpty() || reason.isEmpty() || measure.isEmpty()) {
            Toast.makeText(requireContext(), "填写完毕再存呦", Toast.LENGTH_SHORT).show();
            return;
        }

        Goal goal = new Goal(name, LocalDateTime.now(), LocalDateTime.now().plusDays(1), reason, priority);
        goal.setMeasure(measure);
        goal.setFinished(finished);
        goal.setActive(active);

        DBHelper db = new DBHelper(requireContext());
        GoalDao goalDao = db.getGoalDao();
        goalDao.insert(goal);

        Toast.makeText(requireContext(), "已存储", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}