package com.convalida.ctpl_dt10.foodypos;

import java.util.ArrayList;

/**
 * Created by CTPL-DT10 on 7/30/2018.
 */

public class OrderDetailHeader {
    private ArrayList<OrderDetailChild> orderDetailChildren=new ArrayList<>();

    public ArrayList<OrderDetailChild> getOrderDetailChildren() {
        return orderDetailChildren;
    }

    public void setOrderDetailChildren(ArrayList<OrderDetailChild> orderDetailChildren) {
        this.orderDetailChildren = orderDetailChildren;
    }
}
