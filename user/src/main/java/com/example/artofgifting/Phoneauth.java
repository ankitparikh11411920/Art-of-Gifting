package com.example.artofgifting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Phoneauth extends AppCompatActivity  {
    EditText phoneno;
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneauth);
        phoneno = (EditText) findViewById(R.id.editTextPhone);
        verify=(Button)findViewById(R.id.btn_sendotp);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  number=phoneno.getText().toString();
                if(number.isEmpty() || number.length()<10)
                {
                    phoneno.setError("Valid number is required....");
                    phoneno.requestFocus();
                    return;
                }
                String phonenumber="+91"+number;
                Intent intent=new Intent(Phoneauth.this,Otp.class);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
            }
        });

    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
//        {
//            Intent intent=new Intent(Phoneauth.this,Upload_Image.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//    }
}
