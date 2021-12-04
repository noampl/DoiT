package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.doit.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initNavigation();
    }

    private void initNavigation(){
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        BottomNavigationView bottomNavigationView = _binding.bottomNavBar;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
    }
}
