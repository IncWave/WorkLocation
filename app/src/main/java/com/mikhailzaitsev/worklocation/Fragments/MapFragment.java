package com.mikhailzaitsev.worklocation.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhailzaitsev.worklocation.Db.Group;
import com.mikhailzaitsev.worklocation.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

   private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
   private GoogleMap googleMap;
   private SeekBar seekBar;
   private Marker marker;
   private ArrayList<Group> arrayListGroups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
       // }
        arrayListGroups = Group.makeGroup();
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


    public static MapFragment newInstance() {
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return new MapFragment();
    } //New Instance

    public MapFragment() {
        // Required empty public constructor
    } //Empty constructor


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        getLocationPermission();
        MapsInitializer.initialize(getContext());
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        initialiseMap();
    }

    private void initialiseMap(){
        for (int i = 0; i<arrayListGroups.size(); i++ )
            drawCircle(new LatLng(arrayListGroups.get(i).getLatitude(),
                            arrayListGroups.get(i).getLongitude()),
                    arrayListGroups.get(i).getRadius(),
                    arrayListGroups.get(i).getGroupName());
    }

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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        drawCircle(new LatLng(location.getLatitude(),location.getLongitude()),100, "THIS IS NAME");
    }

    private void drawCircle(LatLng point, int radius, final String name){

        googleMap.addCircle(new CircleOptions()
                        .center(point)
                        .radius(radius)
                        .clickable(true));

        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(final Circle circle) {
                if (marker != null){
                    marker.remove();
                }
                marker = googleMap
                        .addMarker(new MarkerOptions()
                                .position(circle.getCenter())
                                .draggable(true)
                                .title(name));
                marker.setTag(circle);

                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        circle.setCenter(marker.getPosition());
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        circle.setCenter(marker.getPosition());
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        circle.setCenter(marker.getPosition());
                    }
                });

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if (i>= 15)
                        circle.setRadius(i);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if (seekBar.getProgress()>= 15)
                            circle.setRadius(seekBar.getProgress());
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (seekBar.getProgress()>= 15)
                            circle.setRadius(seekBar.getProgress());
                    }
                });
                seekBar.setProgress((int)circle.getRadius());
            }
        });
    }
}
