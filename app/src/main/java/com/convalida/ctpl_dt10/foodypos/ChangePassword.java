package com.convalida.ctpl_dt10.foodypos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePassword extends AppCompatActivity {


    EditText newPassword,oldPassword,confirmPassword;
    Button submit;
    String old_password,new_password,confirm_password,restId,email;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        newPassword=findViewById(R.id.newPassword);
        confirmPassword=findViewById(R.id.confirmPassword);
        oldPassword=findViewById(R.id.oldPassword);
        submit=findViewById(R.id.submit_password);

        submit.setEnabled(false);
        submit.setBackgroundColor(Color.parseColor("#ffccaa"));



            submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                old_password=oldPassword.getText().toString().trim();
                new_password=newPassword.getText().toString().trim();
                confirm_password=confirmPassword.getText().toString().trim();

                if(new_password.equals(confirm_password))
                {
                    sharedPreferences=getApplicationContext().getSharedPreferences("RestaurantId",MODE_PRIVATE);
                    restId=sharedPreferences.getString("Id","");
                    sharedPreferences=getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                    email=sharedPreferences.getString("mailid","");
                    String url="http://business.foodypos.com/App/Api.asmx/ChangePaaword";
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String msg = jsonObject.getString("Message");
                                String resultCode = jsonObject.getString("ResultCode");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                if(resultCode.equals("1")){
                                    Intent intent=new Intent(ChangePassword.this,Login.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(),"Password changed successfully",Toast.LENGTH_LONG).show();
                                }
                                else if(resultCode.equals("0")){
                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                                }
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
                            parameters.put("EmailAddresss",email);
                            parameters.put("OldPassword",old_password);
                            parameters.put("NewPassword",new_password);
                            return parameters;
                        }
                    };
                    RequestQueue requestQueue= Volley.newRequestQueue(ChangePassword.this);
                    requestQueue.add(stringRequest);

                }

                else{
                    confirmPassword.setError("Password do not matches");
                    confirmPassword.requestFocus();
                    submit.setEnabled(false);
                    submit.setBackgroundColor(Color.parseColor("#ffccaa"));
                }

            }
        });


     oldPassword.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

         }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             old_password=oldPassword.getText().toString().trim();
             new_password=newPassword.getText().toString().trim();
             confirm_password=confirmPassword.getText().toString().trim();

             if(old_password.isEmpty())
                {
                       submit.setEnabled(false);
                       submit.setBackgroundColor(Color.parseColor("#ffccaa"));
                }
             if(!old_password.isEmpty() && !new_password.isEmpty() && !confirm_password.isEmpty())
             {
                    submit.setEnabled(true);
                    submit.setBackgroundColor(Color.parseColor("#ff6501"));
             }
         }

         @Override
         public void afterTextChanged(Editable editable) {

         }
     });


     newPassword.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

         }

         @Override
         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             old_password=oldPassword.getText().toString().trim();
             new_password=newPassword.getText().toString().trim();
             confirm_password=confirmPassword.getText().toString().trim();

             if(new_password.isEmpty())
             {
                 submit.setEnabled(false);
                 submit.setBackgroundColor(Color.parseColor("#ffccaa"));
             }
             if(!old_password.isEmpty() && !new_password.isEmpty() && !confirm_password.isEmpty() )
             {
                 submit.setEnabled(true);
                 submit.setBackgroundColor(Color.parseColor("#ff6501"));
             }
         }

         @Override
         public void afterTextChanged(Editable editable) {

         }
     });
     confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                old_password=oldPassword.getText().toString().trim();
                new_password=newPassword.getText().toString().trim();
                confirm_password=confirmPassword.getText().toString().trim();


                if(!old_password.isEmpty() && !new_password.isEmpty() && !confirm_password.isEmpty())
                {
                   submit.setEnabled(true);
                   submit.setBackgroundColor(Color.parseColor("#ff6501"));
                    }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }
}
