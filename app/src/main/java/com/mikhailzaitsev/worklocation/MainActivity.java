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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhailzaitsev.worklocation.Fragments.MapFragment;
import com.mikhailzaitsev.worklocation.Fragments.RoomsFragment;
import com.mikhailzaitsev.worklocation.Fragments.StatisticFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity{

    private static final int REQUEST_CODE = 969;
    List<AuthUI.IdpConfig> providers;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init and choose authentication providers
        providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
                new  AuthUI.IdpConfig.GoogleBuilder().build()); //Google Builder
        showSignInActivity();

        //ViewPager find and init
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
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
            case R.id.go_zero_fragment_button:
                viewPager.setCurrentItem(0);
                break;
            case R.id.go_first_fragment_button:
                viewPager.setCurrentItem(1);
                break;
            case R.id.go_second_fragment_button:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    private class CustomPagerAdapter extends FragmentPagerAdapter{

        public CustomPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return RoomsFragment.newInstance("Second");
                case 2: return StatisticFragment.newInstance("Third");
                case 1:
                default: return MapFragment.newInstance("First");
            }
        }


        @Override
        public int getCount() {
            return 3;
        }
    }

}