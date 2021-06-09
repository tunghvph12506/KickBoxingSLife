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
    TextView tv_bossname,address,numberphone,roomname;
    List<Advertisement> list;
    public AdvertisementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_advertisement, container, false);
        imgLogo=view.findViewById(R.id.imglogoQuan);
        tv_bossname=view.findViewById(R.id.tv_bossname_repost);
        address=view.findViewById(R.id.tv_address_repost);
        numberphone=view.findViewById(R.id.tv_phone_repost);
        roomname=view.findViewById(R.id.tv_roomname_repost);
        list=new ArrayList<>();
        getData();
        return view;
    }

    private void getData() {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference mdata=database.getReference().child("Advertisement");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.d("TAG", "onDataChange: "+postSnapshot.getValue());
                    Advertisement advertisement = postSnapshot.getValue(Advertisement.class);
                    list.add(advertisement);
                    Log.d("TAG", "getData: "+list);
                }
                roomname.setText(list.get(0).getRoomname());
                tv_bossname.setText(list.get(0).getBossname());
                numberphone.setText(list.get(0).getNumberphone());
                address.setText(list.get(0).getAddress());
                Picasso.get().load(list.get(0).getImageUrl()).into(imgLogo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}