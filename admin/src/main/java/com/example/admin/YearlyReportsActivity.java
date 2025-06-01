package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.Model.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YearlyReportsActivity extends AppCompatActivity {

    //views
    Spinner spinner;
    Toolbar toolbar;
    TextView totalproducts,totalamount;


    //vars
    String year[]={"--Select Year--","2020"};
    ArrayList<OrderModel> orderModelArrayList=new ArrayList<>();

    //Firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_reports);
        spinner=findViewById(R.id.admin_spinner_select_year);
        totalproducts=findViewById(R.id.admin_tv_total_product_yearly_reports);
        totalamount=findViewById(R.id.admin_tv_total_amount_yearly_reports);
        inittoolbar();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(YearlyReportsActivity.this, android.R.layout.simple_list_item_1, year);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    Toast.makeText(YearlyReportsActivity.this, "Select a Year", Toast.LENGTH_SHORT).show();
                }else {
                    String year=spinner.getSelectedItem().toString();
                    getOrderDetails(year);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getOrderDetails(final String year) {
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
                getYear(orderModelArrayList,year);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getYear(ArrayList<OrderModel> orderModelArrayList, String year) {
        ArrayList<OrderModel> currentmonthlist=new ArrayList<>();

        int total=0;
        currentmonthlist.clear();
        for(int i=0;i<orderModelArrayList.size();i++){
            if(orderModelArrayList.get(i).getOrderdate().contains(year)){
                currentmonthlist.add(orderModelArrayList.get(i));
                total= total+Integer.parseInt(currentmonthlist.get(i).getFinalamount());
            }
        }
        String grandtotal = String.valueOf(total);
        int size = grandtotal.length();
        if (size > 3 && size <= 5) {
            String grand = grandtotal.substring(0, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("Total Amount accumalated in "+year+":\n ₹ " + grand);
        }
        if (size > 5) {
            String grand = grandtotal.substring(0, size - 5) + "," + grandtotal.substring(size - 5, size - 3) + "," + grandtotal.substring(size - 3);
            totalamount.setText("Total Amount accumalated in "+year+":\n ₹ " + grand);

        }
        if (size <= 3) {
            String grand = grandtotal;
            totalamount.setText("Total Amount accumalated in "+year+":\n ₹ " + grand);
        }
        totalproducts.setText("Total Products sold in "+year+" : \n"+currentmonthlist.size());
    }


    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_yearly_reports_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Yearly Reports");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}