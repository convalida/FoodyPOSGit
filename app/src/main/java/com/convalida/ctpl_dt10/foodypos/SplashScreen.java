package com.convalida.ctpl_dt10.foodypos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=2000;
    String mail, pass;
    private static final String TAG="SplashScreen";
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        img=findViewById(R.id.splashImage);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",MODE_PRIVATE);
                mail=sp.getString("mailid","");
                Log.e(TAG,"Mail id is "+mail);
                pass=sp.getString("password","");
                Log.e(TAG,"password is "+pass);
                if(getIntent().hasExtra("Extra key")){
                    Intent intent=new Intent(SplashScreen.this,OrderList.class);
                    startActivity(intent);
                }
                else {
                    if (mail.equals("") || pass.equals("")) {
                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}