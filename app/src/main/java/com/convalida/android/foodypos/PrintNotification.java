package com.convalida.android.foodypos;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Set;

public class PrintNotification extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /**   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //  Activity currentActivity=getApplicationContext().getCurrentA
            PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
            String jobName = getApplicationContext().getString(R.string.app_name)+ " Document";
            printManager.print(jobName, new MyPrintDocumentAdapter(this),null);
        }
**/
    // doPrint();
        findBluetoothDevice();
        //finish();
    }

    public void doPrint(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //  Activity currentActivity=getApplicationContext().getCurrentA
            PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
            String jobName = getApplicationContext().getString(R.string.app_name)+ " Document";
            printManager.print(jobName, new MyPrintDocumentAdapter(this),null);
        }
      //  finish();
    }

    void findBluetoothDevice(){
        try{
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter==null){
                Toast.makeText(getApplicationContext(),"No bluetooth adapter found",Toast.LENGTH_LONG).show();
            }
            if(!bluetoothAdapter.isEnabled()){
                Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBT,0);
            }
            Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
            if(pairedDevice.size()>0){
                for(BluetoothDevice pairedDev:pairedDevice){
                    //if(pairedDev.getName().equals("")){
                        bluetoothDevice=pairedDev;
                        Toast.makeText(getApplicationContext(),"Bluetooth printer attached "+pairedDev.getName(),Toast.LENGTH_LONG).show();
                        break;
                   // }
                }
            }
            else{

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
