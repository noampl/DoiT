package com.example.doit.Model;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class User {
    private static final String TAG = "User Model";
    private final String NO_IMAGE = "no_image";
    private static final String ISRAEL_COUNRTY_CODE = "+972";
    private Map<String, Object> _user;
    private String _email;
    private String _lastName;
    private String _password;
    private String _image;
    private String _phone;
    private String _countryPhoneCode;
    private Roles.ROLES _role;


    public User() {
        _user = new HashMap<>();
        _user.put("phone_country_code", ISRAEL_COUNRTY_CODE);
        _user.put("role", Roles.ROLES.CLIENT);
        _user.put("image", NO_IMAGE);
    }

    public void setFirstName(String firstName) {
        _user.put("first_name", firstName);
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
        _user.put("last_name", _lastName);
    }

    public void setEmail(String email) {
        if (email != null) {_email = email.toLowerCase(); }
        _user.put("email", _email);
    }

    public String getEmail() {
        return this._email;
    }

    public String getPassword() {
        return this._password;
    }

    public void setPassword(String password) {
        _password = password;
        _user.put("password", _password);
    }

    public void setPhone(String phone) {
        _phone = phone;
        _user.put("phone", _phone);
    }

    public void setImgae(String image_path){
        _image = image_path;
        _user.put("image", image_path);
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        _countryPhoneCode = phoneCountryCode;
        _user.put("phone_country_code", _countryPhoneCode);
    }

    public void setRole(Roles.ROLES role){
        _role = role;
        _user.put("role", _role);
    }

    //todo: add setImage

    public Map<String, Object> getUserMap(){
        return _user;
    }

}
