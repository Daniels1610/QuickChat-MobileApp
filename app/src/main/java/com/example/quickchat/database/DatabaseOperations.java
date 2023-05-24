package com.example.quickchat.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quickchat.models.ChatMessage;
import com.example.quickchat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DatabaseOperations {

    String TAG = "AGRAZ DEBUGGING";

    DatabaseHelper mDBHelper = new DatabaseHelper();

    FirebaseDatabase mDBInstance = mDBHelper.getDatabaseInstance();
    DatabaseReference mDBReference = mDBHelper.getDatabaseReference(mDBInstance);
    FirebaseFirestore mDBFirestore = mDBHelper.getFirestoreInstance();


    public void writeMessage(String messageText, String messageUser) {
        ChatMessage message = new ChatMessage(messageText, messageUser);
        mDBInstance.getReference("ChatGroup")
                .push()
                .setValue(message);
    }

    public void readUser(String userId) {
        mDBReference.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()){
                    Log.e("Firebase", "Error Getting Data", task.getException());
                }
                else {
                    Log.d("Firebase Value of User " + userId, String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    // FIRESTORE CRUD OPERATIONS
    public void addDataToFirestore(User user) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("password", user.getPassword());
        mDBFirestore.collection("users")
                .document(user.getUsername()).set(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Data Inserted");
                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "Error:" + exception);
                });

    }

    public void addMessageToFirestore(ChatMessage message) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("sender", message.getMessageUser());
        data.put("message", message.getMessageText());
        data.put("time", message.getMessageTime());

        mDBFirestore.collection("chats")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Message Inserted");
                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "Error:" + exception);
                });

    }

    public Object[] getUserDocument(String username, FirestoreCallback callback) {
        DocumentReference userDoc = mDBFirestore.collection("users").document(username);
        final Object[] userEmail = new Object[1];

        userDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userEmail[0] = document.get("email");
                    Log.d(TAG, "User Document: " + document.get("email"));
                    callback.onCallback(userEmail);
                } else {
                    Log.d(TAG, "No such document");
                    callback.onCallback(null);
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
                callback.onCallback(null);
            }
        });
        return userEmail;
    }
}
