package com.convalida.ctpl_dt10.foodypos;

public class SalesData {

    String imageText,name,phone,orders,ammount;

    public SalesData(){

    }

    public SalesData(String imageText, String name, String phone, String orders, String ammount) {
        this.imageText = imageText;
        this.name = name;
        this.phone = phone;
        this.orders = orders;
        this.ammount = ammount;
    }

    public String getImageText() {
        return imageText;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getOrders() {
        return orders;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }
}
