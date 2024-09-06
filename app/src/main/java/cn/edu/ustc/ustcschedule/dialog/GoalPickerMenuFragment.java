package cn.edu.ustc.ustcschedule.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.timeflow.R;


public class GoalPickerMenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_picker_menu, container, false);

        Button addGoalButton = view.findViewById(R.id.add_goal_button);
        addGoalButton.setOnClickListener(v -> showAddGoalDialog());

        return view;
    }

    //TODO:there are still mistakes about this function
    private void showAddGoalDialog() {
        FragmentManager fragmentManager = getParentFragmentManager();
        AddGoalDialogFragment addGoalDialogFragment = new AddGoalDialogFragment();
        addGoalDialogFragment.show(fragmentManager, "AddGoalDialogFragment");
    }
}