package com.example.appgym.admin.fragment.chest;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.appgym.R;
import com.example.appgym.admin.activity.ShowExerciseActivity;


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
        Intent intent = new Intent(getActivity(), ShowExerciseActivity.class);
        switch (v.getId())
        {
            case R.id.btn_chestadmin_day1:
                intent.putExtra("dataPath","Exercise/Chest/Day1");
                intent.putExtra("group","Chest");
                intent.putExtra("groupVn","Ngực");
                intent.putExtra("day","Day1");
                intent.putExtra("dayVn","Ngày 1");
                startActivity(intent);
                break;
            case R.id.btn_chestadmin_day2:
                intent.putExtra("dataPath","Exercise/Chest/Day2");
                intent.putExtra("group","Chest");
                intent.putExtra("groupVn","Ngực");
                intent.putExtra("day","Day2");
                intent.putExtra("dayVn","Ngày 2");
                startActivity(intent);
                break;
            case R.id.btn_chestadmin_day3:
                intent.putExtra("dataPath","Exercise/Chest/Day3");
                intent.putExtra("group","Chest");
                intent.putExtra("groupVn","Ngực");
                intent.putExtra("day","Day3");
                intent.putExtra("dayVn","Ngày 3");
                startActivity(intent);
                break;
            case R.id.btn_chestadmin_day4:
                intent.putExtra("dataPath","Exercise/Chest/Day4");
                intent.putExtra("group","Chest");
                intent.putExtra("groupVn","Ngực");
                intent.putExtra("day","Day4");
                intent.putExtra("dayVn","Ngày 4");
                startActivity(intent);
                break;
            case R.id.btn_chestadmin_day5:
                intent.putExtra("dataPath","Exercise/Chest/Day5");
                intent.putExtra("group","Chest");
                intent.putExtra("groupVn","Ngực");
                intent.putExtra("day","Day5");
                intent.putExtra("dayVn","Ngày 5");
                startActivity(intent);
                break;
            case R.id.btn_chestadmin_day6:
                intent.putExtra("dataPath","Exercise/Chest/Day6");
                intent.putExtra("group","Chest");
                intent.putExtra("groupVn","Ngực");
                intent.putExtra("day","Day6");
                intent.putExtra("dayVn","Ngày 6");
                startActivity(intent);
                break;
            case R.id.btn_chestadmin_day7:
                intent.putExtra("dataPath","Exercise/Chest/Day7");
                intent.putExtra("group","Chest");
                intent.putExtra("groupVn","Ngực");
                intent.putExtra("day","Day7");
                intent.putExtra("dayVn","Ngày 7");
                startActivity(intent);
                break;
        }
    }
}