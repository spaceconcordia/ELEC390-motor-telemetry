package com.example.spaceconcordia.spacecadets;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.UUID;

import com.example.spaceconcordia.spacecadets.Bluetooth.BTthread;
import com.example.spaceconcordia.spacecadets.Bluetooth.BluetoothDialog;
import com.example.spaceconcordia.spacecadets.Bluetooth.OfflineTestThread;
import com.example.spaceconcordia.spacecadets.Data_Types.BigData;
import com.example.spaceconcordia.spacecadets.Data_Types.Pressure_Sensor;

import java.io.IOException;

/// Activity allows user to access launch and emergency stop buttons directly, and access to screenselectdialog
public class MainActivity extends AppCompatActivity {


    private Button emergencyStopButton;
    private MenuItem launchButton;
    private MenuItem BluetoothConnectButton;
    private ListView sensorListView;

    //Bluetooth
    private BluetoothAdapter LocalBluetoothAdapter;
    private BluetoothDevice BTrocket;
    private BluetoothDialog BTdialog;
    private Handler writeHandler;
    private TextView RawPacket;
    private TextView SensorsCount;

    //Bluetooth Thread
    private BTthread RocketThread; //Actual bluetooth thread
    private OfflineTestThread OfflineThread; // This thread is for offline (Emulator) testing

    private Boolean BTconnected;
    private Boolean OfflineThreadActivated;

    //PRESENT DATA
    private BigData PresentData;


    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setupUI();

        emergencyStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BTconnected) {

                    //Action of Emergency Stop Button

                    Message msg = Message.obtain();
                    msg.obj = "X";
                    writeHandler.sendMessage(msg);
                }
            }
        });

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSensor = (String) (sensorListView.getItemAtPosition(position));

                Intent intent = new Intent(MainActivity.this, SingleSensorDisplayActivity.class);
                intent.putExtra("sensorName", selectedSensor);
                startActivity(intent);
            }
        });

        BTconnected = false;
        OfflineThreadActivated = false;
        PresentData = new BigData(); // Initialize PresentData

        BluetoothSelect(); // Initial bluetooth connection
        RawPacket = findViewById(R.id.RawPacket);
        SensorsCount = findViewById(R.id.SensorsCount);
    }



    /// OnClickListeners for the Actionbar items
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            launchButton = menu.findItem(R.id.screenSelectActionButton);

            launchButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(BTconnected) {


                        // Action of launch button

                        Message msg = Message.obtain();
                        msg.obj = "S";
                        writeHandler.sendMessage(msg);
                    }
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
            this.emergencyStopButton = findViewById(R.id.emergencyStopButton);
            this.sensorListView = findViewById(R.id.sensorListView);
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

            //Stop all existing threads
            if(BTconnected) {
                RocketThread.KillThread();
                BTconnected = false;
            }
            if(OfflineThreadActivated) {
                OfflineThread.KillThread();
                OfflineThreadActivated = false;
            }

            LocalBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (LocalBluetoothAdapter == null) {
                //Emulator cannot handle Bluetooth
                Toast.makeText(this, R.string.Toast_Emulator, Toast.LENGTH_LONG).show();
                OfflineThreadStarter();
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
            //Toast.makeText(MainActivity.this, "ConnectThread", Toast.LENGTH_SHORT).show();

            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) { }

            mmSocket = tmp;
          //  Toast.makeText(MainActivity.this, "MM SOCKET FIX", Toast.LENGTH_SHORT).show();

        }

        public void run() {

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                //Toast.makeText(MainActivity.this, "Trying to connect", Toast.LENGTH_SHORT).show();
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
            RocketThread = new BTthread(mmSocket, new Handler() {

                @Override
                public void handleMessage(Message message) {

                    String s = (String) message.obj;
                    //PresentData.parse(s);
                    if (!s.isEmpty()) {
                        packethandler(s);
                    }
                }
            });
            writeHandler = RocketThread.getWriteHandler();
            BTconnected = true;
            RocketThread.start();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public void OfflineThreadStarter() {

        OfflineThread = new OfflineTestThread( new Handler() {

            @Override
            public void handleMessage(Message message) {

                String s = (String) message.obj;
                //PresentData.parse(s);
                if (!s.isEmpty()) {
                    packethandler(s);
                }
            }
        });
        OfflineThreadActivated = true;
        OfflineThread.start();
        Toast.makeText(MainActivity.this, "Offline Thread Started", Toast.LENGTH_SHORT).show();

    }

    private void packethandler(String packet){

        /**
         *
         * This function is called each time a packet is received!
         *
         */
        int Nbsensors = PresentData.parse(packet); // this function parse the packet
        RawPacket.setText(packet);
        SensorsCount.setText("Nb of sensors : " + String.valueOf(Nbsensors));

        // Display the sensors in a listview
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PresentData.getAllSensorsByString());
        sensorListView.setAdapter(adapter);

    }

}




