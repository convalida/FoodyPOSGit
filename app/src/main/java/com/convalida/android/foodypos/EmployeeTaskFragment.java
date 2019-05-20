package com.convalida.android.foodypos;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class EmployeeTaskFragment extends Fragment {
     static final String TAG="EmployeeTaskFragment";
   static ArrayList<EmployeeDetailData> employeeDetailDataArrayList;
   boolean isTaskExecuting=false;

    interface EmployeeTaskCallbacks{
        void onPostExecute();
        void onPostFailure();
    }

    private EmployeeTaskCallbacks employeeTaskCallbacks;
    private GetEmployees getEmployees;

    public void onAttach(Context context){
        super.onAttach(context);
        employeeTaskCallbacks= (EmployeeTaskCallbacks) context;
        Log.e(TAG,"OnAttach of fragment");
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.e(TAG,"OnCreate of fragment");
       // if(savedInstanceState==null)
        setRetainInstance(true);
      /**  String param1=null;
        if(getArguments()!=null){
            param1=getArguments().getString("responseEmployee");

        }
        getEmployees=new GetEmployees();
        Log.e(TAG,"ExecutePosts of fragment");
        getEmployees.execute(param1);**/
    }

    public void onDetach(){
        super.onDetach();
        employeeTaskCallbacks=null;
      /**  try {
            Field childFragmentManager=Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this,null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }**/
    }

    public void startBackgroundTask(){
        if(!isTaskExecuting){
            String param1=null;
            if(getArguments()!=null){
                param1=getArguments().getString("responseEmployee");

            }
            getEmployees=new GetEmployees();
           // Log.e(TAG,"ExecutePosts of fragment");
            getEmployees.execute(param1);
        }
    }

    public void updateExecutingStatus(boolean isExecuting){
        this.isTaskExecuting=isExecuting;
    }

    private class GetEmployees extends AsyncTask<String,Void, ArrayList<EmployeeDetailData>>{
        int flagResult=1;

        @Override
        protected ArrayList<EmployeeDetailData> doInBackground(String... response) {
            try {
                Log.e(TAG,"doInBackground of Fragment");
                employeeDetailDataArrayList=new ArrayList<>();
                JSONObject jsonObject=new JSONObject(response[0]);
                if(jsonObject.has("Message")){
                    flagResult=0;
                }
                else {
                    flagResult=1;
                    JSONArray employeesArray = jsonObject.getJSONArray("EmployeeDetails");
                    for (int i = 0; i < employeesArray.length(); i++) {
                        JSONObject employeeObject = employeesArray.getJSONObject(i);
                        String name = employeeObject.getString("Username");
                        String email = employeeObject.getString("EmailId");
                        String role = employeeObject.getString("RoleType");
                        String active = employeeObject.getString("Active");
                        String accountId = employeeObject.getString("AccountId");

                        EmployeeDetailData empData = new EmployeeDetailData();
                        empData.setActive(active);
                        empData.setEmail(email);
                        empData.setName(name);
                        empData.setRole(role);
                        empData.setAcctId(accountId);
                        employeeDetailDataArrayList.add(empData);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return employeeDetailDataArrayList;
        }

        public void onPostExecute(ArrayList<EmployeeDetailData> employeeDetailDataArrayList){
            super.onPostExecute(employeeDetailDataArrayList);
            if(flagResult==1){
                Log.e(TAG,"OnPostExecute of Fragment");
                employeeTaskCallbacks.onPostExecute();
            }
            else if(flagResult==0){
                employeeTaskCallbacks.onPostFailure();
            }
        }
    }
}
