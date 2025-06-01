package com.example.artofgifting.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.artofgifting.Models.ReviewModel;
import com.example.artofgifting.R;
import com.example.artofgifting.SelectedProductActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyReviewsAdapter extends RecyclerView.Adapter<MyReviewsAdapter.ReviewHolder> {

    Context context;
    ArrayList<ReviewModel> reviewModelArrayList;
    ArrayList<String> prodkeylist;
    private OnItemClickListener mlistener;

    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public MyReviewsAdapter(Context context, ArrayList<ReviewModel> reviewModelArrayList, ArrayList<String> prodkeylist) {
        this.context = context;
        this.reviewModelArrayList = reviewModelArrayList;
        this.prodkeylist = prodkeylist;
    }

    public interface OnItemClickListener {
        void OnEditClick(int position);

        void OnDeleteClick(int position);
    }

    public void OnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.raw_review_list, parent, false);
        return new ReviewHolder(v, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewHolder holder, final int position) {
        final Picasso picasso = null;

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageurl = dataSnapshot.child("AllProducts").child(prodkeylist.get(position)).child("imageurl").getValue(String.class);
                String prodname = dataSnapshot.child("AllProducts").child(prodkeylist.get(position)).child("name").getValue(String.class);
                picasso.get().load(imageurl).into(holder.circleImageView);
                holder.username.setText(prodname);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (reviewModelArrayList.get(position).getComment().isEmpty()) {
            holder.comment.setVisibility(View.GONE);
        } else {
            holder.comment.setText(reviewModelArrayList.get(position).getComment());
        }

        holder.date.setText(reviewModelArrayList.get(position).getDate());
        holder.ratingBar.setRating(Float.parseFloat(reviewModelArrayList.get(position).getRating()));

        Query query = myRef.child("Users").child(reviewModelArrayList.get(position).getUser_key()).child("Orders").orderByChild("prod_key").equalTo(prodkeylist.get(position));
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
        return prodkeylist.size();
    }

    private void editingcomment(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View rootview = LayoutInflater.from(context).inflate(R.layout.layout_rating, null);
        builder.setView(rootview);
        final AlertDialog dialog = builder.create();
        dialog.show();
        Button submit;
        RatingBar ratingBar;
        final EditText comment;
        submit = rootview.findViewById(R.id.user_btn_submit_rating_selected_product);
        ratingBar = rootview.findViewById(R.id.user_give_rating_selected_product);
        comment = rootview.findViewById(R.id.user_edt_comment_selected_product);
        final int[] rate = new int[1];
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate[0] = (int) rating;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mcomment = comment.getText().toString().trim();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");

                String formattedDate = df.format(c).trim();
                myRef.child("Rating").child(prodkeylist.get(position)).child(user.getUid()).child("comment").setValue(mcomment);
                myRef.child("Rating").child(prodkeylist.get(position)).child(user.getUid()).child("date").setValue(formattedDate);
                myRef.child("Rating").child(prodkeylist.get(position)).child(user.getUid()).child("rating").setValue(String.valueOf(rate[0]));

                dialog.dismiss();
                notifyDataSetChanged();
            }
        });
    }


    public class ReviewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        Button edit, delete;
        TextView username, verify, comment, date;
        RatingBar ratingBar;

        public ReviewHolder(@NonNull View itemView, final OnItemClickListener mlistener) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.cirimg_userprofile_raw_reviewlist);
            edit = itemView.findViewById(R.id.btn_edit_raw_reviewlist);
            delete = itemView.findViewById(R.id.btn_delete_raw_reviewlist);
            username = itemView.findViewById(R.id.tv_user_name_raw_reviewlist);
            date = itemView.findViewById(R.id.tv_date_raw_reviewlist);
            verify = itemView.findViewById(R.id.tv_verified_purchase_raw_reviewlist);
            comment = itemView.findViewById(R.id.tv_comment_raw_reviewlist);
            ratingBar = itemView.findViewById(R.id.ratingbar_raw_reviewlist);
            edit.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editingcomment(getAdapterPosition());

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myRef.child("Rating").child(prodkeylist.get(getAdapterPosition())).child(user.getUid()).removeValue();
                    myRef.child("Users").child(user.getUid()).child("Rated Products").child(prodkeylist.get(getAdapterPosition())).removeValue();
                    prodkeylist.remove(getAdapterPosition());
                    reviewModelArrayList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SelectedProductActivity.class)
                    .putExtra("PROD_KEY",prodkeylist.get(getAdapterPosition())));
//                    ((Activity)context).finish();

                }
            });
        }
    }
}
