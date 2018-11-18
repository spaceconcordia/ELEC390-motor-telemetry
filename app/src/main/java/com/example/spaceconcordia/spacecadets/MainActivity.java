package com.example.spaceconcordia.spacecadets;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import com.example.spaceconcordia.spacecadets.Bluetooth.BTthread;
import com.example.spaceconcordia.spacecadets.Bluetooth.BluetoothDialog;
import com.example.spaceconcordia.spacecadets.Bluetooth.DisconnectDialog;
import com.example.spaceconcordia.spacecadets.Bluetooth.OfflineTestThread;
import com.example.spaceconcordia.spacecadets.Bluetooth.packetanalysis;
import com.example.spaceconcordia.spacecadets.Data_Types.BigData;
import com.example.spaceconcordia.spacecadets.Database.DBhelper;

import java.io.IOException;

/// Activity allows user to access launch and emergency stop buttons directly, and access to screenselectdialog
public class MainActivity extends AppCompatActivity {

    //Items
    private Button emergencyStopButton;
    private MenuItem launchButton;
    private MenuItem BluetoothConnectButton;
    private MenuItem SaveButton;
    private ListView sensorListView;

    private ArrayAdapter adapter;

    private TextView BTstatusText;
    private Menu menu;

    //DB helper
    private DBhelper DBmanager;
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


    private static String FILE_NAME = "SPACE_CADETS.txt";

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
                Toast.makeText(MainActivity.this, "EMERGENCY STOP SENT", Toast.LENGTH_LONG).show();
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

                String string = (String) (sensorListView.getItemAtPosition(position));
                String selectedSensor[] = string.split("\\r?\\n");

                Intent intent = new Intent(MainActivity.this, SingleSensorDisplayActivity.class);
                intent.putExtra("sensorName", selectedSensor[0]);
                intent.putExtra("sensorPosition", position);
                intent.putExtra("serialized_data", PresentData);
                intent.putExtra("PacketAnalysis", PacketAnalysis);
                intent.putExtra("BTconnected", BTconnected);

                startActivityForResult(intent,0);
            }
        });

        BTconnected = false;
        OfflineThreadActivated = false;
        DBmanager = new DBhelper(this);
        PresentData = new BigData(); // Initialize PresentData

        BluetoothSelect(); // Initial bluetooth connection



    }

    // This method is called when the Sensor activity is finished, if DisconnectStatus = True then disconnect
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
            if (resultCode == RESULT_OK) {
                if (requestCode == 0) { // back from Sensor Activity
                    // get String data from Intent
                    Boolean DisconnectStatus = data.getBooleanExtra("DisconnectStatus", false);
                    if (DisconnectStatus && BTconnected) {
                        //Action of Emergency Stop Button
                        Message msg = Message.obtain();
                        msg.obj = "X";
                        writeHandler.sendMessage(msg);
                        Toast.makeText(MainActivity.this, "EMERGENCY STOP SENT", Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == 10) { // Bluetooth is enabled
                    BluetoothSelect();
                    }
                }
            }





    /// OnClickListeners for the Actionbar items
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);

            this.menu = menu;

            launchButton = menu.findItem(R.id.LaunchButton);
            BluetoothConnectButton = menu.findItem(R.id.BluetoothActionButton);
            SaveButton = menu.findItem(R.id.Save_Button);

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
            SaveButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    savetofile(getCurrentFocus());
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

    // before updating the sensor data in the listView, clear the adapter, add the new data, and send a update notification
    public void refill(String[] sensorData, ArrayAdapter adapter) {
        adapter.clear();
        adapter.addAll(sensorData);
        adapter.notifyDataSetChanged();
    }

    public void savetofile(View v) {

        String text = "SpaceCadets";
            FILE_NAME = String.valueOf(Calendar.getInstance().getTime()) + ".txt";

            /*TODO get contents of DATABASE
             * TODO Convert into string
             */

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root+"/Rocket_Telemetry_LOG/");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            File file = new File(myDir, FILE_NAME);
            FileOutputStream fos = null;
            if (file.exists()) {
                file.delete();
            }
            try {
                if (myDir.exists()) { //Write on External Storage if the dir exist
                    fos = new FileOutputStream(file);
                    Toast.makeText(this, "Data saved to " + file.getCanonicalPath() + "/" + FILE_NAME, Toast.LENGTH_SHORT).show();
                } else { //If not write on the internal storage
                    fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                    Toast.makeText(this, "Data saved to Internal Storage : " + FILE_NAME, Toast.LENGTH_SHORT).show();
                }
                fos.write(text.getBytes());
                //todo write database contents into file with fos.write(databaseString.getBytes()) command
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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
                    startActivityForResult(enableBtIntent, 10);
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
        PacketAnalysis.GenerateStatusBarText(CurrentStatus,BTstatusText); // This function update the status bar

        // Display the sensors in a listview
        if (CurrentStatus != 'B') { // refresh display if the packet was bad!

            // TODO: i should just save the sensors in an arrayList to begin with
            String[] stringList = PresentData.getAllSensorsByString();
            ArrayList<String> lst = new ArrayList<>(Arrays.asList(stringList));

            // only set the adapter if the adapter is null, otherwise update the sensor data
            if(sensorListView.getAdapter() == null) {
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lst);
                sensorListView.setAdapter(adapter);
            }
            else {
                refill(PresentData.getAllSensorsByString(), adapter);
            }
        }
        if (CurrentStatus =='T'){ //Timed out! Disconnect Bluetooth
            KillThreads();
        }

        DBmanager.insertValues(PresentData);

    }

}




