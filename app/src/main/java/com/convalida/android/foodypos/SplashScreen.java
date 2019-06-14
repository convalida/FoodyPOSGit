package com.convalida.android.foodypos;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=2000;
    String mail, pass;
    private static final String TAG="SplashScreen";
    ImageView img;
  //  int orientation;
   // Activity activity;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity=SplashScreen.this;
     /**  int orientation=this.getResources().getConfiguration().orientation;
       if(orientation== Configuration.ORIENTATION_PORTRAIT) {**/
           setContentView(R.layout.activity_splash_screen);
      /** }
       else{
           setContentView(R.layout.splash_landscape);
       }**/


     /**   if (getIntent().getExtras() != null) {
        //    for (String key : getIntent().getExtras().keySet()) {
              //  if (key.equals("body")) {
                String title=getIntent().getExtras().getString("title");
                    String msg = Objects.requireNonNull(getIntent().getExtras()).getString("body");
                    assert msg != null;
                    String[] individualStrings = msg.split(" ");
                    String order = individualStrings[1];
                    String orderNum = order.substring(1);

                    Intent intent = new Intent(getApplicationContext(), OnClickOrder.class);
                    //  intent.putExtra("Msg",msg);
                    intent.putExtra("Order num", orderNum);


                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
                    @SuppressLint("ResourceType") Notification notification = new NotificationCompat.Builder(this, "my_channel_01")
                            .setContentTitle(title)
                            .setContentText(msg)
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(R.drawable.icon)
                            .setAutoCancel(true)
                            .setColor(getResources().getColor(R.color.colorAccent))
                            .build();
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                    // NotificationManager managerCompat= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    managerCompat.notify(123, notification);

              //  }
          //  }
        } else {**/
     if(savedInstanceState==null){

            img = findViewById(R.id.splashImage);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);
                    mail = sp.getString("mailid", "");
                    Log.e(TAG, "Mail id is " + mail);
                    pass = sp.getString("password", "");
                    Log.e(TAG, "password is " + pass);
                    if (getIntent().hasExtra("Extra key")) {
                        Intent intent = new Intent(SplashScreen.this, OrderList.class);
                        startActivity(intent);
                    } else {
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
            }, SPLASH_TIME_OUT);
        }
    }
    }
//}
