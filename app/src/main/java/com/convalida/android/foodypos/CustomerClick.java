package com.convalida.android.foodypos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerClick extends AppCompatActivity {
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    String customerId;
    CustomerClickModel customerClickModel;
    ArrayList<CustomerClickModel> customerHistory;
    Gson gson;
    RelativeLayout noDataLayout;
  //  LinearLayout linearLayout;
    RelativeLayout relativeLayout,progressBar;
   // ProgressBar progressBar;
    String restId;
    TextView name,email,contact;
    OrderDetailData orderDetailData=new OrderDetailData();
    private List<CustomerClickModel> myList=new ArrayList<>();
    private CustomerClickAdapter customerClickAdapter;
private static final String TAG="CustomerClick";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_click);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(CheckNetwork.isNetworkAvailable(CustomerClick.this)) {
            recyclerView = findViewById(R.id.customerDetailList);
            name=findViewById(R.id.userText);
            contact=findViewById(R.id.contactText);
            email=findViewById(R.id.mailText);
            noDataLayout=findViewById(R.id.noDataLayout);
            relativeLayout=findViewById(R.id.mainLayout);
            progressBar=findViewById(R.id.progressLayout);
            Intent intent=getIntent();
            customerId=intent.getStringExtra("Customer click");
          //  Toast.makeText(getApplicationContext(),customerId,Toast.LENGTH_LONG).show();
      //      populateData();
            if(getSupportActionBar()!=null){
                ActionBar actionBar=getSupportActionBar();
                actionBar.setTitle("Customer History");
            }
            requestQueue= Volley.newRequestQueue(this);
            GsonBuilder gsonBuilder=new GsonBuilder();
            gson=gsonBuilder.create();

            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
            restId=sharedPreferences.getString("Id","");
            Log.e(TAG,restId);
           fetchPosts();
          /**  customerClickAdapter = new CustomerClickAdapter(myList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(customerClickAdapter);**/
           // getData();

        }
    }

    private void fetchPosts() {
        final String url=Constants.BASE_URL+"CustomerDetails?RestaurantId="+restId+"&CustomerId="+customerId;
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG,response);
            GetCustomerDetails getCustomerDetails=new GetCustomerDetails();
            getCustomerDetails.execute(response);
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
            Toast.makeText(getApplicationContext(),"Sorry, some error occured",Toast.LENGTH_LONG).show();
        }
    };


    private void populateData() {
        CustomerClickModel customerClickModel=new CustomerClickModel("5134","24","2","08-20-2018","21:15","2","20","7");
        myList.add(customerClickModel);

        customerClickModel=new CustomerClickModel("5132","37","5","08-19-2018","19:30","2","30","7");
        myList.add(customerClickModel);

        customerClickModel=new CustomerClickModel("5133","32","4","05-27-2018","19:15","3","25","7");
        myList.add(customerClickModel);

//        customerClickAdapter.notifyDataSetChanged();
    }

    private class GetCustomerDetails extends AsyncTask<String,Void,Wrapper3> {
        int flagResult=1;

        @Override
        protected Wrapper3 doInBackground(String... response) {
            try {
                JSONObject jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
                else{
                    JSONObject jsonObject1=jsonObject.getJSONObject("Customer_Details");
                    String customerName=jsonObject1.getString("Name");
                    String email=jsonObject1.getString("CustomerEmail");
                    String contactNum=jsonObject1.getString("ContactNo");
                    customerHistory=new ArrayList<>();
                    JSONArray orderDetails=jsonObject1.getJSONArray("Order_Details");
                    for(int i=0;i<orderDetails.length();i++){
                        JSONObject orderDetailsObject=orderDetails.getJSONObject(i);
                        String orderNum=orderDetailsObject.getString("OrderNo");
                        String date=orderDetailsObject.getString("OrderPickupDate");
                        String time=orderDetailsObject.getString("OrderTime");
                        String total=orderDetailsObject.getString("Total");
                        customerClickModel=new CustomerClickModel();
                        customerClickModel.setOrderNum(orderNum);
                        customerClickModel.setDate(date);
                        customerClickModel.setTime(time);
                        customerClickModel.setAmount(total);

                        customerHistory.add(customerClickModel);
                    }
                    orderDetailData.setCustomerName(customerName);
                    orderDetailData.setMailId(email);
                    orderDetailData.setContact(contactNum);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Wrapper3 wrapper3=new Wrapper3();
            wrapper3.customerHistory=customerHistory;
            wrapper3.orderDetailData=orderDetailData;
            return wrapper3;
        }

        public void onPostExecute(Wrapper3 wrapper3){
            super.onPostExecute(wrapper3);
            if(flagResult==1) {
                relativeLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                name.setText(orderDetailData.getCustomerName());
                email.setText(orderDetailData.getMailId());
                contact.setText(orderDetailData.getContact());
                customerClickAdapter = new CustomerClickAdapter(customerHistory);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(customerClickAdapter);
            }
            else{
              //  Toast.makeText(getApplicationContext(),"Server error",Toast.LENGTH_LONG).show();
                Log.e(TAG,"Server error");
                new AlertDialog.Builder(CustomerClick.this)
                        .setMessage("Sorry, Unable to connect to server. Please try after some time")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                relativeLayout.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
            }

    }
}
