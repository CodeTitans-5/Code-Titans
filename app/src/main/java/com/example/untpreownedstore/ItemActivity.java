package com.example.untpreownedstore;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ItemActivity extends AppCompatActivity {
    private static final String TAG = "Item Activity";
    TextView mUserName, mUserEmail, mUserPhone, mProductName, mProductPrice, mProductDescription;
    ImageView mItemImage;
    String productCategory, productId, phoneNumber, email;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productCategory = getIntent().getStringExtra("CATEGORY");
        productId = getIntent().getStringExtra("PRODUCT_ID");
        getSupportActionBar().setTitle(productCategory);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_item);

        uiMethods();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void uiMethods() {
        mUserName = findViewById(R.id.userName);
        mUserEmail = findViewById(R.id.e_mail);
        mUserPhone = findViewById(R.id.phone);
        mProductName = findViewById(R.id.name);
        mProductPrice = findViewById(R.id.price);
        mProductDescription = findViewById(R.id.description);
        mItemImage = findViewById(R.id.itemImage);

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection(productCategory);
        readFromDB();

    }

    private void readFromDB() {
        collectionReference.document(productId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Product product = documentSnapshot.toObject(Product.class);
                mProductName.setText(product.getProductName());
                mProductDescription.setText(product.getProductDescription());
                mProductPrice.setText("$ " + product.getProductPrice());
                Picasso.with(getApplicationContext()).load(product.getProductImage()).into(mItemImage);
                String customerId = product.getUserId();
                getContactInfo(customerId);
            }
        });
    }

    private void getContactInfo(String customerId) {
        firebaseFirestore.collection("Users").document(customerId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                mUserName.setText(user.getLastName() + " " + user.getFirstName() + " " + user.getMiddleName());
                mUserEmail.setText(user.getEmailId());
                mUserPhone.setText("+1" + user.getPhoneNumber());
                phoneNumber = user.getPhoneNumber();
                email = user.getEmailId();
            }
        });
        mUserPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                builder.setMessage("Choose one!!").setCancelable(false);
                builder.setPositiveButton("WhatsApp", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("smsto:" + phoneNumber);
                        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                        i.setPackage("com.whatsapp");
                        startActivity(Intent.createChooser(i, ""));
                    }
                });
                builder.setNegativeButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uri = "tel:" + "+1" + phoneNumber.trim();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        mUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //TODO smth
                }
            }
        });
    }
}