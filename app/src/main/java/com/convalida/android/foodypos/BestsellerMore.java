package com.convalida.android.foodypos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.Toolbar;

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
    String prefList;
    ProgressBar progressBar;
    RelativeLayout noDataLayout;
    int flagDay;
    Button searchBtn;
    private static final String TAG_WORKER_FRAGMENT="WorkerFragment";
    public Fragment mWorkerFragment;
    Date date1,date2;
   // ArrayList<BestsellerHeaderList> parentArrayList;
    private static final String TAG="BestsellerMore";
    private Toolbar toolbar;
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
    //    Toast.makeText(getApplicationContext(), "Inside onsave",Toast.LENGTH_LONG).show();
       // state.putSerializable("List",parentArrayList);
     //   BestsellerMore bestsellerMore=new BestsellerMore();
     //   BestsellerMore.GetBestsellerMore getList=bestsellerMore.new GetBestsellerMore();
     //   state.putSerializable("List",getList.parentArrayList);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("ListPref", Context.MODE_PRIVATE);
        prefList=sharedPreferences.getString("Details","");
        Log.e("OnSave",prefList);
        state.putSerializable("List",prefList);

       // Log.e(TAG,parentArrayList.toString());
     //   Log.e(TAG,getList.parentArrayList.toString());
    }

    protected void onRestoreInstanceState(final Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        prefList=savedInstanceState.getString("List");
   //     Toast.makeText(getApplicationContext(), "Inside onRestore",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestseller_more);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fromDate=findViewById(R.id.From);
        toDate=findViewById(R.id.To);
        noDataLayout=findViewById(R.id.noDataLayout);
        if(CheckNetwork.isNetworkAvailable(BestsellerMore.this)) {
          //  orders=findViewById(R.id.ordersValue);
            expandableListBestseller = findViewById(R.id.expandableBestSellerMore);
            requestQueue=Volley.newRequestQueue(BestsellerMore.this);
            GsonBuilder gsonBuilder=new GsonBuilder();
            gson=gsonBuilder.create();
            mainLayout=findViewById(R.id.mainLayout);
            progressBar=findViewById(R.id.progress);
            searchBtn=findViewById(R.id.search);


          /**  FragmentManager fragmentManager=getFragmentManager();
            mWorkerFragment=fragmentManager.findFragmentByTag(TAG_WORKER_FRAGMENT);
            if(mWorkerFragment==null) {
                mWorkerFragment=new WorkerFragment();
                fragmentManager.beginTransaction().add(mWorkerFragment, TAG_WORKER_FRAGMENT).commit();
               // mWorkerFragmen
            }**/

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
                flagDay=0;
               final Calendar calendar=Calendar.getInstance();
               // calendar.setFirstDayOfWeek(Calendar.MONDAY);


                int day=calendar.get(Calendar.DAY_OF_WEEK);
                if(day==1){
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                    calendar.add(Calendar.DAY_OF_WEEK,-7);
                }
                else{
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                }
                fromDate.setText(simpleDateFormat.format(calendar.getTime()));


                Date date=Calendar.getInstance().getTime();
                toDate.setText(simpleDateFormat.format(date));
            }
            else if(in.getIntExtra("Flag",0)==2){
                actionBar.setTitle("Monthly Bestseller items");
                flagDay=1;
                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH,1);
                fromDate.setText(simpleDateFormat.format(calendar.getTime()));

                Date date=Calendar.getInstance().getTime();
                toDate.setText(simpleDateFormat.format(date));
            }
            else{
                flagDay=2;
                actionBar.setTitle("Yearly Bestseller items");
                int year=Calendar.getInstance().get(Calendar.YEAR);
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.DAY_OF_YEAR,1);
                fromDate.setText(simpleDateFormat.format(myCalendar.getTime()));

                Date date=Calendar.getInstance().getTime();
                toDate.setText(simpleDateFormat.format(date));
            }

          //  if(savedInstanceState==null) {

           // }
           // else {
            if(savedInstanceState!=null){
                fromDate.setText((CharSequence) savedInstanceState.getSerializable("From date saved"));
                toDate.setText((CharSequence)savedInstanceState.getSerializable("To date saved"));
                savedInstanceState.getSerializable("List");
            }
            //  else {
                fetchData();
            //}
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

        }

    }

    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(toolbar==null){
            return;
        }
        final Context context=toolbar.getContext();
    }

    private void fetchData() {
        final String url=Constants.BASE_URL+"GetAllBestselleritems?RestaurantId="+restId+"&fromdate="+fromDate.getText().toString()+"&enddate="+toDate.getText().toString();
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
                else {

                    JSONObject jsonObject1 = jsonObject.getJSONObject("By_DateSelection");
                    if (flagDay == 2) {
                        JSONArray headingArray = jsonObject1.getJSONArray("YearlyBestsellerItems");
                        parentArrayList = new ArrayList<>();
                        for (int i = 0; i < headingArray.length(); i++) {
                            bestsellerHeaderList = new BestsellerHeaderList();
                            JSONObject jsonObject2 = headingArray.getJSONObject(i);
                            String year = jsonObject2.getString("Year");

                            // parentArrayList.add(year)
                            JSONArray itemsArray = jsonObject2.getJSONArray("Items_Details");
                            childArrayList = new ArrayList<>();
                            for (int j = 0; j < itemsArray.length(); j++) {
                                bestsellerChildlist = new BestsellerChildlist();
                                JSONObject jsonObject3 = itemsArray.getJSONObject(j);
                                String itemName = jsonObject3.getString("Subitems");
                                String count = jsonObject3.getString("Counting");
                                int counting = Integer.parseInt(count);
                                bestsellerChildlist.setCounting(count);
                                bestsellerChildlist.setItemname(itemName);
                                childArrayList.add(bestsellerChildlist);
                                orderCount = orderCount + counting;
                            }
                            bestsellerHeaderList.setDay(year);
                            bestsellerHeaderList.setChildlists(childArrayList);
                            parentArrayList.add(bestsellerHeaderList);
                        }
                    }
                    if(flagDay==1){
                        JSONArray headingArray=jsonObject1.getJSONArray("MonthlyBestsellerItems");
                        parentArrayList=new ArrayList<>();
                        for(int i=0;i<headingArray.length();i++){
                            bestsellerHeaderList=new BestsellerHeaderList();
                            JSONObject jsonObject2=headingArray.getJSONObject(i);
                            String month=jsonObject2.getString("Month");
                            JSONArray itemsArray = jsonObject2.getJSONArray("Items_Details");
                            childArrayList = new ArrayList<>();
                            for (int j = 0; j < itemsArray.length(); j++) {
                                bestsellerChildlist = new BestsellerChildlist();
                                JSONObject jsonObject3 = itemsArray.getJSONObject(j);
                                String itemName = jsonObject3.getString("Subitems");
                                String count = jsonObject3.getString("Counting");
                                int counting = Integer.parseInt(count);
                                bestsellerChildlist.setCounting(count);
                                bestsellerChildlist.setItemname(itemName);
                                childArrayList.add(bestsellerChildlist);
                                orderCount = orderCount + counting;
                            }
                            bestsellerHeaderList.setDay(month);
                            bestsellerHeaderList.setChildlists(childArrayList);
                            parentArrayList.add(bestsellerHeaderList);
                        }
                    }
                    if(flagDay==0){
                        JSONArray headingArray=jsonObject1.getJSONArray("WeeklyBestsellerItems");
                        parentArrayList=new ArrayList<>();
                        for(int i=0;i<headingArray.length();i++){
                            bestsellerHeaderList=new BestsellerHeaderList();
                            JSONObject jsonObject2=headingArray.getJSONObject(i);
                            String week=jsonObject2.getString("Week");
                            JSONArray itemsArray = jsonObject2.getJSONArray("Items_Details");
                            childArrayList = new ArrayList<>();
                            for (int j = 0; j < itemsArray.length(); j++) {
                                bestsellerChildlist = new BestsellerChildlist();
                                JSONObject jsonObject3 = itemsArray.getJSONObject(j);
                                String itemName = jsonObject3.getString("Subitems");
                                String count = jsonObject3.getString("Counting");
                                int counting = Integer.parseInt(count);
                                bestsellerChildlist.setCounting(count);
                                bestsellerChildlist.setItemname(itemName);
                                childArrayList.add(bestsellerChildlist);
                                orderCount = orderCount + counting;
                            }
                            bestsellerHeaderList.setDay(week);
                            bestsellerHeaderList.setChildlists(childArrayList);
                            parentArrayList.add(bestsellerHeaderList);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences preferences=getApplicationContext().getSharedPreferences("ListPref",0);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("Details", String.valueOf(parentArrayList));
            editor.apply();
            Log.e("onbackground",parentArrayList.toString());
            return parentArrayList;
        }
        public void onPostExecute(ArrayList<BestsellerHeaderList> parentList){
            super.onPostExecute(parentList);
            if(flagResult==1) {
                progressBar.setVisibility(View.INVISIBLE);
                mainLayout.setVisibility(View.VISIBLE);
                BestsellerMoreAdapter bestsellerMoreAdapter = new BestsellerMoreAdapter(BestsellerMore.this, parentList);
                if(parentList.size()>0) {
                    expandableListBestseller.setAdapter(bestsellerMoreAdapter);
                    expandableListBestseller.expandGroup(0);
                }
              //  orders.setText(String.valueOf(orderCount));
            }
            else if(flagResult==0){
           //     Toast.makeText(getApplicationContext(),"Server error",Toast.LENGTH_LONG).show();
                Log.e(TAG,"Server error");
                new android.support.v7.app.AlertDialog.Builder(BestsellerMore.this)
                        .setMessage("Sorry, Unable to connect to server. Please try after some time")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
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
