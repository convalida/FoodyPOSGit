package com.example.ctpl_dt10.foodypos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class AmountCustomer extends AppCompatActivity {
    TextView itemPrice,tax,tip,total,taxPercen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_customer);

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.x=-20;
        params.height=320;
        params.width=370;
        params.y=-10;
        this.getWindow().setAttributes(params);

        itemPrice=findViewById(R.id.itemPriceValue);
        tax=findViewById(R.id.taxValue);
        tip=findViewById(R.id.tipValue);
        total=findViewById(R.id.totalValue);
        taxPercen=findViewById(R.id.taxPer);

        String itemPriceValue=getIntent().getStringExtra("ItemPrice");
        String taxValue=getIntent().getStringExtra("Tax");
        String tipValue=getIntent().getStringExtra("Tip");
        String totalValue=getIntent().getStringExtra("Total");
        String taxPer=getIntent().getStringExtra("TaxPer");

        itemPrice.setText("$"+itemPriceValue);
        tax.setText("$"+taxValue);
        tip.setText("$"+tipValue);
        total.setText("$"+totalValue);
        taxPercen.setText("("+taxPer+"%)");
    }
}
