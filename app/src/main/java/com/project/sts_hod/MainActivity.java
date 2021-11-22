package com.project.sts_hod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.project.sts_hod.Fragments.Fragment1;
import com.project.sts_hod.Fragments.Fragment2;
import com.project.sts_hod.Fragments.Fragment3;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView navigationView;

    public FirebaseDatabase database;
    public FirebaseAuth auth;
    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottomNavView);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fragment1()).commit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.id_1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fragment1()).commit();
                        break;

                    case R.id.id_2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fragment2()).commit();
                        break;

                    case R.id.id_3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Fragment3()).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.profileMenu_id:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.logoutMenu_id:
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
