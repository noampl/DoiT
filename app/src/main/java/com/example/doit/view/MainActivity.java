package com.example.doit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.doit.R;
import com.example.doit.databinding.ActivityMainBinding;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.LoginViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private ActivityMainBinding _binding;
    private static Context context;
    private HashMap<String,String> credentials;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        _binding.setLoginViewModel(loginViewModel);
        _binding.setLifecycleOwner(this);
        context = getApplicationContext();
        Repository.getInstance().login(getUserCredentials());
        Repository.getInstance().get_authUser().observe(this,new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(Boolean.TRUE.equals(Repository.getInstance().get_isSynced().getValue())){
                    return;
                }
                Log.w(TAG, "user logged in");
                Log.w(TAG, "Requesting all user groups");
                Log.w(TAG, user.toString());
                if (user.get_userId() != null){
                    if (!user.get_userId().equals("")){
                        Repository.getInstance().getAllAuthUserGroups();
                        Repository.getInstance().set_isSynced(true);
                    }
                }
            }
        });
        initNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.app_menu,menu);
        return true;
    }

    public Context getContext() {
        return getApplicationContext();
    }

    private void initNavigation() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        BottomNavigationView bottomNavigationView = _binding.bottomNavBar;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

    }


    private Map<String, String> getUserCredentials(){
        credentials = new HashMap<>();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        credentials.put("email",sharedPref.getString(getString(R.string.email), "NONE"));
        credentials.put("password",sharedPref.getString(getString(R.string.password), "NONE"));
        return credentials;
    }
}


