package com.example.ctpl_dt10.foodypos;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Customer extends AppCompatActivity{
    TextView start,end;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog fromDatePicker, toDatePicker;
    RecyclerView recyclerView;
   // List<CustomerData> customerList=new ArrayList<>();
    ArrayList<SalesData> salesList=new ArrayList<>();
    private static final String TAG="Customer";
   // CustomerAdapter adapter;
    SalesAdapter salesAdapter;
    Button okCustomerBtn;
    private RequestQueue requestQueue;
    String fromClick,toClick;
    Date date1,date2;
    RelativeLayout progressLayout,mainLayout,noDataLayout;
    String customerData;
    Gson gson;
    String restId;
    TextView totalOrders,totalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_sales);
        } else {
            setContentView(R.layout.sales_landscapemode);
        }
        if(CheckNetwork.isNetworkAvailable(Customer.this)) {
            if (getSupportActionBar() != null) {
                android.support.v7.app.ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle("Customer Details");
            }
            totalOrders = findViewById(R.id.ordersValue);
            totalAmount = findViewById(R.id.totalAmountValue);
            start = findViewById(R.id.From);
            end = findViewById(R.id.To);
            okCustomerBtn = findViewById(R.id.search);
            dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            recyclerView = findViewById(R.id.recyclerview);
            progressLayout = findViewById(R.id.progressLayout);
            mainLayout = findViewById(R.id.mainLayout);
            noDataLayout=findViewById(R.id.noDataLayout);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("RestaurantId", MODE_PRIVATE);
            restId = sharedPreferences.getString("Id", "");
            Log.e(TAG, restId);
            // Calendar calendar= GregorianCalendar.getInstance(Locale.US);
            Calendar calendar = Calendar.getInstance();
            //  calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            //calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
            calendar.add(Calendar.DATE, -7);
            String startDate = dateFormat.format(calendar.getTime());
            start.setText(dateFormat.format(calendar.getTime()));
            Log.e(TAG, "From date:" + startDate);

            Date date = Calendar.getInstance().getTime();
            end.setText(dateFormat.format(date));
            if (savedInstanceState != null) {
                start.setText((CharSequence) savedInstanceState.getSerializable("Start date"));
                end.setText((CharSequence) savedInstanceState.getSerializable("End date"));
            }
            requestQueue = Volley.newRequestQueue(Customer.this);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            //  getCustomerDetails();
            getDatedCustomers();
            okCustomerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // fromClick=start.getText().toString();
                    // toClick=end.getText().toString();
                    Log.e(TAG, "From date: " + fromClick);
                    Log.e(TAG, "To date: " + toClick);
                    try {
                        date1 = dateFormat.parse(start.getText().toString());
                        date2 = dateFormat.parse(end.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1 != null && date2 != null) {
                        if (date2.compareTo(date1) < 0) {
                            Log.e(TAG, "To date is less than from date");
                            AlertDialog dialog = new AlertDialog.Builder(Customer.this).create();
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
                            // add code for date selection and then displaying the lists
                            mainLayout.setVisibility(View.INVISIBLE);
                            progressLayout.setVisibility(View.VISIBLE);
                            getDatedCustomers();
                        }
                    } else {
                        Toast.makeText(Customer.this, "Please select a date range", Toast.LENGTH_LONG).show();
                    }

                }
            });
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (start.getText().toString().equals("mm-dd-yyyy")) {
                        Calendar newCalendar = Calendar.getInstance();
                        fromDatePicker = new DatePickerDialog(Customer.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, month, dayOfMonth); // set it to from date of json
                                start.setText(dateFormat.format(newDate.getTime()));
                                start.setTextColor(Color.parseColor("#000000"));
                            }
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        fromDatePicker.getDatePicker().setMaxDate(new Date().getTime());
                        fromDatePicker.show();
                    } else {
                        Log.e(TAG, "From date is " + start.getText().toString());
                        Calendar newCalendar = Calendar.getInstance();
                        try {
                            Date date6 = dateFormat.parse(start.getText().toString());
                            newCalendar.setTime(date6);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        fromDatePicker = new DatePickerDialog(Customer.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, month, dayOfMonth);
                                start.setText(dateFormat.format(newDate.getTime()));
                            }
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        fromDatePicker.getDatePicker().setMaxDate(new Date().getTime());
                        fromDatePicker.show();
                    }
                }
            });
            end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (end.getText().toString().equals("mm-dd-yyyy")) {
                        Calendar newCalendar = Calendar.getInstance();

                        toDatePicker = new DatePickerDialog(Customer.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                end.setTextColor(Color.parseColor("#000000"));
                                newDate.set(year, month, dayOfMonth);
                                end.setText(dateFormat.format(newDate.getTime()));
                            }
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        toDatePicker.getDatePicker().setMaxDate(new Date().getTime());
                        toDatePicker.show();
                    } else {
                        Log.e(TAG, "To date is " + end.getText().toString());
                        Calendar newCalendar = Calendar.getInstance();
                        try {
                            Date date7 = dateFormat.parse(end.getText().toString());
                            newCalendar.setTime(date7);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        toDatePicker = new DatePickerDialog(Customer.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, month, dayOfMonth);
                                end.setText(dateFormat.format(newDate.getTime()));
                            }
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        toDatePicker.getDatePicker().setMaxDate(new Date().getTime());
                        toDatePicker.show();
                    }
                }
            });

        }

    }

    private void getDatedCustomers() {
        final String CUSTOMER_DATES="http://business.foodypos.com/App/Api.asmx/customer?RestaurantId="+restId+"&startdate="+start.getText().toString()+"&enddate="+end.getText().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET,CUSTOMER_DATES,onRequestsLoadedDate,onRequestsErrorDate);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    Response.Listener<String> onRequestsLoadedDate=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
      Log.e(TAG,response);
      GetDateCustomers getDateCustomers=new GetDateCustomers();
      getDateCustomers.execute(response);
        }
    };

    Response.ErrorListener onRequestsErrorDate=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
      Log.e(TAG,error.toString());
      Toast.makeText(getApplicationContext(),"Oops!! Server error. Please try after some time",Toast.LENGTH_LONG).show();
        }
    };

    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putSerializable("Start date",start.getText().toString());
        state.putSerializable("End date",end.getText().toString());
    }

    private class GetDateCustomers extends AsyncTask<String,Void,ArrayList<SalesData>> {
        double amountTotal=0.0,currentAmount,amountTotalRounded;
        int orderTotal=0,currentOrder;
        String finalAmount,finalOrders;
        int flagResult=1;
        @Override
        protected ArrayList<SalesData> doInBackground(String... response) {
            try {
                salesList=new ArrayList<>();
                JSONObject jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
           //     if(jsonObject.getString("Message").equals("Sorry, An Execption occurred"))
                else{
                JSONArray dateCustomerArray=jsonObject.getJSONArray("By_DateSelected");
                for(int i=0;i<dateCustomerArray.length();i++){
                    JSONObject jsonObject2=dateCustomerArray.getJSONObject(i);
                    String cusName=jsonObject2.getString("CustomerName");
                    String contact =jsonObject2.getString("ContactNo");
                    String amount=jsonObject2.getString("Totalamount");
                    String orders=jsonObject2.getString("TotalOrders");
                    currentOrder=Integer.parseInt(orders);
                    currentAmount=Double.parseDouble(amount);
                    SalesData salesData=new SalesData();
                    salesData.setName(cusName);
                    salesData.setPhone(contact);
                    salesData.setAmmount("$ "+amount);
                    salesData.setOrders(orders);
                    salesData.setImageText(String.valueOf(cusName.charAt(0)));
                    salesList.add(salesData);
                    amountTotal=amountTotal+currentAmount;
                    orderTotal=orderTotal+currentOrder;
                }}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            amountTotalRounded=Math.round(amountTotal*100.0)/100.0;
            finalAmount= String.valueOf(amountTotalRounded);
            finalOrders= String.valueOf(orderTotal);
            return salesList;
        }
        public void onPostExecute(ArrayList<SalesData> saleList) {
            super.onPostExecute(saleList);
            if (flagResult == 1) {
                mainLayout.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.INVISIBLE);
                totalOrders.setText(finalOrders);
                totalAmount.setText("$" + finalAmount);
                SalesAdapter adapter = new SalesAdapter(saleList, getApplication());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Customer.this));
            }
            else if(flagResult==0){
                //Toast.makeText(Customer.this,"Server error",Toast.LENGTH_LONG).show();
            Log.e(TAG,"Server error");
            new AlertDialog.Builder(Customer.this)
                    .setMessage("Sorry, unable to connect to server. Please try after some time")
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
