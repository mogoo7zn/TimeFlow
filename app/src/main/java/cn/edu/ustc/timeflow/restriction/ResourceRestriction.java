package cn.edu.ustc.timeflow.restriction;

import cn.edu.ustc.timeflow.bean.Task;

public class ResourceRestriction extends Restriction{
    String resource_name;
    int amount;
    int consumed;
    public ResourceRestriction(String code){
        String[] codes=code.split(" ");
        resource_name=codes[0];
        amount=Integer.parseInt(codes[1]);
        consumed=Integer.parseInt(codes[2]);
    }
    public ResourceRestriction(String resource_name, int amount, int consumed){
        this.resource_name=resource_name;
        this.amount=amount;
        this.consumed=consumed;
    }
    @Override
    public boolean check(Task task) {
        return false;
    }
    @Override
    public String coding() {
        return "ResourceRestriction="+resource_name+" "+amount+" "+consumed;
    }
}
