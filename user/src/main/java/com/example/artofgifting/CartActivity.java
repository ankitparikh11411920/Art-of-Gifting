package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.artofgifting.Adapters.CartRecyclerAdapter;
import com.example.artofgifting.Helper.NetworkHelper;
import com.example.artofgifting.Models.CartModel;
import com.example.artofgifting.Models.ProductModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.Serializable;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import pl.droidsonroids.gif.GifImageView;

public class CartActivity extends AppCompatActivity {
    //views
    RecyclerView cartlist;
    LinearLayout totalamount;
    Toolbar carttoolbar;
    TextView carttotal;
    Button placeorder;
    View view1, view2;
    GifImageView emptycart;

    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //vars
    public final String CART_QUANTITY = "cart_quantity";
    ArrayList<ProductModel> cartproductarraylist;
    ArrayList<CartModel> cartmodelarraylist = new ArrayList<>();
    CartRecyclerAdapter cartRecyclerAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper=new NetworkHelper(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        inittoolbar();
        cartlist = findViewById(R.id.user_recycler_cart_cart_activity);
        totalamount = findViewById(R.id.user_linear_total_amount_cart);
        carttotal = findViewById(R.id.user_tv_total_amount_cart);
        placeorder = findViewById(R.id.user_btn_place_order_cart);
        cartlist.setLayoutManager(new LinearLayoutManager(this));
        view1 = findViewById(R.id.user_view_first_cart_activity);
        emptycart = findViewById(R.id.user_gif_empty_cart_cart_activity);
        view2 = findViewById(R.id.user_view_second_cart_activity);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(cartlist);
        initcart();
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });

    }

    private void order() {

        startActivity(new Intent(CartActivity.this,OrderActivity.class)
                .putExtra("LIST",(Serializable) cartmodelarraylist));
        cartmodelarraylist.clear();
        cartproductarraylist.clear();


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkcart();
    }

    private void checkcart() {
        if (cartmodelarraylist.isEmpty()) {
            emptycart.setVisibility(View.VISIBLE);
            totalamount.setVisibility(View.GONE);
            view1.setVisibility(View.INVISIBLE);
            cartlist.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
        } else {
            emptycart.setVisibility(View.GONE);
            totalamount.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            cartlist.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
        }
    }

    private void initcart() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartproductarraylist = new ArrayList<>();
                cartmodelarraylist.clear();
                if (dataSnapshot.child("Users").child(user.getUid()).child("Cart").exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(user.getUid()).child("Cart").getChildren()) {
                        CartModel cartModel = dataSnapshot1.getValue(CartModel.class);
                        cartmodelarraylist.add(cartModel);
                    }
                    checkcart();
                    cartproductarraylist.clear();
                    for (int i = 0; i < cartmodelarraylist.size(); i++) {
                        if (dataSnapshot.child("AllProducts").child(cartmodelarraylist.get(i).getProd_key()).exists()) {
                            ProductModel productModel = dataSnapshot.child("AllProducts").child(cartmodelarraylist.get(i).getProd_key()).getValue(ProductModel.class);
                            cartproductarraylist.add(productModel);
                            cartRecyclerAdapter = new CartRecyclerAdapter(CartActivity.this, cartproductarraylist, cartmodelarraylist);
                            cartlist.setAdapter(cartRecyclerAdapter);

                            cartRecyclerAdapter.onItemClick(new CartRecyclerAdapter.OnClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    startActivity(new Intent(CartActivity.this, SelectedProductActivity.class)
                                            .putExtra("PROD_KEY", cartmodelarraylist.get(position).getProd_key()));
                                }

                                @Override
                                public void onDeleteClick(int position) {
                                    myRef.child("Users").child(user.getUid()).child("Cart").child(cartmodelarraylist.get(position).getProd_key()).removeValue();
                                    cartmodelarraylist.remove(position);
                                    cartproductarraylist.remove(position);
                                    checkcart();
                                    cartRecyclerAdapter.notifyItemRemoved(position);
                                }


                                @Override
                                public void onPlaceOrderClick(final int position) {
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.child("Users").child(user.getUid()).child("address").exists()) {
                                                    final String prod_key = getIntent().getStringExtra("PROD_KEY");
                                                    CartModel cartModel = cartmodelarraylist.get(position);
//                                                    cartModel.setCart_qty(cartmodelarraylist.get(position).getCart_qty());
//                                                    cartModel.setProd_key(prod_key);
//                                                    ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
                                                    ArrayList<CartModel> cartModelArrayList=new ArrayList<>();
                                                    cartModelArrayList.add(cartModel);

                                                    startActivity(new Intent(CartActivity.this, OrderActivity.class)
                                                            .putExtra("LIST", (Serializable) cartModelArrayList ));

                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                                    View v = LayoutInflater.from(CartActivity.this).inflate(R.layout.raw_dialog_okcancel_textview, null);
                                                    builder.setView(v);
                                                    final AlertDialog dialog = builder.create();
                                                    dialog.show();
                                                    TextView textView = v.findViewById(R.id.user_dialog_tv_message);
                                                    Button buttonok = v.findViewById(R.id.user_dialog_btn_ok);
                                                    Button buttoncancel = v.findViewById(R.id.user_dialog_btn_cancel);
                                                    buttonok.setText("ADD");
                                                    textView.setText("No address found!");
                                                    buttonok.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                            startActivity(new Intent(CartActivity.this, AddressActivity.class));
                                                        }
                                                    });
                                                    buttoncancel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                            StyleableToast.makeText(CartActivity.this, "Address is required to Place Order", R.style.exampletoast).show();
                                                        }
                                                    });


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }


                            });
                        } else {
                            myRef.child("Users").child(user.getUid()).child("Cart").child(cartmodelarraylist.get(i).getProd_key()).removeValue();
                            cartproductarraylist.remove(i);
                            cartmodelarraylist.remove(i);
                            checkcart();
                        }
                    }

                    int total = 0;
                    for (int j = 0; j < cartproductarraylist.size(); j++) {
                        int price = (Integer.parseInt(cartproductarraylist.get(j).getPrice())) * (Integer.parseInt(cartmodelarraylist.get(j).getCart_qty()));
                        total = total + price;
                    }
                    String grandtotal = String.valueOf(total);
                    int size = grandtotal.length();
                    if (size > 3 && size <= 5) {
                        String grand = grandtotal.substring(0, size - 3) + "," + grandtotal.substring(size - 3);
                        carttotal.setText("Total : ₹ " + grand);
                    }
                    if (size > 5) {
                        String grand = grandtotal.substring(0, size - 5) + "," + grandtotal.substring(size - 5, size - 3) + "," + grandtotal.substring(size - 3);
                        carttotal.setText("Total : ₹ " + grand);

                    }
                    if (size <= 3) {
                        String grand = grandtotal;
                        carttotal.setText("Total : ₹ " + grand);
                    }
                }
                else {
                    cartmodelarraylist.clear();
                    checkcart();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void inittoolbar() {
        carttoolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(carttoolbar);
        carttoolbar.setTitle("Cart");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        carttoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                myRef.child("Users").child(user.getUid()).child("Cart").child(cartmodelarraylist.get(position).getProd_key()).removeValue();
                cartproductarraylist.remove(position);
                cartmodelarraylist.remove(position);
                checkcart();
                cartRecyclerAdapter.notifyItemRemoved(position);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.colorRed))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();
        }
    };
}
