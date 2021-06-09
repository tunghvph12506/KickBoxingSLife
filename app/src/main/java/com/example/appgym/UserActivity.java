package com.example.appgym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.appgym.user.fragment.advertisement.AdvertisementFragment;
import com.example.appgym.user.fragment.home.HomeFragment;
import com.example.appgym.user.fragment.report.ReportFragment;
import com.example.appgym.user.fragment.setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class UserActivity extends AppCompatActivity {
    ActionBar toolbar;
    public static String authAll;
    public static String usernameAll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        toolbar = getSupportActionBar();
        toolbar.setElevation(0);
        Bundle gets = getIntent().getExtras();
        authAll = gets.getString("auth");
        usernameAll = gets.getString("username");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Home");
        loadFragment(new HomeFragment());
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle(getResources().getString(R.string.userActivity_title_home));
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_advertisement:
                    toolbar.setTitle(getResources().getString(R.string.userActivity_title_advertisement));
                    fragment = new AdvertisementFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_setting:
                    toolbar.setTitle(getResources().getString(R.string.userActivity_title_setting));
                    fragment = new SettingFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}