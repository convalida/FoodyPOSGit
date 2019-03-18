package com.convalida.android.foodypos;

import java.util.ArrayList;


public class HeaderInfo {
    private String date;
    private String numOfOrders;
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    //  private ArrayList<HashMap<String,String>> childInfos=new ArrayList<>();
    private ArrayList<DetailChildInfo> childInfos=new ArrayList<>();
  //  private ArrayList<ArrayList<DetailChildInfo>> childInfos=new ArrayList<>();

    public String getNumOfOrders() {
        return numOfOrders;
    }

    public void setNumOfOrders(String numOfOrders) {
        this.numOfOrders = numOfOrders;
    }

       public ArrayList<DetailChildInfo> getChildInfos() {
        return childInfos;
    }

    public void setChildInfos(ArrayList<DetailChildInfo> childInfos) {
        this.childInfos = childInfos;
    }




    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
