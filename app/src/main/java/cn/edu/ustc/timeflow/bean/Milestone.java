package cn.edu.ustc.timeflow.bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

import cn.edu.ustc.timeflow.util.DateTimeConverter;

@Entity(tableName = "milestone")
public class Milestone {
    /**
     * 里程碑id
     */
    @PrimaryKey(autoGenerate = true)
    int id;
    /**
     * 里程碑所属任务id
     */
    @ColumnInfo(name = "goal_id")
    int goal_id;
    /**
     * 里程碑名称
     */
    @ColumnInfo(name = "milestone_name")
    String content;
    /**
     * 里程碑时间
     */
    @ColumnInfo(name = "milestone_time")
    @TypeConverters(DateTimeConverter.class)
    LocalDateTime time;

    @ColumnInfo(name = "milestone_finished")
    boolean finished;
}
