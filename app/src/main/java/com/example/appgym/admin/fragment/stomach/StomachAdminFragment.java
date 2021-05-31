package com.example.appgym.admin.fragment.stomach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class StomachAdminFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String dataPath = "Exercise/Stomach";
    String group = "Stomach";
    String groupVn = "Bụng";
    List<Exercise> listExer;
    FirebaseStorage firebaseStorage;


    public StomachAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_admin_stomach, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.rv_stomach);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
                holder.setItem(getActivity(),model.getName(),model.getVideoUrl(),model.getImageUrl(),model.getSearch(),model.getCalo());
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
                        Intent intent = new Intent(getActivity(), EditActivity.class);
                        intent.putExtra("dataPath",dataPath);
                        intent.putExtra("group",group);
                        intent.putExtra("groupVn",groupVn);
                        intent.putExtra("name",listExer.get(position).getName());
                        intent.putExtra("videoUrl",listExer.get(position).getVideoUrl());
                        intent.putExtra("imageUrl",listExer.get(position).getImageUrl());
                        intent.putExtra("search",listExer.get(position).getSearch());
                        intent.putExtra("calo",listExer.get(position).getCalo());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        showAlertDialog(getContext(), position);
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
                            ,childDataSnapshot.child("calo").getValue().toString());
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