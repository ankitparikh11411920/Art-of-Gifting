package com.example.admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Model.CategoryModel;
import com.example.admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryHolder> {

    private Context context;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);

        void OnOptionClick(View v, int position);

        void OnLongClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public CategoryRecyclerAdapter(Context context, ArrayList<CategoryModel> categoryModelArrayList) {
        this.context = context;
        this.categoryModelArrayList = categoryModelArrayList;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.raw_cat_disp, parent, false);
        return new CategoryHolder(v, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Picasso picasso = null;
        picasso.get().load(categoryModelArrayList.get(position).getCatimageurl()).into(holder.catimage);
        holder.catname.setText(categoryModelArrayList.get(position).getCatname());
    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView catname;
        ImageView catimage, moreoptions;

        public CategoryHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            moreoptions = itemView.findViewById(R.id.admin_option_more_cat_disp);
            catname = itemView.findViewById(R.id.admin_tv_catname_raw_cat_disp);
            catimage = itemView.findViewById(R.id.admin_imageview_catimage_raw_cat_disp);
            moreoptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnOptionClick(moreoptions, position);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(itemView, position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnLongClick(itemView, position);
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
}
