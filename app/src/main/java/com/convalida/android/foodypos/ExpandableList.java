package com.convalida.android.foodypos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ExpandableList extends BaseExpandableListAdapter {
    private Context context;
 //   List<String> dataHeader;
    private ArrayList<HeaderInfo> headerInfos;
    private ArrayList<DetailChildInfo> ordersOnADay;
    //private HashMap<String,List<String>> listDataChild;


    public ExpandableList(Context context, ArrayList<HeaderInfo> headerInfos) {
        this.context = context;
        this.headerInfos = headerInfos;
    }

    /**  public ExpandableOrderList(Context context, List<String> dataHeader, HashMap<String, List<String>> listDataChild) {
        this.context = context;
        this.dataHeader = dataHeader;
        this.listDataChild = listDataChild;
    }**/


    @Override
    public int getGroupCount() {
        //return this.dataHeader.size();
        return headerInfos.size();
    }
    public void setmFilter(ArrayList<DetailChildInfo> filteredList) {
        ordersOnADay=new ArrayList<>();
        ordersOnADay.addAll(filteredList);
        notifyDataSetChanged();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        //return this.listDataChild.get(this.dataHeader.get(groupPosition)).size();
       ArrayList<DetailChildInfo> orderDetails=headerInfos.get(groupPosition).getChildInfos();
      //  ArrayList<HashMap<String,String>> orderDetails=headerInfos.get(groupPosition).getChildInfos();
        return orderDetails.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        //return this.dataHeader.get(groupPosition);
    return headerInfos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
      //  return this.listDataChild.get(this.dataHeader.get(groupPosition)).get(childPosition);
        ArrayList<DetailChildInfo> orderdetails=headerInfos.get(groupPosition).getChildInfos();
     //   ArrayList<HashMap<String,String>> orderdetails=headerInfos.get(groupPosition).getChildInfos();
        return orderdetails.get(childPosition);
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
        //return false;
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //String headerTitle= (String) getGroup(groupPosition);
        HeaderInfo headerInfo= (HeaderInfo) getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_group,parent,false);
        }
        TextView headerText=convertView.findViewById(R.id.header);
        TextView numOfOrders=convertView.findViewById(R.id.numOfOrders);
        TextView price=convertView.findViewById(R.id.orderAmount);
      //  headerText.setText(headerTitle);
     //   headerText.setTypeface(null, Typeface.BOLD);
        headerText.setText(headerInfo.getDate().trim());
        numOfOrders.setText(headerInfo.getNumOfOrders());
        price.setText("$"+headerInfo.getPrice());
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    //    final String childText= (String) getChild(groupPosition,childPosition);
        DetailChildInfo detailChildInfo= (DetailChildInfo) getChild(groupPosition,childPosition);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.list_child,null);
        }
        TextView orderNum=convertView.findViewById(R.id.orderNo);
        TextView time=convertView.findViewById(R.id.time);
        TextView price=convertView.findViewById(R.id.price);
       // lblListItem.setText(childText);
        orderNum.setText(detailChildInfo.getOrderNum().trim());
        time.setText(detailChildInfo.getTime().trim());
        price.setText(detailChildInfo.getPrice().trim());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //return false;
        return true;
    }
}
