package com.example.ctpl_dt10.foodypos;

/**
 * Created by CTPL-DT10 on 7/30/2018.
 */

public class OrderDetailChild {
    private String itemName, itemPrice,modifier,addOn,addOnPrice,instructions;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getAddOn() {
        return addOn;
    }

    public void setAddOn(String addOn) {
        this.addOn = addOn;
    }

    public String getAddOnPrice() {
        return addOnPrice;
    }

    public void setAddOnPrice(String addOnPrice) {
        this.addOnPrice = addOnPrice;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
