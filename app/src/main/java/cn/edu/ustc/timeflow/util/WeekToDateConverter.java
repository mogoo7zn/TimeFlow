package cn.edu.ustc.timeflow.util;

import android.content.Context;

import java.time.LocalDate;

/**
 * Convert week to date
 */
public class WeekToDateConverter {

    LocalDate startDate;
    Context context;


    public WeekToDateConverter(Context context){
        this.context = context;
        if(SharedPreferenceHelper.Companion.getString(context,"startdate") == null){
            //TODO: 2024-09-02 is the first day of the first week of the first semester of 2024
            // But how to get the first day of the first week of other semesters?
            startDate = LocalDate.of(2024, 9, 2);
            SharedPreferenceHelper.Companion.saveString(context,"startdate",startDate.toString());
        }
        else{
            startDate = LocalDate.parse(SharedPreferenceHelper.Companion.getString(context,"startdate"));
        }
    }

    public LocalDate getStartDate(){
        return startDate;
    }
    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
        SharedPreferenceHelper.Companion.saveString(context,"startdate",startDate.toString());
    }
    //查询某周星期某的日期(day=1~7,1为星期一)
    public LocalDate convert(int week, int day){
        // Calculate the number of days from the start date
        int daysFromStart = (week - 1) * 7 + (day - 1);
        // Add the days to the start date to get the target date
        return startDate.plusDays(daysFromStart);
    }

}
