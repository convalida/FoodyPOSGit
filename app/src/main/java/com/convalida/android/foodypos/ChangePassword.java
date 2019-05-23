package com.convalida.android.foodypos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    String old_password,new_password,confirm_password,restId,email,newPass,confirmPass;
    SharedPreferences sharedPreferences;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            ActionBar actionBar=getSupportActionBar();
            actionBar.setTitle("FoodyPOS");
        }
        newPassword=findViewById(R.id.newPassword);
        confirmPassword=findViewById(R.id.confirmPassword);
        oldPassword=findViewById(R.id.oldPassword);
        submit=findViewById(R.id.submit_password);

        submit.setEnabled(false);
        submit.setBackgroundColor(Color.parseColor("#ffccaa"));

        newPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId== EditorInfo.IME_ACTION_NEXT){
                    newPass=newPassword.getText().toString();
                    if(newPass.length()>0) {
                        if (8 > newPass.length() || newPass.length() > 15) {
                            newPassword.setError("Password must be 8 to 15 characters long");
                            newPassword.requestFocus();
                            return true;
                        }
                    }else{
                        newPassword.setError("New password is required");
                        newPassword.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        confirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                confirmPass=confirmPassword.getText().toString();
                newPass=newPassword.getText().toString();
                if(newPass.length()>0) {
                    if (8 > newPass.length() || newPass.length() > 15) {
                        newPassword.setError("Password must be 8 to 15 characters long");
                        newPassword.requestFocus();
                        return true;
                    }
                }
                /** else{
                    newPassword.setError("New password is required");
                    newPassword.requestFocus();
                    return true;
                }**/
                return false;
            }
        });

        newPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                newPass=newPassword.getText().toString().trim();
                confirmPass=confirmPassword.getText().toString().trim();
                if(confirmPass.length()>0){
                    if(8 > confirmPass.length() || confirmPass.length()>15){
                        confirmPassword.setError("Password must be 8 to 15 characters long");
                        confirmPassword.requestFocus();
                    }
                    else{
                        if(newPass.length()>0 && (8 < newPass.length() && newPass.length() < 15) ){
                            if(!newPass.equals(confirmPass)){
                                confirmPassword.setError("Password must be 8 to 15 characters long");
                                confirmPassword.requestFocus();
                            }
                        }
                    }
                }

                return false;
            }
        });

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
                    String url=Constants.BASE_URL+"ChangePaaword";
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
