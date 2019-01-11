package com.convalida.ctpl_dt10.foodypos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class MyNotificationManager {
    private Context context;
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context context){
        this.context=context;
    }

    public static synchronized MyNotificationManager getmInstance(Context context){
        if(mInstance==null){
            mInstance=new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title,String body){
        NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(context,Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.foodypos)
                .setContentTitle(title)
                .setContentText(body);

        Intent resultIntent=new Intent(context,OrderList.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager!=null){
            notificationManager.notify(1,mBuilder.build());
           // notificationManager.displayNotification(1,mBuilder.build());
        }
    }

 // public static final int ID_BIG_NOTIFICATION=234;
  //public static final int ID_SMALL_NOTIFICATION=235;

 /** public MyNotificationManager(Context context){
      this.context=context;
  }

  public void showBigNotification(String title, String message, String url, Intent intent){
      PendingIntent pendingIntent=PendingIntent.getActivity(context,ID_BIG_NOTIFICATION,intent,PendingIntent.FLAG_UPDATE_CURRENT);
      NotificationCompat.BigPictureStyle bigPictureStyle=new NotificationCompat.BigPictureStyle();
      bigPictureStyle.setBigContentTitle(title);
      bigPictureStyle.setSummaryText(message);
    //  bigPictureStyle.bigPicture()
  }**/
}
