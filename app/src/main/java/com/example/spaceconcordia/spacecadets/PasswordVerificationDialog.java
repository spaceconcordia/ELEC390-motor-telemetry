package com.example.spaceconcordia.spacecadets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordVerificationDialog extends DialogFragment {

    private static String password = "1234";

    protected Button enterPasswordButton;
    protected Button cancelButton;
    protected EditText passwordEditText;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_verification_dialog, container, false);

        enterPasswordButton = view.findViewById(R.id.Button_Enter_Password);
        cancelButton = view.findViewById(R.id.Button_Cancel);
        passwordEditText = view.findViewById(R.id.Title_Enter_Password);

        enterPasswordButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(passwordEditText.getText().toString().matches("")){
                    Toast.makeText(getActivity(), R.string.Password_Length_Zero_Toast, Toast.LENGTH_LONG).show();
                }
                else if(passwordEditText.getText().toString().equals(password)) {
                    if(getActivity() != null){
                        ((MainActivity) getActivity()).sendLaunchCommand();
                        getDialog().dismiss();
                    }
                }
                else{
                    Toast.makeText(getActivity(), R.string.Incorrect_Password_Toast, Toast.LENGTH_LONG).show();
                }

            }
        });

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){}
}
