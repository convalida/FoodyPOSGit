package com.convalida.android.foodypos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ResetPassword extends AppCompatActivity {
   // TextView backText;
    Button reset;
    LinearLayout linearLayout;
    String otp,pass,confirmPass;
    EditText password,confirmPassword;
    boolean hasPassword=false,hasConfirmPassword=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
  //  backText=findViewById(R.id.back);
        linearLayout=findViewById(R.id.verticalLinearLayout);
        reset=findViewById(R.id.resetBtn);
        password=findViewById(R.id.newPassword);
        confirmPassword=findViewById(R.id.password);

     // if()
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
            if (!confirmPass.equals("")) {
                hasConfirmPassword = true;
            }
            if (hasPassword && hasConfirmPassword) {
                if (8 > pass.length() || pass.length() > 15 || !pass.equals(confirmPass)) {
                    if(!pass.equals(confirmPass)) {
                        confirmPassword.setError("Passwords do not match");
                        requestFocus(confirmPassword);
                    }
                    if(8 > pass.length() || pass.length() > 15 ){
                        Toast.makeText(getApplicationContext(),"Password must be 8 to 15 characters long",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                final String url = "http://business.foodypos.com/App/Api.asmx/OTP";
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
        }
        else {
                if(pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter the password", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please re-enter the password to confirm", Toast.LENGTH_LONG).show();
                }
                }
        }
    });
    this.setFinishOnTouchOutside(false);
    }

    private void requestFocus(View view) {
        if(view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
