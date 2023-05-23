package com.example.quickchat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.quickchat.database.DatabaseOperations;
import com.example.quickchat.databinding.ActivitySignUpBinding;
import com.example.quickchat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;

    private final DatabaseOperations mDBO = new DatabaseOperations();

    ProgressBar progressBar;

    private static final String TAG = "AGRAZ DEBUGGING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        binding.loginNow.setPaintFlags(binding.loginNow.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        progressBar = binding.progressBarCharge;

        binding.loginNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, username, password;
                email = String.valueOf(binding.emailET.getText());
                username = String.valueOf(binding.userET.getText());
                password = String.valueOf(binding.passwordET.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(SignUpActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#7C57D3")));

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Account Created");
                                    Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                                    // After Creating an Account it sends you to LoginActivitiy
//                                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//                                    startActivity(intent);
//                                    FirebaseUser user = mAuth.getCurrentUser();

                                } else {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                mDBO.addDataToFirestore(new User(username, email, password));


            }
        });

    }
}