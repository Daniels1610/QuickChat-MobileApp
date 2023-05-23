package com.example.quickchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.quickchat.database.DatabaseOperations;
import com.example.quickchat.databinding.ActivityMainBinding;
import com.example.quickchat.models.User;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    DatabaseOperations mDBOperations = new DatabaseOperations();

    DatabaseReference msgRef;
    String TAG = "AGRAZ DEBUGGING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        msgRef = writeTest();
//        readTest(msgRef);
//        writeNewUserTest();
//        readUserTest("JTR");
//        insertUserFirebase();
//        readAllUsersFirebase();
        addDataFirebase();


    }

    public DatabaseReference writeTest() {
        msgRef = mDBOperations.writeDatabase("This is a tesLog from WriteTest Main!");
        Log.d(TAG, "Done Writting on DB!");
        return msgRef;
    }

    public void readTest(DatabaseReference msgRef) {
        mDBOperations.readDatabase(msgRef);
    }

    public void writeNewUserTest() {
        mDBOperations.writeNewUser("JTR", "juanTorres", "amazing@gmail.com", "astonish432");
        Log.d(TAG, "Done Inserting User");
    }

    public void readUserTest (String UserID) {
        mDBOperations.readUser(UserID);
    }

    public void insertUserFirebase() {
        mDBOperations.insertUserFirestore(new User("Truffle", "truffle@email.com", "truffle432"));
    }

    public void readAllUsersFirebase() {
        mDBOperations.readFirestore();
    }

    public void addDataFirebase(){
        mDBOperations.addDataToFirestore(new User("cerati", "cerati@gmail.com", "cajanegra23"));
    }
}