package com.example.artofgifting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.OrderModel;
import com.example.artofgifting.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private Context context;
    private ArrayList<OrderModel> orderModelArrayList;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onclick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }


    public OrderAdapter(Context context, ArrayList<OrderModel> orderModelArrayList) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_myorder_item, parent, false);
        return new OrderHolder(v,mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {

        Picasso picasso=null;
        picasso.get().load(orderModelArrayList.get(position).getOrderimageurl()).fit().into(holder.orderimage);
        holder.prodname.setText(orderModelArrayList.get(position).getProdname());
        holder.orderqty.setText(orderModelArrayList.get(position).getOrderqty()+" X "+orderModelArrayList.get(position).getProdname());
        holder.ordertotal.setText("₹ "+orderModelArrayList.get(position).getFinalamount());
        holder.orderdatetime.setText(orderModelArrayList.get(position).getOrderdate()+" "+orderModelArrayList.get(position).getOrdertime());

    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        ImageView orderimage;
        Button downlaodpdf;
        TextView prodname,orderqty,ordertotal,orderdatetime;
        public OrderHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            orderimage=itemView.findViewById(R.id.user_imageview_order_disp_myorder_item);
            prodname=itemView.findViewById(R.id.user_tv_prod_name_myorder_item);
            orderqty=itemView.findViewById(R.id.user_tv_order_qty_myorder_item);
            downlaodpdf=itemView.findViewById(R.id.user_btn_order_billpdf_myorder_item);
            ordertotal=itemView.findViewById(R.id.user_tv_order_totalamount_myorder_item);
            orderdatetime=itemView.findViewById(R.id.user_tv_order_datetime_myorder_item);
            downlaodpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onclick(position, view);
                        }
                    }
                }
            });

        }

    }
}
