package com.convalida.ctpl_dt10.foodypos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

//import retrofit2.Response;


public class Login extends AppCompatActivity {
    EditText email, pass;
    TextInputLayout inputLayoutEmail, inputLayoutPassword;
    Button signIn;
    CheckBox rememberCheck;
    public static final String BASE_URL="http://business.foodypos.com/App/Api.asmx/";
    private static final String TAG="Login";
    private RequestQueue requestQueue;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emailAddress);
        pass = findViewById(R.id.password);
        inputLayoutEmail = findViewById(R.id.input_email);
        inputLayoutPassword = findViewById(R.id.input_password);
        signIn = findViewById(R.id.signInBtn);
        rememberCheck=findViewById(R.id.rememberMe);
    }

    public void forgotPassword(View view) {
        Intent intent = new Intent(Login.this, ForgotPassword.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void signUp(View view) {
   //     Uri uri = Uri.parse("http://www.convalidatech.com/ContactUs.aspx");
     //   Intent intent = new Intent(Intent.ACTION_VIEW, uri);
       // startActivity(intent);
        Intent intentSignUp=new Intent(Login.this, AddEmployeeDetail.class);
        startActivity(intentSignUp);
    }

    public void signIn(View view) {

        String mail = email.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (mail.isEmpty() || password.isEmpty()) {
            if (mail.isEmpty()) {
                /**  int ecolor = R.color.hightlight; // whatever color you want
                 String estring = "Please enter a valid email address";
                 @SuppressLint("ResourceAsColor") ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
                 SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                 ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);**/
                /**    Drawable dr=getResources().getDrawable(R.drawable.error_icon);
                 dr.setBounds(0,0,40,40);
                 email.setCompoundDrawables(null,null,dr,null);
                 email.setError("Email id is required",null);**/

                email.setError("Email id is required");
                // email.setError(ssbuilder);
                requestFocus(email);
            } else if (password.isEmpty()) {
                pass.setError("Password is required");
                requestFocus(pass);
            }

        }
        //  inputLayoutEmail.setError("Email id required");
        //requestFocus(email);
        //  signIn.setBackgroundColor(Integer.parseInt("#000"));
        //}
        else if (!isValidEmail(mail)) {
            email.setError("Please enter a valid email id");
            requestFocus(email);
        } else {
            // launch main activity
            //login successful
         //   login(mail,password);
            requestQueue= Volley.newRequestQueue(Login.this);
            GsonBuilder gsonBuilder=new GsonBuilder();
            gson=gsonBuilder.create();
            fetchPosts(mail,password);
            if(rememberCheck.isChecked()) {
                if(!mail.equals("") && !password.equals("")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("mailid", email.getText().toString());
                    editor.putString("password", pass.getText().toString());
                    editor.apply();
                }
            }

         //   startActivity(intent);
        }

/**       if(password.isEmpty()) {
 pass.setError("Password is required");
 requestFocus(pass);
 }**/
        /** if(password.equals("")){
         //  Toast.makeText(getApplicationContext(),"Password is required",Toast.LENGTH_LONG).show();
         inputLayoutPassword.setError("Password is required");
         requestFocus(pass);
         }**/
        // if(mail.equals("")&&password.equals("")){
        //   signIn.setBackgroundColor(getResources().getColor(R.color.hightlight));
        //}
    }

    private void fetchPosts(String mail, String password) {
         String url ="http://business.foodypos.com/App/Api.asmx/GetLogin?email="+mail+"&password="+password;
     /**   try {
            String encoded= URLEncoder.encode(url,"UTF-8");
            StringRequest stringRequest=new StringRequest(Request.Method.GET,encoded,onPostsLoaded,onPostsError);
            requestQueue.add(stringRequest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }**/

     String encodedPassword=URLEncoder.encode(password);
     String encodedUrl="http://business.foodypos.com/App/Api.asmx/GetLogin?email="+mail+"&password="+encodedPassword;
        StringRequest stringRequest=new StringRequest(Request.Method.GET,encodedUrl,onPostsLoaded,onPostsError);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
  private final Response.Listener<String> onPostsLoaded=new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
          Log.e(TAG,"Response: "+response);
          try {
              JSONObject jsonObject=new JSONObject(response);
              String result=jsonObject.getString("result");
             // String msg=jsonObject.getString()
              if(result.equals("0")){
                  Toast.makeText(getApplicationContext(),"Invalid credentials",Toast.LENGTH_LONG).show();

              }
              else if(result.equals("1")){
                  Toast.makeText(getApplicationContext(),"Login successful", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(Login.this, MainActivity.class);
                  startActivity(intent);
                  String restId=jsonObject.getString("RestaurantId");
                  String restName=jsonObject.getString("RestaurentName");
                  String name=jsonObject.getString("UserName");
                  SharedPreferences preferences=getSharedPreferences("RestaurantId",MODE_PRIVATE);
                  SharedPreferences.Editor editor=preferences.edit();
                  editor.putString("Id",restId);
                  editor.putString("Name",restName);
                  editor.putString("userName",name);
                  editor.apply();
              }
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
  };

    private final Response.ErrorListener onPostsError=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG,error.toString());
            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
        }
    };



  /**  private ApiInterface getInterfaceSevice(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface mInterfaceService=retrofit.create(ApiInterface.class);
        return mInterfaceService;
    }**/

   /** private void login(final String mail, String password) {
        ApiInterface mApiService=this.getInterfaceSevice();
        Call<GetLogin> mService=mApiService.authenticate(mail,password);
        mService.enqueue(new Callback<GetLogin>() {
            @Override
            public void onResponse(Call<GetLogin> call, Response<GetLogin> response) {
                Log.e(TAG,"Response "+response);
                GetLogin mGetLoginObject=response.body();
                String returnedResposnse=mGetLoginObject.isGetLogin;
                Log.e(TAG,"Returned "+returnedResposnse);
                if(returnedResposnse.trim().equals("1")){
                    Log.e(TAG,"Login Successful");
                }
                if(returnedResposnse.trim().equals("0")){
                    Log.e(TAG,"Result code=0");
                }
            }

            @Override
            public void onFailure(Call<GetLogin> call, Throwable t) {
                call.cancel();
                Log.e(TAG,"Please check your network connection");
            }
        });
    }**/

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}