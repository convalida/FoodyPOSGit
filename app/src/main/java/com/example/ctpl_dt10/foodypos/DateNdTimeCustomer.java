package com.example.ctpl_dt10.foodypos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DateNdTimeCustomer extends AppCompatActivity {
    TextView date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_nd_time_customer);
        date=findViewById(R.id.dateText);
        time=findViewById(R.id.timeText);

        String dateValue=getIntent().getStringExtra("Date");
        String timeValue=getIntent().getStringExtra("Time");

        date.setText(dateValue);
        time.setText(timeValue);
    }
}
