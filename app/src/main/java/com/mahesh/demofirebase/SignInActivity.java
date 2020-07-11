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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    private EditText etEmail,etPassword;
    private String email,password,uid;
    private FirebaseAuth auth;
    private Boolean isUser;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        auth= FirebaseAuth.getInstance();

    }

    public void onSignInTapped(View view) {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        authenticateUser();
    }

    private void authenticateUser() {
        if (email.equals("admin@gmail.com") && password.equals("admin123")){
            navigateToAdminActivity();
        }
        else {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    navigateToUserActivity();
                    Toast.makeText(SignInActivity.this, "SignIn Success....", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onSignUpTapped(View view) {
        startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
    }

    public void navigateToUserActivity(){
        startActivity(new Intent(SignInActivity.this,UserActivity.class));
    }

    public void navigateToAdminActivity(){
        startActivity(new Intent(SignInActivity.this,AdminActivity.class));
    }


}
