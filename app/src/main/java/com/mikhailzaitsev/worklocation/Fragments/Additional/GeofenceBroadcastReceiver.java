package com.mikhailzaitsev.worklocation.Fragments.Additional;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.mikhailzaitsev.worklocation.Fragments.MapFragment;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()){
            Log.d("TAG", "GeofenceBroadcastReceiver has an error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        String whatGeofences = "";
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
        switch (geofenceTransition){
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                whatGeofences = "Dwell :";
                for( Geofence geofence: triggeringGeofences){
                    whatGeofences+= "  "+ geofence.getRequestId();
                }
                MapFragment.newInstance().sendNotification(whatGeofences);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                whatGeofences = "Exit :";
                for( Geofence geofence: triggeringGeofences){
                    whatGeofences+= "  "+ geofence.getRequestId();
                }
                MapFragment.newInstance().sendNotification(whatGeofences);
                break;
        }
    }
}
