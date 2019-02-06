package com.convalida.ctpl_dt10.foodypos;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;

public class TopSeller extends AppCompatActivity {

  TextView orderTotal,amountTotal;
    RecyclerView recyclerView;
    List<SalesData> salesList=new ArrayList<>();
    RelativeLayout mainLayout, progressLayout,noDataLayout;
    Button see;
  //  TopSellerAdapter topSellerAdapter;
    SalesAdapter salesAdapter;
    private static final String TAG="TopSeller";
    private RequestQueue requestQueue;
    Gson gson;

    private String jsonString="{\"toprestaurentsale\":[{\"customerName\":\"Phillip Brown \",\"contactnumber\":\"(229)425-6069\",\"totalamount\":\"1370.78\",\"totalorder\":\"9\"},{\"customerName\":\"Brent Bitler\",\"contactnumber\":\"(678)332-7389\",\"totalamount\":\"1127.09\",\"totalorder\":\"17\"},{\"customerName\":\"Lamar Maddox\",\"contactnumber\":\"(478)808-1616\",\"totalamount\":\"701.24\",\"totalorder\":\"16\"}],\"weeksales\":[],\"allsales\":[]}";
ArrayList<SalesData> topSeller;
    ArrayList<HashMap<String,String>> topSellerList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_seller);
if(CheckNetwork.isNetworkAvailable(TopSeller.this)) {
    if (getSupportActionBar() != null) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Top Sale");
    }
    mainLayout = findViewById(R.id.mainLayout);
    progressLayout = findViewById(R.id.progressLayout);
    orderTotal = findViewById(R.id.orderTopValue);
    amountTotal = findViewById(R.id.amountTop);
    noDataLayout=findViewById(R.id.noDataLayout);
    topSeller = new ArrayList<SalesData>();
    recyclerView = findViewById(R.id.recyclerviewTopSeller);
    //   close=findViewById(R.id.close_btn);
    see = findViewById(R.id.seeAll);
    requestQueue = Volley.newRequestQueue(this);
    GsonBuilder gsonBuilder = new GsonBuilder();
    gson = gsonBuilder.create();
    getData();


    //new GetResultsSales().execute(MAIN);
/**try {
 parse();
 }
 catch (Exception e){

 }**/

}
    }

    private void getData() {
        String restId;
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
        restId=sharedPreferences.getString("Id","");
        Log.e(TAG,restId);
        final String MAIN = "http://business.foodypos.com/App/Api.asmx/sales?RestaurantId="+restId+"&startdate=null&enddate=null";
        StringRequest stringRequest=new StringRequest(Request.Method.GET,MAIN,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        Log.e(TAG,response);
        ExecuteTask task=new ExecuteTask();
        task.execute(response);
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
        }
    };



    public void allSales(View view){
        Intent intent=new Intent(TopSeller.this,Sales.class);
        startActivity(intent);
    }


    private class ExecuteTask extends AsyncTask<String,Void,List<SalesData>> {
        double totalAmount=0.0,amountRounded,currentAmount;
        int totalOrder,orderCurrent;
        String finalAmount,finalOrder;
        int flagResult=1;

        @Override
        protected List<SalesData> doInBackground(String... response) {
            try {
                JSONObject jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
                else{
                JSONArray topThree=jsonObject.getJSONArray("TopRestaurentSale");
                for(int i=0;i<topThree.length();i++) {
                    JSONObject jsonObject1 = topThree.getJSONObject(i);
                    String name = jsonObject1.getString("CustomerName");
                    String num = jsonObject1.getString("ContactNumber");
                    String amount = jsonObject1.getString("TotalAmount");
                    String order = jsonObject1.getString("TotalOrder");
                    String customerId = jsonObject1.getString("CustomerId");
                    currentAmount = Double.parseDouble(amount);
                    orderCurrent = Integer.parseInt(order);
                    SalesData salesData = new SalesData();
                    salesData.setName(name);
                    salesData.setImageText(String.valueOf(name.charAt(0)));
                    salesData.setPhone(num);
                    salesData.setCustomerId(customerId);
                    //    salesData.setAmmount(amount);
                    salesData.setAmmount("$ " + amount);
                    salesData.setOrders(order);
                    salesList.add(salesData);
                    totalAmount = currentAmount + totalAmount;
                    totalOrder = orderCurrent + totalOrder;
                }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            amountRounded=Math.round(totalAmount);
            finalAmount=String.valueOf("$"+amountRounded);
            finalOrder=String.valueOf(totalOrder);
            return salesList;
        }
        public void onPostExecute(List<SalesData> result){
            super.onPostExecute(result);
            if(flagResult==1) {
                progressLayout.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.VISIBLE);
                salesAdapter = new SalesAdapter(salesList, getApplication());
                recyclerView.setAdapter(salesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(TopSeller.this));
                orderTotal.setText(finalOrder);
                amountTotal.setText(finalAmount);
            }
            else{
                Log.e(TAG,"Server error");
                new AlertDialog.Builder(TopSeller.this)
                        .setMessage("Sorry, Unable to connect to server. Please try after some time")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                progressLayout.setVisibility(View.INVISIBLE);
                                mainLayout.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
            }
        }
    }
