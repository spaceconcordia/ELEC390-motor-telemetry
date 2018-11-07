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

    private  Handler readHandler;
    private  Handler writeHandler;

    private boolean stop;

    private static final char DELIMITER = '\n';
    private String rx_buffer = "";

    private int PingTime = 250; //Ping time in ms
    private long CurrentTime = 0;
    private long LastPingTime = 0;
    private long LastPacketReceived = 0;

    /**
     Background Thread that handle the bluetooth reception and transmission
     **/
    public BTthread(BluetoothSocket socket, Handler handler) {

        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        stop = false;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        this.readHandler = handler;

        writeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                write((String) message.obj);
            }
        };
        LastPacketReceived = System.currentTimeMillis();

    }

    private String read() {

        String s = "";

        try {
            // Check if there are bytes available
            if (mmInStream.available() > 0) {

                // Read bytes into a buffer
                byte[] inBuffer = new byte[1024];
                int bytesRead = mmInStream.read(inBuffer);

                // Convert read bytes into a string
                s = new String(inBuffer, "ASCII");
                s = s.substring(0, bytesRead);
            }

        } catch (Exception e) {
        }

        return s;
    }



    public void run() {

        // Loop continuously, reading data, until thread.interrupt() is called
        while (!this.isInterrupted() && !stop) {


            // Make sure things haven't gone wrong
            if ((mmInStream == null) || (mmOutStream == null)) {
                break;
            }

            //Ping Connection check code
            CurrentTime = System.currentTimeMillis();
            if (CurrentTime > LastPingTime + PingTime){
                write("P");
                LastPingTime = CurrentTime;
            }

            // Read data and add it to the buffer
            String s = read();
            if (s.length() > 0) {
                rx_buffer += s;
                LastPacketReceived = System.currentTimeMillis();
            }
            // Look for complete messages
            parseMessages();

            //Timeout
            if (LastPacketReceived+2000 < System.currentTimeMillis()){
                sendToReadHandler("T");
            }
        }

        // If thread is interrupted, close connections
             cancel();
            sendToReadHandler("Q");
    }

    /* Call this from the main activity to send data to the remote device */
    private void write(String s) {

        try {
            // Add the delimiter
            s += DELIMITER;

            // Convert to bytes and write
            mmOutStream.write(s.getBytes());

        } catch (Exception e) {
        }
    }

    private void parseMessages() {

        // Find the first delimiter in the buffer
        int inx = rx_buffer.indexOf(DELIMITER);

        // If there is none, exit
        if (inx == -1)
            return;

        // Get the complete message
        String s = rx_buffer.substring(0, inx);

        // Remove the message from the buffer
        rx_buffer = rx_buffer.substring(inx + 1);

        // Send to read handler
        sendToReadHandler(s);

        // Look for more complete messages
        parseMessages();
    }

    private void sendToReadHandler(String s) {

        Message msg = Message.obtain();
        msg.obj = s;
        readHandler.sendMessage(msg);
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    public Handler getWriteHandler() {
        return writeHandler;
    }

    public void KillThread(){
        stop = true;
    }

}