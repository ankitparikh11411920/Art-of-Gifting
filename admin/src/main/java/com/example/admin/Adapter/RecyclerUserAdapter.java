package com.example.admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Model.UserModel;
import com.example.admin.R;
import com.example.admin.UserProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerUserAdapter extends RecyclerView.Adapter<RecyclerUserAdapter.UserHolder> {
    Context context;
    ArrayList<UserModel> userModelArrayList;

    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    public RecyclerUserAdapter(Context context, ArrayList<UserModel> userModelArrayList) {
        this.context = context;
        this.userModelArrayList = userModelArrayList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.raw_user_recycle, parent, false);
        return new UserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {
        final Picasso picasso = null;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(userModelArrayList.get(position).getUserkey()).child("Orders").exists()) {
                    int totalorders = (int)dataSnapshot.child("Users").child(userModelArrayList.get(position).getUserkey()).child("Orders").getChildrenCount();


                    holder.totalorders.setText("Total Orders : "+totalorders);
                }else {
                    holder.totalorders.setText("Total Orders : 0");
                }
                holder.username.setText(userModelArrayList.get(position).getFullname());
                picasso.get().load(userModelArrayList.get(position).getImage_url()).into(holder.circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView username,totalorders;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.admin_user_image_disp);
            username = itemView.findViewById(R.id.admin_user_name_disp);
            totalorders=itemView.findViewById(R.id.admin_user_total_orders_disp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, UserProfileActivity.class)
                            .putExtra("USER_KEY", userModelArrayList.get(getAdapterPosition()).getUserkey()));
                }
            });
        }
    }
}
