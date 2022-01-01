package com.example.doit.viewmodel;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.IResponseHelper;
import com.example.doit.common.Consts;
import com.example.doit.Model.Repository;
import com.example.doit.Model.Roles;
import com.example.doit.Model.User;
import com.example.doit.Model.UserFirebaseWorker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

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
    private Roles.ROLES _role = Roles.ROLES.CLIENT;
    private String _image = "";
    private IResponseHelper responseHelper;
    private final MutableLiveData<Boolean> passwordsIdentical;
    private final UserFirebaseWorker worker;
    private Uri image_uri;
    private MutableLiveData<Boolean> _registering_job_run;

    // endregion

    public RegisterViewModel() {
        repo = Repository.getInstance();
        worker = (UserFirebaseWorker) repo.createWorker(Consts.FIRE_BASE_USERS);
        _user = new User();
        passwordsIdentical = new MutableLiveData<>();
        passwordsIdentical.setValue(true);
    }

    public Uri getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(Uri image_uri) {
        this.image_uri = image_uri;
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
        set_registering_job_run(true);
        if (Boolean.TRUE.equals(passwordsIdentical.getValue())){
            IResponseHelper a = new IResponseHelper() {
                @Override
                public void actionFinished(boolean actionResult) {
                    _user.setImgae(worker.get_image_url());
                    worker.create(_user, responseHelper);
                }
            };
            worker.upload_image(this.getImage_uri(), a);
            return true;
        }
        set_registering_job_run(false);
        return false;
    }

    public String getErrorReason(){
        String error;
        if ((error = worker.get_registerErrorReason()) != null) { return error; }
        return "ERROR: Unrecognized problem with register";
    }

}
