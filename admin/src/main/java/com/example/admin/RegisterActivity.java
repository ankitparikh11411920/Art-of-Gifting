package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText edt_email,edt_password;
    Button btn_signup;

    //firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_pass);
        btn_signup=findViewById(R.id.btn_signupreg);
        mAuth=FirebaseAuth.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edt_email.getText().toString();
                String password=edt_password.getText().toString();
                if(email.isEmpty()){
                    edt_email.setError("Field is required");
                } if(password.isEmpty()){
                    edt_password.setError("Field is required");
                }if(password.length()<6){
                    edt_password.setError("Password must have 6 characters");
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user=mAuth.getCurrentUser();
                                    if(task.isSuccessful()){
                                        assert user != null;
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){
                                                            Toast.makeText(RegisterActivity.this, "Check Your Mail", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                                            finish();
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }
}