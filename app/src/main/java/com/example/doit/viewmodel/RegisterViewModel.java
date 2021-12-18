package com.example.doit.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.IResponseHelper;
import com.example.doit.common.Consts;
import com.example.doit.Model.Repository;
import com.example.doit.Model.Roles;
import com.example.doit.Model.User;
import com.example.doit.Model.UserFirebaseWorker;

public class RegisterViewModel extends ViewModel {

    // region Members

    private final String TAG = "RegisterViewModel";
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

    public String get_firstName() {
        return _firstName;
    }

    public String get_lastName() {
        return _lastName;
    }

    public String get_password() {
        return _password;
    }

    public String get_email() {
        return _email;
    }

    public String get_phoneCountryCode() {
        return _phoneCountryCode;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_passwordValidation() {
        return _passwordValidation;
    }

    public Roles.ROLES get_role() {
        return _role;
    }

    public String get_image() {
        return _image;
    }


    public void set_user(User _user) {
        this._user = _user;
    }

    public void set_firstName(String _firstName) {
        this._firstName = _firstName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public void set_phoneCountryCode(String _phoneCountryCode) {
        this._phoneCountryCode = _phoneCountryCode;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }

    public void set_passwordValidation(String _passwordValidation) {
        this._passwordValidation = _passwordValidation;
    }

    public void set_role(Roles.ROLES _role) {
        this._role = _role;
    }

    public void set_image(String _image) {
        this._image = _image;
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
