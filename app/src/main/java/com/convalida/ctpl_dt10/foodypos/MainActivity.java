package com.convalida.ctpl_dt10.foodypos;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //  Button btn;
    TextView tCustomerImg, tOrderImg, wOrderImg, wCustomerImg;
    TextView monthBtn, yearBtn, graphWeek;
    LinearLayout totalSaleLl, weeklySaleLl, totalOrderLl, weeklyOrderLl, totalCustomerLl, weeklyCustomerLl;
    TextView totalSale, totalOrders, totalCustomers, weekSale, weekOrder, weekCustomer;
    RelativeLayout progressLayout, mainLayout,noDataLayout;

    LineChart chart;
    CheckBox salesBox, ordersBox;
    LineDataSet salesSet, ordersSet;
    List<ILineDataSet> dataSets;
    public static final String TAG = "Dashboard";
    BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    RecyclerView recyclerView;
    ArrayList<String> heading;
    XAxis xAxis;
    String restId, restName;
    ArrayList<Entry> weekOrdersList, weekSalesList, monthOrdersList, monthSalesList, yearOrderList, yearSalesList;
    ArrayList<String> xWeekLabel = new ArrayList<>();
    ArrayList<String> xMonthLabel = new ArrayList<>();
    ArrayList<String> xYearLabel = new ArrayList<>();
    RecyclerView.LayoutManager recylerViewLayoutManager;
    //  RecyclerViewAdapter recyclerViewHorizontalAdapter;
    LinearLayoutManager horizontalLinearLayout;
    public static final String[] TITLES = {"Online Order List", "Weekly Bestsellers", "Monthly Bestsellers", "Yearly Bestsellers", "Top Sale"};
    View childView;
    int recyclerViewItemPosition;
    NavigationView mainNavigationView;
    RequestQueue requestQueue;
    Gson gson;
    private String jsonString = "{\"LabelValues\":{\"TotalSale\":\"$22427.77\",\"TotalOrders\":\"867\",\"TotalCustomers\":\"814\",\"Weeksale\":\"$0\",\"WeeklyOrder\":\"0\",\"WeekCustomer\":\"0\"},\"chart\":{\"weeklist\":null,\"monthlist\":null,\"yearlist\":[{\"TotalSale\":\"3325.5464\",\"TotalOrders\":\"138\",\"month\":\"June 2018\"},{\"TotalSale\":\"4191.6534\",\"TotalOrders\":\"163\",\"month\":\"May 2018\"},{\"TotalSale\":\"4133.3158\",\"TotalOrders\":\"150\",\"month\":\"April 2018\"},{\"TotalSale\":\"4030.7598\",\"TotalOrders\":\"147\",\"month\":\"March 2018\"},{\"TotalSale\":\"3628.8358\",\"TotalOrders\":\"139\",\"month\":\"February 2018\"},{\"TotalSale\":\"3117.6568\",\"TotalOrders\":\"130\",\"month\":\"January 2018\"}]}}\n";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

if(CheckNetwork.isNetworkAvailable(MainActivity.this)){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("RestaurantId", MODE_PRIVATE);
        restId = sharedPreferences.getString("Id", "");
        restName = sharedPreferences.getString("Name", "");
        Log.e(TAG, restId);
        Log.e(TAG, restName);
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(restName);
        }
        //   yearSalesList=new ArrayList<>();
        //  yearOrderList=new ArrayList<>();
        progressLayout = findViewById(R.id.progressLayout);
        mainLayout = findViewById(R.id.mainLayout);
        tCustomerImg = findViewById(R.id.totalCustomerImg);
        tOrderImg = findViewById(R.id.totalOrderImg);
        wOrderImg = findViewById(R.id.weeklyOrderImg);
        wCustomerImg = findViewById(R.id.weeklyCustomerImg);
        chart = findViewById(R.id.chart);
        graphWeek = findViewById(R.id.weekBtn);
        salesBox = findViewById(R.id.salesBtn);
        ordersBox = findViewById(R.id.ordersBtn);
        totalSaleLl = findViewById(R.id.totalSalell);
        weeklySaleLl = findViewById(R.id.weeklySaleLl);
        totalOrderLl = findViewById(R.id.totalOrderLl);
        weeklyOrderLl = findViewById(R.id.weeklyOrderLl);
        totalCustomerLl=findViewById(R.id.totalCustomerParent);
        weeklyCustomerLl=findViewById(R.id.weeklyCustomerParent);
        noDataLayout=findViewById(R.id.noDataLayout);

        totalSale = findViewById(R.id.totalSaleValue);
        totalOrders = findViewById(R.id.totalOrdersValue);
        totalCustomers = findViewById(R.id.totalCustomersValue);
        weekSale = findViewById(R.id.weekSaleValue);
        weekOrder = findViewById(R.id.weekOrderValue);
        weekCustomer = findViewById(R.id.weekCustomerValue);

        monthBtn = findViewById(R.id.monthBtn);
        //       weekBtn=findViewById(R.id.weekBtn);
        yearBtn = findViewById(R.id.yearBtn);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.inflateMenu(R.menu.bottom_navigation);
        mainNavigationView = findViewById(R.id.nav_view);
        Menu menu = mainNavigationView.getMenu();
        MenuItem sale = menu.findItem(R.id.customer_details);



   if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance=NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel=new NotificationChannel(Constants.CHANNEL_ID,Constants.CHANNEL_NAME,importance);
        notificationChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
        notificationChannel.enableLights(true);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100,200,300, 400,500, 400, 300, 200, 400});
        assert notificationManager != null;
        notificationManager.createNotificationChannel(notificationChannel);
    }

 //  MyNotificationManager.getmInstance(this).displayNotification("Hello","Hi");
  //  MyFirebaseMessagingService.s


    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        tCustomerImg.setTypeface(font);
        //sale.setIcon("\uf007");


        totalSaleLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Reports.class);
                startActivity(intent);
            }
        });

        weeklySaleLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Reports.class);
                startActivity(intent);
            }
        });

        totalOrderLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Reports.class);
                startActivity(intent);
            }
        });

        weeklyOrderLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Reports.class);
                startActivity(intent);
            }
        });

        totalCustomerLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Customer.class);
                startActivity(intent);
            }
        });
        weeklyCustomerLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Customer.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.orderList:
                        //  selectedFragment=OrderListFragment.newInstance();
                        // fragment=new OrderListFragment();
                        Intent intent = new Intent(MainActivity.this, OrderList.class);
                        startActivity(intent);
                        break;
                    case R.id.bestSeller:
                        // selectedFragment=BestSellerFragment.newInstance();
                        Intent bestSeller = new Intent(MainActivity.this, BestSeller.class);
                        startActivity(bestSeller);
                        break;
                    case R.id.topSale:
                        //selectedFragment=TopSaleFragment.newInstance();
                        Intent topSale = new Intent(MainActivity.this, TopSeller.class);
                        startActivity(topSale);
                        break;
                }
                /**   final FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                 transaction.replace(R.id.parentRelative,fragment);
                 transaction.commit();**/
                return true;
            }
        });


        chart.setDrawGridBackground(false);
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        // yAxisLeft.setLabelCount(12,false);
        // yAxisLeft.setTextSize(20f);

        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        ordersBox.setChecked(true);
        salesBox.setChecked(true);
        //xAxis.setTextSize(20f);
        xAxis.setGranularity(1f);
        //   xAxis.setGranularityEnabled(true);


        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getDescription().setEnabled(false);


        //Typeface font = Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
        tCustomerImg.setTypeface(font);
        tCustomerImg.setText("\uf007");

        wCustomerImg.setTypeface(font);
        wCustomerImg.setText("\uf007");


        tOrderImg.setTypeface(font);
        tOrderImg.setText("\uf0f5");

        wOrderImg.setTypeface(font);
        wOrderImg.setText("\uf0f5");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        mainNavigationView.setNavigationItemSelectedListener(this);

        Legend legend = chart.getLegend();
        legend.setTextSize(15f);
        salesBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    dataSets.remove(salesSet);
                    chart.setData(new LineData(dataSets));
                    chart.invalidate();
                } else {
                    dataSets.add(salesSet);
                    chart.setData(new LineData(dataSets));
                    chart.invalidate();
                }
            }
        });
        ordersBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    dataSets.remove(ordersSet);
                    chart.setData(new LineData(dataSets));
                    chart.invalidate();
                    Log.e(TAG, "Clear orders");
                } else {
                    dataSets.add(ordersSet);
                    chart.setData(new LineData(dataSets));
                    chart.invalidate();
                }
            }
        });


        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        getLables();
        graphWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Week button clicked");
                graphWeek.setBackgroundColor(Color.parseColor("#ff6501"));
                monthBtn.setBackgroundColor(Color.parseColor("#b3b3ba"));
                yearBtn.setBackgroundColor(Color.parseColor("#b3b3ba"));

                // xMonthLabel.clear();

                chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        //   Log.e(TAG,"xLabel Arraylist "+xlabel.get((int)value));

                        String val = null;
                        Log.e(TAG, String.valueOf(xWeekLabel.size()));
                        try {
                            return xWeekLabel.get((int) value);
                        } catch (Exception e) {
                            // return "";
                        }
                        return "";
                    }

                    public int getDecimalPoints() {
                        return 0;
                    }
                });
                dataSets = new ArrayList<>();
                salesSet = new LineDataSet(weekSalesList, "Sales");
                salesSet.setValueFormatter(new MyValueFormatter());
                salesSet.setValueFormatter(new SalesValueFormatter());
                ordersSet = new LineDataSet(weekOrdersList, "Orders");
                ordersSet.setValueFormatter(new SalesValueFormatter());
                salesSet.setColor(Color.parseColor("#87de75"));
                salesSet.setCircleColor(Color.parseColor("#87de75"));
                salesSet.setCircleColorHole(Color.parseColor("#87de75"));
                salesSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                salesSet.setDrawFilled(true);
                salesSet.setFillColor(Color.parseColor("#87de75"));
                ordersSet.setColor(Color.parseColor("#ed7f7e"));
                ordersSet.setCircleColor(Color.parseColor("#ed7f7e"));
                ordersSet.setCircleColorHole(Color.parseColor("#ed7f7e"));
                ordersSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                ordersSet.setDrawFilled(true);
                ordersSet.setFillColor(Color.parseColor("#ed7f7e"));
                if (salesBox.isChecked() && ordersBox.isChecked()) {
                    dataSets.add(salesSet);
                    dataSets.add(ordersSet);
                    chart.setData(new LineData(dataSets));

                }
                if (salesBox.isChecked() && !ordersBox.isChecked()) {
                    dataSets.add(salesSet);
                    chart.setData(new LineData(dataSets));
                }
                if (ordersBox.isChecked() && !salesBox.isChecked()) {
                    dataSets.add(ordersSet);
                    Log.e(TAG, "dataset" + dataSets);
                    chart.setData(new LineData(dataSets));
                }
                chart.setExtraOffsets(10, 10, 20, 10);
                chart.invalidate();
                // xAxis.setAvoidFirstLastClipping(true);
                // xAxis.setXOffset(2);
            }
        });

    }
}


    private void getLables() {
        final String MAIN = "http://business.foodypos.com/App/Api.asmx/dashboard?RestaurantId="+restId;
        StringRequest stringRequest=new StringRequest(Request.Method.GET,MAIN,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
      Log.e(TAG,response);
      ExecutePosts posts=new ExecutePosts();
      posts.execute(response);
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
      Log.e(TAG,error.toString());
        }
    };




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        //  super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            finishAffinity();
        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

       /** if (id == R.id.action_settings) {
            return true;
        }**/
       switch (id){
           case R.id.changePassword:
               Intent intent=new Intent(MainActivity.this,ChangePassword.class);
               startActivity(intent);
               break;
           case R.id.logout:
               //webservice for logout - sharedPrefernces
               SharedPreferences sharedPreferences=getSharedPreferences("Login",MODE_PRIVATE);
               SharedPreferences.Editor editor=sharedPreferences.edit();
               editor.clear();
               editor.apply();
               finish();
               Intent intentLogout=new Intent(MainActivity.this,Login.class);
               startActivity(intentLogout);
       }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.dashboard:
                Intent intentDashboard=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intentDashboard);
                break;
            case R.id.orderList:
                Intent intentOrderList=new Intent(MainActivity.this,OrderList.class);
                startActivity(intentOrderList);

                break;
            case R.id.customer_details:
                Intent intentCustomer=new Intent(MainActivity.this,Customer.class);
                startActivity(intentCustomer);

                //new activity with fields similar to TopSeller, but datewise customer details, bt topseller shows top 3 Customer
              //  Intent intentCustomer=new Intent(MainActivity.this,TopSeller.class);
               // startActivity(intentCustomer);
                break;
            case R.id.reports:
                Intent reportsIntent=new Intent(MainActivity.this,Reports.class);
                startActivity(reportsIntent);

                break;
            case R.id.employee:
                Intent intentEmployee=new Intent(MainActivity.this,EmployeeDetails.class);
                startActivity(intentEmployee);
                break;

            case R.id.changePassword:
                Intent intentChangePassword=new Intent(MainActivity.this,ChangePassword.class);
                startActivity(intentChangePassword);
                break;
            case R.id.logout:
                //webservice for user logout using sharedprefernces
                SharedPreferences sharedPreferences=getSharedPreferences("Login",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent intentLogout=new Intent(MainActivity.this,Login.class);
                startActivity(intentLogout);
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    public void monthGraph(View view) {
        Log.e(TAG, "Month button tapped");
        monthBtn.setBackgroundColor(Color.parseColor("#ff6501"));
        graphWeek.setBackgroundColor(Color.parseColor("#b3b3ba"));
        yearBtn.setBackgroundColor(Color.parseColor("#b3b3ba"));
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //   Log.e(TAG,"xLabel Arraylist "+xlabel.get((int)value));
                String val = null;
                Log.e(TAG, String.valueOf(xMonthLabel.size()));
                try {
                    return xMonthLabel.get((int) value);
                }
                catch (Exception e){
                    Log.e(TAG,"Exception in setting xMonth label");
                }
               return "";
            }

            public int getDecimalPoints() {
                return 0;
            }
        });

        dataSets = new ArrayList<>();
        salesSet = new LineDataSet(monthSalesList, "Sales");
        salesSet.setValueFormatter(new MyValueFormatter());
        salesSet.setValueFormatter(new SalesValueFormatter());
        ordersSet = new LineDataSet(monthOrdersList, "Orders");
        ordersSet.setValueFormatter(new SalesValueFormatter());
        salesSet.setColor(Color.parseColor("#87de75"));
        salesSet.setCircleColor(Color.parseColor("#87de75"));
        salesSet.setCircleColorHole(Color.parseColor("#87de75"));
        salesSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        salesSet.setDrawFilled(true);
        salesSet.setFillColor(Color.parseColor("#87de75"));
        ordersSet.setColor(Color.parseColor("#ed7f7e"));
        ordersSet.setCircleColor(Color.parseColor("#ed7f7e"));
        ordersSet.setCircleColorHole(Color.parseColor("#ed7f7e"));
        ordersSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ordersSet.setDrawFilled(true);
        ordersSet.setFillColor(Color.parseColor("#ed7f7e"));
        if (salesBox.isChecked() && ordersBox.isChecked()) {
            dataSets.add(salesSet);
            dataSets.add(ordersSet);
            chart.setData(new LineData(dataSets));
        }
        if (salesBox.isChecked() && !ordersBox.isChecked()) {
            dataSets.add(salesSet);
            chart.setData(new LineData(dataSets));
        }
        if (ordersBox.isChecked() && !salesBox.isChecked()) {
            dataSets.add(ordersSet);
            Log.e(TAG, "dataset" + dataSets);
            chart.setData(new LineData(dataSets));
        }
        chart.setExtraOffsets(10, 10, 20, 10);
        chart.invalidate();
        //dataSets.clear();
    }




    public void yearGraph(View view) {
        Log.e(TAG, "Month button tapped");
        yearBtn.setBackgroundColor(Color.parseColor("#ff6501"));
        monthBtn.setBackgroundColor(Color.parseColor("#b3b3ba"));
        graphWeek.setBackgroundColor(Color.parseColor("#b3b3ba"));
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //   Log.e(TAG,"xLabel Arraylist "+xlabel.get((int)value));
                String val = null;
try {
    return xYearLabel.get((int) value);
}
catch (Exception e){
    Log.e(TAG,"Exception in setting xYearLabel");
}
return "";
            }

            public int getDecimalPoints() {
                return 0;
            }
        });

        dataSets = new ArrayList<>();
        salesSet = new LineDataSet(yearSalesList, "Sales");
        salesSet.setValueFormatter(new MyValueFormatter());
        salesSet.setValueFormatter(new SalesValueFormatter());
        ordersSet = new LineDataSet(yearOrderList, "Orders");
        ordersSet.setValueFormatter(new SalesValueFormatter());
        salesSet.setColor(Color.parseColor("#87de75"));
        salesSet.setCircleColor(Color.parseColor("#87de75"));
        salesSet.setCircleColorHole(Color.parseColor("#87de75"));
        salesSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        salesSet.setDrawFilled(true);
        salesSet.setFillColor(Color.parseColor("#87de75"));
        ordersSet.setColor(Color.parseColor("#ed7f7e"));
        ordersSet.setCircleColor(Color.parseColor("#ed7f7e"));
        ordersSet.setCircleColorHole(Color.parseColor("#ed7f7e"));
        ordersSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ordersSet.setDrawFilled(true);
        ordersSet.setFillColor(Color.parseColor("#ed7f7e"));
        if (salesBox.isChecked() && ordersBox.isChecked()) {
            dataSets.add(salesSet);
            dataSets.add(ordersSet);
            chart.setData(new LineData(dataSets));
        }
        if (salesBox.isChecked() && !ordersBox.isChecked()) {
            dataSets.add(salesSet);
            chart.setData(new LineData(dataSets));
        }
        if (ordersBox.isChecked() && !salesBox.isChecked()) {
            dataSets.add(ordersSet);
            Log.e(TAG, "dataset" + dataSets);
            chart.setData(new LineData(dataSets));
        }
        chart.setExtraOffsets(10, 10, 20, 10);
        chart.invalidate();
        //dataSets.clear();



    }//yearGraph function ends

    private class MyValueFormatter implements com.github.mikephil.charting.formatter.IValueFormatter {
      //  private DecimalFormat decimalFormat;
        private java.text.DecimalFormat decimalFormat;

      //  @RequiresApi(api = Build.VERSION_CODES.N)
        public MyValueFormatter(){
        //    decimalFormat=new DecimalFormat("###,###,##0.00");
            decimalFormat=new java.text.DecimalFormat("###,###,##0.00");
        }
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return decimalFormat.format(value);
        }
    }

    private class MyMarkerView extends MarkerView implements IMarker{
        private TextView tvContentMarker;
        private MPPointF mOffset;

        public MyMarkerView(Context context, int layoutResource) {
            super(context,layoutResource);
            tvContentMarker=findViewById(R.id.tvContent);
        }

        @SuppressLint("SetTextI18n")
        public void refreshContent(Entry e, Highlight highlight){
            tvContentMarker.setText(""+e.getY());
        }

        public MPPointF getOffset(){
            if(mOffset==null){
                mOffset=new MPPointF(-(getWidth()/2),-getHeight());
            }
            return mOffset;
        }

    }

    private class ExecutePosts extends AsyncTask<String,Void,Wrapper> {
        String totalSales, totalOrder, totalCustomer, weeklySale, weeklyOrder, weeklyCustomer;
        ArrayList<String> labelsList;
        int v, flagResult = 1;

        @Override
        protected Wrapper doInBackground(String... response) {
            try {
                JSONObject jsonObject = new JSONObject(response[0]);
                // if(jsonObject.getString("Message").equals("Sorry, An Execption occurred")){
                if (jsonObject.has("Message")) {
                    flagResult = 0;
                } else {
                    // flagResult = 1;

                    JSONObject jsonObject1 = jsonObject.getJSONObject("LabelValues");
                    totalSales = jsonObject1.getString("TotalSale");
                    totalOrder = jsonObject1.getString("TotalOrders");
                    totalCustomer = jsonObject1.getString("TotalCustomers");
                    weeklySale = jsonObject1.getString("Weeksale");
                    weeklyOrder = jsonObject1.getString("WeeklyOrder");
                    weeklyCustomer = jsonObject1.getString("WeekCustomer");

                    JSONObject jsonObject2 = jsonObject.getJSONObject("chart");
                    weekSalesList = new ArrayList<>();
                    weekOrdersList = new ArrayList<>();
                    xWeekLabel = new ArrayList<>();
                    monthSalesList = new ArrayList<>();
                    monthOrdersList = new ArrayList<>();
                    xMonthLabel = new ArrayList<>();
                    yearSalesList = new ArrayList<>();
                    yearOrderList = new ArrayList<>();
                    xYearLabel = new ArrayList<>();
                    JSONArray weekArray = jsonObject2.getJSONArray("Week");
                    weekSalesList.add(new Entry(0, 0));
                    weekOrdersList.add(new Entry(0, 0));
                    xWeekLabel.add("0");
                    int m = 1;
                    for (int i = weekArray.length() - 1; i >= 0; i--) {
                        v = i;
                        JSONObject weekJsonObject = weekArray.getJSONObject(i);
                        String weekX = weekJsonObject.getString("WeekDay");
                        String weekOrders = weekJsonObject.getString("TotalOrders");
                        float fWeekOrders = Float.valueOf(weekOrders);
                        String weekSale = weekJsonObject.getString("TotalSale");
                        float fWeekSales = Float.valueOf(weekSale);
                        float roundedfWeekSales = Math.round(fWeekSales);
                        int roundediWeekSales = (int) roundedfWeekSales;
                        weekSalesList.add(new Entry(m, roundediWeekSales));
                        weekOrdersList.add(new Entry(m, fWeekOrders));
                        m++;
                        String[] weekXLabels = weekX.split(" ");
                        String weekDate = weekXLabels[0];
                        String weekDay = weekXLabels[1];
                        String day = weekDay.substring(0, 3);
                        String xAxisValue = weekDate + " " + day;
                        xWeekLabel.add(xAxisValue);

                    }

                    JSONArray monthArray = jsonObject2.getJSONArray("Month");
                    monthSalesList.add(new Entry(0, 0));
                    monthOrdersList.add(new Entry(0, 0));
                    xMonthLabel.add("0");
                    int l = 1;
                    for (int j = monthArray.length() - 1; j >= 0; j--) {
                        //    for (int k = 1; k <= monthArray.length(); k++) {
                        JSONObject monthJsonObject = monthArray.getJSONObject(j);
                        String monthX = monthJsonObject.getString("Daydate");
                        String monthOrders = monthJsonObject.getString("TotalOrders");
                        float fMonthOrders = Float.valueOf(monthOrders);
                        String monthSales = monthJsonObject.getString("TotalSale");
                        float fMonthSales = Float.valueOf(monthSales);
                        float roundedfMonthSale = (float) (Math.round(fMonthSales));
                        int roundedValue = (int) roundedfMonthSale;
                        monthSalesList.add(new Entry(l, roundedValue));
                        monthOrdersList.add(new Entry(l, fMonthOrders));
                        l++;
                        String[] monthXLabels = monthX.split(" ");
                        String monthDate = monthXLabels[0];
                        String monthName = monthXLabels[1];
                        String month = monthName.substring(0, 3);
                        String xMonthValue = monthDate + " " + month;
                        xMonthLabel.add(xMonthValue);
                        // }
                    }

                    JSONArray yearArray = jsonObject2.getJSONArray("Year");
                    yearSalesList.add(new Entry(0, 0));
                    yearOrderList.add(new Entry(0, 0));
                    xYearLabel.add("0");
                    int n = 1;
                    for (int k = yearArray.length() - 1; k >= 0; k--) {
                        JSONObject yearJsonObject = yearArray.getJSONObject(k);
                        String yearX = yearJsonObject.getString("Month");
                        String yearOrders = yearJsonObject.getString("TotalOrders");
                        float orders = Float.valueOf(yearOrders);
                        String yearSales = yearJsonObject.getString("TotalSale");
                        float sales = Float.valueOf(yearSales);
                        float fYearSales = Math.round(sales);
                        int roundedFYearSales = (int) fYearSales;
                        yearSalesList.add(new Entry(n, roundedFYearSales));
                        yearOrderList.add(new Entry(n, orders));
                        n++;
                        String[] xLables = yearX.split(" ");
                        String month = xLables[0];
                        String year = xLables[1];
                        String mon = month.substring(0, 3);
                        String yea = year.substring(2);
                        String xAxis = mon + "-" + yea;
                        xYearLabel.add(xAxis);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            labelsList = new ArrayList<>();
            labelsList.add(totalSales);
            labelsList.add(totalOrder);
            labelsList.add(totalCustomer);
            labelsList.add(weeklySale);
            labelsList.add(weeklyOrder);
            labelsList.add(weeklyCustomer);
            Wrapper w = new Wrapper();
            w.labels = labelsList;
            w.weekSalesList = weekSalesList;
            w.weekOrderList = weekOrdersList;
            w.xWeeksLabel = xWeekLabel;

            return w;
        }

        public void onPostExecute(Wrapper wrapper) {
            super.onPostExecute(wrapper);
       //     if (isNetworkAvailable()) {
                if (flagResult == 1) {
                    progressLayout.setVisibility(View.INVISIBLE);
                    mainLayout.setVisibility(View.VISIBLE);
                    totalSale.setText(labelsList.get(0));
                    graphWeek.setBackgroundColor(Color.parseColor("#ff6501"));
                    totalOrders.setText(labelsList.get(1));
                    totalCustomers.setText(labelsList.get(2));
                    weekSale.setText(labelsList.get(3));
                    weekOrder.setText(labelsList.get(4));
                    weekCustomer.setText(labelsList.get(5));

                    chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            //   Log.e(TAG,"xLabel Arraylist "+xlabel.get((int)value));
                            String val = null;
                            Log.e(TAG, String.valueOf(xWeekLabel.size()));
                            try {
                                return xWeekLabel.get((int) value);
                            } catch (Exception e) {
                                Log.e(TAG, "Exception in week while loading in background");
                            }

                            return "";
                        }

                        public int getDecimalPoints() {
                            return 0;
                        }
                    });

                    dataSets = new ArrayList<>();
                    salesSet = new LineDataSet(weekSalesList, "Sales");
                    salesSet.setValueFormatter(new MyValueFormatter());
                    salesSet.setValueFormatter(new SalesValueFormatter());
                    ordersSet = new LineDataSet(weekOrdersList, "Orders");
                    ordersSet.setValueFormatter(new SalesValueFormatter());
                    salesSet.setColor(Color.parseColor("#87de75"));
                    salesSet.setCircleColor(Color.parseColor("#87de75"));
                    salesSet.setCircleColorHole(Color.parseColor("#87de75"));
                    salesSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    salesSet.setDrawFilled(true);
                    salesSet.setFillColor(Color.parseColor("#87de75"));
                    ordersSet.setColor(Color.parseColor("#ed7f7e"));
                    ordersSet.setCircleColor(Color.parseColor("#ed7f7e"));
                    ordersSet.setCircleColorHole(Color.parseColor("#ed7f7e"));
                    ordersSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    ordersSet.setDrawFilled(true);
                    ordersSet.setFillColor(Color.parseColor("#ed7f7e"));
                    if (salesBox.isChecked() && ordersBox.isChecked()) {
                        dataSets.add(salesSet);
                        dataSets.add(ordersSet);
                        chart.setData(new LineData(dataSets));
                    }
                    if (salesBox.isChecked() && !ordersBox.isChecked()) {
                        dataSets.add(salesSet);
                        chart.setData(new LineData(dataSets));
                    }
                    if (ordersBox.isChecked() && !salesBox.isChecked()) {
                        dataSets.add(ordersSet);
                        Log.e(TAG, "dataset" + dataSets);
                        chart.setData(new LineData(dataSets));
                    }
                    chart.setExtraOffsets(10, 10, 20, 10);
                    chart.invalidate();
                    //     xWeekLabel.clear();
                    // xAxis.setAvoidFirstLastClipping(true);
                    // xAxis.setXOffset(2);
                } else if (flagResult == 0) {
                    //Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_LONG).show();
                    Log.e(TAG,"Server error");
                    new AlertDialog.Builder(MainActivity.this)
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
          //  }
         //   else {
           //     displayAlert();
            //}

        }

    }

}
