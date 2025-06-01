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

import com.example.admin.Adapter.ReviewRecyclerAdapter;
import com.example.admin.Model.AddProductModel;
import com.example.admin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Review_Fragment extends Fragment {
    //vars
    RecyclerView recyclerView;
    ArrayList<AddProductModel> addProductModelArrayList=new ArrayList<>();
    ReviewRecyclerAdapter reviewRecyclerAdapter;

    //firebase
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.review_fragment, null);

        recyclerView=v.findViewById(R.id.admin_recycler_reviews_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                addProductModelArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.child("AllProducts").getChildren()){
                    AddProductModel addProductModel=dataSnapshot1.getValue(AddProductModel.class);
                    addProductModelArrayList.add(addProductModel);
                }
                reviewRecyclerAdapter=new ReviewRecyclerAdapter(getActivity(),addProductModelArrayList);
                recyclerView.setAdapter(reviewRecyclerAdapter);
                reviewRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }
}
