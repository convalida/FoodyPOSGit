package com.example.ctpl_dt10.foodypos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by CTPL-DT10 on 10/18/2018.
 */

public class CheckNetwork {
    private static final String TAG=CheckNetwork.class.getSimpleName();
    public static boolean isNetworkAvailable (final Context context){
      //  NetworkInfo networkInfo=(NetworkInfo) ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
       NetworkInfo activeNetworkInfo = null;
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
        activeNetworkInfo=connectivityManager.getActiveNetworkInfo();
        }
        if(activeNetworkInfo==null){
            new AlertDialog.Builder(context)
                    .setTitle("Internet connection is required")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(context,context.getClass());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            return false;
        }
     else{
           return true;
        }
    }
}
