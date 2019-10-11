package com.mikhailzaitsev.worklocation.Fragments.Additional;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.mikhailzaitsev.worklocation.Db.Db;

import java.util.ArrayList;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> idOfTriggered = new ArrayList<>();
        Boolean inOrNot;
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()){
            Log.d("TAG", "GeofenceBroadcastReceiver has an error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL){
            inOrNot = true;
        }else {
            inOrNot = false;
        }
        for (int i = 0; i <triggeringGeofences.size(); i++){
            idOfTriggered.add(triggeringGeofences.get(i).getRequestId());
        }
        Db.newInstance().changeMyOnlineByUserId(idOfTriggered,inOrNot);
    }
}
