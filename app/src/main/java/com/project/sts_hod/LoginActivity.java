package com.project.sts_hod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sts_hod.Model.HODModel;

public class LoginActivity extends AppCompatActivity {
    public EditText emailEditText, passwordEditText;
    public Button loginButton;

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText_id);
        passwordEditText = findViewById(R.id.passwordEditText_id);
        loginButton = findViewById(R.id.loginButton_id);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Please Wait");
            dialog.setMessage("Logging you in");
            dialog.show();

            if(!email.isEmpty() && !password.isEmpty()){
                database.getReference().child("HODAccounts")
                        .child(email.replace(".",""))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    HODModel model = new HODModel();
                                    model = snapshot.getValue(model.getClass());

                                    if(model.getEmail().replace(".","")
                                            .equals(email.replace(".",""))

                                            &&
                                            model.getPassword().equals(password)){

                                        // Data matched

                                        createAccount(email, password);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
            else{
                Toast.makeText(LoginActivity.this, "Fields Empty", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createAccount(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                login(email, password);
            }
        });
    }

    public void login(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
    }
}