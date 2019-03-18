package com.convalida.android.foodypos;

import java.util.ArrayList;

public class BestsellerHeaderList {
    private String day;


    private ArrayList<BestsellerChildlist> childlists=new ArrayList<>();

    public String getDay(){
        return day;
    }
    public void setDay(String day){
        this.day=day;
    }

    public ArrayList<BestsellerChildlist> getChildlists(){
        return childlists;
    }

    public void setChildlists(ArrayList<BestsellerChildlist> childlists){
        this.childlists=childlists;
    }
}
