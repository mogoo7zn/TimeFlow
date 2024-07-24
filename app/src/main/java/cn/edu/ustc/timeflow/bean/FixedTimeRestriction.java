package cn.edu.ustc.timeflow.bean;

import java.time.LocalTime;
import java.util.List;

public class FixedTimeRestriction {
    LocalTime start;
    LocalTime end;
    int type;//0:daily,1:weekly,2:monthly,3:yearly
    List<Integer> days;
}
