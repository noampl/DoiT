package com.example.doit.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.common.Consts;
import com.example.doit.interfaces.IActionBarHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginViewModel extends ViewModel {

    //region members

    private static final String TAG = "LoginViewModel";
    private String _email;
    private String _password;
    private long mLastClickTime = 0;
    private MutableLiveData<Boolean> _isBottomNavUp;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> _logedIn;
    private WeakReference<IActionBarHelper> _actionBarHelper;

    //endregion

    public LoginViewModel() {
        _email = "";
        _password = "";
        _isBottomNavUp = Repository.getInstance().get_isBottomNavigationUp();
        _logedIn = Repository.getInstance().get_loggedIn();
        _actionBarHelper = Repository.getInstance().getActionBarHelper();

    }

    public WeakReference<IActionBarHelper> get_actionBarHelper() {
        return _actionBarHelper;
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
        return _logedIn;
    }

    public MutableLiveData<String> get_Error() {
        return Repository.getInstance().get_remoteError();
    }

    public void login(Map<String, String> user) {
        if (Objects.equals(user.get(Consts.EMAIL), Consts.INVALID_STRING) ||
                Objects.equals(user.get(Consts.PASSWORD), Consts.INVALID_STRING)){
            return;
        }
        Repository.getInstance().login(user);
    }

    public boolean Login(String email, String password){
        Map<String, String> user = new HashMap<>();
        user.put(Consts.EMAIL, email);
        user.put(Consts.PASSWORD, password);
        if(email != null && !email.equals("") &&  password != null && !password.equals("")){
            Repository.getInstance().login(user);
        }
        return true;
    }
}
