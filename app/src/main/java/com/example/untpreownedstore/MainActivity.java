package com.example.untpreownedstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
    }
    // The following method executes when user clicks on the Let's go button. Which will
//    take the user to StartPageActivity.class
    public void onClickLetsGo(View view) {
        try{
            Intent intent = new Intent(this,StartPageActivity.class);
            startActivity(intent);

        }
        catch (Exception e){


        }
    }
}