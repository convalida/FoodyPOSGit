package com.convalida.android.foodypos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class EmployeeDetailAdapter extends RecyclerView.Adapter<EmployeeViewHolder> {
    List<EmployeeDetailData> employeeDetails= Collections.emptyList();
    Context context;

    public EmployeeDetailAdapter(List<EmployeeDetailData> employeeDetails, Context context) {
        this.employeeDetails = employeeDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_employee_details,parent,false);
        return new EmployeeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, final int position) {
        final String empName=employeeDetails.get(position).getName();
        final String empEmail=employeeDetails.get(position).getEmail();
        final String empRole=employeeDetails.get(position).getRole();
        final String isActive=employeeDetails.get(position).getActive();
        final String accId=employeeDetails.get(position).getAcctId();
        holder.name.setText(empName);
        holder.email.setText(empEmail);
        holder.role.setText(empRole);
        String active=isActive;
        if(active.equals("True")){
            holder.active.setText("Yes");
        }
        else{
            holder.active.setText("No");
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EditEmployeeDetail.class);
                intent.putExtra("ref1",empName);
                intent.putExtra("ref2",empEmail);
                intent.putExtra("ref3",empRole);
                intent.putExtra("ref4",isActive);
                intent.putExtra("ref5",accId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeDetails.size();
    }
}
class EmployeeViewHolder extends RecyclerView.ViewHolder{
    ImageButton edit;
    CardView cardView;
    TextView name,email,role,active;

     EmployeeViewHolder(View itemView) {
        super(itemView);

        edit=itemView.findViewById(R.id.edit);
        cardView=itemView.findViewById(R.id.ly_root);
        name=itemView.findViewById(R.id.employeeName);
        email=itemView.findViewById(R.id.EmployeeId);
        role=itemView.findViewById(R.id.roleType);
        active=itemView.findViewById(R.id.active);
    }
}
