package com.example.artofgifting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.ProductModel;
import com.example.artofgifting.R;
import com.example.artofgifting.SelectedProductActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VerticalRecyclerAdapter extends RecyclerView.Adapter<VerticalRecyclerAdapter.VerticalHolder> {
    private Context context;
    private ArrayList<ProductModel> productrecentArrayList;

    public VerticalRecyclerAdapter(Context context, ArrayList<ProductModel> productrecentArrayList) {
        this.context = context;
        this.productrecentArrayList=productrecentArrayList;
    }

    @NonNull
    @Override
    public VerticalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.raw_recent_products,parent,false);
        return new VerticalHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VerticalHolder holder, final int position) {

        holder.shimmerFrameLayout.startShimmer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso picasso=null;
                picasso.get().load(productrecentArrayList.get(position).getImageurl()).into(holder.prodimage);
                holder.prodname.setText(productrecentArrayList.get(position).getName());
                holder.prodprice.setText("₹ "+productrecentArrayList.get(position).getPrice());
                holder.shimmerFrameLayout.setVisibility(View.GONE);
            }
        },1000);

    }

    @Override
    public int getItemCount() {
        return productrecentArrayList.size();
    }

    public class VerticalHolder extends RecyclerView.ViewHolder {
        ImageView prodimage;
        TextView prodname,prodprice;
        ShimmerFrameLayout shimmerFrameLayout;
        public VerticalHolder(@NonNull View itemView) {
            super(itemView);
            prodimage=itemView.findViewById(R.id.user_imageview_prod_disp_recent_item);
            shimmerFrameLayout=itemView.findViewById(R.id.user_shimmer_raw_recent_item);
            prodname=itemView.findViewById(R.id.user_tv_prodname_recent_item);
            prodprice=itemView.findViewById(R.id.user_tv_prodprice_recent_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SelectedProductActivity.class)
                    .putExtra("PROD_KEY",productrecentArrayList.get(getAdapterPosition()).getKey()));
                }
            });
        }
    }
}
