package com.convalida.android.foodypos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder> {
    private Context context;
    private ArrayList<ReportsData> reportsList=new ArrayList<>();

    public ReportsAdapter(Context context, ArrayList<ReportsData> reportsList) {
        this.context = context;
        this.reportsList = reportsList;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_row,parent,false);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        ReportsData reportsData=reportsList.get(position);
        if(reportsData.getDay()!=null) {
            holder.day.setText(reportsData.getDay());
        }
        else if(reportsData.getWeek()!=null){
            holder.day.setText(reportsData.getWeek());
        }
        else if(reportsData.getMonth()!=null){
            holder.day.setText(reportsData.getMonth());
        }
        holder.order.setText(reportsData.getTotalOrder());
        holder.sales.setText(context.getString(R.string.price,reportsData.getTotalSale()));
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }

    //public void setmFilter(ArrayList<ReportsData> filteredList) {
    //}

    public class ReportsViewHolder extends RecyclerView.ViewHolder {
        TextView day,sales,order;

        public ReportsViewHolder(View itemView) {
            super(itemView);
            day=itemView.findViewById(R.id.dayValue);
            sales=itemView.findViewById(R.id.saleValue);
            order=itemView.findViewById(R.id.orderValue);

        }
    }
}
