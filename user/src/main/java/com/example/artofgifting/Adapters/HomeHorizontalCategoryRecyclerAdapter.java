package com.example.artofgifting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.CategoryModel;
import com.example.artofgifting.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeHorizontalCategoryRecyclerAdapter extends RecyclerView.Adapter<HomeHorizontalCategoryRecyclerAdapter.HomeCatviewholder> {
    private Context context;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public void OnItemClick(OnItemClickListener listener){
        mlistener=listener;
    }
    public HomeHorizontalCategoryRecyclerAdapter(Context context, ArrayList<CategoryModel> categoryModelArrayList) {
        this.context = context;
        this.categoryModelArrayList = categoryModelArrayList;
    }

    @NonNull
    @Override
    public HomeCatviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_horizontalcatname, null, false);
        return new HomeCatviewholder(v,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCatviewholder holder, int position) {
        Picasso picasso = null;
        holder.textView.setText(categoryModelArrayList.get(position).getCatname());
        holder.textView.setSelected(true);
        picasso.get().load(categoryModelArrayList.get(position).getCatimageurl()).placeholder(R.drawable.loading).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    public class HomeCatviewholder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public HomeCatviewholder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_cat_raw_horizontalcatname);
            imageView = itemView.findViewById(R.id.cirimg_cat_raw_horizontalcatname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}


