package com.example.appgym.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appgym.R;
import com.squareup.picasso.Picasso;

public class ViewHolder_Exercise extends RecyclerView.ViewHolder{

    ImageView img_exercise;
    TextView name_exercise, calo_exercise;

    public ViewHolder_Exercise(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getLayoutPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getLayoutPosition());
                return true;
            }
        });

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

    private ViewHolder_Exercise.ClickListener mClickListener;

    //Interface to send callbacks...
    public interface ClickListener{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder_Exercise.ClickListener clickListener){
        mClickListener = clickListener;
    }

}
