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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductsOutOfStockActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    //vars
    ArrayList<AddProductModel> addProductModelArrayList=new ArrayList<>();
    //firebase
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_out_of_stock);
        inittoolbar();
        recyclerView=findViewById(R.id.admin_recycler_out_of_stock);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query=FirebaseDatabase.getInstance().getReference("AllProducts")
                .orderByChild("quantity")
                .equalTo("0");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addProductModelArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    AddProductModel addProductModel=dataSnapshot1.getValue(AddProductModel.class);
                    addProductModelArrayList.add(addProductModel);
                }
                HorizontalProductDisplayAdapter horizontalProductDisplayAdapter=new HorizontalProductDisplayAdapter(ProductsOutOfStockActivity.this,addProductModelArrayList);
                recyclerView.setAdapter(horizontalProductDisplayAdapter);
                horizontalProductDisplayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_product_out_of_stock_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Reports");
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
