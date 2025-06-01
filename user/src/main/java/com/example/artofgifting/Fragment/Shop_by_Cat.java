package com.example.artofgifting.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Adapters.CategoryRecyclerAdapter_ShopByCat;
import com.example.artofgifting.Models.CategoryModel;
import com.example.artofgifting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Shop_by_Cat extends Fragment {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    CategoryRecyclerAdapter_ShopByCat categoryRecyclerAdapter;

    //firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef= database.getReference();

    ArrayList<CategoryModel> categoryArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.shopbycat_fragment, container, false);

        recyclerView=rootview.findViewById(R.id.user_recyclerview_cat_disp);

        toolbar=getActivity().findViewById(R.id.home_toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        upbutton();
        initrecyclerview();


        return rootview;
    }

    private void initrecyclerview() {
        categoryArrayList=new ArrayList<>();
        recyclerView.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryArrayList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").getChildren()) {
                    CategoryModel categoryModel=dataSnapshot1.getValue(CategoryModel.class);
                    String dataModel=dataSnapshot1.getKey();
                    categoryArrayList.add(categoryModel);
                    /*categoryRecyclerAdapter=new CategoryRecyclerAdapter(getActivity(),stringArrayList);
                    recyclerView.setAdapter(categoryRecyclerAdapter);*/

                }
                categoryRecyclerAdapter=new CategoryRecyclerAdapter_ShopByCat(getActivity(),categoryArrayList);
                recyclerView.setAdapter(categoryRecyclerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void upbutton() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
//                getFragmentManager().popBackStack();
            }
        });
    }
}
