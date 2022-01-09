package com.example.doit.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.doit.model.Repository;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TasksViewModel extends ViewModel {

    // region Members

    private MutableLiveData<List<Task>> _tasks;

    // endregion

    // region C'tor

    public TasksViewModel(){
        _tasks = Repository.getInstance().get_tasks();
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

    public Group getGroupByTask(Task task) {
            return Repository.getInstance().getGroupById(task.get_groupId());
    }

    public User getUserByTask(Task task) {
        return Repository.getInstance().getUserFromSql(task.get_groupId());
    }

    public void fetchTasks(){
        Repository.getInstance().fetchTasks();
    }

    // endregion

}
