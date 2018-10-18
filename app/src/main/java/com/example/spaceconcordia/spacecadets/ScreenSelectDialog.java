package com.example.spaceconcordia.spacecadets;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ScreenSelectDialog extends DialogFragment{
    private static final String TAG = "ScreenSelectDialog";

    Button singleSensorButton;
    Button multiSensorButton;
    Button overlaySensorButton;
    Button cancelButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflator, @Nullable ViewGroup container, Bundle savedInstanceState){
        view = inflator.inflate(R.layout.fragment_screen_select, container, false);

        this.setupUI();

        // Go to the SingleSensorSelectActivity on click
        singleSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SingleSensorSelectActivity.class);
                startActivity(intent);
            }
        });

        multiSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MultiSensorSelectActivity.class);
                startActivity(intent);
            }
        });

        overlaySensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OverlaySensorSelectActivity.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick: cancel button");
                getDialog().dismiss();
            }
        });

        return view;
    }

    protected void setupUI(){
        singleSensorButton = view.findViewById(R.id.singleSensorSelectButton);
        multiSensorButton = view.findViewById(R.id.multiSensorSelectButton);
        overlaySensorButton = view.findViewById(R.id.overlaySensorSelectButton);
        cancelButton = view.findViewById(R.id.cancelButton);
    }

}
