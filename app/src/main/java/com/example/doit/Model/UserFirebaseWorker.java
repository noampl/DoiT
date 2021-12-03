package com.example.doit.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.doit.IResponseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class UserFirebaseWorker implements IDataWorker{
    private static final String USERS_COLLECTION_NAME = "users";
    private static final String TAG = "User Firebase Worker";
    private final FirebaseFirestore db;
    private User _user;

    public UserFirebaseWorker() {
        db = FirebaseFirestore.getInstance();
    }

    private boolean validateValues(User user) {
        Map<String, Object> userMap = user.getUserMap();
        for(Map.Entry<String, Object> entry : userMap.entrySet()){
            if (entry.getValue() == null)
                return false;
        }
        return true;
    }

    public void create(User user, IResponseHelper responseHelper ) {
        if(!validateValues(user))
            return;
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        responseHelper.actionFinished(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        responseHelper.actionFinished(false);
                    }
                });
    }

}
