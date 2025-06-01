package com.example.admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdAdapterRecycler extends RecyclerView.Adapter<AdAdapterRecycler.AdViewHolder> {

    Context context;
    ArrayList<String> adsmodelarraylist;
    private OnItemClick mlistener;

    public AdAdapterRecycler(Context context, ArrayList<String> adsmodelarraylist) {
        this.context = context;
        this.adsmodelarraylist = adsmodelarraylist;
    }
    public interface OnItemClick{
        void OnRemoveClick(int position,View v);
    }
    public void OnRemoveClick(OnItemClick listener){
        mlistener=listener;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.raw_ad_layout,parent,false);
        return new AdViewHolder(v,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        Picasso picasso=null;
        picasso.get().load(adsmodelarraylist.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return adsmodelarraylist.size();
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public AdViewHolder(@NonNull View itemView,final OnItemClick listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.admin_imageview_ads_adlayout);
            textView=itemView.findViewById(R.id.admin_tv_remove_ad_adlayout);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnRemoveClick(position,v);
                        }
                    }
                }
            });

        }
    }
}
