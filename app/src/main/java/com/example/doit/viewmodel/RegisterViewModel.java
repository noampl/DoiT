package com.example.doit.viewmodel;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.model.Consts;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.model.UserFirebaseWorker;
import com.example.doit.common.Roles;

public class RegisterViewModel extends ViewModel {

    // region Members

    private final String TAG = "RegisterViewModel";
    private final Repository repo;
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
    private Roles _role = Roles.CLIENT;
    private MutableLiveData<User> _authUser;
    private final MutableLiveData<Boolean> passwordsIdentical;
    private final UserFirebaseWorker worker;
    private Uri ImageUri;
    private MutableLiveData<Boolean> _registering_job_run;

    // endregion

    public RegisterViewModel() {
        repo = Repository.getInstance();
        worker = (UserFirebaseWorker) repo.createWorker(Consts.FIRE_BASE_USERS);
        _user = new User();
        _authUser = repo.get_authUser();
        passwordsIdentical = new MutableLiveData<>();
        passwordsIdentical.setValue(true);
    }

    public Uri getImageUri() {
        return ImageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.ImageUri = imageUri;
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
        if (_phoneCountryCode == null) { _phoneCountryCode = "+972"; }
        return _phoneCountryCode;
    }

    public String get_phone() {
        return _phone;
    }

    public MutableLiveData<User> get_authUser() {
        return _authUser;
    }

    public String get_passwordValidation() {
        return _passwordValidation;
    }

    public Roles get_role() {
        if (_role == null) { _role = Roles.CLIENT; }
        return _role;
    }

    public MutableLiveData<Boolean> get_registering_job_run() {
        if(_registering_job_run == null){
            _registering_job_run = new MutableLiveData<>(false);
        }
        return _registering_job_run;
    }

    public void set_registering_job_run(Boolean _registering_job_run) {
        this._registering_job_run.setValue(_registering_job_run);
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

    public void set_role(Roles _role) {
        this._role = _role;
    }

    public MutableLiveData<Boolean> getPasswordsIdentical() {
        return passwordsIdentical;
    }

    public void setPasswordsIdentical(boolean passwordsIdentical) {
        this.passwordsIdentical.setValue(passwordsIdentical);
    }

    public void onFirstNameChange(CharSequence s, int start, int before, int count) {
        _firstName = s.toString().toLowerCase();
        _user.setFirstName(_firstName);
    }

    public void onLastNameChange(CharSequence s, int start, int before, int count) {
        _lastName = s.toString().toLowerCase();
        _user.setLastName(_lastName);
    }

    public void onPhoneChange(CharSequence s, int start, int before, int count) {
        _phone = s.toString();
        _user.setPhone(_phone);
    }

    public void onEmailChange(CharSequence s, int start, int before, int count) {
        _email = s.toString().toLowerCase();
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
        repo.register(this.getImageUri().toString(), _user);
        return true;
    }

    public String getErrorReason(){
        String error;
        if ((error = worker.get_registerErrorReason()) != null) { return error; }
        return "ERROR: Unrecognized problem with register";
    }

}
