package com.example.doit.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.IResponseHelper;
import com.example.doit.Model.Repository;
import com.example.doit.Model.User;
import com.example.doit.Model.UserFirebaseWorker;
import com.example.doit.common.Consts;

import java.util.Map;

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
    private MutableLiveData<Boolean> LoggedOut;
    private MutableLiveData<Boolean> EditDetails;
    private boolean ImageChanged = false;
    private String ImageUrl;
    //endregion

    public AccountViewModel() {
        repo = Repository.getInstance();
        worker = (UserFirebaseWorker) repo.createWorker(Consts.FIRE_BASE_USERS);
        setNumberOfGroups("Num Of Groups: XXX");
        setNumberOfTasks("Num Of Tasks: XXX");
    }

    public void updateUserDetails() {
        User user = worker.getAuthenticatedUserDetails();
        Map<String, Object> userHashMap = user.getUserMap();
        setUserEmailAddress(user.getEmail());
        setFirstName((String) userHashMap.get("first_name"));
        setLastName((String) userHashMap.get("last_name"));
        setImageUrl((String) userHashMap.get("image"));
    }

    public MutableLiveData<Boolean> getEditDetails() {
        if(EditDetails == null) { EditDetails = new MutableLiveData<>(false); }
        return EditDetails;
    }

    public void setEditDetails(Boolean editDetails) {
        EditDetails.setValue(editDetails);
    }

    public MutableLiveData<Boolean> getLoggedOut() {
        if (LoggedOut == null) { LoggedOut = new MutableLiveData<Boolean>(false); }
        return LoggedOut;
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
        ImageChanged = aBoolean;
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
        worker.logoutAuthUser();
        LoggedOut.setValue(true);
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
    }

    //todo add on change and fix the doc updating

    public void changeDetails(IResponseHelper helper) {
        User user = worker.getAuthenticatedUserDetails();
        user.setEmail(this.getUserEmailAddress().getValue().toLowerCase());
        user.setFirstName(this.getFirstName().getValue());
        user.setLastName(this.getLastName().getValue());
        user.setImgae(this.getImageUrl());
        worker.updateAuthUserDetails(user,Uri.parse(this.ImageUrl),helper, ImageChanged);
    }
}
