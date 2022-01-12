package com.example.doit.viewmodel;

import android.net.Uri;
import android.widget.DatePicker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;

import java.util.Date;
import java.util.List;

public class TasksViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<Task>> _tasks;
    private IDialogNavigationHelper _iDialogNavigationHelper;
    private int taskValue;
    private long _targetDate;
    private String _groupId;
    private String _assigneeId;
    private String _createdById;

    // endregion

    // region C'tor

    public TasksViewModel(){
        _tasks = Repository.getInstance().get_tasks();
        _createdById = Repository.getInstance().get_authUser().getValue().get_userId();
    }

    // endregion

    // region Properties

    public MutableLiveData<List<Task>> get_tasks() {
        return _tasks;
    }

    public void set_tasks(List<Task> _tasks) {
        this._tasks.setValue(_tasks);
    }

    // endregion

    // region Public

    public void set_iDialogNavigationHelper(IDialogNavigationHelper _iDialogNavigationHelper) {
        this._iDialogNavigationHelper = _iDialogNavigationHelper;
    }

    public Group getGroupByTask(Task task) {
            return Repository.getInstance().getGroupById(task.get_groupId());
    }

    public User getUserByTask(Task task) {
        return Repository.getInstance().getUserFromSql(task.get_assigneeId());
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

    public boolean createTask(Uri uri, String taskName, String taskDesc) {

        if (taskName == null || taskName.equals("")){
            return false;
        }
        String ImageUri = uri != null ? uri.toString() : "";
        _assigneeId = Repository.getInstance().get_selectedUsers().get(0).get_userId();
        Task task = new Task("", _groupId, taskName, taskDesc, (long)new Date().getTime(),
                _targetDate, _createdById, _assigneeId, taskValue, ImageUri);
        Repository.getInstance().createTask(task);
        return true;
    }

    public void getTasksByGroupId(String groupId) {
        _groupId = groupId;
       set_tasks(Repository.getInstance().getTasksByGroupId(groupId));
    }

    public void setTargetDate(DatePicker datePicker){
        _targetDate = datePicker.getAutofillValue().getDateValue();
    }

    // endregion

}
