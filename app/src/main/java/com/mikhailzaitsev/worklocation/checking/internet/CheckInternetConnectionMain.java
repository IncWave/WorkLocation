package com.mikhailzaitsev.worklocation.checking.internet;

import android.os.AsyncTask;

import com.mikhailzaitsev.worklocation.MainActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class CheckInternetConnectionMain extends AsyncTask<Void,Void,Void> {

    private WeakReference<MainActivity> activityWeakReference;

    CheckInternetConnectionMain(MainActivity activity){
        activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (!isOnline()){
            activityWeakReference.get().flag = true;
        }
        return null;
    }

    private boolean isOnline(){
        try {
            int timeoutMS = 1500;
            Socket socket = new Socket();
            SocketAddress socketAddr = new InetSocketAddress("8.8.8.8", 53);
            socket.connect(socketAddr,timeoutMS);
            socket.close();
            return true;
        }catch (IOException e){return false;}
    }
}