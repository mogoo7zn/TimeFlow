package cn.edu.ustc.timeflow.util;

public abstract class CourseTableWebConverter {

    String html;
    public CourseTableWebConverter (String html){
        this.html = html;
    }
    public abstract void parse();
}
