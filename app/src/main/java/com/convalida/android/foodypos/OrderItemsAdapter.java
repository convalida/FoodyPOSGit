package com.convalida.android.foodypos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderItemsAdapter extends BaseAdapter {
    ArrayList<OrderDetailChild> itemsList;
    Context context;
    int possibility=0;

    public OrderItemsAdapter(ArrayList<OrderDetailChild> itemsList, Context context) {
        this.itemsList=itemsList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //  ItemData type=itemsList.get(position);
        TextView itemNameKey, itemNameValue, priceKey, priceValue, totalKey, totalValue, modifierKey, modifierValue, addOnKey, addOnValue, addOnPriceKey, addOnPriceValue;
        if (view == null) {

                        switch (possibility) {
                case 0:
                    view = LayoutInflater.from(context).inflate(R.layout.row1, parent, false);
                    break;
                case 1:
                    view = LayoutInflater.from(context).inflate(R.layout.row2, parent, false);
                    break;

                case 2:
                    view = LayoutInflater.from(context).inflate(R.layout.row3, parent, false);
                    break;
                case 3:
                    view = LayoutInflater.from(context).inflate(R.layout.row4, parent, false);
                    break;
                case 4:
                    view = LayoutInflater.from(context).inflate(R.layout.row5, parent, false);
                    break;
                case 5:
                    view = LayoutInflater.from(context).inflate(R.layout.row6, parent, false);
                    break;
                case 6:
                    view = LayoutInflater.from(context).inflate(R.layout.row7, parent, false);
                    break;
                case 7:
                    view = LayoutInflater.from(context).inflate(R.layout.row8, parent, false);
                    break;
            }
        }
        assert view != null;
        itemNameKey = view.findViewById(R.id.nameText);
        itemNameValue = view.findViewById(R.id.itemValue);
        priceKey = view.findViewById(R.id.price);
        priceValue = view.findViewById(R.id.priceValue);
        totalKey = view.findViewById(R.id.total);
        totalValue = view.findViewById(R.id.totalValue);

        String dollar="$";
        itemNameKey.setText("Name");
        itemNameValue.setText(itemsList.get(position).getItemName());
        priceKey.setText("Price");
        priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));

        totalKey.setText("Total");
        totalValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getTotal()));

        try {
            switch (possibility) {
                case 0:
                    itemNameKey.setText("Name");
                    itemNameValue.setText(itemsList.get(position).getItemName());
                    priceKey.setText("Price");
                    priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));
                    totalKey.setText("Total");
                    totalValue.setText(itemsList.get(position).getTotal());
                    modifierKey = view.findViewById(R.id.modifier);
                    modifierValue = view.findViewById(R.id.modifierValue);
                    modifierKey.setText("Modifier");
                    modifierValue.setText(itemsList.get(position).getModifier());
                    addOnKey = view.findViewById(R.id.addon);
                    addOnValue = view.findViewById(R.id.addonValue);
                    addOnKey.setText("Add On");
                    addOnValue.setText(itemsList.get(position).getAddOn());
                    addOnPriceKey = view.findViewById(R.id.addonPrice);
                    addOnPriceKey.setText("AddOn Price");
                    addOnPriceValue = view.findViewById(R.id.addOnPriceValue);
                    addOnPriceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getAddOnPrice()));
                    TextView instructionKey = view.findViewById(R.id.instruction);
                    instructionKey.setText("Instructions");
                    TextView instructionsValue = view.findViewById(R.id.instructionValue);
                    instructionsValue.setText(itemsList.get(position).getInstructions());
                    break;
                case 1:
                    itemNameKey.setText("Name");
                    itemNameValue.setText(itemsList.get(position).getItemName());
                    priceKey.setText("Price");
                    priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));
                    totalKey.setText("Total");
                    totalValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getTotal()));
                    break;
                case 2:
                    itemNameKey.setText("Name");
                    itemNameValue.setText(itemsList.get(position).getItemName());
                    priceKey.setText("Price");
                    priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));
                    totalKey.setText("Total");
                    totalValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getTotal()));
                    modifierKey = view.findViewById(R.id.modifier);
                    modifierValue = view.findViewById(R.id.modifierValue);
                    modifierKey.setText("Modifier");
                    modifierValue.setText(itemsList.get(position).getModifier());
                    addOnKey = view.findViewById(R.id.addon);
                    addOnKey.setText("AddOn");
                    addOnValue = view.findViewById(R.id.addonValue);
                    addOnValue.setText(itemsList.get(position).getAddOn());
                    addOnPriceKey = view.findViewById(R.id.addonPrice);
                    addOnPriceKey.setText("AddOn Price");
                    addOnPriceValue = view.findViewById(R.id.addOnPriceValue);
                    addOnPriceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getAddOnPrice()));
                    break;
                case 3:
                    itemNameKey.setText("Name");
                    itemNameValue.setText(itemsList.get(position).getItemName());
                    priceKey.setText("Price");
                    priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));
                    totalKey.setText("Total");
                    addOnKey = view.findViewById(R.id.addon);
                    addOnValue = view.findViewById(R.id.addonValue);
                    addOnPriceKey = view.findViewById(R.id.addonPrice);
                    addOnPriceValue = view.findViewById(R.id.addOnPriceValue);
                    //  inflater.inflate(R.layout.row4,parent,false);
                    addOnKey.setText("Add On");
                    addOnValue.setText(itemsList.get(position).getAddOn());
                    addOnPriceKey.setText("AddOn Price");
                    addOnPriceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getAddOnPrice()));

                    instructionKey = view.findViewById(R.id.instruction);
                    instructionKey.setText("Instructions");
                    instructionsValue = view.findViewById(R.id.instructionValue);
                    instructionsValue.setText(itemsList.get(position).getInstructions());
                    break;
                case 4:
                    itemNameKey.setText("Name");
                    itemNameValue.setText(itemsList.get(position).getItemName());
                    priceKey.setText("Price");
                    priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));
                    totalKey.setText("Total");
                    totalValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getTotal()));

                    modifierKey = view.findViewById(R.id.modifier);
                    modifierValue = view.findViewById(R.id.modifierValue);

                    modifierKey.setText("Modifier");
                    modifierValue.setText(itemsList.get(position).getModifier());
                    break;
                case 5:
                    itemNameKey.setText("Name");
                    itemNameValue.setText(itemsList.get(position).getItemName());
                    priceKey.setText("Price");
                    priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));
                    totalKey.setText("Total");
                    addOnKey = view.findViewById(R.id.addon);
                    addOnValue = view.findViewById(R.id.addonValue);
                    addOnPriceKey = view.findViewById(R.id.addonPrice);
                    addOnPriceValue = view.findViewById(R.id.addOnPriceValue);
                    addOnKey.setText("Add On");
                    addOnValue.setText(itemsList.get(position).getAddOn());
                    addOnPriceKey.setText("AddOn Price");
                    addOnPriceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getAddOnPrice()));
                    break;
                case 6:
                    itemNameKey.setText("Name");
                    itemNameValue.setText(itemsList.get(position).getItemName());
                    priceKey.setText("Price");
                    priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));
                    totalKey.setText("Total");

                    instructionKey = view.findViewById(R.id.instruction);
                    instructionKey.setText("Instructions");
                    instructionsValue = view.findViewById(R.id.instructionValue);
                    instructionsValue.setText(itemsList.get(position).getInstructions());
                    break;
                case 7:
                    itemNameKey.setText("Name");
                    itemNameValue.setText(itemsList.get(position).getItemName());
                    priceKey.setText("Price");
                    priceValue.setText(context.getResources().getString(R.string.price,itemsList.get(position).getItemPrice()));
                    totalKey.setText("Total");

                    modifierKey = view.findViewById(R.id.modifier);
                    modifierValue = view.findViewById(R.id.modifierValue);

                    modifierKey.setText("Modifier");
                    modifierValue.setText(itemsList.get(position).getModifier());

                    instructionKey = view.findViewById(R.id.instruction);
                    instructionKey.setText("Instructions");
                    TextView instructionValue = view.findViewById(R.id.instructionValue);
                    instructionValue.setText(itemsList.get(position).getInstructions());
                    break;
            }
        } catch (NullPointerException ex) {

        }
        return view;
    }
    public int getViewTypeCount() {
        return 8;
    }

    public int getItemViewType(int position) {
       if (itemsList.get(position).getInstructions().equals("") && !itemsList.get(position).getAddOn().equals("") && !itemsList.get(position).getModifier().equals("")) {
           possibility=2;
        }
        if (!itemsList.get(position).getInstructions().equals("") && itemsList.get(position).getAddOn().equals("") && !itemsList.get(position).getModifier().equals("")) {
       possibility=7;
       }
        if (itemsList.get(position).getInstructions().equals("") && itemsList.get(position).getAddOn().equals("") && !itemsList.get(position).getModifier().equals("")) {
            possibility=4;
        }
        // } else {
        //           if (!itemsList.get(position).getAddOn().equals("")) {
        if (!itemsList.get(position).getInstructions().equals("") && !itemsList.get(position).getAddOn().equals("") && itemsList.get(position).getModifier().equals("")) {
            possibility=3;
        }
        //else {
        if (itemsList.get(position).getInstructions().equals("") && !itemsList.get(position).getAddOn().equals("") && itemsList.get(position).getModifier().equals("")) {
            possibility=5;
        }

        //        }

        //}
        //   else {
        if (!itemsList.get(position).getInstructions().equals("") && itemsList.get(position).getAddOn().equals("") && itemsList.get(position).getModifier().equals("")) {
            possibility=6;
        } //else
        if (itemsList.get(position).getInstructions().equals("") && itemsList.get(position).getAddOn().equals("") && itemsList.get(position).getModifier().equals("")) {
            possibility=1;
        }
        //  }
        // }


        //} else {
        if (!itemsList.get(position).getModifier().equals("") && !itemsList.get(position).getAddOn().equals("") && !itemsList.get(position).getInstructions().equals("")) {
            possibility=0;
        }
        return possibility;
    }
}

