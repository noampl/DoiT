package com.example.doit.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.MyApplication;
import com.example.doit.R;
import com.example.doit.common.Consts;
import com.example.doit.viewmodel.LoginViewModel;

import java.util.HashMap;
import java.util.Map;

public class introActivity extends AppCompatActivity {

    // region Members

    private boolean _triedToConnect;
    private HashMap<String,String> credentials;
    private final AppCompatActivity activity = this;
    private boolean isFirstTime = true;
    // endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        LoginViewModel _loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        _loginViewModel.get_logedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                System.out.println("peleg try to connect " + aBoolean);
                if (!isFirstTime) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("LOGIN", aBoolean);
                    startActivity(intent);
                    finish();
                }
                isFirstTime = false;
            }
        });
        _loginViewModel.initLogin(getUserCredentials());

    }

    // endregion

    // region Private Methods

    private Map<String, String> getUserCredentials(){
        if(_triedToConnect){
            return credentials;
        }
        _triedToConnect = true;
        credentials = new HashMap<>();
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences(getString(R.string.file_key),Context.MODE_PRIVATE);
        credentials.put(Consts.EMAIL,sharedPref.getString(getString(R.string.email), Consts.INVALID_STRING));
        credentials.put(Consts.PASSWORD,sharedPref.getString(getString(R.string.password), Consts.INVALID_STRING));
        saveUserForLater();
        return credentials;
    }

    private void saveUserForLater(){
        SharedPreferences sharedPref =  MyApplication.getAppContext().getSharedPreferences(getString(R.string.file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.email), getUserCredentials().get(Consts.EMAIL));
        editor.putString(getString(R.string.password), getUserCredentials().get(Consts.PASSWORD));
        editor.apply();
    }
}