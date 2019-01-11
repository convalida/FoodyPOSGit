package com.convalida.ctpl_dt10.foodypos;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    public void onNewToken(String s){
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);


    }

    public void onTokenRefresh(){

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().size()>0){

        }
        String title=Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String body=remoteMessage.getNotification().getBody();
    }
}
