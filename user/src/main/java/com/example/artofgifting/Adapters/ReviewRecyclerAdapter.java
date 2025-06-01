package com.example.artofgifting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.ProductModel;
import com.example.artofgifting.Models.ReviewModel;
import com.example.artofgifting.R;
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

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewHolder> {


    ArrayList<ReviewModel> reviewModelArrayList;
    Context context;
    ArrayList<String> prodkeylist;
    OnClickListener mlistener;

    //firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public interface OnClickListener {
        void OnEditClick(int position);

        void OnDeleteClick(int position);
    }

    public void OnItemClickListener(OnClickListener listener) {
        mlistener = listener;
    }

    public ReviewRecyclerAdapter(ArrayList<ReviewModel> reviewModelArrayList, Context context, ArrayList<String> prodkeylist) {
        this.reviewModelArrayList = reviewModelArrayList;
        this.context = context;
        this.prodkeylist = prodkeylist;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.raw_review_list, parent, false);
        return new ReviewHolder(v, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewHolder holder, final int position) {
        Picasso picasso = null;

        picasso.get().load(reviewModelArrayList.get(position).getImgurl()).into(holder.circleImageView);
        holder.username.setText(reviewModelArrayList.get(position).getUsername());
        if (reviewModelArrayList.get(position).getUser_key().equals(user.getUid())) {
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }

        if (reviewModelArrayList.get(position).getComment().isEmpty()) {
            holder.comment.setVisibility(View.GONE);
        } else {
            holder.comment.setText(reviewModelArrayList.get(position).getComment());
        }
        holder.date.setText(reviewModelArrayList.get(position).getDate());
        holder.ratingBar.setRating(Float.parseFloat(reviewModelArrayList.get(position).getRating()));


            Query query = myRef.child("Users").child(reviewModelArrayList.get(position).getUser_key()).child("Orders").orderByChild("prod_key").equalTo(prodkeylist.get(0));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        holder.verify.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }


    @Override
    public int getItemCount() {
            return reviewModelArrayList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        Button edit, delete;
        TextView username, verify, comment, date;
        RatingBar ratingBar;

        public ReviewHolder(@NonNull View itemView, final OnClickListener mlistener) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.cirimg_userprofile_raw_reviewlist);
            edit = itemView.findViewById(R.id.btn_edit_raw_reviewlist);
            delete = itemView.findViewById(R.id.btn_delete_raw_reviewlist);
            username = itemView.findViewById(R.id.tv_user_name_raw_reviewlist);
            date = itemView.findViewById(R.id.tv_date_raw_reviewlist);
            verify = itemView.findViewById(R.id.tv_verified_purchase_raw_reviewlist);
            comment = itemView.findViewById(R.id.tv_comment_raw_reviewlist);
            ratingBar = itemView.findViewById(R.id.ratingbar_raw_reviewlist);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.OnEditClick(position);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.OnDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
