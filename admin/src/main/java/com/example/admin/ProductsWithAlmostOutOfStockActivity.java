package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.admin.Adapter.HorizontalProductDisplayAdapter;
import com.example.admin.Model.AddProductModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductsWithAlmostOutOfStockActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    //vars
    ArrayList<AddProductModel> productModelArrayList=new ArrayList<>();
    ArrayList<AddProductModel> productalmostoutofstockList=new ArrayList<>();
    HorizontalProductDisplayAdapter horizontalProductDisplayAdapter;

    //firebase
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("AllProducts");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_with_almost_out_of_stock);
        inittoolbar();
        recyclerView=findViewById(R.id.admin_recycler_almost_out_of_stock);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    AddProductModel addProductModel=dataSnapshot1.getValue(AddProductModel.class);
                    productModelArrayList.add(addProductModel);
                }
                for(int i=0;i<productModelArrayList.size();i++){
                    if(Integer.parseInt(productModelArrayList.get(i).getQuantity())<=5 && Integer.parseInt(productModelArrayList.get(i).getQuantity())>0){
                        AddProductModel addProductModel=productModelArrayList.get(i);
                        productalmostoutofstockList.add(addProductModel);
                    }
                }
                horizontalProductDisplayAdapter=new HorizontalProductDisplayAdapter(ProductsWithAlmostOutOfStockActivity.this,productalmostoutofstockList);
                recyclerView.setAdapter(horizontalProductDisplayAdapter);
                horizontalProductDisplayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_product_almost_out_of_stock_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Products almost out of Stock");
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
