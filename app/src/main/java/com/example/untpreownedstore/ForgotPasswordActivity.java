package com.example.untpreownedstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    public Button mResetButton;
    private FirebaseAuth firebaseAuth;
    public EditText mEmailAddress;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Reset Password");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_forgot_password);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        mEmailAddress=findViewById(R.id.email_address);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    public void onClickResetPassword(View view) {
        String email = mEmailAddress.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email should not be empty",Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage(" Sending Email ...");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email).
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email sent successfully.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ForgotPasswordActivity.this, NewLoginActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}