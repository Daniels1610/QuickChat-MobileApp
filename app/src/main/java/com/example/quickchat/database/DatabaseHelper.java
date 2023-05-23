package com.example.quickchat.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseHelper {

    public FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance();
    }

    public DatabaseReference getDatabaseReference(FirebaseDatabase DBInstance) {
        return DBInstance.getReference();
    }

    public FirebaseFirestore getFirestoreInstance(){
        return FirebaseFirestore.getInstance();
    }
}
