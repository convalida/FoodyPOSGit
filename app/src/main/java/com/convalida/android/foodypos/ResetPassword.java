package com.convalida.android.foodypos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ResetPassword extends AppCompatActivity  {
   // TextView backText;
    Button reset;
    LinearLayout linearLayout;
    String otp,pass,confirmPass;
    EditText password,confirmPassword;

    boolean hasPassword=false,hasConfirmPassword=false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
  //  backText=findViewById(R.id.back);
        linearLayout=findViewById(R.id.verticalLinearLayout);
        reset=findViewById(R.id.resetBtn);
        password=findViewById(R.id.newPassword);
        confirmPassword=findViewById(R.id.password);
        pass=password.getText().toString();



        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if(actionId==EditorInfo.IME_ACTION_NEXT){
                pass=password.getText().toString();
                if (!pass.equals("")) {
                    hasPassword = true;
                }
                else{
                    hasPassword=false;
                }
                if (hasPassword) {
                    if (8 > pass.length() || pass.length() > 15) {
                        password.setError("Password must be 8 to 15 characters long");
                        password.requestFocus();
                        return true;
                    }
                    else {
                    return false;
                    }
                }

            }
            return false;
            }

        });

           confirmPassword.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pass = password.getText().toString();
                    if (!pass.equals("")) {
                        hasPassword = true;
                    }
                    else{
                        hasPassword = false;
                    }
                    if (hasPassword) {
                        if (8 > pass.length() || pass.length() > 15) {
                            password.setError("Password must be 8 to 15 characters long");
                            password.requestFocus();
                            return true;
                        }

                    }
                        return false;

                }

            });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                confirmPass = confirmPassword.getText().toString();
                if (!confirmPass.equals("")) {
                    if (8 > confirmPass.length() || confirmPass.length() > 15) {
                        confirmPassword.setError("Password must be 8 to 15 characters long");
                        confirmPassword.requestFocus();
                        return true;
                    }
                    else if(!pass.equals(confirmPass)) {
                        confirmPassword.setError("Passwords do not match");
                        requestFocus(confirmPassword);
                     //   return true;
                    }

                    return false;
                }
                return false;

            }

        });
        Intent i=getIntent();
        otp=i.getStringExtra("OTP");

    reset.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pass=password.getText().toString();
            confirmPass=confirmPassword.getText().toString();
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(linearLayout.getWindowToken(),0);
            if (!pass.equals("")) {
                hasPassword = true;
            }
            else{
                hasPassword=false;
            }
            if (!confirmPass.equals("")) {
                hasConfirmPassword = true;
            }
            else{
                hasConfirmPassword=false;
            }
            int hasPasswordFlag=0, hasConfirmPasswordFlag=0;
        //    if (hasPassword && hasConfirmPassword) {
            if(hasConfirmPassword) {
                if (8 > confirmPass.length() || confirmPass.length() > 15 || !pass.equals(confirmPass)) {

                    if (8 > confirmPass.length() || confirmPass.length() > 15) {
                        //Toast.makeText(getApplicationContext(),"Password must be 8 to 15 characters long",Toast.LENGTH_LONG).show();
                        confirmPassword.setError("Password must be 8 to 15 characters long");
                        requestFocus(confirmPassword);
                    } else if (!pass.equals(confirmPass)) {
                        confirmPassword.setError("Passwords do not match");
                        requestFocus(confirmPassword);
                    }
                } else {
                    hasConfirmPasswordFlag = 1;
                }
            }

                if(hasPassword){
                    if (8 > pass.length() || pass.length() > 15) {
                        //Toast.makeText(getApplicationContext(),"Password must be 8 to 15 characters long",Toast.LENGTH_LONG).show();
                        password.setError("Password must be 8 to 15 characters long");
                        requestFocus(password);
                    }
                    else{
                        hasPasswordFlag=1;
                    }
                }
              //  }
                if(hasPasswordFlag==1 && hasConfirmPasswordFlag==1){
                final String url = Constants.BASE_URL+"OTP";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("message");
                            String resultCode = jsonObject.getString("ResultCode");
                            if (resultCode.equals("1")) {
                                Intent intent = new Intent(ResetPassword.this, Login.class);
                                startActivity(intent);
                            }
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Sorry, some error occurred", Toast.LENGTH_LONG).show();
                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("Otp", otp);
                          parameters.put("password",pass);
                        return parameters;

                    }

                };
                    RequestQueue requestQueue= Volley.newRequestQueue(ResetPassword.this);
                    requestQueue.add(stringRequest);
            }
      //  }

            else {
                if (!hasPassword) {
                    // Toast.makeText(getApplicationContext(), "Please enter the password", Toast.LENGTH_LONG).show();
                    password.setError("Please enter your password");
                    requestFocus(password);
                } /**else {
                    // Toast.makeText(getApplicationContext(), "Please re-enter the password to confirm", Toast.LENGTH_LONG).show();
                    if (8 > pass.length() || pass.length() > 15) {
                        password.setError("Password must be 8 to 15 characters long");
                        password.requestFocus();
                    }**/
                //    else {
                        if (!hasConfirmPassword) {
                            confirmPassword.setError("Please enter your password to confirm");
                            requestFocus(confirmPassword);

                    }
                }
            }

       // }
    });
    this.setFinishOnTouchOutside(false);
    }


    private void requestFocus(View view) {
        if(view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
