package com.example.doit.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.model.dao.UserDao;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private static final String TAG = "Repository";

    // region Members

    private static Repository instance;
    private FirebaseFirestore _remoteDb;
    private Map<String, IDataWorker> workers;
    /**
     * Uses for add users / admins query in addition dialog
     */
    private MutableLiveData<List<User>> _users = new MutableLiveData<>(new ArrayList<>());

    /**
     * THe User Groups
     */
    private MutableLiveData<List<Group>> _groups = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<com.example.doit.model.entities.Task>> _tasks = new MutableLiveData<>(new ArrayList<>()); // FIXME
    private final ExecutorService _executorService;
    private MutableLiveData<Boolean> _isBottomNavigationUp;
    private UserDao userDao;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> _loggedIn;
    private UserFirebaseWorker userFirebaseWorker;
    private GroupFirebaseWorker groupFirebaseWorker;
    private MutableLiveData<String> _fireBaseError;
    private MutableLiveData<Boolean> _isSynced;

    /**
     * Uses for addGroup dialog
     */
    private MutableLiveData<List<User>> _newGroupUsers;
    /**
     * Uses for addGroup dialog
     */
    private MutableLiveData<List<User>> _newGroupAdmins;

    // endregion

    // region Singeltone


    private Repository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
        workers = new HashMap<>();
        workers.put(Consts.FIRE_BASE_USERS, new UserFirebaseWorker());
        _executorService = Executors.newFixedThreadPool(4);
        _isBottomNavigationUp = new MutableLiveData<>(false);
        _authUser = new MutableLiveData<User>(new User());
        userDao = LocalDB.db.userDao();
        userFirebaseWorker = (UserFirebaseWorker) createWorker(Consts.FIRE_BASE_USERS);
        groupFirebaseWorker = new GroupFirebaseWorker();
        userFirebaseWorker.setAuthUser(_authUser);
        _newGroupAdmins = new MutableLiveData<>(new ArrayList<>());
        _newGroupUsers = new MutableLiveData<>(new ArrayList<>());

        initFake();
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    // endregion

    // region Properties


    public MutableLiveData<Boolean> get_isSynced() {
        if (_isSynced == null) {
            _isSynced = new MutableLiveData<>(false);
        }
        return _isSynced;
    }

    public void set_isSynced(Boolean isSynced) {
        if (_isSynced == null) {
            _isSynced = new MutableLiveData<>();
        }
        this._isSynced.postValue(isSynced);
    }

    public IDataWorker createWorker(String worker) {
        return workers.get(worker);
    }

    public ExecutorService getExecutorService() {
        return _executorService;
    }

    public MutableLiveData<List<Group>> getGroups() {
        if (_groups == null) {
            _groups = new MutableLiveData<>(new ArrayList<>());
        }
        return _groups;
    }

    public LiveData<List<Group>> getGroupsByUserId(String userId) {
        return LocalDB.db.groupDao().getAllUserGroups(userId);
    }

    public MutableLiveData<List<User>> get_users() {
        return _users;
    }

    public MutableLiveData<Boolean> get_isBottomNavigationUp() {
        return _isBottomNavigationUp;
    }

    public MutableLiveData<String> get_remoteError() {
        return userFirebaseWorker.get_firebaseError();
    }

    public MutableLiveData<User> get_authUser() {
        if (_authUser == null) {
            _authUser = new MutableLiveData<User>();
        }
        return _authUser;
    }

    private void set_loggedIn(Boolean aBoolean) {
        if (_loggedIn == null) {
            _loggedIn = new MutableLiveData<>();
        }
        _loggedIn.setValue(aBoolean);
    }

    public MutableLiveData<Boolean> get_loggedIn() {
        if (_loggedIn == null) {
            _loggedIn = new MutableLiveData<>(false);
        }
        return _loggedIn;
    }

    public MutableLiveData<List<User>> get_newGroupUsers() {
        if (_newGroupUsers == null) {
            _newGroupUsers = new MutableLiveData<>();
        }
        return _newGroupUsers;
    }

    public MutableLiveData<List<User>> get_newGroupAdmins() {
        return _newGroupAdmins;
    }

    public MutableLiveData<List<com.example.doit.model.entities.Task>> get_tasks() {
        return _tasks;
    }

    // endregion

    // region Public Methods

    public void getAllAuthUserGroups() {
        synchronized (this) {
            //saveOrUpdateUser(userFirebaseWorker.getAuthenticatedUserDetails());
            Log.d("FRISHMAN", "auth getAllauthUser");
            Task<QuerySnapshot> a = userFirebaseWorker.getAuthenticatedUser();
//                Task<QuerySnapshot> b = a.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            Log.d("FRISHMAN", "Repo oncomplete auth getAllauthUser");
//                            userFirebaseWorker.getAllAuthUserGroupAndTasks();
//                        }
//                    }
//                });
        }
    }

    public void login(Map<String, String> user) {
        userFirebaseWorker.login(user, get_loggedIn());
    }

    public void saveOrUpdateUser(User user) {
        _executorService.execute(() -> userDao.insertAll(user));
    }

    public void logout() {
        set_loggedIn(false);
        userFirebaseWorker.logoutAuthUser(get_loggedIn());
        set_isSynced(false);
        getGroups().setValue(new ArrayList<>());
        get_users().setValue(new ArrayList<>());
    }

    public void register(String image_uri, User user) {
        user.set_image(image_uri);
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
        if (ImageChanged) {
            userFirebaseWorker.upload_image(uri.toString(), user);
        }
        if (emailChanged) {
            userFirebaseWorker.updateUserEmail(user);
        }
        saveOrUpdateUser(user);
    }

    public void insertGroup(Group group) {
        _executorService.execute(new Runnable() {
            @Override
            public void run() {
                groupFirebaseWorker.createNewGroup(group);
                Log.d("PELEG", "update groups");
            }
        });
    }

    public List<com.example.doit.model.entities.Task> getTasksByGroupId(String groupId) {
        return LocalDB.db.taskDao().getTasksByGroup(groupId);
        //TODO: check if should do it async.
    }

    public void deleteTask(com.example.doit.model.entities.Task task) {
        _executorService.execute(() -> userFirebaseWorker.deleteTask(task));
    }

    public void createTask(com.example.doit.model.entities.Task task) {
        _executorService.execute(() -> userFirebaseWorker.createTask(task));
    }

    public int getValueSumOfTasksInGroup(Group group) {
        /*
          receive: group
          return: sum of all tasks value that users have done
         */
        return LocalDB.db.groupDao().getSumTasksUsersValuesFromGroup(group.getMembersId());
    }

    public void insertTaskLocal(com.example.doit.model.entities.Task task) {
        _executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    LocalDB.db.taskDao().insertAll(task);
                }
                _tasks.postValue(LocalDB.db.taskDao().getAll());
            }
        });
    }


    public void insertGroupLocal(Group group) {
        _executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    Log.d("FRISHMAN", "insering");
                    LocalDB.db.groupDao().insertAll(group);
                }
                Log.d("FRISHMAN", LocalDB.db.groupDao().getAll().toString());
                for (Group g : LocalDB.db.groupDao().getAll()) {
                    Log.d("FRISHMAN", g.create().toString());
                }
                _groups.postValue(LocalDB.db.groupDao().getAll());
            }
        });
    }


    public void searchUsersByNameOrMail(String query) {
        _executorService.execute(() ->
                userFirebaseWorker.lookForAllUsersByEmailOrName(query, get_users()));
    }

    public void deleteNotExistTask() {
        _executorService.execute(() -> {
            synchronized (this) {
                List<String> groupsId = new ArrayList<>();
                if(getGroups().getValue() == null){
                    return;
                }
                for (Group g : getGroups().getValue()) {
                    groupsId.add(g.get_groupId());
                }
                LocalDB.db.taskDao().deleteTaskWhichItsGroupNotExist(groupsId);
            }
        });
    }

    public void deleteNotExistGroupsOnFirebase(String userID) {
        _executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    for(Group g : LocalDB.db.groupDao().getAll()){
                        Log.d("FRISHMAN", "members" + g.getMembersId());
                        if(!g.getMembersId().contains(userID)){
                            LocalDB.db.groupDao().delete(g);
                        }
                    }
                    ArrayList<Group> clone = new ArrayList<Group>(Objects.requireNonNull(getGroups().getValue()));
                    for (Group group : getGroups().getValue()) {
                        Log.d("FRISHMAN", "members " + group.getMembersId());
                        if (!group.getMembersId().contains(userID)) {
                            clone.remove(group);
                            for (String taskId : group.get_tasksId()) {
                                LocalDB.db.taskDao().deleteTaskById(taskId);
                            }
                        }
                    }
                    getGroups().postValue(clone);
                    deleteNotExistTask();
                }
            }
        });
    }


    public User getUserFromSql(String userId) {
        return LocalDB.db.userDao().getUserById(userId);
    }

    public Group getGroupById(String groupId) {
        return LocalDB.db.groupDao().getGroup(groupId);
    }

    public void fetchTasks() {
        _executorService.execute(() -> _tasks.postValue(LocalDB.db.taskDao()
                .getTasksByAssignee(_authUser.getValue().get_userId())));
    }

    // endregion

    // region event listeners

    private void initFake() {
//        _users.getValue().add(new User("123456","someMail@com","test","pp","password","","0526727960","+972", Roles.CLIENT,
//                null));
//        _executorService.execute(()->LocalDB.db.taskDao().insertAll(new com.example.doit.model.entities.Task("2","3","Test", "this is peleg test", new Date().getTime(),new Date().getTime(),
//                "QqsLagcb5RPK0EGI5OdnFsLbz1v1","QqsLagcb5RPK0EGI5OdnFsLbz1v1",5,"")));

    }


    // endregion

}
