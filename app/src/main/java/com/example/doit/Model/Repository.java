package com.example.doit.Model;

import com.example.doit.common.Consts;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Repository {
    private static Repository instance;
    FirebaseFirestore db;
    Map<String, IDataWorker> workers;

    private Repository() {
        workers = new HashMap<>();
        workers.put(Consts.FIRE_BASE_USERS, new UserFirebaseWorker());
    }

    public static Repository getInstance() {
        if (instance == null){
            instance = new Repository();
        }
        return instance;
    }

    public IDataWorker createWorker(String worker) {
        return workers.get(worker);
    }
}
