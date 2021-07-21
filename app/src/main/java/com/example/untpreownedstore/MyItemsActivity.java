package com.example.untpreownedstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyItemsActivity extends AppCompatActivity {
    private static final String TAG = "Buy Activity";
    private String userId;
    RecyclerView recyclerView;
    MyItemsAdapter adapter;
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
        getSupportActionBar().setTitle("My items");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_my_items);
        mContext = getApplicationContext();
        mInfo = findViewById(R.id.info);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users");
        mCurrentUser = mAuth.getCurrentUser();
        userId = mCurrentUser.getUid();
        readFromDb();
        recyclerMethod(productArrayList);

    }



    private void recyclerMethod(ArrayList<Product> productArrayList) {
        recyclerView = findViewById(R.id.items);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        attendanceArrayList = arrayList;
        adapter = new MyItemsAdapter(productArrayList,mContext);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerClick(String documentId, String productCategory) {

            }

            @Override
            public void onRecyclerEditClick(String productId, String userId) {
                Intent intent = new Intent(getBaseContext(), EditItemActivity.class);
                intent.putExtra("USER_ID", userId);
                intent.putExtra("PRODUCT_ID", productId);
                startActivity(intent);
            }

            @Override
            public void onRecyclerDeleteClick(String productId, String userId,String productCategory) {
                removeItem(productId,userId,productCategory);
            }
        });
    }

    private void removeItem(String productId, String userId, String productCategory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyItemsActivity.this);

        builder.setTitle("Attention!");
        builder.setMessage("Are you sure to delete this item?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                firebaseFirestore.collection("Users").
                        document(userId).collection("My Items")
                        .document(productId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "Item successfully deleted!");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "Error deleting Item", e);
                            }
                        });
                firebaseFirestore.collection(productCategory).document(productId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i(TAG, "Item successfully deleted!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.i(TAG, "Error deleting Item", e);
                    }
                });
                Toast.makeText(MyItemsActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();

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

    private void readFromDb() {
        Log.i(TAG, "readFromDb Method Invoked");
        collectionReference.document(userId).collection("My Items")
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
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}