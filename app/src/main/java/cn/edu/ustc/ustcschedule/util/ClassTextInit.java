package cn.edu.ustc.ustcschedule.util;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.example.timeflow.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ClassTextInit {
    public void initText(View view) {
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatYear = new SimpleDateFormat("yyyy", Locale.CHINA);
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatMonth = new SimpleDateFormat("MM", Locale.CHINA);
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatDay = new SimpleDateFormat("dd", Locale.CHINA);
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatWeek = new SimpleDateFormat("EEEE", Locale.CHINA);


        Calendar c = Calendar.getInstance(Locale.CHINA);
        TextView textYear = view.findViewById(R.id.text_current_year);
        textYear.setText(formatYear.format(c.getTimeInMillis()));
        TextView textMonth = view.findViewById(R.id.text_current_month);
        textMonth.setText(formatMonth.format(c.getTimeInMillis()));
        TextView textDay = view.findViewById(R.id.text_current_day);
        textDay.setText(formatDay.format(c.getTimeInMillis()));
        TextView textWeek = view.findViewById(R.id.text_current_week);
        textWeek.setText(formatWeek.format(c.getTimeInMillis()));
    }
}
