package com.example.doit.Model;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.doit.IResponseHelper;
import com.example.doit.Model.dao.UserDao;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private static final String TAG = "Repository";

    // region Members

    private static Repository instance;
    private FirebaseFirestore _remoteDb;
    private Map<String, IDataWorker> workers;
    private LiveData<List<User>> _users;
    private LiveData<User> _curUser;
    private LiveData<List<Group>> _groups;
    private final ExecutorService _executorService;
    private MutableLiveData<Boolean> _isBottomNavigationUp;
    private UserDao userDao;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> _authSuccess;
    private UserFirebaseWorker userFirebaseWorker;
    // endregion

    private Repository() {
        workers = new HashMap<>();
        workers.put(Consts.FIRE_BASE_USERS, new UserFirebaseWorker());
        _executorService = Executors.newFixedThreadPool(4);
        _isBottomNavigationUp = new MutableLiveData<>(false);
        _authUser = new MutableLiveData<User>(new User());
        userDao = LocalDB.db.userDao();
        userFirebaseWorker = (UserFirebaseWorker) createWorker(Consts.FIRE_BASE_USERS);
        userFirebaseWorker.setAuthUser(_authUser);
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

    public void login(Map<String, Object> user) {
        IResponseHelper repoHelper = new IResponseHelper() {
            @Override
            public void actionFinished(boolean actionResult) {
                if (actionResult) {
                    _authUser.setValue(userFirebaseWorker.getAuthenticatedUserDetails());
                    saveOrUpdateUser(_authUser.getValue());
                    set_authSuccess(true);
                } else {
                    set_authSuccess(false);
                    _authUser.setValue(new User());
                }
            }
        };
        userFirebaseWorker.login(user, repoHelper);
    }

    public MutableLiveData<User> get_authUser() {
        if (_authUser == null) { _authUser = new MutableLiveData<User>(); }
        return _authUser;
    }

    private void set_authSuccess(Boolean aBoolean) {
        if (_authSuccess == null) { _authSuccess = new MutableLiveData<>(); }
        _authSuccess.setValue(aBoolean);
    }

    public MutableLiveData<Boolean> get_authSuccess() {
        if (_authSuccess == null) { _authSuccess = new MutableLiveData<>(); }
        return _authSuccess;
    }

    public void saveOrUpdateUser(User user) {
        _executorService.execute(() -> userDao.insertAll(user));
    }

    public void logout() {
        set_authSuccess(false);
        userFirebaseWorker.logoutAuthUser();
    }

    public void register(String image_uri, User user) {
        user.setImgae(image_uri);
        userFirebaseWorker.create(user);
    }

    private void fetchData(){
        _executorService.execute(()-> _users = LocalDB.db.userDao().getAll());
        _executorService.execute(()-> _curUser = LocalDB.db.userDao().loadUserById("TODO"));
    }

    // region event listeners


    // endregion

}
