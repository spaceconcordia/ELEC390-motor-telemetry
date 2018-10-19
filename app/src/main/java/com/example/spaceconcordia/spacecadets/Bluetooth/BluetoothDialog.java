package com.example.spaceconcordia.spacecadets;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spaceconcordia.spacecadets.Bluetooth.BTpaired;

import java.util.ArrayList;

public class BluetoothDialog extends DialogFragment{
    private static final String TAG = "InsertAssignmentDialog";


    protected Context mContext;
    protected Button Buttoon_Standalone;
    protected ListView BluetoothList;
    protected BTpaired BTpair;

    @Nullable
    @Override
   public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
       View view = inflator.inflate(R.layout.fragment_bluetooth, container, false);
        //Toast.makeText(getContext(), BTpair.Name[0], Toast.LENGTH_LONG).show();

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

    public void passData(Context context, BTpaired BTpairIN) {
        mContext = context;
        BTpair = BTpairIN;
    }

}
