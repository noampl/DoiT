package com.example.doit.Model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.IResponseHelper;
import com.example.doit.common.Roles;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserFirebaseWorker implements IDataWorker{

    // region Members

    private static final String USERS_COLLECTION_NAME = "users";
    private static final String TAG = "User Firebase Worker";
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;
    private User _user;
    private String _registerErrorReason;
    private FirebaseAuth mAuth;
    private String _image_url;
    private DocumentReference authDocRef;
    private MutableLiveData<User> authUser;
    private MutableLiveData<String> _firebaseError;
  
    // endregion

    // region C'tor

    public UserFirebaseWorker() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS_COLLECTION_NAME);
        mAuth = FirebaseAuth.getInstance();
        _firebaseError = new MutableLiveData<>();
    }

    // endregion

    // region Properties

    public void set_authDocRef() {
        Task<QuerySnapshot> uAuthDoc = usersRef.whereEqualTo("id", mAuth.getCurrentUser()
                .getUid()).limit(1).get();
        uAuthDoc.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots){
                    authDocRef = usersRef.document(doc.getId());
                }
                authDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Log.w(TAG, "Listen to auth user failsd.", error);
                        }
                        if(value != null && value.exists()){
                            User newUser = insertDocumentToUser(value);
                            newUser.setEmail(mAuth.getCurrentUser().getEmail());
                            authUser.postValue(newUser);
                        }
                    }
                });
            }
        });

    }

    public String get_image_url() {
        return _image_url;
    }

    public void setAuthUser(MutableLiveData<User> authUser) {
        this.authUser = authUser;
    }

    public User insertDocumentToUser(DocumentSnapshot doc){
        User newUser = new User();
        newUser.set_id((String) Objects.requireNonNull(doc.get("id")));
        newUser.setPassword((String) doc.get("password"));
        newUser.setEmail((String) doc.get("email"));
        newUser.setPhone((String) doc.get("phone"));
        newUser.setFirstName((String) doc.get("firstName"));
        newUser.setLastName((String) doc.get("lastName"));
        newUser.setPhone((String) doc.get("phone"));
        newUser.setPhoneCountryCode((String) doc.get("phone_country_code"));
        if (doc.get("role") != null) {
            newUser.setRole(Roles.valueOf((String) doc.get("role")));
        }
        newUser.setImgae((String) doc.get("image"));
        return newUser;
    }

    public User getAuthenticatedUserDetails() {
        Objects.requireNonNull(authUser.getValue())
                .setEmail(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        authUser.getValue().set_id(mAuth.getCurrentUser().getUid());
        return authUser.getValue();
    }

    public String get_registerErrorReason() {
        return _registerErrorReason;
    }

    public MutableLiveData<String> get_firebaseError() {
        return _firebaseError;
    }

    // endregion

    // region Public Methods

    public void upload_image(String uri, User user){
        if (uri == null){
            return;
        }
        File file = new File(uri);
        String imageName = file.getName();
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("profile_images")
                .child(System.currentTimeMillis()+imageName);
        fileRef.putFile(Uri.parse(uri)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.d(TAG, "url " + url);
                        user.setPassword(null);
                        user.setImgae(url);
                        usersRef.whereEqualTo("id", user.get_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        usersRef.document(document.getId()).update(user.create());
                                    }
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error on uploading user profile image", e);
                    }
                });
            }
        });
    }

    public void getAuthenticatedUser() {
        if(mAuth.getCurrentUser() != null){
            authUser.postValue(new User());
            usersRef.whereEqualTo("id", mAuth.getCurrentUser().getUid())
                    .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(DocumentSnapshot doc : task.getResult()){
                                    Log.d(TAG, insertDocumentToUser(doc).toString());
                                    User newUser = insertDocumentToUser(doc);
                                    newUser.setEmail(mAuth.getCurrentUser().getEmail());
                                    newUser.set_id(mAuth.getCurrentUser().getUid());
                                    authUser.postValue(newUser);
                                    set_authDocRef();
                                }
                            }
                        }
                    });
            }

    }


    public void deleteUser(DocumentReference documentReference){
        usersRef.document(documentReference.getId()).delete()
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

    public Task<AuthResult> create(User user, MutableLiveData<Boolean> logedIn) {
        return mAuth.createUserWithEmailAndPassword(user.get_email(), user.get_password()).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    mAuth.signInWithEmailAndPassword(user.get_email(), user.get_password());
                                    getAuthenticatedUser();
                                    user.set_id(Objects.requireNonNull(mAuth.getCurrentUser().getUid()));
                                    createNewUserDoc(user);
                                    logedIn.postValue(true);
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    _registerErrorReason = Objects.requireNonNull(task.getException()).getMessage();
                                    logedIn.postValue(false);
                                }
                            }
                        }
        );
    }

    private void createNewUserDoc(User user){
        user.setPassword(null);
        usersRef.add(user.create()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Task<DocumentSnapshot> docTask = documentReference.get();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        upload_image(user.get_image(), user);
                        docTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    User newUser = insertDocumentToUser(Objects.requireNonNull(task.getResult()));
                                    newUser.setEmail(mAuth.getCurrentUser().getEmail());
                                    authUser.postValue(newUser);
                                    set_authDocRef();
                                }
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void updateUser(User user){
        usersRef.whereEqualTo("id", user.get_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usersRef.document(document.getId()).update(user.create());
                            }
                            getAuthenticatedUser();
                        }
                    }
                });

    }

    public void updateUserEmail(User user) {
        mAuth.getCurrentUser().updateEmail(user.get_email()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Changed user email");
                    getAuthenticatedUser();
                } else {
                    Log.w(TAG,"Failed to change password", task.getException());
                    getAuthenticatedUser();
                    _firebaseError.postValue("Failed to change email");
                }
            }
        });
    }

    public void updateUserPassword(User user) {
        mAuth.getCurrentUser().updatePassword(user.get_email()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,"Failed to change password", e);
                _firebaseError.postValue("Failed to change password");
            }
        });
    }

    public void login(Map<String, Object> user, MutableLiveData<Boolean> loggedIn) {
        Log.d(TAG, "looking for user");
        authUser.postValue(new User());
        String email = (String) user.get("email");
        String password = (String) user.get("password");
        assert email != null && password != null;
        mAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            getAuthenticatedUser();
                            authUser.getValue().setEmail(mAuth.getCurrentUser().getEmail());
                            loggedIn.postValue(true);
                            set_authDocRef();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "signInWithEmail:failure", e);
                            loggedIn.postValue(false);
            }
        });
    }

    public void logoutAuthUser(MutableLiveData<Boolean> loggedIn) {
        authUser.postValue(new User());
        mAuth.signOut();
        loggedIn.postValue(false);
    }

    public void updateAuthUserDetails(User user, Uri image_uri, Boolean ImageHasChanged) {
        if (ImageHasChanged) {upload_image(image_uri.toString(), user); }
        user.create().remove("image");
        mAuth.getCurrentUser().updateEmail(user.get_email().toLowerCase()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    usersRef.whereEqualTo("id", mAuth.getCurrentUser().getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        usersRef.document(document.getId()).update(user.create());
                                    }
                                }
                            });
                       }
                    }
                });
            }


    // endregion

    // region Private Methods

    // endregion
}
