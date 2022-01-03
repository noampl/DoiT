package com.example.doit.Model;

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
    private MutableLiveData<List<User>> _users;
    private MutableLiveData<User> _curUser;
    private MutableLiveData<List<Group>> _groups;
    private final ExecutorService _executorService;

    // endregion

    private Repository() {
        workers = new HashMap<>();
        workers.put(Consts.FIRE_BASE_USERS, new UserFirebaseWorker());
        _executorService = Executors.newFixedThreadPool(4);
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

    public MutableLiveData<List<User>> getUsers() {
        return _users;
    }

    public MutableLiveData<List<Group>> getGroups() {
        return _groups;
    }

//    private void fetchData(){
//        _executorService.execute(()-> _users = LocalDB.db.userDao().getAll());
//        _executorService.execute(()-> _groups = LocalDB.db.groupDao().getAll(_curUser.getValue()));
//
//    }
}
