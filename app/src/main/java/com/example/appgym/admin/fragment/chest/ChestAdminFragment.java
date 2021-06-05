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
    Button btn_day1;
    public ChestAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_admin_chest, container, false);
        btn_day1 = view.findViewById(R.id.btn_chestadmin_day1);

        btn_day1.setOnClickListener(this);
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
        }
    }
}