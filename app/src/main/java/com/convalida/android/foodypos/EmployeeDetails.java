package com.convalida.android.foodypos;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

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

//import okhttp3.Response;


public class EmployeeDetails extends AppCompatActivity implements EmployeeTaskFragment.EmployeeTaskCallbacks {
    FloatingActionButton add;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    Gson gson;
    RelativeLayout noDataLayout;
    String employeeData;
    ArrayList<EmployeeDetailData> employeeDetailDataArrayList;
    EmployeeDetailAdapter adapter;
    private static final String TAG="EmployeeDetails";
    RelativeLayout mainLayout,progressLayout;
    private EmployeeTaskFragment employeeTaskFragment;
    private static final String TAG_EMPLOYEEE_TASK="employee_task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Employee Details");
        }
        progressLayout=findViewById(R.id.progressLayout);
        mainLayout=findViewById(R.id.mainLayout);
        employeeDetailDataArrayList=new ArrayList<>();
        add=findViewById(R.id.addDetails);
        recyclerView=findViewById(R.id.employees);
        noDataLayout=findViewById(R.id.noDataLayout);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i1=new Intent(EmployeeDetails.this,AddEmployeeDetail.class);
                startActivity(i1);
            }
        });

  //      employeeData="{\"result\":{\"Message\":\"EmailID are already exists\",\"ResultCode\":\"0\"},\"EmployeeDetails\":[{\"Username\":\"order@metropolisgrill.com\",\"EmailId\":\"order@metropolisgrill.com\",\"RolwType\":\"Manager\",\"Active\":\"True\"}]}\n" +
    //            "\n";

     /**   try {
            generateData();
        } catch (Exception e) {
            e.printStackTrace();
        }**/
     /**   EmployeeDetailAdapter adapter = new EmployeeDetailAdapter(this, employeeDetailDataArrayList);
        ListView listView = findViewById(R.id.listview);

        // 3. setListAdapter
        listView.setAdapter(adapter);**/
     requestQueue= Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder=new GsonBuilder();
        gson=gsonBuilder.create();
        getEmployees();
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(EmployeeDetails.this,MainActivity.class);
        startActivity(intent);
    }

    private void getEmployees() {
        String restId;
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
        restId=sharedPreferences.getString("Id","");
        Log.e(TAG,restId);
        final String MAIN = Constants.BASE_URL+"ViewEmployee?RestaurantId="+restId;
        StringRequest stringRequest=new StringRequest(Request.Method.GET,MAIN,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG + " onPosts", response);
            //  GetEmployees employees=new GetEmployees();
            //  employees.execute(response);
            // FragmentManager fragmentManager=getFragmentManager();
            employeeTaskFragment = (EmployeeTaskFragment) getSupportFragmentManager().findFragmentByTag(TAG_EMPLOYEEE_TASK);
            Bundle bundle = new Bundle();
            bundle.putString("responseEmployee", response);
            //   try{
            if (employeeTaskFragment == null) {
             //   try {
                    employeeTaskFragment = new EmployeeTaskFragment();
                    employeeTaskFragment.setArguments(bundle);
                    Log.e(TAG , "On posts Fragment initalized");
                    getSupportFragmentManager().beginTransaction().add(employeeTaskFragment, TAG_EMPLOYEEE_TASK).commitAllowingStateLoss();
                    // }

                /**} catch (Exception e) {
                    e.printStackTrace();
                }**/
            }
            if(employeeTaskFragment!=null){
                employeeTaskFragment.startBackgroundTask();
            }
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
        }
    };

    @Override
    public void onPostExecute() {
        Log.e(TAG,"onPostExecute after rotation");
        if(employeeTaskFragment!=null){
            employeeTaskFragment.updateExecutingStatus(false);
        }
        progressLayout.setVisibility(View.INVISIBLE);
        Log.e(TAG,"Progress layout invisible");
        mainLayout.setVisibility(View.VISIBLE);
        Log.e(TAG,"Main layout visible");

        EmployeeDetailAdapter empAdapter = new EmployeeDetailAdapter(EmployeeTaskFragment.employeeDetailDataArrayList, getApplication());
        recyclerView.setAdapter(empAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeDetails.this));
    }

    @Override
    public void onPostFailure() {
        Log.e(TAG,"Server error");
        new AlertDialog.Builder(EmployeeDetails.this)
                .setMessage("Sorry, unable to connect to server. Please try after some time")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        noDataLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.INVISIBLE);
                        progressLayout.setVisibility(View.INVISIBLE);
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }



}




