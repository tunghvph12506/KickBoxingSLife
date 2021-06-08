package com.example.appgym.user.fragment.challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appgym.R;
import com.example.appgym.user.fragment.ShowExerciseUserActivity;

public class LegChallengeActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_day1,btn_day2,btn_day3,btn_day4,btn_day5,btn_day6,btn_day7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leg_challenge);
        btn_day1 = findViewById(R.id.btn_leg_day1);
        btn_day2 = findViewById(R.id.btn_leg_day2);
        btn_day3 = findViewById(R.id.btn_leg_day3);
        btn_day4 = findViewById(R.id.btn_leg_day4);
        btn_day5 = findViewById(R.id.btn_leg_day5);
        btn_day6 = findViewById(R.id.btn_leg_day6);
        btn_day7 = findViewById(R.id.btn_leg_day7);

        btn_day1.setOnClickListener(this);
        btn_day2.setOnClickListener(this);
        btn_day3.setOnClickListener(this);
        btn_day4.setOnClickListener(this);
        btn_day5.setOnClickListener(this);
        btn_day6.setOnClickListener(this);
        btn_day7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LegChallengeActivity.this, ShowExerciseUserActivity.class);
        intent.putExtra("BGLayout",R.drawable.border4);
        intent.putExtra("TextLayout","Tập Chân");
        switch (v.getId())
        {
            case R.id.btn_leg_day1:
                intent.putExtra("dataPath","Exercise/Leg/Day1");
                intent.putExtra("group","Leg");
                intent.putExtra("groupVn","Chân");
                intent.putExtra("day","Day1");
                intent.putExtra("dayVn","Ngày 1");
                startActivity(intent);
                break;
            case R.id.btn_leg_day2:
                intent.putExtra("dataPath","Exercise/Leg/Day2");
                intent.putExtra("group","Leg");
                intent.putExtra("groupVn","Chân");
                intent.putExtra("day","Day2");
                intent.putExtra("dayVn","Ngày 2");
                startActivity(intent);
                break;
            case R.id.btn_leg_day3:
                intent.putExtra("dataPath","Exercise/Leg/Day3");
                intent.putExtra("group","Leg");
                intent.putExtra("groupVn","Chân");
                intent.putExtra("day","Day3");
                intent.putExtra("dayVn","Ngày 3");
                startActivity(intent);
                break;
            case R.id.btn_leg_day4:
                intent.putExtra("dataPath","Exercise/Leg/Day4");
                intent.putExtra("group","Leg");
                intent.putExtra("groupVn","Chân");
                intent.putExtra("day","Day4");
                intent.putExtra("dayVn","Ngày 4");
                startActivity(intent);
                break;
            case R.id.btn_leg_day5:
                intent.putExtra("dataPath","Exercise/Leg/Day5");
                intent.putExtra("group","Leg");
                intent.putExtra("groupVn","Chân");
                intent.putExtra("day","Day5");
                intent.putExtra("dayVn","Ngày 5");
                startActivity(intent);
                break;
            case R.id.btn_leg_day6:
                intent.putExtra("dataPath","Exercise/Leg/Day6");
                intent.putExtra("group","Leg");
                intent.putExtra("groupVn","Chân");
                intent.putExtra("day","Day6");
                intent.putExtra("dayVn","Ngày 6");
                startActivity(intent);
                break;
            case R.id.btn_leg_day7:
                intent.putExtra("dataPath","Exercise/Leg/Day7");
                intent.putExtra("group","Leg");
                intent.putExtra("groupVn","Chân");
                intent.putExtra("day","Day7");
                intent.putExtra("dayVn","Ngày 7");
                startActivity(intent);
                break;
        }
    }
}