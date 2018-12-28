package com.example.ctpl_dt10.foodypos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AmountDetails extends AppCompatActivity {
    String subTotal,taxPer,taxvalue,tip,grandTotal;
    TextView subTotalValue,taxPerVal,taxNumValue,tipValue,grandTotalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_details);
        Intent intent=getIntent();
        subTotal=intent.getStringExtra("SubTotal");
        taxPer=intent.getStringExtra("TaxPer");
        taxvalue=intent.getStringExtra("TaxValue");
        tip=intent.getStringExtra("Tip");
        grandTotal=intent.getStringExtra("GrandTotal");

        subTotalValue=findViewById(R.id.subTotalValue);
        taxPerVal=findViewById(R.id.taxPerValue);
        taxNumValue=findViewById(R.id.taxNumValue);
        tipValue=findViewById(R.id.tipValue);
        grandTotalValue=findViewById(R.id.grandTotalValue);

        subTotalValue.setText("$"+subTotal);
        taxPerVal.setText("("+taxPer+"%):");
        taxNumValue.setText("$"+taxvalue);
        tipValue.setText("$"+tip);
        grandTotalValue.setText("$"+grandTotal);

    }
}
