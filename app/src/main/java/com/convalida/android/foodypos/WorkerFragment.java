package com.convalida.android.foodypos;

import android.os.Bundle;
import android.app.Fragment;

public class WorkerFragment extends Fragment {
    private String data;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setData(String data){
        this.data=data;
    }

    public String getData(){
        return data;
    }
}
