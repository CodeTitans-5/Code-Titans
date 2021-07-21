package com.example.untpreownedstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CategoriesActivity extends AppCompatActivity {
    private static final String TAG = "Categories Activity";
    private ImageView mAutomobile,mBooks,mClothing,mElectronics,mFootwear,mSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Categories");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_categories);
        uiMethods();
    }

    private void uiMethods() {
        mAutomobile = findViewById(R.id.automobile);
        mBooks = findViewById(R.id.books);
        mClothing = findViewById(R.id.clothing);
        mElectronics = findViewById(R.id.electronics);
        mFootwear = findViewById(R.id.footwear);
        mSports = findViewById(R.id.sports);
        mAutomobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BuyActivity.class);
                intent.putExtra("CATEGORY", "Automobile");
                startActivity(intent);
            }
        });
        mBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BuyActivity.class);
                intent.putExtra("CATEGORY", "Books");
                startActivity(intent);
            }
        });
        mClothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BuyActivity.class);
                intent.putExtra("CATEGORY", "Clothing");
                startActivity(intent);
            }
        });
        mElectronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BuyActivity.class);
                intent.putExtra("CATEGORY", "Electronics");
                startActivity(intent);
            }
        });
        mFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BuyActivity.class);
                intent.putExtra("CATEGORY", "Footwear");
                startActivity(intent);
            }
        });
        mSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BuyActivity.class);
                intent.putExtra("CATEGORY", "Sports");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}