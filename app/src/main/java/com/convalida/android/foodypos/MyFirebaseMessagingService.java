package com.convalida.android.foodypos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.print.PrintManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

//import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG="MyFirebaseMessaginging";
    Uri defaultSoundUri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    Context context;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread thread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    public void onNewToken(String s){
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);

        //SharedPrefManagerToken.getmInstance(getApplicationContext()).saveDeviceToken(s);

    }

    public void onTokenRefresh(){

    }

    /**@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        //  if(remoteMessage.getData().size()>0){
         //Log.e(TAG,"Data Payload: "+remoteMessage.getData().toString());
         // Toast.makeText(getApplicationContext(),"Data Payload: "+remoteMessage.getData().toString(),Toast.LENGTH_LONG).show();
         //try {
         //JSONObject jsonObject=new JSONObject(remoteMessage.getData().toString());
         //sendPushNotification(jsonObject);
         //} catch (JSONException e) {
         //e.printStackTrace();
        // }
         //}
         //else{**/
       /** String msg=Objects.requireNonNull(remoteMessage.getNotification()).getBody();
        assert msg != null;
        String[] individualStrings=msg.split(" ");
        String order=individualStrings[1];
        String orderNum=order.substring(1);
        int id= (int) (Math.random()*10);

        // Map<String,String> data=remoteMessage.getData();
        // data.get("vibrate");
        Intent intent=new Intent(getApplicationContext(),OnClickOrder.class);
        //  intent.putExtra("Msg",msg);
        intent.putExtra("Order num",orderNum);
        //intent.setAction(Intent.ACTION_MAIN);
        // intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        @SuppressLint("ResourceType") Notification notification=new NotificationCompat.Builder(this,"my_channel_01")
                //     Notification notification=new Notification.Builder(getApplicationContext())
                .setContentTitle(Objects.requireNonNull(remoteMessage.getNotification()).getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSmallIcon(R.drawable.notification_icon)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{1000,100,1000,100,1000})
                // .setLights()
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorAccent))
                .build();
        // intent.removeExtra("Order num");x
        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(getApplicationContext());
        // NotificationManager managerCompat= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        managerCompat.notify(id,notification);
        //Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // assert vibrator != null;
        // vibrator.vibrate(new long[] {1000, 100, 1000, 100, 1000},1);**/
        /**Log.e(TAG, Integer.toString(id));
        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){

            NotificationChannel notificationChannel=new NotificationChannel(Constants.CHANNEL_ID,Constants.CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100,200,300, 400,500, 400, 300, 200, 400});
            NotificationManager notificationManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
            //   Toast.makeText(getApplicationContext(),id.hashCode(),Toast.LENGTH_LONG).show();

        }
    }**/

           @RequiresApi(api = Build.VERSION_CODES.KITKAT)
           public void onMessageReceived(RemoteMessage remoteMessage){
               super.onMessageReceived(remoteMessage);
               String msg=Objects.requireNonNull(remoteMessage.getData()).get("body");
               assert msg != null;
               String[] individualStrings=msg.split(" ");
               String order=individualStrings[1];
               String orderNum=order.substring(1);
               int id= (int) (Math.random()*10);
               Intent intent=new Intent(getApplicationContext(),OnClickOrder.class);
               //  intent.putExtra("Msg",msg);
               intent.putExtra("Order num",orderNum);
               //intent.setAction(Intent.ACTION_MAIN);
               // intent.addCategory(Intent.CATEGORY_LAUNCHER);
               PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
               String title = remoteMessage.getData().get("title");
               String body = remoteMessage.getData().get("body");
               Notification notification = new NotificationCompat.Builder(this,"my_channel_01")
                       .setContentTitle(title)
                       .setContentText(body)
                       .setSmallIcon(R.drawable.notification_icon)
                       .setSound(defaultSoundUri)
                       .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                       .setPriority(Notification.PRIORITY_HIGH)
                       .setVisibility(Notification.VISIBILITY_PUBLIC)
                       .setVibrate(new long[]{1000,100,1000,100,1000})
                       // .setLights()
                       .setContentIntent(pendingIntent)
                       .setAutoCancel(true)
                       .setColor(getResources().getColor(R.color.colorAccent))
                       .build();
               NotificationManagerCompat managerCompat=NotificationManagerCompat.from(getApplicationContext());
               managerCompat.notify(id,notification);
               Log.e(TAG, Integer.toString(id));
               if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){

                   NotificationChannel notificationChannel=new NotificationChannel(Constants.CHANNEL_ID,Constants.CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
                   notificationChannel.enableVibration(true);
                   notificationChannel.setVibrationPattern(new long[]{100,200,300, 400,500, 400, 300, 200, 400});
                   NotificationManager notificationManager= (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                   assert notificationManager != null;
                   notificationManager.createNotificationChannel(notificationChannel);
                   //   Toast.makeText(getApplicationContext(),id.hashCode(),Toast.LENGTH_LONG).show();


               }
       /**        Intent intent1=new Intent(this,PrintNotification.class);
             //  intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent1);**/
            //   finish();
               //doPrint();
            //   findBluetoothDevice();
              // openBluetoothPrinter();
           }

         /**  protected void attachBaseContext(Context newBase){
               context= newBase;
               super.attachBaseContext();
           }**/

    private void doPrint() {
               if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                 //  Activity currentActivity=getApplicationContext().getCurrentA
        PrintManager printManager = (PrintManager) getApplicationContext().getSystemService(Context.PRINT_SERVICE);
        String jobName = getApplicationContext().getString(R.string.app_name)+ " Document";
        printManager.print(jobName, new MyPrintDocumentAdapter(getApplicationContext()),null);
    }
}

/**
 * void findBluetoothDevice(){
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                Toast.makeText(getApplicationContext(),"No bluetooth adapter found",Toast.LENGTH_LONG).show();
            }
            if(bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }
        }catch (Exception ex){

        }
}
 **/


//        String title=Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
  //      String body=remoteMessage.getNotification().getBody();
    //}

   /** @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sendPushNotification(JSONObject jsonObject) {
        Log.e(TAG,"Notification JSON "+jsonObject.toString());
//        Toast.makeText(getApplicationContext(),"NotificationJson "+jsonObject.toString(),Toast.LENGTH_LONG).show();
        try {
            JSONObject data=jsonObject.getJSONObject("data");
            String title=data.getString("title");
            String message=data.getString("body");

            MyNotificationManager myNotificationManager=new MyNotificationManager(getApplicationContext());
         //   Intent intent=new Intent(getApplicationContext(),OrderList.class);

            if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
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
            }
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




public static String getToken(Context context){
        return SharedPrefManagerToken.getmInstance(context).getDeviceToken();
}
}
