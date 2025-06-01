package com.example.artofgifting.Adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.ProductModel;
import com.example.artofgifting.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.artofgifting.Adapters.SearchAdapter.*;

public class SearchAdapter extends RecyclerView.Adapter implements Filterable {
    private Context context;
    private ArrayList<ProductModel> productModelArrayList;
    private ArrayList<ProductModel> allproductlist;
    private OnTextClickListener mlistener;
    int view;

    public SearchAdapter(Context context, ArrayList<ProductModel> productModelArrayList, int view) {
        this.view = view;
        this.context = context;
        this.productModelArrayList = productModelArrayList;
        allproductlist = new ArrayList<>(productModelArrayList);
    }

    public interface OnTextClickListener {
        void OnTextClick(int position);

        void OnItemClick(int position);
    }

    public void OnTextClick(OnTextClickListener listener) {
        mlistener = listener;
    }

    public void OnItemClick(OnTextClickListener listener) {
        mlistener = listener;
    }

    @Override
    public Filter getFilter() {
        return searchfilter;
    }

    private Filter searchfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ProductModel> filteredlist = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredlist.clear();
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (ProductModel addProductModel : allproductlist) {
                    if (addProductModel.getName().toLowerCase().contains(filterpattern)) {
                        filteredlist.add(addProductModel);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productModelArrayList.clear();
            productModelArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemViewType(int position) {
        return view;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(context).inflate(R.layout.raw_searchitem, parent, false);
            return new ViewHolderSearching(v, mlistener);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.raw_products_display, parent, false);
            return new ViewHolderSearched(v, mlistener);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (view == 0) {
            ViewHolderSearching viewHolderSearching = (ViewHolderSearching) holder;
            viewHolderSearching.textView.setText(productModelArrayList.get(position).getName());
        } else {
            final ViewHolderSearched viewHolderSearched = (ViewHolderSearched) holder;
                    Picasso picasso = null;
                    viewHolderSearched.prodname.setText(productModelArrayList.get(position).getName());
                    viewHolderSearched.prodprice.setText("₹ "+productModelArrayList.get(position).getPrice());
                    picasso.get()
                            .load(productModelArrayList.get(position).getImageurl())
                            .placeholder(R.drawable.loading)
                            .into(viewHolderSearched.imageView);
                }


    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }
}

class ViewHolderSearching extends RecyclerView.ViewHolder {
    TextView textView;

    public ViewHolderSearching(@NonNull View itemView, final OnTextClickListener mlistener) {
        super(itemView);
        textView = itemView.findViewById(R.id.user_tv_prodname_raw_searchitem);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mlistener.OnTextClick(position);
                    }
                }
            }
        });
    }
}


class ViewHolderSearched extends RecyclerView.ViewHolder {
    TextView prodname, prodprice;
    ImageView imageView;
    ShimmerFrameLayout shimmerFrameLayout;

    public ViewHolderSearched(@NonNull View itemView, final OnTextClickListener mlistener) {
        super(itemView);
        prodname = itemView.findViewById(R.id.user_tv_prod_name);
        prodprice = itemView.findViewById(R.id.user_tv_prod_price);
        imageView = itemView.findViewById(R.id.user_imageview_prod_disp);
        shimmerFrameLayout=itemView.findViewById(R.id.shimmer_raw_prod_disp);
        shimmerFrameLayout.setVisibility(View.GONE);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mlistener.OnItemClick(position);
                    }
                }
            }
        });
    }
}
