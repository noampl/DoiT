package com.example.doit.viewmodel;

import android.net.Uri;
import android.util.Log;
import android.widget.DatePicker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.interfaces.IFragmentNavigitionHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.UserFirebaseWorker;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TasksViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<Task>> _tasks;
    private IDialogNavigationHelper _iDialogNavigationHelper;
    private IFragmentNavigitionHelper _iFragmentNavigationHelper;
    private int taskValue;
    private long _targetDate;
    private List<User> _groupUsers;
    private String _groupId;
    private String _assigneeId;
    private String _createdById;
    private String _tasksDetailsId;
    // endregion

    // region C'tor

    public TasksViewModel(){
        _tasks = Repository.getInstance().get_tasks();
        _createdById = Repository.getInstance().get_authUser().getValue().get_userId();
        _tasksDetailsId = Repository.getInstance().get_taskDetailsId();
    }

    // endregion

    // region Properties

    public List<User> get_groupUsers() {
        return new ArrayList<>(Repository.getInstance().getUsersByGroup(_groupId));
    }

    public String get_groupId() {
        return _groupId;
    }

    public void set_groupId(String _groupId) {
        this._groupId = _groupId;
    }

    public String get_tasksDetailsId() {
        return _tasksDetailsId;
    }

    public void set_tasksDetailsId(String _tasksDetailsId) {
        this._tasksDetailsId = _tasksDetailsId;
    }

    public MutableLiveData<List<Task>> get_tasks() {
        return _tasks;

    }

    public void set_tasks(List<Task> _tasks) {
        this._tasks.setValue(_tasks);
    }

    public void set_iDialogNavigationHelper(IDialogNavigationHelper _iDialogNavigationHelper) {
        this._iDialogNavigationHelper = _iDialogNavigationHelper;
    }

    public IFragmentNavigitionHelper get_iFragmentNavigationHelper() {
        return _iFragmentNavigationHelper;
    }

    public void set_iFragmentNavigationHelper(IFragmentNavigitionHelper _iFragmentNavigationHelper) {
        this._iFragmentNavigationHelper = _iFragmentNavigationHelper;
    }

    // endregion

    // region Public

    public Task getTaskById(String taskId) {
        return Repository.getInstance().getTaskById(taskId);
    }

    public Group getGroupByTask(Task task) {
            return Repository.getInstance().getGroupById(task.get_groupId());
    }

    public User getUserByTask(Task task) {
        return getUserById(task.get_assigneeId());
    }

    public User getUserById(String id) {
        return Repository.getInstance().getUserFromSql(id);
    }

    public void fetchTasks(){
        Repository.getInstance().fetchTasks();
    }

    public void valueSelected(int i, long l){
        taskValue = i;
    }

    public void openDialog(){
        _iDialogNavigationHelper.openDialog();
    }

    public boolean createTask(Uri uri, String taskName, String taskDesc, int taskValue) {

        if (taskName == null || taskName.equals("")){
            return false;
        }
        String ImageUri = uri != null ? uri.toString() : "";

        if (Repository.getInstance().get_selectedUsers().size() == 0) {
            _assigneeId = "";
        } else {
            _assigneeId = Repository.getInstance().get_selectedUsers().get(0).get_userId();
        }
        Task task = new Task("", _groupId, taskName, taskDesc, (long)new Date().getTime(),
                _targetDate, _createdById, _assigneeId, taskValue, ImageUri);
        Repository.getInstance().createTask(task);
        return true;
    }

    public void getTasksByGroupId(String groupId) {
        set_groupId(groupId);
        set_tasks(Repository.getInstance().getTasksByGroupId(groupId));
    }

    public void setTargetDate(DatePicker datePicker){
        _targetDate = datePicker.getAutofillValue().getDateValue();
    }

    public boolean details(Task task){
        set_tasksDetailsId(task.get_taskId());
        _iFragmentNavigationHelper.openFragment();
        return true;
    }

    // endregion

}
