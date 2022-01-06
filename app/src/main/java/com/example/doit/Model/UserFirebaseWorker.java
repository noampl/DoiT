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
  
    // endregion

    // region C'tor

    public UserFirebaseWorker() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS_COLLECTION_NAME);
        mAuth = FirebaseAuth.getInstance();
        if (authUser != null) {
            authDocRef = usersRef.document(authUser.getValue().get_id());
        }
    }

    // endregion

    // region Properties

    public String get_image_url() {
        return _image_url;
    }

    public void setAuthUser(MutableLiveData<User> authUser) {
        this.authUser = authUser;
    }

    public User insertDocumentToUser(DocumentSnapshot doc){
        User newUser = new User();
        newUser.set_id((String) doc.getId());
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
        return authUser.getValue();
    }

    public String get_registerErrorReason() {
        return _registerErrorReason;
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
                        user.setImgae(uri.toString());
                        usersRef.document(user.get_id()).update(user.create());
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error on uploading user profile image", e);
                    }
                });;
            }
        });

    }

    public void getAuthenticatedUser(IResponseHelper responseHelper) {
        if(mAuth.getCurrentUser() != null){
            authUser.setValue(new User());
            usersRef.whereEqualTo("email", mAuth.getCurrentUser().getEmail())
                    .limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot documents = task.getResult();
                        assert documents != null;
                        for (DocumentSnapshot doc: documents) {
                            if (doc.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + doc.getData());
                                authUser.setValue(insertDocumentToUser(doc));
                                authDocRef = usersRef.document(authUser.getValue().get_id());
                                authDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (error != null) {
                                            Log.w(TAG, "Failed listen to auth user", error);
                                        }
                                        if (value != null && value.exists()) {
                                            // here!
                                            User updatedUser = insertDocumentToUser(value);
                                            Repository.getInstance().saveOrUpdateUser(updatedUser);
                                            authUser.setValue(updatedUser);
                                        }
                                    }
                                });
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

    public void create(User user) {
        mAuth.createUserWithEmailAndPassword(user.get_email(), user.get_password()).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    createNewUserDoc(user);
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    _registerErrorReason = Objects.requireNonNull(task.getException()).getMessage();
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
                        user.set_id(documentReference.getId());
                        upload_image(user.get_image(), user);
                        docTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    authUser.setValue(insertDocumentToUser(Objects.requireNonNull(task.getResult())));
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

    public void login(Map<String, Object> user, IResponseHelper responseHelper) {
        Log.d(TAG, "looking for user");
        authUser.setValue(new User());
        Query findUser = usersRef;
        for (Map.Entry<String,Object> entry: user.entrySet()) {
            findUser = findUser.whereEqualTo(entry.getKey(), entry.getValue());
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

    public void logoutAuthUser() {
        authUser.setValue(new User());
        mAuth.signOut();
    }

    public void updateAuthUserDetails(User user, Uri image_uri, IResponseHelper helper, Boolean ImageHasChanged) {
        IResponseHelper help_image = new IResponseHelper() {
            @Override
            public void actionFinished(boolean actionResult) {
                user.setImgae(get_image_url());
                usersRef.whereEqualTo("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                                    String doc_id = doc.getId();
                                    mAuth.getCurrentUser().updateEmail(user.get_email().toLowerCase()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                usersRef.document(doc_id).update(user.create());
                                                getAuthenticatedUser(helper);
                                                helper.actionFinished(true);
                                            } else {
                                                helper.actionFinished(false);
                                            }
                                        }
                                    });

                                }
                            }
                        });
            }
        };
        if (ImageHasChanged) {upload_image(image_uri.toString(), user); }
        else { help_image.actionFinished(true); }
    }


    // endregion

    // region Private Methods

    // endregion
}
