package cn.edu.ustc.timeflow.restriction;

import androidx.annotation.Nullable;

import cn.edu.ustc.timeflow.bean.Task;
/**
 * 任务数量约束，包括总数，未完成数，已完成数
 * 要求总数大于等于未完成数和已完成数之和
 */
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTodo() {
        return todo;
    }

    public void setTodo(int todo) {
        this.todo = todo;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof AmountRestriction){
            AmountRestriction r=(AmountRestriction)obj;
            return r.total==total && r.todo==todo && r.finished==finished;
        }
        return false;
    }
}
