package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.artofgifting.Adapters.HomeSubCategoryRecyclerAdapter;
import com.example.artofgifting.Adapters.ProductRecyclerAdapter;
import com.example.artofgifting.Helper.NetworkHelper;
import com.example.artofgifting.Models.ProductModel;
import com.example.artofgifting.Models.SubCategoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllSubCatActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView categoryproductsrecyclerview, subcategory;

    //vars
    ArrayList<SubCategoryModel> subcatModelArrayList = new ArrayList<>();
    ArrayList<ProductModel> productModelArrayList=new ArrayList<>();

    //firebase
    DatabaseReference myRef=FirebaseDatabase.getInstance().getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper=new NetworkHelper(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_sub_cat);
        String categoryname = getIntent().getStringExtra("HORIZONTALCATNAME");
        String categorykey=getIntent().getStringExtra("HORIZONTALCATKEY");
        categoryproductsrecyclerview = findViewById(R.id.user_recycler_categoryproduct_viewallsubcat_activity);
        subcategory = findViewById(R.id.user_recycler_subcategory_viewallsubcat_activity);

        inittoolbar(categoryname);
        catproducts(categoryname,categorykey);
    }

    private void inittoolbar(String categoryname) {
        toolbar = findViewById(R.id.user_viewallsubcat_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(categoryname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void catproducts(final String catname,final String categorykey) {
        subcategory.setLayoutManager(new GridLayoutManager(ViewAllSubCatActivity.this, 2, GridLayoutManager.VERTICAL, false));
        subcategory.setHasFixedSize(true);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    subcatModelArrayList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").child(categorykey).getChildren()) {
                        if (dataSnapshot1.hasChildren()) {
                            SubCategoryModel subcatModel = dataSnapshot1.getValue(SubCategoryModel.class);
                            subcatModelArrayList.add(subcatModel);
                        }
                    }
                    HomeSubCategoryRecyclerAdapter subCategoryRecyclerAdapter1 = new HomeSubCategoryRecyclerAdapter(ViewAllSubCatActivity.this,subcatModelArrayList,null,subcatModelArrayList.size());
                    subcategory.setAdapter(subCategoryRecyclerAdapter1);
                    subCategoryRecyclerAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        categoryproductsrecyclerview.setLayoutManager(new GridLayoutManager(ViewAllSubCatActivity.this, 2, RecyclerView.VERTICAL, false));
        categoryproductsrecyclerview.setHasFixedSize(true);
        productModelArrayList = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference("AllProducts")
                .orderByChild("category")
                .equalTo(catname);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productModelArrayList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                        productModelArrayList.add(productModel);
                    }
                    ProductRecyclerAdapter productAdapter = new ProductRecyclerAdapter(ViewAllSubCatActivity.this, productModelArrayList,productModelArrayList.size(),0);
                    categoryproductsrecyclerview.setAdapter(productAdapter);
                    productAdapter.OnItemClick(new ProductRecyclerAdapter.OnItemCLickListener() {

                        @Override
                        public void OnItemClickListener(int position) {
                            startActivity(new Intent(ViewAllSubCatActivity.this,SelectedProductActivity.class)
                            .putExtra("PROD_KEY",productModelArrayList.get(position).getKey()));
                            myRef.child("Users").child(user.getUid()).child("RecentViewed").child(productModelArrayList.get(position).getKey()).setValue(productModelArrayList.get(position).getKey());

                        }

                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
