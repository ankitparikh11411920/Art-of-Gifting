package com.example.admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Model.AddProductModel;
import com.example.admin.Product_Description;
import com.example.admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HorizontalProductDisplayAdapter extends RecyclerView.Adapter<HorizontalProductDisplayAdapter.HorizontalHolder> {

    private Context context;
    private ArrayList<AddProductModel> productalmostoutofstocklist;

    public HorizontalProductDisplayAdapter(Context context, ArrayList<AddProductModel> productalmostoutofstocklist) {
        this.context = context;
        this.productalmostoutofstocklist = productalmostoutofstocklist;
    }

    @NonNull
    @Override
    public HorizontalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.raw_product_hor_disp,parent,false);
        return new HorizontalHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalHolder holder, int position) {
        Picasso picasso=null;
        picasso.get().load(productalmostoutofstocklist.get(position).getImageurl()).into(holder.image);
        holder.name.setText(productalmostoutofstocklist.get(position).getName());
        holder.price.setText(productalmostoutofstocklist.get(position).getPrice());
        holder.qty.setText(productalmostoutofstocklist.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return productalmostoutofstocklist.size();
    }

    public class HorizontalHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name,price,qty;
        public HorizontalHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.admin_imageview_prod_disp_hor_item);
            name=itemView.findViewById(R.id.admin_tv_prod_name_hor_disp_item);
            price=itemView.findViewById(R.id.admin_tv_prod_price_hor_disp_item);
            qty=itemView.findViewById(R.id.admin_tv_prod_qty_hor_disp_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Product_Description.class)
                    .putExtra("key",productalmostoutofstocklist.get(getAdapterPosition()).getKey()));
                }
            });
        }
    }
}
