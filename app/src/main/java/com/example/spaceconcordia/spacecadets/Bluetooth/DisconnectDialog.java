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

import com.example.spaceconcordia.spacecadets.MainActivity;
import com.example.spaceconcordia.spacecadets.R;

import java.util.ArrayList;
import java.util.Set;

public class DisconnectDialog extends DialogFragment{


    protected Button Button_Disc;
    protected Button Button_Cancel;


    @Nullable
    @Override
   public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
       View view = inflator.inflate(R.layout.fragment_disconnect, container, false);

        Button_Disc = view.findViewById(R.id.Button_Disconnect_Dialog_Confirm);
        Button_Cancel = view.findViewById(R.id.Button_Disconnect_Dialog_Cancel);

        Button_Disc.setOnClickListener(new Button.OnClickListener() {
           @Override
           public void onClick(View view) {
               ((MainActivity) getActivity()).KillThreads();
               getDialog().dismiss();
               }
       });

        Button_Cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });



        return view;

   }
}
