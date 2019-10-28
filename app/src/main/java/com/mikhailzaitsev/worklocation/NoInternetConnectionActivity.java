package com.mikhailzaitsev.worklocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class NoInternetConnectionActivity extends AppCompatActivity {

    Timer timerInternet;
    boolean flag = false;

    @Override
    protected void onStart() {
        super.onStart();
        setRepeatingAsyncTask(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
    }

    private void setRepeatingAsyncTask(final NoInternetConnectionActivity activity){
        timerInternet = new Timer();
        final CheckInternetConnectionNoInternet checkInternetConnectionNoInternet = new CheckInternetConnectionNoInternet(activity);
        TimerTask timeTaskInternet = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (flag){
                        timerInternet.cancel();
                        timerInternet.purge();
                        activity.startActivity(new Intent(activity, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                    else {
                        checkInternetConnectionNoInternet.doInBackground();
                    }
                }catch (Exception e){
                    //
                    //
                }
            }
        };
        timerInternet.schedule(timeTaskInternet, 0,2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerInternet.cancel();
        timerInternet.purge();
        timerInternet = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


