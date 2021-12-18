package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.doit.Model.Repository;

public class MainActivity extends AppCompatActivity {
    private Repository db;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, LogInFragment.class, null)
                    .commit();
        }
    }
}