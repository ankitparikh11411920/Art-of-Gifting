package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.artofgifting.Adapters.SearchAdapter;
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

public class SearchlistActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    //vars
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    private SearchAdapter searchAdapter;
    SearchAdapter searchAdapter1;
    //
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper=new NetworkHelper(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlist);

        toolbar = findViewById(R.id.user_searchlist_toolbar);
        recyclerView = findViewById(R.id.user_recyclerview_search_activity);
        searchView = findViewById(R.id.user_searchview_search_activity);
        searchView.requestFocus();

        initrecyclerview();
        initsearch();
        inittoolbar();
    }

    private void initsearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerView.setVisibility(View.INVISIBLE);
                recyclerView.setLayoutManager(new GridLayoutManager(SearchlistActivity.this, 2));
                searchAdapter1.getFilter().filter(query);
                recyclerView.setAdapter(searchAdapter1);
                recyclerView.setVisibility(View.VISIBLE);
                searchView.getWindowToken();
                searchView.clearFocus();
                searchAdapter1.OnItemClick(new SearchAdapter.OnTextClickListener() {
                    @Override
                    public void OnTextClick(int position) {

                    }

                    @Override
                    public void OnItemClick(int position) {
                        startActivity(new Intent(SearchlistActivity.this,SelectedProductActivity.class)
                                .putExtra("PROD_KEY",productModelArrayList.get(position).getKey()));
                        myRef.child("Users").child(user.getUid()).child("RecentViewed").child(productModelArrayList.get(position).getKey()).setValue(productModelArrayList.get(position).getKey());

                        searchView.clearFocus();
                        searchView.setQuery(null,true);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(searchAdapter);
                searchAdapter.getFilter().filter(newText);
                searchAdapter.OnTextClick(new SearchAdapter.OnTextClickListener() {
                    @Override
                    public void OnTextClick(int position) {
                        searchView.setQuery(productModelArrayList.get(position).getName(),true);
                    }

                    @Override
                    public void OnItemClick(int position) {

                    }
                });
                return false;
            }
        });
    }

    private void initrecyclerview() {
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("AllProducts").getChildren()) {
                    ProductModel productModel = dataSnapshot1.getValue(ProductModel.class);
                    productModelArrayList.add(productModel);
                }
                searchAdapter = new SearchAdapter(SearchlistActivity.this, productModelArrayList, 0);
                searchAdapter1 = new SearchAdapter(getApplicationContext(), productModelArrayList, 1);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void inittoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
