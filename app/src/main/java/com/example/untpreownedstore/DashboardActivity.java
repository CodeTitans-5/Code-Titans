package com.example.untpreownedstore;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
    public void onClickSell(View view) {
//        Intent intent = new Intent(this, AddProductActivity.class);
//        startActivity(intent);
    }

    public void onClickBuy(View view) {
//        Intent intent = new Intent(this, CategoriesActivity.class);
//        startActivity(intent);
    }

    public void onClickEditInfo(View view) {
//        Intent intent = new Intent(this, EditInfoActivity.class);
//        startActivity(intent);
    }

    public void onClickViewItems(View view) {
//        Intent intent = new Intent(this, MyItemsActivity.class);
//        startActivity(intent);
    }

    public void onClickLogout(View view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
//
//        builder.setTitle("Attention!");
//        builder.setMessage("Are you sure want to logout");
//        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                FirebaseAuth.getInstance().signOut();
//                finish();
//                Intent intent = new Intent(DashboardActivity.this, StartPageActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                // Do nothing
//                dialog.dismiss();
//            }
//        });
//
//        AlertDialog deleteAlertDialog = builder.create();
//        deleteAlertDialog.show();
    }
}