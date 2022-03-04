package com.example.doit.model;

import static com.example.doit.model.firebaseUtils.getGroupListener;
import static com.example.doit.model.firebaseUtils.getTaskListener;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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

import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class GroupFirebaseWorker implements IDataWorker {

    // region Members

    private static final String USERS_COLLECTION_NAME = "users";
    private static final String GROUPS_COLLECTION_NAME = "groups";
    private static final String TASKS_COLLECTION_NAME = "tasks";
    private static final String TAG = "Group Firebase Worker";
    private final FirebaseFirestore db;
    private final CollectionReference usersRef;
    private final CollectionReference groupsRef;
    private MutableLiveData<User> authUser;



    // endregion

    // region C'tor

    public GroupFirebaseWorker() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(USERS_COLLECTION_NAME);
        groupsRef = db.collection(GROUPS_COLLECTION_NAME);
    }

    // endregion

    // region Public Methods

    public void createNewGroup(Group group) {
        group.getMembersId().add(Repository.getInstance().get_authUser().getValue().get_userId());
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
                                        if (task.isSuccessful()) {
                                            Repository.getInstance().insertGroupLocal(group);
                                            User newAuth = Repository.getInstance().get_authUser().getValue();
                                            if (newAuth != null) {
                                                newAuth.addGroupOrUpdate(group);
                                                for (String userId : group.getMembersId()) {
                                                    addGroupToUser(userId, group.get_groupId());
                                                }
                                            }
                                            DocumentSnapshot docTask = task.getResult();
                                            if (docTask != null) {
                                                docTask.getReference().addSnapshotListener(getGroupListener(authUser));
                                            }
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

    public void addGroupToUser(String userId, String groupId) {
        usersRef.whereEqualTo("id", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    for (DocumentSnapshot doc : querySnapshot) {
                        List<String> groups = (List<String>) doc.get("groups");
                        if (groups == null) {
                            groups = new ArrayList<>();
                        }
                        if (!groups.contains(groupId)) {
                            groups.add(groupId);
                        }
                        doc.getReference().update("groups", groups).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Updated " + userId);
                                } else {
                                    Log.d(TAG, "Update failed " + userId);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void uploadImage(String uri, Group group) {
        if (uri == null || uri.equals("")) {
            return;
        }
        File file = new File(uri);
        String imageName = file.getName();
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("groups_images")
                .child(System.currentTimeMillis() + imageName);
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
                                if (task.isSuccessful()) {
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

    public void deleteUserFromGroup(Group group, User user) {
        System.out.println("peleg - delete remote group " + group.get_groupId());
        group.getMembersId().remove(user.get_userId());
        user.get_groupsId().remove(group.get_groupId());
        groupsRef.document(group.get_groupId()).update("membersId", group.getMembersId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, user.get_email() + " deleted from " + group.get_name());
                            usersRef.whereEqualTo("id", user.get_userId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null) {
                                            for (DocumentSnapshot doc : querySnapshot) {
                                                doc.getReference().update("groups", user.get_groupsId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            System.out.println("peleg - delete local group " + group.get_groupId());
                                                            Repository.getInstance().deleteLocalGroup(group);
                                                            Log.d(TAG, group.get_name() + " deleted from " + user.get_email());
                                                        } else {
                                                            Log.d(TAG, group.get_name() + " failed to be deleted from " + user.get_email());
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, user.get_email() + " failed to be deleted from " + group.get_name());
                        }
                    }
                });
    }



    private void addGroupToUser(User user, Group group) {
        usersRef.whereEqualTo("id", user.get_userId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshot = task.getResult();
                    if (queryDocumentSnapshot == null) {
                        Log.w(TAG, "User Id did not found");
                        return;
                    }
                    for (DocumentSnapshot doc : queryDocumentSnapshot.getDocuments()) {
                        DocumentReference documentReference = doc.getReference();
                        if (!user.get_groupsId().contains(group.get_groupId())) {
                            user.addGroupOrUpdate(group);
                        }
                        documentReference.update("groups", user.get_groupsId())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Group added to user");
                                        } else {
                                            Log.w(TAG, "Had a problem with adding group to user");
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    public void updateTask(com.example.doit.model.entities.Task task) {
        groupsRef.document(task.get_groupId()).collection(TASKS_COLLECTION_NAME).document(task.get_taskId())
                .update(task.create()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> fireBaseTask) {
                Log.d(TAG, "Was task update successfully? " + fireBaseTask.isSuccessful());
            }
        });
    }

    public void updateGroup(Group group){
        groupsRef.document(group.get_groupId()).update(group.create()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    uploadImage(group.get_image(), group);
                    Log.d(TAG, "updated group: " + group.get_groupId());
                } else {
                    Log.d(TAG, "problem with update group: " + task.toString());
                }
            }
        });
    }

    public void deleteTask(com.example.doit.model.entities.Task task) {
        groupsRef.document(task.get_groupId()).collection(TASKS_COLLECTION_NAME).document(task.get_taskId())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Repository.getInstance().deleteLocalTask(task);
                    }
                });
    }

    // endregion

}
