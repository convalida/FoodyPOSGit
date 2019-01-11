package com.convalida.ctpl_dt10.foodypos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesListViewHolder> implements View.OnClickListener {

    List<SalesData> list = Collections.emptyList();
    Context context;

    public SalesAdapter(List<SalesData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SalesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sales, parent, false);
        return new SalesListViewHolder(v);

    }



    @Override
    public void onBindViewHolder(@NonNull SalesListViewHolder holder, final int position) {

        holder.imageText.setText(String.valueOf((list.get(position).name).charAt(0)));
        holder.name.setText(list.get(position).name);
        holder.phone.setText(list.get(position).phone);
        holder.orders.setText(list.get(position).orders);
        holder.ammount.setText(list.get(position).ammount);
        holder.name.setOnClickListener(this);

    }



    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, SalesData data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(SalesData data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context,CustomerClick.class);
      //  intent.putExtra()
        context.startActivity(intent);
    }
}



class SalesListViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView imageText, name, phone, orders, ammount;


    SalesListViewHolder(View itemView) {
        super(itemView);


        cv = itemView.findViewById(R.id.cv);
        imageText = itemView.findViewById(R.id.imageText);
        name = itemView.findViewById(R.id.name);
        phone = itemView.findViewById(R.id.phone);
        orders=itemView.findViewById(R.id.orders);
        ammount=itemView.findViewById(R.id.ammount);
    }
}