package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.Adapter.ViewOrdersRecyclerAdapter;
import com.example.admin.Model.OrderModel;
import com.example.admin.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {


    //views
    CircleImageView profileimage;
    TextView user_name, user_dob, user_contact, user_address, user_orders;
    Toolbar toolbar;
    RecyclerView recyclerView;

    //vars
    Picasso picasso;
    ViewOrdersRecyclerAdapter viewOrdersRecyclerAdapter;
    ArrayList<OrderModel> orderlist = new ArrayList<>();
    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final String user_key = getIntent().getStringExtra("USER_KEY");
        inittoolbar();

        profileimage = findViewById(R.id.admin_circleimage_user_image_userprofile);
        user_name = findViewById(R.id.admin_tv_user_name_userprofile);
        user_dob = findViewById(R.id.admin_tv_user_dob_userprofile);
        user_contact = findViewById(R.id.admin_tv_user_contact_userprofile);
        user_orders = findViewById(R.id.admin_tv_orders_userprofile);
        user_address = findViewById(R.id.admin_tv_user_address_userprofile);
        recyclerView = findViewById(R.id.admin_recycler_orders_userprofile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initdetails(user_key);
        initrecyclerview(user_key);
        checkdata();
    }

    private void checkdata() {
        if (orderlist.isEmpty()) {
            user_orders.setVisibility(View.VISIBLE);
        } else {
            user_orders.setVisibility(View.INVISIBLE);
        }
    }

    private void initrecyclerview(final String user_key) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user_key).child("Orders").exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(user_key).child("Orders").getChildren()) {
                        OrderModel orderModel = dataSnapshot1.getValue(OrderModel.class);
                        orderlist.add(orderModel);
                        checkdata();
                    }
                } else {
                    orderlist.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = FirebaseDatabase.getInstance().getReference("Orders")
                .orderByChild("user_key")
                .equalTo(user_key);
        orderlist.clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Orders").getChildren()) {
                        OrderModel orderModel = dataSnapshot1.getValue(OrderModel.class);
                        orderlist.add(orderModel);
                    }
                    viewOrdersRecyclerAdapter = new ViewOrdersRecyclerAdapter(UserProfileActivity.this, orderlist);
                    recyclerView.setAdapter(viewOrdersRecyclerAdapter);
                    viewOrdersRecyclerAdapter.notifyDataSetChanged();
                } else {
                    orderlist.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initdetails(final String user_key) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = dataSnapshot.child("Users").child(user_key).getValue(UserModel.class);
                if (dataSnapshot.child("Users").child(user_key).child("address").exists()) {
                    String address = dataSnapshot.child("Users").child(user_key).child("address").getValue(String.class);
                    user_address.setText(address);
                    user_address.setTextColor(Color.BLACK);
                } else {
                    user_address.setText("Currently not provided by User");
                    user_address.setTextColor(Color.RED);
                }
                picasso.get().load(userModel.getImage_url()).into(profileimage);
                user_name.setText(userModel.getFullname());
                user_dob.setText(userModel.getDob());
                user_contact.setText(userModel.getContact());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_userprofile_toolbar);
        toolbar.setTitle("User Profile");

    }
}
