package cn.edu.ustc.ustcschedule.fragment;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.timeflow.R;
import com.example.timeflow.databinding.FragmentScheduleDayBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loper7.date_time_picker.DateTimeConfig;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;

import java.time.LocalDate;
import java.util.Calendar;

import cn.edu.ustc.timeflow.ui.dialog.AddActionDialogFragment;

import cn.edu.ustc.ustcschedule.util.ClassTextInit;

public class DayScheduleFragment extends Fragment {

    private FragmentScheduleDayBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleDayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        ClassTextInit textInit = new ClassTextInit();
        ScrollView scrollView = view.findViewById(R.id.scroll);

        textInit.initText(view);
        scrollView.post(() -> scrollView.scrollTo(0, scrollView.getHeight() / 2));

        Button buttonMonth = view.findViewById(R.id.button_month);
        buttonMonth.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.monthScheduleFragment)
        );

        Button buttonWeek = view.findViewById(R.id.button_week);
        buttonWeek.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.weekScheduleFragment)
        );

        FloatingActionButton addActionFab = view.findViewById(R.id.add_action_fab);
        addActionFab.setOnClickListener(v -> {
            AddActionDialogFragment addActionDialog = new AddActionDialogFragment();
            addActionDialog.show(getParentFragmentManager(), "AddActionDialogFragment");
        });


        /**
         * 选择日期
         */
        ImageView imageView = view.findViewById(R.id.filter);
        imageView.setOnClickListener(v -> {
            new CardDatePickerDialog.Builder(getContext())
                    .setTitle("选择日期")
                    .setDisplayType(DateTimeConfig.YEAR,DateTimeConfig.MONTH, DateTimeConfig.DAY)
                    .setOnChoose("确定", time_millisecond -> {
                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(time_millisecond);
                        ClassTextInit textInit1 = new ClassTextInit(c);
                        textInit1.initText(view);
                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.day_list_container,
                                new DayListFragment(LocalDate.of(c.get(Calendar.YEAR),
                                        c.get(Calendar.MONTH) + 1,
                                        c.get(Calendar.DAY_OF_MONTH)
                                    )
                                )
                        );
                        fragmentTransaction.commit();
                        return null;
                    }).build().show();
        });


    }
}
