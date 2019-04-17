package com.convalida.android.foodypos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
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

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class OrderList extends AppCompatActivity {

    public String user_id;
    // List<OrderlistData> data = new ArrayList<>();
    ExpandableList expandableListAdapter;
    private LinkedHashMap<String, HeaderInfo> details=new LinkedHashMap<>();
    private ArrayList<HeaderInfo> headerList;
    TextView orders,amount;
    List<String> headers;
    HashMap<String,List<String>> children;
   // String custName,email,num;
   // ArrayList<OrderlistData> data=new ArrayList<>();
   // ArrayList<NewOrderlistData> newData=new ArrayList<>();
   // ArrayList<List<OrderlistData>> dataArray=new ArrayList<>();
    String fromClick, toClick, restId,startDate,endDate;
  //  ImageButton person,orderDetails;
    private static final String TAG="OrderList";
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog startDateDialog, endDateDialog;
    TextView fromDate, toDate;
    RelativeLayout progress, noDataLayout;
    LinearLayout mainLayout;
    String orderNum;
    ArrayList<String> orderNumList;
   // OrderListAdapter adapter;
   // NewOrderListAdapter newAdapter;
    RecyclerView recyclerView;
    ArrayList<DetailChildInfo> ordersOnADay=new ArrayList<>();
    RequestQueue requestQueue;
    ArrayList<ArrayList<String>> parentArray;
    Gson gson;
    Button dateBtn;
    Date date1,date2;
  //  ArrayList subitemsArrayList;
  //  SharedPreferences preferences;
    //OrderlistData result;
    String fromdateSaved, todateSaved;
    ExpandableListView expandableListView;

  // SharedPreferences.Editor editor;
    @Override
 /**   public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

      /**  final ArrayList<OrderlistData> filteredList=filter(data,newText);
        adapter.setmFilter(filteredList);
        final ArrayList<NewOrderlistData> filteredList=filter(newData,newText);
        newAdapter.setmFilter(filteredList);**/


     //  final ArrayList<DetailChildInfo> filteredList=filter(ordersOnADay,newText);
       // expandableListAdapter.setmFilter(filteredList);

    /**    return false;
    }

    private ArrayList<DetailChildInfo> filter(ArrayList<DetailChildInfo> ordersOnADay, String newText) {
        final ArrayList<DetailChildInfo> filteredList=new ArrayList<>();
        for(DetailChildInfo listData:ordersOnADay){
            final String orderNum=listData.getOrderNum();
            if(orderNum.contains(newText)){
                filteredList.add(listData);
            }
        }
        return filteredList;
    }

    /**   private ArrayList<OrderlistData> filter(ArrayList<OrderlistData> data, String newText) {
        final ArrayList<OrderlistData> filteredList=new ArrayList<>();
        for(OrderlistData listData:data){
            final String orderNum=listData.getOrderNo();
            if(orderNum.contains(newText)){
                filteredList.add(listData);
            }
        }
        return filteredList;
    }**/
 /** private ArrayList<NewOrderlistData> filter(ArrayList<NewOrderlistData> newData, String newText){
      final ArrayList<NewOrderlistData> filteredList=new ArrayList<>();
      for(NewOrderlistData newListData:newData){
          final String neworderNum=newListData.getOrderNo();
          if(neworderNum.contains(newText)){
              filteredList.add(newListData);
          }
      }
      return filteredList;
  }**/


    protected void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        state.putSerializable("From date saved",fromDate.getText().toString());
        state.putSerializable("To date saved",toDate.getText().toString());
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = this.getResources().getConfiguration().orientation;
        //   if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        setContentView(R.layout.activity_order_list);
        // } else {
        //   setContentView(R.layout.sales_landscapemode);
        //}
        //    setContentView(R.layout.activity_order_list);
        fromDate = findViewById(R.id.From);
        toDate = findViewById(R.id.To);
   /**     if(getIntent().getExtras()!=null){
            String title = Objects.requireNonNull(getIntent().getExtras()).getString("title");
            String msg = Objects.requireNonNull(getIntent().getExtras()).getString("body");
            assert msg != null;
            String[] individualStrings = msg.split(" ");
            String order = individualStrings[1];
            String orderNum = order.substring(1);

            Intent intent = new Intent(getApplicationContext(), OnClickOrder.class);
            //  intent.putExtra("Msg",msg);
            intent.putExtra("Order num", orderNum);


            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
            @SuppressLint("ResourceType") Notification notification = new NotificationCompat.Builder(this, "my_channel_01")
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.icon)
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
            // NotificationManager managerCompat= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            managerCompat.notify(123, notification);
        }**/

        if (CheckNetwork.isNetworkAvailable(OrderList.this)) {
            Toolbar toolbar=findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            orders = findViewById(R.id.orders);
            amount = findViewById(R.id.amountValue);
            requestQueue = Volley.newRequestQueue(OrderList.this);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            mainLayout = findViewById(R.id.mainOrderlistLayout);
            progress = findViewById(R.id.orderProgress);
            noDataLayout=findViewById(R.id.noDataLayout);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("RestaurantId", MODE_PRIVATE);
            restId = sharedPreferences.getString("Id", "");
            //  search=findViewById(R.id.search);

            if (getSupportActionBar() != null) {
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle("Order List");
            }
            expandableListView = findViewById(R.id.expadableList);
            //     prepareData();
            /**   try {
             parseJson();
             } catch (Exception e) {
             e.printStackTrace();
             }**/


            expandableListView.setOnChildClickListener(myListItemClicked);
            expandableListView.setOnGroupClickListener(myListGroupClicked);

            //    expandableListAdapter=new ExpandableList(this,headers,children);

            //   expandAll();
            simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
            // preferences=getApplicationContext().getSharedPreferences("DatePreferences",MODE_PRIVATE);



            /** fromDate.setText("mm-dd-yyyy");
             toDate.setText("mm-dd-yyyy");**/
            /**    if(preferences.contains("From date")){
             String savedFromDate=preferences.getString("From date","");
             //   fromDate.setText(simpleDateFormat.format(savedFromDate));
             simpleDateFormat.format(savedFromDate);
             Log.e(TAG,"From date: "+savedFromDate);
             }
             else{
             fromDate.setText("mm-dd-yyyy");
             }
             if(preferences.contains("To date")){
             String savedToDate=preferences.getString("To date","");
             // toDate.setText(simpleDateFormat.format(savedToDate));
             Log.e(TAG,"To date: "+savedToDate);
             }
             else{
             toDate.setText("mm-dd-yyyy");
             }**/
            /** if(savedInstanceState!=null) {
             if (savedInstanceState.getSerializable("From date saved") != "mm-dd-yyyy") {
             fromDate.setText((CharSequence) savedInstanceState.getSerializable("From date saved"));
             fromDate.setTextColor(Color.parseColor("#000000"));
             } else {
             fromDate.setText("mm-dd-yyyy");
             }
             if (savedInstanceState.getSerializable("To date saved") != "mm-dd-yyyy") {
             toDate.setText((CharSequence) savedInstanceState.getSerializable("To date saved"));
             toDate.setTextColor(Color.parseColor("#000000"));
             } else {
             toDate.setText("mm-dd-yyyy");
             }
             }else{
             fromDate.setText("mm-dd-yyyy");
             toDate.setText("mm-dd-yyyy");
             }**/
            final Calendar myCalendar = Calendar.getInstance();
//  myCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
  myCalendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        //    myCalendar.add(Calendar.DATE, -7);
            startDate = simpleDateFormat.format(myCalendar.getTime());
            fromDate.setText(startDate);
            Date date = Calendar.getInstance().getTime();
            endDate = simpleDateFormat.format(date);
            toDate.setText(endDate);
            //  toDate.setText("mm-dd-yyyy");
            dateBtn = findViewById(R.id.search);
            if(savedInstanceState!=null){
                fromDate.setText((CharSequence) savedInstanceState.getSerializable("From date saved"));
                toDate.setText((CharSequence) savedInstanceState.getSerializable("To date saved"));
            }
            fetchPosts();

            fromDate.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(final View v) {

                    /**     editor=preferences.edit();
                     editor.remove("From date");**/
                    if (fromDate.getText().toString().equals("mm-dd-yyyy")) {
                        Calendar newCalendar = Calendar.getInstance();
                        startDateDialog = new DatePickerDialog(OrderList.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance(); //
                                newDate.set(year, month, dayOfMonth);
                                /**     editor.putString("From date", String.valueOf(newDate.getTime()));
                                 //  editor.putLong("From date",newDate.getTime());
                                 editor.apply();**/
                                fromdateSaved = simpleDateFormat.format(newDate.getTime());
                                fromDate.setText(simpleDateFormat.format(newDate.getTime()));
                                fromDate.setTextColor(Color.parseColor("#000000"));
                            }
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        startDateDialog.getDatePicker().setMaxDate(new Date().getTime());
                        startDateDialog.show();
                    } else {
                        Log.e(TAG, "From date is " + fromDate.getText().toString());
                        Calendar newCalendar = Calendar.getInstance();
                        try {
                            Date date8 = simpleDateFormat.parse(fromDate.getText().toString());
                            newCalendar.setTime(date8);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        startDateDialog = new DatePickerDialog(OrderList.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance(); //
                                newDate.set(year, month, dayOfMonth);
                                //     editor.putString("From date", String.valueOf(newDate.getTime()));
                                //   editor.apply();
                                fromdateSaved = simpleDateFormat.format(newDate.getTime());
                                fromDate.setText(simpleDateFormat.format(newDate.getTime()));
                                fromDate.setTextColor(Color.parseColor("#000000"));
                            }
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        startDateDialog.getDatePicker().setMaxDate(new Date().getTime());
                        startDateDialog.show();
                    }
                }
            });

            toDate.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(View v) {
                    /**    editor.remove("To date");
                     editor.apply();**/
                    Calendar newCalendar = Calendar.getInstance();
                    if (toDate.getText().toString().equals("mm-dd-yyyy")) {
                        endDateDialog = new DatePickerDialog(OrderList.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, month, dayOfMonth);
                                /**  editor.putString("To date", String.valueOf(newDate.getTime()));
                                 editor.apply();**/
                                todateSaved = simpleDateFormat.format(newDate.getTime());
                                toDate.setText(simpleDateFormat.format(newDate.getTime()));
                                toDate.setTextColor(Color.parseColor("#000000"));

                            }
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        endDateDialog.getDatePicker().setMaxDate(new Date().getTime());
                        endDateDialog.show();
                    } else {
                        try {
                            Date dateTo = simpleDateFormat.parse(toDate.getText().toString());
                            newCalendar.setTime(dateTo);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        endDateDialog = new DatePickerDialog(OrderList.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, month, dayOfMonth);
                                /** editor.putString("To date", String.valueOf(newDate.getTime()));
                                 editor.apply();**/
                                todateSaved = simpleDateFormat.format(newDate.getTime());
                                toDate.setText(simpleDateFormat.format(newDate.getTime()));
                                toDate.setTextColor(Color.parseColor("#000000"));
                            }
                        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        endDateDialog.getDatePicker().setMaxDate(new Date().getTime());
                        endDateDialog.show();
                    }
                }
            });


            dateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fromClick = fromDate.getText().toString();
                    toClick = toDate.getText().toString();
                    Log.e(TAG, "From date: " + fromClick);
                    Log.e(TAG, "To date: " + toClick);

                    try {
                        date1 = simpleDateFormat.parse(fromClick);
                        date2 = simpleDateFormat.parse(toClick);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (date1 != null && date2 != null) {
                        if (date2.compareTo(date1) < 0) {
                            Log.e(TAG, "To date is less than from date");
                            AlertDialog dialog = new AlertDialog.Builder(OrderList.this).create();
                            dialog.setIcon(R.drawable.mark1);
                            dialog.setTitle("Invalid date selection");
                            dialog.setMessage("To date is less than from date");
                            dialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialog.show();
                        }

                        mainLayout.setVisibility(View.INVISIBLE);
                        progress.setVisibility(View.VISIBLE);
                        fetchPosts();

                    } else {
                        Toast.makeText(OrderList.this, "Please select a date range", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    private void fetchPosts() {
        final String url="http://business.foodypos.com/App/Api.asmx/Orderlist?RestaurantId="+restId+"&startdate="+fromDate.getText().toString()+"&enddate="+toDate.getText().toString()+"&ordernumber=null";
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
      Log.e(TAG,response);
      GetOrderList getOrderList=new GetOrderList();
      getOrderList.execute(response);
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"Sorry, some error occurred",Toast.LENGTH_LONG).show();
        }
    };


    private ExpandableListView.OnChildClickListener myListItemClicked=new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            HeaderInfo headerInfo=headerList.get(groupPosition);
            DetailChildInfo detailChildInfo=headerInfo.getChildInfos().get(childPosition);
            Intent intent=new Intent(OrderList.this,OnClickOrder.class);
           // String orderDetails= String.valueOf(detailChildInfo.getOnClickJson());
           // intent.putExtra("OrderDetails",  orderDetails);
            intent.putExtra("Start date",fromDate.getText().toString());
            intent.putExtra("End date",toDate.getText().toString());
            intent.putExtra("Order num",parentArray.get(groupPosition).get(childPosition));
          //  Toast.makeText(getApplicationContext(),parentArray.get(groupPosition).get(childPosition),Toast.LENGTH_LONG).show();
          //  startActivity(intent);
            startActivityForResult(intent,2);
            return false;
        }
    };

    private ExpandableListView.OnGroupClickListener myListGroupClicked=new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            HeaderInfo headerInfo=headerList.get(groupPosition);
            return false;
        }
    };



  /**  private void expandAll() {
        int count=expandableListAdapter.getGroupCount();
        for(int i=0;i<count;i++){
            expandableListView.expandGroup(i);
        }
    }
**/

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu){
        //super.onCreateOptionsMenu(menu);
        //menuInflater.inflate(R.menu.menu_search,menu);
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.search_icon);

        final SearchView searchView= (SearchView) item.getActionView();
      //  int options=searchView.getImeOptions();
      //  searchView.setImeOptions(options);

        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setQueryHint("Enter Order No.");
      /**  searchView.setOnKeyListener(new View.OnKeyListener() {
            private boolean extended = false;
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(!extended && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    extended=true;
                    ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
                    layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
                }
                return false;
            }
        });**/
      //  ViewGroup.LayoutParams layoutParams=searchView.getLayoutParams();
      //  layoutParams.width=ViewGroup.LayoutParams.MATCH_PARENT;
      //  item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW|MenuItem.SHOW_AS_ACTION_ALWAYS);
       // item.expandActionView();
     //   EditText editText=getBaseContext().getResources().findViewById(R.id.search_)
        searchView.setMaxWidth(Integer.MAX_VALUE);
        int searchPlateId=android.support.v7.appcompat.R.id.search_plate;
        View searchPlate=searchView.findViewById(searchPlateId);
      //  ViewGroup.LayoutParams layoutParams=searchPlate.getLayoutParams();
      //  layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
       final TextView searchText ;
        if(searchPlate!=null){
            searchPlate.setBackgroundColor(Color.parseColor("#ff6501"));
            int searchTextId=searchPlate.getContext().getResources().getIdentifier("@android:search_src_text",null,null);
            searchText=searchPlate.findViewById(searchTextId);
            if(searchText!=null){
                searchText.setTextColor(Color.WHITE);
                searchText.setHintTextColor(Color.WHITE);
            }
        }

        final AutoCompleteTextView searchTextView=searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawable=TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawable.setAccessible(true);
            mCursorDrawable.set(searchTextView,R.drawable.cursor);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                Intent intent=new Intent(OrderList.this,OnClickOrder.class);
                intent.putExtra("Order num",query);
                intent.putExtra("Start date", (Bundle) null);
                startActivity(intent);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
return super.onCreateOptionsMenu(menu);


//        return true;
    }


    public void onBackPressed(){
        super.onBackPressed();
    /**    editor.remove("To date");
        editor.remove("From date");
        editor.commit();**/
    }


    private class GetOrderList extends AsyncTask<String,Void,ArrayList<HeaderInfo>> {
       double currentPrice,totalPrice=0.0,roundedPrice;
        String totalOrders,totalAmount,finalPrice;
        int flagResult=1;

        @Override
        protected ArrayList<HeaderInfo> doInBackground(String... response) {
            try {
                JSONObject jsonObject=new JSONObject(response[0]);
           //    if(jsonObject.getString("Message").equals("Sorry, An Execption occurred")){
                if(jsonObject.has("Message")){

                   flagResult=0;


               }
                else{
           //  if(jsonObject)
                 //  flagResult=1;
                    parentArray=new ArrayList<>();
                   headerList=new ArrayList<>();
                JSONObject jsonObject1=jsonObject.getJSONObject("By_DateSelection");
                totalOrders=jsonObject1.getString("TotalOrders");
                totalAmount=jsonObject1.getString("TotalAmount");
                DetailChildInfo detailChildInfo;
              //  currentAmount=Double.parseDouble(totalAmount);
               // currentOrder=Integer.parseInt(totalOrders);
                JSONArray dateArray=jsonObject1.getJSONArray("Date");
                for(int i=0;i<dateArray.length();i++) {
                    // headerList=new ArrayList<>();
                    JSONObject jsonObject2 = dateArray.getJSONObject(i);
                    String date = jsonObject2.getString("OrderDate");
                    ordersOnADay = new ArrayList<>();
                    totalPrice=0.0;
                    orderNumList=new ArrayList<>();
                    JSONArray orderNumArray = jsonObject2.getJSONArray("Order_NumberDetails");
                    for (int j = 0; j < orderNumArray.length(); j++) {
                        JSONObject jsonObject3 = orderNumArray.getJSONObject(j);
                        orderNum = jsonObject3.getString("OrderNo");
                        String price = jsonObject3.getString("TotalPrices");
                        currentPrice = Double.parseDouble(price);
                        String time = jsonObject3.getString("PickupTime");
                        totalPrice = totalPrice + currentPrice;
                        roundedPrice = Math.round(totalPrice * 100.0) / 100.0;
                        finalPrice = String.valueOf(roundedPrice);
                        detailChildInfo = new DetailChildInfo();
                        detailChildInfo.setOrderNum("#" + orderNum);
                        detailChildInfo.setTime(time);
                        detailChildInfo.setPrice("$" + price);
                        orderNumList.add(orderNum);
                        ordersOnADay.add(j, detailChildInfo);
                    }

                    parentArray.add(orderNumList);
                    HeaderInfo headerInfo = new HeaderInfo();
                    headerInfo.setDate(date);
                    headerInfo.setPrice(finalPrice);
                    headerInfo.setNumOfOrders(String.valueOf(orderNumArray.length()) + " order(s)");
                    headerList.add(headerInfo);
                    headerInfo.setChildInfos(ordersOnADay);
                }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//return null;
            return headerList;
        }
       public void onPostExecute(ArrayList<HeaderInfo> headerList){
            super.onPostExecute(headerList);

           if(flagResult==1) {
               mainLayout.setVisibility(View.VISIBLE);
               progress.setVisibility(View.INVISIBLE);
               amount.setText("$"+totalAmount);
            orders.setText(totalOrders);
                if (headerList.size() > 0) {
                    expandableListAdapter = new ExpandableList(OrderList.this, headerList);
                    expandableListView.setAdapter(expandableListAdapter);
                }
            }
           else if(flagResult==0){
            //    Toast.makeText(OrderList.this,"Server error",Toast.LENGTH_LONG).show();

               new android.support.v7.app.AlertDialog.Builder(OrderList.this)
                       .setMessage("Sorry, Unable to connect to server. Please try after some time")
                       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               noDataLayout.setVisibility(View.VISIBLE);
                               progress.setVisibility(View.INVISIBLE);
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
