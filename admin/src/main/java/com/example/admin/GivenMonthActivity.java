package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.Model.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GivenMonthActivity extends AppCompatActivity {


    //views
    Toolbar toolbar;
    TextView totalproducts,totalamount;

    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    //vars
    String current;
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_given_month);
        month = getIntent().getIntExtra("month", 0);
        totalproducts=findViewById(R.id.admin_given_month_totalproducts);
        totalamount=findViewById(R.id.admin_given_month_totalamount);
        inittoolbar();
        getOrderDetails();


    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_given_month_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void getMonth(ArrayList<OrderModel> orderModelArrayList) {
        switch (month) {
            case 1:
                current = "Jan-2020";
                toolbar.setTitle(current);
                checkproducts(current);
                break;
            case 2:
                Toast.makeText(this, "feb", Toast.LENGTH_SHORT).show();
                current = "Feb-2020";
                toolbar.setTitle(current);
                checkproducts(current);

                break;
            case 3:
                current = "Mar-2020";
                toolbar.setTitle(current);
                checkproducts(current);

                break;
            case 4:
                current = "Apr-2020";
                toolbar.setTitle(current);
                checkproducts(current);

                break;
            case 5:
                current = "May-2020";
                toolbar.setTitle(current);
                checkproducts(current);
                break;
            case 6:
                current = "Jun-2020";
                toolbar.setTitle(current);
                checkproducts(current);
                break;
            case 7:
                current = "Jul-2020";
                toolbar.setTitle(current);
                checkproducts(current);
                break;
            case 8:
                current = "Aug-2020";
                checkproducts(current);
                toolbar.setTitle(current);
                break;
            case 9:
                current = "Sep-2020";
                toolbar.setTitle(current);
                checkproducts(current);
                break;
            case 10:
                current = "Oct-2020";
                toolbar.setTitle(current);
                checkproducts(current);
                break;
            case 11:
                current = "Nov-2020";
                toolbar.setTitle(current);
                checkproducts(current);
                break;
            case 12:
                current = "Dec-2020";
                toolbar.setTitle(current);
                checkproducts(current);
                break;
        }
    }

    private void checkproducts(String current) {
        ArrayList<OrderModel> currentmonthlist=new ArrayList<>();

        int total=0;
        currentmonthlist.clear();
        for(int i=0;i<orderModelArrayList.size();i++){
            if(orderModelArrayList.get(i).getOrderdate().contains(current)){
                currentmonthlist.add(orderModelArrayList.get(i));
                total= total+Integer.parseInt(orderModelArrayList.get(i).getFinalamount());
            }
        }
        totalproducts.setText(""+currentmonthlist.size());
        totalamount.setText(""+total+" Rs");
    }

    private void getOrderDetails() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderModelArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.child("Orders").getChildren()){
                    OrderModel orderModel=dataSnapshot1.getValue(OrderModel.class);
                    orderModelArrayList.add(orderModel);
                }
                progressDialog.dismiss();
                getMonth(orderModelArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}