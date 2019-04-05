package com.convalida.android.foodypos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EnterOTP extends AppCompatActivity implements TextWatcher {
    TextView mailId, timerText;
    EditText num1,num2,num3,num4,num5,num6;
    Button verify,resendOtp;
    CountDownTimer countDownTimer;
    String mail,otp;
    LinearLayout mainLayout;
    private static final String TAG="Enter OTP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        mailId=findViewById(R.id.userMailId);
        num1=findViewById(R.id.digitOne);
        num2=findViewById(R.id.digitTwo);
        num3=findViewById(R.id.digitThree);
        num4=findViewById(R.id.digitFour);
        num5=findViewById(R.id.digitFive);
        num6=findViewById(R.id.digitSix);
        verify=findViewById(R.id.verifyBtn);
        resendOtp=findViewById(R.id.resendBtn);
        mainLayout=findViewById(R.id.mainLayout);
     //   timerText=findViewById(R.id.timer);

   /**    countDownTimer=  new CountDownTimer(30000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(millisUntilFinished/1000+" s");
            }

            @Override
            public void onFinish() {
                timerText.setVisibility(View.INVISIBLE);
                verify.setVisibility(View.INVISIBLE);
                resendOtp.setVisibility(View.VISIBLE);
            }
        }.start();**/

        num1.addTextChangedListener(this);
        num2.addTextChangedListener(this);
        num3.addTextChangedListener(this);
        num4.addTextChangedListener(this);
        num5.addTextChangedListener(this);
        num6.addTextChangedListener(this);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("Mail",MODE_PRIVATE);
        mail=sharedPreferences.getString("EmailAddress","");
        mailId.setText(mail);

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Log.e(TAG,"Resend Button clicked");
                Intent intent=new Intent(EnterOTP.this,EmptyActivity.class);
                startActivity(intent);
                String url="http://business.foodypos.com/App/Api.asmx/ForgotPassword";
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("message");
                            String resultCode = jsonObject.getString("ResultCode");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                      //      if(resultCode.equals("1")){
                                Intent intent=new Intent(getApplication(),EnterOTP.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                        /**    }
                            else{
                                Intent intent=new Intent(getApplication(),ForgotP.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }**/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Sorry, some error occurred",Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String,String> getParams() throws AuthFailureError{
                        Map<String,String> parameters=new HashMap<>();
                        parameters.put("EmailAddress",mail);
                        return parameters;
                    }
                };
                RequestQueue requestQueue=Volley.newRequestQueue(EnterOTP.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()==1){
            if(num1.length()==1){
                num2.requestFocus();
            }
            if(num2.length()==1){
                num3.requestFocus();
            }
            if(num3.length()==1){
                num4.requestFocus();
            }
            if(num4.length()==1){
                num5.requestFocus();
            }
            if(num5.length()==1){
                num6.requestFocus();
            }
        }
        else if(s.length()==0){
            if(num6.length()==0){
                num5.requestFocus();
            }
            if(num5.length()==0){
                num4.requestFocus();
            }
            if(num4.length()==0){
                num3.requestFocus();
            }
            if(num3.length()==0){
                num2.requestFocus();
            }
            if(num2.length()==0){
                num1.requestFocus();
            }
        }
        if(num1.length()==1 && num2.length()==1 && num3.length()==1 && num4.length()==1 && num5.length()==1 && num6.length()==1){
            valueFilled();
        }
        else{
            valueNotFilled();
        }
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      countDownTimer.cancel();
          //      timerText.setVisibility(View.INVISIBLE);
             //   verify.setVisibility(View.INVISIBLE);
               // resendOtp.setVisibility(View.VISIBLE);
                otp=num1.getText().toString()+num2.getText().toString()+num3.getText().toString()+num4.getText().toString()+num5.getText().toString()+num6.getText().toString();
                InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),0);
                //EnterOTP.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                String url="http://business.foodypos.com/App/Api.asmx/OTP";
                StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("message");
                            String resultCode = jsonObject.getString("ResultCode");
                            if (resultCode.equals("1")) {
                                Intent intent = new Intent(EnterOTP.this, ResetPassword.class);
                                intent.putExtra("OTP",otp);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Some error occurred",Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String,String> getParams() throws AuthFailureError{
                        Map<String,String> parameters=new HashMap<>();
                        parameters.put("Otp",otp);
                        parameters.put("password","null");
                        return parameters;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(EnterOTP.this);
                requestQueue.add(stringRequest);

            }
        });
    }

    private void valueNotFilled() {
        verify.setBackgroundColor(Color.parseColor("#ffccaa"));
        verify.setEnabled(false);
        resendOtp.setBackgroundColor(Color.parseColor("#ff6501"));
        resendOtp.setEnabled(true);
    }

    private void valueFilled() {
        verify.setBackgroundColor(Color.parseColor("#ff6501"));
        verify.setEnabled(true);
     //   resendOtp.setBackgroundColor(Color.parseColor("#ffccaa"));
     //   resendOtp.setEnabled(false);
    }



}
