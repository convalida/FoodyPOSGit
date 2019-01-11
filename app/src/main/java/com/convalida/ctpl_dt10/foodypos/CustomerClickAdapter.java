package com.convalida.ctpl_dt10.foodypos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class CustomerClickAdapter extends RecyclerView.Adapter<CustomerClickAdapter.CustomerClickViewHolder> {
    private List<CustomerClickModel> customerOrdersList;
    CustomerClickModel customerClickModel;
    Context context;

    @NonNull
    @Override
    public CustomerClickAdapter.CustomerClickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_click_row,parent,false);
        return new CustomerClickViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerClickAdapter.CustomerClickViewHolder holder, final int position) {
        customerClickModel=customerOrdersList.get(position);
        holder.orderNum.setText("#"+customerClickModel.getOrderNum());
        holder.price.setText("$"+customerClickModel.getAmount());
    //    holder.price.setOnClickListener(this);
      // holder.clock.setOnClickListener(this);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getRootView().getContext(),CustomerOrderDetails.class);
                v.getRootView().getContext().startActivity(intent);
            }
        });
    holder.clock.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(v.getRootView().getContext(),DateNdTimeCustomer.class);
            intent.putExtra("Date",customerOrdersList.get(position).getDate());
            intent.putExtra("Time",customerOrdersList.get(position).getTime());
            v.getRootView().getContext().startActivity(intent);
        }
    });
    holder.price.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(v.getRootView().getContext(),AmountCustomer.class);
            intent.putExtra("ItemPrice",customerOrdersList.get(position).getItemPrice());
            intent.putExtra("Tax",customerOrdersList.get(position).getTax());
            intent.putExtra("Tip",customerOrdersList.get(position).getTip());
            intent.putExtra("Total",customerOrdersList.get(position).getAmount());
            intent.putExtra("TaxPer",customerOrdersList.get(position).getTaxPer());
            v.getRootView().getContext().startActivity(intent);
        }
    });

    }


    @Override
    public int getItemCount() {
        return customerOrdersList.size();
    }

    public CustomerClickAdapter(List<CustomerClickModel> customerOrdersList) {
        this.customerOrdersList = customerOrdersList;
    }



    public class CustomerClickViewHolder extends RecyclerView.ViewHolder {
        public TextView orderNum, price;
        ImageView clock,more;
        public CustomerClickViewHolder(View itemView) {
            super(itemView);
            orderNum=itemView.findViewById(R.id.orderNum);
            price=itemView.findViewById(R.id.cost);
            clock=itemView.findViewById(R.id.time);
            more=itemView.findViewById(R.id.moreIcon);
        }
    }
}
