package com.convalida.ctpl_dt10.foodypos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;



public class ExpandableOnClickList extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<OrderDetailHeader> orderDetailHeaderArrayList;
    private ArrayList<OrderDetailChild> orderDetailChildArrayList;
    RelativeLayout simpleRelativeLayout;

    public ExpandableOnClickList(Context context, ArrayList<OrderDetailHeader> orderDetailHeaderArrayList) {
        this.context = context;
        this.orderDetailHeaderArrayList = orderDetailHeaderArrayList;
    }

    @Override
    public int getGroupCount() {
        return orderDetailHeaderArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<OrderDetailChild> orderDetailChildren=orderDetailHeaderArrayList.get(groupPosition).getOrderDetailChildren();
        return orderDetailChildren.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orderDetailHeaderArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<OrderDetailChild> childDetails=orderDetailHeaderArrayList.get(groupPosition).getOrderDetailChildren();
        return childDetails.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        OrderDetailHeader orderDetailHeader= (OrderDetailHeader) getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       convertView=inflater.inflate(R.layout.order_detail_head,null);
        }
        TextView headerIcon=convertView.findViewById(R.id.orderDetailHeader);
        Typeface font=Typeface.createFromAsset(context.getAssets(),"fonts/fontawesome-webfont.ttf");
        headerIcon.setTypeface(font);
        headerIcon.setText("\uf0f5");
        return convertView;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        OrderDetailChild orderDetailChild= (OrderDetailChild) getChild(groupPosition,childPosition);
        TextView itemName, itemPrice;
       // View view=null;

        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.order_detail_child,null);

        }
     /**   TextView itemName=convertView.findViewById(R.id.itemName);
        TextView itemPrice=convertView.findViewById(R.id.itemPrice);
        TextView modifier=convertView.findViewById(R.id.modifierText);
        TextView addOn=convertView.findViewById(R.id.addon);
        TextView addOnPrice=convertView.findViewById(R.id.addonPrice);
        TextView instructions=convertView.findViewById(R.id.instructions);
        itemName.setText(orderDetailChild.getItemName().trim());
        itemPrice.setText(orderDetailChild.getItemPrice().trim());
        modifier.setText(orderDetailChild.getModifier().trim());
        addOn.setText(orderDetailChild.getAddOn().trim());
        addOnPrice.setText(orderDetailChild.getAddOnPrice().trim());
        instructions.setText(orderDetailChild.getInstructions().trim());**/

     //   itemName=convertView.findViewById(R.id.itemName);
       // itemPrice=convertView.findViewById(R.id.itemPrice);

   //  simpleRelativeLayout=convertView.findViewById(R.id.parentRelativeLayout);
     //RelativeLayout.LayoutParams layoutModifier=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
       //      RelativeLayout.LayoutParams.WRAP_CONTENT);
     TextView itemText=new TextView(context);
        itemText.setId(1);
     //itemText.setLayoutParams(layoutModifier);
     itemText.setText(orderDetailChild.getItemName());
     //layoutModifier.addRule(RelativeLayout.CENTER_HORIZONTAL);
     itemText.setTextColor(Color.parseColor("#000000"));
//     simpleRelativeLayout.addView(itemText);
    // simpleRelativeLayout.addView(convertView);
       // convertView.addV

  // notifyDataSetChanged();

    long orderDetailChild1=getChildId(groupPosition,childPosition);

  // if(!OnClickOrder.itemDetails.get(childPosition).getModifier().equals("----")){
  /** if(!orderDetailChild.getModifier().equals("----")){
       simpleRelativeLayout=convertView.findViewById(R.id.parentRelativeLayout);
        RelativeLayout.LayoutParams layoutModifier=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView itemModifier=new TextView(context);
        layoutModifier.addRule(RelativeLayout.BELOW,R.id.itemName);
        itemModifier.setLayoutParams(layoutModifier);
        itemModifier.setText(orderDetailChild.getModifier());
        simpleRelativeLayout.addView(itemModifier);
        simpleRelativeLayout
       itemName.setText(orderDetailChild.getItemName().trim());
       itemPrice.setText(orderDetailChild.getItemPrice().trim());
       return simpleRelativeLayout;

    }
    else {
       itemName.setText(orderDetailChild.getItemName().trim());
       itemPrice.setText(orderDetailChild.getItemPrice().trim());


       return convertView;
   }**/
  //return view;
        return convertView;
  // return simpleRelativeLayout;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
