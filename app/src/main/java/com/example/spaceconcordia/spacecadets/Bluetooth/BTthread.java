package com.example.spaceconcordia.spacecadets.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.example.spaceconcordia.spacecadets.Data_Types.BigData;
import com.example.spaceconcordia.spacecadets.MainActivity;
import com.example.spaceconcordia.spacecadets.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BTthread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    /**
     Background Thread that handle the bluetooth reception and transmission
     **/
    public BTthread(BluetoothSocket socket) {

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


        while (true) {

            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer); //byte counter


            } catch (IOException e) {
                break;
            }


        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void Transmit(byte[] bytes) {
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