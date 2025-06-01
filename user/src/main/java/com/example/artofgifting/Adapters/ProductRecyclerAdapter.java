package com.example.artofgifting.Adapters;

import android.content.Context;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<ProductModel> productModelArrayList;
    private OnItemCLickListener mlistener;
    private int count, view;

    public ProductRecyclerAdapter(Context context, ArrayList<ProductModel> productModelArrayList, int count, int view) {
        this.context = context;
        this.productModelArrayList = productModelArrayList;
        this.count = count;
        this.view = view;
    }

    @Override
    public int getItemViewType(int position) {
        return view;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (view == 0) {
            v = LayoutInflater.from(context).inflate(R.layout.raw_products_display, parent, false);
            return new ProductHolderVertical(v);
        } else if(view==1) {
            v = LayoutInflater.from(context).inflate(R.layout.raw_product_disp_horizontal, parent, false);
            return new ProductHolderHorizontal(v);
        }
        else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (view == 0) {
            final ProductHolderVertical productHolderVertical = (ProductHolderVertical) holder;
            productHolderVertical.shimmerFrameLayout.startShimmer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso picasso = null;
                    productHolderVertical.prodname.setText(productModelArrayList.get(position).getName());
                    productHolderVertical.prodprice.setText("₹ " +productModelArrayList.get(position).getPrice());
                    picasso.get()
                            .load(productModelArrayList.get(position).getImageurl())
                            .into(productHolderVertical.imageView);
                    productHolderVertical.shimmerFrameLayout.setVisibility(View.GONE);
                }
            }, 1000);

        } else if(view==1){
            final ProductHolderHorizontal productHolderHorizontal = (ProductHolderHorizontal) holder;
            productHolderHorizontal.shimmerFrameLayouthorizontal.startShimmer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso picasso = null;
                    picasso.get().load(productModelArrayList.get(position).getImageurl()).into(productHolderHorizontal.prodimagehorizontal);
                    productHolderHorizontal.prodnamehorizontal.setText("Name : " + productModelArrayList.get(position).getName());
                    productHolderHorizontal.prodnamehorizontal.setSelected(true);
                    productHolderHorizontal.prodpricehorizontal.setText("₹ " + productModelArrayList.get(position).getPrice());
                    productHolderHorizontal.prodqtyhorizontal.setText("Qty : " + productModelArrayList.get(position).getQuantity());
                    productHolderHorizontal.shimmerFrameLayouthorizontal.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public interface OnItemCLickListener {
        void OnItemClickListener(int position);
    }

    public class ProductHolderVertical extends RecyclerView.ViewHolder {
        TextView prodname, prodprice;
        ImageView imageView;
        ShimmerFrameLayout shimmerFrameLayout;

        public ProductHolderVertical(@NonNull View itemView) {
            super(itemView);
            prodname = itemView.findViewById(R.id.user_tv_prod_name);
            prodprice = itemView.findViewById(R.id.user_tv_prod_price);
            imageView = itemView.findViewById(R.id.user_imageview_prod_disp);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_raw_prod_disp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.OnItemClickListener(position);
                        }
                    }
                }
            });
        }
    }

    public class ProductHolderHorizontal extends RecyclerView.ViewHolder {
        TextView prodnamehorizontal, prodpricehorizontal, prodqtyhorizontal;
        ImageView prodimagehorizontal;
        ShimmerFrameLayout shimmerFrameLayouthorizontal;


        public ProductHolderHorizontal(@NonNull View itemView) {
            super(itemView);
            prodimagehorizontal = itemView.findViewById(R.id.user_imageview_prod_disp_order_item);
            prodnamehorizontal = itemView.findViewById(R.id.user_tv_prod_name_order_item);
            prodpricehorizontal = itemView.findViewById(R.id.user_tv_prod_price_order_item);
            prodqtyhorizontal = itemView.findViewById(R.id.user_tv_prod_qty_order_item);
            shimmerFrameLayouthorizontal = itemView.findViewById(R.id.user_shimmer_raw_prod_item_disp_horizontal);
        }
    }

    public void OnItemClick(OnItemCLickListener listener) {
        mlistener = listener;
    }


    /*public ProductRecyclerAdapter(Context context, ArrayList<ProductModel> productModelArrayList, int count) {
        this.context = context;
        this.productModelArrayList=productModelArrayList;
        this.count=count;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.raw_products_display, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Picasso picasso = null;
        holder.prodname.setText(productModelArrayList.get(position).getName());
        holder.prodprice.setText(productModelArrayList.get(position).getPrice());
        picasso.get()
                .load(productModelArrayList.get(position).getImageurl())
                .placeholder(R.drawable.loading)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView prodname, prodprice;
        ImageView imageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            prodname = itemView.findViewById(R.id.user_tv_prod_name);
            prodprice = itemView.findViewById(R.id.user_tv_prod_price);
            imageView = itemView.findViewById(R.id.user_imageview_prod_disp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.OnItemClickListener(position);
                        }
                    }
                }
            });


        }
    }*/
}
