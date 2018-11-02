package com.example.spaceconcordia.spacecadets;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
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

import java.util.UUID;

import com.example.spaceconcordia.spacecadets.Bluetooth.BTthread;
import com.example.spaceconcordia.spacecadets.Bluetooth.BluetoothDialog;
import com.example.spaceconcordia.spacecadets.Bluetooth.DisconnectDialog;
import com.example.spaceconcordia.spacecadets.Bluetooth.OfflineTestThread;
import com.example.spaceconcordia.spacecadets.Bluetooth.packetanalysis;
import com.example.spaceconcordia.spacecadets.Data_Types.BigData;
import com.example.spaceconcordia.spacecadets.Data_Types.Pressure_Sensor;

import java.io.IOException;

/// Activity allows user to access launch and emergency stop buttons directly, and access to screenselectdialog
public class MainActivity extends AppCompatActivity {

    //Items
    private Button emergencyStopButton;
    private MenuItem launchButton;
    private MenuItem BluetoothConnectButton;
    private ListView sensorListView;
    private TextView BTstatusText;
    private Menu menu;

    //Icons
    private Drawable BluetoothConnect;
    private Drawable BluetoothDisc;

    //Bluetooth
    private BluetoothAdapter LocalBluetoothAdapter;
    private BluetoothDevice BTrocket;
    private BluetoothDialog BTdialog;
    private DisconnectDialog DiscDialog;

    private Handler writeHandler;

    //Bluetooth Thread
    private BTthread RocketThread; //Actual bluetooth thread
    private OfflineTestThread OfflineThread; // This thread is for offline (Emulator) testing

    private Boolean BTconnected;
    private Boolean OfflineThreadActivated;

    //PRESENT DATA
    private BigData PresentData;
    private char CurrentStatus;

    private packetanalysis PacketAnalysis;

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

    }



    /// OnClickListeners for the Actionbar items
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);

            this.menu = menu;

            launchButton = menu.findItem(R.id.screenSelectActionButton);
            BluetoothConnectButton = menu.findItem(R.id.BluetoothActionButton);

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

            BluetoothConnectButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(!BTconnected) {

                        //If not Connected, Start BT connection
                        BluetoothSelect();

                    } else if(OfflineThreadActivated){

                        //If simulation thread active, simply kill it;
                        KillThreads();
                    } else if(BTconnected){
                        //If Bluetooth is connected, ask for confirmation;
                        DiscDialog = new DisconnectDialog();
                        DiscDialog.show(getSupportFragmentManager(),"Disconnect Dialog");
                    }
                    return false;
                }
            });


            return true;
        }

        protected void setupUI () {
            this.emergencyStopButton = findViewById(R.id.emergencyStopButton);
            this.sensorListView = findViewById(R.id.sensorListView);
            this.BTstatusText = findViewById(R.id.BTStatusTextview);
            this.BluetoothConnect = getResources().getDrawable(R.drawable.oc_bluetooth);
            this.BluetoothDisc = getResources().getDrawable(R.drawable.oc_disconnectbt);

        }

    //This Update the function menu icons when invalidateOptionsMenu() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

            BluetoothConnectButton = menu.findItem(R.id.BluetoothActionButton);
            if (BTconnected || OfflineThreadActivated) {
                BluetoothConnectButton.setIcon(BluetoothDisc);
            } else {
                BluetoothConnectButton.setIcon(BluetoothConnect);
            }
        return super.onPrepareOptionsMenu(menu);
    }

        /***
         *
         *
         ---------  ONLY BLUETOOTH CODE BELOW THIS ----------
         *
         *
        ***/

        public void setBTrocket(BluetoothDevice device){
            BTrocket = device;
            BTdialog.dismiss();
            ConnectThread BTthread = new ConnectThread(BTrocket);
            BTthread.run();
        }

        //This kills both Bluetooth and offline simulation thread;
    public void KillThreads(){
        if(BTconnected) {
            RocketThread.KillThread();
            BTconnected = false;
        }
        if(OfflineThreadActivated) {
            OfflineThread.KillThread();
            OfflineThreadActivated = false;
        }
        BTstatusText.setText("Disconnected");
        invalidateOptionsMenu();
    }

    protected void BluetoothSelect(){

            //Stop all existing threads

        KillThreads();

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
            invalidateOptionsMenu();
            PacketAnalysis = new packetanalysis();
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
        invalidateOptionsMenu();
        PacketAnalysis = new packetanalysis();

    }

    private void packethandler(String packet){

        /**
         *
         * This function is called each time a packet is received!
         *
         */
        CurrentStatus = PresentData.parse(packet); // this function parse the packet

        String StatusText = "";
        switch (CurrentStatus){
            case 'B':
                StatusText = "Bad Packet";
                BTstatusText.setBackgroundColor(Color.RED);
                break;
            case 'I':
                StatusText = "Idle";
                BTstatusText.setBackgroundColor(Color.GREEN);
                break;
            case 'F':
                StatusText ="Fired";
                BTstatusText.setBackgroundColor(Color.YELLOW);

                break;
            case 'X':
                StatusText = "E. Stop"; // Emergency Stop
                BTstatusText.setBackgroundColor(Color.RED);

                break;
            case 'x':
                StatusText = "D. Stop"; // Disconnect Stop
                BTstatusText.setBackgroundColor(Color.RED);
                break;
            case 'S':
                StatusText ="Simulated";
                BTstatusText.setBackgroundColor(Color.LTGRAY);
                break;
            case 'C':
                StatusText ="Completed";
                BTstatusText.setBackgroundColor(Color.BLUE);
                break;
            case 'D':
                StatusText ="Bad Connection";
                BTstatusText.setBackgroundColor(Color.RED);
                break;
        }

        // Calculate Frequency
        int freq = PacketAnalysis.FrequencyCalc();
        // Calculate Health
        int health = PacketAnalysis.PacketHealth(CurrentStatus);
        // get total bad packet
        int BadPacket = PacketAnalysis.getBadPacketTotal();

        //Add the frequency to the Status text
        if (freq != 0){
            StatusText += "-" +freq+"Hz";
        }
        //Add the Health to the Status text
        if (health != 0){
            StatusText += "-QL:" +health+"%";
        }
        StatusText += "-Bad:" +BadPacket;



        BTstatusText.setText(StatusText);

        // Display the sensors in a listview
        if (CurrentStatus != 'B') { // refresh display if the packet was bad!
            ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, PresentData.getAllSensorsByString());
            sensorListView.setAdapter(adapter);
        }
    }

}




