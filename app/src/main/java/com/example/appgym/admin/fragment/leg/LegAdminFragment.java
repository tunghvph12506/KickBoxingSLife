package com.example.appgym.admin.fragment.leg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.appgym.admin.ViewHolder_Exercise;
import com.example.appgym.model.Exercise;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LegAdminFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    String dataPath = "Data/Leg";
    List<String> listId;


    public LegAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_admin_leg, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.rv_leg);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseReference = database.getReference().child(dataPath);

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

        builder.setTitle("Confirm Delete").setMessage("Do you want to delete this exercise");

        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DatabaseReference dref = database.getReference(dataPath).child(listId.get(pos));
                dref.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                listId.remove(pos);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getAllList()
    {
        listId = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childDataSnapshot : snapshot.getChildren())
                {
                    listId.add(childDataSnapshot.getKey());
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