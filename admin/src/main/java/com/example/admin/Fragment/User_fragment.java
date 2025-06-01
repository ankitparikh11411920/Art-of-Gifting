package com.example.admin.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Adapter.RecyclerUserAdapter;
import com.example.admin.Model.UserModel;
import com.example.admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User_fragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerUserAdapter useradapter;
    private ArrayList<UserModel> userModelArrayList;

    //firebase
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.users_fragment, container, false);
        recyclerView = v.findViewById(R.id.admin_user_recyclerview);
        progressBar = v.findViewById(R.id.admin_progressbar_users_fragment);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar.setVisibility(View.VISIBLE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModelArrayList = new ArrayList<>();
                userModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").getChildren()) {
                    UserModel userModel = dataSnapshot1.getValue(UserModel.class);
                    TextView textView = v.findViewById(R.id.admin_tv_totalusers_userfragment);
                    userModelArrayList.add(userModel);
                    if (!userModelArrayList.isEmpty()) {
                        useradapter = new RecyclerUserAdapter(getContext(), userModelArrayList);
                        recyclerView.setAdapter(useradapter);
                        useradapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setText("TOTAL USERS : " + userModelArrayList.size());
                    } else {
                        Toast.makeText(getActivity(), "No Users", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }
}
