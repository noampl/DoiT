package com.example.doit;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.Model.Roles;

public class RegisterViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";
    private static final String ISRAEL_COUNTRY_CODE = "+972";
    public final String title = "Regiter";
    private String _firstName = "";
    private String _lastName = "";
    private String _password = "";
    private String _email = "";
    private String _phoneCountryCode = ISRAEL_COUNTRY_CODE;
    private String _phone = "";
    private Roles.ROLES _role = Roles.ROLES.CLIENT;
    private String _image = "";


    public RegisterViewModel() {

    }

    public void onFirstNameChange(CharSequence s, int start, int before, int count) {
        _firstName = s.toString();
    }

    public void onLastNameChange(CharSequence s, int start, int before, int count) {
        _lastName = s.toString();
    }

    public void onPhoneChange(CharSequence s, int start, int before, int count) {
        _phone = s.toString();
    }

    public void onEmailChange(CharSequence s, int start, int before, int count) {
        _email = s.toString();
    }

    public void onPasswordChange(CharSequence s, int start, int before, int count) {
        _password = s.toString();
    }

}
