package com.project.sts_hod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sts_hod.Model.HODModel;

public class ProfileActivity extends AppCompatActivity {
    public Button branch;
    public Button name, email, password;

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        branch = findViewById(R.id.branch_id);
        name = findViewById(R.id.name_id);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password_id);

        String emaill = user.getEmail();

        getSupportActionBar().hide();

        database.getReference().child("HODAccounts").child(emaill.replace(".",""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HODModel model = new HODModel();
                        model = snapshot.getValue(HODModel.class);

                        branch.setText("HOD:  " + model.getBranch());
                        name.setText("Name:  " + model.getName());
                        email.setText("Email:  " + model.getEmail());
                        password.setText("Password:  " + model.getPassword());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
