package com.example.doit.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.common.Consts;
import com.example.doit.interfaces.IActionBarHelper;
import com.example.doit.model.dao.UserDao;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private static final String TAG = "Repository";

    // region Members

    private static Repository instance;
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
    private final MutableLiveData<Boolean> _isBottomNavigationUp;
    private final UserDao userDao;
    private MutableLiveData<User> _authUser;
    private MutableLiveData<Boolean> _loggedIn;
    private final UserFirebaseWorker userFirebaseWorker;
    private final GroupFirebaseWorker groupFirebaseWorker;
    private MutableLiveData<Boolean> _isSynced;
    private MutableLiveData<List<User>> _selectedUsers = new MutableLiveData<>(new ArrayList<>());
    private String _taskDetailsId = "";
    /**
     * Uses for addGroup dialog
     */
    private MutableLiveData<List<User>> _newGroupUsers;
    private WeakReference<IActionBarHelper> actionBarHelper;

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
        _newGroupUsers = new MutableLiveData<>(new ArrayList<>());

    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    // endregion

    // region Properties

    public WeakReference<IActionBarHelper> getActionBarHelper() {
        return actionBarHelper;
    }

    public void setActionBarHelper(IActionBarHelper helper) {
        actionBarHelper = new WeakReference<IActionBarHelper>(helper);
    }

    public String get_taskDetailsId() {
        return _taskDetailsId;
    }

    public MutableLiveData<List<User>> get_selectedUsers() {
        return this._selectedUsers;
    }

    public void cleanCache() {
        LocalDB.db.taskDao().deleteAll();
        LocalDB.db.groupDao().deleteAll();
        LocalDB.db.userDao().deleteAll();
    }

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

    public MutableLiveData<List<com.example.doit.model.entities.Task>> get_tasks() {
        if (_tasks == null) {
            _tasks = new MutableLiveData<>(new ArrayList<>());
            _tasks.postValue(LocalDB.db.taskDao().getAll());
        }
        return _tasks;
    }

    // endregion

    // region Public Methods

    public void getAllAuthUserGroups() {
        synchronized (this) {
            Task<QuerySnapshot> a = userFirebaseWorker.getAuthenticatedUser();
        }
    }

    public void login(Map<String, String> user) {
        if (!Objects.equals(user.get(Consts.EMAIL), null) && !Objects.equals(user.get(Consts.PASSWORD), null) &&
                !Objects.equals(user.get(Consts.EMAIL) ,Consts.INVALID_STRING)
                && !Objects.equals(user.get(Consts.PASSWORD), Consts.INVALID_STRING)) {

            userFirebaseWorker.login(user, get_loggedIn());
        }
    }

    public void logout() {
        set_loggedIn(false);
        userFirebaseWorker.logoutAuthUser(get_loggedIn());
        set_isSynced(false);
        getGroups().setValue(new ArrayList<>());
        get_users().setValue(new ArrayList<>());
        _authUser.setValue(new User());
        cleanCache();
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
            userFirebaseWorker.upload_profile_image(uri.toString(), user);
        }
        if (emailChanged) {
            userFirebaseWorker.updateUserEmail(user);
        }
        saveOrUpdateUser(user);
    }

    public void deleteUserFromGroup(Group group, User user) {
        _executorService.execute(() -> {
            groupFirebaseWorker.deleteUserFromGroup(group, user);
        });
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

    public void deleteNotExistTask() {
        _executorService.execute(() -> {
            synchronized (this) {
                Log.d(TAG, "deleteNotExistTask:: starting list" + get_tasks().getValue());
                List<String> groupIds = new ArrayList<>();
                for (Group g : LocalDB.db.groupDao().getAll()) {
                    groupIds.add(g.get_groupId());
                }
                //TODO 2022-01-14 12:41:11.529 21197-21259/com.example.doit E/AndroidRuntime: FATAL EXCEPTION: pool-5-thread-3
                //    Process: com.example.doit, PID: 21197
                //    java.util.ConcurrentModificationException
                //        at java.util.ArrayList$Itr.next(ArrayList.java:860)
                //        at com.example.doit.model.Repository.lambda$deleteNotExistTask$5$com-example-doit-model-Repository(Repository.java:323)
                //        at com.example.doit.model.Repository$$ExternalSyntheticLambda0.run(Unknown Source:2)
                //        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
                //        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
                //        at java.lang.Thread.run(Thread.java:923)
                List<com.example.doit.model.entities.Task> clone = new ArrayList<>(get_tasks().getValue());
                for (com.example.doit.model.entities.Task task : get_tasks().getValue()) {
                    if (!groupIds.contains(task.get_groupId())) {
                        clone.remove(task);
                        LocalDB.db.taskDao().delete(task);
                    }
                }
                get_tasks().postValue(clone);
                Log.d(TAG, "deleteNotExistTask:: finish list" + get_tasks().getValue());
            }
        });
    }

    public void deleteNotExistGroupsOnFirebase(String userID) {
        _executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    for (Group g : LocalDB.db.groupDao().getAll()) {
                        if (!g.getMembersId().contains(userID)) {
                            LocalDB.db.groupDao().delete(g);
                        }
                    }
                    ArrayList<Group> clone = new ArrayList<Group>(Objects.requireNonNull(getGroups().getValue()));
                    for (Group group : getGroups().getValue()) {
                        if (!group.getMembersId().contains(userID)) {
                            clone.remove(group);
                            for (String taskId : group.get_tasksId()) {
                                LocalDB.db.taskDao().deleteTaskById(taskId);
                            }
                        }
                    }
                    getGroups().postValue(clone);
                }
            }
        });
    }

    // endregion

    // region Db interaction

//    public LiveData<List<Group>> getGroupsByUserId(String userId) {
//        return LocalDB.db.groupDao().getAllUserGroups(userId);
//    }

    public void deleteLocalTask(com.example.doit.model.entities.Task task) {
        _executorService.execute(() -> {
            LocalDB.db.taskDao().delete(task);
            userFirebaseWorker.deleteTask(task);
        });
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

    public void setTaskByGroupId(String groupId) {
        _executorService.execute(() -> _tasks.postValue(LocalDB.db.taskDao().getTasksByGroup(groupId)));
    }

    public CompletableFuture<List<com.example.doit.model.entities.Task>> getTasksByGroupId(String groupId) {
        return CompletableFuture.supplyAsync(()->LocalDB.db.taskDao().getTasksByGroup(groupId));
    }

    public void insertTaskLocal(com.example.doit.model.entities.Task task) {
        _executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                   LocalDB.db.taskDao().insertAll(task);
                }
            }
        });
    }

    public void insertGroupLocal(Group group) {
        _executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    LocalDB.db.groupDao().insertAll(group);
                }
                _groups.postValue(LocalDB.db.groupDao().getAll());
            }
        });
    }

    public void updateLocalGroup(Group group) {
        _executorService.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    LocalDB.db.groupDao().update(group);
                }
            }
        });
    }

    public void saveOrUpdateUser(User user) {
        _executorService.execute(() -> userDao.insertAll(user));
    }

    public void searchUsersByNameOrMail(String query) {
        _executorService.execute(() ->
                userFirebaseWorker.lookForAllUsersByEmailOrName(query, get_users()));
    }

    public List<User> getUsersByGroup(String groupId) {
        return LocalDB.db.userDao().getUsersByGroup(groupId);
    }

    public User getUserFromLocal(String userId) {
        User user = LocalDB.db.userDao().getUserById(userId);
        if (user == null) {
            userFirebaseWorker.getUser(userId);
        }
        return LocalDB.db.userDao().getUserById(userId);
    }

    public CompletableFuture<Group> getGroupById(String groupId) {
        return CompletableFuture.supplyAsync(()->LocalDB.db.groupDao().getGroup(groupId));
    }

    public void fetchTasks() {
        _executorService.execute(() -> _tasks.postValue(LocalDB.db.taskDao()
                .getTasksByAssignee(_authUser.getValue().get_userId())));
    }

    public CompletableFuture<com.example.doit.model.entities.Task> getTaskById(String tasksDetailsId) {
        return  CompletableFuture.supplyAsync(()-> LocalDB.db.taskDao().getTaskById(tasksDetailsId));
    }

    public void setUsersById(String id) {
        _executorService.execute(() -> {
            ArrayList<User> members = new ArrayList<>();
            List<String> membersId = new ArrayList<>(LocalDB.db.groupDao().getMembersId(id));
            String[] membersArray = membersId.toString().replace("[","").replace("]","").replace("\"","").split(",");
            for(String userId : membersArray){
                members.add(LocalDB.db.userDao().getUserById(userId));
            }
            if (members.size() > 0){
                _users.postValue(members);
            }
        });
    }

    public void updateTask(com.example.doit.model.entities.Task task) {
        _executorService.execute(() -> groupFirebaseWorker.updateTask(task));
    }

    public void updateLocalTask(com.example.doit.model.entities.Task task) {
        _executorService.execute(() -> {
            LocalDB.db.taskDao().update(task);
            List<com.example.doit.model.entities.Task> tmp = _tasks.getValue();
            for (com.example.doit.model.entities.Task t : _tasks.getValue()){
                if (t.get_taskId().equals(task.get_taskId())){
                    tmp.remove(t);
                    break;
                }
            }
            tmp.add(task);
            _tasks.postValue(tmp);
        });
    }

    public CompletableFuture<Integer> getUserScoreByGroup(User user, String groupId) {
        return CompletableFuture.supplyAsync(()->LocalDB.db.taskDao().getUserScoreByGroup(user.get_userId(), groupId));
    }

    public void getUsersFromLocalDb() {
        _executorService.execute(() -> _users.postValue(LocalDB.db.userDao().getAll()));
    }

    public void deleteGroup(Group group) {
        _executorService.execute(() -> groupFirebaseWorker.deleteUserFromGroup(group,
                Objects.requireNonNull(get_authUser().getValue())));
    }

    public void deleteGroupById(String groupId) {
        getGroupById(groupId).thenAccept((group -> groupFirebaseWorker.deleteUserFromGroup(group,
                Objects.requireNonNull(get_authUser().getValue()))));
    }

    public void updateGroup(Group group) {
        _executorService.execute(() -> groupFirebaseWorker.updateGroup(group));
    }

    public void deleteTask(com.example.doit.model.entities.Task task){
        _executorService.execute(() -> groupFirebaseWorker.deleteTask(task));
    }

    // endregion

}
