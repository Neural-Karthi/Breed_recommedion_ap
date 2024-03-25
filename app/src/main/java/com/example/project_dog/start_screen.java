package com.example.project_dog;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class start_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        boolean shouldHideActionBar = true;
        if (shouldHideActionBar) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent new_screen=new Intent(start_screen.this, MainActivity.class);
                startActivity(new_screen);
                finish();
            }
        },3000);
    }
}