package com.project.sts_hod.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.sts_hod.Model.FacultyModel;
import com.project.sts_hod.R;

import java.security.SecureRandom;

import papaya.in.sendmail.SendMail;

public class Fragment2 extends Fragment {
    public Button randomPasswordButton, addButton;
    public EditText branch, name, email,password;
    public Spinner spinner;
    public View view;

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    public boolean isExist = false;

    public String arr[] = {"IT", "CE"};
    public String spinnerBranch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_2, container, false);

        randomPasswordButton = view.findViewById(R.id.randomPasswordButton_id);
        name = view.findViewById(R.id.faculty_nameEditText_id);
        email = view.findViewById(R.id.faculty_emailEditText_id);
        password = view.findViewById(R.id.faculty_passwordEditText_id);
        addButton = view.findViewById(R.id.facult_addButton_id);
        spinner = view.findViewById(R.id.addFacultySpinner);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        ArrayAdapter aa = new ArrayAdapter(view.getContext() ,android.R.layout.simple_spinner_item, arr);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerBranch = arr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerBranch = arr[0];
            }
        });

        randomPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordd = generateRandomPassword(8);
                password.setText(passwordd);
            }
        });

        addButton.setOnClickListener(v -> {
            showDialog();
        });

        return view;
    }

    public String generateRandomPassword(int len)
    {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "abcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public void showDialog(){
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.faculty_add_confirm, null);
        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setView(dialogView)
                .create();

        Button confirmButton = dialogView.findViewById(R.id.faculty_confirm_button_id);
        confirmButton.setOnClickListener(v -> {
            String namee = name.getText().toString();
            String emaill = email.getText().toString();
            String passwordd = password.getText().toString();

            FacultyModel model = new FacultyModel();
            model.setName(namee);
            model.setEmail(emaill);
            model.setPassword(passwordd);
            model.setBranch(spinnerBranch);

            if(!spinnerBranch.isEmpty() && !namee.isEmpty() && !emaill.isEmpty() && !passwordd.isEmpty()){

                database.getReference().child("FACULTYAccounts"+user.getEmail().replace(".",""))
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                {
                                    for(DataSnapshot snapshot1: snapshot.getChildren())
                                    {
                                        FacultyModel model1 = new FacultyModel();
                                        model1 = snapshot1.getValue(FacultyModel.class);

                                        if(model1.getEmail().equals(emaill)){
                                            isExist = true;
                                        }
                                    }

                                    if(isExist){
                                        Toast.makeText(getContext(), "Account Already Exists", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                    else{
                                        // setting data in firebase after creating the account in authentication
                                        database.getReference().child("FACULTYAccounts"+user.getEmail().replace(".",""))
                                                .child(emaill.replace(".",""))
                                                .setValue(model);

                                        database.getReference().child("HODOFFACULTY").child(model.getEmail().replace(".",""))
                                                .setValue(user.getEmail().replace(".",""));

                                        // TODO: Sending email of email & password

                                        SendMail mail = new SendMail("studenttrackersystem@gmail.com", "sts_sts_sts",
                                                model.getEmail(),
                                                "LJ Faculty App",
                                                "App Link" + "https://drive.google.com/drive/folders/1qHRZ7UVRVgsADZ_4BPDdV51tVDs8iUTP?usp=sharing" +
                                                        "\nYour email & password: " + model.getPassword());

                                        mail.execute();

                                        dialog.dismiss();

                                        Toast.makeText(view.getContext(), "Data Sent Succesfully", Toast.LENGTH_LONG).show();
                                        name.setText("");
                                        email.setText("");
                                        password.setText("");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
            else{
                Toast.makeText(view.getContext(), "Fields Empty", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
