package com.example.spaceconcordia.spacecadets.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spaceconcordia.spacecadets.Bluetooth.bluetooth_viewmaker;
import com.example.spaceconcordia.spacecadets.MainActivity;
import com.example.spaceconcordia.spacecadets.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDialog extends DialogFragment{
    private static final String TAG = "InsertAssignmentDialog";


    protected Button Buttoon_Standalone;
    private ListView Bluetooth_Listview;
    protected ArrayAdapter<String> AdapterBluetoothList;
    private BluetoothAdapter LocalBluetoothAdapter;
    private static String SelectedBluetoothAdapter;
    ArrayList<String> Name;
    ArrayList<String> Address;

    public BluetoothDialog(){
        SelectedBluetoothAdapter = "test";
    }

    @Nullable
    @Override
   public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
       View view = inflator.inflate(R.layout.fragment_bluetooth, container, false);
        Bluetooth_Listview = view.findViewById(R.id.Bluetooth_Dialog_Listview);


            Set<BluetoothDevice> pairedDevices = LocalBluetoothAdapter.getBondedDevices();

            if (!pairedDevices.isEmpty()) {

                Name = new ArrayList<>();
                Address = new ArrayList<>();

                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                   Name.add(device.getName());
                   Address.add(device.getAddress());
                }

                AdapterBluetoothList = new bluetooth_viewmaker(getActivity(), Name,Address);
                Bluetooth_Listview.setAdapter(AdapterBluetoothList);
            }


        Buttoon_Standalone = view.findViewById(R.id.Button_Bluetooth_Dialog_StandAlone);

        Buttoon_Standalone.setOnClickListener(new Button.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(getContext(), "Stand Alone Mode", Toast.LENGTH_LONG).show();
               getDialog().dismiss();
               }
       });
        Bluetooth_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectedBluetoothAdapter = Address.get(position);
                Toast.makeText(getActivity(), SelectedBluetoothAdapter, Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        });
       return view;
   }

   //this function pass the bluetooth adapter to the dialog
   public void passBTadapter(BluetoothAdapter BTadapter){
       LocalBluetoothAdapter = BTadapter;
   }
    public String GetSelectedBluetoothAdapter(){
        return SelectedBluetoothAdapter;
    }
}
