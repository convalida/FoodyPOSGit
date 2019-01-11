package com.convalida.ctpl_dt10.foodypos;

/**
 * Created by CTPL-DT10 on 6/18/2018.
 */

public class ReportsData {
    String day, totalSale,totalOrder,week,month;

    public ReportsData(String day, String totalSale, String totalOrder) {
        this.day = day;
        this.totalSale = totalSale;
        this.totalOrder = totalOrder;
    }

    public ReportsData() {

    }

    public String getDay() {
        return day;
    }

    public String getTotalSale() {
        return totalSale;
    }

    public String getTotalOrder() {
        return totalOrder;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTotalSale(String totalSale) {
        this.totalSale = totalSale;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }
}
