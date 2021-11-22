package com.project.sts_hod;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FacultyProfile extends AppCompatActivity {

    public TextView branchTextView;
    public Button nameButton, passwordButton, emailButton;
    public Button remove;

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile);

        branchTextView = findViewById(R.id.branch_faculty_id);
        nameButton = findViewById(R.id.name_faculty_id);
        passwordButton = findViewById(R.id.password_faculty_id);
        emailButton = findViewById(R.id.email_faculty_id);
        remove = findViewById(R.id.facultyRemoveButton_id);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String branch, name, email, password;
        branch = getIntent().getStringExtra("branch");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        branchTextView.setText("Branch: " +branch);
        nameButton.setText("Name:  " + name);
        emailButton.setText("Email:  " + email);
        passwordButton.setText("Password:  " + password);

        remove.setOnClickListener(v -> {
            // Removing faculty
            View dialogView = LayoutInflater.from(FacultyProfile.this).inflate(R.layout.faculty_remove_confirm, null);
            AlertDialog dialog = new AlertDialog.Builder(FacultyProfile.this)
                    .setView(dialogView)
                    .create();

            Button confirmButton = dialogView.findViewById(R.id.faculty_remove_confirm_id);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.getReference().child("FACULTYAccounts"+user.getEmail().replace(".",""))
                            .child(email.replace(".",""))
                            .setValue(null);

                    database.getReference().child("HODOFFACULTY").child(email.replace(".",""))
                            .setValue(null);

                    Toast.makeText(FacultyProfile.this, "Faculty Removed", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        });
    }
}
