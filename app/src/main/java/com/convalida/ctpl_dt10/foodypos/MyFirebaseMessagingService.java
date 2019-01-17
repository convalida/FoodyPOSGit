package com.convalida.ctpl_dt10.foodypos;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG="MyFirebaseMessaginging";

    public void onNewToken(String s){
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);


    }

    public void onTokenRefresh(){

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
    /**   if(remoteMessage.getData().size()>0){
            Log.e(TAG,"Data Payload: "+remoteMessage.getData().toString());
           // Toast.makeText(getApplicationContext(),"Data Payload: "+remoteMessage.getData().toString(),Toast.LENGTH_LONG).show();
           try {
                JSONObject jsonObject=new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
       }
       else{**/
            Intent intent=new Intent(getApplicationContext(),OrderList.class);
            //intent.setAction(Intent.ACTION_MAIN);
            // intent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,0);
            @SuppressLint("ResourceType") Notification notification=new NotificationCompat.Builder(this,"my_channel_01")
       //     Notification notification=new Notification.Builder(getApplicationContext())
                    .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .build();
            NotificationManagerCompat managerCompat=NotificationManagerCompat.from(getApplicationContext());
           // NotificationManager managerCompat= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            managerCompat.notify(123,notification);

        }
//        String title=Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
  //      String body=remoteMessage.getNotification().getBody();
    //}

  /**  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sendPushNotification(JSONObject jsonObject) {
        Log.e(TAG,"Notification JSON "+jsonObject.toString());
//        Toast.makeText(getApplicationContext(),"NotificationJson "+jsonObject.toString(),Toast.LENGTH_LONG).show();
        try {
            JSONObject data=jsonObject.getJSONObject("data");
            String title=data.getString("title");
            String message=data.getString("body");

            MyNotificationManager myNotificationManager=new MyNotificationManager(getApplicationContext());
         //   Intent intent=new Intent(getApplicationContext(),OrderList.class);

        /**    if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
                NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance=NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel=new NotificationChannel(Constants.CHANNEL_ID,Constants.CHANNEL_NAME,importance);
                notificationChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
                notificationChannel.enableLights(true);
             //   notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100,200,300, 400,500, 400, 300, 200, 400});
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);
            }**/
        //    myNotificationManager.displayNotification(title,message,intent);
       //     myNotificationManager.displayNotification(title,message);
     /**       Intent intent=new Intent(getApplicationContext(),com.convalida.ctpl_dt10.foodypos.OrderList.class);
            //   TaskStackBuilder stackBuilder=TaskStackBuilder.create(context);
            // stackBuilder.addNextIntentWithParentStack(intent);
            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // intent.setAction(Intent.ACTION_MAIN);
            // intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // intent.putExtra("Extra key",1);
            /**     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);**/
        /**    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent=PendingIntent.getService(getApplicationContext(),1,intent,PendingIntent.FLAG_ONE_SHOT);
            Notification notification=new Notification.Builder(getApplicationContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.icon)
                    .build();
            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(1,notification);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }**/
}
