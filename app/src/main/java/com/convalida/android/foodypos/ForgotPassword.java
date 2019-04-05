package com.convalida.android.foodypos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class ForgotPassword extends AppCompatActivity {
    EditText mailId;
    Button submit;
    TextInputLayout inputEmail;
    RelativeLayout progressLayout;
    LinearLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    com.example.ctpl_dt10.foodypos.Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_forgot_password);
        mailId = findViewById(R.id.forgotEmail);
        submit = findViewById(R.id.submitBtn);
        progressLayout=findViewById(R.id.progressLayout);
        mainLayout=findViewById(R.id.verticalLinearLayout);
        inputEmail = findViewById(R.id.input_email);
        this.setFinishOnTouchOutside(false);
    }

    public void loginScreen(View view) {
        Intent intent = new Intent(ForgotPassword.this, Login.class);
        startActivity(intent);
    }

    public void forgotSubmit(View view) {

        final String mail = mailId.getText().toString();
        if (mail.isEmpty()) {
            mailId.setError("Email id is required");
            requestFocus(mailId);
        } else if (!isValidEmail(mail)) {
            mailId.setError("Please enter a valid email id");
            requestFocus(mailId);
        } else {
            //forgot password functionality
           Intent intent=new Intent(ForgotPassword.this,EmptyActivity.class);
            startActivity(intent);
          //  com.example.ctpl_dt10.foodypos.Utils.changeToTheme(this, com.example.ctpl_dt10.foodypos.Utils.THEME_WHITE);
          //  mainLayout.setVisibility(View.INVISIBLE);
          //  progressLayout.setVisibility(View.VISIBLE);
          //  setTheme(android.R.style.Theme_Black_NoTitleBar);


            String url="http://business.foodypos.com/App/Api.asmx/ForgotPassword";
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");
                        String resultCode = jsonObject.getString("ResultCode");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        if(resultCode.equals("1")){
                            Intent intent=new Intent(getApplication(),EnterOTP.class);
                        //    Intent intent=new Intent(getApplication(),ResetPassword.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            Intent intent=new Intent(getApplication(),ForgotPassword.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        progressLayout.setVisibility(View.INVISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Some error occurred",Toast.LENGTH_LONG).show();
                }
            }){
              protected Map<String,String> getParams() throws AuthFailureError{
                  Map<String,String> parameters=new HashMap<>();
                  parameters.put("EmailAddress",mailId.getText().toString());
                  return parameters;
              }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(ForgotPassword.this);
            requestQueue.add(stringRequest);
            SharedPreferences sharedPreferences=getSharedPreferences("Mail",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("EmailAddress",mailId.getText().toString());
            editor.apply();

        }
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    finishAndRemoveTask();
        //}
    }
}