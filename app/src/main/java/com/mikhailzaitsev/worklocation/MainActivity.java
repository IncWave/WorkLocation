package com.mikhailzaitsev.worklocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.Fragments.MapFragment;
import com.mikhailzaitsev.worklocation.Fragments.GroupsFragment;
import com.mikhailzaitsev.worklocation.Fragments.StatisticFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private static final int REQUEST_CODE = 969;
    List<AuthUI.IdpConfig> providers;
    private ImageButton goSecondFragmentButton;
    private ImageButton goFirstFragmentButton;
    private ImageButton goZeroFragmentButton;
    private ViewPager viewPager;

    //current user
    private static String currentUserId;
    private static String currentUserName;
    private static Uri currentUserUri;
    private static Location currentUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////////////////////////////////////////////Init and choose authentication providers
        /////////////////////////////////////////////providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
        /////////////////////////////////////////////        new AuthUI.IdpConfig.GoogleBuilder().build()); //Google Builder
        /////////////////////////////////////////////showSignInActivity();
        DELETE_THIS();


        //geofencingClient
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(this);

        goZeroFragmentButton = findViewById(R.id.activity_main_go_zero_fragment_button);
        goFirstFragmentButton = findViewById(R.id.activity_main_go_first_fragment_button);
        goSecondFragmentButton = findViewById(R.id.activity_main_go_second_fragment_button);

        //ViewPager find and init
        viewPager = findViewById(R.id.activity_main_view_pager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        goZeroFragment();
                        break;
                    case 1:
                        goFirstFragment();
                        break;
                    case 2:
                        goSecondFragment();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void showSignInActivity() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.CustomTheme)
                .setLogo(R.drawable.logolian)
                .build(), REQUEST_CODE);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK || resultCode == RESULT_CANCELED) {
                // Successfully signed in, so Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                setCurrentUserId(user.getIdToken(true).toString());
                setCurrentUserName(user.getDisplayName());
                setCurrentUserUri(user.getPhotoUrl());
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this,"This application cannot work without those permissions",Toast.LENGTH_LONG).show();
                        showSignInActivity();
                        return;
                    }
                }else if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) || PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                    Toast.makeText(this,"This application cannot work without those permissions",Toast.LENGTH_LONG).show();
                    showSignInActivity();
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 43111, 50, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        GroupsFragment.newInstance().setCurrentLocation(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                });
            }
            else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Toast.makeText(this, "" + response.getError().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
*/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    public void DELETE_THIS(){
        // Successfully signed in, so Get User
        setCurrentUserId("asdasdsd");
        setCurrentUserName("Mikhail Zaitsev");
        setCurrentUserUri(Uri.parse("dfdf"));
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4111, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Db.newInstance().setLocation(location);
                Toast.makeText(getApplicationContext(),"Location Changed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        });
    }


    public void onClickOrientation(View view) {
        switch (view.getId()){
            case R.id.activity_main_go_zero_fragment_button:
                viewPager.setCurrentItem(0);
                goZeroFragment();
                break;
            case R.id.activity_main_go_first_fragment_button:
                viewPager.setCurrentItem(1);
                goFirstFragment();
                break;
            case R.id.activity_main_go_second_fragment_button:
                viewPager.setCurrentItem(2);
                goSecondFragment();
                break;
        }
    }

    private void goZeroFragment(){
        goZeroFragmentButton.setImageResource(R.drawable.list_36dp);
        goFirstFragmentButton.setImageResource(R.drawable.map_pin_grey36dp);
        goSecondFragmentButton.setImageResource(R.drawable.chart_grey36dp);
    }
    private void goFirstFragment(){
        goZeroFragmentButton.setImageResource(R.drawable.list_grey36dp);
        goFirstFragmentButton.setImageResource(R.drawable.map_pin_36dp);
        goSecondFragmentButton.setImageResource(R.drawable.chart_grey36dp);
    }
    private void goSecondFragment(){
        goZeroFragmentButton.setImageResource(R.drawable.list_grey36dp);
        goFirstFragmentButton.setImageResource(R.drawable.map_pin_grey36dp);
        goSecondFragmentButton.setImageResource(R.drawable.chart_36dp);
    }

    private class CustomPagerAdapter extends FragmentPagerAdapter{

        CustomPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 2:
                    return StatisticFragment.newInstance("Third");
                case 1:
                    return MapFragment.newInstance();
                default:
                case 0:
                    return GroupsFragment.newInstance();
            }
        }


        @Override
        public int getCount() {
            return 3;
        }
    }



    public static String getCurrentUserId() {
        return currentUserId;
    }
    private void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
    public static String getCurrentUserName() {
        return currentUserName;
    }
    private void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }
    public static Uri getCurrentUserUri() {
        return currentUserUri;
    }
    private void setCurrentUserUri(Uri currentUserUri) {
        this.currentUserUri = currentUserUri;
    }
    private static Location getCurrentUserLocation(){
        return currentUserLocation;
    }
    public void setCurrentUserLocation(Location currentUserLocation) {
        MainActivity.currentUserLocation = currentUserLocation;
    }
}
