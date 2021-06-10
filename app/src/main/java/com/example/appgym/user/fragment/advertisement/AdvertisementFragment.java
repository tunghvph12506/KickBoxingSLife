package com.example.appgym.user.fragment.advertisement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appgym.R;
import com.example.appgym.model.Advertisement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementFragment extends Fragment {
    ImageView imgLogo;
    TextView tv_bossname,tv_address,tv_numberphone,tv_roomname;
    public AdvertisementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advertisement, container, false);
        imgLogo = view.findViewById(R.id.imglogoQuan);
        tv_bossname = view.findViewById(R.id.tv_bossname_repost);
        tv_address = view.findViewById(R.id.tv_address_repost);
        tv_numberphone = view.findViewById(R.id.tv_phone_repost);
        tv_roomname = view.findViewById(R.id.tv_roomname_repost);
        getData();
        return view;
    }

    private void getData() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Advertisement").child("All");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Advertisement advertisement = snapshot.getValue(Advertisement.class);
                    tv_roomname.setText(advertisement.getRoomname());
                    tv_bossname.setText(advertisement.getBossname());
                    tv_numberphone.setText(advertisement.getNumberphone());
                    tv_address.setText(advertisement.getAddress());
                    String imgUrl = advertisement.getImageUrl();
                    if(imgUrl != null)
                    {
                        Picasso.get().load(imgUrl).into(imgLogo);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}