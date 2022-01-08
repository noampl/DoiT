package com.example.doit.model;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.model.entities.Group;
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
       newGroup.setMembersId((List<String>) groupDocument.get("Users"));
       newGroup.set_image((String) groupDocument.get("image"));
       newGroup.set_tasksId((List<String>) groupDocument.get("tasks"));
       return newGroup;
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
                }
            }
        };
    }
}
