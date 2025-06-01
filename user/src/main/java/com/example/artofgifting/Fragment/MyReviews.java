package com.example.artofgifting.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Adapters.MyReviewsAdapter;
import com.example.artofgifting.Adapters.ReviewRecyclerAdapter;
import com.example.artofgifting.Models.ReviewModel;
import com.example.artofgifting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyReviews extends Fragment {
    private Toolbar toolbar;
    RecyclerView review_recycler;

    //vars
    final ArrayList<String> ratedproductkeylist = new ArrayList<>();
    final ArrayList<ReviewModel> reviewModelArrayList = new ArrayList<>();
    MyReviewsAdapter myReviewsAdapter;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.myreviews_fragment, container, false);
        toolbar = getActivity().findViewById(R.id.home_toolbar);
        review_recycler = rootview.findViewById(R.id.user_recycler_myreviews_fragment);
        ((AppCompatActivity) getActivity()).getSupportActionBar();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
//                getFragmentManager().popBackStack();
            }
        });
        initreviewlist();
        return rootview;
    }

    private void initreviewlist() {
        review_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ratedproductkeylist.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(user.getUid()).child("Rated Products").getChildren()) {
                    String productkey = dataSnapshot1.getValue(String.class);
                    ratedproductkeylist.add(productkey);
                }
                reviewModelArrayList.clear();
                for (int i = 0; i < ratedproductkeylist.size(); i++) {
                    final int finalI = i;
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ReviewModel reviewModel = dataSnapshot.child("Rating").child(ratedproductkeylist.get(finalI)).child(user.getUid()).getValue(ReviewModel.class);
                            reviewModelArrayList.add(reviewModel);

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                if (!reviewModelArrayList.isEmpty() && !ratedproductkeylist.isEmpty())//checking if the data is loaded or not
                    {
                    final MyReviewsAdapter myReviewsAdapter = new MyReviewsAdapter(getContext(), reviewModelArrayList, ratedproductkeylist);
                    review_recycler.setAdapter(myReviewsAdapter);
                } else {
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);


    }

}
