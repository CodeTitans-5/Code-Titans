
package com.example.untpreownedstore;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Add Product Activity";
    private TextInputEditText mProductName,mProductDescription,mProductPrice;
    private ImageView mProductImage;
    private Spinner mCategorySpinner;
    private TextView mProductId;
    String userId,productId,productName,productDescription,productPrice,productCategory = "Automobiles";
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageReference;
    Product product = new Product();
    private Uri mProductImageUri;
    private StorageTask mUploadTask;
    String downloadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The following line is used to set the title of the screen in the action bar.
        getSupportActionBar().setTitle("Product Info");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_product);
        uiMethods();
    }

    //        The following method initializes the  edittext fields, text field, button, spinner and image view.
    private void uiMethods() {
        mProductName = findViewById(R.id.productName);
        mProductDescription = findViewById(R.id.productDescription);
        mProductPrice = findViewById(R.id.productPrice);
        mProductId = findViewById(R.id.productId);
        mCategorySpinner = findViewById(R.id.categorySpinner);
        mProductImage = findViewById(R.id.product_image);

//       Following lines of code is used to set the static values to the spinner.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(this, R.array.categories,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapter);
        mCategorySpinner.setOnItemSelectedListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
//        collectionReference = firebaseFirestore.collection("Employees");
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            userId = user.getUid();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    // The following method will take the user back to DashboardActivity.class
    public void onClickCancel(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
    // The following method validates if all fields are filled with appropriate data.
    public void onClickAddProduct(View view) {


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
    //    In the following method data from fields are set to product object.
    private void saveData() {
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Toast.makeText(AddProductActivity.this,
                    "Upload in progress", Toast.LENGTH_SHORT).show();

        }else{
            Log.i(TAG, "Add product object is created");
            product.setProductName(productName);
            product.setProductDescription(productDescription);
            product.setProductPrice(productPrice);
            product.setUserId(userId);
            collectionReference = firebaseFirestore.collection(productCategory);
            sendDataTODB(productId);
        }

    }
    // Data will be sent to the database using the following method.
    private void sendDataTODB(String productId) {
        collectionReference.document(productId)
                .set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                clearUi();
                Log.i(TAG, "clearUi method is executed");
//                finish();
                Intent i = new Intent(AddProductActivity.this,
                        DashboardActivity.class);
                startActivity(i);
//                Log.i(TAG, "Add Employee Activity is closed and returns to Main Activity");
                Toast.makeText(AddProductActivity.this,
                        "Product added successfully.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProductActivity.this,
                        "Issue while adding product. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        firebaseFirestore.collection("Users").document(userId)
                .collection("My Items").document(productId).set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "onSuccess:"+ "Item added to user's items");
                    }
                });
    }
    // This method clears all fields after the data is sent to the database
    private void clearUi() {
        mProductId.setVisibility(View.GONE);
        mProductName.setText("");
        mProductDescription.setText("");
        mProductPrice.setText("");
        mProductImage.setVisibility(View.GONE);
    }
    //The following method is used to uplaod product image from the gallery.
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
            Picasso.with(this).load(mProductImageUri).into(mProductImage);
//            mImageView.setImageURI(mImageUri);
            if (mProductImageUri != null) {
                mProductImage.setVisibility(View.VISIBLE);
            } else {
                mProductImage.setVisibility(View.GONE);
            }
        }
    }
    //The following method saves the image into firebase storage
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

                                    Toast.makeText(AddProductActivity.this,
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
                            Toast.makeText(AddProductActivity.this,
                                    "Issue While adding data" + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    //The following method is used to get file extension from the image
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        productCategory = parent.getItemAtPosition(position).toString();
        DocumentReference ref = firebaseFirestore
                .collection(productCategory).document();
        productId  = ref.getId();
        mProductId.setText(productId);
        product.setProductId(productId);
        product.setProductCategory(productCategory);
    }

    public void setProductDetails()
    {
        Bundle extras = getIntent().getExtras();
        String pName = extras.getString("productName");
        String pDesc = extras.getString("productDescription");
        String pPrice = extras.getString("productPrice");
        productName = pName;
        productDescription = pDesc;
        productPrice = pPrice;
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


//Add progress bar for image