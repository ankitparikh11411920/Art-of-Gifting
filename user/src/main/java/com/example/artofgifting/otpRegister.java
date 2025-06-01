package com.example.artofgifting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artofgifting.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class otpRegister extends AppCompatActivity implements View.OnClickListener {
    Button goregister;
    EditText gofullname;
    TextView godob;
    Calendar c;
    DatePickerDialog dpd;
    private DatabaseReference myRef;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_register);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");

        goregister = findViewById(R.id.btn_otpsignup);
        gofullname = findViewById(R.id.edt_otpfullname);
        godob = findViewById(R.id.edt_otpdob);
        goregister.setOnClickListener(this);
        godob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(otpRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        godob.setText(mday + "/" + (mmonth + 1) + "/" + myear);
                    }
                }, day, month, year);
                dpd.show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_otpsignup:

                String Otp_fullname = gofullname.getText().toString();
                String Otp_date_of_birth = godob.getText().toString();

                if (TextUtils.isEmpty(Otp_fullname)) {
                    gofullname.setError("Fullname is Required");
                    return;
                }
                if (TextUtils.isEmpty(Otp_date_of_birth)) {
                    godob.setError("Fullname is Required");
                    return;
                } else {
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    String email=user.getPhoneNumber();

                    UserModel dbgo = new UserModel();
                    dbgo.setUserkey(id);
                    dbgo.setFullname(Otp_fullname);
                    dbgo.setDob(Otp_date_of_birth);
                    dbgo.setContact(email);
                    myRef.child(id).setValue(dbgo);
                    Intent intent = new Intent(otpRegister.this, Upload_Image.class);
                    startActivity(intent);
                    finish();
                }
                break;


        }
    }
}
