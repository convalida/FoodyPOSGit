package com.convalida.android.foodypos;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopSaleTaskFragment extends Fragment {
    boolean isTaskExecuting=false;
    static List<SalesData> salesList;
    static String finalAmount,finalOrder;

    interface TopSaleTaskCallback{
        void onPostExecute();
        void onPostFailure();
    }

    public TopSaleTaskCallback topSaleTaskCallback;
    public ExecuteTask executeTask;

    public void onAttach(Context context){
        super.onAttach(context);
        topSaleTaskCallback= (TopSaleTaskCallback) context;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void onDetach(){
        super.onDetach();
        topSaleTaskCallback=null;
    }

    public void startTask() {
        if(!isTaskExecuting){
            String param1=null;
            if(getArguments()!=null){
                param1=getArguments().getString("response");
            }
            executeTask=new ExecuteTask();
            executeTask.execute(param1);
        }

    }

    public void updateExecutingStatus(boolean isExecuting){
        this.isTaskExecuting=isExecuting;
    }

    private class ExecuteTask extends AsyncTask<String,Void, List<SalesData>>{

        double totalAmount=0.0,amountRounded,currentAmount;
        int totalOrder,orderCurrent;

        int flagResult=1;


        @Override
        protected List<SalesData> doInBackground(String... response) {
            salesList=new ArrayList<>();
            try {
                JSONObject jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
                else{
                    flagResult=1;
                    JSONArray topThree=jsonObject.getJSONArray("TopRestaurentSale");
                    for(int i=0;i<topThree.length();i++) {
                        JSONObject jsonObject1 = topThree.getJSONObject(i);
                        String name = jsonObject1.getString("CustomerName");
                        String num = jsonObject1.getString("ContactNumber");
                        String amount = jsonObject1.getString("TotalAmount");
                        String order = jsonObject1.getString("TotalOrder");
                        String customerId = jsonObject1.getString("CustomerId");
                        currentAmount = Double.parseDouble(amount);
                        orderCurrent = Integer.parseInt(order);
                        SalesData salesData = new SalesData();
                        salesData.setName(name);
                        salesData.setImageText(String.valueOf(name.charAt(0)));
                        salesData.setPhone(num);
                        salesData.setCustomerId(customerId);
                        //    salesData.setAmmount(amount);
                        salesData.setAmmount("$ " + amount);
                        salesData.setOrders(order);
                        salesList.add(salesData);
                        totalAmount = currentAmount + totalAmount;
                        totalOrder = orderCurrent + totalOrder;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            amountRounded=Math.round(totalAmount *100.0)/100.0;
            finalAmount=String.valueOf("$"+amountRounded);
            // finalAmount=String.valueOf("$"+totalAmount);
            finalOrder=String.valueOf(totalOrder);
            return salesList;

        }
        public void onPostExecute(List<SalesData> salesList){
            super.onPostExecute(salesList);
           // Toast.makeText(getContext(),"onPostExecute of TaskFragment called",Toast.LENGTH_LONG).show();
            if(topSaleTaskCallback!=null) {
                if (flagResult == 1) {
                    Log.e("TopSaleTaskFragment", "OnPostExecute of fragment called");
                    topSaleTaskCallback.onPostExecute();
                } else if (flagResult == 0) {
                    topSaleTaskCallback.onPostFailure();
                }
            }
        }
    }
}
