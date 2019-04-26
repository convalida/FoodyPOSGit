package com.convalida.android.foodypos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OnClickOrder extends AppCompatActivity {
    TextView ordersImg, nameText, emailText,phone,amountValue,menuIcon;
  //  ExpandableListView ordersExpandable;
  //  ExpandableOnClickList expandableOnClickListAdapter;
  //  private ArrayList<OrderDetailHeader> orderDetailHeaders=new ArrayList<>();
    String receivedJson;

    ListView listView;
    OrderDetailData orderDetailData=new OrderDetailData();
   public static ArrayList<OrderDetailChild> itemDetails;
    OrderDetailChild orderDetailChild;
    RelativeLayout relativeLayout,noDataLayout;
    Gson gson;
    String startDate,endDate,orderNum;
    RequestQueue requestQueue;
    String restId;
    public ArrayList<OrderDetailChild> itemsList=new ArrayList<>();
    private static final String TAG="OnCLickOrder";
    ArrayList<OrderDetailData> orderDetailDataArrayList;
    RelativeLayout progress;
    LinearLayout mainLayout;
    int flag=0;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click_order);
       // ordersImg=findViewById(R.id.orderIcon);
       // Typeface font=Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
       // ordersImg.setTypeface(font);
       // ordersImg.setText("\uf0f5");

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            progress=findViewById(R.id.orderDetailProgress);
        mainLayout=findViewById(R.id.orderDetailLayout);
        nameText=findViewById(R.id.userText);
        emailText=findViewById(R.id.mailText);
        phone=findViewById(R.id.contactText);
        noDataLayout=findViewById(R.id.noDataLayout);
        amountValue=findViewById(R.id.amountTotal);
        relativeLayout=findViewById(R.id.amount);
        menuIcon=findViewById(R.id.menuIcon);
        Typeface font=Typeface.createFromAsset(getAssets(),"fonts/fontawesome-webfont.ttf");
        menuIcon.setTypeface(font);
        menuIcon.setText("\uf0f5");
        listView=findViewById(R.id.list);

        /**   listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });**/
   //  Utility.setListViewHeightBasedOnChildren(listView);
    //    ordersExpandable=findViewById(R.id.expandleOrderItems);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId", MODE_PRIVATE);
        restId=sharedPreferences.getString("Id","");
        Log.e(TAG, restId);
        requestQueue=Volley.newRequestQueue(OnClickOrder.this);
        GsonBuilder gsonBuilder=new GsonBuilder();
        gson=gsonBuilder.create();
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Order Details");
        }





        if(Objects.requireNonNull(getIntent().getExtras()).get("OrderNo")!=null) {
            //called when notification is clicked received while app not running
          /**  String extras = getIntent().getExtras().get("body").toString();
           // Toast.makeText(getApplicationContext(), extras, Toast.LENGTH_LONG).show();
            assert extras != null;
            String[] individualStrings=extras.split(" ");
            String order=individualStrings[1];
            String orderNo=order.substring(1);**/
          String orderNo=getIntent().getExtras().get("OrderNo").toString();

            orderNum=orderNo;

        }
     /**   else if(getIntent().getExtras().get("OrderNo")!=null){
            String orderNo=getIntent().getExtras().get("OrderNo").toString();
            orderNum=orderNo;
        }**/
        else{
           // Toast.makeText(getApplicationContext(),"No extras",Toast.LENGTH_LONG).show();
         //   Toast.makeText(getApplicationContext(),orderNum,Toast.LENGTH_LONG).show();
            //called order no. clicked when in app
            flag=1;
            Intent i=getIntent();
            startDate=i.getStringExtra("Start date");
            endDate=i.getStringExtra("End date");
         orderNum=i.getStringExtra("Order num");

        //    Toast.makeText(getApplicationContext(),orderNum,Toast.LENGTH_LONG).show();
        }


        fetchPosts();
        //     ordersExpandable.setOnChildClickListener(myListItemClicked);
        //   ordersExpandable.setOnGroupClickListener(myListGroupClicked);
        //  expandableOnClickListAdapter=new ExpandableOnClickList(this,orderDetailHeaders);
        //ordersExpandable.setAdapter(expandableOnClickListAdapter);
        //    expandAll();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OnClickOrder.this,AmountDetails.class);
                intent.putExtra("SubTotal",orderDetailData.getSubTotal());
                intent.putExtra("TaxPer",orderDetailData.getTaxPercent());
                intent.putExtra("TaxValue",orderDetailData.getTaxValue());
                intent.putExtra("Tip",orderDetailData.getTip());
                intent.putExtra("GrandTotal",orderDetailData.getGrandTotal());
                startActivity(intent);
            }
        });
     /**   Intent i=getIntent();
        receivedJson=i.getStringExtra("OrderDetails");
        try {
            parseJson();
        } catch (Exception e) {
            e.printStackTrace();
        }**/

   //     nameText.setText(orderDetailData.getCustomerName());
     //   emailText.setText(orderDetailData.getMailId());
      //  phone.setText(orderDetailData.getContact());

        if(!getIntent().hasExtra("Start date")){
            // called by notification click, call read/unread webservice here
            updateReadStatus();
        }
    }

    public void onBackPressed(){
        super.onBackPressed();

    }

    private void updateReadStatus() {
       final String token=SharedPrefManagerToken.getmInstance(this).getDeviceToken();
    //    final String url="http://business.foodypos.com/App/Api.asmx/ReadNotificationByUser";
        final String url="http://demo.metropolisgrill.com/App/Api.asmx/ReadNotificationByUser";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObject1=jsonObject.getJSONObject("Result");
                    String resultCode=jsonObject1.getString("ResultCode");
                    String message=jsonObject1.getString("Message");
                    if(resultCode.equals("0")){
                      //  Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
             //   Toast.makeText(getApplicationContext(),"Some error occurred",Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String,String> getParams() {
                Map<String,String> parameters=new HashMap<>();
                parameters.put("deviceid",token);
                parameters.put("orderno",orderNum);
                return parameters;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(OnClickOrder.this);
        requestQueue.add(stringRequest);

    }

    /**  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(resultCode,requestCode,data);
        if(requestCode!=2){
            if (data.getExtras() != null) {
                //    for (String key : getIntent().getExtras().keySet()) {
                //  if (key.equals("body")) {
                String title = getIntent().getExtras().getString("title");
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
            }


        }


    }**/

    private void fetchPosts() {
        final String url=Constants.BASE_URL+"Orderlist?RestaurantId="+restId+"&startdate="+startDate+"&enddate="+endDate+"&ordernumber="+orderNum;
        StringRequest stringRequest=new StringRequest(Request.Method.GET,url,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            GetOrderDetails orderDetails=new GetOrderDetails();
            orderDetails.execute(response);
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());

        }
    };

    private class GetOrderDetails extends AsyncTask<String,Void,Wrapper2>{
        int flagResult=1;
        int flagOderNum=1;
     //   OrderDetailData orderDetailData=new OrderDetailData();

        @Override
        protected Wrapper2 doInBackground(String... response) {
            try {
                JSONObject jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
                else{

                    orderDetailDataArrayList=new ArrayList<>();
                    JSONArray jsonArray=jsonObject.getJSONArray("By_OrderNumber");
                    for(int i=0;i<jsonArray.length();i++) {
                          JSONObject jsonObject1=jsonArray.getJSONObject(i);
                          if(jsonObject1.has("Message")){
                              flagOderNum=0;
                          }
                          else {
                              JSONObject jsonObject2 = jsonObject1.getJSONObject("OnClick");
                              String name = jsonObject2.getString("CustomerName");
                              String id = jsonObject2.getString("CustomerId");
                              String email = jsonObject2.getString("Email");
                              String contact = jsonObject2.getString("ContactNumber");
                              String subTotal = jsonObject2.getString("SubTotal");
                              String taxPer = jsonObject2.getString("TaxInPercentage");
                              String taxVal = jsonObject2.getString("Taxvalue");
                              String tip = jsonObject2.getString("Tip");
                              String grandTotal = jsonObject2.getString("GrandTotal");
                              itemsList = new ArrayList<>();
                              JSONArray itemDetailsArray = jsonObject2.getJSONArray("OrderItemDetails");
                              for (int j = 0; j < itemDetailsArray.length(); j++) {

                                  JSONObject jsonObject3 = itemDetailsArray.getJSONObject(j);
                                  String itemName = jsonObject3.getString("SubitemsNames");
                                  String modifier = jsonObject3.getString("Modifier");
                                  String addOn = jsonObject3.getString("AddOn");
                                  String instruction = jsonObject3.getString("Instruction");
                                  String price = jsonObject3.getString("Price");
                                  String addOnPrice = jsonObject3.getString("AddOnPrices");
                                  String total = jsonObject3.getString("Total");
                                  orderDetailChild = new OrderDetailChild();
                                  orderDetailChild.setItemName(itemName);
                                  orderDetailChild.setModifier(modifier);
                                  orderDetailChild.setAddOn(addOn);
                                  orderDetailChild.setItemPrice(price);
                                  orderDetailChild.setAddOnPrice(addOnPrice);
                                  orderDetailChild.setInstructions(instruction);
                                  orderDetailChild.setTotal(total);

                                  itemsList.add(orderDetailChild);
                              }
                              //   OrderDetailData orderDetailData=new OrderDetailData();
                              orderDetailData.setCustomerName(name);
                              orderDetailData.setMailId(email);
                              orderDetailData.setContact(contact);
                              orderDetailData.setGrandTotal(grandTotal);
                              orderDetailData.setSubTotal(subTotal);
                              orderDetailData.setTaxPercent(taxPer);
                              orderDetailData.setTaxValue(taxVal);
                              orderDetailData.setCustomerId(id);
                              orderDetailData.setTip(tip);
                              //  orderDetailDataArrayList.add(orderDetailData);
                          }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           // return orderDetailDataArrayList;
           // return orderDetailData;
            Wrapper2 w2=new Wrapper2();
            w2.itemsList=itemsList;
            w2.orderDetailData=orderDetailData;
        return w2;
        }

        public void onPostExecute(Wrapper2 wrapper2){
         //   super.onPostExecute(orderDetailDataArrayList);
            super.onPostExecute(wrapper2);
            if(flagResult==1 && flagOderNum==1){
                mainLayout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.INVISIBLE);
                nameText.setText(orderDetailData.getCustomerName());
              /**  nameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(OnClickOrder.this,CustomerClick.class);
                        startActivity(intent);
                    }
                });**/
              nameText.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      Intent intent=new Intent(OnClickOrder.this,CustomerClick.class);
                      intent.putExtra("Customer click",orderDetailData.getCustomerId());
                      startActivity(intent);
                  }
              });
                emailText.setText(orderDetailData.getMailId());
                phone.setText(orderDetailData.getContact());
                amountValue.setText("$"+orderDetailData.getGrandTotal());
               OrderItemsAdapter adapter=new OrderItemsAdapter(itemsList,OnClickOrder.this);
                listView.setAdapter(adapter);
            }
            else{
                if(flagResult==0){
                    Log.e(TAG,"Server error");
                    new AlertDialog.Builder(OnClickOrder.this)
                            .setMessage("Sorry, unable to connect to server")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    noDataLayout.setVisibility(View.VISIBLE);
                                    progress.setVisibility(View.INVISIBLE);
                                    mainLayout.setVisibility(View.INVISIBLE);
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();
                }
                else if(flagOderNum==0){
                  //  Intent intent=new Intent(OnClickOrder.this,OrderList.class);
                  //  startActivity(intent);
                    onBackPressed();
                    Toast.makeText(getApplicationContext(),"Entered order no. not found",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


}
