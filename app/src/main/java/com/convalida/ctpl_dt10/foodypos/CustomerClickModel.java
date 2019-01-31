package com.convalida.ctpl_dt10.foodypos;

/**
 * Created by CTPL-DT10 on 8/21/2018.
 */

public class CustomerClickModel {
   private String orderNum, amount, tip, date, time, tax, itemPrice,taxPer;

     CustomerClickModel(String orderNum, String amount, String tip, String date, String time, String tax, String itemPrice, String taxPer) {
        this.orderNum = orderNum;
        this.amount = amount;
        this.tip = tip;
        this.date = date;
        this.time=time;
        this.tax=tax;
        this.itemPrice=itemPrice;
        this.taxPer=taxPer;
    }

    public CustomerClickModel() {

    }

    public String getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(String taxPer) {
        this.taxPer = taxPer;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
