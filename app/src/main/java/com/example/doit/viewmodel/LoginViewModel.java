package com.example.doit;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.doit.Model.Consts;
import com.example.doit.Model.Repository;
import com.example.doit.Model.UserFirebaseWorker;

import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends ViewModel {
    //region members
    private static final String TAG = "LoginViewModel";
    private final Repository repo;
    private String _email;
    private String _password;
    private IResponseHelper responseHelper;
    //endregion

    public LoginViewModel() {
        repo = Repository.getInstance();
        _email = "Email";
        _password = "";
    }

    public IResponseHelper getResponseHelper() {
        return responseHelper;
    }

    public void setResponseHelper(IResponseHelper responseHelper) {
        this.responseHelper = responseHelper;
    }

    public String getUserName(){
        return this._email;
    }

    public String getPassword() {
        return this._password;
    }

    public void onUserNameChange(CharSequence s, int start, int before, int count) {
        this._email = s.toString();
    }

    public void onPasswordChange(CharSequence s, int start, int before, int count) {
        this._password = s.toString();
    }

    public boolean Login(){
        Log.d(TAG, "Login: " + this._email);
        Log.d(TAG, "Password: " + this._password);
        UserFirebaseWorker worker = (UserFirebaseWorker) repo.createWorker(Consts.FIRE_BASE_USERS);
        Map<String, Object> user = new HashMap<>();
        user.put("email", this._email);
        user.put("password", this._password);
        Log.d(TAG, "Login: " + user.get("email"));
        worker.find(user, this.responseHelper, 1);
        return true;
    }
}