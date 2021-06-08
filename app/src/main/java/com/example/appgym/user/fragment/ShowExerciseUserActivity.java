package com.example.appgym.user.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgym.R;
import com.example.appgym.admin.AddActivity;
import com.example.appgym.admin.EditActivity;
import com.example.appgym.admin.ViewHolder_Exercise;
import com.example.appgym.admin.activity.ShowExerciseActivity;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShowExerciseUserActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ActionBar toolbar;
    String dataPath,group,groupVn,day,dayVn;
    List<Exercise> listExer;
    FirebaseStorage firebaseStorage;
    int checkchild = 0;
    LinearLayout linearLayout;
    TextView tv_BG;
    String textBG;
    int layoutBG;
    Drawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exercise_user);

        Bundle gets = getIntent().getExtras();
        dataPath = gets.getString("dataPath");
        group = gets.getString("group");
        groupVn = gets.getString("groupVn");
        day = gets.getString("day");
        dayVn = gets.getString("dayVn");
        layoutBG = gets.getInt("BGLayout");
        textBG = gets.getString("TextLayout");

        recyclerView = findViewById(R.id.rv_exercise_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        linearLayout = findViewById(R.id.linearLayout_Practice);
        drawable = ResourcesCompat.getDrawable(getResources(),layoutBG,null);
        linearLayout.setBackground(drawable);
        tv_BG = findViewById(R.id.tv_BG);
        tv_BG.setText(textBG);

        toolbar = getSupportActionBar();
        toolbar.setTitle(groupVn+" - "+dayVn);
        toolbar.setDisplayHomeAsUpEnabled(true);

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
                holder.setItem(ShowExerciseUserActivity.this,model.getName(),model.getVideoUrl(),model.getImageUrl(),model.getSearch(),model.getCalo(),model.getDay());
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

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder_exercise;
            }

        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public void readyPractice(View view)
    {
        if(checkchild != 0)
        {
            Intent intent = new Intent(ShowExerciseUserActivity.this, StartPracticeActivity.class);
            intent.putExtra("listexercise",(Serializable) listExer);
            intent.putExtra("dayVn",dayVn);
            intent.putExtra("groupVn",groupVn);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(ShowExerciseUserActivity.this,R.string.show_exercise_toast_noexercise,Toast.LENGTH_SHORT).show();
        }

    }

    private void getAllList()
    {
        listExer = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    boolean check = true;
                    for(int i = 0; i<listExer.size(); i++)
                    {
                        if(listExer.get(i).getSearch().equals(childDataSnapshot.child("search").getValue().toString()))
                        {
                            check = false;
                        }
                    }
                    if(check == true)
                    {
                        listExer.add(exercise);
                    }
                    checkchild = (int) snapshot.getChildrenCount();
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