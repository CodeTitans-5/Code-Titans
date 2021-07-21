package com.example.untpreownedstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class EditInfoActivity extends AppCompatActivity {
    private static final String TAG = "Edit Info Activity";
    private TextInputEditText mFirstName, mMiddleName, mLastName, mPhoneNumber, mEmail;
    MaterialButtonToggleGroup mGender;
    Button mMale, mFemale, mOther;
    String userId, firstName, middleName, lastName, gender = "male", phoneNumber, email,password;
    User user;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Edit Info");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_info);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users");
        userId = user.getUid();
        uiMethods();
        readDataFromDB();
    }

    private void readDataFromDB() {
        collectionReference.document(userId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                        if (documentSnapshot != null) {
                            User user = new User();
                            user = documentSnapshot.
                                    toObject(User.class);
                            try {
                                mFirstName.setText(user.getFirstName());
                            } catch (NullPointerException e1) {
                                Log.i(TAG, String.valueOf(e1));
                            }
                            try {
                                mMiddleName.setText(user.getMiddleName());
                            } catch (NullPointerException e2) {
                                Log.i(TAG, "onEvent: " + e2.getMessage());
                            }
                            try {
                                mLastName.setText(user.getLastName());
                            } catch (NullPointerException e2) {
                                Log.i(TAG, "onEvent: " + e2.getMessage());
                            }
                            try {
                                String gender = user.getGender();
                                if(gender.equals("male")){
                                    mMale.setEnabled(true);
                                } else if(gender.equals("female")){
                                    mFemale.setEnabled(true);
                                }else{
                                    mOther.setEnabled(true);
                                }
                            }
                            catch (NullPointerException e3){
                                Log.i(TAG, "onEvent: "+e3.getMessage());
                            }
                            try {
                                mPhoneNumber.setText(user.getPhoneNumber());
                            }catch (NullPointerException e4){
                                Log.i(TAG, "onEvent: "+e4.getMessage());
                            }
                            try {
                                mEmail.setText(user.getEmailId());
                                password = user.getPassword();
                            }catch (NullPointerException e4){
                                Log.i(TAG, "onEvent: "+e4.getMessage());
                            }
                        }

                    }
                });
    }

    private void uiMethods() {
        mFirstName = findViewById(R.id.firstName);
        mMiddleName = findViewById(R.id.middleName);
        mLastName = findViewById(R.id.lastName);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mEmail = findViewById(R.id.email);
        mGender = findViewById(R.id.genderToggleGroup);
        mMale = findViewById(R.id.male);
        mFemale = findViewById(R.id.female);
        mOther = findViewById(R.id.other);

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        Log.i(TAG, "ui initialization method Completed.");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void onClickSubmit(View view) {
        Log.i(TAG, "onClickSubmit: ");

        firstName = mFirstName.getText().toString();
        middleName = mMiddleName.getText().toString();
        lastName = mLastName.getText().toString();
        phoneNumber = mPhoneNumber.getText().toString();
        email = mEmail.getText().toString();
        if ((TextUtils.isEmpty(firstName)) && (TextUtils.isEmpty(middleName)) &&
                (TextUtils.isEmpty(lastName)) && (TextUtils.isEmpty(phoneNumber)) &&
                (TextUtils.isEmpty(email))) {
            // Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
            mFirstName.setError("First Name should not be empty");
            mLastName.setError("Last Name should not be empty");
            mEmail.setError("Email should not be empty");
            mPhoneNumber.setError("Phone number should not be empty");
        } else {
            if (TextUtils.isEmpty(firstName)) {
                mFirstName.setError("First Name should not be empty");
                //Toast.makeText(this, "User Name should not be empty", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email should not be empty");
                //Toast.makeText(this, "Email should not be empty", Toast.LENGTH_LONG).show();

            } else if (!email.contains("@") || !email.contains(".")) {
                mEmail.setError("Enter a valid email address");
                //Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_LONG).show();
            } else {
                saveData();
            }

        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users");
//                Map<String, Object> adduser = new HashMap<>();
//                adduser.put("Username",uname );
//                adduser.put("Email", em);
//                itemCollection.document().set(adduser);
    }

    private void saveData() {
        user = new User();
        Log.i(TAG, "User object is created");
        user.setUserId(userId);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmailId(email);
        user.setGender(gender);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        sendDataTODB(userId);
    }

    private void sendDataTODB(String userId) {
        collectionReference.document(userId)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                clearUi();
                Log.i(TAG, "clearUi method is executed");
//                finish();
//                Log.i(TAG, "Add Employee Activity is closed and returns to Main Activity");
                Toast.makeText(EditInfoActivity.this,
                        "New details saved.", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditInfoActivity.this,
                        "Issue while Signing in. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clearUi() {
        mFirstName.setText("");
        mMiddleName.setText("");
        mLastName.setText("");
        mEmail.setText("");
        mPhoneNumber.setText("");
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
    public void maleClicked(View view) {
        gender = "male";
    }

    public void femaleClicked(View view) {
        gender = "female";
    }

    public void otherClicked(View view) {
        gender = "other";
    }

    public void onClickChangePassword(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}