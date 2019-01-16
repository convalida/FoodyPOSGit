package com.convalida.ctpl_dt10.foodypos;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManagerToken {
    private static final String SHARED_PREF_NAME="FCMSharedPref";
    private static final String TAG_TOKEN="tagtoken";

    private static SharedPrefManagerToken mInstance;
    private static Context mCtx;

    private SharedPrefManagerToken(Context context){
        mCtx=context;
    }

    public static synchronized SharedPrefManagerToken getmInstance(Context context){
        if(mInstance==null){
            mInstance=new SharedPrefManagerToken(context);
        }
        return mInstance;
    }

    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(TAG_TOKEN,token);
        editor.apply();
        return true;
    }

    public String getDeviceToken(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN,null);
    }
}
