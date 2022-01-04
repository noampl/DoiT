package com.example.doit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.example.doit.IResponseHelper;
import com.example.doit.R;
import com.example.doit.databinding.ActivityMainBinding;
import com.example.doit.viewmodel.LoginViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding _binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        _binding.setLoginViewModel(loginViewModel);
        _binding.setLifecycleOwner(this);
        initNavigation();
    }

    public static Context getContext() {
        return getContext();
    }

    private void initNavigation() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        BottomNavigationView bottomNavigationView = _binding.bottomNavBar;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

    }
}


