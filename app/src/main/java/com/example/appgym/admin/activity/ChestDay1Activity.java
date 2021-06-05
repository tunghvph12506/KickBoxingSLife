package com.example.appgym.admin.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appgym.R;
import com.example.appgym.admin.EditActivity;
import com.example.appgym.admin.ViewHolder_Exercise;
import com.example.appgym.model.Exercise;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChestDay1Activity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String dataPath = "Exercise/Chest/Day1";
    String group = "Chest";
    String day = "Day1";
    String groupVn = "Ngực";
    String dayVn = "Ngày 1";
    List<Exercise> listExer;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chest_day1);

        recyclerView = findViewById(R.id.rv_chest_day1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = database.getReference().child(dataPath);
        getAllList();

        firebaseStorage = FirebaseStorage.getInstance();

        FirebaseRecyclerOptions<Exercise> options = new FirebaseRecyclerOptions.Builder<Exercise>()
                .setQuery(databaseReference, Exercise.class)
                .build();

        FirebaseRecyclerAdapter<Exercise, ViewHolder_Exercise> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Exercise, ViewHolder_Exercise>(options) {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Exercise holder, int position, @NonNull Exercise model) {
                holder.setItem(ChestDay1Activity.this,model.getName(),model.getVideoUrl(),model.getImageUrl(),model.getSearch(),model.getCalo(),model.getDay());
            }

            @NonNull
            @Override
            public ViewHolder_Exercise onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.exercise_item,parent,false);
                ViewHolder_Exercise viewHolder_exercise = new ViewHolder_Exercise(view);
                viewHolder_exercise.setOnClickListener(new ViewHolder_Exercise.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(ChestDay1Activity.this, EditActivity.class);
                        intent.putExtra("dataPath",dataPath);
                        intent.putExtra("group",group);
                        intent.putExtra("groupVn",groupVn);
                        intent.putExtra("day",day);
                        intent.putExtra("dayVn",dayVn);
                        intent.putExtra("name",listExer.get(position).getName());
                        intent.putExtra("videoUrl",listExer.get(position).getVideoUrl());
                        intent.putExtra("imageUrl",listExer.get(position).getImageUrl());
                        intent.putExtra("search",listExer.get(position).getSearch());
                        intent.putExtra("calo",listExer.get(position).getCalo());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        showAlertDialog(ChestDay1Activity.this, position);
                    }
                });

                return viewHolder_exercise;
            }

        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public void showAlertDialog(final Context context, int pos)  {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.admin_dialog_title).setMessage(R.string.admin_dialog_message);

        builder.setCancelable(true);

        builder.setPositiveButton(R.string.admin_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DatabaseReference dref = database.getReference(dataPath).child(listExer.get(pos).getSearch());
                StorageReference imageRef = firebaseStorage.getReferenceFromUrl(listExer.get(pos).getImageUrl());
                StorageReference videoRef = firebaseStorage.getReferenceFromUrl(listExer.get(pos).getVideoUrl());
                dref.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        imageRef.delete();
                        videoRef.delete();
                        Toast.makeText(context,R.string.admin_deleted, Toast.LENGTH_SHORT).show();
                    }
                });
                listExer.remove(pos);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.admin_dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getAllList()
    {
        listExer = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childDataSnapshot : snapshot.getChildren())
                {
                    Exercise exercise = new Exercise(childDataSnapshot.child("name").getValue().toString()
                            ,childDataSnapshot.child("videoUrl").getValue().toString()
                            ,childDataSnapshot.child("imageUrl").getValue().toString()
                            ,childDataSnapshot.child("search").getValue().toString()
                            ,childDataSnapshot.child("calo").getValue().toString()
                            ,childDataSnapshot.child("day").getValue().toString());
                    listExer.add(exercise);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        getAllList();
        super.onResume();
    }
}