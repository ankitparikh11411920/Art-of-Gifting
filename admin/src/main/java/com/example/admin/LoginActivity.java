package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText adminusername, adminpassword;
    Button adminlogin,adminregister;

    //firebase
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onStart() {
        super.onStart();
        if(user!=null && user.isEmailVerified()){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        adminusername = findViewById(R.id.admin_username);
        adminpassword = findViewById(R.id.admin_password);
        adminregister=findViewById(R.id.admin_btnregister);
        adminlogin = findViewById(R.id.admin_btnlogin);
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String email = adminusername.getText().toString();
                 String password = adminpassword.getText().toString();
                if (email.isEmpty()) {
                    adminusername.setError("Email is required");
                }
                if (password.isEmpty()) {
                    adminpassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6) {
                    adminpassword.setError("Password must have 6 characters");
                    return;
                }else {

                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user=mAuth.getCurrentUser();
                                        if(user.isEmailVerified()){
                                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                            finish();
                                        }else {
                                            Toast.makeText(LoginActivity.this, "Verify your email", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
        adminregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
}
