package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity implements View.OnClickListener {
    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText txtcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.user_phone_progressbar);

        txtcode= findViewById(R.id.user_edt_code_otp);
        String phonenumber=getIntent().getStringExtra("phonenumber");
        sendVerificationcode(phonenumber);


    }

    private void sendVerificationcode(String phonenumber) {
    progressBar.setVisibility(View.VISIBLE);
    PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,90,TimeUnit.SECONDS,TaskExecutors.MAIN_THREAD,mCallBack);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s,PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(s,forceResendingToken);
            verificationid =s;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                txtcode.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Otp.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.user_btn_confirm_otp:
            String code = txtcode.getText().toString().trim();

            if ((code.isEmpty() || code.length() < 6)){

                txtcode.setError("Enter code");
                txtcode.requestFocus();

                return;

            }
            verifyCode(code);

        }
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationid,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String id=mAuth.getUid();
                    Intent intent=new Intent(Otp.this, otpRegister.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("ID",id);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Otp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
