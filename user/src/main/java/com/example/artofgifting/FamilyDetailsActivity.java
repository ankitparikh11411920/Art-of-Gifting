package com.example.artofgifting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.artofgifting.Models.FamilyDetailsModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Calendar;

public class FamilyDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    //vars
    private static final String[] relatation = new String[]{
            "Brother", "Sister", "Father", "Mother", "Relative", "GrandMother",
            "GrandFather", "GrandSon", "GrandDaughter", "Nephew", "Niece", "Husband", "Wife",
            "Father-in-law", "Mother-in-law", "Sister-in-law", "Brother-in-law",
            "Son-in-law", "Aunt", "Uncle", "Assistant", "Manager", "Friend", "Girlfriend", "Boyfriend", "Boss"
    };
    String gender;
    public static final String FIELD_ERROR = "Field Required";
    //views
    private TextInputLayout Name, Address;
    private EditText Birthdate, Anniversary;
    private Button submit;
    DatePickerDialog dpd;
    RadioGroup family_Gender;
    Toolbar toolbar;
    RadioButton radioButton;
    AutoCompleteTextView Relation;
    Calendar c;
    //firebase
    DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_details);
        inittoolbar();
        initrelationspinner();
        Name = findViewById(R.id.user_edt_Name_family_details);
        Birthdate = findViewById(R.id.user_edt_birthdate_family_details);
        Anniversary = findViewById(R.id.user_edt_anniversary_family_details);
        Address = findViewById(R.id.user_edt_address_family_details);
        submit = findViewById(R.id.user_btn_submit_family_details);
        family_Gender = findViewById(R.id.user_radiogroup_family_details);
        family_Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.user_radiobtn_male_family_details) {
                    gender = "Male";
                } else if(checkedId==R.id.user_radiobtn_female_family_details){
                    gender = "Female";
                }else {
                    StyleableToast.makeText(FamilyDetailsActivity.this,"Gender is required",R.style.exampletoast).show();
                    return;
                }
            }
        });

        submit.setOnClickListener(this);
        Birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(FamilyDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        Birthdate.setText(mday + "/" + (mmonth + 1) + "/" + myear);
                    }
                }, day, month, year);
                dpd.show();

            }
        });
        Anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(FamilyDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int myear, int mmonth, int mday) {
                        Anniversary.setText(mday + "/" + (mmonth + 1) + "/" + myear);
                    }
                }, day, month, year);
                dpd.show();

            }
        });


    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.user_familydetails_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Family Member");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initrelationspinner() {
        Relation = findViewById(R.id.user_edt_relationship_family_details);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.raw_relation_list, R.id.text_view_list_item, relatation);
        Relation.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_btn_submit_family_details:
                checkfield();

                /*String familykey = myref.push().getKey();
                FamilyDetailsModel familyDetailsModel = new FamilyDetailsModel();
                familyDetailsModel.setUserkey(familykey);
                familyDetailsModel.setName(mName);
                familyDetailsModel.setGender(mGender);
                familyDetailsModel.setBirthdate(mBirthdate);
                familyDetailsModel.setAnniversary(mAnniversary);
                familyDetailsModel.setRelatation(mRelatationship);
                familyDetailsModel.setAddress(mAddress);
                myref.child("Users").child(user.getUid()).child("Family Details").child(familykey).setValue(familyDetailsModel);
                startActivity(new Intent(FamilyDetailsActivity.this, HomeActivity.class));
                finish();*/
        }

    }

    private void checkfield() {
        final String mName = Name.getEditText().getText().toString().trim();
        final String mBirthdate = Birthdate.getText().toString().trim();
        final String mAnniversary = Anniversary.getText().toString().trim();
        final String mRelationship = Relation.getText().toString().trim();
        final String mAddress = Address.getEditText().getText().toString().trim();

        if (mName.isEmpty()) {
            Name.setError(FIELD_ERROR);
            Name.requestFocus();
            refresherror();
        }

        if (mBirthdate.isEmpty()) {
            Birthdate.setError(FIELD_ERROR);
            Birthdate.requestFocus();
            refresherror();

        }
        if (mRelationship.isEmpty()) {
            Relation.setError(FIELD_ERROR);
            Relation.requestFocus();
            refresherror();

        }
        if (mAddress.isEmpty()) {
            Address.setError(FIELD_ERROR);
            refresherror();
            Address.requestFocus();

        }
        if (gender==null) {
            StyleableToast.makeText(FamilyDetailsActivity.this, "Gender is required", R.style.exampletoast).show();
            refresherror();
            return;
        }
        else {
            addmember(mName, mBirthdate, mAddress, mAnniversary, mRelationship, gender);
        }
    }

    private void refresherror() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Name.setError(null);
                Birthdate.setError(null);
                Anniversary.setError(null);
                Relation.setError(null);
                Address.setError(null);
                Name.clearFocus();
                Birthdate.clearFocus();
                Anniversary.clearFocus();
                Relation.clearFocus();
                Address.clearFocus();
            }
        }, 4000);
    }

    private void addmember(String mName, String mBirthdate, String mAddress, String mAnniversary, String mRelatationship, String mGender) {
        String familykey = myref.push().getKey();
        FamilyDetailsModel familyDetailsModel = new FamilyDetailsModel();
        familyDetailsModel.setUserkey(familykey);
        familyDetailsModel.setName(mName);
        familyDetailsModel.setGender(mGender);
        familyDetailsModel.setBirthdate(mBirthdate);
        familyDetailsModel.setAnniversary(mAnniversary);
        familyDetailsModel.setRelation(mRelatationship);
        familyDetailsModel.setAddress(mAddress);
        myref.child("Users").child(user.getUid()).child("Family Details").child(familykey).setValue(familyDetailsModel);
//        startActivity(new Intent(FamilyDetailsActivity.this, HomeActivity.class));
        finish();
    }

}

