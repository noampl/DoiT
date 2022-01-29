package com.example.doit.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;

import java.util.List;

public class UsersViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<User>> _users;
    private List<User> _selectedUser;

    // endregion

    // rergion C'tor

    public UsersViewModel() {
        _users = Repository.getInstance().get_users();
        _selectedUser = Repository.getInstance().get_selectedUsers();
    }

    // endregion

    // region Properties

    public List<User> get_selectedUser() {
        return _selectedUser;
    }

    public void set_selectedUser(List<User> _selectedUser) {
        this._selectedUser = _selectedUser;
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

    public void addUser(int position) {
        User temp = _users.getValue().get(position);
        if (_selectedUser.contains(temp)) {
            _selectedUser.remove(temp);
        }
        else{
            _selectedUser.add(temp);
        }
    }

    public void setUsersById(String groupId){
        Repository.getInstance().setUsersById(groupId);
    }

    public void removeUser(int position){
        _selectedUser.remove(_users.getValue().get(position));
    }

    public void submitUsers() {
        // FIXME needed?
    }

    public int getUserScore(User item) {
        return 0;
    }

    // endregion

}
