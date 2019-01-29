package com.convalida.ctpl_dt10.foodypos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BestsellerMore extends AppCompatActivity {
    ActionBar actionBar;
    ExpandableListView expandableListBestseller;
    TextView fromDate,toDate,orders;
    BestsellerMoreAdapter bestsellerMoreAdapter;
    RelativeLayout mainLayout;
    ProgressBar progressBar;
    Button searchBtn;
    Date date1,date2;
    private static final String TAG="BestsellerMore";
    //  ArrayList<BestsellerHeaderList> bestsellerHeaderLists=new ArrayList<>();

    BestsellerHeaderList bestsellerHeaderList;
    BestsellerChildlist bestsellerChildlist=new BestsellerChildlist();
    Gson gson;
    String startDate,endDate,restId,fromDateSaved,toDateSaved,fromClick,toClick;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog startDateDialog,endDateDialog;
    RequestQueue requestQueue;

    protected void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putSerializable("From date saved",fromDate.getText().toString());
        state.putSerializable("To date saved",toDate.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestseller_more);
        fromDate=findViewById(R.id.From);
        toDate=findViewById(R.id.To);
        if(CheckNetwork.isNetworkAvailable(BestsellerMore.this)) {
            orders=findViewById(R.id.ordersValue);
            expandableListBestseller = findViewById(R.id.expandableBestSellerMore);
            requestQueue=Volley.newRequestQueue(BestsellerMore.this);
            GsonBuilder gsonBuilder=new GsonBuilder();
            gson=gsonBuilder.create();
            mainLayout=findViewById(R.id.mainLayout);
            progressBar=findViewById(R.id.progress);
            searchBtn=findViewById(R.id.search);
            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
            restId=sharedPreferences.getString("Id","");

            simpleDateFormat=new SimpleDateFormat("MM-dd-yyyy",Locale.US);
            final Calendar myCalendar=Calendar.getInstance();
            Intent in=getIntent();
            if(getSupportActionBar()!=null){
                actionBar=getSupportActionBar();
            }
            if(in.getIntExtra("Flag",0)==1){
                actionBar.setTitle("Weekly Bestseller Items");
            }
            else if(in.getIntExtra("Flag",0)==2){
                actionBar.setTitle("Monthly Bestseller items");
            }
            else{
                actionBar.setTitle("Yearly Bestseller items");
                int year=Calendar.getInstance().get(Calendar.YEAR);
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.DAY_OF_YEAR,1);
                fromDate.setText(simpleDateFormat.format(myCalendar.getTime()));

                Date date=Calendar.getInstance().getTime();
                toDate.setText(simpleDateFormat.format(date));
            }

            if(savedInstanceState!=null){
                fromDate.setText((CharSequence) savedInstanceState.getSerializable("From date saved"));
                toDate.setText((CharSequence)savedInstanceState.getSerializable("To date saved"));
            }

            fetchData();

            fromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar=Calendar.getInstance();
                    try {
                        Date date=simpleDateFormat.parse(fromDate.getText().toString());
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    startDateDialog=new DatePickerDialog(BestsellerMore.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            Calendar newDate=Calendar.getInstance();
                            newDate.set(year,month,dayOfMonth);
                            fromDateSaved=simpleDateFormat.format(newDate.getTime());
                            fromDate.setText(simpleDateFormat.format(newDate.getTime()));
                            fromDate.setTextColor(Color.parseColor("#000000"));
                        }
                    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    startDateDialog.getDatePicker().setMaxDate(new Date().getTime());
                    startDateDialog.show();
                }
            });
            toDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar=Calendar.getInstance();
                    try {
                        Date dateTo=simpleDateFormat.parse(toDate.getText().toString());
                        calendar.setTime(dateTo);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDateDialog=new DatePickerDialog(BestsellerMore.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            Calendar newDate=Calendar.getInstance();
                            newDate.set(year,month,dayOfMonth);
                            toDateSaved=simpleDateFormat.format(newDate.getTime());
                            toDate.setText(simpleDateFormat.format(newDate.getTime()));
                            toDate.setTextColor(Color.parseColor("#000000"));
                        }
                    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    endDateDialog.getDatePicker().setMaxDate(new Date().getTime());
                    endDateDialog.show();
                }
            });

            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fromClick=fromDate.getText().toString();
                    toClick=toDate.getText().toString();
                    try {
                        date1=simpleDateFormat.parse(fromClick);
                        date2=simpleDateFormat.parse(toClick);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(date1!=null && date2!=null){
                        if(date2.compareTo(date1)<0){
                            AlertDialog dialog=new AlertDialog.Builder(BestsellerMore.this).create();
                            dialog.setIcon(R.drawable.mark1);
                            dialog.setTitle("Invalid date selection");
                            dialog.setMessage("To date is less than from date");
                            dialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dialog.show();
                        }
                        else {
                            mainLayout.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            fetchData();

                        }
                    }
                    /**  Calendar myCalendar=Calendar.getInstance();
                    startDateDialog=new DatePickerDialog(BestsellerMore.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                        }
                    })**/
                }
            });
//getData();
          /**  ArrayList<BestsellerHeaderList> headerLists = new ArrayList<>();
            BestsellerHeaderList bestsellerHeaderList1 = new BestsellerHeaderList();
            BestsellerChildlist bestsellerChildlist = new BestsellerChildlist();
            ArrayList<BestsellerChildlist> bestsellerChildlists = new ArrayList<>();
            bestsellerChildlist.setItemname("Tikka Masala Chicken");
            bestsellerChildlist.setCounting("3");
            bestsellerChildlists.add(bestsellerChildlist);
            bestsellerChildlist.setItemname("Tikka Masala Chicken");
            bestsellerChildlist.setCounting("3");
            bestsellerChildlists.add(bestsellerChildlist);
            bestsellerChildlist.setItemname("Tikka Masala Chicken");
            bestsellerChildlist.setCounting("3");
            bestsellerChildlists.add(bestsellerChildlist);
            bestsellerChildlist.setItemname("Tikka Masala Chicken");
            bestsellerChildlist.setCounting("3");
            bestsellerChildlists.add(bestsellerChildlist);
            bestsellerChildlist.setItemname("Tikka Masala Chicken");
            bestsellerChildlist.setCounting("3");
            bestsellerChildlists.add(bestsellerChildlist);
            bestsellerHeaderList1.setDay("18 November To 24 November");
            bestsellerHeaderList1.setChildlists(bestsellerChildlists);
            headerLists.add(bestsellerHeaderList1);
            BestsellerHeaderList bestsellerHeaderList2 = new BestsellerHeaderList();
            BestsellerChildlist bestsellerChildlist2 = new BestsellerChildlist();
            ArrayList<BestsellerChildlist> bestsellerChildlists2 = new ArrayList<>();
            bestsellerChildlist2.setItemname("Tikka Masala Chicken");
            bestsellerChildlist2.setCounting("3");
            bestsellerChildlists2.add(bestsellerChildlist2);
            bestsellerChildlist2.setItemname("Tikka Masala Chicken");
            bestsellerChildlist2.setCounting("3");
            bestsellerChildlists2.add(bestsellerChildlist2);
            bestsellerChildlist2.setItemname("Tikka Masala Chicken");
            bestsellerChildlist2.setCounting("3");
            bestsellerChildlists2.add(bestsellerChildlist2);
            bestsellerChildlist2.setItemname("Tikka Masala Chicken");
            bestsellerChildlist2.setCounting("3");
            bestsellerChildlists2.add(bestsellerChildlist2);
            bestsellerChildlist2.setItemname("Tikka Masala Chicken");
            bestsellerChildlist2.setCounting("3");
            bestsellerChildlists2.add(bestsellerChildlist2);
            bestsellerHeaderList2.setDay("11 November To 17 November");
            bestsellerHeaderList2.setChildlists(bestsellerChildlists2);
            headerLists.add(bestsellerHeaderList2);
            BestsellerMoreAdapter bestsellerMoreAdapter = new BestsellerMoreAdapter(BestsellerMore.this, headerLists);
            expandableListBestseller.setAdapter(bestsellerMoreAdapter);
            int count = bestsellerMoreAdapter.getGroupCount();
            for (int i = 0; i < count; i++) {
                expandableListBestseller.expandGroup(i);
            }**/

        }

    }

    private void fetchData() {
        final String url="http://business.foodypos.com/App/Api.asmx/GetAllBestselleritems?RestaurantId="+restId+"&fromdate="+fromDate.getText().toString()+"&enddate="+toDate.getText().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(300000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


            }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG,response);
            GetBestsellerMore bestsellerMore=new GetBestsellerMore();
            bestsellerMore.execute(response);
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"Sorry, some error occured",Toast.LENGTH_LONG).show();
        }
    };

    private class GetBestsellerMore extends AsyncTask<String, Void, ArrayList<BestsellerHeaderList>> {
        ArrayList<BestsellerHeaderList> parentArrayList;
        ArrayList<BestsellerChildlist> childArrayList;
        BestsellerHeaderList bestsellerHeaderList;
        BestsellerChildlist bestsellerChildlist;
        int flagResult=1;
        int orderCount=0;
        @Override
        protected ArrayList<BestsellerHeaderList> doInBackground(String... response) {
            try {
                JSONObject jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
                else{

                    JSONObject jsonObject1=jsonObject.getJSONObject("By_DateSelection");
                    JSONArray headingArray=jsonObject1.getJSONArray("YearlyBestsellerItems");
                    parentArrayList=new ArrayList<>();
                    for(int i=0;i<headingArray.length();i++){
                        bestsellerHeaderList=new BestsellerHeaderList();
                        JSONObject jsonObject2=headingArray.getJSONObject(i);
                        String year=jsonObject2.getString("Year");

                       // parentArrayList.add(year)
                        JSONArray itemsArray=jsonObject2.getJSONArray("Items_Details");
                        childArrayList=new ArrayList<>();
                        for(int j=0;j<itemsArray.length();j++){
                            bestsellerChildlist=new BestsellerChildlist();
                            JSONObject jsonObject3=itemsArray.getJSONObject(j);
                            String itemName=jsonObject3.getString("Subitems");
                            String count=jsonObject3.getString("Counting");
                            int counting=Integer.parseInt(count);
                            bestsellerChildlist.setCounting(count);
                            bestsellerChildlist.setItemname(itemName);
                            childArrayList.add(bestsellerChildlist);
                            orderCount=orderCount+counting;
                        }
                        bestsellerHeaderList.setDay(year);
                        bestsellerHeaderList.setChildlists(childArrayList);
                        parentArrayList.add(bestsellerHeaderList);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return parentArrayList;
        }
        public void onPostExecute(ArrayList<BestsellerHeaderList> parentList){
            super.onPostExecute(parentList);
            progressBar.setVisibility(View.INVISIBLE);
            mainLayout.setVisibility(View.VISIBLE);
            BestsellerMoreAdapter bestsellerMoreAdapter = new BestsellerMoreAdapter(BestsellerMore.this, parentList);
            expandableListBestseller.setAdapter(bestsellerMoreAdapter);
            expandableListBestseller.expandGroup(0);
            orders.setText(String.valueOf(orderCount));
        }
    }
}
