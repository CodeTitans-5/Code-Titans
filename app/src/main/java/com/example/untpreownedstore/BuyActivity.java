package com.example.untpreownedstore;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BuyActivity extends AppCompatActivity {
    private static final String TAG = "Buy Activity";
    String productCategory;
    RecyclerView recyclerView;
    ItemsAdapter adapter;
    TextView mInfo;
    Context mContext;
    private FirebaseAuth mAuth;

    private FirebaseUser mCurrentUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    ArrayList<Product> productArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productCategory =getIntent().getStringExtra("CATEGORY");
        getSupportActionBar().setTitle(productCategory);
        setContentView(R.layout.activity_buy);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        mInfo = findViewById(R.id.info);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection(productCategory);
        mCurrentUser = mAuth.getCurrentUser();
        readFromDb();
        recyclerMethod(productArrayList);

    }

    private void readFromDb() {
        Log.i(TAG, "readFromDb Method Invoked");
        collectionReference
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            productArrayList.clear();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Product product = doc.toObject(Product.class);
                                productArrayList.add(product);
                                //countArrayList.add(employee);
                            }
                            //notify Adapter for Updated Data
                            adapter.notifyDataSetChanged();
                            count(productArrayList);
                        } else {
                            productArrayList.clear();
                            adapter.notifyDataSetChanged();
                            count(productArrayList);
                        }
                    }
                });
    }

    private void count(ArrayList<Product> productArrayList) {
        int i = productArrayList.size();
        //        int i = employeeArrayList.size();
//        Log.i(TAG, "count thing " + i);
//        Log.i(TAG, "count Method is invoked");
        if (i == 0) {
            mInfo.setVisibility(View.VISIBLE);}
        else {
            mInfo.setVisibility(View.INVISIBLE);
        }

    }

    private void recyclerMethod(ArrayList<Product> productArrayList) {
        recyclerView = findViewById(R.id.items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        attendanceArrayList = arrayList;
        adapter = new ItemsAdapter(productArrayList,mContext);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerClick(String productId, String productCategory) {
                Intent intent = new Intent(getBaseContext(), ItemActivity.class);
                intent.putExtra("CATEGORY", productCategory);
                intent.putExtra("PRODUCT_ID", productId);
                startActivity(intent);
            }

            @Override
            public void onRecyclerEditClick(String productId, String userId) {

            }

            @Override
            public void onRecyclerDeleteClick(String productId, String userId,String productCategory) {

            }

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}