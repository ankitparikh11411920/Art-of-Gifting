package com.example.admin.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Adapter.ViewOrdersRecyclerAdapter;
import com.example.admin.Model.OrderModel;
import com.example.admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Order_Fragment extends Fragment {
    //views
    RecyclerView recyclerView;

    //vars
    ViewOrdersRecyclerAdapter viewOrdersRecyclerAdapter;
    ArrayList<OrderModel> orderlist=new ArrayList<>();

    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.order_fragment, null);

        recyclerView = v.findViewById(R.id.admin_recycler_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderlist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Orders").getChildren()) {
                    OrderModel orderModel=dataSnapshot1.getValue(OrderModel.class);
                    orderlist.add(orderModel);
                }
                viewOrdersRecyclerAdapter=new ViewOrdersRecyclerAdapter(getActivity(),orderlist);
                recyclerView.setAdapter(viewOrdersRecyclerAdapter);
                viewOrdersRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }
}
