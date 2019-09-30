package com.mikhailzaitsev.worklocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
import com.mikhailzaitsev.worklocation.Fragments.MapFragment;
import com.mikhailzaitsev.worklocation.Fragments.GroupsFragment;
import com.mikhailzaitsev.worklocation.Fragments.StatisticFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity{

    private static final int REQUEST_CODE = 969;
    List<AuthUI.IdpConfig> providers;
    private GeofencingClient geofencingClient;
    private ImageButton goSecondFragmentButton;
    private ImageButton goFirstFragmentButton;
    private ImageButton goZeroFragmentButton;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init and choose authentication providers
        providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
                new  AuthUI.IdpConfig.GoogleBuilder().build()); //Google Builder
        showSignInActivity();

        //geofencingClient
        geofencingClient = LocationServices.getGeofencingClient(this);

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
                switch (position){
                    case 0:goZeroFragment();
                        break;
                    case 1:goFirstFragment();
                        break;
                    case 2:goSecondFragment();
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
                .build(),REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK){
                // Successfully signed in, so Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }
            else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Toast.makeText(this, "" + response.getError().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
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




}
