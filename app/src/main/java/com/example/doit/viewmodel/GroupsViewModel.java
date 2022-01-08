package com.example.doit.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.interfaces.IGroupDialogHelper;
import com.example.doit.model.entities.Group;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;
import com.example.doit.interfaces.IDialogNavigationHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * THis View Model holds all the logged in user's groups
 */
public class GroupsViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<Group>> _groups;
    private MutableLiveData<Boolean> _isBottomNavigationUp;
    private IDialogNavigationHelper _iDialogNavigationHelper;

    private MutableLiveData<Integer> _selectedPosition;
    private MutableLiveData<List<User>> _newGroupMembers;
    private MutableLiveData<List<User>> _newGroupAdmins;

    // endregion

    // region C'tor

    public GroupsViewModel(){
        _groups = Repository.getInstance().getGroups();
        _isBottomNavigationUp = Repository.getInstance().get_isBottomNavigationUp();
        _newGroupAdmins = Repository.getInstance().get_newGroupAdmins();
        _newGroupMembers = Repository.getInstance().get_newGroupUsers();
    }

    // endregion

    // region Properties

    public MutableLiveData<Integer> get_selectedPosition() {
        return _selectedPosition;
    }

    public void set_selectedPosition(MutableLiveData<Integer> _selectedPosition) {
        this._selectedPosition = _selectedPosition;
    }

    public MutableLiveData<List<User>> get_newGroupMembers() {
        return _newGroupMembers;
    }

    public void set_newGroupMembers(List<User> _newGroupMembers) {
        this._newGroupMembers.setValue(_newGroupMembers);
    }

    public MutableLiveData<List<User>> get_newGroupAdmins() {
        return _newGroupAdmins;
    }

    public void set_newGroupAdmins(List<User> _newGroupAdmins) {
        this._newGroupAdmins.setValue(_newGroupAdmins);
    }

    public IDialogNavigationHelper get_iDialogNavigationHelper() {
        return _iDialogNavigationHelper;
    }

    public void set_iDialogNavigationHelper(IDialogNavigationHelper _iDialogNavigationHelper) {
        this._iDialogNavigationHelper = _iDialogNavigationHelper;
    }

    public MutableLiveData<Boolean> get_isBottomNavigationUp() {
        return _isBottomNavigationUp;
    }

    public void set_isBottomNavigationUp(boolean _isBottomNavigationUp) {
        this._isBottomNavigationUp.setValue(_isBottomNavigationUp);
    }

    public LiveData<List<Group>> get_groups() {
        return _groups;
    }

    public void set_groups(List<Group> _groups) {
        this._groups.setValue(_groups);
    }

    // endregion

    // region Public Methods

    /**
     * Handling add Group Dialog
     * @return true
     */
    public boolean addGroupDialog(){
        _newGroupMembers.getValue().add(Repository.getInstance().get_authUser().getValue());
        _newGroupAdmins.getValue().add(Repository.getInstance().get_authUser().getValue());
        _iDialogNavigationHelper.openDialog();

        // consume the btn press
        return true;
    }

    public boolean addAdminsDialog(){

        // TODO implement later
        return true;
    }

    public boolean addMembersDialog(){

        _iDialogNavigationHelper.openDialog();

        return true;
    }

    public void clearNewGroup(){
        _newGroupAdmins.getValue().clear();
        _newGroupMembers.getValue().clear();
    }

    public boolean addNewGroup(Uri imgUri, String name, String desc, List<User> admins, List<User> members) {
        if(name == null || name.equals("")){
            return false;
        }
        Uri uri = imgUri != null ? imgUri : Uri.parse("");
        String description = desc != null ? desc : "";

        List<String> membersId = new ArrayList<>(), adminsId = new ArrayList<>();
        members.forEach((u)->membersId.add(u.get_userId()));
        admins.forEach((u)->adminsId.add(u.get_userId()));


        Group group = new Group("", name , description, membersId, adminsId, uri.toString() , new ArrayList<String>());
        Repository.getInstance().insertGroup(group);

        return true;
    }

    // endregion

}
