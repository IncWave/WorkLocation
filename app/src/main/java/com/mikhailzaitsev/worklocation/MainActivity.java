package com.mikhailzaitsev.worklocation;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.mikhailzaitsev.worklocation.Db.Db;
import com.mikhailzaitsev.worklocation.Fragments.MapFragment;
import com.mikhailzaitsev.worklocation.Fragments.GroupsFragment;
import com.mikhailzaitsev.worklocation.Fragments.StatisticFragment;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity {

    private static final int REQUEST_CODE = 969;
    List<AuthUI.IdpConfig> providers;
    private ImageButton goSecondFragmentButton;
    private ImageButton goFirstFragmentButton;
    private ImageButton goZeroFragmentButton;

    private ViewPager viewPager;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private StatisticFragment statisticFragment;
    private GroupsFragment groupsFragment;
    private MapFragment mapFragment;

    //current user
    private static String currentUserId;
    private static String currentUserName;
    private static Uri currentUserUri;

    //AsyncTask
    Timer timerDb;
    Timer timerInternet;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //////////////////////////////////////////////Init and choose authentication providers
        /////////////////////////////////////////////providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
        /////////////////////////////////////////////        new AuthUI.IdpConfig.GoogleBuilder().build()); //Google Builder
        /////////////////////////////////////////////showSignInActivity();
        DELETE_THIS();


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

        groupsFragment = GroupsFragment.newInstance(calculateCurrentUserIdThatCouldBeShowed());
        mapFragment = MapFragment.newInstance();
        statisticFragment = StatisticFragment.newInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Db.newInstance();
        setRepeatingAsyncTaskDb();
        setRepeatingAsyncTaskInternet(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerDb.cancel();
        timerDb.purge();
        timerInternet.cancel();
        timerInternet.purge();
        timerDb = null;
        timerInternet = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Db.newInstance().deleteDb();
        System.gc();
        viewPager.clearOnPageChangeListeners();
        locationManager.removeUpdates(locationListener);
        locationListener = null;
    }




    private void setRepeatingAsyncTaskDb() {
        timerDb = new Timer();

        TimerTask timerTaskDb = new TimerTask() {
            @Override
            public void run() {
                try {
                    new CheckLocalDbChanged().execute(mapFragment, groupsFragment);
                } catch (Exception e) {
                    Log.e("TAG","DataBase changes checking error(Exception) " + e.getMessage());
                }
            }
        };

        timerDb.schedule(timerTaskDb, 0, 2000);
    }


    private void setRepeatingAsyncTaskInternet(final MainActivity activity){
        timerInternet = new Timer();
        TimerTask timeTaskInternet = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (flag){
                        activity.startActivity(new Intent(activity, NoInternetConnectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }else {
                        new CheckInternetConnectionMain(activity).execute();
                    }
                }catch (Exception e){
                    Log.e("TAG","Internet connection checking error(Exception) " + e.getMessage());
                }
            }
        };

        timerInternet.schedule(timeTaskInternet, 0,3000);
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
                        groupsFragment.setCurrentLocation(location);
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
        setCurrentUserId("RdKxPTuHXRdNENmEd6vrg15dzTs1");
        setCurrentUserName("Mikhail Zaitsev");
        setCurrentUserUri(Uri.parse("dfdf"));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
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
        };
        Objects.requireNonNull(locationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
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
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 2:
                    return statisticFragment;
                case 1:
                    return mapFragment;
                default:
                case 0:
                    return groupsFragment;
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
        MainActivity.currentUserId = currentUserId;
    }
    public static String getCurrentUserName() {
        return currentUserName;
    }
    private void setCurrentUserName(String currentUserName) {
        MainActivity.currentUserName = currentUserName;
    }
    public static Uri getCurrentUserUri() {
        return currentUserUri;
    }
    private void setCurrentUserUri(Uri currentUserUri) {
        MainActivity.currentUserUri = currentUserUri;
    }

    public void sendNotification(String whatGeofences){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder((this),"chanel_id")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Title")
                        .setContentText(whatGeofences);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).notify(1, notification);
    }

    private String calculateCurrentUserIdThatCouldBeShowed(){
        String[] alphab = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
                "r", "s", "t", "u", "v", "w", "x", "y", "z", "A","B","C","D","E","F","G","H","I","J","K","L",
                "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] codes = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 ", " 9 ", " 10 ", " 11 ", " 12 "
                , " 13 ", " 14 ", " 15 ", " 16 ", " 17 ", " 18 ", " 19 ", " 20 ", " 21 ", " 22 ", " 23 ", " 24 "
                , " 25 ", " 26 "," 27 "," 28 "," 29 "," 30 "," 31 "," 32 "," 33 "," 34 "," 35 "," 36 "
                ," 37 "," 38 "," 39 "," 40 "," 41 "," 42 "," 43 "," 44 "," 45 "," 46 "," 47 "," 48 "," 49 "," 50 "
                ," 51 "," 52 "};
        String result = currentUserId.substring(0,5);
        String strnum = " " + currentUserId.substring(5) + " ";
        for (int i = 0; i < alphab.length; i++) {
            strnum = strnum.replaceAll(alphab[i], codes[i]);
        }
        String[] words = strnum.split(" ");
        int num = 0;
        for (int i = 0; i <words.length; i++){
            if (words[i].length() != 0) {
                num += (Integer.parseInt(words[i]))*i;
            }
        }
        return result + num + words.length + currentUserName.length();
    }

}
