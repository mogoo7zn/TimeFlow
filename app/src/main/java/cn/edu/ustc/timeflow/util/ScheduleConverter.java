package cn.edu.ustc.timeflow.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于将教务处获得的Schedule化为Action可用信息
 */
public class ScheduleConverter {
    String code;

    public ScheduleConverter(String code) {
        this.code = code;
    }

    public List<item> parse() {
        List<item> items = new ArrayList<>();
        if (code.contains("\n")) {
            code = code.replace("\n", " ");
        }
        String[] courses = code.split(" (?=\\d+~\\d+周|\\d+周|\\d+~\\d+\\(双\\)|\\d+~\\d+\\(单\\)|\\d+~\\d+,|\\d+,)");

        for (String course : courses) {
            if(course.isEmpty())
                continue;
            item i = new item();
            String[] parts = course.split(" ");
            String[] weeks = parts[0].split(",");
            for (String week : weeks) {
                if (week.contains("~")) {
                    String[] startEnd = week.split("~");
                    i.StartWeeks.add(Integer.parseInt(startEnd[0]));
                    i.EndWeeks.add(Integer.parseInt(startEnd[1].split("周|\\(双\\)|\\(单\\)")[0]));
                    if (week.contains("双")) {
                        i.EvenOrOddWeeks.add(2);
                    } else if (week.contains("单")) {
                        i.EvenOrOddWeeks.add(1);
                    } else {
                        i.EvenOrOddWeeks.add(0);
                    }
                } else {
                    i.StartWeeks.add(Integer.parseInt(week.split("周")[0]));
                    i.EndWeeks.add(Integer.parseInt(week.split("周")[0]));
                    i.EvenOrOddWeeks.add(0);
                }
            }
            i.Room = parts[1];
            i.Time = parts[2].split(":")[1];
            i.Teacher = parts[3];
            items.add(i);
        }
        return items;
    }

    public static class item {
        public List<Integer> StartWeeks = new ArrayList<>();
        public List<Integer> EndWeeks = new ArrayList<>();
        public List<Integer> EvenOrOddWeeks = new ArrayList<>();
        public String Room;
        public String Time;
        public String Teacher;

        @NonNull
        @Override
        public String toString() {
            StringBuilder s= new StringBuilder();
            for (int i = 0; i < StartWeeks.size(); i++) {
                s.append("item{" + "StartWeek=").append(StartWeeks.get(i)).append(", EndWeek=").append(EndWeeks.get(i)).append(", EvenOrOddWeek=").append(EvenOrOddWeeks.get(i)).append(", Room='").append(Room).append('\'').append(", Time='").append(Time).append('\'').append(", Teacher='").append(Teacher).append('\'').append('}');
            }
            return s.toString();
        }
    }
}


