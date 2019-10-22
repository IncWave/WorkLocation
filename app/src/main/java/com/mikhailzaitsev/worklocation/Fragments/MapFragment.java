package com.mikhailzaitsev.worklocation.Fragments;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.Db.Group;
import com.mikhailzaitsev.worklocation.Fragments.Additional.GeofenceBroadcastReceiver;
import com.mikhailzaitsev.worklocation.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

   private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
   private GoogleMap googleMap;
   private SeekBar seekBar;
    private Button saveButton;
   private ArrayList <Circle> circleArrayList;
   private ArrayList<Group> arrayListGroups;

   private GeofencingClient geofencingClient;
   private ArrayList <Geofence> geofenceArrayList;
   private PendingIntent pendingIntent;

////////////////////////////////////////////////////////////////////////permission----
    private void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            setMyLocEnabled();
        }else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
                new PermissionAsyncTask(getContext()).execute();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            setMyLocEnabled();
        }else {
            googleMap.setMyLocationEnabled(false);
        }
    }

    static private class PermissionAsyncTask extends AsyncTask<Void,Void,Void> {

        PermissionAsyncTask(Context context){
            WeakReference<Context> weakReferenceContext = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    private void setMyLocEnabled(){
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
    }
////////////////////////////////////////////////////////////////////////----permission

    public void dataHasBeenChanged(){
        initMapWithGeofencings();
        initMapWithMarkersAndCircles();
    }


    public static MapFragment newInstance() {
        return new MapFragment();
    } //New Instance

    public MapFragment() {
        // Required empty public constructor
    } //Empty constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geofencingClient = LocationServices.getGeofencingClient(Objects.requireNonNull(getContext()));
        arrayListGroups = Db.newInstance().getGroupArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        seekBar = view.findViewById(R.id.fragment_map_change_radius);
        saveButton = view.findViewById(R.id.fragment_map_save_button);
        saveButton.setVisibility(View.INVISIBLE);
        saveButton.setOnClickListener(onClick());
        return view;
    }

    private View.OnClickListener onClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setVisibility(View.INVISIBLE);
                arrayListGroups = Db.newInstance().saveCirclesChanges(circleArrayList);
                dataHasBeenChanged();
            }
        };
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mapView = view.findViewById(R.id.fragment_map_mapview);
        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();
        MapsInitializer.initialize(Objects.requireNonNull(getContext()));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        dataHasBeenChanged();
    }

    private void initMapWithMarkersAndCircles(){
        if (googleMap != null){
            googleMap.clear();
        }

        Circle circle;
        circleArrayList = new ArrayList<>();
        for (int i = 0; i<arrayListGroups.size(); i++ ) {
            circle = drawCircle(arrayListGroups.get(i), i);
            circle.setStrokeColor(R.color.colorAccent);
            circle.setStrokeWidth(9);
            circleArrayList.add(circle);
        }
    }

    private Circle drawCircle(Group group, int i){

        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(new LatLng(group.getLatitude(),
                        group.getLongitude()))
                .radius(group.getRadius())
                .clickable(true));
        circle.setTag(i);
        drawMarker(circle,group);

        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(final Circle circle) {

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if (i!=0){
                            circle.setRadius(i*50);
                        }else {
                            circle.setRadius(50);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if (seekBar.getProgress()!=0){
                            circle.setRadius(seekBar.getProgress()*50);
                        }else {
                            circle.setRadius(50);
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (seekBar.getProgress()!=0){
                            circle.setRadius(seekBar.getProgress()*50);
                        }else {
                            circle.setRadius(50);
                        }
                        saveButton.setVisibility(View.VISIBLE);
                    }
                });
                seekBar.setProgress((int)circle.getRadius()/50);
            }
        });
        return circle;
    }

    private void drawMarker(final Circle circle, final Group group){
        Marker marker = googleMap
                .addMarker(new MarkerOptions()
                        .position(circle.getCenter())
                        .draggable(true)
                        .title(group.getGroupName()));
        marker.setTag(circle);

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Circle circle1 = (Circle) marker.getTag();
                Objects.requireNonNull(circle1).setCenter(marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Circle circle1 = (Circle) marker.getTag();
                Objects.requireNonNull(circle1).setCenter(marker.getPosition());
                saveButton.setVisibility(View.VISIBLE);
            }
        });
    }

//////////////////////////////////////////////////////////////////Geofencing------

    private void initMapWithGeofencings(){
        if (pendingIntent!=null){
            geofencingClient.removeGeofences(pendingIntent);
        }
        geofenceArrayList = new ArrayList<>();
        for (int i = 0; i < arrayListGroups.size();i++) {
            geofenceArrayList.add(drawGeofence(arrayListGroups.get(i)));
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
    }

    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(geofenceArrayList);
        return builder.build();
    }

        private PendingIntent getGeofencePendingIntent(){
        if (pendingIntent != null){
            return pendingIntent;
        }
        Intent intent = new Intent(getContext(), GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent
                .getBroadcast(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private Geofence drawGeofence(Group group){
        return new Geofence.Builder()
                .setRequestId(String.valueOf(group.getGroupId()))
                .setCircularRegion(group.getLatitude(),group.getLongitude(),group.getRadius())
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

///////////////////////////////////////////////////////////////////-----Geofencing


///////////////////////////////////////////////////////////////
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
/////////////////////////////////////////////////////////////////
}