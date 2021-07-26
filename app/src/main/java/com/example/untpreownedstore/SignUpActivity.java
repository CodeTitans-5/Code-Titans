package com.example.untpreownedstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "Sign Up Activity";
    private TextInputEditText mFirstName,mMiddleName,mLastName,mPhoneNumber,mEmail,mPassword,mReenterPassword;
    MaterialButtonToggleGroup mGender;
    Button mMale,mFemale,mOther;
    String userId,firstName,middleName,lastName,gender="male",phoneNumber,email,password,reenterPassword;
    User user;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sign Up Form !!");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sign_up);
        uiMethods();

    }
    //The following method initializes the  edittext fields, text field, button, spinner and image view.
    private void uiMethods() {
        mFirstName = findViewById(R.id.firstName);
        mMiddleName = findViewById(R.id.middleName);
        mLastName = findViewById(R.id.lastName);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passsword);
        mReenterPassword =findViewById(R.id.reenterPasssword);
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
    // The data in the fields are assigned to the corresponding strings and checks if
//    data is appropriate for that field. If so, user data will be sent to the database and
//    verification link will be sent to the user's email.
    public void onClickSubmit(View view) {
        Log.i(TAG, "onClickSubmit: ");

        firstName = mFirstName.getText().toString();
        middleName = mMiddleName.getText().toString();
        lastName = mLastName.getText().toString();
        phoneNumber = mPhoneNumber.getText().toString();
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        reenterPassword = mReenterPassword.getText().toString();
        if ((TextUtils.isEmpty(firstName)) && (TextUtils.isEmpty(middleName)) &&
                (TextUtils.isEmpty(lastName)) && (TextUtils.isEmpty(phoneNumber)) &&
                (TextUtils.isEmpty(email)) && (TextUtils.isEmpty(password)) &&
                (TextUtils.isEmpty(reenterPassword))) {
            // Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
            mFirstName.setError("First Name should not be empty");
            mLastName.setError("Last Name should not be empty");
            mEmail.setError("Email should not be empty");
            mPhoneNumber.setError("Phone number should not be empty");
            mPassword.setError("Password should not be empty");
            mReenterPassword.setError("Re-Password should not be empty");
        }else {
            if (TextUtils.isEmpty(firstName)) {
                mFirstName.setError("First Name should not be empty");
                //Toast.makeText(this, "User Name should not be empty", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email should not be empty");
                //Toast.makeText(this, "Email should not be empty", Toast.LENGTH_LONG).show();

            } else if (!email.contains("@") || !email.contains(".")) {
                mEmail.setError("Enter a valid email address");
                //Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password should not be empty");
                // Toast.makeText(this, "Password should not be empty", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(reenterPassword)) {
                mReenterPassword.setError("Re-Enter Password field should not be empty");
                // Toast.makeText(this, "Re-Password should not be empty", Toast.LENGTH_LONG).show();

            } else if (password.length() < 6) {
                mPassword.setError("Password should have a minimum length of 6 characters");
                // Toast.makeText(this, "Password should have a minimum length of 8 characters",
                // Toast.LENGTH_LONG).show();

            } else if (!(password.equals(reenterPassword))) {
                mReenterPassword.setError("Password and Confirm Passwords does not match");
                mPassword.setError("Password does not match");
                //Toast.makeText(this,"Password and Confirm Passwords does not match",
                // Toast.LENGTH_LONG).show();

            }
            else{
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                        (this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    userId = user.getUid();
                                    assert user != null;
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this,
                                                                "Verification link sent to " + email,
                                                                Toast.LENGTH_LONG).show();
                                                        Log.i( "success" ,"email sent");

                                                        saveData();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(SignUpActivity.this,
                                            "Could not register please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                );
                firebaseFirestore = FirebaseFirestore.getInstance();
                collectionReference = firebaseFirestore.collection("Users");
//                Map<String, Object> adduser = new HashMap<>();
//                adduser.put("Username",uname );
//                adduser.put("Email", em);
//                itemCollection.document().set(adduser);
            }
        }
    }
    //User data is saved into user object
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
    // User Data is sent to database.
    private void sendDataTODB(String userId) {
        collectionReference.document(userId)
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                clearUi();
                Log.i(TAG, "clearUi method is executed");
//                finish();
//                Log.i(TAG, "Add Employee Activity is closed and returns to Main Activity");
                Toast.makeText(SignUpActivity.this,
                        "Successfully signed up", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,
                        "Issue while Signing in. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //The following method clears all data form the UI after data is sent to database
    private void clearUi() {
        mFirstName.setText("");
        mMiddleName.setText("");
        mLastName.setText("");
        mEmail.setText("");
        mPassword.setText("");
        mReenterPassword.setText("");
        mPhoneNumber.setText("");
        Intent i = new Intent(SignUpActivity.this,
                LoginActivity.class);
        startActivity(i);
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
}