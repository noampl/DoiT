package com.example.doit.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.common.Consts;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    // region Members

    private static Repository instance;
    private FirebaseFirestore _remoteDb;
    private Map<String, IDataWorker> workers;
    private LiveData<List<User>> _users;
    private LiveData<User> _curUser;
    private LiveData<List<Group>> _groups;
    private final ExecutorService _executorService;
    private MutableLiveData<Boolean> _isBottomNavigationUp;

    // endregion

    private Repository() {
        workers = new HashMap<>();
        workers.put(Consts.FIRE_BASE_USERS, new UserFirebaseWorker());
        _executorService = Executors.newFixedThreadPool(4);
        _isBottomNavigationUp = new MutableLiveData<>(false);
    }

    public static Repository getInstance() {
        if (instance == null){
            instance = new Repository();
        }
        return instance;
    }

    public IDataWorker createWorker(String worker) {
        return workers.get(worker);
    }

    public ExecutorService getExecutorService() {
        return _executorService;
    }

    public LiveData<List<Group>> getGroups() {
        return _groups;
    }

    public MutableLiveData<Boolean> get_isBottomNavigationUp() {
        return _isBottomNavigationUp;
    }

    public void set_isBottomNavigationUp(boolean _isBottomNavigationUp) {
        this._isBottomNavigationUp.postValue(_isBottomNavigationUp);
    }

    private void fetchData(){
        _executorService.execute(()-> _users = LocalDB.db.userDao().getAll());
        _executorService.execute(()-> _curUser = LocalDB.db.userDao().loadUserById("TODO"));

    }

}
