package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {
    Button register;
    EditText  memail, mpassword;
    FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        register = findViewById(R.id.btn_signupreg);
        memail = findViewById(R.id.edt_email);
        mpassword = findViewById(R.id.edt_pas);
        fauth = FirebaseAuth.getInstance();
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_signupreg:

                final String email = memail.getText().toString().trim();
                final String password = mpassword.getText().toString().trim();

                if (email.isEmpty()) {
                    memail.setError("Email is required");
                }
                if (TextUtils.isEmpty(password)) {
                    mpassword.setError("password is required");
                }
                if (password.length() < 6) {
                    mpassword.setError("password must have 6 characters");
                }
                else {
                    fauth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = fauth.getCurrentUser();
                            String email=user.getEmail();

                            if(task.isSuccessful()){
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Register.this, "Check Your Mail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                startActivity(new Intent(Register.this,LoginActivity.class));
                                finish();
                            }else
                            {
                                Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        }
    }
}











