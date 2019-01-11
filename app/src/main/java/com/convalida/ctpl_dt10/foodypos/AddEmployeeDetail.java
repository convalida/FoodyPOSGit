package com.convalida.ctpl_dt10.foodypos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddEmployeeDetail extends AppCompatActivity {

    Button add,reset;
    EditText name,email,password,confirmPassword;
    Spinner spinner;
    boolean hasValueName =false, hasEmailValue=false, hasPsswordValue=false, hasConfirmPasswordValue=false, hasSpinnerValue=false;
    private static final String TAG="AddEmployeeDetail";
    int spinnerPosiotion;
    String restId,modifiedBy,pass;
    String selectedItem;
    String addEmployeeData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee_detail);
        reset=findViewById(R.id.reset);
        name=findViewById(R.id.EmployeeName);
        email=findViewById(R.id.EmployeeEmail);
        password=findViewById(R.id.EmployeePassword);
        add=findViewById(R.id.add);
        reset=findViewById(R.id.reset);
        confirmPassword=findViewById(R.id.EmployeeConfirmPassword);
        spinner=findViewById(R.id.spinner);

    //    addEmployeeData="{\"result\":{\"Message\":\"EmailID are already exists\",\"ResultCode\":\"0\"},\"EmployeeDetails\":[{\"Username\":\"order@metropolisgrill.com\",\"EmailId\":\"order@metropolisgrill.com\",\"RolwType\":\"Manager\",\"Active\":\"True\"}]}\n" +
      //          "\n" +
        //        "\n";



 name.addTextChangedListener(new TextWatcher() {
     @Override
     public void beforeTextChanged(CharSequence s, int start, int count, int after) {

     }

     @Override
     public void onTextChanged(CharSequence s, int start, int before, int count) {

     }

     @Override
     public void afterTextChanged(Editable s) {

         if(name.getText().toString().equals("")){
             hasValueName=false;
         }
         else{
             hasValueName=true;
         }
         valueChanged();
     }
 });

email.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(email.getText().toString().equals("")){
            hasEmailValue=false;
        }
        else{
            hasEmailValue=true;
        }
        valueChanged();
    }
});

password.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(password.getText().toString().equals("")){
            hasPsswordValue=false;
        }
        else{
            hasPsswordValue=true;
        }
        valueChanged();
    }
});

confirmPassword.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        if(confirmPassword.getText().toString().equals("")){
            hasConfirmPasswordValue=false;
        }
        else{
            hasConfirmPasswordValue=true;
        }
        valueChanged();
    }
});




        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name.setText("");
                email.setText("");
                password.setText("");
                confirmPassword.setText("");
                spinner.setSelection(0);
            }
        });


        String[] arraySpinner = new String[] {
               "Select", "Manager","Employee"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(position>0)
                {
                    selectedItem = parent.getItemAtPosition(position).toString();
                    Log.i("Selected item",selectedItem);

                    spinnerPosiotion=position;
                    hasSpinnerValue=true;
                }
                else{
                    Log.e(TAG,"Position"+position);
                    hasSpinnerValue=false;
                }
                valueChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=password.getText().toString();
                if(hasValueName && hasEmailValue && hasPsswordValue && hasConfirmPasswordValue && hasSpinnerValue) {
                    if (!isValidEmail(email.getText().toString()) || !pass.equals(confirmPassword.getText().toString())||8 > pass.length() || pass.length() > 15 ) {
                        if (!pass.equals(confirmPassword.getText().toString())) {
                            confirmPassword.setError("Passwords do not match");
                            requestFocus(confirmPassword);
                        }
                        if (!isValidEmail(email.getText().toString())) {
                            email.setError("Please enter a valid email id");
                            requestFocus(email);
                        }
                        if(8 > pass.length() || pass.length() > 15){
                            Toast.makeText(getApplicationContext(),"Password must be 8 to 15 characters long",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
                        restId=sharedPreferences.getString("Id","");
                        modifiedBy=sharedPreferences.getString("userName","");
                        String url="http://business.foodypos.com/App/Api.asmx/AddEmployee";
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("Result");
                                    String resultCode = jsonObject1.getString("ResultCode");
                                    String msg = jsonObject1.getString("Message");
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(),"Some error occured",Toast.LENGTH_LONG).show();
                            }
                        }){
                            protected Map<String,String> getParams() throws AuthFailureError{
                                Map<String,String> parameters=new HashMap<>();
                                parameters.put("RestaurantId",restId);
                                parameters.put("Name",name.getText().toString());
                                parameters.put("EmailAddresss",email.getText().toString());
                                parameters.put("RoleType",selectedItem);
                                parameters.put("Password",password.getText().toString());
                                parameters.put("ModifiedBy",modifiedBy);
                                return parameters;
                            }
                        };
                        RequestQueue requestQueue= Volley.newRequestQueue(AddEmployeeDetail.this);
                        requestQueue.add(stringRequest);
                        Intent intent=new Intent(AddEmployeeDetail.this,EmployeeDetails.class);
                        startActivity(intent);

                    }
                    //add code to add employee details in database
                    /**  String e_name=name.getText().toString();
                     String e_email=email.getText().toString();
                     String e_password=password.getText().toString();**/

                    /**   try {

                     Log.i("enter","enter");
                     JSONObject root = new JSONObject(addEmployeeData);
                     JSONObject object= root.getJSONObject("result");


                     Log.i("Message",object.getString("Message"));
                     Log.i("Result Code",object.getString("ResultCode"));

                     if(object.getString("ResultCode").equals("0"))
                     {
                     Toast.makeText(getApplicationContext(),"User Already Exist",Toast.LENGTH_SHORT).show();
                     }
                     else{
                     Toast.makeText(getApplicationContext(),""+object.getString("ResultCode"),Toast.LENGTH_SHORT).show();
                     //register successful ,activity dismiss
                     finish();
                     }

                     } catch (JSONException e) {
                     e.printStackTrace();
                     }
                     **/


                    // }
                    // else {



                    //}
                }else{
                    //disabled btn shown as default
                }
            }
        });
    }

    private void requestFocus(View view) {
        if(view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidEmail(String s) {
        return Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }

    private void valueChanged() {
        Log.e(TAG,"hasValueName: "+hasValueName+" hasEmailValue: "+hasEmailValue+" hasPasswordValue "+hasPsswordValue
                +" hasConfirmPasswordValue "+hasConfirmPasswordValue+" hasSpinnerValue "+hasSpinnerValue);
        if(!hasValueName || !hasEmailValue || !hasPsswordValue || !hasConfirmPasswordValue || !hasSpinnerValue){
          //  add.setBackgroundColor(Color.parseColor("#ff6501"));
            add.setBackgroundColor(Color.parseColor("#ffccaa"));
        }
        else {
            Log.e(TAG,""+hasValueName);
            add.setBackgroundColor(Color.parseColor("#ff6501"));
       //     add.setBackgroundColor(Color.parseColor("#ffccaa"));
        }
        if(hasValueName || hasEmailValue || hasPsswordValue || hasConfirmPasswordValue || hasSpinnerValue){
            reset.setBackgroundColor(Color.parseColor("#ff6501"));
        }
        else{
            reset.setBackgroundColor(Color.parseColor("#ffccaa"));
        }


    }
}
