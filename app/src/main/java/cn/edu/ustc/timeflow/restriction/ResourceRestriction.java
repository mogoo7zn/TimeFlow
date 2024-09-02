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

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getConsumed() {
        return consumed;
    }

    public void setConsumed(int consumed) {
        this.consumed = consumed;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ResourceRestriction){
            ResourceRestriction r=(ResourceRestriction)obj;
            return r.resource_name.equals(resource_name) && r.amount==amount && r.consumed==consumed;
        }
        return false;
    }
}
