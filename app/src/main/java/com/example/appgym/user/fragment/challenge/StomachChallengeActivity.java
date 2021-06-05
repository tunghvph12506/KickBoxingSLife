package com.example.appgym.user.fragment.challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appgym.R;

public class StomachChallengeActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_day1,btn_day2,btn_day3,btn_day4,btn_day5,btn_day6,btn_day7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stomach_challenge);
        btn_day1 = findViewById(R.id.btn_stomach_day1);
        btn_day2 = findViewById(R.id.btn_stomach_day2);
        btn_day3 = findViewById(R.id.btn_stomach_day3);
        btn_day4 = findViewById(R.id.btn_stomach_day4);
        btn_day5 = findViewById(R.id.btn_stomach_day5);
        btn_day6 = findViewById(R.id.btn_stomach_day6);
        btn_day7 = findViewById(R.id.btn_stomach_day7);

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
        switch (v.getId())
        {
            case R.id.btn_stomach_day1:
        }
    }
}