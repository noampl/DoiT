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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterViewModel extends ViewModel {

    // region Members

    private final String TAG = "RegisterViewModel";
    private final Repository repo;
    private static final String ISRAEL_COUNTRY_CODE = "+972";
    private User _user;
    public final String title = "Regiter";
    private MutableLiveData<String> _firstName = new MutableLiveData<>("");
    private MutableLiveData<String> _lastName = new MutableLiveData<>("");
    private MutableLiveData<String> _password = new MutableLiveData<>("");
    private MutableLiveData<String> _email = new MutableLiveData<>("");
    private MutableLiveData<String> _phoneCountryCode = new MutableLiveData<>(ISRAEL_COUNTRY_CODE);
    private MutableLiveData<String> _phone = new MutableLiveData<>("");
    private MutableLiveData<String> _passwordValidation = new MutableLiveData<>("");
    private MutableLiveData<Map<String,Boolean>> isClicked = new MutableLiveData<>(new HashMap<>());
    private MutableLiveData<Boolean> attrValid = new MutableLiveData<>(false);
    private MutableLiveData<String> registerError = new MutableLiveData<>("");
    private Roles _role = Roles.CLIENT;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> passwordsIdentical;
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
        initClicked();
    }

    private void initClicked(){
        Map<String, Boolean> map = isClicked.getValue();
        assert map != null;
        map.put("firstName", false);
        map.put("lastName", false);
        map.put("phone", false);
        map.put("password",false);
        isClicked.postValue(map);
    }

    public Uri getImageUri() {
        return ImageUri;
    }

    public MutableLiveData<Boolean> getAttrValid() {
        return attrValid;
    }

    public MutableLiveData<String> getRegisterError() {
        return registerError;
    }

    public void setRegisterError(MutableLiveData<String> registerError) {
        this.registerError = registerError;
    }

    public void setAttrValid(MutableLiveData<Boolean> attrValid) {
        this.attrValid = attrValid;
    }

    public void setImageUri(Uri imageUri) {
        this.ImageUri = imageUri;
        _user.setImage(imageUri.toString());
    }

    public MutableLiveData<String> get_firstName() {
        return _firstName;
    }

    public MutableLiveData<String> get_lastName() {
        return _lastName;
    }

    public MutableLiveData<String> get_password() {
        return _password;
    }

    public MutableLiveData<String> get_email() {
        return _email;
    }

    public MutableLiveData<String> get_phoneCountryCode() {
        if (_phoneCountryCode == null) { _phoneCountryCode.postValue("+972"); }
        return _phoneCountryCode;
    }

    public void setPasswordsIdentical(MutableLiveData<Boolean> passwordsIdentical) {
        if(passwordsIdentical == null) { passwordsIdentical = new MutableLiveData<>(); }
        this.passwordsIdentical = passwordsIdentical;
    }

    public MutableLiveData<String> get_phone() {
        return _phone;
    }

    public MutableLiveData<User> get_authUser() {
        return _authUser;
    }

    public MutableLiveData<String> get_passwordValidation() {
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
        this._firstName.postValue(_firstName);
    }

    public void set_lastName(String _lastName) {
        this._lastName.postValue(_lastName);
    }

    public void set_password(String _password) {
        this._password.postValue(_password);
    }

    public void set_email(String _email) {
        this._email.postValue(_email);
    }

    public void set_phoneCountryCode(String _phoneCountryCode) {
        this._phoneCountryCode.postValue(_phoneCountryCode);
    }

    public void set_phone(String _phone) {
        this._phone.postValue(_phone);
    }

    public void set_passwordValidation(String _passwordValidation) {
        this._passwordValidation.postValue(_passwordValidation);
    }

    public void set_role(Roles _role) {
        this._role = _role;
    }

    public MutableLiveData<Boolean> getPasswordsIdentical() {
        if(passwordsIdentical == null) { passwordsIdentical = new MutableLiveData<>(); }
        return passwordsIdentical;
    }

    public MutableLiveData<Map<String, Boolean>> getIsClicked() {
        return isClicked;
    }

    public void setIsClicked(MutableLiveData<Map<String, Boolean>> isClicked) {
        this.isClicked = isClicked;
    }

    public void onFirstNameChange(CharSequence s, int start, int before, int count) {
        _firstName.postValue(s.toString());
        isClicked.getValue().put("firstName",true);
        _user.setFirstName(s.toString());
    }

    public void onLastNameChange(CharSequence s, int start, int before, int count) {
        _lastName.postValue(s.toString());
        isClicked.getValue().put("lastName",true);
        _user.setLastName(s.toString());
    }

    public void onPhoneChange(CharSequence s, int start, int before, int count) {
        _phone.postValue(s.toString());
        isClicked.getValue().put("phone",true);
        _user.setPhone(s.toString());
    }

    public void onEmailChange(CharSequence s, int start, int before, int count) {
        _email.postValue(s.toString().toLowerCase());
        isClicked.getValue().put("email",true);
        _user.setEmail(s.toString().toLowerCase());
    }

    public void onPasswordChange(CharSequence s, int start, int before, int count) {
        _password.postValue(s.toString());
        isClicked.getValue().put("password",true);
        _user.setPassword(s.toString());
    }

    public void onPasswordValidateChange(CharSequence s, int start, int before, int count) {
        _passwordValidation.postValue(s.toString());
        if (!s.toString().equals(_password.getValue())){
            passwordsIdentical.setValue(false);
            attrValid.postValue(false);
        }
        else{
            passwordsIdentical.setValue(true);
            attrValid.postValue(true);
        }
    }

    private boolean checkIfValid() {
        if(Boolean.FALSE.equals(attrValid.getValue())) { return false; }
        for (Map.Entry<String,Object> entry: _user.create().entrySet()) {
               if(entry.getKey().equals("id") || entry.getKey().equals("groups")){
                   continue;
               } else if (entry.getValue() != null && !Objects.equals(entry.getKey(), "")){
                   continue;
               }
               registerError.setValue(entry.getKey() + " can not be empty!");
               return false;
        }
        return true;
    }

    public boolean register(){
        Log.d(TAG, "Trying to register");
        if (!checkIfValid())
        {
            return false;
        }
        repo.register(this.getImageUri().toString(), _user);
        return true;
    }

    public String getErrorReason(){
        String error;
        if ((error = worker.get_registerErrorReason()) != null) { return error; }
        return "ERROR: Unrecognized problem with register";
    }

}
