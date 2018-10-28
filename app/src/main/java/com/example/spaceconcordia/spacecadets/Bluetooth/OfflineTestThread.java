package com.example.spaceconcordia.spacecadets.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class OfflineTestThread extends Thread {

    private int SLEEPTIME = 100;
    private boolean stop;
    private  Handler readHandler;

    /**
     Background Thread that Simulate Bluetooth reception and transmission
     **/
    public OfflineTestThread(Handler handler) {

        this.readHandler = handler;
        stop = false;
    }


    public void run() {

        // Loop continuously, reading data, until thread.interrupt() is called
        while (!this.isInterrupted() && !stop) {

            try {
                Thread.sleep(SLEEPTIME);
            } catch(InterruptedException e) {
                // Process exception
            }
            //String s = "445‑A90‑21B2‑E15‑2281‑1147‑140E‑1550‑2023‑70B‑F45‑D71‑139A‑FA4‑14C7‑1F40‑E04‑15B7‑A84‑13E4‑15C7‑1FD0‑1A5F‑1FD2‑171‑2164‑2113‑5E1‑2233";


            Random r = new Random();
            int NextRandom = r.nextInt(1000);
            String s = Integer.toHexString(NextRandom);
       for (int i=1;i<29;i++){
           NextRandom = r.nextInt(1000);
           s = s.concat("-");
        s = s.concat(Integer.toHexString(NextRandom));

       }

            sendToReadHandler(s);
        }

        sendToReadHandler("Offline Thread Stopped");

    }


    private void sendToReadHandler(String s) {

        Message msg = Message.obtain();
        msg.obj = s;
        readHandler.sendMessage(msg);
    }

    public void KillThread(){
        stop = true;
    }

}