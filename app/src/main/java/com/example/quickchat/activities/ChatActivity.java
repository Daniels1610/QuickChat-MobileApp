package com.example.quickchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.R;
import com.example.quickchat.database.DatabaseHelper;
import com.example.quickchat.database.DatabaseOperations;
import com.example.quickchat.databinding.ActivityChatBinding;
import com.example.quickchat.models.ChatMessage;
import com.firebase.ui.database.FirebaseListAdapter;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String TAG = "AGRAZ DEBUGGING";

    DatabaseOperations mDBOperations = new DatabaseOperations();
    DatabaseHelper mDBHelper = new DatabaseHelper();

    String username;
    ImageButton btn_send;
    EditText text_send;
    public FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ListView listMessages = binding.messageLV;
        btn_send = binding.sendBtn;
        text_send = binding.inputMessage;

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        Log.d(TAG, username);

        adapter = new FirebaseListAdapter<ChatMessage>(ChatActivity.this, ChatMessage.class, R.layout.message, mDBHelper.getDatabaseInstance().getReference().child("ChatGroup")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                messageUser.setText(model.getMessageUser());
                messageText.setText(model.getMessageText());
                messageTime.setText(model.getMessageTime());
            }
        };

        listMessages.setAdapter(adapter);

        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")) {
//                    sendMessage(new ChatMessage(msg, username));
                    sendMessageRDTB(new ChatMessage(msg, username));
                } else {
                    Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
    }

    private void sendMessageRDTB(ChatMessage message){
        mDBOperations.writeMessage(message.getMessageText(), message.getMessageUser());
    }

}