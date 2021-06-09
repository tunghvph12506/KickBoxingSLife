package com.example.appgym.user.fragment.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appgym.R;
import com.example.appgym.UserActivity;
import com.example.appgym.account.ChangePasswordActivity;
import com.example.appgym.account.SignInActivity;
import com.example.appgym.model.Information;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingFragment extends Fragment {
    TextView tv_username_setting ,tv_settingAccount,btn_for_get_password_setting,tv_log_out;
    CircleImageView imageView;
    DatabaseReference databaseReferenceCustomerImage;
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setting, container, false);
        tv_username_setting=view.findViewById(R.id.tv_username_setting);
        tv_settingAccount=view.findViewById(R.id.tv_settingAccount);
        btn_for_get_password_setting=view.findViewById(R.id.btn_for_get_password_setting);
        tv_log_out=view.findViewById(R.id.tv_log_out);
        tv_username_setting.setText(UserActivity.usernameAll);
        imageView = view.findViewById(R.id.circleImageView);
        databaseReferenceCustomerImage = FirebaseDatabase.getInstance().getReference("Customer_information").child(UserActivity.usernameAll);
        databaseReferenceCustomerImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Information information = snapshot.getValue(Information.class);
                    if(information.getImageUrl() != null)
                    {
                        String imageUrl = information.getImageUrl();
                        Picasso.get().load(imageUrl).into(imageView);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tv_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), SignInActivity.class);
                startActivity(i);
            }
        });
        tv_settingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), SettingAccountActivity.class);
                Bundle b=new Bundle();
                b.putString("tentk",UserActivity.usernameAll);
                i.putExtras(b);
                startActivity(i);
            }
        });
        btn_for_get_password_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), ChangePasswordActivity.class);
                Bundle b=new Bundle();
                b.putString("tentk",UserActivity.usernameAll);
                i.putExtras(b);
                startActivity(i);
            }
        });


        return view;
    }
}