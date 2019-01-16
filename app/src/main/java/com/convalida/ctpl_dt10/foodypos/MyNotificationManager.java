package com.convalida.ctpl_dt10.foodypos;

import android.app.LauncherActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MyNotificationManager {
    private Context context;
    private static MyNotificationManager mInstance;
    public static final int ID_BIG_NOTIFICATION=234;

    public MyNotificationManager(Context context){
        this.context=context;
    }

    public static synchronized MyNotificationManager getmInstance(Context context){
        if(mInstance==null){
            mInstance=new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title,String body){
      /** NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context,Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.foodypos)
                .setContentTitle(title)
                .setContentText(body);

        Intent resultIntent=new Intent(context,OrderList.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager!=null){
            notificationManager.notify(1,mBuilder.build());**/

      Intent intent=new Intent(context,com.convalida.ctpl_dt10.foodypos.OrderList.class);
     //   TaskStackBuilder stackBuilder=TaskStackBuilder.create(context);
       // stackBuilder.addNextIntentWithParentStack(intent);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       // intent.setAction(Intent.ACTION_MAIN);
       // intent.addCategory(Intent.CATEGORY_LAUNCHER);
       // intent.putExtra("Extra key",1);
   /**     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);**/
   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      PendingIntent pendingIntent=PendingIntent.getService(context.getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
      Notification notification=new Notification.Builder(context.getApplicationContext())
              .setContentTitle(title)
              .setContentText(body)
              .setContentIntent(pendingIntent)
              .setSmallIcon(R.mipmap.logo)
              .build();
      NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      assert notificationManager != null;
      notificationManager.notify(1,notification);

    //udemy
     //   PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
      /**  PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context,Constants.CHANNEL_ID)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,mBuilder.build());**/
           // notificationManager.displayNotification(1,mBuilder.build());
        }
  /**   NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context,Constants.CHANNEL_ID);
     mBuilder.setSmallIcon(R.mipmap.logo);
     Intent intent=new Intent(context,OrderList.class);
     PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
     mBuilder.setContentIntent(pendingIntent);
    // mBuilder.setLargeIcon(BitmapFactory.decodeResource(getR))
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(1,mBuilder.build());**/
    }

   /** public void displayNotification(String title,String body, Intent intent){
        PendingIntent pendingIntent=PendingIntent.getActivity(context,ID_BIG_NOTIFICATION,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context,Constants.CHANNEL_ID);
        Notification notification;
        notification=mBuilder.setSmallIcon(R.mipmap.logo).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .build();
        notification.flags |=Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(ID_BIG_NOTIFICATION,notification);
    }**/


//}
