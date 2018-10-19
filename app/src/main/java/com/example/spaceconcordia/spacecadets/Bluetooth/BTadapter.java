package com.example.spaceconcordia.spacecadets;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.spaceconcordia.spacecadets.Bluetooth.BTpaired;

import java.util.Set;


public class BTadapter extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;

    Boolean Connect(BTpaired BTpairList) {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false; // Emulator cant handle bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

       Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

/* that part is still buggy
if(pairedDevices.size()>0){
        // There are paired devices. Get the name and address of each paired device.
        for (BluetoothDevice device : pairedDevices) {
            BTpairList.Name[BTpairList.count] = device.getName();
            BTpairList.Address[BTpairList.count] = device.getAddress(); // MAC address
            BTpairList.count++;
        }
    }
*/
    return true;
    }

}
