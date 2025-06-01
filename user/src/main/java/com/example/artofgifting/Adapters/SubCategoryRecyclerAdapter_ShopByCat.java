package com.example.artofgifting.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.CategoryModel;
import com.example.artofgifting.ProductsActivity;
import com.example.artofgifting.R;

import java.util.ArrayList;

public class SubCategoryRecyclerAdapter_ShopByCat extends RecyclerView.Adapter<SubCategoryRecyclerAdapter_ShopByCat.SubCategoryHolder> {

    Context context;
    ArrayList<String> subcategoryarraylist;
    ArrayList<CategoryModel> categorynamearraylist;

    public SubCategoryRecyclerAdapter_ShopByCat(Context context, ArrayList<String> subcategoryarraylist, ArrayList<CategoryModel> categorynamearraylist
    ) {
        this.context = context;
        this.subcategoryarraylist = subcategoryarraylist;
        this.categorynamearraylist = categorynamearraylist;
    }

    @NonNull
    @Override
    public SubCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.raw_subcat_disp, parent, false);
        return new SubCategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryHolder holder, final int position) {
        holder.textView.setText(subcategoryarraylist.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProductsActivity.class)
                        .putExtra("subcategoryname", subcategoryarraylist.get(position))
                        .putExtra("categoryname", categorynamearraylist.get(position).getCatname()));


            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategoryarraylist.size();
    }

    public class SubCategoryHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public SubCategoryHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.user_tv_subcatname_shopbycategory);

        }
    }
}
