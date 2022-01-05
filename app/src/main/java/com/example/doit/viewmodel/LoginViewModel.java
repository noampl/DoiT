package com.example.doit.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.IResponseHelper;
import com.example.doit.Model.Consts;
import com.example.doit.Model.Repository;
import com.example.doit.Model.User;
import com.example.doit.Model.UserFirebaseWorker;

import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends ViewModel {

    //region members

    private static final String TAG = "LoginViewModel";
    private String _email;
    private String _password;
    private IResponseHelper responseHelper;
    private UserFirebaseWorker worker;
    private long mLastClickTime = 0;
    private MutableLiveData<Boolean> _isBottomNavUp;

    //endregion

    public LoginViewModel() {
        _email = "";
        _password = "";
        worker = (UserFirebaseWorker) Repository.getInstance().createWorker(Consts.FIRE_BASE_USERS);
        _isBottomNavUp = Repository.getInstance().get_isBottomNavigationUp();

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

    public User getAuthUser() {
        return worker.getAuthenticatedUserDetails();
    }

    public String getPassword() {
        return this._password;
    }

    public void onUserNameChange(CharSequence s, int start, int before, int count) {
        this._email = s.toString().toLowerCase();
    }

    public void onPasswordChange(CharSequence s, int start, int before, int count) {
        this._password = s.toString();
    }

    public MutableLiveData<Boolean> get_isBottomNavUp() {
        return _isBottomNavUp;
    }

    public void set_isBottomNavUp(boolean _isBottomNavUp) {
        this._isBottomNavUp.setValue(_isBottomNavUp);
    }

    public boolean Login(String email, String password){
        Log.d(TAG, "Login: " + email);
        Log.d(TAG, "Password: " + password);
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);
        Repository.getInstance().saveAuthUser(user, this.responseHelper);
        //worker.login(user, this.responseHelper);
        return true;
    }
}
