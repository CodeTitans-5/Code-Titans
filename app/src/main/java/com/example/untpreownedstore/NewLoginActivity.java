package com.example.untpreownedstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class NewLoginActivity extends AppCompatActivity {
    private static final String TAG = "New Login Activity";
    private TextInputEditText mEmail, mPassword;
    private String email, password, userId;
    User user;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Login !!");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_new_login);
        uiMethods();
    }
    //The following method initializes the  edittext fields and firebase.
    private void uiMethods() {
        mEmail = findViewById(R.id.new_email_login);
        mPassword = findViewById(R.id.new_password);
        firebaseAuth = FirebaseAuth.getInstance();

        try {
            firebaseUser = firebaseAuth.getCurrentUser();
        } catch (NullPointerException e) {
            Log.i(TAG, "uiMethods: " + " User is not logged in yet");
        }
        try {
            userId = firebaseUser.getUid();
        } catch (NullPointerException nullPointerException) {
            Log.i(TAG, "uiMethods: " + " User don't have an account");
        }
        progressDialog = new ProgressDialog(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users");
//        try{
//            collectionReference.document(userId)
//                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                            if (documentSnapshot != null) {
//                                User user = new User();
//                                user = documentSnapshot.
//                                        toObject(User.class);
//                                verifyPassword = user.getPassword();
//                            }
//
//                        }
//                    });
//        }catch (NullPointerException nullPointerException){
//            Log.i(TAG, "uiMethods: "+ " User don't have an account");
//        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
// THe following method checks if credentials entered by the user are correct or not.
    //If so user will be logged into the application.
    public void onClickLogin(View view) {
        email = Objects.requireNonNull(mEmail.getText()).toString().trim();
        password = Objects.requireNonNull(mPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email should not be empty");
            //Toast.makeText(this,"Email should not be empty",Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password should not be empty");
            // Toast.makeText(this,"password should not be empty",Toast.LENGTH_LONG).show();
            return;
        }
//        Log.i(TAG, "onClickLogin: " + password + verifyPassword);
        if (email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            if (password.length() >= 6) {


//                Log.i(TAG, "onClickLogin: Test1");
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(NewLoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    Log.i(TAG, "onComplete: Test2");
                                        if (firebaseUser.isEmailVerified()) {
                                        progressDialog.setMessage("Logging in...");
                                        progressDialog.show();
                                        Toast.makeText(NewLoginActivity.this,
                                                "Login successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(NewLoginActivity.this, DashboardActivity.class);
                                        startActivity(i);
                                        userId = firebaseUser.getUid();
                                        saveNewPassword();
                                    } else {
                                        Log.i(TAG, "onComplete: " + "Test");
                                        //Toast.makeText(SigninActivity.this, "User email is not verified", Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(NewLoginActivity.this);
                                        builder.setMessage("User Email not verified").setCancelable(false);
                                        builder.setPositiveButton("send email verification again", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                firebaseUser.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(NewLoginActivity.this, "Email sent", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    Toast.makeText(NewLoginActivity.this, "Problem in sending email verification please try again", Toast.LENGTH_LONG).show();
                                                                }
                                                                //dialog.cancel();

                                                            }
                                                        });

                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.setTitle("Email verification");
                                        alert.show();
                                    }
                                } else {
                                    Toast.makeText(NewLoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });


            } else {
                mPassword.setError("Password length is minimum 6 characters.");
                Toast.makeText(this, "Password limit not reached", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Email pattern not matched", Toast.LENGTH_LONG).show();
        }
    }
    //New password will be updated to the database
    private void saveNewPassword() {
        firebaseFirestore.collection("Users")
                .document(userId)
                .update("password",mPassword.getText().toString());

    }
}