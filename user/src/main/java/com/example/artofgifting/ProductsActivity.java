package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.artofgifting.Adapters.ProductRecyclerAdapter;
import com.example.artofgifting.Helper.NetworkHelper;
import com.example.artofgifting.Models.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProductsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btnsort;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int sortid = R.id.sort_none_raw_sort_dialog;


    //firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private ProductRecyclerAdapter productRecyclerAdapter;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();

    //vars
    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper=new NetworkHelper(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        //vars
        toolbar = findViewById(R.id.user_products_toolbar);
        recyclerView = findViewById(R.id.user_recyclerview_prod_disp_products_activity);
        recyclerView.setLayoutManager(new GridLayoutManager(ProductsActivity.this, 2));
        btnsort = findViewById(R.id.btnsort_activity_subcatprod);
        settoolbar();
        setproddata();

        recyclerclick();
        sortbuttonAnimation();
        btnsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productModelArrayList.isEmpty()) {
                    StyleableToast.makeText(ProductsActivity.this, "There is no products for sort", R.style.exampletoast).show();
                } else {
                    sort();
                }
            }
        });

    }
    private void sortbuttonAnimation() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        btnsort.clearAnimation();
        btnsort.startAnimation(anim);
    }

    private void sort() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        final View myview = layoutInflater.inflate(R.layout.raw_sort_dialog, null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setTitle("Sort By");
        dialog.setIcon(R.drawable.ic_sort);
        dialog.setView(myview);
        dialog.show();
        radioGroup = myview.findViewById(R.id.sort_group_raw_sort_dialog);
        radioGroup.check(sortid);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedid) {
                radioButton = myview.findViewById(checkedid);
                if (checkedid == R.id.sort_btn_lowtohigh_raw_sort_dialog) {
                    sortpricelowtohigh();
                    sortid = R.id.sort_btn_lowtohigh_raw_sort_dialog;
                    dialog.dismiss();
                } else if (checkedid == R.id.sort_btn_hightolow_raw_sort_dialog) {
                    sortpricehightolow();
                    sortid = R.id.sort_btn_hightolow_raw_sort_dialog;
                    dialog.dismiss();
                }
            }
        });
    }

    private void sortpricehightolow() {
        Collections.sort(productModelArrayList, new Comparator<ProductModel>() {
            @Override
            public int compare(ProductModel productModel, ProductModel t1) {
                return Integer.parseInt(t1.getPrice()) - Integer.parseInt(productModel.getPrice());
            }
        });
        productRecyclerAdapter.notifyDataSetChanged();
    }

    private void sortpricelowtohigh() {
        Collections.sort(productModelArrayList, new Comparator<ProductModel>() {
            @Override
            public int compare(ProductModel productModel, ProductModel t1) {
                return Integer.parseInt(productModel.getPrice()) - Integer.parseInt(t1.getPrice());
            }
        });
        productRecyclerAdapter.notifyDataSetChanged();
    }



    private void settoolbar() {

        final String subcatkey = getIntent().getStringExtra("subcategoryname").trim();
        toolbar.setTitle(subcatkey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setproddata() {

        final String subcatkey = getIntent().getStringExtra("subcategoryname").trim();
        if (subcatkey != null) {
            Query query = FirebaseDatabase.getInstance().getReference("AllProducts")
                    .orderByChild("subcategory")
                    .equalTo(subcatkey);
            query.addValueEventListener(valueEventListener);
        }
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            productModelArrayList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                    productModelArrayList.add(productModel);
                }
                productRecyclerAdapter = new ProductRecyclerAdapter(ProductsActivity.this, productModelArrayList,productModelArrayList.size(),0);
                recyclerView.setAdapter(productRecyclerAdapter);
                productRecyclerAdapter.notifyDataSetChanged();
                productRecyclerAdapter.OnItemClick(new ProductRecyclerAdapter.OnItemCLickListener() {
                    @Override
                    public void OnItemClickListener(int position) {
                        startActivity(new Intent(ProductsActivity.this, SelectedProductActivity.class)
                                .putExtra("PROD_KEY", productModelArrayList.get(position).getKey()));
                        String key = productModelArrayList.get(position).getKey();
//                RecentModel recentModel = new RecentModel();
//                recentModel.setProdkey(key);
                        myRef.child("Users").child(user.getUid()).child("RecentViewed").child(key).setValue(key);
                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void recyclerclick() {

    }
}
