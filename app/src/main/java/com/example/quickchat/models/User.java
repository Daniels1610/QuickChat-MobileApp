package com.example.quickchat.models;

import com.example.quickchat.support.Encoder;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    public final String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = Encoder.sha256(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
}
