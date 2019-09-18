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
import com.mikhailzaitsev.worklocation.R;

import java.lang.ref.WeakReference;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

   private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
   private GoogleMap googleMap;
   private View view;
   private SeekBar seekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // if (getArguments() != null) {
       // }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
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
        MapFragment fragment = new MapFragment();
        //Bundle args = new Bundle();
        //args.putBoolean();
        //fragment.setArguments(args);
        return fragment;
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
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    setMyLocEnabled();
                }else {
                    googleMap.setMyLocationEnabled(false);
                }
            }
            break;
        }
    }

    static private class PermissionAsyncTask extends AsyncTask<Void,Void,Void> {
        private final WeakReference<Context> weakReferenceContext;

        PermissionAsyncTask(Context context){
            this.weakReferenceContext = new WeakReference<>(context);
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
        //Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
       // Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
        drawCircle(new LatLng(location.getLatitude(),location.getLongitude()),100, "THIS IS NAME");
    }

    private void drawCircle(LatLng point, int radius, final String name){

        final Circle circle = googleMap
                .addCircle(new CircleOptions()
                        .center(new LatLng(point.latitude,point.longitude))
                        .radius(radius)
                        .clickable(true));

        final Marker marker = googleMap
                .addMarker(new MarkerOptions()
                        .position(circle.getCenter())
                        .draggable(true)
                        .title(name));
        marker.setTag(circle);


        googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(final Circle circle) {
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        if (i>15)
                        circle.setRadius(i);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if (seekBar.getProgress()>15)
                            circle.setRadius(seekBar.getProgress());
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (seekBar.getProgress()>15)
                            circle.setRadius(seekBar.getProgress());
                    }
                });
                seekBar.setProgress((int)circle.getRadius());
            }
        });

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
    }

}
