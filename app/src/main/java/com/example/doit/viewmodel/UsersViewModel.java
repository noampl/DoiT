package com.example.doit.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.model.Repository;
import com.example.doit.model.entities.User;

import java.util.List;

public class UsersViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<User>> _users;

    // endregion

    // ergion C'tor

    public UsersViewModel() {
        _users = Repository.getInstance().get_users();
    }

    // endregion

    // region Properties

    public MutableLiveData<List<User>> get_users() {
        return _users;
    }

    public void set_users(List<User> _users) {
        this._users.setValue(_users);
    }

    public void searchUsers(String query) {
        Repository.getInstance().searchUsersByNameOrMail(query);
    }


    // endregion

}
