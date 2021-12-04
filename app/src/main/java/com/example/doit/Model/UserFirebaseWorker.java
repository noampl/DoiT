package com.example.doit.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.doit.IResponseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserFirebaseWorker implements IDataWorker{
    // region Members
    private static final String USERS_COLLECTION_NAME = "users";
    private static final String TAG = "User Firebase Worker";
    private static final String USERS_FIREBASE_MAP = "userMap.";
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;
    private User _user;
    // endregion

    public UserFirebaseWorker() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS_COLLECTION_NAME);
    }

    private boolean validateCreateValues(User user) {
        Map<String, Object> userMap = user.getUserMap();
        for(Map.Entry<String, Object> entry : userMap.entrySet()){
            if (entry.getValue() == null)
                return false;
        }
        return true;
    }

    public void create(User user, IResponseHelper responseHelper ) {
        if(!validateCreateValues(user))
            return;
        usersRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

    public void find(Map<String, Object> user, IResponseHelper responseHelper, int limit) {
        Log.d(TAG, "looking for user");
        Query findUser = usersRef;
        for (Map.Entry<String,Object> entry: user.entrySet()) {
            findUser = findUser.whereEqualTo(USERS_FIREBASE_MAP+entry.getKey(), entry.getValue());
        }
        findUser.limit(limit).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = Objects.requireNonNull(task.getResult()).getDocuments();
                        if (!documents.isEmpty()) {
                            Log.d(TAG, "find " + limit + " users");
                            for (DocumentSnapshot document: documents) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            responseHelper.actionFinished(true);
                            }
                        } else {
                            Log.d(TAG, "User is not exist");
                            responseHelper.actionFinished(false);
                        }
                    } else {
                        Log.d(TAG, "Failed to query users");
                    }
                }
        });
    }
}
