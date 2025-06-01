package com.example.artofgifting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.SubCategoryModel;
import com.example.artofgifting.ProductsActivity;
import com.example.artofgifting.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeSubCategoryRecyclerAdapter extends RecyclerView.Adapter<HomeSubCategoryRecyclerAdapter.HomeSubCategoryHolder> {
    private Context context;
    private ArrayList<SubCategoryModel> subcategoryarraylist;
    private ArrayList<String> catnamearraylist;
    int count;


    public HomeSubCategoryRecyclerAdapter(Context context, ArrayList<SubCategoryModel> subcategoryarraylist, ArrayList<String> catnamearraylist, int count) {
        this.context = context;
        this.subcategoryarraylist = subcategoryarraylist;
        this.catnamearraylist=catnamearraylist;
        this.count=count;
//        this.subCategoryModelArrayList = subCategoryModelArrayList;
    }


    @NonNull
    @Override
    public HomeSubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_subcat_home_disp, null, false);
        return new HomeSubCategoryHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeSubCategoryHolder holder, final int position) {
        holder.shimmerFrameLayout.startShimmer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso picasso = null;
                holder.textView.setText(subcategoryarraylist.get(position).getSubcatname());
                picasso.get().load(subcategoryarraylist.get(position).getSubcatimageurl()).into(holder.imageView);
                holder.shimmerFrameLayout.setVisibility(View.GONE);
                holder.linearLayout.setVisibility(View.GONE);
            }
        }, 1000);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProductsActivity.class)
                .putExtra("subcategoryname",subcategoryarraylist.get(position).getSubcatname()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class HomeSubCategoryHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        ShimmerFrameLayout shimmerFrameLayout;
        LinearLayout linearLayout;

        public HomeSubCategoryHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_user_subcatname_raw_subcat_disp);
            imageView = itemView.findViewById(R.id.img_user_subimage_raw_subcat_disp);
            linearLayout = itemView.findViewById(R.id.linear_raw_subcat_disp);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_raw_subcat_disp);
        }
    }

}
