package com.example.untpreownedstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start_page);
    }

    public void onClickSignUp(View view) {
//        Intent intent = new Intent(this,SignUpActivity.class);
//        startActivity(intent);
    }

    public void onClickLogin(View view) {
//        Intent intent = new Intent(this,LoginActivity.class);
//        startActivity(intent);
    }
}