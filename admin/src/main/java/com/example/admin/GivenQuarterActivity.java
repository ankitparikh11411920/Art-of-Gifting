package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class GivenQuarterActivity extends AppCompatActivity {

    //vars
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    String month1;
    String month2;
    String month3;

    //views
    Toolbar toolbar;
    TextView totalproduct, totalamount;

    //Firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_given_quarter);
        totalproduct = findViewById(R.id.admin_given_quarter_totalproducts);
        totalamount = findViewById(R.id.admin_given_quarter_totalamount);


        inittoolbar();
        String quarter = getIntent().getStringExtra("quarter");
        getOrderDetails(quarter);

    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_given_quarter_toolbar);
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

    private void getOrderDetails(final String quarter) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Orders").getChildren()) {
                    OrderModel orderModel = dataSnapshot1.getValue(OrderModel.class);
                    orderModelArrayList.add(orderModel);
                }
                if (quarter.equals("q1")) {
                    toolbar.setTitle("Q1 2020");
                    q1();
                } else if (quarter.equals("q2")) {
                    toolbar.setTitle("Q2 2020");
                    q2();

                } else if (quarter.equals("q3")) {
                    toolbar.setTitle("Q3 2020");
                    q3();

                } else if (quarter.equals("q4")) {
                    toolbar.setTitle("Q4 2020");
                    q4();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void q4() {
//        getOrderDetails();
        month1 = "Oct-2020";
        month2 = "Nov-2020";
        month3 = "Dec-2020";
        ArrayList<OrderModel> currentmonthlist = new ArrayList<>();

//        orderModelArrayList = getOrderDetails();
        int total = 0;
        currentmonthlist.clear();
        for (int i = 0; i < orderModelArrayList.size(); i++) {
            if (orderModelArrayList.get(i).getOrderdate().contains(month1) || orderModelArrayList.get(i).getOrderdate().contains(month2) || orderModelArrayList.get(i).getOrderdate().contains(month3)) {
                currentmonthlist.add(orderModelArrayList.get(i));
                total = total + Integer.parseInt(currentmonthlist.get(i).getFinalamount());
            }
        }
        String grandtotal = String.valueOf(total);
        int size = grandtotal.length();
        if (size > 3 && size <= 5) {
            String grand = grandtotal.substring(0, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("₹ "+grand);
        }
        if (size > 5) {
            String grand = grandtotal.substring(0, size - 5) + "," + grandtotal.substring(size - 5, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("₹ "+grand);

        }
        if (size <= 3) {
            String grand = grandtotal;
            totalamount.setText("₹ "+grand);
        }
        totalproduct.setText("" + currentmonthlist.size());

    }

    private void q3() {
//        getOrderDetails();
        month1 = "Jul-2020";
        month2 = "Aug-2020";
        month3 = "Sep-2020";
        ArrayList<OrderModel> currentmonthlist = new ArrayList<>();

//        orderModelArrayList = getOrderDetails();
        int total = 0;
        currentmonthlist.clear();
        for (int i = 0; i < orderModelArrayList.size(); i++) {
            if (orderModelArrayList.get(i).getOrderdate().contains(month1) || orderModelArrayList.get(i).getOrderdate().contains(month2) || orderModelArrayList.get(i).getOrderdate().contains(month3)) {
                currentmonthlist.add(orderModelArrayList.get(i));
                total = total + Integer.parseInt(currentmonthlist.get(i).getFinalamount());
            }
        }
        String grandtotal = String.valueOf(total);
        int size = grandtotal.length();
        if (size > 3 && size <= 5) {
            String grand = grandtotal.substring(0, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("₹ "+grand);
        }
        if (size > 5) {
            String grand = grandtotal.substring(0, size - 5) + "," + grandtotal.substring(size - 5, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("₹ "+grand);

        }
        if (size <= 3) {
            String grand = grandtotal;
            totalamount.setText("₹ "+grand);
        }
        totalproduct.setText("" + currentmonthlist.size());
    }

    private void q2() {
//        getOrderDetails();
        month1 = "Apr-2020";
        month2 = "May-2020";
        month3 = "Jun-2020";
        ArrayList<OrderModel> currentmonthlist = new ArrayList<>();

//        orderModelArrayList = getOrderDetails();
        int total = 0;
        currentmonthlist.clear();
        for (int i = 0; i < orderModelArrayList.size(); i++) {
            if (orderModelArrayList.get(i).getOrderdate().contains(month1) || orderModelArrayList.get(i).getOrderdate().contains(month2) || orderModelArrayList.get(i).getOrderdate().contains(month3)) {
                currentmonthlist.add(orderModelArrayList.get(i));
                total = total + Integer.parseInt(currentmonthlist.get(i).getFinalamount());
            }
        }
        String grandtotal = String.valueOf(total);
        int size = grandtotal.length();
        if (size > 3 && size <= 5) {
            String grand = grandtotal.substring(0, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("₹ "+grand);
        }
        if (size > 5) {
            String grand = grandtotal.substring(0, size - 5) + "," + grandtotal.substring(size - 5, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("₹ "+grand);

        }
        if (size <= 3) {
            String grand = grandtotal;
            totalamount.setText("₹ "+grand);
        }
        totalproduct.setText("" + currentmonthlist.size());
    }

    private void q1() {
        month1 = "Jan-2020";
        month2 = "Feb-2020";
        month3 = "Mar-2020";
        ArrayList<OrderModel> currentmonthlist = new ArrayList<>();

//        orderModelArrayList = getOrderDetails();
        int total = 0;
        currentmonthlist.clear();
        for (int i = 0; i < orderModelArrayList.size(); i++) {
            if (orderModelArrayList.get(i).getOrderdate().contains(month1) || orderModelArrayList.get(i).getOrderdate().contains(month2) || orderModelArrayList.get(i).getOrderdate().contains(month3)) {
                currentmonthlist.add(orderModelArrayList.get(i));
                total = total + Integer.parseInt(currentmonthlist.get(i).getFinalamount());
            }
        }
        String grandtotal = String.valueOf(total);
        int size = grandtotal.length();
        if (size > 3 && size <= 5) {
            String grand = grandtotal.substring(0, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("₹ "+grand);
        }
        if (size > 5) {
            String grand = grandtotal.substring(0, size - 5) + "," + grandtotal.substring(size - 5, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("₹ "+grand);

        }
        if (size <= 3) {
            String grand = grandtotal;
            totalamount.setText("₹ "+grand);
        }
        totalproduct.setText("" + currentmonthlist.size());

    }
}