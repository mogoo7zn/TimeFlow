package cn.edu.ustc.ustcschedule.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.example.timeflow.R;
import com.example.timeflow.databinding.FragmentScheduleWeekBinding;
import com.loper7.date_time_picker.DateTimeConfig;
import com.loper7.date_time_picker.dialog.CardDatePickerDialog;
import com.loper7.date_time_picker.dialog.CardWeekPickerDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Formatter;

import cn.edu.ustc.ustcschedule.util.ClassTextInit;


public class WeekScheduleFragment extends Fragment {

    private FragmentScheduleWeekBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleWeekBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ClassTextInit textInit = new ClassTextInit();

        ScrollView scrollView = view.findViewById(R.id.scroll);
        textInit.initText(view);
        scrollView.post(() -> scrollView.scrollTo(0, scrollView.getHeight() / 2));

        ImageView imageView = view.findViewById(R.id.filter);
        imageView.setOnClickListener(v -> {
            new CardWeekPickerDialog.Builder(getContext())
                    .setTitle("选择周")
                    .setOnChoose("选择",(weekData,formatValue) -> {
                                //formatValue like 2024/09/09  -  2024/09/15
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                                LocalDate start = LocalDate.parse(formatValue.split("  -  ")[0], formatter);
                                Calendar c = Calendar.getInstance();
                                c.set(start.getYear(), start.getMonthValue() - 1, start.getDayOfMonth());

                                ClassTextInit textInit1 = new ClassTextInit(c);
                                textInit1.initText(view);
                                FragmentManager fragmentManager = getChildFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(
                                        R.id.week_content_container,
                                        new WeekContentFragment(start)
                                );
                                fragmentTransaction.commit();
                                return null;
                            }
                            )
                    .build().show();



        });
    }
}
