package cn.edu.ustc.timeflow.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.timeflow.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class GoalPickerMenuFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_picker_menu, container, false);

        return view;
    }

}