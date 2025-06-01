package com.example.admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.Model.OrderModel;
import com.example.admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewOrdersRecyclerAdapter extends RecyclerView.Adapter<ViewOrdersRecyclerAdapter.OrderHolder> {
    private Context context;
    private ArrayList<OrderModel> orderlist;

    public ViewOrdersRecyclerAdapter(Context context, ArrayList<OrderModel> orderlist) {
        this.context = context;
        this.orderlist = orderlist;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.raw_order_item,parent,false);

        return new OrderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Picasso picasso=null;

        picasso.get().load(orderlist.get(position).getOrderimageurl()).into(holder.orderimage);
        holder.ordername.setText("Order Name : "+orderlist.get(position).getProdname());
        holder.orderqty.setText("Order Qty : "+orderlist.get(position).getOrderqty());
        holder.ordertotal.setText("Order Total : ₹ "+orderlist.get(position).getFinalamount());
        holder.orderedby.setText("Ordered By : "+orderlist.get(position).getOrderedby());
        holder.orderedon.setText("Ordered On : "+orderlist.get(position).getOrderdate()+" " +orderlist.get(position).getOrdertime());

    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        ImageView orderimage;
        TextView ordername,orderedby,orderedon,orderqty,ordertotal;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            orderimage=itemView.findViewById(R.id.admin_imageview_orderimage_order_item);
            ordername=itemView.findViewById(R.id.admin_tv_ordername_order_item);
            orderedby=itemView.findViewById(R.id.admin_tv_ordered_by_order_item);
            orderedon=itemView.findViewById(R.id.admin_tv_ordered_on_order_item);
            orderqty=itemView.findViewById(R.id.admin_tv_order_qty_order_item);
            ordertotal=itemView.findViewById(R.id.admin_tv_order_total_order_item);

        }
    }
}
