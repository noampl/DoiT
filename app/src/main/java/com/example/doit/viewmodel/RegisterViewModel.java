package com.example.doit.viewmodel;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.interfaces.IActionBarHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.common.Roles;

import java.lang.ref.WeakReference;
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
    private MutableLiveData<String> _passwordValidation = new MutableLiveData<>("");
    private MutableLiveData<Map<String,Boolean>> isClicked = new MutableLiveData<>(new HashMap<>());
    private MutableLiveData<Boolean> attrValid = new MutableLiveData<>(false);
    private MutableLiveData<String> registerError;
    private Roles _role = Roles.CLIENT;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> passwordsIdentical;
    private Uri ImageUri;
    private MutableLiveData<Boolean> _registering_job_run;

    // endregion

    public RegisterViewModel() {
        repo = Repository.getInstance();
        _user = new User();
        _authUser = repo.get_authUser();
        passwordsIdentical = new MutableLiveData<>();
        passwordsIdentical.setValue(true);
        registerError = repo.get_remoteError();
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
        if (ImageUri == null) {
            ImageUri = Uri.EMPTY;
        }
        return ImageUri;
    }

    public MutableLiveData<Boolean> getAttrValid() {
        return attrValid;
    }

    public MutableLiveData<String> getRegisterError() {
        return registerError;
    }

    public void setRegisterError(String registerError) {
        this.registerError.setValue(registerError);
    }

    public void setAttrValid(MutableLiveData<Boolean> attrValid) {
        this.attrValid = attrValid;
    }

    public void setImageUri(Uri imageUri) {
        this.ImageUri = imageUri;
        _user.set_image(imageUri.toString());
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

    public void setPasswordsIdentical(MutableLiveData<Boolean> passwordsIdentical) {
        if(passwordsIdentical == null) { passwordsIdentical = new MutableLiveData<>(); }
        this.passwordsIdentical = passwordsIdentical;
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

    public WeakReference<IActionBarHelper> getActionBarHelper() {
        return repo.getActionBarHelper();
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

    public void onEmailChange(CharSequence s, int start, int before, int count) {
        _email.postValue(s.toString().toLowerCase());
        isClicked.getValue().put("email",true);
        _user.setEmail(s.toString().toLowerCase());
    }

    public void onPasswordChange(CharSequence s, int start, int before, int count) {
        _password.postValue(s.toString());
        isClicked.getValue().put("password",true);
        _user.setPassword(s.toString());
        passwordsIdentical.setValue(false);
    }

    public void onPasswordValidateChange(CharSequence s, int start, int before, int count) {
        _passwordValidation.postValue(s.toString());
        if (_password.getValue().equals("")||!s.toString().equals(_password.getValue())){
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
        if (_user.get_firstName().equals("")){
            registerError.setValue("first name can not be empty!");
            return false;
        }
        if (_user.get_lastName().equals("")){
            registerError.setValue("last name can not be empty!");
            return false;
        }
        if (_user.get_email().equals("")){
            registerError.setValue("email can not be empty");
            return false;
        }
        if (_user.get_password().equals("")){
            registerError.setValue("password cannot be empty");
            return false;
        }
        if (_user.get_password().length() < 6){
            registerError.setValue("password must be longer than 6 characters");
        }
        if (!passwordsIdentical.getValue()){
            registerError.setValue("passwords does not match");
            return false;
        }
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

}
