package cn.edu.ustc.ustcschedule.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;


import com.example.timeflow.R;
import com.example.timeflow.databinding.FragmentScheduleMonthBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MonthScheduleFragment extends Fragment {

    private FragmentScheduleMonthBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleMonthBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ClassPopUpMenu popUpMenu = new ClassPopUpMenu();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        setTextMonth(view);

//        view.findViewById(R.id.month_mode).setOnClickListener(view1 -> Navigation.findNavController(
//                requireView()).navigate(R.id.action_schedule_month_to_day));
//        view.findViewById(R.id.side_bar).setOnClickListener(view2 -> MainActivity.result.openDrawer());
//        view.findViewById(R.id.add_events).setOnClickListener(view3 -> popUpMenu.onAddEvent(view3, fragmentManager));
//        view.findViewById(R.id.filter).setOnClickListener(popUpMenu::onFilter);
    }

    private void setTextMonth(@NonNull View view) {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        CalendarView calendarView = view.findViewById(R.id.calendar);
        TextView textYear = view.findViewById(R.id.text_current_year_month);
        TextView textMonth1 = view.findViewById(R.id.text_current_month1);
        TextView textMonth2 = view.findViewById(R.id.text_current_month2);
        TextView textDay = view.findViewById(R.id.text_current_day_month);
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatYear = new SimpleDateFormat("yyyy", Locale.CHINA);
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatMonth = new SimpleDateFormat("MM", Locale.CHINA);
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatDay = new SimpleDateFormat("dd", Locale.CHINA);
        textYear.setText(formatYear.format(c.getTimeInMillis()));
        textMonth1.setText(formatMonth.format(c.getTimeInMillis()));
        textMonth2.setText(formatMonth.format(c.getTimeInMillis()));
        textDay.setText(formatDay.format(c.getTimeInMillis()));

        calendarView.setOnDateChangeListener((calendarView1, year, month, day) -> {
            textYear.setText(String.valueOf(year));
            String monthString;
            if (month<9) {
                monthString = "0"+ (month + 1);
            } else {
                monthString = String.valueOf(month+1);
            }
            textMonth1.setText(monthString);
            textMonth2.setText(monthString);
            String dayString;
            if (day<10) {
                dayString = "0"+ day;
            } else {
                dayString = String.valueOf(day);
            }
            textDay.setText(dayString);

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            calendarView1.setDate(calendar.getTimeInMillis());
            FragmentTransaction ft= getParentFragmentManager().beginTransaction().setReorderingAllowed(true);
            ft.replace(R.id.month_list_container,new MonthListFragment());
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.commit();
            //this.getView().findViewById(R.id.)
        });
    }
}
