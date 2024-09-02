package cn.edu.ustc.timeflow.model;

import android.content.Context;

import cn.edu.ustc.timeflow.bean.Action;
import cn.edu.ustc.timeflow.util.DBHelper;

public class NewValuer implements Valuer {
    Context context;
    public NewValuer(Context context) {
        this.context = context;
    }


    // This method is not implemented
    @Override
    public double valuate(Action action) {
        return 0;
    }

}
