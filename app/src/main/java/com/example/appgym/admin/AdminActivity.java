package com.example.appgym.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.appgym.R;
import com.example.appgym.admin.fragment.leg.LegAdminFragment;
import com.example.appgym.admin.fragment.map.MapAdminFragment;
import com.example.appgym.admin.fragment.chest.ChestAdminFragment;
import com.example.appgym.admin.fragment.hand.HandAdminFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {
    ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_admin);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle(R.string.tv_chestchallenge);
        loadFragment(new ChestAdminFragment());

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_tap_nguc:
                    toolbar.setTitle(R.string.bottomnav_chest);
                    fragment = new ChestAdminFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_tap_bung:
                    toolbar.setTitle(R.string.bottomnav_stomach);
                    fragment = new ChestAdminFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_tap_tay:
                    toolbar.setTitle(R.string.bottomnav_hand);
                    fragment = new HandAdminFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_tap_chan:
                    toolbar.setTitle(R.string.bottomnav_leg);
                    fragment = new LegAdminFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_dia_diem:
                    toolbar.setTitle(R.string.bottomnav_map);
                    fragment = new MapAdminFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_admin, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_video_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_icon:
                Intent intent = new Intent(AdminActivity.this,AddActivity.class);
                startActivity(intent);
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }
}