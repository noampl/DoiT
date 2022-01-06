package com.example.doit.Model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.Model.dao.UserDao;
import com.example.doit.Model.entities.Group;
import com.example.doit.Model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private MutableLiveData<Boolean> _loggedIn;
    private UserFirebaseWorker userFirebaseWorker;
    private MutableLiveData<String> _fireBaseError;
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

    public MutableLiveData<String> get_fireBaseError() { return userFirebaseWorker.get_firebaseError(); }

    public void login(Map<String, Object> user) {
        userFirebaseWorker.login(user, _loggedIn);
    }

    public MutableLiveData<User> get_authUser() {
        if (_authUser == null) { _authUser = new MutableLiveData<User>(); }
        return _authUser;
    }

    private void set_loggedIn(Boolean aBoolean) {
        if (_loggedIn == null) { _loggedIn = new MutableLiveData<>(); }
        _loggedIn.setValue(aBoolean);
    }

    public MutableLiveData<Boolean> get_loggedIn() {
        if (_loggedIn == null) { _loggedIn = new MutableLiveData<>(false); }
        return _loggedIn;
    }

    public void saveOrUpdateUser(User user) {
        _executorService.execute(() -> userDao.insertAll(user));
    }

    public void logout() {
        set_loggedIn(false);
        userFirebaseWorker.logoutAuthUser(get_loggedIn());
    }

    public void register(String image_uri, User user) {
        user.setImgae(image_uri);
        Task<AuthResult> createUser = userFirebaseWorker.create(user, get_loggedIn());
        createUser.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                saveOrUpdateUser(userFirebaseWorker.getAuthenticatedUserDetails());
            }
        });
    }

    public void updateAuthUserDetails(User user, Uri uri, Boolean ImageChanged, Boolean emailChanged) {
        userFirebaseWorker.updateUser(user);
        if(ImageChanged) { userFirebaseWorker.upload_image(uri.toString(), user); }
        if(emailChanged) { userFirebaseWorker.updateUserEmail(user); }
        saveOrUpdateUser(user);
    }

    private void fetchData(){
        _executorService.execute(()-> _users = LocalDB.db.userDao().getAll());
        _executorService.execute(()-> _curUser = LocalDB.db.userDao().loadUserById("TODO"));
    }

    // region event listeners


    // endregion

}
