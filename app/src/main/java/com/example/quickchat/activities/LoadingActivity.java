package com.example.quickchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.bumptech.glide.Glide;
import com.example.quickchat.R;
import com.example.quickchat.databinding.ActivityLoadingBinding;

public class LoadingActivity extends AppCompatActivity {

    ActivityLoadingBinding binding;
    MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this).load(R.drawable.chat_logo2).into(binding.chatImage);

        myCountDownTimer = new MyCountDownTimer(5000, 1000);
        myCountDownTimer.start();
    }

    public class MyCountDownTimer extends CountDownTimer {
        int progress = 0;

        public MyCountDownTimer(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished){
            progress = progress + 20;
            binding.loadingProgressBar.setProgress(progress);
        }

        @Override
        public void onFinish(){
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            startActivity(intent);

        }

    }
}