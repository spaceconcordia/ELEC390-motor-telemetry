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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.example.spaceconcordia.spacecadets.Bluetooth.BluetoothDialog;
import com.example.spaceconcordia.spacecadets.Bluetooth.bluetoothRx;

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
    private BluetoothDevice BTrocket;
    private ConnectThread BTthread;
    private BluetoothDialog BTdialog;


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

        BluetoothSelect(); // Initial bluetooth connection

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
                    BluetoothSelect();
                    return false;
                }
            });


            return true;
        }

        protected void setupUI () {
            this.launchButton = findViewById(R.id.launchButton);
            this.emergencyStopButton = findViewById(R.id.emergencyStopButton);
        }

        /***
         ---------  ONLY BLUETOOTH CODE BELOW THIS ----------
        ***/

        public void setBTrocket(BluetoothDevice device){
            BTrocket = device;
            BTdialog.dismiss();
            ConnectThread BTthread = new ConnectThread(BTrocket);
            BTthread.run();
        }

        protected void BluetoothSelect(){

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
            //Ask the used which bluetooth adapter to connect to.
            if (LocalBluetoothAdapter != null && LocalBluetoothAdapter.isEnabled()) {
                BTdialog = new BluetoothDialog();
                BTdialog.passBTadapter(LocalBluetoothAdapter); // This function pass the bluetooth adapter to the fragment

                BTdialog.show(getSupportFragmentManager(), "Select Bluetooth Module");
            }
        }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;


        public ConnectThread(BluetoothDevice device) {
            Toast.makeText(MainActivity.this, "ConnectThread", Toast.LENGTH_SHORT).show();

            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) { }
            mmSocket = tmp;
            Toast.makeText(MainActivity.this, "MM SOCKET FIX", Toast.LENGTH_SHORT).show();

        }

        public void run() {

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                Toast.makeText(MainActivity.this, "Trying to connect", Toast.LENGTH_SHORT).show();
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                Toast.makeText(MainActivity.this, "Unable to Connect", Toast.LENGTH_SHORT).show();

                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
/*
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);*/
            Toast.makeText(MainActivity.this, "Connection Successful", Toast.LENGTH_SHORT).show();
            ConnectedThread RocketThread = new ConnectedThread(mmSocket);
            RocketThread.run();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;


        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            Toast.makeText(MainActivity.this, "loop", Toast.LENGTH_SHORT).show();

            /*
            while (true) {

                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer); //byte counter
                    bluetoothRx.receive(buffer.toString());


                } catch (IOException e) {
                    break;
                }


            }*/
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    }




