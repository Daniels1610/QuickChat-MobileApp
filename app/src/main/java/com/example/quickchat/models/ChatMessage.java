package com.example.quickchat.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.type.DateTime;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

@IgnoreExtraProperties
public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageTime;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd (HH:mm:ss)");
    Date currentDate = new Date();

    public ChatMessage(String messageText, String messageUser){
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = sdf.format(currentDate);
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
