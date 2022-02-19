package com.example.doit.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.MyApplication;
import com.example.doit.R;
import com.example.doit.databinding.ActivityMainBinding;
import com.example.doit.interfaces.IActionBarHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.viewmodel.AccountViewModel;
import com.example.doit.viewmodel.LoginViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.common.base.Supplier;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IActionBarHelper, Toolbar.OnMenuItemClickListener {

    // region Members

    private final String TAG = "MainActivity";
    private ActivityMainBinding _binding;
    private AccountViewModel accountViewModel;
    private NavHostFragment navHostFragment;
    private AppBarConfiguration _appBarConfiguration;
    private boolean isLoggedIn;
    private FragmentManager _fragmentManager;

    // endregion

    // region LifeCycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Intent intent = getIntent();
        isLoggedIn = intent.getBooleanExtra("LOGIN",false);
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.setActionBarHelper(this);
        _binding.setLoginViewModel(loginViewModel);
        _binding.setLifecycleOwner(this);
        _fragmentManager = getSupportFragmentManager();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Repository.getInstance().cleanCache();
    }

    // endregion


    // region Private Methods

    private void init(){
        initNavigation();
        initListeners();
    }

    private void initNavigation() {
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        BottomNavigationView bottomNavigationView = _binding.bottomNavBar;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

        _appBarConfiguration =
                new AppBarConfiguration.Builder(navHostFragment.getNavController().getGraph()).build();
        NavigationUI.setupWithNavController(
                _binding.mainToolbar, navHostFragment.getNavController(), _appBarConfiguration);
        if (!isLoggedIn) {
           navHostFragment.getNavController().navigate(R.id.logInFragment2);
        }
    }

    private void initListeners(){
        _binding.mainToolbar.setOnMenuItemClickListener(this);
        _binding.mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUI.navigateUp(navHostFragment.getNavController(), _appBarConfiguration);
            }
        });
    }

    private void exitDialog(){
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(this).create();
        alertDialog.setTitle("Are you sure you want to exit");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
            accountViewModel.onClickLogoutButton();
            finishAndRemoveTask();
            finish();
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialogInterface, i) ->
                alertDialog.dismiss());
        alertDialog.show();
    }

    // endregion

    // region IActionBarHelper

    @Override
    public void setNavIcon(Integer id) {
        if(id != null){
            _binding.mainToolbar.setNavigationIcon(id);
        }
        else{
            _binding.mainToolbar.setNavigationIcon(null);
        }
    }

    @Override
    public void setTitle(String title) {
        _binding.mainToolbar.setTitle(title);
    }

    @Override
    public void setMenu(int resId) {
        _binding.mainToolbar.invalidateMenu();
        _binding.mainToolbar.getMenu().clear();
        _binding.mainToolbar.inflateMenu(resId);
    }

    @Override
    public void setMenuClickListener(Toolbar.OnMenuItemClickListener listener) {
        if (listener != null){
            _binding.mainToolbar.setOnMenuItemClickListener(listener);
        }
        else{
            _binding.mainToolbar.setOnMenuItemClickListener(this);

        }
    }

    // endregion

    // region Toolbar

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                navHostFragment.getNavController().navigate(R.id.about);
                return true;
            case R.id.exitButton:
                exitDialog();
                return true;
            default:
                Log.e("MainActivity", "Unrecognaize item pressed");
        }
        return false;
    }

    // endregion

    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        String currentFragment = navHostFragment.getNavController().getCurrentBackStackEntry().
                getDestination().getDisplayName();
        if (currentFragment.equals("com.example.doit:id/groupsFragment2") ||
                currentFragment.equals("com.example.doit:id/logInFragment2"))
            exitDialog();
        else
            super.onBackPressed();

    }
}


