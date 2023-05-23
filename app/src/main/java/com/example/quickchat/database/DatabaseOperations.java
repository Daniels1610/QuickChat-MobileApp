package com.example.quickchat.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.quickchat.activities.MainActivity;
import com.example.quickchat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DatabaseOperations {

    String TAG = "AGRAZ DEBUGGING";

    DatabaseHelper mDBHelper = new DatabaseHelper();

    FirebaseDatabase mDBInstance = mDBHelper.getDatabaseInstance();
    DatabaseReference mDBReference = mDBHelper.getDatabaseReference(mDBInstance);
    FirebaseFirestore mDBFirestore = mDBHelper.getFirestoreInstance();


    // RTDB CRUD OPERATIONS
    public DatabaseReference writeDatabase(String value) {
        FirebaseDatabase database = mDBInstance;
        DatabaseReference myRef = database.getReference("testLogs");

        myRef.setValue(value);
        return myRef;
    }

    public void readDatabase(DatabaseReference myRef) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void writeNewUser(String userId, String name, String email, String password) {
        User user = new User(name, email, password);

        mDBReference.child("users").child(userId).setValue(user);
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
    public User insertUserFirestore(User user){
        mDBFirestore.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                   @Override
                   public void onSuccess(DocumentReference documentReference){
                       Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                   }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        return user;
    }

    public void addDataToFirestore(User user) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("password", user.getPassword());

        mDBFirestore.collection("users")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Data Inserted");
                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "Error:" + exception);
                });

    }

    public void addMessageToFirestore(String sender, String receiver, String message) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("sender", sender);
        data.put("receiver", receiver);
        data.put("message", message);
        data.put("timestamp", ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());

        mDBFirestore.collection("chats")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Message Inserted");
                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "Error:" + exception);
                });

    }


    public void readFirestore(){
        mDBFirestore.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                });
    }
}
