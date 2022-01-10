package com.example.doit.model;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.Task;
import com.example.doit.model.entities.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
        newTask.set_groupId(taskDocument.getId());
        newTask.set_name((String) taskDocument.get("name"));
        newTask.set_description((String) taskDocument.get("description"));
        newTask.set_createdById((String) taskDocument.get("createdByUserId"));
        newTask.set_assigneeId((String) taskDocument.get("assigneeId"));
        newTask.set_image((String) taskDocument.get("image"));
        newTask.set_createdDate(taskDocument.get("createdDate") == null ? 0 : (Long) taskDocument.get("createdDate"));
        newTask.set_finishDate(taskDocument.get("finishDate") == null ? 0 : (Long) taskDocument.get("createdDate"));
        newTask.set_targetDate(taskDocument.get("targetDate") == null ? 0 : (Long) taskDocument.get("createdDate"));
        newTask.set_value(taskDocument.get("value") == null ? 0 : (Integer) taskDocument.get("value"));
        newTask.set_image((String) taskDocument.get("image"));

        return newTask;
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
                    Objects.requireNonNull(user.getValue()).addGroupOrUpdate(newGroup);
                    Repository.getInstance().insertGroupLocal(newGroup);
                    Repository.getInstance().deleteNotExistGroupsOnFirebase(user.getValue().get_userId());
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
                    Objects.requireNonNull(Repository.getInstance().get_tasks().getValue()).add(newTask);
                    Repository.getInstance().insertTaskLocal(newTask);
                }
            }
        };
    }
}
