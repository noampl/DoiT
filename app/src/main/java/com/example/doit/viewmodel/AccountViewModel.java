package com.example.doit.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.IResponseHelper;
import com.example.doit.Model.Repository;
import com.example.doit.Model.User;
import com.example.doit.Model.UserFirebaseWorker;
import com.example.doit.common.Consts;

import java.util.HashMap;
import java.util.Map;

public class AccountViewModel extends ViewModel {
    //region members
    private static final String TAG = "AccountViewModel";
    private final Repository repo;
    private IResponseHelper responseHelper;
    private UserFirebaseWorker worker;
    private MutableLiveData<String> UserEmailAddress;
    private MutableLiveData<String> UserName;
    private MutableLiveData<String> NumberOfGroups;
    private MutableLiveData<String> NumberOfTasks;
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
        setUserName(userHashMap.get("first_name") + " " + userHashMap.get("last_name"));
        setImageUrl((String) userHashMap.get("image"));
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public MutableLiveData<String> getUserEmailAddress() {
        return UserEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        if (this.UserEmailAddress == null) {
            this.UserEmailAddress = new MutableLiveData<String>("no email address");
        }
        this.UserEmailAddress.setValue(userEmailAddress);
    }

    public MutableLiveData<String> getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        if(UserName == null) { UserName = new MutableLiveData<String>();}
        UserName.setValue(userName);
    }

    public MutableLiveData<String> getNumberOfGroups() {
        return NumberOfGroups;
    }

    public void setNumberOfGroups(String numberOfGroups) {
        if(NumberOfGroups == null) { NumberOfGroups = new MutableLiveData<>(); }
        NumberOfGroups.setValue(numberOfGroups);
    }

    public MutableLiveData<String> getNumberOfTasks() {
        return NumberOfTasks;
    }

    public void setNumberOfTasks(String numberOfTasks) {
        if(NumberOfTasks == null) { NumberOfTasks = new MutableLiveData<>(); }
        NumberOfTasks.setValue(numberOfTasks);
    }
}
