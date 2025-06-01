package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.artofgifting.Adapters.ProductRecyclerAdapter;
import com.example.artofgifting.Adapters.VerticalRecyclerAdapter;
import com.example.artofgifting.Helper.NetworkHelper;
import com.example.artofgifting.Models.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentlyViewedActivity extends AppCompatActivity {
    //views
    RecyclerView recentproducts;
    Toolbar toolbar;
    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //vars
    ArrayList<String> recentkeyarraylist = new ArrayList<>();
    VerticalRecyclerAdapter verticalRecyclerAdapter;
//    ProductRecyclerAdapter productRecyclerAdapter;
    ArrayList<ProductModel> productrecentarraylist = new ArrayList<>();
    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper=new NetworkHelper(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_viewed);
        recentproducts = findViewById(R.id.user_recyclerview_recentproducts_RecentlyViewed);
        toolbar=findViewById(R.id.user_recentlyviewed_toolbar);
        recentproducts.setLayoutManager(new LinearLayoutManager(RecentlyViewedActivity.this));

        inittoolbar();
        initrecentitems();
    }

    private void inittoolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Recently Viewed");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initrecentitems() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user.getUid()).child("RecentViewed").exists()) {
                    recentkeyarraylist.clear();
                    productrecentarraylist.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(user.getUid()).child("RecentViewed").getChildren()) {
                        String prodkey = dataSnapshot1.getKey();
                        recentkeyarraylist.add(prodkey);
                    }
                }
                for (int i = 0; i < recentkeyarraylist.size(); i++) {
                    ProductModel productModel = dataSnapshot.child("AllProducts").child(recentkeyarraylist.get(i)).getValue(ProductModel.class);
                    productrecentarraylist.add(productModel);
                }
                verticalRecyclerAdapter = new VerticalRecyclerAdapter(RecentlyViewedActivity.this, productrecentarraylist);
                recentproducts.setAdapter(verticalRecyclerAdapter);
                verticalRecyclerAdapter.notifyDataSetChanged();
//                productRecyclerAdapter=new ProductRecyclerAdapter(RecentlyViewedActivity.this,productrecentarraylist,productrecentarraylist.size(),1);
//                recentproducts.setAdapter(productRecyclerAdapter);
//                productRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recent_viewed_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.user_cleardata_recentviewed:
                productrecentarraylist.clear();
                recentkeyarraylist.clear();
                myRef.child("Users").child(user.getUid()).child("RecentViewed").removeValue();
                verticalRecyclerAdapter.notifyDataSetChanged();
//                productRecyclerAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}
