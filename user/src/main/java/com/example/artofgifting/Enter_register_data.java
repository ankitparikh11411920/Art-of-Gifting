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

import com.example.artofgifting.Helper.NetworkHelper;
import com.example.artofgifting.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

public class Enter_register_data extends AppCompatActivity implements View.OnClickListener {

    Button register;
    EditText fullname;
    TextView dob;
    FirebaseAuth fauth;
    Calendar c;
    DatePickerDialog dpd;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper=new NetworkHelper(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_register_data);

        register = findViewById(R.id.user_btn_signup_registerdata);
        fullname = findViewById(R.id.user_edt_fullname_registerdata);
        dob = findViewById(R.id.user_edt_dob_registerdata);

        fauth = FirebaseAuth.getInstance();
        register.setOnClickListener(this);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(Enter_register_data.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        dob.setText(mday + "/" + (mmonth + 1) + "/" + myear);
                    }
                }, day, month, year);
                dpd.show();

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.user_btn_signup_registerdata:
                final String username = fullname.getText().toString();
                final String bdob = dob.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    fullname.setError("Username is required");
                }
                if (TextUtils.isEmpty(bdob)) {
                    dob.setError("Date of birth is required");
                } else {
                    FirebaseUser user = fauth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        String email=user.getEmail();
                        String userkey = fauth.getUid();
                        UserModel db = new UserModel();
                        db.setUserkey(userkey);
                        db.setFullname(username);
                        db.setDob(bdob);
                        db.setContact(email);
                        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                        String refreshToken= FirebaseInstanceId.getInstance().getToken();
                        myRef.child(userkey).setValue(db);
                        myRef.child(userkey).child("token").setValue(refreshToken);

                        startActivity(new Intent(Enter_register_data.this, Upload_Image.class));

                        finish();
                    } else {
                        Toast.makeText(this, "Verify your Email by clicking on link provided in Email", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}
