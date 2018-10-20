package com.example.spaceconcordia.spacecadets.Bluetooth;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.spaceconcordia.spacecadets.R;

import java.util.ArrayList;


//This class inflate and populate each elements of the listview
public class bluetooth_viewmaker extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> BluetoothName;
    private final ArrayList<String> BluetoothAddress;

        //Constructor
    public bluetooth_viewmaker(Activity context, ArrayList<String> BluetoothNameIN, ArrayList<String> BluetoothAddressIN) {
        super(context, R.layout.bluetoothlist, BluetoothNameIN);
        this.context = context;
        this.BluetoothName = BluetoothNameIN;
        this.BluetoothAddress = BluetoothAddressIN;

    }

    //This function general all the Views and populate the Adapter at once
    @Override
    public View getView(int Index, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //inflate current Course display
        View rowView= inflater.inflate(R.layout.bluetoothlist, null, true);

        //Populate each textview element of course_and_grades
        TextView TexttBluetoothName = (TextView) rowView.findViewById(R.id.Bluetooth_NAME);
        TextView textBluetoothAddress = (TextView) rowView.findViewById(R.id.Bluetooth_ADDRESS);

        TexttBluetoothName.setText(BluetoothName.get(Index));
        textBluetoothAddress.setText("Address : " + BluetoothAddress.get(Index));

        return rowView;
    }
}

