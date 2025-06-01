package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.admin.Adapter.RecyclerUserAdapter;
import com.example.admin.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersWithMostBuysActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    //vars
    ArrayList<UserModel> userlist=new ArrayList<>();

    //firebase
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_with_most_buys);
        inittoolbar();
        recyclerView=findViewById(R.id.admin_recycler_users_with_most_buys);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.child("Users").getChildren()){
                    if(dataSnapshot1.hasChild("Orders")){
                        UserModel userModel=dataSnapshot1.getValue(UserModel.class);
                        userlist.add(userModel);
                    }
                }
                RecyclerUserAdapter recyclerUserAdapter=new RecyclerUserAdapter(UsersWithMostBuysActivity.this,userlist);
                recyclerView.setAdapter(recyclerUserAdapter);
                recyclerUserAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_users_with_most_buys_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Users with Orders");
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
