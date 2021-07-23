package com.example.untpreownedstore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Home");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_dashboard);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    //    On clicking sell on dashboard user will be sent to AddProductActivity.class
    public void onClickSell(View view) {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }
    //On clicking buy on dashboard user will be sent to CategoriesActivity.class
    public void onClickBuy(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }
    //On clicking edit info on dashboard user will be sent to EditInfoActivity.class
    public void onClickEditInfo(View view) {
        Intent intent = new Intent(this, EditInfoActivity.class);
        startActivity(intent);
    }
    //On clicking view my items on dashboard user will be sent to MyItemsActivity.class
    public void onClickViewItems(View view) {
        Intent intent = new Intent(this, MyItemsActivity.class);
        startActivity(intent);
    }
    //    On click logout user will see a prompt to confirm yes or no for logout
    public void onClickLogout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

        builder.setTitle("Attention!");
        builder.setMessage("Are you sure want to logout");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(DashboardActivity.this, StartPageActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog deleteAlertDialog = builder.create();
        deleteAlertDialog.show();
    }
}