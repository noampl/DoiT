package com.example.doit.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.common.Consts;
import com.example.doit.interfaces.IActionBarHelper;
import com.example.doit.interfaces.IFragmentNavigitionHelper;
import com.example.doit.model.entities.Group;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;
import com.example.doit.interfaces.IDialogNavigationHelper;

import java.lang.ref.WeakReference;
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
    private IFragmentNavigitionHelper _iFragmentNavigitionHelper;
    private MutableLiveData<Integer> _selectedPosition;
    private MutableLiveData<List<User>> _newGroupMembers;
    private String selectedGroupId;
    private WeakReference<IActionBarHelper> _actionBarHelper;

    // endregion

    // region C'tor

    public GroupsViewModel(){
        _groups = Repository.getInstance().getGroups();
        _isBottomNavigationUp = Repository.getInstance().get_isBottomNavigationUp();
        _newGroupMembers = Repository.getInstance().get_newGroupUsers();
        _actionBarHelper = Repository.getInstance().getActionBarHelper();
    }

    // endregion

    // region Properties

    public WeakReference<IActionBarHelper> get_actionBarHelper() {
        return _actionBarHelper;
    }

    public String getSelectedGroupId() {
        return selectedGroupId;
    }

    public IFragmentNavigitionHelper get_iFragmentNavigitionHelper() {
        return _iFragmentNavigitionHelper;
    }

    public void set_iFragmentNavigitionHelper(IFragmentNavigitionHelper _iFragmentNavigitionHelper) {
        this._iFragmentNavigitionHelper = _iFragmentNavigitionHelper;
    }

    public MutableLiveData<Integer> get_selectedPosition() {
        if(_selectedPosition == null) { _selectedPosition = new MutableLiveData<>(Consts.INVALID_POSITION); }
        return _selectedPosition;
    }

    public void set_selectedPosition(Integer _selectedPosition) {
        this._selectedPosition.setValue(_selectedPosition);
    }

    public MutableLiveData<List<User>> get_newGroupMembers() {
        return _newGroupMembers;
    }

    public void set_newGroupMembers(List<User> _newGroupMembers) {
        this._newGroupMembers.setValue(_newGroupMembers);
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

    public MutableLiveData<List<Group>> get_groups() {
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
        set_selectedPosition(Consts.INVALID_POSITION);
        _newGroupMembers.getValue().add(Repository.getInstance().get_authUser().getValue());
        _iDialogNavigationHelper.openDialog();

        // consume the btn press
        return true;
    }

    public boolean addMembersDialog(){
        _iDialogNavigationHelper.openDialog();

        return true;
    }

    public void clearNewGroup(){
        _newGroupMembers.getValue().clear();
    }

    public boolean addNewGroup(Uri imgUri, String name, String desc) {
        if(name == null || name.equals("")){
            return false;
        }
        Uri uri = imgUri != null ? imgUri : Uri.parse("");
        String description = desc != null ? desc : "";

        List<String> membersId = new ArrayList<>();
        Repository.getInstance().get_selectedUsers().forEach((u)->membersId.add(u.get_userId()));

        Group group = new Group("", name , description, membersId, uri.toString() , new ArrayList<String>());
        Repository.getInstance().insertGroup(group);

        return true;
    }

    public void showGroupTasks(Group group, int itemPosition){
        if (_selectedPosition.getValue() != itemPosition) {
            selectedGroupId = group.get_groupId();
            Repository.getInstance().setTaskByGroupId(selectedGroupId);
            _iFragmentNavigitionHelper.openFragment();
        }
        set_selectedPosition(Consts.INVALID_POSITION);
    }

    public Group getGroupById(String id){
        return Repository.getInstance().getGroupById(id);
    }

    // endregion

}
