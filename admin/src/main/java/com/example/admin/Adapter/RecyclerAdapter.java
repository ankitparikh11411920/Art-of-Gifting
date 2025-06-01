package com.example.admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Model.AddProductModel;
import com.example.admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> implements Filterable {

    Context context;
    ArrayList<AddProductModel> addProductModelArrayList;
    ArrayList<AddProductModel> allproductlist;
    private OnItemClickListener mlistener;


    public interface OnItemClickListener {
        void onItemClick(int position, View view);

        void onOptionClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public RecyclerAdapter(Context context, ArrayList<AddProductModel> addproductModelArrayList) {
        this.context = context;
        this.addProductModelArrayList = addproductModelArrayList;
        allproductlist = new ArrayList<>(addproductModelArrayList);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.raw_product_recycle, parent, false);
        return new RecyclerViewHolder(v, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        Picasso picasso = null;
        holder.prodnamedisp.setText(addProductModelArrayList.get(position).getName());
        holder.prodpricedisp.setText("Rs. "+ addProductModelArrayList.get(position).getPrice());
        holder.prodqtydisp.setText("Qty. "+addProductModelArrayList.get(position).getQuantity());

        picasso.get().load(addProductModelArrayList.get(position).getImageurl()).fit().placeholder(R.drawable.ic_loading).into(holder.prodimagedisp);
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, Product_Description.class);
//                i.putExtra("imageurl", addProductModelArrayList.get(position).getImageurl());
//                i.putExtra("key", addProductModelArrayList.get(position).getKey());
//                context.startActivity(i);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return addProductModelArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView prodnamedisp, prodpricedisp, prodqtydisp;
        ImageView prodimagedisp, prodmoreoption;
        CardView cardView;

        public RecyclerViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            prodimagedisp = itemView.findViewById(R.id.admin_prod_image_disp);
            prodnamedisp = itemView.findViewById(R.id.admin_prod_name_disp);
            prodpricedisp = itemView.findViewById(R.id.admin_prod_price_disp);
            prodmoreoption = itemView.findViewById(R.id.admin_option_more);
            prodqtydisp = itemView.findViewById(R.id.admin_prod_qty_disp);
            cardView = itemView.findViewById(R.id.admin_card_product_display);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, v);
                        }
                    }
                }
            });
            prodmoreoption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onOptionClick(position, v);
                        }
                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return searchfilter;
    }

    private Filter searchfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<AddProductModel> filteredlist = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredlist.addAll(allproductlist);
            } else {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (AddProductModel addProductModel : allproductlist) {
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
            addProductModelArrayList.clear();
            addProductModelArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
}
