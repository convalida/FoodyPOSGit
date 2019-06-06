package com.convalida.android.foodypos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
//import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Sales extends AppCompatActivity {

    TextView from,to;
    Button search;
    RecyclerView recyclerView;
    private Gson gson;
    DatePickerDialog startDatePicker=null, endDatePicker=null;
    DatePickerDialog picker;
   // private SimpleDateFormat simpleDateFormat;
    private SimpleDateFormat simpleDateFormat;
    String fromdate,todate;
    List<SalesData> data=new ArrayList<>();
    private static final String TAG="Sales";
    SalesAdapter adapter;
    Date dateFrom, dateTo;
    ArrayList<SalesData> saleList=new ArrayList<>();
    private RequestQueue requestQueue;
    RelativeLayout progressLayout,mainLayout,noDataLayout;
    String restId;

   TextView totalOrders,totalAmount;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_sales);
     //   int orientation = this.getResources().getConfiguration().orientation;
      //  if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_sales);
      //  } else {
         //   setContentView(R.layout.sales_landscapemode);
      //  }
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (CheckNetwork.isNetworkAvailable(Sales.this)) {
            if (getSupportActionBar() != null) {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle("Sales");
            }
            from = findViewById(R.id.From);
            to = findViewById(R.id.To);
            search = findViewById(R.id.search);
            progressLayout = findViewById(R.id.progressLayout);
            mainLayout = findViewById(R.id.mainLayout);
            simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            recyclerView = findViewById(R.id.recyclerview);
            totalOrders = findViewById(R.id.ordersValue);
            totalAmount = findViewById(R.id.totalAmountValue);
            noDataLayout=findViewById(R.id.noDataLayout);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("RestaurantId", MODE_PRIVATE);
            restId = sharedPreferences.getString("Id", "");
            Log.e(TAG, restId);

            final Calendar myCalendar = Calendar.getInstance();
            //  myCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
            int day=myCalendar.get(Calendar.DAY_OF_WEEK);
            if(day==1){
                myCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                myCalendar.add(Calendar.DAY_OF_WEEK,-7);
            }
            else{
                myCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            }
             myCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
          //  myCalendar.add(Calendar.DATE, -7);
            String startDate = simpleDateFormat.format(myCalendar.getTime());
            from.setText(simpleDateFormat.format(myCalendar.getTime()));
            Log.e(TAG, "From date:" + startDate);

            Date date = Calendar.getInstance().getTime();
            to.setText(simpleDateFormat.format(date));

            if (savedInstanceState != null) {
                from.setText((CharSequence) savedInstanceState.getSerializable("Start date"));
                to.setText((CharSequence) savedInstanceState.getSerializable("End date"));
            }
            requestQueue = Volley.newRequestQueue(Sales.this);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            //     getWeekSales();
            fetchPosts();


            /**    SalesAdapter adapter = new SalesAdapter(data, getApplication());
             recyclerView.setAdapter(adapter);
             recyclerView.setLayoutManager(new LinearLayoutManager(this));**/

            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fromdate = from.getText().toString();
                    todate = to.getText().toString();
                    Log.e(TAG, "From: " + fromdate);
                    Log.e(TAG, "To: " + todate);
                    try {
                        dateFrom = simpleDateFormat.parse(fromdate);
                        dateTo = simpleDateFormat.parse(todate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (dateFrom != null && dateTo != null) {
                        if (dateFrom.compareTo(dateTo) > 0) {
                            Log.e(TAG, "From date is larger than to date");
                            AlertDialog dialog = new AlertDialog.Builder(Sales.this).create();
                            dialog.setIcon(R.drawable.mark1);
                            dialog.setTitle("Invalid date selection");
                            dialog.setMessage("To date is less than from date");
                            dialog.setButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.show();
                        } else {
                            mainLayout.setVisibility(View.INVISIBLE);
                            progressLayout.setVisibility(View.VISIBLE);
                            fetchPosts();

                        }
                    } else {
                        Toast.makeText(Sales.this, "Please select a date range to search", Toast.LENGTH_LONG).show();
                    }
                }
            });


            from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (from.getText().toString().equals("mm-dd-yyyy")) {
                        Calendar myCalendar = Calendar.getInstance();
                        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                        int month = myCalendar.get(Calendar.MONTH);
                        int year = myCalendar.get(Calendar.YEAR);
                        // date picker dialog
                        startDatePicker = new DatePickerDialog(Sales.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        //   from.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(year, monthOfYear, dayOfMonth);
                                        from.setText(simpleDateFormat.format(newDate.getTime()));
                                        from.setTextColor(Color.parseColor("#000000"));
                                    }
                                }, year, month, day);
                        startDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                        startDatePicker.show();
                    } else {
                        Calendar newCalendar = Calendar.getInstance();
                        try {
                            Date dateFrom = simpleDateFormat.parse(from.getText().toString());
                            newCalendar.setTime(dateFrom);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        startDatePicker = new DatePickerDialog(Sales.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        //   from.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(year, monthOfYear, dayOfMonth);
                                        from.setText(simpleDateFormat.format(newDate.getTime()));
                                    }
                                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        startDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                        startDatePicker.show();
                    }

                }
            });

            to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (to.getText().toString().equals("mm-dd-yyyy")) {
                        endDatePicker = new DatePickerDialog(Sales.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(year, monthOfYear, dayOfMonth);
                                        to.setText(simpleDateFormat.format(newDate.getTime()));
                                        to.setTextColor(Color.parseColor("#000000"));
                                    }
                                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                        endDatePicker.getDatePicker().setMaxDate(new Date().getTime());
                        endDatePicker.show();
                    } else {
                        Calendar newCalender = Calendar.getInstance();
                        try {
                            Date dateTo = simpleDateFormat.parse(to.getText().toString());
                            newCalender.setTime(dateTo);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        endDatePicker = new DatePickerDialog(Sales.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        Calendar newDate = Calendar.getInstance();
                                        newDate.set(year, monthOfYear, dayOfMonth);
                                        to.setText(simpleDateFormat.format(newDate.getTime()));
                                    }
                                }, newCalender.get(Calendar.YEAR), newCalender.get(Calendar.MONTH), newCalender.get(Calendar.DAY_OF_MONTH));
                        endDatePicker.getDatePicker().setMaxDate(new Date().getTime());
                        endDatePicker.show();
                    }
                }
            });
        }
    }


    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if (startDatePicker!=null){
            if(startDatePicker.isShowing()){
                startDatePicker.dismiss();
                final Calendar calendar=Calendar.getInstance();
                try{
                     Date date=simpleDateFormat.parse(from.getText().toString());
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startDatePicker=new DatePickerDialog(Sales.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar1=Calendar.getInstance();
                        calendar1.set(year,month,dayOfMonth);
                        from.setText(simpleDateFormat.format(calendar1.getTime()));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                startDatePicker.getDatePicker().setMaxDate(new Date().getTime());
                startDatePicker.show();
            }
        }
        if(endDatePicker!=null){
            if(endDatePicker.isShowing()){
                endDatePicker.dismiss();
                final Calendar newCalendar=Calendar.getInstance();
                try{
                    Date date=simpleDateFormat.parse(to.getText().toString());
                    newCalendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDatePicker=new DatePickerDialog(Sales.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar=Calendar.getInstance();
                        calendar.set(year,month,dayOfMonth);
                        to.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
                endDatePicker.getDatePicker().setMaxDate(new Date().getTime());
                endDatePicker.show();
            }
        }
    }

public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
    state.putSerializable("Start date",from.getText().toString());
    state.putSerializable("End date",to.getText().toString());
}

    private void fetchPosts() {
      //  progressLayout.setVisibility(View.VISIBLE);
      //  mainLayout.setVisibility(View.VISIBLE);

        final String MAIN = Constants.BASE_URL+"sales?RestaurantId="+restId+"&startdate="+from.getText().toString()+"&enddate="+to.getText().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET,MAIN,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG,response);
            GetResultsSales resultsSales=new GetResultsSales();
            resultsSales.execute(response);

        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
        }
    };




    private class GetResultsSales extends AsyncTask<String, Void, ArrayList<SalesData>> {
        double amountTotal=0.0,currentAmount,amountTotalRounded;
        int orderTotal=0,currentOrder;
        String finalAmount,finalOrder;
        int flagResult=1;


        @Override
        protected ArrayList<SalesData> doInBackground(String... response) {
                try {
                    saleList=new ArrayList<>();
                    JSONObject jsonObject= new JSONObject(response[0]);
                    if(jsonObject.has("Message")){
                        flagResult=0;
                    }
                    else {
                        JSONArray dateWise = jsonObject.getJSONArray("AllSales");
                        for (int i = 0; i < dateWise.length(); i++) {
                            JSONObject jObj = dateWise.getJSONObject(i);
                            String custName = jObj.getString("CustomerName");
                            String num = jObj.getString("ContactNumber");
                            String amount = jObj.getString("TotalAmount");
                            String order = jObj.getString("TotalOrder");
                            String customerId=jObj.getString("CustomerId");
                            currentOrder = Integer.parseInt(order);
                            currentAmount = Double.parseDouble(amount);
                            SalesData salesData = new SalesData();
                            salesData.setName(custName);
                            salesData.setImageText(String.valueOf(custName.charAt(0)));
                            salesData.setPhone(num);
                            salesData.setAmmount("$ " + amount);
                            salesData.setOrders(order);
                            salesData.setCustomerId(customerId);
                            saleList.add(salesData);
                            amountTotal = currentAmount + amountTotal;
                            orderTotal = currentOrder + orderTotal;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                amountTotalRounded=Math.round(amountTotal*100.0)/100.0;
                finalAmount=String.valueOf(amountTotalRounded);
                finalOrder=String.valueOf(orderTotal);

           // }
            return saleList;
        }
        protected void onPostExecute(ArrayList<SalesData> list){
            super.onPostExecute(saleList);
            if(flagResult==1) {
                progressLayout.setVisibility(View.INVISIBLE);
                totalOrders.setText(finalOrder);
                //totalAmount.setText("$"+finalAmount);
                totalAmount.setText(getString(R.string.price,finalAmount));
                mainLayout.setVisibility(View.VISIBLE);
                SalesAdapter adapter = new SalesAdapter(saleList, getApplication());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Sales.this));
            }
            else{
                if(flagResult==0){
                    Log.e(TAG,"Server error");
                    new AlertDialog.Builder(Sales.this)
                            .setMessage("Sorry, unable to connect to server. Please try after some time.")
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
}
