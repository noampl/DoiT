package com.example.doit.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.interfaces.IResponseHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.model.UserFirebaseWorker;

import java.util.Objects;
public class AccountViewModel extends ViewModel {
    //region members
    private static final String TAG = "AccountViewModel";
    private final Repository repo;
    private IResponseHelper responseHelper;
    private UserFirebaseWorker worker;
    private MutableLiveData<String> UserEmailAddress;
    private MutableLiveData<String> FirstName;
    private MutableLiveData<String> LastName;
    private MutableLiveData<String> NumberOfGroups;
    private MutableLiveData<String> NumberOfTasks;
    private MutableLiveData<Boolean> EditDetails;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> _authSuccess;
    private MutableLiveData<String> _loginError;
    private boolean emailChanged = false;
    private boolean imageChanged = false;
    private String ImageUrl;
    //endregion

    public AccountViewModel() {
        repo = Repository.getInstance();
        _authUser = repo.get_authUser();
        setNumberOfGroups("Num Of Groups: XXX");
        setNumberOfTasks("Num Of Tasks: XXX");
    }

    public void updateUserDetails() {
        User user = _authUser.getValue();
        assert user != null;
        setUserEmailAddress(user.get_email());
        setFirstName(user.get_firstName());
        setLastName(user.get_lastName());
        setImageUrl(user.get_image());
    }

    public MutableLiveData<String> get_operationError(){
        if (_loginError == null) { _loginError = repo.get_Error(); }
        return _loginError;
    }

    public MutableLiveData<User> get_authUser() {
        return _authUser;
    }

    public MutableLiveData<Boolean> get_authSuccess() {
        return repo.get_loggedIn();
    }

    public MutableLiveData<Boolean> getEditDetails() {
        if(EditDetails == null) { EditDetails = new MutableLiveData<>(false); }
        return EditDetails;
    }

    public void setEditDetails(Boolean editDetails) {
        EditDetails.setValue(editDetails);
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public MutableLiveData<String> getUserEmailAddress() {
        if (this.UserEmailAddress == null) {
            this.UserEmailAddress = new MutableLiveData<String>("no email address");
        }
        return UserEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        if (this.UserEmailAddress == null) {
            this.UserEmailAddress = new MutableLiveData<String>("no email address");
        }
        this.UserEmailAddress.setValue(userEmailAddress);
    }

    public MutableLiveData<String> getFirstName() {
        if(FirstName == null) { FirstName = new MutableLiveData<String>(""); }
        return FirstName;
    }

    public void setFirstName(String firstName) {
        if(FirstName == null) { FirstName = new MutableLiveData<String>(""); }
        FirstName.setValue(firstName);
    }

    public MutableLiveData<String> getLastName() {
        if(LastName == null) { LastName = new MutableLiveData<String>(""); }
        return LastName;
    }

    public void setLastName(String lastName) {
        if(LastName == null) { LastName = new MutableLiveData<String>(""); }
        LastName.setValue(lastName);
    }

    public MutableLiveData<String> getNumberOfGroups() {
        if(NumberOfGroups == null) { NumberOfGroups = new MutableLiveData<>(""); }
        return NumberOfGroups;
    }

    public void setImageChanged(Boolean aBoolean) {
        imageChanged = aBoolean;
    }

    public void setNumberOfGroups(String numberOfGroups) {
        if(NumberOfGroups == null) { NumberOfGroups = new MutableLiveData<>(""); }
        NumberOfGroups.setValue(numberOfGroups);
    }

    public MutableLiveData<String> getNumberOfTasks() {
        if(NumberOfTasks == null) { NumberOfTasks = new MutableLiveData<>(""); }
        return NumberOfTasks;
    }

    public void setNumberOfTasks(String numberOfTasks) {
        if(NumberOfTasks == null) { NumberOfTasks = new MutableLiveData<>(""); }
        NumberOfTasks.setValue(numberOfTasks);
    }

    public boolean onClickLogoutButton() {
        repo.logout();
        return true;
    }

    public void onFirstNameChange(CharSequence s, int start, int before, int count) {
        this.setFirstName(s.toString());
    }

    public void onLastNameChange(CharSequence s, int start, int before, int count) {
        this.setLastName(s.toString());
    }

    public void onEmailChange(CharSequence s, int start, int before, int count) {
        this.setUserEmailAddress(s.toString());
        emailChanged = true;
    }

    public void changeDetails() {
        User user = this._authUser.getValue();
        assert user != null;
        user.setEmail(Objects.requireNonNull(this.getUserEmailAddress().getValue()).toLowerCase());
        user.setFirstName(this.getFirstName().getValue());
        user.setLastName(this.getLastName().getValue());
        user.set_image(this.getImageUrl());
        repo.updateAuthUserDetails(user,Uri.parse(this.ImageUrl), imageChanged, emailChanged);
        emailChanged = false;
        imageChanged = false;
    }
}
