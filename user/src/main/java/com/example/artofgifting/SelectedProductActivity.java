package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artofgifting.Adapters.ReviewRecyclerAdapter;
import com.example.artofgifting.Helper.NetworkHelper;
import com.example.artofgifting.Models.CartModel;
import com.example.artofgifting.Models.ProductModel;
import com.example.artofgifting.Models.ReviewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SelectedProductActivity extends AppCompatActivity implements View.OnClickListener {
    ///view
    ImageView imageView;
    Spinner squantity;
    Button btnadd, button_place_order;
    Picasso picasso = null;
    LinearLayout selectedcontainer;
    TextView tvprodname, tvproddesc, tvprodprice;
    ///view

    ///rating
    RatingBar ratingBar;
    Button ratingsubmit;
    EditText comment;
    TextView totalratings, averagerating, total5stars, total4stars, total3stars, total2stars, total1stars, totalquantity;
    TextView totalreviews;
    LinearLayout ratingcontainer;
    RecyclerView reviewlist;
    ProgressBar progressBar5stars, progressBar4stars, progressBar3stars, progressBar2stars, progressBar1stars;
    ///rating

    //firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ///firebase

    ///variables
    String qty[];
    String quantity, username, imageurl;
    ProductModel productModel;
    private int[] stars = new int[5];
    String prod_quantity;
    ReviewRecyclerAdapter reviewRecyclerAdapter;

    ///variables


    @Override
    protected void onStart() {
        super.onStart();
        NetworkHelper networkHelper=new NetworkHelper(SelectedProductActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_product);
        final String prod_key = getIntent().getStringExtra("PROD_KEY");

        button_place_order = findViewById(R.id.user_btn_place_order_selected_product);
        squantity = findViewById(R.id.spinner_quantity);

        selectedcontainer = findViewById(R.id.user_selectedcontainer_linear);

        totalquantity = findViewById(R.id.user_tv_prod_qty_selected_product);
        ///rating
        reviewlist = findViewById(R.id.user_recycler_reviewlist_selected_product);
        ratingcontainer = findViewById(R.id.user_ratingcontainer_selected_product);
        ratingBar = findViewById(R.id.user_give_rating_selected_product);
        ratingsubmit = findViewById(R.id.user_btn_submit_rating_selected_product);
        totalratings = findViewById(R.id.user_tv_total_ratings_selected_product);
        totalreviews=findViewById(R.id.user_tv_total_reviews_selected_product);
        averagerating = findViewById(R.id.user_tv_average_rating_selected_product);
        total1stars = findViewById(R.id.user_tv_total_1stars_selected_product);
        total2stars = findViewById(R.id.user_tv_total_2stars_selected_product);
        total3stars = findViewById(R.id.user_tv_total_3stars_selected_product);
        total4stars = findViewById(R.id.user_tv_total_4stars_selected_product);
        total5stars = findViewById(R.id.user_tv_total_5stars_selected_product);
        progressBar1stars = findViewById(R.id.user_progress_1star_selected_product);
        progressBar2stars = findViewById(R.id.user_progress_2star_selected_product);
        progressBar3stars = findViewById(R.id.user_progress_3star_selected_product);
        progressBar4stars = findViewById(R.id.user_progress_4star_selected_product);
        progressBar5stars = findViewById(R.id.user_progress_5star_selected_product);
        comment = findViewById(R.id.user_edt_comment_selected_product);
        checkrating(prod_key);
        ///rating
        imageView = findViewById(R.id.imagesubproduct);
        btnadd = findViewById(R.id.user_btn_add_to_cart_selected_product);
        tvprodname = findViewById(R.id.prodname);
        tvproddesc = findViewById(R.id.proddesc);
        tvprodprice = findViewById(R.id.prodprice);
        //product info
        prodinfo();
        showcomments(prod_key);

        //getuserdata
        getimagename();
//        showRatings();
        showrating(prod_key);
        btnadd.setOnClickListener(this);
        button_place_order.setOnClickListener(this);
    }

    private void showcomments(final String prod_key) {
        final ArrayList<ReviewModel> reviewModelArrayList = new ArrayList<>();

        reviewlist.setLayoutManager(new LinearLayoutManager(this));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Rating").child(prod_key).getChildren()) {
                    ReviewModel reviewModel = dataSnapshot1.getValue(ReviewModel.class);
                    reviewModelArrayList.add(reviewModel);

                }
                totalreviews.setText("Product Reviews ("+reviewModelArrayList.size()+")");
                ArrayList<String> prodkeylist=new ArrayList<>();
                prodkeylist.add(prod_key);
                reviewRecyclerAdapter = new ReviewRecyclerAdapter(reviewModelArrayList, SelectedProductActivity.this, prodkeylist);
                reviewlist.setAdapter(reviewRecyclerAdapter);
                reviewRecyclerAdapter.OnItemClickListener(new ReviewRecyclerAdapter.OnClickListener() {
                    @Override
                    public void OnEditClick(int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SelectedProductActivity.this);
                        View rootview = LayoutInflater.from(SelectedProductActivity.this).inflate(R.layout.layout_rating, null);
                        builder.setView(rootview);
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        Button submit;
                        RatingBar ratingBar;
                        final EditText comment;
                        submit = rootview.findViewById(R.id.user_btn_submit_rating_selected_product);
                        ratingBar = rootview.findViewById(R.id.user_give_rating_selected_product);
                        comment = rootview.findViewById(R.id.user_edt_comment_selected_product);
                        final int[] rate = new int[1];
                        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                rate[0] = (int) rating;
                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String mcomment = comment.getText().toString().trim();
                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String formattedDate = df.format(c).trim();
                                setratingbar(prod_key, rate[0], mcomment, formattedDate);
                                dialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void OnDeleteClick(int position) {
                        reviewRecyclerAdapter.notifyItemRemoved(position);
                        reviewModelArrayList.remove(position);
                        myRef.child("Rating").child(prod_key).child(user.getUid()).removeValue();
                        myRef.child("Users").child(user.getUid()).child("Rated Products").child(prod_key).removeValue();
                        showcomments(prod_key);
                        showrating(prod_key);
                    }
                });

                reviewRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getimagename() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username = dataSnapshot.child("Users").child(user.getUid()).child("fullname").getValue(String.class);
                    imageurl = dataSnapshot.child("Users").child(user.getUid()).child("image_url").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkqty() {
        if (Integer.parseInt(quantity) > 10) {
            totalquantity.setText("In Stock");
            totalquantity.setTextColor(Color.GREEN);
            totalquantity.setTypeface(null, Typeface.BOLD);
        } else if (Integer.parseInt(quantity) <= 10 && Integer.parseInt(quantity) > 5) {
            totalquantity.setText("Only " + quantity + " Remaining ! ");
            totalquantity.setTextColor(Color.BLACK);
            totalquantity.setTypeface(null, Typeface.BOLD);
        } else if (Integer.parseInt(quantity) <= 5 && Integer.parseInt(quantity) > 0) {
            totalquantity.setText("Only " + quantity + " Remaining ! ");
            totalquantity.setTextColor(Color.RED);
            totalquantity.setTypeface(null, Typeface.BOLD);
        } else if (Integer.parseInt(quantity) == 0) {
            totalquantity.setText("Out of Stock");
            totalquantity.setTextColor(Color.RED);
            totalquantity.setTypeface(null, Typeface.BOLD);
        }
    }

    ///product info
    private void prodinfo() {
        final String prod_key = getIntent().getStringExtra("PROD_KEY");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                productModel = dataSnapshot.child("AllProducts").child(prod_key).getValue(ProductModel.class);
                tvprodname.setText(productModel.getName());
                tvproddesc.setText(productModel.getDescription());
                tvprodprice.setText("₹ " + productModel.getPrice());
                prod_quantity = productModel.getQuantity();
                quantity = productModel.getQuantity();
                picasso.get().load(productModel.getImageurl()).placeholder(R.drawable.loading).into(imageView);
                checkqty();


                qty = new String[Integer.parseInt(prod_quantity) + 1];
                qty[0] = "-Qty-";
                for (int i = 1; i <= Integer.parseInt(prod_quantity); i++) {
                    qty[i] = String.valueOf(i);

                }
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SelectedProductActivity.this, android.R.layout.simple_list_item_1, qty);
                squantity.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    ///product info

    ///rating coding start
    /*private void showRatings() {
        final String prod_key = getIntent().getStringExtra("PROD_KEY");
        final int stars[] = new int[5];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < 5; i++) {
                    stars[i] = (int) dataSnapshot.child("Ratings").child(prod_key).child(String.valueOf(i + 1)).getChildrenCount();
                }
                ratingstars(stars);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/
    private void showrating(String prodkey) {
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            Query queryrating1 = myRef.child("Rating").child(prodkey).orderByChild("rating").equalTo("" + (finalI + 1));
            queryrating1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    stars[finalI] = (int) snapshot.getChildrenCount();
                    ratingstars(stars);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void checkrating(final String prod_key) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user.getUid()).child("Rated Products").hasChildren()) {
                    if (dataSnapshot.child("Users").child(user.getUid()).child("Rated Products").hasChild(prod_key)) {
                        ratingcontainer.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.INVISIBLE);
                        ratingsubmit.setVisibility(View.INVISIBLE);
                    } else {
                        ratingbar(prod_key);
                    }
                } else {
                    ratingbar(prod_key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ratingbar(final String prod_key) {
        ratingcontainer.setVisibility(View.VISIBLE);
        final int[] rate = new int[1];


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate[0] = (int) rating;
            }
        });
        ratingsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mcomment = comment.getText().toString().trim();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                String formattedDate = df.format(c).trim();
                setratingbar(prod_key, rate[0], mcomment, formattedDate);
            }
        });
    }

    private void ratingstars(int[] stars) {
        float totalstars = stars[0] + stars[1] + stars[2] + stars[3] + stars[4];
        if (totalstars == 0) {
            totalratings.setText("0 Ratings");
            total1stars.setText("0");
            total2stars.setText("0");
            total3stars.setText("0");
            total4stars.setText("0");
            total5stars.setText("0");
            averagerating.setText("0");
        } else {
            totalratings.setText(totalstars + " Ratings");
            total1stars.setText("" + stars[0]);
            total2stars.setText("" + stars[1]);
            total3stars.setText("" + stars[2]);
            total4stars.setText("" + stars[3]);
            total5stars.setText("" + stars[4]);

            float average = (((5 * stars[4]) + (4 * stars[3]) + (3 * stars[2]) + (2 * stars[1]) + (stars[0])) / totalstars);
            String float_average = String.format("%.1f", average);
            averagerating.setText(float_average);
            float progress1 = (stars[0] / totalstars) * 100;
            float progress2 = (stars[1] / totalstars) * 100;
            float progress3 = (stars[2] / totalstars) * 100;
            float progress4 = (stars[3] / totalstars) * 100;
            float progress5 = (stars[4] / totalstars) * 100;

            progressBar1stars.setProgress((int) progress1);
            progressBar2stars.setProgress((int) progress2);
            progressBar3stars.setProgress((int) progress3);
            progressBar4stars.setProgress((int) progress4);
            progressBar5stars.setProgress((int) progress5);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                progressBar1stars.setProgressTintList(ColorStateList.valueOf(Color.RED));
                progressBar2stars.setProgressTintList(ColorStateList.valueOf(Color.RED));
                progressBar3stars.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                progressBar4stars.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                progressBar5stars.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

            }
        }
    }

    private void setratingbar(final String prodkey, int rate, String mcomment, String formattedDate) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (rate > 0) {
            ReviewModel reviewModel = new ReviewModel();
            reviewModel.setUsername(username);
            reviewModel.setDate(formattedDate);
            reviewModel.setComment(mcomment);
            reviewModel.setImgurl(imageurl);
            reviewModel.setUser_key(user.getUid());
            reviewModel.setRating(String.valueOf(rate));
            myRef.child("Rating").child(prodkey).child(user.getUid()).setValue(reviewModel);
            myRef.child("Users").child(user.getUid()).child("Rated Products").child(prodkey).setValue(prodkey);
            ratingcontainer.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            ratingsubmit.setVisibility(View.GONE);
            Toast.makeText(this, "Rated", Toast.LENGTH_SHORT).show();
            checkrating(prodkey);
            showrating(prodkey);
//                showRatings();


//            myRef.child("Ratings").child(prodkey).child(user.getUid()).setValue(prodkey);
//            myRef.child("Users").child(user.getUid()).child("Rated Products").child(prodkey).setValue(prodkey);
        } else {
            Toast.makeText(SelectedProductActivity.this, "Rating should be at least 1 star", Toast.LENGTH_SHORT).show();
        }
    }

    ///rating coding end
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_btn_add_to_cart_selected_product:
                if (squantity.getSelectedItem() != "-Qty-") {
                    addtocart();
                    Snackbar.make(selectedcontainer, "Product Added in Cart", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(selectedcontainer, "Select Quantity", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.user_btn_place_order_selected_product:
                if (squantity.getSelectedItem() != "-Qty-") {
                    String order_qty = squantity.getSelectedItem().toString();
                    placeorder(order_qty);
                } else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Select Quantity", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void addtocart() {
        CartModel cartModel = new CartModel();
        final String prod_key = getIntent().getStringExtra("PROD_KEY");
        cartModel.setCart_qty(squantity.getSelectedItem().toString());
        cartModel.setProd_key(prod_key);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myRef.child("Users").child(user.getUid()).child("Cart").child(prod_key).setValue(cartModel);
    }

    private void placeorder(final String order_qty) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user.getUid()).child("address").exists()) {
                    final String prod_key = getIntent().getStringExtra("PROD_KEY");
                    CartModel cartModel = new CartModel();
                    cartModel.setCart_qty(squantity.getSelectedItem().toString());
                    cartModel.setProd_key(prod_key);
                    ArrayList<CartModel> cartModelArrayList = new ArrayList<>();
                    cartModelArrayList.add(cartModel);

                    startActivity(new Intent(SelectedProductActivity.this, OrderActivity.class)
                            .putExtra("LIST", (Serializable) cartModelArrayList));

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectedProductActivity.this);
                    View v = LayoutInflater.from(SelectedProductActivity.this).inflate(R.layout.raw_dialog_okcancel_textview, null);
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
                            startActivity(new Intent(SelectedProductActivity.this, AddressActivity.class));
                        }
                    });
                    buttoncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            StyleableToast.makeText(SelectedProductActivity.this, "Address is required to Place Order", R.style.exampletoast).show();
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
