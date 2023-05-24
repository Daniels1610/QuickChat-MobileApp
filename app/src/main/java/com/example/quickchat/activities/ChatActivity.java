package com.example.quickchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quickchat.database.DatabaseHelper;
import com.example.quickchat.database.DatabaseOperations;
import com.example.quickchat.databinding.ActivityChatBinding;
import com.example.quickchat.models.ChatAdapter;
import com.example.quickchat.models.ChatMessage;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String TAG = "AGRAZ DEBUGGING";

    DatabaseOperations mDBOperations = new DatabaseOperations();
    DatabaseHelper mDBHelper = new DatabaseHelper();

    String username;
    ImageButton btn_send;
    EditText text_send;
    List<ChatMessage> chatMessages = new ArrayList<>();
    ChatAdapter chatAdapter = new ChatAdapter(chatMessages, username);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        listenMessage();

        btn_send = binding.sendBtn;
        text_send = binding.inputMessage;


        binding.chatRecyclerView.setAdapter(chatAdapter);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");


        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")) {
                    sendMessage(username, username, msg);
                } else {
                    Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
    }



    private void sendMessage(String sender, String receiver, String message){
        mDBOperations.addMessageToFirestore(sender, receiver, message);
    }

    private void listenMessage() {
        mDBHelper.getFirestoreInstance().collection("chats")
                .whereEqualTo("senderID", username)
                .whereEqualTo("receiverID", username)
                .addSnapshotListener(eventListener);
        mDBHelper.getFirestoreInstance().collection("chats")
                .whereEqualTo("senderID", username)
                .whereEqualTo("receiverID", username)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null) {
            return;
        }
        if(value != null) {
            int count = chatMessages.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderID = documentChange.getDocument().getString("senderID");
                    chatMessage.receiverID = documentChange.getDocument().getString("receiverID");
                    chatMessage.message = documentChange.getDocument().getString("message");
                    chatMessage.dateTime = getReadableTime(documentChange.getDocument().getDate("timestamp"));
                    chatMessage.dateObject = documentChange.getDocument().getDate("timestamp");
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if(count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() -1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
    };

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }

    public String getReadableTime(Date date) {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

}