package com.example.appgym.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.appgym.R;
import com.example.appgym.admin.fragment.chan.TapChanFragment;
import com.example.appgym.admin.fragment.diadiem.DiaDiemFragment;
import com.example.appgym.admin.fragment.nguc.TapNgucFragment;
import com.example.appgym.admin.fragment.tay.TapTayFragment;
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
        DatabaseReference myRef = database.getReference().child("QuanLyBan");
        myRef.setValue("Hello, World!");
        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_admin);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Tập ngực");
        loadFragment(new TapNgucFragment());

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_tap_nguc:
                    toolbar.setTitle("Tập ngực");
                    fragment = new TapNgucFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_tap_bung:
                    toolbar.setTitle("Tập bụng");
                    fragment = new TapNgucFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_tap_tay:
                    toolbar.setTitle("Tập tay");
                    fragment = new TapTayFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_tap_chan:
                    toolbar.setTitle("Tập chân");
                    fragment = new TapChanFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_dia_diem:
                    toolbar.setTitle("Địa điểm");
                    fragment = new DiaDiemFragment();
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


}