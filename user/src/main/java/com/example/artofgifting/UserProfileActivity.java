package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.artofgifting.Models.UserModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    //views
    CircleImageView circleImageView;
    TextView name, email, dateofbirth, address, username;
    LinearLayout linear_name, linear_dateofbirth, linear_address, linear_password, linear_addfamily;

    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    //vars
    Picasso picasso;
    private String uname;
    private String uemail;
    private String uaddress;
    private String udob;
    private String uimageurl;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        circleImageView = findViewById(R.id.user_img_userimage_user_info);
        username = findViewById(R.id.user_tv_username_user_info);
        name = findViewById(R.id.user_tv_username_user_profile);
        email = findViewById(R.id.user_tv_email_user_profile);
        dateofbirth = findViewById(R.id.user_tv_birthdate_user_info);
        address = findViewById(R.id.user_tv_address_user_info);
        progressDialog = new ProgressDialog(this);
        linear_name = findViewById(R.id.user_linear_name_user_info);
        linear_address = findViewById(R.id.user_linear_address_user_info);
        linear_password = findViewById(R.id.user_linear_password_user_info);
        linear_dateofbirth = findViewById(R.id.user_linear_dateofbirth_user_info);
//        linear_addfamily = findViewById(R.id.user_linear_btn_add_familydetails);

        setuserinfo();
        changeuserinfo();

    }

    private void changeuserinfo() {
        linear_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changename();
            }
        });

        linear_dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changedob();
            }
        });

        linear_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeaddress();
            }
        });
        /*linear_addfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, FamilyDetailsActivity.class));
            }
        });*/

        linear_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changepassword();
            }
        });
    }

    private void changepassword() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootview = LayoutInflater.from(UserProfileActivity.this)
                .inflate(R.layout.raw_dialog_okcancel, null);
        builder.setView(rootview);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button ok = rootview.findViewById(R.id.user_dialog_btn_ok);
        Button cancel = rootview.findViewById(R.id.user_dialog_btn_cancel);
        final TextInputLayout dialog_username = rootview.findViewById(R.id.user_dialog_edt_name);
        dialog_username.setHint("Password");
        dialog_username.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog_username.setPasswordVisibilityToggleEnabled(true);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = dialog_username.getEditText().getText().toString();
                if (!newname.isEmpty()) {
                    user.updatePassword(newname);
                    alertDialog.dismiss();
                } else {
                    dialog_username.setError("Enter a valid Password");
                    dialog_username.requestFocus();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void changeaddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootview = LayoutInflater.from(UserProfileActivity.this)
                .inflate(R.layout.raw_dialog_okcancel, null);
        builder.setView(rootview);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button ok = rootview.findViewById(R.id.user_dialog_btn_ok);
        Button cancel = rootview.findViewById(R.id.user_dialog_btn_cancel);
        final TextInputLayout dialog_username = rootview.findViewById(R.id.user_dialog_edt_name);
        dialog_username.getEditText().setText(uaddress);
        dialog_username.setHint("Address");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = dialog_username.getEditText().getText().toString().trim();
                if (!newname.isEmpty()) {
                    myRef.child("Users").child(user.getUid()).child("address").setValue(newname);
                    address.setText(newname);
                    alertDialog.dismiss();
                } else {
                    dialog_username.setError("Field cannot be Empty!");
                    dialog_username.requestFocus();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void changedob() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootview = LayoutInflater.from(UserProfileActivity.this)
                .inflate(R.layout.raw_dialog_okcancel, null);
        builder.setView(rootview);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button ok = rootview.findViewById(R.id.user_dialog_btn_ok);
        Button cancel = rootview.findViewById(R.id.user_dialog_btn_cancel);
        final TextInputLayout dialog_username = rootview.findViewById(R.id.user_dialog_edt_name);
        dialog_username.getEditText().setText(udob);
        dialog_username.setHint("Date Of Birth");
        dialog_username.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = dialog_username.getEditText().getText().toString();
                if (!newname.isEmpty()) {
                    myRef.child("Users").child(user.getUid()).child("dob").setValue(newname);
                    dateofbirth.setText(newname);
                    alertDialog.dismiss();
                } else {
                    dialog_username.setError("Enter a valid BirthDate");
                    dialog_username.requestFocus();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void changename() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootview = LayoutInflater.from(UserProfileActivity.this)
                .inflate(R.layout.raw_dialog_okcancel, null);
        builder.setView(rootview);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button ok = rootview.findViewById(R.id.user_dialog_btn_ok);
        Button cancel = rootview.findViewById(R.id.user_dialog_btn_cancel);
        final TextInputLayout dialog_username = rootview.findViewById(R.id.user_dialog_edt_name);
        dialog_username.getEditText().setText(uname);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = dialog_username.getEditText().getText().toString();
                if (!newname.isEmpty()) {
                    myRef.child("Users").child(user.getUid()).child("fullname").setValue(newname);
                    name.setText(newname);
                    username.setText(newname);
                    alertDialog.dismiss();
                } else {
                    dialog_username.setError("Field cannot be Empty!");
                    dialog_username.requestFocus();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void setuserinfo() {
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DataSnapshot dataSnapshot1=dataSnapshot.child("Users").child(user.getUid());
//                UserModel userdata = dataSnapshot.child("Users").child(user.getUid()).getValue(UserModel.class);
                uname = dataSnapshot1.child("fullname").getValue(String.class);
                udob = dataSnapshot1.child("dob").getValue(String.class);
                uaddress = dataSnapshot1.child("address").getValue(String.class);
                uemail = user.getEmail();
                uimageurl = dataSnapshot.child("Users").child(user.getUid()).child("image_url").getValue(String.class);
                picasso.get().load(uimageurl).into(circleImageView);
                name.setText(uname);
                username.setText(uname);
                email.setText(uemail);
                address.setText(uaddress);
                dateofbirth.setText(udob);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
