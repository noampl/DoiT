package com.example.doit.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.Model.Group;
import com.example.doit.Model.Repository;

/**
 * THis View Model holds all the logged in user's groups
 */
public class GroupsViewModel extends ViewModel {

    // region Members

    private MutableLiveData<Group> _groups;

    // endregion

    // region C'tor

    public GroupsViewModel(){
        _groups = Repository.getInstance();
    }

    // endregion

}
