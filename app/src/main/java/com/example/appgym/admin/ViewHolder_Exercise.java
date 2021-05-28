package com.example.appgym.admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgym.R;
import com.squareup.picasso.Picasso;

public class ViewHolder_Exercise extends RecyclerView.ViewHolder {

    ImageView img_exercise;
    TextView name_exercise, calo_exercise;

    public ViewHolder_Exercise(@NonNull View itemView) {
        super(itemView);
    }

    public void setItem(FragmentActivity activity,String name, String videoUrl, String imageUrl, String search,String calo)
    {
        name_exercise = itemView.findViewById(R.id.tv_name_exercise);
        img_exercise = itemView.findViewById(R.id.img_exercise);
        calo_exercise = itemView.findViewById(R.id.tv_calo_exercise);

        Picasso.get().load(imageUrl).into(img_exercise);
        name_exercise.setText(name);
        calo_exercise.setText(calo+" Calo");
    }
}
