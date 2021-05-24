package com.example.appgym.user.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.appgym.R;
import com.example.appgym.user.fragment.challenge.ChestChallengeActivity;
import com.example.appgym.user.fragment.challenge.HandChallengeActivity;
import com.example.appgym.user.fragment.challenge.LegChallengeActivity;
import com.example.appgym.user.fragment.challenge.StomachChallengeActivity;

public class HomeFragment extends Fragment implements View.OnClickListener {
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout linearStomach = view.findViewById(R.id.linearStomach);
        LinearLayout linearChest = view.findViewById(R.id.linearChest);
        LinearLayout linearHand = view.findViewById(R.id.linearHand);
        LinearLayout linearLeg = view.findViewById(R.id.linearLeg);

        linearStomach.setOnClickListener(this);
        linearChest.setOnClickListener(this);
        linearHand.setOnClickListener(this);
        linearLeg.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.linearStomach:
                Intent intent = new Intent(getActivity(), StomachChallengeActivity.class);
                startActivity(intent);
                break;
            case R.id.linearChest:
                Intent intent2 = new Intent(getActivity(), ChestChallengeActivity.class);
                startActivity(intent2);
                break;
            case R.id.linearHand:
                Intent intent3 = new Intent(getActivity(), HandChallengeActivity.class);
                startActivity(intent3);
                break;
            case R.id.linearLeg:
                Intent intent4 = new Intent(getActivity(), LegChallengeActivity.class);
                startActivity(intent4);
                break;
        }
    }
}