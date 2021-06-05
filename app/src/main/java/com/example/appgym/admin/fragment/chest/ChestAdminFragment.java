package com.example.appgym.admin.fragment.chest;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.appgym.R;
import com.example.appgym.admin.EditActivity;
import com.example.appgym.admin.ViewHolder_Exercise;
import com.example.appgym.admin.activity.ChestDay1Activity;
import com.example.appgym.admin.activity.ChestDay2Activity;
import com.example.appgym.admin.activity.ChestDay3Activity;
import com.example.appgym.admin.activity.ChestDay4Activity;
import com.example.appgym.admin.activity.ChestDay5Activity;
import com.example.appgym.admin.activity.ChestDay6Activity;
import com.example.appgym.admin.activity.ChestDay7Activity;
import com.example.appgym.model.Exercise;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ChestAdminFragment extends Fragment implements View.OnClickListener {
    Button btn_day1,btn_day2,btn_day3,btn_day4,btn_day5,btn_day6,btn_day7;
    public ChestAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_admin_chest, container, false);
        btn_day1 = view.findViewById(R.id.btn_chestadmin_day1);
        btn_day2 = view.findViewById(R.id.btn_chestadmin_day2);
        btn_day3 = view.findViewById(R.id.btn_chestadmin_day3);
        btn_day4 = view.findViewById(R.id.btn_chestadmin_day4);
        btn_day5 = view.findViewById(R.id.btn_chestadmin_day5);
        btn_day6 = view.findViewById(R.id.btn_chestadmin_day6);
        btn_day7 = view.findViewById(R.id.btn_chestadmin_day7);

        btn_day1.setOnClickListener(this);
        btn_day2.setOnClickListener(this);
        btn_day3.setOnClickListener(this);
        btn_day4.setOnClickListener(this);
        btn_day5.setOnClickListener(this);
        btn_day6.setOnClickListener(this);
        btn_day7.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_chestadmin_day1:
                Intent intent = new Intent(getActivity(), ChestDay1Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_chestadmin_day2:
                Intent intent2 = new Intent(getActivity(), ChestDay2Activity.class);
                startActivity(intent2);
                break;
            case R.id.btn_chestadmin_day3:
                Intent intent3 = new Intent(getActivity(), ChestDay3Activity.class);
                startActivity(intent3);
                break;
            case R.id.btn_chestadmin_day4:
                Intent intent4 = new Intent(getActivity(), ChestDay4Activity.class);
                startActivity(intent4);
                break;
            case R.id.btn_chestadmin_day5:
                Intent intent5 = new Intent(getActivity(), ChestDay5Activity.class);
                startActivity(intent5);
                break;
            case R.id.btn_chestadmin_day6:
                Intent intent6 = new Intent(getActivity(), ChestDay6Activity.class);
                startActivity(intent6);
                break;
            case R.id.btn_chestadmin_day7:
                Intent intent7 = new Intent(getActivity(), ChestDay7Activity.class);
                startActivity(intent7);
                break;
        }
    }
}