package com.example.doit.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.interfaces.IActionBarHelper;
import com.example.doit.interfaces.IResponseHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.model.UserFirebaseWorker;
import com.example.doit.view.MainActivity;

import java.lang.ref.WeakReference;
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
    private MutableLiveData<String> numberOfGroups;
    private MutableLiveData<String> NumberOfTasks;
    private MutableLiveData<Boolean> EditDetails;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> _authSuccess;
    private MutableLiveData<String> _firebaseError;
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
        String stringOfGroups = "Number of groups: ";
        numberOfGroups = new MutableLiveData<>(stringOfGroups + size);
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
        String stringOfTasks = "Number of tasks: ";
        NumberOfTasks = new MutableLiveData<>(stringOfTasks + size);
        return  NumberOfTasks;
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

    public void setActionBarHelper(IActionBarHelper helper) {
        repo.setActionBarHelper(helper);
    }

    public WeakReference<IActionBarHelper> getActionBarHelper() {
        return repo.getActionBarHelper();
    }
}
