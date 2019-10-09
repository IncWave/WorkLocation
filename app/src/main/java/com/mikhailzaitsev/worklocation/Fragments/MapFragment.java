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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

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
   private Marker marker;
   private Button saveButton;
   private Button addButton;
   private ArrayList <Circle> circleArrayList;
   private ArrayList<Group> arrayListGroups;

   private GeofencingClient geofencingClient;
   private ArrayList <Geofence> geofenceArrayList;
   private PendingIntent pendingIntent;

   private static MapFragment mapfragment;
////////////////////////////////////////////////////////////////////////permission----
    private void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            setMyLocEnabled();
        }else {
            ActivityCompat.requestPermissions(getActivity(),
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


    public static MapFragment newInstance() {
        if (mapfragment == null){
            mapfragment = new MapFragment();
        }
        return mapfragment;
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
        return view;
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
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        initMapWithGeofencings();
        initMapWithMarkersAndCircles();
    }

    public void initMapWithMarkersAndCircles(){
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
                        int index = 0;
                        for (Circle circle1: circleArrayList){
                            if (circle1.equals(circle)){
                                index = (int)circle1.getTag();
                                Toast.makeText(getActivity(),"AAAAAAAAAAAAAAAAAAAAAAAAAA",Toast.LENGTH_LONG).show();
                            }
                        }
                        if (i==0)
                            ++i;
                        circle.setRadius(i*50);
                        arrayListGroups = Db.newInstance().saveCircleChanges(circle, index);
                        initMapWithGeofencings();
                        initMapWithMarkersAndCircles();
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
                    }
                });
                seekBar.setProgress((int)circle.getRadius()/50);
            }
        });
        return circle;
    }

    private void drawMarker(final Circle circle, final Group group){
        marker = googleMap
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
                circle1.setCenter(marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Circle circle1 = (Circle) marker.getTag();
                circle1.setCenter(marker.getPosition());
                arrayListGroups = Db.newInstance().saveCircleChanges(circle1, (int)circle1.getTag());
                initMapWithGeofencings();
                initMapWithMarkersAndCircles();
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
                .setLoiteringDelay(100)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

///////////////////////////////////////////////////////////////////-----Geofencing


//////////////////////////////////////////////////////////////////My Location Clicked-----
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }
///////////////////////////////////////////////////////////////////-----My Location Clicked


    public void sendNotification(String whatGeofences){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext(),"chanel_id")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Title")
                        .setContentText(whatGeofences);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}