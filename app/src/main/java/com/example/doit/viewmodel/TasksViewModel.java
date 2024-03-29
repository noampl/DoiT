package com.example.doit.viewmodel;

import android.net.Uri;
import android.widget.DatePicker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.common.Consts;
import com.example.doit.interfaces.IActionBarHelper;
import com.example.doit.interfaces.IDialogNavigationHelper;
import com.example.doit.interfaces.IFragmentNavigitionHelper;
import com.example.doit.model.Repository;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TasksViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<Task>> _tasks;
    private IDialogNavigationHelper _iDialogNavigationHelper;
    private IFragmentNavigitionHelper _iFragmentNavigationHelper;
    private int taskValue;
    private MutableLiveData<Long> _targetDate;
    private String _groupId;
    private String _assigneeId;
    private String _createdById;
    private String _tasksDetailsId;
    private final WeakReference<IActionBarHelper> _actionBarHelper;
    private final MutableLiveData<String> _selectedTaskId;
    private final MutableLiveData<Boolean> _isEdit;
    private MutableLiveData<Boolean> _isTaskLoading;

    // endregion

    // region C'tor

    public TasksViewModel(){
        _tasks = Repository.getInstance().get_tasks();
        _createdById = Repository.getInstance().get_authUser().getValue().get_userId();
        _tasksDetailsId = Repository.getInstance().get_taskDetailsId();
        _actionBarHelper = Repository.getInstance().getActionBarHelper();
        _isTaskLoading = Repository.getInstance().getIsTaskLoading();
        _selectedTaskId = new MutableLiveData<>(Consts.INVALID_STRING);
        _isEdit = new MutableLiveData<>(false);
        _targetDate = new MutableLiveData<>();
    }

    // endregion

    // region Properties

    public MutableLiveData<Boolean> getIsTaskLoading(){
        return _isTaskLoading;
    }

    public MutableLiveData<Long> get_targetDate() {
        return _targetDate;
    }

    public MutableLiveData<Boolean> get_isEdit() {
        return _isEdit;
    }

    public void set_isEdit(boolean _isEdit) {
        this._isEdit.setValue(_isEdit);
    }

    public WeakReference<IActionBarHelper> get_actionBarHelper() {
        return _actionBarHelper;
    }

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

    public MutableLiveData<String> get_selectedTaskID() {
        return _selectedTaskId;
    }

    public void set_selectedTaskID(String id) {
        this._selectedTaskId.setValue(id);
    }

    // endregion

    // region Public

    public CompletableFuture<Task> getTaskById(String taskId) {
        return Repository.getInstance().getTaskById(taskId);
    }

    public CompletableFuture<Group> getGroupByTask(Task task) {
            return Repository.getInstance().getGroupById(task.get_groupId());
    }

    public User getUserByTask(Task task) {
        return getUserById(task.get_assigneeId());
    }

    public User getUserById(String id) {
        return Repository.getInstance().getUserFromLocal(id);
    }

    public void fetchLocalTasks(){
        Repository.getInstance().fetchLocalTasks();
    }

    public void fetchRemoteTasks(String _groupId){
        Repository.getInstance().fetchRemoteTasks(_groupId);
    }

    public void valueSelected(int i, long l){
        taskValue = i;
    }

    public void openDialog(){
        set_selectedTaskID(Consts.INVALID_STRING);
        _iDialogNavigationHelper.openDialog();
    }

    public boolean createTask(Uri uri, String taskName, String taskDesc, int taskValue, String groupId) {
        if (taskName == null || taskName.equals("")){
            return false;
        }
        String ImageUri = uri != null ? uri.toString() : "";

        if (Repository.getInstance().get_selectedUsers().getValue().size() == 0) {
            _assigneeId = "";
        } else {
            _assigneeId = Repository.getInstance().get_selectedUsers().getValue().get(0).get_userId();
        }
        Task task = new Task("", groupId, taskName, taskDesc, (long)new Date().getTime(),
                (_targetDate.getValue() != null ? _targetDate.getValue() : 0), _createdById, _assigneeId, taskValue, ImageUri);
        Repository.getInstance().createTask(task);
        return true;
    }

    public void getTasksByGroupId(String groupId) {
        set_groupId(groupId);
        Repository.getInstance().getTasksByGroupId(groupId).thenAccept(this::set_tasks);
    }

    public void setTargetDate(DatePicker datePicker) {
        setTargetDate(datePicker.getAutofillValue().getDateValue());
    }

    public void setTargetDate(long targetDate){
        _targetDate.setValue(targetDate);
    }

    public boolean details(Task task){
        if(!_selectedTaskId.getValue().equals(task.get_taskId())) {
            set_tasksDetailsId(task.get_taskId());
            _iFragmentNavigationHelper.openFragment();
        }
        set_selectedTaskID(Consts.INVALID_STRING);
        return true;
    }

    public void updateTask(Task task) {
        Repository.getInstance().updateTask(task);
    }

    public void saveChanges(Task task, String name, String description, boolean isChecked) {
        if (name != null && !name.equals("")){
            task.set_name(name);
        }
        if(description != null && !description.equals("")){
            task.set_description(description);
        }
        if (isChecked){
            task.set_finishDate(new Date().getTime());
        }
        updateTask(task);
        set_isEdit(false);
    }

    public void deleteTask(Task task) {
        Repository.getInstance().deleteTask(task);
        set_selectedTaskID(Consts.INVALID_STRING);
    }

    public void deleteTask(String taskId) {
        Task task = null;
        for (Task t: _tasks.getValue()) {
            if (t.get_taskId().equals(taskId)){
                task = t;
                break;
            }
        }
        Repository.getInstance().deleteTask(task);
        set_selectedTaskID(Consts.INVALID_STRING);
    }

    public void setTaskChecked(Task task, boolean isChecked) {
        for (Task t:_tasks.getValue()) {
            if(t.get_taskId().equals(task.get_taskId())){
                if (isChecked) {
                    t.set_finishDate(new Date().getTime());
                } else {
                    t.set_finishDate(0);
                }
                Repository.getInstance().updateTask(t);
                break;

            }
        }
    }

    public void setTaskChecked(Task task,boolean isChecked, boolean isEdit){
        if (!isEdit)
            setTaskChecked(task, isChecked);
    }

    public String getLoggedInUserId() {
        return Repository.getInstance().get_authUser().getValue().get_userId();
    }

    public void fetchREmoteUserTask() {
        Repository.getInstance().fetchRemoteUserTasks();
    }
    // endregion

}
