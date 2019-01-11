package com.convalida.ctpl_dt10.foodypos;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class CustomerClick extends AppCompatActivity {
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    Gson gson;
    private List<CustomerClickModel> myList=new ArrayList<>();
    private CustomerClickAdapter customerClickAdapter;
private static final String TAG="CustomerClick";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_click);
        if(CheckNetwork.isNetworkAvailable(CustomerClick.this)) {
            recyclerView = findViewById(R.id.customerDetailList);
            populateData();
            if(getSupportActionBar()!=null){
                ActionBar actionBar=getSupportActionBar();
                actionBar.setTitle("Customer History");
            }
            requestQueue= Volley.newRequestQueue(this);
            GsonBuilder gsonBuilder=new GsonBuilder();
            gson=gsonBuilder.create();
            customerClickAdapter = new CustomerClickAdapter(myList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(customerClickAdapter);
            getData();
        }
    }

    private void getData() {
        String restId;
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("Restaurant Id",MODE_PRIVATE);
        restId=sharedPreferences.getString("Id","");
        Log.e(TAG,restId);
        //final String MAIN="http://business.foodypos.com/App/Api.asmx/CustomerDetails?RestaurantId="+restId+"&CustomerId="
    }

    private void populateData() {
        CustomerClickModel customerClickModel=new CustomerClickModel("5134","24","2","08-20-2018","21:15","2","20","7");
        myList.add(customerClickModel);

        customerClickModel=new CustomerClickModel("5132","37","5","08-19-2018","19:30","2","30","7");
        myList.add(customerClickModel);

        customerClickModel=new CustomerClickModel("5133","32","4","05-27-2018","19:15","3","25","7");
        myList.add(customerClickModel);

//        customerClickAdapter.notifyDataSetChanged();
    }
}
