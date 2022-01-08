package com.example.doit.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.doit.model.entities.Group;
import com.example.doit.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class GroupFirebaseWorker implements IDataWorker{

    // region Members

    private static final String USERS_COLLECTION_NAME = "users";
    private static final String GROUPS_COLLECTION_NAME = "groups";
    private static final String TAG = "User Firebase Worker";
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;
    private final CollectionReference groupsRef;
    private User _user;
    private String _registerErrorReason;
    private FirebaseAuth mAuth;
    private String _image_url;
    private DocumentReference authDocRef;
    private MutableLiveData<User> authUser;
    private MutableLiveData<String> _firebaseError;

    // endregion

    // region C'tor

    public GroupFirebaseWorker() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS_COLLECTION_NAME);
        groupsRef = db.collection(GROUPS_COLLECTION_NAME);
        mAuth = FirebaseAuth.getInstance();
        _firebaseError = new MutableLiveData<>();
    }

    // endregion

    // region Public Methods

    public void createNewGroup(Group group){
        groupsRef.add(group.create()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Repository.getInstance().getExecutorService().execute(new Runnable() {
                    @Override
                    public void run() {
                        Task<DocumentSnapshot> docTask = documentReference.get();
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        group.set_groupId(documentReference.getId());
                        uploadImage(group.get_image(), group);
                        docTask.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    Repository.getInstance().insertGroupLocal(group);
                                }
                            }
                        });
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

    public void uploadImage(String uri, Group group){
        if (uri == null){
            return;
        }
        File file = new File(uri);
        String imageName = file.getName();
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("groups_images")
                .child(System.currentTimeMillis()+imageName);
        fileRef.putFile(Uri.parse(uri)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.d(TAG, "url " + url);
                        group.set_image(url);
                        groupsRef.document(group.get_groupId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    groupsRef.document(group.get_groupId()).update(group.create());
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

    // endregion

}
