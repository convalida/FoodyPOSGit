package com.convalida.android.foodypos;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.android.volley.AuthFailureError;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskFragment.TaskCallbacks {
    //  Button btn;
    TextView tCustomerImg, tOrderImg, wOrderImg, wCustomerImg;
    TextView monthBtn, yearBtn, graphWeek;
    LinearLayout totalSaleLl, weeklySaleLl, totalOrderLl, weeklyOrderLl, totalCustomerLl, weeklyCustomerLl;
    TextView totalSale, totalOrders, totalCustomers, weekSale, weekOrder, weekCustomer;
    RelativeLayout progressLayout, mainLayout, noDataLayout;
    private TaskFragment taskFragment;
    private static final String TAG_TASK_FRAGMENT = "task_fragment";

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
    Menu menu;
    String restId, restName, roleType;
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

        if (CheckNetwork.isNetworkAvailable(MainActivity.this)) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            mainNavigationView = findViewById(R.id.nav_view);
            menu = mainNavigationView.getMenu();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("RestaurantId", MODE_PRIVATE);
            restId = sharedPreferences.getString("Id", "");
            restName = sharedPreferences.getString("Name", "");
            roleType = sharedPreferences.getString("RoleType", "");
            if (roleType.equals("Employee")) {
                hideItem();
            }
            MenuItem item = menu.findItem(R.id.dashboard);
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
            totalCustomerLl = findViewById(R.id.totalCustomerParent);
            weeklyCustomerLl = findViewById(R.id.weeklyCustomerParent);
            noDataLayout = findViewById(R.id.noDataLayout);

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

            //    hideItem();
            MenuItem sale = menu.findItem(R.id.customer_details);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
                notificationChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
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
                    Intent intent = new Intent(MainActivity.this, Customer.class);
                    startActivity(intent);
                }
            });
            weeklyCustomerLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Customer.class);
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
                            Log.e(TAG, String.valueOf(TaskFragment.xWeekLabel.size()));
                            try {
                                Log.e(TAG,String.valueOf(TaskFragment.xWeekLabel.get((int) value)));
                                return TaskFragment.xWeekLabel.get((int) value);
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
                    salesSet = new LineDataSet(TaskFragment.weekSalesList, "Sales");
                    salesSet.setValueFormatter(new MyValueFormatter());
                    salesSet.setValueFormatter(new SalesValueFormatter());
                    ordersSet = new LineDataSet(TaskFragment.weekOrdersList, "Orders");
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

    /**
     * public void onNavigationItemSelected(MenuItem item){
     * int id=item.getItemId();
     * if(id==R.id.dashboard){
     * item.setChecked(false);
     * }
     * }
     **/


    private void hideItem() {
        menu.findItem(R.id.employee).setVisible(false);
    }


    private void getLables() {
        final String MAIN = Constants.BASE_URL+"dashboard?RestaurantId=" + restId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MAIN, onPostsLoaded, onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG, response);
           //   ExecutePosts posts=new ExecutePosts();
            //  posts.execute(response);
            FragmentManager fragmentManager = getSupportFragmentManager();
            taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
            Bundle bundle = new Bundle();
            bundle.putString("response", response);

            if (taskFragment == null) {
           //     try {
                    taskFragment = new TaskFragment();
                    taskFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commitAllowingStateLoss();
              /**  }
                catch (Exception e){
                    e.printStackTrace();
                }**/
            }
       //     if(taskFragment!=null){
                taskFragment.startBackgroundTask();
         //   }
        }
    };
    Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, error.toString());
        }
    };


    boolean doubleBackToExitPressedOnce = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //  super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // finishAffinity();
            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
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
        switch (id) {
            case R.id.changePassword:
                Intent intent = new Intent(MainActivity.this, ChangePassword.class);
                startActivity(intent);
                break;
            case R.id.logout:
                //webservice for logout - sharedPrefernces

                logoutUser();


                //Intent intentLogout=new Intent(MainActivity.this,Login.class);
                // startActivity(intentLogout);
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        final String token;

        token = SharedPrefManagerToken.getmInstance(this).getDeviceToken();

        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        finish();
        final String logoutUrl = Constants.BASE_URL+"LogoutByApp";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, logoutUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String resultCode = jsonObject.getString("result");
                    String message = jsonObject.getString("message");
                    if (resultCode.equals("1")) {

                        Toast.makeText(getApplicationContext(), "User logged out successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("deviceId", token);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        Intent intentLogout = new Intent(MainActivity.this, Login.class);
        startActivity(intentLogout);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // item.setChecked(false);
        switch (id) {
            case R.id.dashboard:
                Intent intentDashboard = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intentDashboard);
                break;
            case R.id.orderList:
                //  item.setChecked(false);
                Intent intentOrderList = new Intent(MainActivity.this, OrderList.class);
                startActivity(intentOrderList);

                break;
            case R.id.customer_details:
                Intent intentCustomer = new Intent(MainActivity.this, Customer.class);
                startActivity(intentCustomer);
                // item.setChecked(false);
                //new activity with fields similar to TopSeller, but datewise customer details, bt topseller shows top 3 Customer
                //  Intent intentCustomer=new Intent(MainActivity.this,TopSeller.class);
                // startActivity(intentCustomer);
                break;
            case R.id.reports:
                Intent reportsIntent = new Intent(MainActivity.this, Reports.class);
                startActivity(reportsIntent);
                // item.setChecked(false);
                break;
            case R.id.employee:
                Intent intentEmployee = new Intent(MainActivity.this, EmployeeDetails.class);
                startActivity(intentEmployee);
                // item.setChecked(false);
                break;

            case R.id.changePassword:
                Intent intentChangePassword = new Intent(MainActivity.this, ChangePassword.class);
                startActivity(intentChangePassword);
                // item.setChecked(false);
                break;
            case R.id.logout:
                //webservice for user logout using sharedprefernces
                /**     SharedPreferences sharedPreferences=getSharedPreferences("Login",MODE_PRIVATE);
                 SharedPreferences.Editor editor=sharedPreferences.edit();
                 editor.clear();
                 editor.apply();
                 finish();
                 Intent intentLogout=new Intent(MainActivity.this,Login.class);
                 startActivity(intentLogout);**/
                logoutUser();
                // item.setChecked(false);
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
                Log.e(TAG, String.valueOf(TaskFragment.xMonthLabel.size()));
                try {
                    return TaskFragment.xMonthLabel.get((int) value);
                } catch (Exception e) {
                    Log.e(TAG, "Exception in setting xMonth label");
                }
                return "";
            }

            public int getDecimalPoints() {
                return 0;
            }
        });

        dataSets = new ArrayList<>();
        salesSet = new LineDataSet(TaskFragment.monthSalesList, "Sales");
        salesSet.setValueFormatter(new MyValueFormatter());
        salesSet.setValueFormatter(new SalesValueFormatter());
        ordersSet = new LineDataSet(TaskFragment.monthOrdersList, "Orders");
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
                    return TaskFragment.xYearLabel.get((int) value);
                } catch (Exception e) {
                    Log.e(TAG, "Exception in setting xYearLabel");
                }
                return "";
            }

            public int getDecimalPoints() {
                return 0;
            }
        });

        dataSets = new ArrayList<>();
        salesSet = new LineDataSet(TaskFragment.yearSalesList, "Sales");
        salesSet.setValueFormatter(new MyValueFormatter());
        salesSet.setValueFormatter(new SalesValueFormatter());
        ordersSet = new LineDataSet(TaskFragment.yearOrderList, "Orders");
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




     @Override
    public void onPostExecute() {
     //   super.onPostExecute(wrapper);
         if(taskFragment!=null){
             taskFragment.updateExecutingStatus(false);
         }
        progressLayout.setVisibility(View.INVISIBLE);
        mainLayout.setVisibility(View.VISIBLE);

        totalSale.setText(TaskFragment.labelsList.get(0));
        graphWeek.setBackgroundColor(Color.parseColor("#ff6501"));
        totalOrders.setText(TaskFragment.labelsList.get(1));
        totalCustomers.setText(TaskFragment.labelsList.get(2));
        weekSale.setText(TaskFragment.labelsList.get(3));
        weekOrder.setText(TaskFragment.labelsList.get(4));
        weekCustomer.setText(TaskFragment.labelsList.get(5));

        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //   Log.e(TAG,"xLabel Arraylist "+xlabel.get((int)value));
                String val = null;
                Log.e(TAG, String.valueOf(TaskFragment.xWeekLabel.size()));
                try {
                    return TaskFragment.xWeekLabel.get((int) value);
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
        salesSet = new LineDataSet(TaskFragment.weekSalesList, "Sales");
        salesSet.setValueFormatter(new MyValueFormatter());
        salesSet.setValueFormatter(new SalesValueFormatter());
        ordersSet = new LineDataSet(TaskFragment.weekOrdersList, "Orders");
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
    }

    @Override
    public void onPostFailure() {
        Log.e(TAG, "Server error");
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


}
