package com.example.admin.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Model.AddProductModel;
import com.example.admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ReviewHolder> {
    private Context context;
    private ArrayList<AddProductModel> addProductModelArrayList;
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();

    public ReviewRecyclerAdapter(Context context, ArrayList<AddProductModel> addProductModelArrayList) {
        this.context = context;
        this.addProductModelArrayList = addProductModelArrayList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.raw_review_item,parent,false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewHolder holder, final int position) {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final int stars[] = new int[5];
                for (int i = 0; i < 5; i++) {
                    stars[i] = (int) dataSnapshot.child("Ratings").child(addProductModelArrayList.get(position).getKey()).child(String.valueOf(i + 1)).getChildrenCount();
                }
                int totalstars = stars[0] + stars[1] + stars[2] + stars[3] + stars[4];
                Picasso picasso=null;
                picasso.get().load(addProductModelArrayList.get(position).getImageurl()).into(holder.prodimage);
                holder.prodname.setText(addProductModelArrayList.get(position).getName());
                holder.prodprice.setText("₹ "+addProductModelArrayList.get(position).getPrice());
                holder.prodreview.setText("Total Reviews : "+totalstars);
                holder.prodqty.setText("Qty : "+addProductModelArrayList.get(position).getQuantity());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*private void ratingstars(int[] stars) {
        float totalstars = stars[0] + stars[1] + stars[2] + stars[3] + stars[4];
        if (totalstars == 0) {
            totalratings.setText("0 Ratings");
            total1stars.setText("0");
            total2stars.setText("0");
            total3stars.setText("0");
            total4stars.setText("0");
            total5stars.setText("0");
            averagerating.setText("0");
        } else {
            totalratings.setText(totalstars + " Ratings");
            total1stars.setText("" + stars[0]);
            total2stars.setText("" + stars[1]);
            total3stars.setText("" + stars[2]);
            total4stars.setText("" + stars[3]);
            total5stars.setText("" + stars[4]);

            float average = (((5 * stars[4]) + (4 * stars[3]) + (3 * stars[2]) + (2 * stars[1]) + (stars[0])) / totalstars);
            String float_average = String.format("%.1f", average);
            averagerating.setText(float_average);
            float progress1 = (stars[0] / totalstars) * 100;
            float progress2 = (stars[1] / totalstars) * 100;
            float progress3 = (stars[2] / totalstars) * 100;
            float progress4 = (stars[3] / totalstars) * 100;
            float progress5 = (stars[4] / totalstars) * 100;

            progressBar1stars.setProgress((int) progress1);
            progressBar2stars.setProgress((int) progress2);
            progressBar3stars.setProgress((int) progress3);
            progressBar4stars.setProgress((int) progress4);
            progressBar5stars.setProgress((int) progress5);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressBar1stars.setProgressTintList(ColorStateList.valueOf(Color.RED));
                progressBar2stars.setProgressTintList(ColorStateList.valueOf(Color.RED));
                progressBar3stars.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                progressBar4stars.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                progressBar5stars.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

            }
        }
    }
*/
    @Override
    public int getItemCount() {
        return addProductModelArrayList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        ImageView prodimage;
        TextView prodname,prodprice,prodqty,prodreview;
        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            prodimage=itemView.findViewById(R.id.admin_prod_image_review_item);
            prodname=itemView.findViewById(R.id.admin_tv_prod_name_review_item);
            prodprice=itemView.findViewById(R.id.admin_tv_prod_price_review_item);
            prodqty=itemView.findViewById(R.id.admin_tv_prod_qty_review_item);
            prodreview=itemView.findViewById(R.id.admin_tv_totalreview_review_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
