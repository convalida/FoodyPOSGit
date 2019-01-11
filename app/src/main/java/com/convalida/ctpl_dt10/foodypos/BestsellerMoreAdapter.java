package com.convalida.ctpl_dt10.foodypos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BestsellerMoreAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<BestsellerHeaderList> bestsellerHeaderLists=new ArrayList<>();
    //  private ArrayList<HashMap<String,ArrayList<BestsellerChildlist>>> bestsellerHeaderLists=new ArrayList<>();


    public BestsellerMoreAdapter(Context context, ArrayList<BestsellerHeaderList> bestsellerHeaderLists){
        this.context=context;
        this.bestsellerHeaderLists=bestsellerHeaderLists;
    }
    @Override
    public int getGroupCount() {
        return bestsellerHeaderLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<BestsellerChildlist> bestsellerChildlists=bestsellerHeaderLists.get(groupPosition).getChildlists();
        return bestsellerChildlists.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return bestsellerHeaderLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<BestsellerChildlist> bestsellerChildlists=bestsellerHeaderLists.get(groupPosition).getChildlists();
        return bestsellerChildlists.get(childPosition);
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
        BestsellerHeaderList bestsellerHeaderList= (BestsellerHeaderList) getGroup(groupPosition);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.bestseller_header,parent,false);
        }
        TextView day=convertView.findViewById(R.id.dayHeader);
        day.setText(bestsellerHeaderList.getDay());
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        BestsellerChildlist bestsellerChildlist= (BestsellerChildlist) getChild(groupPosition,childPosition);
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.bestseller_child,parent,false);
        }
        TextView name=convertView.findViewById(R.id.item);
        TextView counting=convertView.findViewById(R.id.qty);
        name.setText(bestsellerChildlist.getItemname());
        counting.setText((bestsellerChildlist.getCounting()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
