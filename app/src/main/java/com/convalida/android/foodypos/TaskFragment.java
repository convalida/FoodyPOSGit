package com.convalida.android.foodypos;

import android.annotation.SuppressLint;
import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

   static ArrayList<Entry> monthOrdersList, monthSalesList, yearOrderList, yearSalesList;
          static ArrayList<Entry>  weekSalesList ,weekOrdersList;
   static ArrayList<String> xWeekLabel = new ArrayList<>();
   static ArrayList<String> xMonthLabel = new ArrayList<>();
    static ArrayList<String> xYearLabel = new ArrayList<>();

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
    private android.support.v4.app.Fragment fragment;
    RecyclerView recyclerView;
    ArrayList<String> heading;
    XAxis xAxis;
    Menu menu;
    String restId, restName, roleType;
    static ArrayList<String> labelsList;
    boolean isTaskExecuting=false;


    interface TaskCallbacks {
        void onPostExecute();
        void onPostFailure();
    }

    private TaskCallbacks taskCallbacks;
    private ExecutePosts executePosts;

    public void onAttach(Context context) {
        super.onAttach(context);
        taskCallbacks = (TaskCallbacks) context;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    public void onDetach() {
        super.onDetach();
        taskCallbacks = null;
       /** try {
            Field childFragmentManager=Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this,null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }**/
    }

    public void startBackgroundTask(){
        if(!isTaskExecuting){
            String param1=null;
            if(getArguments()!=null){
                param1=getArguments().getString("response");
            }
            executePosts = new ExecutePosts();
            executePosts.execute(param1);
        }

    }

    public void updateExecutingStatus(boolean isExecuting){
        this.isTaskExecuting=isExecuting;
    }

    private class MyValueFormatter implements com.github.mikephil.charting.formatter.IValueFormatter {
        //  private DecimalFormat decimalFormat;
        private java.text.DecimalFormat decimalFormat;

        //  @RequiresApi(api = Build.VERSION_CODES.N)
        public MyValueFormatter() {
            //    decimalFormat=new DecimalFormat("###,###,##0.00");
            decimalFormat = new java.text.DecimalFormat("###,###,##0.00");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return decimalFormat.format(value);
        }
    }

    private class MyMarkerView extends MarkerView implements IMarker {
        private TextView tvContentMarker;
        private MPPointF mOffset;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContentMarker = findViewById(R.id.tvContent);
        }

        @SuppressLint("SetTextI18n")
        public void refreshContent(Entry e, Highlight highlight) {
            tvContentMarker.setText("" + e.getY());
        }

        public MPPointF getOffset() {
            if (mOffset == null) {
                mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
            }
            return mOffset;
        }

    }


    private class ExecutePosts extends AsyncTask<String, Void, Wrapper> {
        String totalSales, totalOrder, totalCustomer, weeklySale, weeklyOrder, weeklyCustomer;

        int v, flagResult = 1;

        @Override
        protected Wrapper doInBackground(String... response) {
            try {
                JSONObject jsonObject = new JSONObject(response[0]);
                // if(jsonObject.getString("Message").equals("Sorry, An Execption occurred")){
                if (jsonObject.has("Message")) {
                    flagResult = 0;
                } else {
                     flagResult = 1;
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
            if(taskCallbacks!=null) {
                if (flagResult == 1) {
                    taskCallbacks.onPostExecute();
                } else if (flagResult == 0) {
                    taskCallbacks.onPostFailure();
                }
            }
            //     if (isNetworkAvailable()) {
        /**    if (flagResult == 1) {
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
                Log.e(TAG, "Server error");
                new AlertDialog.Builder(getActivity())
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
            }**/
            //  }
            //   else {
            //     displayAlert();
            //}

        }

    }
}