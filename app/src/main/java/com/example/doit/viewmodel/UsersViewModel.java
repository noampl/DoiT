package com.example.doit.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UsersViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<User>> _users;
    private MutableLiveData<List<User>> _selectedUser;

    // endregion

    // rergion C'tor

    public UsersViewModel() {
        _users = Repository.getInstance().get_users();
        _selectedUser = Repository.getInstance().get_selectedUsers();
    }

    // endregion

    // region Properties

    public MutableLiveData<List<User>> get_selectedUser() {
        return _selectedUser;
    }

    public void set_selectedUser(List<User> _selectedUser) {
        this._selectedUser.setValue(_selectedUser);
    }

    public MutableLiveData<List<User>> get_users() {
        return _users;
    }

    public void set_users(List<User> _users) {
        this._users.setValue(_users);
    }

    // endregion

    // region Public Methods

    public void searchUsers(String query) {
        Repository.getInstance().searchUsersByNameOrMail(query);
    }

    public void addUser(User user) {
        List<User> tmpList = _selectedUser.getValue();
        tmpList.add(user);
        _selectedUser.setValue(tmpList);
    }

    public void setUsersById(String groupId){
        Repository.getInstance().setUsersById(groupId);
    }

    public void removeUser(User user){
        List<User> tmpList = _selectedUser.getValue();
        tmpList.remove(user);
        _selectedUser.setValue(tmpList);
    }

    public void submitUsers() {
        _users.setValue(_selectedUser.getValue());
    }

    public CompletableFuture<Integer> getUserScore(User item, String groupId) {
        return Repository.getInstance().getUserScoreByGroup(item, groupId);
    }

    public void fetchUsers(){
        Repository.getInstance().getUsersFromLocalDb();
    }

    // endregion

}
