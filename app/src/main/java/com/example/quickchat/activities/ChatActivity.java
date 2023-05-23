package com.example.quickchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.quickchat.database.DatabaseOperations;
import com.example.quickchat.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String TAG = "AGRAZ DEBUGGING";

    DatabaseOperations mDBOperations = new DatabaseOperations();

    String username;
    ImageButton btn_send;
    EditText text_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn_send = binding.sendBtn;
        text_send = binding.inputMessage;

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
}