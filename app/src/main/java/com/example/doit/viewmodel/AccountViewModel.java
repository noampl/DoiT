package com.example.doit.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.interfaces.IActionBarHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.model.UserFirebaseWorker;

import java.lang.ref.WeakReference;

public class AccountViewModel extends ViewModel {
    //region members
    private static final String TAG = "AccountViewModel";
    private final Repository repo;
    private UserFirebaseWorker worker;
    private MutableLiveData<String> UserEmailAddress;
    private MutableLiveData<String> FirstName;
    private MutableLiveData<String> LastName;
    private MutableLiveData<String> numberOfGroups;
    private MutableLiveData<String> NumberOfTasks;
    private MutableLiveData<Boolean> EditDetails;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> _authSuccess;
    private MutableLiveData<String> _firebaseError;
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
        if (_firebaseError == null) { _firebaseError = repo.get_remoteError(); }
        return _firebaseError;
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
        String size = String.valueOf(Repository.getInstance().getGroups().getValue().size());
        numberOfGroups = new MutableLiveData<>(size);
        return numberOfGroups;
    }

    public void setImageChanged(Boolean aBoolean) {
        imageChanged = aBoolean;
    }

    public void setNumberOfGroups(String numberOfGroups) {
        if(this.numberOfGroups == null) { this.numberOfGroups = new MutableLiveData<>(""); }
        this.numberOfGroups.setValue(numberOfGroups);
    }

    public MutableLiveData<String> getNumberOfTasks() {
        String size = String.valueOf(Repository.getInstance().get_tasks().getValue().size());
        NumberOfTasks = new MutableLiveData<>(size);
        return  NumberOfTasks;
    }

    public void setNumberOfTasks(String numberOfTasks) {
        if(NumberOfTasks == null) { NumberOfTasks = new MutableLiveData<>(""); }
        NumberOfTasks.setValue(numberOfTasks);
    }

    public void onClickLogoutButton() {
        repo.logout();
    }

    public boolean saveDetails(String email, String firstName, String lastName) {
        User user = this._authUser.getValue();

        if (email == null || email.equals("") || isInValidEmail(email)){
            return false;
        }
        user.setEmail(email.toLowerCase().trim());
        if (firstName == null || firstName.equals("") || firstName.length() < 1){
            return false;
        }
        user.setFirstName(firstName);
        if (lastName == null || lastName.equals("") || lastName.length() < 1){
            return false;
        }
        user.setLastName(this.getLastName().getValue());
        user.set_image(this.getImageUrl());
        repo.updateAuthUserDetails(user,Uri.parse(this.ImageUrl), imageChanged, true);
        imageChanged = false;
        return true;
    }

    private boolean isInValidEmail(String email) {
        return (email.length() < 5 || !email.contains("@") || !email.contains("."));
    }

    public void setActionBarHelper(IActionBarHelper helper) {
        repo.setActionBarHelper(helper);
    }

    public WeakReference<IActionBarHelper> getActionBarHelper() {
        return repo.getActionBarHelper();
    }
}
