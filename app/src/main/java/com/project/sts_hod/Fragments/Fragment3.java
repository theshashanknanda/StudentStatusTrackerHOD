package com.project.sts_hod.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sts_hod.Adapters.FacultyRecylerView;
import com.project.sts_hod.Model.FacultyModel;
import com.project.sts_hod.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment {
    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    public RecyclerView recyclerView;
    public FacultyRecylerView adapter;

    public List<FacultyModel> modelList;

    public TextView noFaculty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        modelList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.facultyRecyclerView_id);
        adapter = new FacultyRecylerView(modelList, view.getContext());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        noFaculty = view.findViewById(R.id.noFaculties_id);

        database.getReference().child("FACULTYAccounts"+user.getEmail().replace(".",""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        modelList.clear();

                        if(snapshot.hasChildren())
                        {
                            for(DataSnapshot snapshot1: snapshot.getChildren())
                            {
                                FacultyModel model = new FacultyModel();
                                model = snapshot1.getValue(FacultyModel.class);
                                modelList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                            noFaculty.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
