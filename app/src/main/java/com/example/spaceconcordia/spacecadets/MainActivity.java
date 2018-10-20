package com.example.spaceconcordia.spacecadets;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.spaceconcordia.spacecadets.Bluetooth.BluetoothDialog;

import java.io.IOException;

/// Activity allows user to access launch and emergency stop buttons directly, and access to screenselectdialog
public class MainActivity extends AppCompatActivity {

    private Button launchButton;
    private Button emergencyStopButton;
    private MenuItem screenSelectButton;
    private MenuItem BluetoothConnectButton;

    //Bluetooth
    private BluetoothAdapter LocalBluetoothAdapter;
    private String SelectedBluetoothAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO what happens when the launch button is clicked
            }
        });

        emergencyStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO what happens when the emergency stop button is clicked
            }
        });

        BluetoothConnect(); // Initial bluetooth connection

        }

        /// OnClickListener for the "Display Mode" button in the action bar
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            screenSelectButton = menu.findItem(R.id.screenSelectActionButton);

            screenSelectButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(MainActivity.this, ScreenSelectDialog.class);
//                startActivity(intent);

                    ScreenSelectDialog dialog = new ScreenSelectDialog();
                    dialog.show(getSupportFragmentManager(), "Select Display Mode");
                    return false;
                }

            });

            //On click listener of actionbar bluetooth connect button
            BluetoothConnectButton = menu.findItem(R.id.BluetoothActionButton);
            BluetoothConnectButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    BluetoothConnect();
                    return false;
                }
            });


            return true;
        }

        protected void setupUI () {
            this.launchButton = findViewById(R.id.launchButton);
            this.emergencyStopButton = findViewById(R.id.emergencyStopButton);
        }

        protected void BluetoothConnect(){

            LocalBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (LocalBluetoothAdapter == null) {
                //Emulator cannot handle Bluetooth
                Toast.makeText(this, R.string.Toast_Emulator, Toast.LENGTH_LONG).show();
            } else {
                //Check if bluetooth is turned on
                if (!LocalBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 0);
                }
            }
            //Ask the used which bluetooth adapter to connecto to.
            if (LocalBluetoothAdapter != null && LocalBluetoothAdapter.isEnabled()) {
                BluetoothDialog BTdialog = new BluetoothDialog();
                BTdialog.passBTadapter(LocalBluetoothAdapter); // This function pass the bluetooth adapter to the fragment

                BTdialog.show(getSupportFragmentManager(), "Select Bluetooth Module");
                SelectedBluetoothAddress = BTdialog.GetSelectedBluetoothAdapter();


            }

        }
    }




