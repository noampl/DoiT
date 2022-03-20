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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * THis View Model holds all the logged in user's groups
 */
public class GroupsViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<Group>> _groups;
    private MutableLiveData<Boolean> _isBottomNavigationUp;
    private IDialogNavigationHelper _iDialogNavigationHelper;
    private IFragmentNavigitionHelper _iFragmentNavigitionHelper;
    private MutableLiveData<String> _selectedGroupId; // uses for the UI
    private MutableLiveData<List<User>> _newGroupMembers;
    private String selectedGroupIdForDb; // uses for the Db
    private WeakReference<IActionBarHelper> _actionBarHelper;
    private MutableLiveData<Boolean> _isEdit;
    private MutableLiveData<Boolean> _isGroupsLoading;

    // endregion

    // region C'tor

    public GroupsViewModel(){
        _groups = Repository.getInstance().getGroups();
        _isBottomNavigationUp = Repository.getInstance().get_isBottomNavigationUp();
        _newGroupMembers = Repository.getInstance().get_newGroupUsers();
        _actionBarHelper = Repository.getInstance().getActionBarHelper();
        _isEdit = new MutableLiveData<>(false);
        _isGroupsLoading = Repository.getInstance().getIsGroupsLoading();
    }

    // endregion

    // region Properties

    public MutableLiveData<Boolean> get_isGroupsLoading() {
        return _isGroupsLoading;
    }

    public void set_isGroupsLoading(boolean _isGroupsLoading) {
        this._isGroupsLoading.setValue(_isGroupsLoading);
    }

    public MutableLiveData<Boolean> get_isEdit() {
        return _isEdit;
    }

    public void set_isEdit(boolean isEdit) {
        this._isEdit.setValue(isEdit);
    }

    public WeakReference<IActionBarHelper> get_actionBarHelper() {
        return _actionBarHelper;
    }

    public String getSelectedGroupIdForDb() {
        return selectedGroupIdForDb;
    }

    public void setSelectedGroupIdForDb(String groupId){
        selectedGroupIdForDb = groupId;
    }

    public IFragmentNavigitionHelper get_iFragmentNavigitionHelper() {
        return _iFragmentNavigitionHelper;
    }

    public void set_iFragmentNavigitionHelper(IFragmentNavigitionHelper _iFragmentNavigitionHelper) {
        this._iFragmentNavigitionHelper = _iFragmentNavigitionHelper;
    }

    public MutableLiveData<String> get_selectedGroupId() {
        if(_selectedGroupId == null) { _selectedGroupId = new MutableLiveData<>(Consts.INVALID_STRING); }
        return _selectedGroupId;
    }

    public void set_selectedGroupId(String _selectedGroupId) {
        this._selectedGroupId.setValue(_selectedGroupId);
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
        set_selectedGroupId(Consts.INVALID_STRING);
        _newGroupMembers.getValue().add(Repository.getInstance().get_authUser().getValue());
        _iDialogNavigationHelper.openDialog();

        // consume the btn press
        return true;
    }

    public boolean addMembersDialog(){
        Repository.getInstance().get_users().getValue().clear();
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
        Repository.getInstance().get_selectedUsers().getValue().forEach((u)->membersId.add(u.get_userId()));

        Group group = new Group("", name , description, membersId, uri.toString() , new ArrayList<String>());
        Repository.getInstance().insertGroup(group);

        return true;
    }

    public void showGroupTasks(Group group){
        if (!_selectedGroupId.getValue().equals(group.get_groupId())) {
            selectedGroupIdForDb = group.get_groupId();
            Repository.getInstance().setTaskByGroupId(selectedGroupIdForDb);
            _iFragmentNavigitionHelper.openFragment();
        }
        set_selectedGroupId(Consts.INVALID_STRING);
    }

    public CompletableFuture<Group> getGroupById(String id){
        return Repository.getInstance().getGroupById(id);
    }

    public void saveEditGroup(String name, String desc, Group groupToUpdate, List<User> users) {
        if (name != null && !name.equals("")){
            groupToUpdate.set_name(name);
        }
        if(desc != null && !desc.equals("")){
            groupToUpdate.set_description(desc);
        }
        if (users != null){
            // update all the users that removed from the group
            List<String> oldUsersId = groupToUpdate.getMembersId();
            for (String id : oldUsersId){
                boolean isInUsers = false;
                for (User user : users){
                    if (user.get_userId().equals(id)){
                        isInUsers = true;
                        break;
                    }
                }
                if (!isInUsers){
                    Repository.getInstance().deleteUserFromGroup(groupToUpdate, id);
                }
            }
            groupToUpdate.setMembersId(users.stream().map(User::get_userId).collect(Collectors.toList()));
        }
        updateGroup(groupToUpdate);
        set_isEdit(false);
    }

    public void updateGroup(Group group){
        Repository.getInstance().updateGroup(group);
    }

    public void deleteGroup(Group group){
        Repository.getInstance().deleteGroup(group);
    }

    public void deleteGroup(String groupId){
        Repository.getInstance().deleteGroupById(groupId);
    }

    public void fetchRemoteGroups() {
        Repository.getInstance().fetchRemoteGroups();
    }

    // endregion

}
