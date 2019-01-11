package com.convalida.ctpl_dt10.foodypos;

import org.json.JSONObject;

/**
 * Created by CTPL-DT10 on 7/24/2018.
 */

public class DetailChildInfo {
    private String orderNum, time, price;
    JSONObject onClickJson;

    public DetailChildInfo(String orderNum, String time, String price) {
        this.orderNum = orderNum;
        this.time = time;
        this.price = price;
    }

    public DetailChildInfo() {

    }

    public JSONObject getOnClickJson() {
        return onClickJson;
    }

    public void setOnClickJson(JSONObject onClickJson) {
        this.onClickJson = onClickJson;
    }

    /**  public JSONObject getTableValues() {
        return tableValues;
    }

    public void setTableValues(JSONObject tableValues) {
        this.tableValues = tableValues;
    }**/


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
