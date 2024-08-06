package cn.edu.ustc.timeflow.util;

import android.content.Context;

public abstract class CourseTableWebConverter {

    String html;
    Context context;
    public CourseTableWebConverter (String html, Context context){
        this.html = html;
        this.context = context;
    }
    public abstract void parse();
}
