package com.example.untpreownedstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditItemActivity extends AppCompatActivity {
    private static final String TAG = "Item Activity";
    EditText mProductName, mProductDescription, mProductPrice;
    ImageView mItemImage;
    String productName,productDescription,productPrice,userId,productId,productCategory;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private StorageReference mStorageReference;
    Product product = new Product();
    private Uri mProductImageUri;
    private FirebaseStorage firebaseStorage;
    private StorageTask mUploadTask;
    String downloadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Edit item details");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_item);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = firebaseStorage.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users");
        userId = getIntent().getStringExtra("USER_ID");
        productId =getIntent().getStringExtra("PRODUCT_ID");
        uiMethods();
        readDataFromDB();
    }
    //The items data is read from the database and assigned to the corresponding fields.
    private void readDataFromDB() {
        firebaseFirestore.collection("Users").document(userId)
                .collection("My Items").document(productId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshot, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null) {
                            Product product = new Product();
                            product = documentSnapshot.
                                    toObject(Product.class);
                            try {
                                mProductName.setText(product.getProductName());
                            } catch (NullPointerException e1) {
                                Log.i(TAG, String.valueOf(e1));
                            }
                            productCategory = product.getProductCategory();
                            try {
                                mProductDescription.setText(product.getProductDescription());
                            } catch (NullPointerException e2) {
                                Log.i(TAG, "onEvent: " + e2.getMessage());
                            }
                            try {
                                mProductPrice.setText(product.getProductPrice());
                            } catch (NullPointerException e2) {
                                Log.i(TAG, "onEvent: " + e2.getMessage());
                            }
                            if(product.getProductImage() != null) {
                                mItemImage.setVisibility(View.VISIBLE);
                                Picasso.with(getApplicationContext()).load(product.getProductImage()).into(mItemImage);
                            }
                        }
                    }
                });
    }
    // the following method Initializes fields in the UI
    private void uiMethods() {
        mProductName = findViewById(R.id.productName1);
        mProductDescription = findViewById(R.id.productDescription1);
        mProductPrice = findViewById(R.id.productPrice1);
        mItemImage = findViewById(R.id.product_image1);
        firebaseFirestore = FirebaseFirestore.getInstance();
//        collectionReference = firebaseFirestore.collection("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        Log.i(TAG, "ui initialization method Completed.");
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    //New data is read from the fields and sent  to the database
    public void onClickUpdateProductDetails(View view) {
        productName = mProductName.getText().toString();
        productDescription = mProductDescription.getText().toString();
        productPrice = mProductPrice.getText().toString();
        if ((TextUtils.isEmpty(productName)) && (TextUtils.isEmpty(productDescription)) &&
                (TextUtils.isEmpty(productPrice))) {
            // Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
            mProductName.setError("Product Name should not be empty");
            mProductDescription.setError("Product description should not be empty");
            mProductPrice.setError("Product price should not be empty");
        }else{
            if (TextUtils.isEmpty(productName)) {
                mProductName.setError("Product Name should not be empty");
                //Toast.makeText(this, "User Name should not be empty", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(productDescription)) {
                mProductDescription.setError("Email should not be empty");
                //Toast.makeText(this, "Email should not be empty", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(productPrice)) {
                mProductPrice.setError("Product price should not be empty");
                //Toast.makeText(this, "Email should not be empty", Toast.LENGTH_LONG).show();

            }
            else {
                saveData();
            }
        }

    }

    private void saveData() {
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Toast.makeText(EditItemActivity.this,
                    "Upload in progress", Toast.LENGTH_SHORT).show();

        }else{
            Log.i(TAG, "Add product object is created");

            firebaseFirestore.collection("Users").document(userId)
                    .collection("My Items").document(productId).
                    update("productDescription",productDescription,"productName",productName,
                            "productPrice",productPrice );
            firebaseFirestore.collection(productCategory).document(productId)
                    .update("productDescription",productDescription,"productName",product,
                            "productPrice",productPrice);
            Toast.makeText(this, "Details updated successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MyItemsActivity.class);
            startActivity(intent);
        }
    }

    public void onClickCancel(View view) {
        Intent intent = new Intent(this, MyItemsActivity.class);
        startActivity(intent);
    }

    public void onClickUploadImage(View view) {
        openFileChooser(1);
    }

    private void openFileChooser(int i) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, i);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            mProductImageUri = data.getData();
            uploadFile(mProductImageUri, 1);
            Picasso.with(this).load(mProductImageUri).into(mItemImage);
//            mImageView.setImageURI(mImageUri);
            if (mProductImageUri != null) {
                mItemImage.setVisibility(View.VISIBLE);
            } else {
                mItemImage.setVisibility(View.GONE);
            }
        }
    }
    private void uploadFile(Uri mImageUri, final int i) {
        Log.i(TAG, "Step 1");
        if (mImageUri != null) {
            Log.i(TAG, "Step 2");
            final StorageReference fileReference = mStorageReference.
                    child(System.currentTimeMillis()
                            + "." + getFileExtension(mImageUri));
            Log.i(TAG, "Step 3");
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener
                            (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    fileReference.getDownloadUrl()
                                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {

                                                    downloadUrl = String.valueOf(task.getResult());
                                                    Log.i(TAG, downloadUrl + "Check Check");
                                                }
                                            });

                                    Toast.makeText(EditItemActivity.this,
                                            "Image added Successfully",
                                            Toast.LENGTH_SHORT).show();


                                    fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            downloadUrl = String.valueOf(task.getResult());
                                            product.setProductImage(downloadUrl);


                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditItemActivity.this,
                                    "Issue While adding data" + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    //get file extension from the image
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}