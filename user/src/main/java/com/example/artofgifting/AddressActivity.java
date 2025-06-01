package com.example.artofgifting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.artofgifting.Helper.NetworkHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout Pincode, houseno, colony, landmark;
    private Button submit;
    //vars
    public static final String FIELD_ERROR = "Field Required";


    //fireabse
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper = new NetworkHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Pincode = findViewById(R.id.user_edt_pin);
        houseno = findViewById(R.id.user_edt_houseno);
        colony = findViewById(R.id.user_edt_street_address_);
        landmark = findViewById(R.id.user_edt_Landmark);
        submit = findViewById(R.id.user_btn_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_btn_submit:
                final String mhouseno = houseno.getEditText().getText().toString();
                final String mcolony = colony.getEditText().getText().toString();
                final String mlandmark = landmark.getEditText().getText().toString();
                final String mpincode = Pincode.getEditText().getText().toString();
                if (mhouseno.isEmpty()) {
                    houseno.setError(FIELD_ERROR);
                }
                if (mcolony.isEmpty()) {
                    colony.setError(FIELD_ERROR);
                }
                if (mlandmark.isEmpty()) {
                    landmark.setError(FIELD_ERROR);
                }
                if (mpincode.isEmpty()) {
                    Pincode.setError(FIELD_ERROR);
                } else {
                    String total = mhouseno + "," + mcolony + "," + mlandmark + "," + mpincode;
                    ref.child("Users").child(user.getUid()).child("address").setValue(total);
                    StyleableToast.makeText(AddressActivity.this, "Address Added", R.style.exampletoast).show();
                    finish();
                }
//                    String userkey=ref.push().getKey();

//                    useraddressmodel.setAddress(mhouseno+","+mcolony+","+mlandmark+","+mpincode);
        }

    }
}
