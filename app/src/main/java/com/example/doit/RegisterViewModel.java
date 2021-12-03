package com.example.doit;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.Model.Consts;
import com.example.doit.Model.Repository;
import com.example.doit.Model.Roles;
import com.example.doit.Model.User;
import com.example.doit.Model.UserFirebaseWorker;

public class RegisterViewModel extends ViewModel {

    // region Members

    private static final String TAG = "RegisterViewModel";
    private Repository repo;
    private static final String ISRAEL_COUNTRY_CODE = "+972";
    private User _user;
    public final String title = "Regiter";
    private String _firstName = "";
    private String _lastName = "";
    private String _password = "";
    private String _email = "";
    private String _phoneCountryCode = ISRAEL_COUNTRY_CODE;
    private String _phone = "";
    private String _passwordValidation = "";
    private Roles.ROLES _role = Roles.ROLES.CLIENT;
    private String _image = "";
    private IResponseHelper responseHelper;
    private final MutableLiveData<Boolean> passwordsIdentical;

// endregion

    public RegisterViewModel() {
        _user = new User();
        repo = Repository.getInstance();
        passwordsIdentical = new MutableLiveData<>();
        passwordsIdentical.setValue(true);
    }

    public MutableLiveData<Boolean> getPasswordsIdentical() {
        return passwordsIdentical;
    }

    public void setPasswordsIdentical(boolean passwordsIdentical) {
        this.passwordsIdentical.setValue(passwordsIdentical);
    }

    public IResponseHelper getResponseHelper() {
        return responseHelper;
    }

    public void setResponseHelper(IResponseHelper responseHelper) {
        this.responseHelper = responseHelper;
    }

    public void onFirstNameChange(CharSequence s, int start, int before, int count) {
        _firstName = s.toString();
        _user.setFirstName(_firstName);
    }

    public void onLastNameChange(CharSequence s, int start, int before, int count) {
        _lastName = s.toString();
        _user.setLastName(_lastName);
    }

    public void onPhoneChange(CharSequence s, int start, int before, int count) {
        _phone = s.toString();
        _user.setPhone(_phone);
    }

    public void onEmailChange(CharSequence s, int start, int before, int count) {
        _email = s.toString();
        _user.setEmail(_email);
    }

    public void onPasswordChange(CharSequence s, int start, int before, int count) {
        _password = s.toString();
        _user.setPassword(_password);
    }

    public void onPasswordValidateChange(CharSequence s, int start, int before, int count) {
        _passwordValidation = s.toString();
        if (!_passwordValidation.equals(_password)){
            passwordsIdentical.setValue(false);
        }
        else{
            passwordsIdentical.setValue(true);
        }
    }

    public boolean register(){
        Log.d(TAG, "Trying to register");
        if (Boolean.TRUE.equals(passwordsIdentical.getValue())){
            UserFirebaseWorker worker = (UserFirebaseWorker) repo.createWorker(Consts.FIRE_BASE_USERS);
            worker.create(_user, responseHelper);
        }
        return true;
    }

}
