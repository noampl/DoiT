package com.example.doit;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";
    private String _userName;
    private String _password;

    public LoginViewModel() {
        _userName = "User Name";
        _password = "";
    }

    public String getUserName(){
        return _userName;
    }

    public String getPassword() {
        return _password;
    }

    public void onUserNameChange(CharSequence s, int start, int before, int count) {
        _userName = s.toString();
    }

    public void onPasswordChange(CharSequence s, int start, int before, int count) {
        _password = s.toString();
    }

    public boolean Login(){
        Log.d(TAG, "Login: " + _userName);
        Log.d(TAG, "Password: " + _password);
        return true;
    }
}
