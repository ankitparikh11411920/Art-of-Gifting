package com.example.artofgifting.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.CartModel;
import com.example.artofgifting.Models.ProductModel;
import com.example.artofgifting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartItemHolder> {

    Context context;
    private ArrayList<ProductModel> cartproductarraylist;
    private ArrayList<CartModel> cartModelArrayList;
    private OnClickListener mlistener;
    private DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

    public interface OnClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onPlaceOrderClick(int position);
    }

    public void onItemClick(OnClickListener listener){
        mlistener=listener;
    }
    public void onDeleteClick(OnClickListener listener){
        mlistener=listener;
    }
    public void onPlaceOrderClick(OnClickListener listener){
        mlistener=listener;
    }




    public CartRecyclerAdapter(Context context, ArrayList<ProductModel> cartproductarraylist, ArrayList<CartModel> cartModelArrayList) {
        this.context = context;
        this.cartproductarraylist = cartproductarraylist;
        this.cartModelArrayList=cartModelArrayList;
    }

    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.raw_cart_item, parent, false);
        return new CartItemHolder(v, mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, final int position) {
        Picasso picasso = null;
        holder.prod_name.setText(cartproductarraylist.get(position).getName());
        holder.prod_price.setText(cartproductarraylist.get(position).getPrice());
        holder.cart_qty.setText(cartModelArrayList.get(position).getCart_qty());

        checkstock(holder,position);
        picasso.get().load(cartproductarraylist.get(position).getImageurl()).into(holder.prod_img);

    }

    private void checkstock(CartItemHolder holder, int position) {
        int cart_qty=Integer.parseInt(cartModelArrayList.get(position).getCart_qty());
        int prod_qty=Integer.parseInt(cartproductarraylist.get(position).getQuantity());
        if(cart_qty>prod_qty){
            holder.cart_stock.setVisibility(View.VISIBLE);
            holder.cart_stock.setText("Out Of Stock");
            holder.cart_stock.setTypeface(null, Typeface.BOLD);
            holder.cart_stock.setTextColor(Color.RED);
        }
        else {
            holder.cart_stock.setVisibility(View.VISIBLE);
            holder.cart_stock.setText("In Stock");
            holder.cart_stock.setTypeface(null, Typeface.BOLD);
            holder.cart_stock.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return cartproductarraylist.size();
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {

        ImageView delete, prod_img;
        TextView prod_name, prod_price, cart_qty,cart_stock;
        Button increase, decrease, place_order;

        CartItemHolder(@NonNull View itemView, final OnClickListener mlistener) {
            super(itemView);
            delete = itemView.findViewById(R.id.user_btn_crossdelete_cart_item);
            prod_img = itemView.findViewById(R.id.user_cart_prodimg_disp);
            prod_name = itemView.findViewById(R.id.user_tv_prodname_cart_item);
            prod_price = itemView.findViewById(R.id.user_tv_prodprice_cart_item);
            cart_qty = itemView.findViewById(R.id.user_tv_cart_qty_cart_item);
            cart_stock=itemView.findViewById(R.id.user_tv_stock_cart_item);
            increase = itemView.findViewById(R.id.user_btn_incr_cart_item);
            decrease = itemView.findViewById(R.id.user_btn_decr_cart_item);
            place_order = itemView.findViewById(R.id.user_btn_place_order_cart_item);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.onDeleteClick(position);
                        }
                    }
                }
            });
            place_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.onPlaceOrderClick(position);
                        }
                    }
                }
            });

            increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty=Integer.parseInt(cart_qty.getText().toString());
                    qty++;
                    int prod_qty=Integer.parseInt(cartproductarraylist.get(getAdapterPosition()).getQuantity());
                    if(qty<prod_qty+1){
                        myRef.child(user.getUid()).child("Cart").child(cartproductarraylist.get(getAdapterPosition()).getKey()).child("cart_qty").setValue(String.valueOf(qty));
                        cartproductarraylist.get(getAdapterPosition()).setQuantity(String.valueOf(qty));
                        cart_qty.setText(String.valueOf(qty));
                    }
                    else
                    {
                        StyleableToast.makeText(context,"Maximum Quantity reached",R.style.exampletoast).show();
//                        Toast.makeText(context, "Maximum Quantity reached", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty=Integer.parseInt(cart_qty.getText().toString());
                    qty--;
                    if(qty!=0){
                        myRef.child(user.getUid()).child("Cart").child(cartproductarraylist.get(getAdapterPosition()).getKey()).child("cart_qty").setValue(String.valueOf(qty));
                        cartproductarraylist.get(getAdapterPosition()).setQuantity(String.valueOf(qty));
                        cart_qty.setText(String.valueOf(qty));
                    }
                    else
                    {
                        /*myRef.child(user.getUid()).child("Cart").child(cartproductarraylist.get(getAdapterPosition()).getKey()).removeValue();
                        cartproductarraylist.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());*/
                        StyleableToast.makeText(context,"Minimum Quantity Reached for "+cartproductarraylist.get(getAdapterPosition()).getName(),R.style.exampletoast).show();
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
