package com.convalida.android.foodypos;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManagerToken {
    private static final String TOKEN_SHARED_PREF="FCMSharedPref";
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
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(TOKEN_SHARED_PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(TAG_TOKEN,token);
        editor.apply();
        return true;
    }

    public String getDeviceToken(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(TOKEN_SHARED_PREF,Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN,null);
    }
}
