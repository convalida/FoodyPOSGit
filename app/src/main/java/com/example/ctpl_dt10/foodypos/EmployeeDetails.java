package com.example.ctpl_dt10.foodypos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

import static android.R.layout.*;

public class EmployeeDetails extends AppCompatActivity {
    FloatingActionButton add;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    Gson gson;
    String employeeData;
    ArrayList<EmployeeDetailData> employeeDetailDataArrayList;
    EmployeeDetailAdapter adapter;
    private static final String TAG="EmployeeDetails";
    RelativeLayout mainLayout,progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        if(getSupportActionBar()!=null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Employee Details");
        }
employeeDetailDataArrayList=new ArrayList<>();
        add=findViewById(R.id.addDetails);
        recyclerView=findViewById(R.id.employees);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i1=new Intent(EmployeeDetails.this,AddEmployeeDetail.class);
                startActivity(i1);
            }
        });
        mainLayout=findViewById(R.id.mainLayout);
        progressLayout=findViewById(R.id.progressLayout);
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
        final String MAIN = "http://business.foodypos.com/App/Api.asmx/ViewEmployee?RestaurantId="+restId;
        StringRequest stringRequest=new StringRequest(Request.Method.GET,MAIN,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.e(TAG,response);
            GetEmployees employees=new GetEmployees();
            employees.execute(response);
        }
    };
    Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
        }
    };

    private class GetEmployees extends AsyncTask<String,Void,ArrayList<EmployeeDetailData>> {
        @Override
        protected ArrayList<EmployeeDetailData> doInBackground(String... response) {
            try {
                employeeDetailDataArrayList=new ArrayList<>();
                JSONObject jsonObject=new JSONObject(response[0]);
                JSONArray employeesArray=jsonObject.getJSONArray("EmployeeDetails");
                for(int i=0;i<employeesArray.length();i++){
                    JSONObject employeeObject=employeesArray.getJSONObject(i);
                    String name=employeeObject.getString("Username");
                    String email=employeeObject.getString("EmailId");
                    String role=employeeObject.getString("RoleType");
                    String active=employeeObject.getString("Active");
                    String accountId=employeeObject.getString("AccountId");

                    EmployeeDetailData empData=new EmployeeDetailData();
                    empData.setActive(active);
                    empData.setEmail(email);
                    empData.setName(name);
                    empData.setRole(role);
                    empData.setAcctId(accountId);
                    employeeDetailDataArrayList.add(empData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return employeeDetailDataArrayList;
        }
        public void onPostExecute(ArrayList<EmployeeDetailData> empList){
            super.onPostExecute(empList);
            mainLayout.setVisibility(View.VISIBLE);
            progressLayout.setVisibility(View.INVISIBLE);
            EmployeeDetailAdapter empAdapter=new EmployeeDetailAdapter(empList,getApplication());
            recyclerView.setAdapter(empAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(EmployeeDetails.this));
        }
    }


}




