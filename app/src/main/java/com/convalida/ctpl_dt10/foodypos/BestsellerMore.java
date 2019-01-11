package com.convalida.ctpl_dt10.foodypos;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class BestsellerMore extends AppCompatActivity {
    ActionBar actionBar;
    ExpandableListView expandableListBestseller;
    BestsellerMoreAdapter bestsellerMoreAdapter;
    //  ArrayList<BestsellerHeaderList> bestsellerHeaderLists=new ArrayList<>();

    BestsellerHeaderList bestsellerHeaderList;
    BestsellerChildlist bestsellerChildlist=new BestsellerChildlist();
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestseller_more);
        Intent in=getIntent();
        if(getSupportActionBar()!=null){
            actionBar=getSupportActionBar();
        }
        if(in.getIntExtra("Flag",0)==1){
            actionBar.setTitle("Weekly Bestseller Items");
        }
        else if(in.getIntExtra("Flag",0)==2){
            actionBar.setTitle("Monthly Bestseller items");
        }
        else{
            actionBar.setTitle("Yearly Bestseller items");
        }

        expandableListBestseller=findViewById(R.id.expandableBestSellerMore);

//getData();
        ArrayList<BestsellerHeaderList> headerLists=new ArrayList<>();
        BestsellerHeaderList bestsellerHeaderList1=new BestsellerHeaderList();
        BestsellerChildlist bestsellerChildlist=new BestsellerChildlist();
        ArrayList<BestsellerChildlist> bestsellerChildlists=new ArrayList<>();
        bestsellerChildlist.setItemname("Tikka Masala Chicken");
        bestsellerChildlist.setCounting("3");
        bestsellerChildlists.add(bestsellerChildlist);
        bestsellerChildlist.setItemname("Tikka Masala Chicken");
        bestsellerChildlist.setCounting("3");
        bestsellerChildlists.add(bestsellerChildlist);
        bestsellerChildlist.setItemname("Tikka Masala Chicken");
        bestsellerChildlist.setCounting("3");
        bestsellerChildlists.add(bestsellerChildlist);
        bestsellerChildlist.setItemname("Tikka Masala Chicken");
        bestsellerChildlist.setCounting("3");
        bestsellerChildlists.add(bestsellerChildlist);
        bestsellerChildlist.setItemname("Tikka Masala Chicken");
        bestsellerChildlist.setCounting("3");
        bestsellerChildlists.add(bestsellerChildlist);
        bestsellerHeaderList1.setDay("18 November To 24 November");
        bestsellerHeaderList1.setChildlists(bestsellerChildlists);
        headerLists.add(bestsellerHeaderList1);
        BestsellerHeaderList bestsellerHeaderList2=new BestsellerHeaderList();
        BestsellerChildlist bestsellerChildlist2=new BestsellerChildlist();
        ArrayList<BestsellerChildlist> bestsellerChildlists2=new ArrayList<>();
        bestsellerChildlist2.setItemname("Tikka Masala Chicken");
        bestsellerChildlist2.setCounting("3");
        bestsellerChildlists2.add(bestsellerChildlist2);
        bestsellerChildlist2.setItemname("Tikka Masala Chicken");
        bestsellerChildlist2.setCounting("3");
        bestsellerChildlists2.add(bestsellerChildlist2);
        bestsellerChildlist2.setItemname("Tikka Masala Chicken");
        bestsellerChildlist2.setCounting("3");
        bestsellerChildlists2.add(bestsellerChildlist2);
        bestsellerChildlist2.setItemname("Tikka Masala Chicken");
        bestsellerChildlist2.setCounting("3");
        bestsellerChildlists2.add(bestsellerChildlist2);
        bestsellerChildlist2.setItemname("Tikka Masala Chicken");
        bestsellerChildlist2.setCounting("3");
        bestsellerChildlists2.add(bestsellerChildlist2);
        bestsellerHeaderList2.setDay("11 November To 17 November");
        bestsellerHeaderList2.setChildlists(bestsellerChildlists2);
        headerLists.add(bestsellerHeaderList2);
        BestsellerMoreAdapter bestsellerMoreAdapter=new BestsellerMoreAdapter(BestsellerMore.this,headerLists);
        expandableListBestseller.setAdapter(bestsellerMoreAdapter);
        int count=bestsellerMoreAdapter.getGroupCount();
        for(int i=0;i<count;i++){
            expandableListBestseller.expandGroup(i);
        }



    }
}
