package com.example.appgym.user.fragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.appgym.R;
import com.example.appgym.account.SignInActivity;
import com.example.appgym.model.Exercise;
import com.example.appgym.splash.SplashActivity;

import java.util.List;

public class StartPracticeActivity extends AppCompatActivity {

    VideoView mVideoview;
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    TextView tv_instruction, tv_time, tv_name;
    int totaltime = 30;
    int factor = 100/totaltime;
    int totaltime2 = 10;
    int factor2 = 100/totaltime2;
    int currentPos = 0;
    String instruction = "";
    String instruction2 = "";
    List<Exercise> listExercise;
    ActionBar toolbar;
    String dayVn,groupVn;
    Button btn_practice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_practice);

        instruction = getResources().getString(R.string.start_practice_string_instruction1);
        instruction2 = getResources().getString(R.string.start_practice_string_instruction2);

        mProgressBar=(ProgressBar)findViewById(R.id.progressbar_practice);
        mVideoview = findViewById(R.id.videoview_practice);
        mVideoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        tv_instruction = findViewById(R.id.tv_practice_instruction);
        tv_time = findViewById(R.id.tv_practice_time);
        tv_name = findViewById(R.id.tv_practice_name);

        btn_practice = findViewById(R.id.btn_practice_start);

        Intent get = getIntent();
        listExercise = (List<Exercise>) get.getSerializableExtra("listexercise");
        dayVn = get.getStringExtra("dayVn");
        groupVn = get.getStringExtra("groupVn");

        toolbar = getSupportActionBar();
        toolbar.setTitle(groupVn+" - "+dayVn);
        toolbar.setDisplayHomeAsUpEnabled(true);

    }

    public void startPractice(View view)
    {
        mVideoview.setVideoPath(listExercise.get(currentPos).getVideoUrl());
        mVideoview.start();
        RunProgressBar(mProgressBar,totaltime2,factor2,instruction2);
        btn_practice.setVisibility(View.INVISIBLE);
    }


    private void RunProgressBar(ProgressBar progressBar, int totalTime, int Factor, String Instruction)
    {
        if(currentPos < listExercise.size())
        {
            tv_instruction.setText(Instruction);
            tv_time.setText(""+totalTime);
            tv_name.setText(listExercise.get(currentPos).getName());
            mVideoview.setVideoPath(listExercise.get(currentPos).getVideoUrl());
            mVideoview.start();
            mCountDownTimer=new CountDownTimer(totalTime*1000,1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int secondsRemaining = (int) millisUntilFinished/1000;
                    int progressPercentage = (totalTime-secondsRemaining) * Factor;
                    progressBar.setProgress(progressPercentage);
                    tv_time.setText(""+secondsRemaining);
                }

                @Override
                public void onFinish() {
                    progressBar.setProgress(100);
                    tv_time.setText(""+0);
                    if(totalTime == 30)
                    {
                        currentPos++;
                        RunProgressBar(progressBar,totaltime2,factor2,instruction2);
                    }
                    else
                    {
                        RunProgressBar(progressBar,totaltime,factor,instruction);
                    }
                }
            };
            mCountDownTimer.start();
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Toast.makeText(StartPracticeActivity.this,R.string.start_practice_toast_congratulations,Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, 5 * 1000);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }
}