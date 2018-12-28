package com.example.ctpl_dt10.foodypos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Reports extends AppCompatActivity{
    SimpleDateFormat simpleDateFormat;
    TextView from,to,orders,amount;
    Button searchBtn;
    String restId;
    int flagDate;
    String finalAmountDay,finalOrderDay,finalAmountWeek,finalAmountMonth,finalOrderWeek,finalOrderMonth;
    LinearLayout mainReportsLayout;
    RelativeLayout mainProgressLayout,childProgressLayout,noDataLayout;
    DatePickerDialog startDialog,endDialog;
    RecyclerView recyclerView;
    ArrayList<ReportsData> dayReports=new ArrayList<>();
    ArrayList<ReportsData> weekReports=new ArrayList<>();
    ArrayList<ReportsData> monthReports=new ArrayList<>();
    ReportsAdapter adapter;
    TextView day,week,month;
    RequestQueue requestQueue;
    Gson gson;
    String fromClick, toClick;
    private static final String TAG="Reports";
    int SEVEN_DAYS_BEFORE=1000*60*60*24*7;
    int defaultDateFlag=0;
    String weekBefore;
    String fromCompare,toCompare;
    Date date1,date2;
    String fromDate,toDate; int dateD,dateM,dateY;
    ImageView image1,image2,image3;
    String myreportData;
    int flag=1;


   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /**  int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_reports);
            } else {
            setContentView(R.layout.report_landscapemode);
        }**/
     setContentView(R.layout.activity_reports);
       from=findViewById(R.id.startDateReport);
       to=findViewById(R.id.endDateReport);
if(CheckNetwork.isNetworkAvailable(Reports.this)){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Sales Report");
        }
        mainReportsLayout=findViewById(R.id.mainReports);
        mainProgressLayout=findViewById(R.id.mainReportsProgress);
        orders=findViewById(R.id.ordersValue);
        amount=findViewById(R.id.amountValue);
       // childProgressLayout=findViewById(R.id.childProgressLayout);
        recyclerView=findViewById(R.id.reportRecycler);
        day=findViewById(R.id.byDay);
        week=findViewById(R.id.byMonth);
        month=findViewById(R.id.byYear);
        image1=findViewById(R.id.image1);
        image2=findViewById(R.id.image2);
        image3=findViewById(R.id.image3);
        searchBtn=findViewById(R.id.search);
        noDataLayout=findViewById(R.id.noDataLayout);

    //    adapter=new ReportsAdapter(Reports.this,reportsData);

        simpleDateFormat=new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        final Date date=new Date();

       final Calendar cal=Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
         cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
       //cal.add(Calendar.DATE,-7);
       String startDateWeek=simpleDateFormat.format(cal.getTime());
       from.setText(startDateWeek);
       from.setTextColor(Color.parseColor("#000000"));

       Date todayDate=Calendar.getInstance().getTime();
       to.setText(simpleDateFormat.format(todayDate));
       to.setTextColor(Color.parseColor("#000000"));

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
        restId=sharedPreferences.getString("Id","");
        Log.e(TAG,restId);

        requestQueue= Volley.newRequestQueue(Reports.this);
        GsonBuilder gsonBuilder=new GsonBuilder();
        gson=gsonBuilder.create();

      /**  if(savedInstanceState!=null){
            if(savedInstanceState.getSerializable("Start date")!="mm-dd-yyyy"){
                from.setText((CharSequence) savedInstanceState.getSerializable("Start date"));
                from.setTextColor(Color.parseColor("#000000"));
            }
            else{
                from.setText("mm-dd-yyyy");
            }
            if(savedInstanceState.getSerializable("End date")!="mm-dd-yyyy"){
                to.setText((CharSequence) savedInstanceState.getSerializable("End date"));
                to.setTextColor(Color.parseColor("#000000"));
            }
            else{
                to.setText("mm-dd-yyyy");
            }
        }
        else{
            from.setText("mm-dd-yyyy");
            to.setText("mm-dd-yyyy");
        }**/
searchBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String finalAmountDay,finalOrderDay,finalAmountWeek,finalAmountMonth,finalOrderWeek,finalOrderMonth;
        mainReportsLayout.setVisibility(View.INVISIBLE);
        mainProgressLayout.setVisibility(View.VISIBLE);


        displayDatesResults();
    }
});
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

defaultDateFlag=1;
              /**  if(from.getText().toString().equals("mm-dd-yyyy")) {
                    Calendar newCalendar=Calendar.getInstance();
                    Log.e(TAG,"Default date: "+fromDate);
                    startDialog = new DatePickerDialog(Reports.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year,month,dayOfMonth);
                            from.setText(simpleDateFormat.format(newDate.getTime()));
                            from.setTextColor(Color.BLACK);

                        }
                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    startDialog.getDatePicker().setMaxDate(new Date().getTime());

                    startDialog.show();
                }
                else{**/
                    Log.e(TAG,"From date set is: "+fromDate);
                    Calendar newCalendar=Calendar.getInstance();
                    try {
                        Date date4=simpleDateFormat.parse(from.getText().toString());
                        newCalendar.setTime(date4);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }




                    startDialog=new DatePickerDialog(Reports.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar newDate=Calendar.getInstance();
                            newDate.set(year,month,dayOfMonth);
                            from.setText(simpleDateFormat.format(newDate.getTime()));
                        }
                    },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.DAY_OF_MONTH));
                    startDialog.getDatePicker().setMaxDate(new Date().getTime());
                    startDialog.show();
              //  }
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultDateFlag=1;
        /**        if(to.getText().toString().equals("mm-dd-yyyy")) {

                    final Calendar newCalendar = Calendar.getInstance();
                    endDialog = new DatePickerDialog(Reports.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, month, dayOfMonth);
                            to.setText(simpleDateFormat.format(newDate.getTime()));
                            to.setTextColor(Color.BLACK);
                        }
                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    endDialog.getDatePicker().setMaxDate(new Date().getTime());
                    endDialog.show();
                }
                else{**/
                    Log.e(TAG,"To date is "+to.getText().toString());
                    final Calendar newCalendar=Calendar.getInstance();
                    try {
                        Date date5=simpleDateFormat.parse(to.getText().toString());
                        newCalendar.setTime(date5);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endDialog = new DatePickerDialog(Reports.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year,month,dayOfMonth);
                            to.setText(simpleDateFormat.format(newDate.getTime()));
                        }
                    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    endDialog.getDatePicker().setMaxDate(new Date().getTime());
                    endDialog.show();
               // }
            }
        });

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        displayResults();
    }
    }
    protected void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
      //  if(state!=null) {

        if(from.length()>0 && to.length()>0) {
            state.putSerializable("Start date", from.getText().toString());
            state.putSerializable("End date", to.getText().toString());
        }
            //}
        }

    private void displayDatesResults() {
        final String DATE_PASS = "http://business.foodypos.com/App/Api.asmx/getreport?RestaurantId=" + restId + "&fromdate=" + from.getText().toString() + "&enddate=" + to.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DATE_PASS, onPostsDateLoaded, onPostsDateError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
    Response.Listener<String> onPostsDateLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG,response);
            GetReports reports=new GetReports(flag);
            reports.execute(response);

        }
    };
    Response.ErrorListener onPostsDateError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
        }
    };



    private void checkDateRange() {

        try{
            date1 = simpleDateFormat.parse(from.getText().toString());
            date2 = simpleDateFormat.parse(to.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //  if(!from.getText().toString().equals("mm-dd-yyyy") && !to.getText().toString().equals("mm-dd-yyyy")) {
        if (date2.compareTo(date1) < 0) {
            flagDate=0;
            Log.e(TAG, "To date is less than from date");
            AlertDialog dialog = new AlertDialog.Builder(Reports.this).create();
            dialog.setTitle("Invalid date selection");
            dialog.setMessage("To date cannot be less than from date");
            dialog.setIcon(R.drawable.mark1);
            dialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        } else {
            flagDate=1;
            //displayResults();
        }      //  adapter.notifyDataSetChanged();


    }

    private void displayResults() {
        StringRequest stringRequest = null;
        final String MAIN = "http://business.foodypos.com/App/Api.asmx/getreport?RestaurantId=" + restId + "&fromdate=null&enddate=null";
      //  final String DATE_PASS = "http://business.foodypos.com/App/Api.asmx/getreport?RestaurantId=" + restId + "&fromdate=" + from.getText().toString() + "&enddate=" + to.getText().toString();
        //if (defaultDateFlag == 0) {
            stringRequest = new StringRequest(Request.Method.GET, MAIN, onPostsLoaded, onPostsError);
        //}
        //else if(defaultDateFlag ==1){
          //  stringRequest=new StringRequest(Request.Method.GET,DATE_PASS,onPostsLoaded,onPostsError);
       // }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

    }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG,response);
            GetReports reports=new GetReports(flag);
            reports.execute(response);
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
        }
    };

    public void DailyReport(View v){
   // fromClick= from.getText().toString();
        dailyReport();
      }

    private void dailyReport() {
        flag=1;
        if (defaultDateFlag == 0){
         //   orders.setText(finalOrderDay);
           // amount.setText(finalAmountDay);
            final Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String startDateWeek = simpleDateFormat.format(cal.getTime());
        from.setText(startDateWeek);
    }//cal.add(Calendar.DATE,-7);
      /**  else{
        //    childProgressLayout.setVisibility(View.VISIBLE);
          //  recyclerView.setVisibility(View.INVISIBLE);
            displayResults();
        }**/
        checkDateRange();
        if(flagDate==1) {

            week.setTextColor(Color.parseColor("#4d4f56"));
            image2.setColorFilter(getApplicationContext().getResources().getColor(R.color.hightlight));

            month.setTextColor(Color.parseColor("#4d4f56"));
            image3.setColorFilter(getApplicationContext().getResources().getColor(R.color.hightlight));

            day.setTextColor(Color.parseColor("#ff6501"));
            image1.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimary));

            //childProgressLayout.setVisibility(View.INVISIBLE);
           // recyclerView.setVisibility(View.VISIBLE);
            ReportsAdapter adapter = new ReportsAdapter(getApplication(), dayReports);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(Reports.this));
            orders.setText(finalOrderDay);
             amount.setText("$"+finalAmountDay);

        }
    }


    public void WeeklyReport(View v){
  //  fromClick= from.getText().toString();

        weeklyReport();

    }

    private void weeklyReport() {
        flag=2;
        if(defaultDateFlag==0) {
           // orders.setText(finalOrderWeek);
         //   amount.setText(finalAmountWeek);
            Calendar calendar = Calendar.getInstance();
//if(from.getText().toString().equals())
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startDate = simpleDateFormat.format(calendar.getTime());
            from.setText(startDate);
            ReportsAdapter adapter=new ReportsAdapter(getApplication(),weekReports);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(Reports.this));
        }
/**else{
 //  childProgressLayout.setVisibility(View.VISIBLE);
 //  recyclerView.setVisibility(View.INVISIBLE);
 displayResults();
 ReportsAdapter adapter=new ReportsAdapter(getApplication(),weekReports);
 recyclerView.setAdapter(adapter);
 recyclerView.setLayoutManager(new LinearLayoutManager(Reports.this));

 }**/
        checkDateRange();
        if(flagDate==1) {
            month.setTextColor(Color.parseColor("#4d4f56"));
            image3.setColorFilter(getApplicationContext().getResources().getColor(R.color.hightlight));

            day.setTextColor(Color.parseColor("#4d4f56"));
            image1.setColorFilter(getApplicationContext().getResources().getColor(R.color.hightlight));

            week.setTextColor(Color.parseColor("#ff6501"));
            image2.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimary));

            ReportsAdapter adapter = new ReportsAdapter(getApplication(), weekReports);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(Reports.this));
            orders.setText(finalOrderWeek);
               amount.setText("$"+finalAmountWeek);


            //      childProgressLayout.setVisibility(View.INVISIBLE);
            //    recyclerView.setVisibility(View.VISIBLE);


        }
        }

    public void MonthlyReport(View v){
 //   fromClick= from.getText().toString();
        montlyReport();
           }

    private void montlyReport() {

        flag=3;
        if(defaultDateFlag==0){
        //    orders.setText(finalOrderMonth);
          //  amount.setText(finalAmountMonth);
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH,1);
            String startDate=simpleDateFormat.format(calendar.getTime());
            from.setText(startDate);}
        /** else{
         displayResults();
         }**/
        checkDateRange();
        if(flagDate==1) {

            day.setTextColor(Color.parseColor("#4d4f56"));
            image1.setColorFilter(getApplicationContext().getResources().getColor(R.color.hightlight));

            week.setTextColor(Color.parseColor("#4d4f56"));
            image2.setColorFilter(getApplicationContext().getResources().getColor(R.color.hightlight));

            month.setTextColor(Color.parseColor("#ff6501"));
            image3.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimary));

            ReportsAdapter adapter = new ReportsAdapter(getApplication(), monthReports);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(Reports.this));
            orders.setText(finalOrderMonth);
              amount.setText("$"+finalAmountMonth);
        }
    }
    private class GetReports extends AsyncTask<String,Void,ArrayList<ReportsData>> {
        int flag;
        public GetReports(int flag) {
            this.flag=flag;
        }

        double amountTotalDay=0.0,amountTotalWeek=0.0,amountTotalMonth=0.0;
        double amountCurrentDay,amountRoundedDay,amountCurrentWeek,amountCurrentMonth,amountRoundedWeek,amountRoundedMonth;
        int orderTotalDay,orderCurrentDay,orderTotalWeek,orderTotalMonth,orderCurrentWeek,orderCurrentMonth;
        int flagResult=1;

        @Override
        protected ArrayList<ReportsData> doInBackground(String... response) {
            dayReports=new ArrayList<>();
            weekReports=new ArrayList<>();
            monthReports=new ArrayList<>();
            try {
                JSONObject jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
                else {
                    JSONArray dailyArray = jsonObject.getJSONArray("Day");
                    for (int i = 0; i < dailyArray.length(); i++) {
                        JSONObject jsonObject1 = dailyArray.getJSONObject(i);
                        String day = jsonObject1.getString("Day");
                        String sale = jsonObject1.getString("Totalsales");
                        String order = jsonObject1.getString("TotalsOrders");
                        orderCurrentDay = Integer.parseInt(order);
                        amountCurrentDay = Double.parseDouble(sale);

                        ReportsData reportsData = new ReportsData();
                        reportsData.setDay(day);
                        reportsData.setTotalOrder(order);
                        reportsData.setTotalSale(sale);
                        dayReports.add(reportsData);
                        amountTotalDay = amountCurrentDay + amountTotalDay;
                        orderTotalDay = orderCurrentDay + orderTotalDay;
                    }
                    amountRoundedDay = Math.round(amountTotalDay * 100.0) / 100.0;
                    finalAmountDay = String.valueOf(amountRoundedDay);
                    finalOrderDay = String.valueOf(orderTotalDay);
                    JSONArray weeklyArray = jsonObject.getJSONArray("Week");
                    for (int j = 0; j < weeklyArray.length(); j++) {
                        JSONObject weekObject = weeklyArray.getJSONObject(j);
                        String week = weekObject.getString("Week");
                        String sale = weekObject.getString("Totalsales");
                        String order = weekObject.getString("TotalsOrders");
                        orderCurrentWeek = Integer.parseInt(order);
                        amountCurrentWeek = Double.parseDouble(sale);

                        ReportsData reportsData = new ReportsData();
                        reportsData.setWeek(week);
                        reportsData.setTotalSale(sale);
                        reportsData.setTotalOrder(order);
                        weekReports.add(reportsData);
                        amountTotalWeek = amountCurrentWeek + amountTotalWeek;
                        orderTotalWeek = orderCurrentWeek + orderTotalWeek;
                    }
                    amountRoundedWeek = Math.round(amountTotalWeek * 100.0) / 100.0;
                    finalAmountWeek = String.valueOf(amountRoundedWeek);
                    finalOrderWeek = String.valueOf(orderTotalWeek);

                    JSONArray monthlyArray = jsonObject.getJSONArray("Month");
                    for (int k = 0; k < monthlyArray.length(); k++) {
                        JSONObject monthObject = monthlyArray.getJSONObject(k);
                        String month = monthObject.getString("Month");
                        String sale = monthObject.getString("Totalsales");
                        String order = monthObject.getString("TotalsOrders");
                        orderCurrentMonth = Integer.parseInt(order);
                        amountCurrentMonth = Double.parseDouble(sale);

                        ReportsData reportsData = new ReportsData();
                        reportsData.setMonth(month);
                        reportsData.setTotalOrder(order);
                        reportsData.setTotalSale(sale);
                        monthReports.add(reportsData);
                        amountTotalMonth = amountCurrentMonth + amountTotalMonth;
                        orderTotalMonth = orderCurrentMonth + orderTotalMonth;
                    }
                    amountRoundedMonth = Math.round(amountTotalMonth * 100.0) / 100.0;
                    finalAmountMonth = String.valueOf(amountRoundedMonth);
                    finalOrderMonth = String.valueOf(orderTotalMonth);
                }
                } catch (JSONException e) {
                e.printStackTrace();
            }
            return dayReports;
        }
        public void onPostExecute(ArrayList<ReportsData> dailyReports){
            super.onPostExecute(dailyReports);
       if(flagResult==1) {
           if (defaultDateFlag == 0) {
               dailyReport();
           } else {
               if (flag == 1) {
                   dailyReport();
               } else if (flag == 2) {
                   weeklyReport();
               } else if (flag == 3) {
                   montlyReport();
               }
           }
           mainReportsLayout.setVisibility(View.VISIBLE);
           mainProgressLayout.setVisibility(View.INVISIBLE);
       }
       else{
         //  Toast.makeText(Reports.this,"Server error",Toast.LENGTH_LONG).show();
       Log.e(TAG,"Server error");
       new AlertDialog.Builder(Reports.this)
               .setMessage("Sorry, unable to connect to server. Please try after some time.")
               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       noDataLayout.setVisibility(View.VISIBLE);
                       mainProgressLayout.setVisibility(View.INVISIBLE);
                       mainReportsLayout.setVisibility(View.INVISIBLE);
                   }
               })
               .setCancelable(false)
               .create()
               .show();
       }
       }
    }
}
