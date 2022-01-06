package com.example.doit.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.model.entities.Group;
import com.example.doit.model.Repository;
import com.example.doit.view.interfaces.IGroupDialogHelper;

import java.util.List;

/**
 * THis View Model holds all the logged in user's groups
 */
public class GroupsViewModel extends ViewModel {

    // region Members

    private LiveData<List<Group>> _groups;
    private MutableLiveData<Boolean> _isBottomNavigationUp;
    private IGroupDialogHelper _iGroupDialogHelper;

    // endregion

    // region C'tor

    public GroupsViewModel(){
        _groups = Repository.getInstance().getGroups();
        _isBottomNavigationUp = Repository.getInstance().get_isBottomNavigationUp();
    }

    // endregion

    // region Properties


    public IGroupDialogHelper get_iGroupDialogHelper() {
        return _iGroupDialogHelper;
    }

    public void set_iGroupDialogHelper(IGroupDialogHelper _iGroupDialogHelper) {
        this._iGroupDialogHelper = _iGroupDialogHelper;
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

    public void set_groups(LiveData<List<Group>> _groups) {
        this._groups = _groups;
    }

    // endregion

    // region Public Methods

    /**
     * Handling add Group Dialog
     * @return true
     */
    public boolean addGroup(){
        _iGroupDialogHelper.openDialog();
        return true;
    }

    public void updateGroup() {
        
    }

    // endregion

}
