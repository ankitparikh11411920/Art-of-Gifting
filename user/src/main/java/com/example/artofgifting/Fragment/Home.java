package com.example.artofgifting.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artofgifting.Adapters.HomeHorizontalCategoryRecyclerAdapter;
import com.example.artofgifting.Adapters.HomeRecyclerAdapter;
import com.example.artofgifting.Adapters.ProductRecyclerAdapter;
import com.example.artofgifting.CartActivity;
import com.example.artofgifting.Models.CategoryModel;
import com.example.artofgifting.Models.ProductModel;
import com.example.artofgifting.R;
import com.example.artofgifting.RecentlyViewedActivity;
import com.example.artofgifting.SearchlistActivity;
import com.example.artofgifting.SliderAdapter;
import com.example.artofgifting.SelectedProductActivity;
import com.example.artofgifting.ViewAllSubCatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class Home extends Fragment {

    //views
    private SliderView sliderView;
    private RecyclerView reccatname, horreccatname,rec_recentviewuser;
    private Toolbar toolbar;
    private ProgressBar progcat1, progslider;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private TextView textCartItemCount;
    private LinearLayout linearLayout;
    private Button recentviewall;


    //vars
    private ArrayList<String> adbannerarraylist;
    private ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    private ArrayList<ProductModel>  productModelArrayList1=new ArrayList<>();
    ProductRecyclerAdapter productAdapter;
    private ArrayList<CategoryModel> catnamearraylist;
    final ArrayList<String> prodkeyarraylist = new ArrayList<>();


    //firebase
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private HomeHorizontalCategoryRecyclerAdapter homeHorizontalCategoryRecyclerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.home, container, false);

        sliderView = rootview.findViewById(R.id.imageSlider);
        toolbar = getActivity().findViewById(R.id.home_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        reccatname = rootview.findViewById(R.id.rec_cat_disp);
        horreccatname = rootview.findViewById(R.id.horizontal_reccat_home);
        rec_recentviewuser = rootview.findViewById(R.id.rec_recentviewuser_homefragment);
        linearLayout = rootview.findViewById(R.id.lnr_recentviewed_home);
        recentviewall=rootview.findViewById(R.id.user_btn_viewall_recent_home);

        drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        progslider = rootview.findViewById(R.id.slider_progress);
        progcat1 = rootview.findViewById(R.id.prog_cat1);

        recentviewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecentlyViewedActivity.class));
            }
        });
        /*((AppCompatActivity)getActivity())
                .getSupportActionBar();
        */

        toolbar.setTitle("Home");
//        postbadge();
        horizontalcatdisp();
        slidershow();
        catdisp();
        recentviewedproddisp();

        return rootview;
    }

    private void horizontalcatdisp() {
        horreccatname.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").getChildren()) {
                    CategoryModel categoryModel = dataSnapshot1.getValue(CategoryModel.class);
                    categoryModelArrayList.add(categoryModel);
                }
                homeHorizontalCategoryRecyclerAdapter = new HomeHorizontalCategoryRecyclerAdapter(getContext(), categoryModelArrayList);
                horreccatname.setAdapter(homeHorizontalCategoryRecyclerAdapter);
                homeHorizontalCategoryRecyclerAdapter.notifyDataSetChanged();
                onhorcatclick();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onhorcatclick() {
        homeHorizontalCategoryRecyclerAdapter.OnItemClick(new HomeHorizontalCategoryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                startActivity(new Intent(getContext(), ViewAllSubCatActivity.class)
                        .putExtra("HORIZONTALCATNAME", categoryModelArrayList.get(position).getCatname())
                .putExtra("HORIZONTALCATKEY",categoryModelArrayList.get(position).getCatkey()));
            }
        });
    }

    private void catdisp() {
        reccatname.setLayoutManager(new LinearLayoutManager(getActivity()));
        reccatname.setHasFixedSize(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        progcat1.setVisibility(View.VISIBLE);
        catnamearraylist = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                catnamearraylist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").getChildren()) {
                    progcat1.setVisibility(View.INVISIBLE);
                    CategoryModel categoryModel=dataSnapshot1.getValue(CategoryModel.class);
                    catnamearraylist.add(categoryModel);
                }
                HomeRecyclerAdapter homeRecyclerAdapter = new HomeRecyclerAdapter(getContext(), catnamearraylist);
                reccatname.setAdapter(homeRecyclerAdapter);
                homeRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void slidershow() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Ad Banner");
        progslider.setVisibility(View.VISIBLE);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adbannerarraylist = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String adbanner = dataSnapshot1.getValue(String.class);
                    adbannerarraylist.add(adbanner);
                }

                if (!adbannerarraylist.isEmpty()) {
                    progslider.setVisibility(View.INVISIBLE);
                    sliderView.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    SliderAdapter sliderAdapter = new SliderAdapter(getContext(), adbannerarraylist);
                    sliderView.setSliderAdapter(sliderAdapter);
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                    sliderView.setSliderAnimationDuration(800);
                    sliderView.setIndicatorAnimationDuration(800);
                    sliderView.setScrollTimeInSec(8); //set scroll delay in seconds :
                    sliderView.startAutoCycle();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //menu option menu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_toolbar_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        final View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        textCartItemCount.setVisibility(View.GONE);
        QuantityThread quantityThread=new QuantityThread();
        quantityThread.start();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
    }

    class QuantityThread extends Thread{
        @Override
        public void run() {
            postbadge();
        }
    }

    private void postbadge() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user.getUid()).child("Cart").exists()) {
                    int item = (int) dataSnapshot.child("Users").child(user.getUid()).child("Cart").getChildrenCount();
                    if (item == 0) {
                        textCartItemCount.setVisibility(View.GONE);
                    } else {
                        textCartItemCount.setText(String.valueOf(Math.min(item, 99)));
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                } else {
                    textCartItemCount.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart: {
                startActivity(new Intent(getActivity(), CartActivity.class));
                break;
            }
            case R.id.search: {
                startActivity(new Intent(getActivity(), SearchlistActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                QuantityThread quantityThread=new QuantityThread();
                quantityThread.start();
            }
        },1000);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(user.getUid()).hasChild("RecentViewed")){
                   linearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearLayout.setVisibility(View.GONE);
                    prodkeyarraylist.clear();
                    productModelArrayList1.clear();
                    rec_recentviewuser.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recentviewedproddisp() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rec_recentviewuser.setLayoutManager(gridLayoutManager);
        rec_recentviewuser.setHasFixedSize(true);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(user.getUid()).child("RecentViewed").exists()) {
                    prodkeyarraylist.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Users").child(user.getUid()).child("RecentViewed").getChildren()) {
                        String prodkey = dataSnapshot1.getKey();
                        prodkeyarraylist.add(prodkey);
                    }
                    productModelArrayList1.clear();
                    for (int i = 0; i < prodkeyarraylist.size(); i++) {
                        ProductModel productModel = dataSnapshot.child("AllProducts").child(prodkeyarraylist.get(i)).getValue(ProductModel.class);
                        productModelArrayList1.add(productModel);

                    }
                    if(productModelArrayList1.size()>4){
                        recentviewall.setVisibility(View.VISIBLE);
                        productAdapter = new ProductRecyclerAdapter(getContext(), productModelArrayList1,4,0);
                    }else {
                        recentviewall.setVisibility(View.GONE);
                        productAdapter = new ProductRecyclerAdapter(getContext(), productModelArrayList1,productModelArrayList1.size(),0);
                    }
                    rec_recentviewuser.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                    productAdapter.OnItemClick(new ProductRecyclerAdapter.OnItemCLickListener() {
                        @Override
                        public void OnItemClickListener(int position) {
                            startActivity(new Intent(getActivity(), SelectedProductActivity.class)
                                    .putExtra("PROD_KEY", productModelArrayList1.get(position).getKey()));
                        }
                    });
                    checkrecentdata();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkrecentdata() {
        if (productModelArrayList1.isEmpty()) {
            rec_recentviewuser.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);

        } else {
            rec_recentviewuser.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}

