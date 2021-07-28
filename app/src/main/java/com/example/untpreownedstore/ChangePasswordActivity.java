package com.example.untpreownedstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "Change Password Activity";
    private TextInputEditText mOldPassword, mNewPassword, mConfirmNewPassword;
    String oldPassword, verifyOldPassword, newPassword, verifyPassword, userId;
    User user;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Change Password");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_change_password);
        initializeUI();
    }
    //The following method initializes the  edittext fields and firebase.
    private void initializeUI() {
        mOldPassword = findViewById(R.id.enter_password);
        mNewPassword = findViewById(R.id.enter_new_password);
        mConfirmNewPassword = findViewById(R.id.confirm_new_password);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null) {
            userId = firebaseUser.getUid();
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users");
        try {
            collectionReference.document(userId)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (documentSnapshot != null) {
                                User user = new User();
                                user = documentSnapshot.
                                        toObject(User.class);
                                verifyOldPassword = user.getPassword();
                            }

                        }
                    });
        } catch (Exception e) {
            Log.i(TAG, "uiMethods: " + " User don't have an account");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    //    The following method checks whether the data is entered into the fields appropriately
//    if so password will be updated successfully.
    public void onClickUpdatePassword(View view) {
        oldPassword = mOldPassword.getText().toString();

        newPassword = mNewPassword.getText().toString();
        verifyPassword = mConfirmNewPassword.getText().toString();
        Log.i(TAG, "onClickUpdatePassword: " + mOldPassword.getText().toString() + "  " + verifyOldPassword);
        if ((TextUtils.isEmpty(oldPassword)) && (TextUtils.isEmpty(newPassword)) &&
                (TextUtils.isEmpty(verifyPassword))) {
            // Toast.makeText(this, "All fields must be filled", Toast.LENGTH_LONG).show();
            mOldPassword.setError("Old password should not be empty");
            mNewPassword.setError("New Password should not be empty");
            mConfirmNewPassword.setError("Confirm new password should not be empty");
        } else {
            if (TextUtils.isEmpty(oldPassword)) {
                mOldPassword.setError("Old password should not be empty");
                //Toast.makeText(this, "User Name should not be empty", Toast.LENGTH_LONG).show();

            }else if (!oldPassword.equals(verifyOldPassword)) {
                mOldPassword.setError("Enter correct password");
                mOldPassword.setText("");
                //Toast.makeText(this, "Email should not be empty", Toast.LENGTH_LONG).show();

            }
            else if (TextUtils.isEmpty(newPassword)) {
                mNewPassword.setError("New password should not be empty");
                //Toast.makeText(this, "Email should not be empty", Toast.LENGTH_LONG).show();

            } else if (TextUtils.isEmpty(verifyPassword)) {
                mConfirmNewPassword.setError("Confirm new password should not be empty");
                //Toast.makeText(this, "Email should not be empty", Toast.LENGTH_LONG).show();

            } else if (oldPassword.length() < 6) {
                mOldPassword.setError("Password should have a minimum length of 6 characters");
                // Toast.makeText(this, "Password should have a minimum length of 8 characters",
                // Toast.LENGTH_LONG).show();

            } else if (verifyPassword.length() < 6) {
                mConfirmNewPassword.setError("Password should have a minimum length of 6 characters");
                // Toast.makeText(this, "Password should have a minimum length of 8 characters",
                // Toast.LENGTH_LONG).show();

            } else if (newPassword.length() < 6) {
                mNewPassword.setError("Password should have a minimum length of 6 characters");
                // Toast.makeText(this, "Password should have a minimum length of 8 characters",
                // Toast.LENGTH_LONG).show();

            } else if (!(newPassword.equals(verifyPassword))) {
                mConfirmNewPassword.setError("New Password and Confirm New Password does not match");
                mNewPassword.setError("New Password and Confirm New Password does not match");
                //Toast.makeText(this,"Password and Confirm Passwords does not match",
                // Toast.LENGTH_LONG).show();

            } else {
                collectionReference.document(userId).update("password",newPassword);
                Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ChangePasswordActivity.this,
                        DashboardActivity.class);
                startActivity(i);
            }


        }
    }
    public void setPwdDetails()
    {
        Bundle extras = getIntent().getExtras();
        String old = extras.getString("oldPassword");
        String newPassword1 = extras.getString("newPassword");
        String verifyPassword1 = extras.getString("verifyPassword");
        oldPassword = old;
        newPassword = newPassword1;
        verifyPassword = verifyPassword1;
    }
}