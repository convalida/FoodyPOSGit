 package com.convalida.android.foodypos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


 public class EditEmployeeDetail extends AppCompatActivity {
     Gson gson;
     RequestQueue requestQueue;
     String restId,modifiedBy;



    String ref_name, ref_email, ref_role, ref_active,acctId;

    TextView name, email;
    Spinner spinner;
    CheckBox status;
    String active;
    Button update, cancel;
     String[] arraySpinner;
     String selectedItem;
     private static final String TAG="EditEmployeeDetail";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee_detail);
        name = findViewById(R.id.uname);
        email = findViewById(R.id.emailid);
        spinner = findViewById(R.id.selectRole);
        status = findViewById(R.id.isactive);
        cancel = findViewById(R.id.cancelBtn);
        update = findViewById(R.id.updateBtn);
        this.setFinishOnTouchOutside(false);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent profile = this.getIntent();
        ref_name = profile.getStringExtra("ref1");
        ref_email = profile.getStringExtra("ref2");
        ref_role = profile.getStringExtra("ref3");
        ref_active = profile.getStringExtra("ref4");
        acctId=profile.getStringExtra("ref5");

        name.setText(ref_name);
        email.setText(ref_email);

        if(ref_role.equals("Manager"))
        {
            arraySpinner = new String[]{
                    "Manager", "Employee"
            };

        }

        else
        {
            arraySpinner = new String[]{
                     "Employee","Manager"
            };

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    /**    requestQueue= Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder=new GsonBuilder();
        gson=gsonBuilder.create();**/

    selectedItem=spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedItem = parent.getItemAtPosition(position).toString();
                    Log.i("Selected Item",selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        if (ref_active.equals("True")) {
            status.setChecked(true);
        } else {
            status.setChecked(false);
        }


        /** submit.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {

        }
        });**/
        int flagResult=1;
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // write the update code here
                //get the value of role type from "selectedItem"
                //and active status from check box
                if(status.isChecked()){
                    active="True";
                }else{
                    active="False";
                }

                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
                restId=sharedPreferences.getString("Id","");
                modifiedBy=sharedPreferences.getString("userName","");
                Log.e(TAG,restId);
           //     final String MAIN = "http://business.foodypos.com/App/Api.asmx/UpdateEmployee?AccountId="+acctId+"&RestaurantId="+restId+"&ModifiedBy="+modifiedBy+"&Role="+ref_role+"&Active="+ref_active;
             //  StringRequest stringRequest=new StringRequest(Request.Method.GET,MAIN,onPostsLoaded,onPostsError);
              //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
              //  requestQueue.add(stringRequest);

                final String url = "http://business.foodypos.com/App/Api.asmx/UpdateEmployee?AccountId="+acctId+"&RestaurantId="+restId+"&ModifiedBy="+modifiedBy+"&Role="+selectedItem+"&Active="+active;
               final String url2=Constants.BASE_URL+"UpdateEmployee";
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String resultCode = jsonObject.getString("ResultCode");
                            String message = jsonObject.getString("Message");
                            if(resultCode.equals("0")){
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            }
                            else if(resultCode.equals("1")){
                                Toast.makeText(getApplicationContext(), "Employee details updated successfully", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Some error occured",Toast.LENGTH_LONG).show();
                    if(error instanceof TimeoutError || error instanceof NoConnectionError){
                        Log.e(TAG,"No connection error");
                    }
                    else if(error instanceof AuthFailureError){
                        Log.e(TAG,"AuthFailureError");
                    }
                    else if(error instanceof ServerError){
                        Log.e(TAG,"Server Error");
                    }
                    else if(error instanceof NetworkError){
                        Log.e(TAG,"Network error");
                    }
                    else if(error instanceof ParseError){
                        Log.e(TAG,"Parse error");
                    }
                    }
                })
               {
                  protected Map<String,String> getParams() throws AuthFailureError{
                      Map<String,String> parameters=new HashMap<String, String>();
                      parameters.put("AccountId",acctId);
                      parameters.put("RestaurantId",restId);
                      parameters.put("ModifiedBy",modifiedBy);
                      parameters.put("Role",selectedItem);
                      parameters.put("Active",active);
                      return parameters;
                  }
                };
           /**     {
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        if (response.headers == null)
                        {
                            // cant just set a new empty map because the member is final.
                            response = new NetworkResponse(
                                    response.statusCode,
                                    response.data,
                                    Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                                    response.notModified,
                                    response.networkTimeMs);


                        }

                        return super.parseNetworkResponse(response);
                    }

                };**/

                RequestQueue requestQueue=Volley.newRequestQueue(EditEmployeeDetail.this);
                requestQueue.add(stringRequest);

                Intent intent=new Intent(EditEmployeeDetail.this,EmployeeDetails.class);
                      startActivity(intent);
                 finish();
            }

         /**   Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e(TAG,response);
                    EditEmployees editEmployees=new EditEmployees();
                    editEmployees.execute(response);
                }
            };
            Response.ErrorListener onPostsError=new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG,error.toString());
                }
            };**/
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


     private class EditEmployees extends AsyncTask<String,Void,String> {
        String message;
        @Override
         protected String doInBackground(String... response) {
             try {
                 JSONObject jsonObject=new JSONObject(response[0]);
                 String resultCode=jsonObject.getString("ResultCode");
                 message=jsonObject.getString("Message");
             } catch (JSONException e) {
                 e.printStackTrace();
             }
             return message;
         }
         public void onPostExecute(String msg){
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
         }
     }
 }

