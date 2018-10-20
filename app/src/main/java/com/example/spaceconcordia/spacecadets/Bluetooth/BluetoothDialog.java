package com.example.spaceconcordia.spacecadets.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spaceconcordia.spacecadets.Bluetooth.bluetooth_viewmaker;
import com.example.spaceconcordia.spacecadets.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDialog extends DialogFragment{
    private static final String TAG = "InsertAssignmentDialog";


    protected Button Buttoon_Standalone;
    private BluetoothAdapter LocalBluetoothAdapter;
    private ListView Bluetooth_Listview;
    protected ArrayAdapter<String> AdapterBluetoothList;

    @Nullable
    @Override
   public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
       View view = inflator.inflate(R.layout.fragment_bluetooth, container, false);

        LocalBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Bluetooth_Listview = view.findViewById(R.id.Bluetooth_Dialog_Listview);

        if (LocalBluetoothAdapter == null) {
            //Emulator cannot handle Bluetooth
            Toast.makeText(getContext(), R.string.Toast_Emulator, Toast.LENGTH_LONG).show();
             getDialog().dismiss();
        }else {


            if (!LocalBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 0);
            }

            Set<BluetoothDevice> pairedDevices = LocalBluetoothAdapter.getBondedDevices();

            if (!pairedDevices.isEmpty()) {

                ArrayList<String> Name = new ArrayList<>();
                ArrayList<String> Address = new ArrayList<>();

                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                   Name.add(device.getName());
                   Address.add(device.getAddress());
                  //  Toast.makeText(getContext(),device.getName(), Toast.LENGTH_LONG).show();
                }

                AdapterBluetoothList = new bluetooth_viewmaker(getActivity(), Name,Address);
                Bluetooth_Listview.setAdapter(AdapterBluetoothList);
            }
        }

        Buttoon_Standalone = view.findViewById(R.id.Button_Bluetooth_Dialog_StandAlone);

        Buttoon_Standalone.setOnClickListener(new Button.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.d(TAG, "onclick: cancel button");
               getDialog().dismiss();
           }
       });
        Buttoon_Standalone.setOnClickListener(new Button.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(getContext(), "Stand Alone Mode", Toast.LENGTH_LONG).show();
               getDialog().dismiss();
               }
       });
       return view;
   }


}
