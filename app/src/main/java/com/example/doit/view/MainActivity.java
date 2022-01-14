package com.example.doit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.MyApplication;
import com.example.doit.R;
import com.example.doit.databinding.ActivityMainBinding;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.AccountViewModel;
import com.example.doit.viewmodel.LoginViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.base.Supplier;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private ActivityMainBinding _binding;
    private static Context context;
    private HashMap<String,String> credentials;
    private AccountViewModel accountViewModel;
    private NavHostFragment navHostFragment;
    private boolean _triedToConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        _binding.setLoginViewModel(loginViewModel);
        _binding.setLifecycleOwner(this);
        context = getApplicationContext();
        Repository.getInstance().login(getUserCredentials());
        Repository.getInstance().cleanCache();
        initNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_menu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.about:
                navHostFragment.getNavController().navigate(R.id.about);
                return true;
            case R.id.exitButton:
                accountViewModel.onClickLogoutButton();
                finishAndRemoveTask();
                return true;
        }
        return false;
    }

    public Context getContext() {
        return getApplicationContext();
    }

    private void initNavigation() {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        BottomNavigationView bottomNavigationView = _binding.bottomNavBar;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());
    }


    private Map<String, String> getUserCredentials(){
        if(_triedToConnect){
            return credentials;
        }
        _triedToConnect = true;
        credentials = new HashMap<>();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        credentials.put("Email",sharedPref.getString(getString(R.string.email), "NONE"));
        credentials.put("Password",sharedPref.getString(getString(R.string.password), "NONE"));
        saveUserForLater();
        return credentials;
    }

    private void saveUserForLater(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.email), getUserCredentials().get("Email"));
        editor.putString(getString(R.string.password), getUserCredentials().get("Password"));
        editor.apply();
    }
}


