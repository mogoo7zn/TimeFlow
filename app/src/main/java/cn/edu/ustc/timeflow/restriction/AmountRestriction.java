package cn.edu.ustc.timeflow.restriction;

import cn.edu.ustc.timeflow.bean.Task;

public class AmountRestriction extends Restriction{
    int total;
    int todo;
    int finished;
    public AmountRestriction(String code){
        String[] codes=code.split(" ");
        total=Integer.parseInt(codes[0]);
        todo=Integer.parseInt(codes[1]);
        finished=Integer.parseInt(codes[2]);
    }

    @Override
    public String coding() {
        return "AmountRestriction="+total+" "+todo+" "+finished;
    }

    @Override
    public boolean check(Task task) {
        return total>todo+finished;
    }
}
