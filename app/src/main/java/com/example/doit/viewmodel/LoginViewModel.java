package com.example.doit.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.model.Consts;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.model.UserFirebaseWorker;

import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends ViewModel {

    //region members

    private static final String TAG = "LoginViewModel";
    private String _email;
    private String _password;
    private long mLastClickTime = 0;
    private MutableLiveData<Boolean> _isBottomNavUp;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> _logedIn;

    //endregion

    public LoginViewModel() {
        _email = "";
        _password = "";
        _isBottomNavUp = Repository.getInstance().get_isBottomNavigationUp();

    }

    public String getUserName(){
        return this._email;
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

    public MutableLiveData<User> get_authUser() {
        _authUser = Repository.getInstance().get_authUser();
        return _authUser;
    }

    public MutableLiveData<Boolean> get_logedIn() {
        _logedIn = Repository.getInstance().get_loggedIn();
        return _logedIn;
    }

    public MutableLiveData<String> get_Error() {
        return Repository.getInstance().get_remoteError();
    }


    public boolean Login(String email, String password){
        Log.d(TAG, "Login: " + email);
        Log.d(TAG, "Password: " + password);
        Map<String, String> user = new HashMap<>();
        user.put("Email", email);
        user.put("Password", password);
        if(email != null && !email.equals("") &&  password != null && !password.equals("")){
            Repository.getInstance().login(user);
        }
        return true;
    }
}
