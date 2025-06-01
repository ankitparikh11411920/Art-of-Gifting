package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artofgifting.Adapters.OrderAdapter;
import com.example.artofgifting.Adapters.ProductRecyclerAdapter;
import com.example.artofgifting.Helper.NetworkHelper;
import com.example.artofgifting.Models.CartModel;
import com.example.artofgifting.Models.OrderModel;
import com.example.artofgifting.Models.ProductModel;
import com.example.artofgifting.Notify.APIService;
import com.example.artofgifting.Notify.Client;
import com.example.artofgifting.Notify.Data;
import com.example.artofgifting.Notify.MyResponse;
import com.example.artofgifting.Notify.NotificationSender;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    //views
    RecyclerView orderlist;
    TextView total, fee, grandtotal, useraddress;
    Button confirm, changeaddress, editaddress;
    Toolbar toolbar;

    //vars
    OrderAdapter orderAdapter;
    ProductRecyclerAdapter productRecyclerAdapter;
    private APIService apiService;
    String fullname;
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    int finalamount;
    String address;
    int finalproductqty;
    int finalorderqty;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    private Handler handler = new Handler();
    ArrayList<CartModel> keyAndQuantityModelArrayList = new ArrayList<>();


    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String imageurl;

    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper=new NetworkHelper(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent i = getIntent();
        keyAndQuantityModelArrayList = (ArrayList<CartModel>) i.getSerializableExtra("LIST");

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        useraddress = findViewById(R.id.user_tv_address_order_activity);
        changeaddress = findViewById(R.id.user_btn_change_address_order_activity);
        editaddress = findViewById(R.id.user_btn_edit_address_order_activity);
        total = findViewById(R.id.user_tv_total_order_activity);
        grandtotal = findViewById(R.id.user_tv_grand_total_order_activity);
        confirm = findViewById(R.id.user_btn_confirmorder_order_activity);
        fee = findViewById(R.id.user_tv_delivery_fee_order_activity);
        orderlist = findViewById(R.id.user_recycler_order_order_activity);
        orderlist.setLayoutManager(new LinearLayoutManager(this));

        inittoolbar();
        setaddress();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmorder(finalamount);
            }
        });
        initrecyclerview();
        setprice();
        editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeaddress();
            }
        });
    }

    private void getimage(final int position) {
        Intent i = getIntent();
        keyAndQuantityModelArrayList = (ArrayList<CartModel>) i.getSerializableExtra("LIST");
        final String[] image = new String[1];
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                image[0] = dataSnapshot.child("AllProducts").child(keyAndQuantityModelArrayList.get(position).getProd_key()).child("imageurl").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        imageurl = image[0];
    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.user_order_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Order Details");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void changeaddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootview = LayoutInflater.from(OrderActivity.this)
                .inflate(R.layout.raw_dialog_okcancel, null);
        builder.setView(rootview);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button ok = rootview.findViewById(R.id.user_dialog_btn_ok);
        Button cancel = rootview.findViewById(R.id.user_dialog_btn_cancel);
        final TextInputLayout dialog_username = rootview.findViewById(R.id.user_dialog_edt_name);
        dialog_username.getEditText().setText(address);
        dialog_username.setHint("Address");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newaddress = dialog_username.getEditText().getText().toString().trim();
                if (!newaddress.isEmpty()) {
                    myRef.child("Users").child(user.getUid()).child("address").setValue(newaddress);
                    useraddress.setText(newaddress);
                    address=newaddress;
                    alertDialog.dismiss();
                } else {
                    dialog_username.setError("Field cannot be Empty!");
                    dialog_username.requestFocus();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void setaddress() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                address = dataSnapshot.child("Users").child(user.getUid()).child("address").getValue(String.class);
                useraddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void confirmorder(final int finalamount) {

        getusername();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.raw_progress_dialog, null);
        final int[] flag = {1};
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        Button cancel = v.findViewById(R.id.user_progress_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag[0] = 0;
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnectionAvailable()) {


                    if (flag[0] == 1) {
                        for (int i = 0; i < keyAndQuantityModelArrayList.size(); i++) {
                            final int finalI = i;
                            myRef.child("AllProducts").child(keyAndQuantityModelArrayList.get(i).getProd_key()).child("quantity").runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                    String prodqty = mutableData.getValue(String.class);
                                    int finalproductqty = Integer.parseInt(prodqty);
                                    Transaction.Result result = null;

                                    int new_qty;
                                    if (prodqty != null) {
                                        if (finalorderqty <= finalproductqty) {
                                            new_qty = finalproductqty - Integer.parseInt(keyAndQuantityModelArrayList.get(finalI).getCart_qty());
                                            mutableData.setValue(String.valueOf(new_qty));
                                            result = Transaction.success(mutableData);
                                        } else {
                                            result = Transaction.abort();
                                        }
                                    }
                                    return result;
                                }

                                @Override
                                public void onComplete(@Nullable final DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                    Log.d("TAG", "postTransaction:onComplete:" + databaseError);
                                    if (b) {
                                        int size = keyAndQuantityModelArrayList.size();
                                        dialog.dismiss();
                                        StyleableToast.makeText(OrderActivity.this, "Order Successfully Placed", R.style.exampletoast).show();
                                        final String orderkey = myRef.push().getKey();
                                        Date c = Calendar.getInstance().getTime();
                                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
                                        final String formattedDate = df.format(c);
                                        final String formattedtime = tf.format(c);
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                imageurl = dataSnapshot.child("AllProducts").child(keyAndQuantityModelArrayList.get(finalI).getProd_key()).child("imageurl").getValue(String.class);
                                                int amount = Integer.parseInt(keyAndQuantityModelArrayList.get(finalI).getCart_qty()) * Integer.parseInt(productModelArrayList.get(finalI).getPrice());
                                                OrderModel orderModel = new OrderModel(orderkey, user.getUid(), keyAndQuantityModelArrayList.get(finalI).getProd_key(), keyAndQuantityModelArrayList.get(finalI).getCart_qty(), address, String.valueOf(amount), productModelArrayList.get(finalI).getName(), formattedDate, formattedtime, imageurl, fullname);
                                                String token=dataSnapshot.child("Admin").child("token").getValue(String.class);
                                                sendNotifications(orderModel,token);
                                                createnotification(orderModel);
                                                myRef.child("Orders").child(orderkey).setValue(orderModel);
                                                myRef.child("Users").child(user.getUid()).child("Orders").child(orderkey).setValue(orderModel);
                                                myRef.child("Users").child(user.getUid()).child("Cart").child(keyAndQuantityModelArrayList.get(finalI).getProd_key()).removeValue();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        StyleableToast.makeText(OrderActivity.this, "Order Failed, \nPlease Update your quantity", R.style.exampletoast).show();
                                        finish();
                                    }
                                }
                            });
                        }
                    } else {
                        dialog.dismiss();
                    }


                } else {
                    dialog.dismiss();
                    startActivity(new Intent(OrderActivity.this, InternetConnectionActivity.class));
                }
            }
        }, 3100);
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;

        } catch(IOException e) {
            System.out.println(e);
            return null;
        }
    }

    private void createnotification(OrderModel orderModel) {
        Random random=new Random();
        int number=random.nextInt();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("Your order has been successfully placed")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Order Name : "+orderModel.getProdname()+"\nAmount : "+orderModel.getFinalamount()+" Rs\nOrder Quantity : "+orderModel.getOrderqty()+"\nOrdered On :"+orderModel.getOrderdate()+" "+orderModel.getOrdertime()))
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("0");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "0",
                    "My App",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        notificationManager.notify(number, builder.build());

    }

    private void getusername() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullname = dataSnapshot.child("Users").child(user.getUid()).child("fullname").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendNotifications(OrderModel orderModel,String token) {
//        Data data = new Data(address, prodname);
        Data data=new Data(orderModel.getProdname(),orderModel.getOrder_address(),orderModel.getOrder_key(),orderModel.getProd_key(),orderModel.getFinalamount(),orderModel.getOrderdate(),orderModel.getUser_key(),orderModel.getOrderimageurl(),orderModel.getOrderedby(),orderModel.getOrdertime(),orderModel.getOrderqty());
        NotificationSender sender = new NotificationSender(data, token);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(OrderActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initrecyclerview() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelArrayList.clear();

                for (int i = 0; i < keyAndQuantityModelArrayList.size(); i++) {
                    ProductModel productModel = dataSnapshot.child("AllProducts").child(keyAndQuantityModelArrayList.get(i).getProd_key()).getValue(ProductModel.class);
                    productModelArrayList.add(productModel);
                    productModelArrayList.get(i).setQuantity(keyAndQuantityModelArrayList.get(i).getCart_qty());
                }
                productRecyclerAdapter = new ProductRecyclerAdapter(OrderActivity.this, productModelArrayList, productModelArrayList.size(), 1);
                orderlist.setAdapter(productRecyclerAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setprice() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int amount = 0;
                for (int i = 0; i < keyAndQuantityModelArrayList.size(); i++) {
                    String price = dataSnapshot.child("AllProducts").child(keyAndQuantityModelArrayList.get(i).getProd_key()).child("price").getValue(String.class);
                    amount = amount + (Integer.parseInt(price)) * (Integer.parseInt(keyAndQuantityModelArrayList.get(i).getCart_qty()));
                }
                String totalamount = String.valueOf(amount);
                total.setText("₹ " + Float.parseFloat(totalamount));
                finalamount = (int) (Float.parseFloat(totalamount) + Float.parseFloat("40"));
                grandtotal.setText("₹ " + ((Float.parseFloat(totalamount) + Float.parseFloat("40"))));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
//            Log.d("Network", "Connected");
            return true;
        } else {
//            Log.d("Network", "Not Connected");

            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

/*
if (flag[0] == 1) {
final String orderkey = myRef.push().getKey();
        finalorderqty = Integer.parseInt(order_qty);
final int[] prod_qty = new int[1];
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String qty = dataSnapshot.child("AllProducts").child(prod_key).child("quantity").getValue(String.class);
        prod_qty[0] = Integer.parseInt(qty);
        if (prod_qty[0] == 0) {
        Toast.makeText(OrderActivity.this, "Order Failed", Toast.LENGTH_SHORT).show();
        } else {
        if (prod_qty[0] >= finalorderqty) {
        checkquantity();
        if (finalproductqty == 0) {

        Toast.makeText(OrderActivity.this, "Order Failed", Toast.LENGTH_SHORT).show();
        } else {
        int new_qty = finalproductqty - finalorderqty;
        myRef.child("AllProducts").child(prod_key).child("quantity").setValue(String.valueOf(new_qty));
        OrderModel orderModel = new OrderModel(orderkey, user.getUid(), order_qty, address, String.valueOf(finalamount), productModelArrayList.get(0).getName());
        myRef.child("Orders").child(orderkey).child(user.getUid()).setValue(orderModel);
        myRef.child("Users").child(user.getUid()).child("Orders").child(orderkey).setValue(orderModel);
        }
        }
        }

                        */
/*if(prod_qty[0]<finalorderqty){
                            Toast.makeText(OrderActivity.this, "Order Failed", Toast.LENGTH_SHORT).show();
                        }else{
                            if (prod_qty[0] == 0) {
                                StyleableToast.makeText(OrderActivity.this, "Order Failed", R.style.exampletoast).show();
                            } else {
                                int new_qty = prod_qty[0] - finalorderqty;
                                if (new_qty < 0) {
                                    StyleableToast.makeText(OrderActivity.this, "Order Failed", R.style.exampletoast).show();
                                } else {
                                    myRef.child("AllProducts").child(prod_key).child("quantity").setValue(String.valueOf(new_qty));
                                    if (finalorderqty <= prod_qty[0]) {
                                        OrderModel orderModel = new OrderModel(orderkey, user.getUid(), order_qty, address, String.valueOf(finalamount), productModelArrayList.get(0).getName());
                                        myRef.child("Orders").child(orderkey).child(user.getUid()).setValue(orderModel);
                                        myRef.child("Users").child(user.getUid()).child("Orders").child(orderkey).setValue(orderModel);
                                    } else {
                                        StyleableToast.makeText(OrderActivity.this, "Order Failed", R.style.exampletoast).show();
                                        finish();
                                    }
                                }

                            }
                        }*/
/*


        }

@Override
public void onCancelled(@NonNull DatabaseError databaseError) {

        }
        });

        dialog.dismiss();
        } else {
        dialog.dismiss();
        }
*/
