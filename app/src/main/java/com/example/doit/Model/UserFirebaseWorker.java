package com.example.doit.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.doit.IResponseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
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
    private FirebaseAuth mAuth;
    private User _authUser;
    // endregion

    public UserFirebaseWorker() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS_COLLECTION_NAME);
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean validateCreateValues(User user) {
        Map<String, Object> userMap = user.getUserMap();
        for(Map.Entry<String, Object> entry : userMap.entrySet()){
            if (entry.getValue() == null)
                return false;
        }
        return true;
    }

    private User insertDocumentToUser(DocumentSnapshot doc){
        User newUser = new User();
        newUser.setPassword((String) doc.get(USERS_FIREBASE_MAP+"password"));
        newUser.setEmail((String) doc.get(USERS_FIREBASE_MAP+"email"));
        newUser.setPhone((String) doc.get(USERS_FIREBASE_MAP+"phone"));
        newUser.setFirstName((String) doc.get(USERS_FIREBASE_MAP+"first_name"));
        newUser.setLastName((String) doc.get(USERS_FIREBASE_MAP+"last_name"));
        newUser.setPhone((String) doc.get(USERS_FIREBASE_MAP+"phone"));
        newUser.setPhoneCountryCode((String) doc.get(USERS_FIREBASE_MAP+"phone_country_code"));
        newUser.setRole(Roles.ROLES.valueOf((String) doc.get(USERS_FIREBASE_MAP+"role")));
        //todo: add setImage
        return newUser;
    }

    public User getAuthenticatedUserDetails() {
        if (_authUser != null){ return _authUser; }
        return null;
    }

    public void getAuthenticatedUser(IResponseHelper responseHelper) {
        if(mAuth.getCurrentUser() != null){
            Query userQuery = usersRef;
            userQuery.whereEqualTo(USERS_FIREBASE_MAP+"email", mAuth.getCurrentUser().getEmail());
            userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot documents = task.getResult();
                        assert documents != null;
                        for (DocumentSnapshot doc: documents) {
                            if (doc.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
                                _authUser =  insertDocumentToUser(doc);
                                responseHelper.actionFinished(true);
                            } else {
                                Log.d(TAG, "No such document");
                                responseHelper.actionFinished(false);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        responseHelper.actionFinished(false);
                    }
                }
            });

        }
    }


    public void deleteUser(DocumentReference documentReference){
        usersRef.document(documentReference.getPath()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "user successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting user", e);
                    }
                });
    }

    public void create(User user, IResponseHelper responseHelper ) {
        if(!validateCreateValues(user))
            return;
        usersRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        mAuth.createUserWithEmailAndPassword(_user.getEmail(), _user.get_password())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            getAuthenticatedUser(responseHelper);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            responseHelper.actionFinished(false);
                                            deleteUser(documentReference);
                                        }
                                    }
                                });
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

    public void login(Map<String, Object> user, IResponseHelper responseHelper) {
        Log.d(TAG, "looking for user");
        Query findUser = usersRef;
        for (Map.Entry<String,Object> entry: user.entrySet()) {
            findUser = findUser.whereEqualTo(USERS_FIREBASE_MAP+entry.getKey(), entry.getValue());
        }
        findUser.limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = Objects.requireNonNull(task.getResult()).getDocuments();
                        if (!documents.isEmpty()) {
                            String email = (String) user.get("email");
                            String password = (String) user.get("password");
                            assert email != null && password != null;
                            mAuth.signInWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "signInWithEmail:success");
                                                getAuthenticatedUser(responseHelper);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                responseHelper.actionFinished(false);
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "User is not exist");
                            responseHelper.actionFinished(false);
                        }
                    } else {
                        Log.d(TAG, "Failed to query users");
                        responseHelper.actionFinished(false);
                    }
                }
        });
    }
}
