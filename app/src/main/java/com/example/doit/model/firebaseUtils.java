package com.example.doit.model;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.common.Roles;
import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class firebaseUtils {

    private static final String TAG = "Firebase Utils";

    public static Group convertFirebaseDocumentToGroup(DocumentSnapshot groupDocument){
       Group newGroup = new Group();
       newGroup.set_groupId(groupDocument.getId());
       newGroup.set_name((String) groupDocument.get("name"));
       newGroup.set_description((String) groupDocument.get("description"));
       newGroup.setMembersId((List<String>) groupDocument.get("membersId"));
       newGroup.set_image((String) groupDocument.get("image"));
       newGroup.set_tasksId((List<String>) groupDocument.get("taskId"));
       return newGroup;
    }

    public static Task convertFirebaseDocumentToTask(DocumentSnapshot taskDocument){
        Task newTask = new Task();
        newTask.set_taskId(taskDocument.getId());
        newTask.set_groupId((String) taskDocument.get("groupId"));
        newTask.set_name((String) taskDocument.get("name"));
        newTask.set_description((String) taskDocument.get("description"));
        newTask.set_createdById((String) taskDocument.get("createdByUserId"));
        newTask.set_assigneeId((String) taskDocument.get("assigneeId"));
        newTask.set_image((String) taskDocument.get("image"));
        if (taskDocument.get("createdDate") != null){
            Timestamp createdDate = (Timestamp) taskDocument.get("createdDate");
            assert createdDate != null;
            newTask.set_createdDate(createdDate.toDate().getTime());
        }
        if (taskDocument.get("finishDate") != null){
            Timestamp finishDate = (Timestamp) taskDocument.get("finishDate");
            assert finishDate != null;
            newTask.set_finishDate(finishDate.toDate().getTime());
        }
        if (taskDocument.get("targetDate") != null){
            Timestamp targetDate = (Timestamp) taskDocument.get("targetDate");
            assert  targetDate != null;
            newTask.set_targetDate(targetDate.toDate().getTime());
        }
        newTask.set_value(taskDocument.get("value") == null ? 0 : Integer.parseInt((String) taskDocument.get("value")));
        newTask.set_image((String) taskDocument.get("image"));

        return newTask;
    }

    public static User insertDocumentToUser(DocumentSnapshot doc){
        User newUser = new User();
        newUser.set_userId((String) Objects.requireNonNull(doc.get("id")));
        newUser.setPassword((String) doc.get("password"));
        newUser.setEmail((String) doc.get("email"));
        newUser.setPhone((String) doc.get("phone"));
        newUser.setFirstName((String) doc.get("firstName"));
        newUser.setLastName((String) doc.get("lastName"));
        newUser.setPhone((String) doc.get("phone"));
        newUser.set_groupsId((List<String>) doc.get("groups"));
        newUser.setPhoneCountryCode((String) doc.get("phone_country_code"));
        if (doc.get("role") != null) {
            newUser.setRole(Roles.valueOf((String) doc.get("role")));
        }
        newUser.set_image((String) doc.get("image"));
        return newUser;
    }

    public static EventListener<DocumentSnapshot> getGroupListener(MutableLiveData<User> user){
        return new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.w(TAG, "Error with groupDocument listener");
                    return;
                }
                if(value != null && value.exists()){
                    Group newGroup = convertFirebaseDocumentToGroup(value);
                    Repository.getInstance().insertGroupLocal(newGroup);
                    String userId = Repository.getInstance().get_authUser().getValue().get_userId();
                    Repository.getInstance().deleteNotExistGroupsOnFirebase(userId);
                }
            }
        };
    }

    public static EventListener<DocumentSnapshot> getTaskListener(){
        return new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.w(TAG, "Error with taskDocument listener");
                    return;
                }
                if(value != null && value.exists()){
                    Task newTask = convertFirebaseDocumentToTask(value);
                    List<Task> list = Repository.getInstance().get_tasks().getValue();
                    list.add(newTask);
                    Repository.getInstance().get_tasks().postValue(list);
                    Repository.getInstance().insertTaskLocal(newTask);
                }
            }
        };
    }

    public static EventListener<DocumentSnapshot> getUserListener() {
        return new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.w(TAG,"Error on listening", error);
                    return;
                }
                if (value != null && value.exists()){
                    Repository.getInstance().saveOrUpdateUser(insertDocumentToUser(value));
                }
            }
        };
    }
}
