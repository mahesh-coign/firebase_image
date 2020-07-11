package com.mahesh.demofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText etName,etEmail,etPassword,etMobile;
    private String name,mobile,email,password;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etName      = findViewById(R.id.et_name_signUp);
        etMobile    = findViewById(R.id.et_mobile_signUp);
        etEmail     = findViewById(R.id.et_email_signUp);
        etPassword  = findViewById(R.id.et_password_signUp);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void onSignUpTapped(View view) {
        name        = etName.getText().toString().trim();
        email       = etEmail.getText().toString().trim();
        mobile      = etMobile.getText().toString().trim();
        password    = etPassword.getText().toString();
        createUser();
    }

    private void createUser() {
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                storeUserDetails();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserDetails() {
        String uId = auth.getCurrentUser().getUid();
        User user = new User(name,email,mobile);
        userRef.child(uId).setValue(user);
    }

    public void onSignInTapped(View view) {
        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
    }
}
