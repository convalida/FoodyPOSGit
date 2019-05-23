package com.convalida.android.foodypos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

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
import java.util.List;

public class BestSeller extends AppCompatActivity {
    TextView yearTopFirst, yearTopFirstNum,yearTopSecond,yearTopSecondNum,yearTopThird,yearTopThirdNum;
    TextView monthTopFirst, monthTopFirstNum, monthTopSecond, monthTopSecondNum, monthTopThird, monthTopThirdNum;
    TextView weekTopFirst, weekTopFirstNum, weekTopSecond, weekTopSecondNum, weekTopThird, weekTopThirdNum;
    private static final String TAG="BestSeller";
    List<BestsellerChildlist> yearSeller;
    List<BestsellerChildlist> monthSeller;
    List<BestsellerChildlist> weekSeller;
    RelativeLayout monthRelativeLayout,weekRelativeLayout,yearRelativeLayout;
    TableLayout monthTableLayout,weekTableLayout, yearTableLayout;
  //  LinearLayout mainLayout;
   ScrollView mainLayout;
    RelativeLayout progressLayout;
    RelativeLayout noDataLayout;
    Button weekMore,monthMore,yearMore;
  //  RelativeLayout progressLayout;
  //  ProgressBar progressLayout;
    BestsellerChildlist bestsellerChildlist1=new BestsellerChildlist();
    private Gson gson;
   // JsonParser jsonParser=new JsonParser();

    private String jsonString="{\"WeeklyBestsellersItem\":[],\"MonthelyBestsellersItem\":[],\"YearlyBestsellersItem\":[{\"Subitems\":\"Greek Gyro\",\"Counting\":\"169\"},{\"Subitems\":\"Chicken Tikka Masala\",\"Counting\":\"126\"},{\"Subitems\":\"Gyro Supreme\",\"Counting\":\"105\"}]}";

    String yearItem1,yearItem1Num,yrItem2,yrItem2Num,yearItem3,yearItem3Num;
    String monthItem1, monthItem1Num, monthItem2,monthItem2Num, monthItem3, monthItem3Num;
    String weekItem1, weekItem1Num, weekItem2, weekItem2Num, weekItem3, weekItem3Num;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_seller);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Bestseller Items");
        }
        mainLayout=findViewById(R.id.mainLayout);
       // mainLayout.setVisibility(View.INVISIBLE);
       // mainLayout.setVisibility(View.GONE);
        progressLayout=findViewById(R.id.progressLayout);
        noDataLayout=findViewById(R.id.noDataLayout);
        monthRelativeLayout=findViewById(R.id.noDataMonth);
        weekRelativeLayout=findViewById(R.id.noDataWeek);
        yearRelativeLayout=findViewById(R.id.noDataYear);
        monthTableLayout=findViewById(R.id.monthTableLayout);
        yearTableLayout=findViewById(R.id.yearTableLayout);
        yearTopFirst=findViewById(R.id.yearBestOne);
        yearTopFirstNum=findViewById(R.id.yearBestOneNum);
        yearTopSecond=findViewById(R.id.yearBestTwo);
        yearTopSecondNum=findViewById(R.id.yearBestTwoNum);
        yearTopThird=findViewById(R.id.yearBestThree);
        yearTopThirdNum=findViewById(R.id.yearBestThreeNum);

        monthTopFirst=findViewById(R.id.monthItem1);
       monthTopFirstNum=findViewById(R.id.monthItem1Qty);
       monthTopSecond=findViewById(R.id.monthItem2);
       monthTopSecondNum=findViewById(R.id.monthItem2Qty);
       monthTopThird=findViewById(R.id.monthItem3);
       monthTopThirdNum=findViewById(R.id.monthItem3Qty);

        weekTopFirst=findViewById(R.id.weekItem1);
        weekTopFirstNum=findViewById(R.id.weekItem1Qty);
        weekTopSecond=findViewById(R.id.weekItem2);
        weekTopSecondNum=findViewById(R.id.weekItem2Qty);
        weekTopThird=findViewById(R.id.weekItem3);
        weekTopThirdNum=findViewById(R.id.weekItem3Qty);
        weekTableLayout=findViewById(R.id.weekTableLayout);


        weekMore=findViewById(R.id.weeklyMore);
        monthMore=findViewById(R.id.monthlyMore);
        yearMore=findViewById(R.id.yearlyMore);

     requestQueue= Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder=new GsonBuilder();
        gson=gsonBuilder.create();
     fetchPosts();

        weekMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BestSeller.this,BestsellerMore.class);
                intent.putExtra("Flag",1);
                startActivity(intent);
            }
        });
        monthMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BestSeller.this,BestsellerMore.class);
                intent.putExtra("Flag",2);
                startActivity(intent);
            }
        });

        yearMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BestSeller.this,BestsellerMore.class);
                intent.putExtra("Flag",3);
                startActivity(intent);
            }
        });
    }

    private void fetchPosts() {
      //  SharedPreferences sharedPreferences
        String restId;
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
        restId=sharedPreferences.getString("Id","");
        Log.e(TAG,restId);
        final String ENDPOINT=Constants.BASE_URL+"bestselleritems?RestaurantId="+restId;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(stringRequest);
    }

    private final Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG,response);

            ArrayList<String> bestweek=new ArrayList<>();
            ExecuteTask task=new ExecuteTask();
            task.execute(response);

            //Gson gson=new Gson();
            //BestSellerItemModel bestSellerItemModel=gson.fromJson(response,BestSellerItemModel.class);
        }
    };

    private final Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
      Log.e(TAG, error.toString());
        }
    };

    private void parse() throws JSONException {
        JSONObject jsonObject=new JSONObject(jsonString);
        JSONArray yearly=jsonObject.getJSONArray("YearlyBestsellersItem");

        JSONObject yearJObj=yearly.getJSONObject(0);
        yearItem1=yearJObj.getString("Subitems");
        yearItem1Num=yearJObj.getString("Counting");

        JSONObject yearJsObj=yearly.getJSONObject(1);
        yrItem2=yearJsObj.getString("Subitems");
        yrItem2Num=yearJsObj.getString("Counting");

        JSONObject yrJsoObj=yearly.getJSONObject(2);
        yearItem3=yrJsoObj.getString("Subitems");
        yearItem3Num=yrJsoObj.getString("Counting");
    }

    private class ExecuteTask extends AsyncTask<String, Void, ArrayList<List<BestsellerChildlist>>> {
        private ProgressBar progressBar;
        private int progressStatus=0;
        private Handler handler=new Handler();
        int flagMonth=1, flagWeek=1, flagYear=1;
        int flagResult=1;

        @Override
        protected ArrayList<List<BestsellerChildlist>> doInBackground(String... response) {
            JSONObject jsonObject=null;
            BestsellerChildlist bestsellerChildlist;
            ArrayList<List<BestsellerChildlist>> sumArray=new ArrayList<>();
            try {
                jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
                else {
                    JSONArray weekBestSeller = jsonObject.getJSONArray("WeeklyBestsellersItem");
                    weekSeller = new ArrayList<>();
                    if (weekBestSeller.length() > 0) {
                        for (int k = 0; k < weekBestSeller.length(); k++) {
                            JSONObject jsonObject1 = weekBestSeller.getJSONObject(k);
                            String name = jsonObject1.getString("Subitems");
                            String count = jsonObject1.getString("Counting");
                            bestsellerChildlist = new BestsellerChildlist();
                            bestsellerChildlist.setItemname(name);
                            bestsellerChildlist.setCounting(count);
                            weekSeller.add(bestsellerChildlist);
                        }
                    } else {
                        flagWeek = 0;
                    }

                    JSONArray monthBestSeller = jsonObject.getJSONArray("MonthelyBestsellersItem");
                    monthSeller = new ArrayList<>();
                    if (monthBestSeller.length() != 0) {
                        for (int j = 0; j < monthBestSeller.length(); j++) {
                            JSONObject jsonObject1 = monthBestSeller.getJSONObject(j);
                            String name = jsonObject1.getString("Subitems");
                            String count = jsonObject1.getString("Counting");
                            bestsellerChildlist = new BestsellerChildlist();
                            bestsellerChildlist.setItemname(name);
                            bestsellerChildlist.setCounting(count);
                            monthSeller.add(bestsellerChildlist);
                        }
                    } else {
                        flagMonth = 0;
                    }
                    JSONArray yearBestSeller = jsonObject.getJSONArray("YearlyBestsellersItem");
                    /**   if(weekBestSeller.length()>0){
                     weekSeller= Arrays.asList(gson.fromJson(weekBestSeller.toString(),BestSellerItemModel[].class));
                     for(BestSellerItemModel bestSellerItemModel1:weekSeller){
                     BestSellerItemModel bestSellerItemModel2=
                     }
                     }**/
                    yearSeller = new ArrayList<>();
                   // if(yearBestSeller.length()!=0){
                    if(yearBestSeller.length()!=0) {
                        for (int i = 0; i < yearBestSeller.length(); i++) {
                            JSONObject jsonObject1 = yearBestSeller.getJSONObject(i);
                            String name = jsonObject1.getString("Subitems");
                            String count = jsonObject1.getString("Counting");
                            bestsellerChildlist = new BestsellerChildlist();
                            bestsellerChildlist.setItemname(name);
                            bestsellerChildlist.setCounting(count);
                            yearSeller.add(bestsellerChildlist);

                        }
                    }
                    else{
                        flagYear = 0;
                    }
                    if (flagWeek != 0) {
                        sumArray.add(weekSeller);
                    }
                    if (flagMonth != 0) {
                        sumArray.add(monthSeller);
                    }
                    if(flagYear !=0) {
                        sumArray.add(yearSeller);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sumArray;
        }
        public void onPostExecute(ArrayList<List<BestsellerChildlist>> sumArray){
            super.onPostExecute(sumArray);
            if(flagResult==1) {
                mainLayout.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.INVISIBLE);
                if(flagYear == 1) {
                    yearTopFirst.setText(yearSeller.get(0).getItemname());
                    yearTopFirstNum.setText(yearSeller.get(0).getCounting());
                    if (yearSeller.size() > 1 && yearSeller.get(1) != null) {
                        yearTopSecond.setText(yearSeller.get(1).getItemname());
                        yearTopSecondNum.setText(yearSeller.get(1).getCounting());
                    }
                    if (yearSeller.size() > 2 && yearSeller.get(2) != null) {
                        yearTopThird.setText(yearSeller.get(2).getItemname());
                        yearTopThirdNum.setText(yearSeller.get(2).getCounting());
                    }
                }
                else{
                    yearTableLayout.setVisibility(View.INVISIBLE);
                    yearRelativeLayout.setVisibility(View.VISIBLE);
                }
                if (flagMonth == 1) {
                    monthTopFirst.setText(monthSeller.get(0).getItemname());
                    monthTopFirstNum.setText(monthSeller.get(0).getCounting());
                    if (monthSeller.size() > 1 && monthSeller.get(1) != null) {
                        monthTopSecond.setText(monthSeller.get(1).getItemname());
                        monthTopSecondNum.setText(monthSeller.get(1).getCounting());
                    }
                    if (monthSeller.size() > 2 && monthSeller.get(2) != null) {
                        monthTopThird.setText(monthSeller.get(2).getItemname());
                        monthTopThirdNum.setText(monthSeller.get(2).getCounting());
                    }
                } else {
                    monthTableLayout.setVisibility(View.INVISIBLE);
                    monthRelativeLayout.setVisibility(View.VISIBLE);
                }
                if (flagWeek == 1) {
                    weekTopFirst.setText(weekSeller.get(0).getItemname());
                    weekTopFirstNum.setText(weekSeller.get(0).getCounting());
                    if (weekSeller.size() > 1 && weekSeller.get(1) != null) {
                        weekTopSecond.setText(weekSeller.get(1).getItemname());
                        weekTopSecondNum.setText(weekSeller.get(1).getCounting());
                    }
                    if (weekSeller.size() > 2 && weekSeller.get(2) != null) {
                        weekTopThird.setText(weekSeller.get(2).getItemname());
                        weekTopThirdNum.setText(weekSeller.get(2).getCounting());
                    }
                } else {
                    weekTableLayout.setVisibility(View.INVISIBLE);
                    weekRelativeLayout.setVisibility(View.VISIBLE);
                }
            }
            else if(flagResult==0){
                Log.e(TAG,"Server error");
                new AlertDialog.Builder(BestSeller.this)
                        .setMessage("Sorry, unable to connect to server. Please try after some time")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mainLayout.setVisibility(View.INVISIBLE);
                                progressLayout.setVisibility(View.INVISIBLE);
                                noDataLayout.setVisibility(View.VISIBLE);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();

            }
            }
    }
}
