package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.admin.Model.AddProductModel;
import com.example.admin.Adapter.RecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ShowAllProductsActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ArrayList<AddProductModel> productModelArrayList = new ArrayList<>();
    RadioGroup radioGroup;
    ProgressBar progressBar;
    RadioButton radioButton;
    Toolbar toolbar;

    //firebase
    DatabaseReference myRef=FirebaseDatabase.getInstance().getReference();
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    //vars
    int sortid = R.id.sort_none;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_products);
        recyclerView = findViewById(R.id.admin_allproducts_recyclerview_showallproductsactivity);
        fab = findViewById(R.id.admin_floating_add_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressBar = findViewById(R.id.admin_product_display_progressbar);
        inittoolbar();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Add_Product.class));
            }
        });

        editdatabase();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_showallproducts_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("All Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedialog(position);
                    adapter.notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:
                    deletedialog(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed))
                    .addActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    private void editdatabase() {
        progressBar.setVisibility(View.VISIBLE);
        productModelArrayList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                productModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("AllProducts").getChildren()) {
                    AddProductModel productModel = dataSnapshot1.getValue(AddProductModel.class);
                    productModelArrayList.add(productModel);
                    if (!productModelArrayList.isEmpty()) {

                        progressBar.setVisibility(View.INVISIBLE);
                        adapter = new RecyclerAdapter(ShowAllProductsActivity.this, productModelArrayList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(final int position, View view) {
                                Intent i = new Intent(getApplicationContext(), Product_Description.class);
                                i.putExtra("imageurl", productModelArrayList.get(position).getImageurl());
                                i.putExtra("key", productModelArrayList.get(position).getKey());
                                startActivity(i);
                            }

                            @Override
                            public void onOptionClick(final int position, View view) {
                                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                                popupMenu.getMenuInflater();
                                popupMenu.inflate(R.menu.product_description_menu);
                                popupMenu.show();
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.delete:
                                                deletedialog(position);
                                                break;
                                        }
                                        return true;
                                    }
                                });
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletedialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Delete ?");
        builder.setTitle("Delete");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String key = productModelArrayList.get(position).getKey();
                        String categoryname = productModelArrayList.get(position).getCategory();
                        String subcat = productModelArrayList.get(position).getSubcategory();
                        productModelArrayList.remove(position);
                        mStorageRef.child("Product_Image").child(key + ".jpg").delete();
                        myRef.child("AllProducts").child(key).removeValue();
                        myRef.child("Category").child(categoryname).child(subcat).child(key).removeValue();
                        adapter.notifyItemRemoved(position);
                    }
                });
        builder.setNegativeButton(
                "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        adapter.notifyDataSetChanged();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_fragment_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.product_fragment_menu_search).getActionView();

        if(productModelArrayList.isEmpty()){
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            searchView.onActionViewCollapsed();
        }
        else{
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }


        return super.onCreateOptionsMenu(menu);

    }


    //    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.product_fragment_menu_sort:
                if (productModelArrayList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                } else {
                    sortdialog();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void sortdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.raw_sort_dialog, null, false);
        final AlertDialog dialog = builder.create();
        dialog.setView(v);
        dialog.show();

        radioGroup = v.findViewById(R.id.sort_group);
        radioGroup.check(sortid);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = v.findViewById(checkedId);
                if (checkedId == R.id.sort_name_ascending) {
                    sortAZ();
                    sortid = R.id.sort_name_ascending;
                    dialog.dismiss();
                } else if (checkedId == R.id.sort_name_descending) {
                    sortZA();
                    sortid = R.id.sort_name_descending;
                    dialog.dismiss();
                } else if (checkedId == R.id.sort_price_lowtohigh) {
                    sortpriceAZ();
                    sortid = R.id.sort_price_lowtohigh;
                    dialog.dismiss();
                } else if (checkedId == R.id.sort_price_hightolow) {
                    sortpriceZA();
                    sortid = R.id.sort_price_hightolow;
                    dialog.dismiss();
                }
            }
        });
    }

    private void sortpriceZA() {
        Collections.sort(productModelArrayList, new Comparator<AddProductModel>() {
            @Override
            public int compare(AddProductModel o1, AddProductModel o2) {
                return Integer.parseInt(o2.getPrice()) - Integer.parseInt(o1.getPrice());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortpriceAZ() {
        Collections.sort(productModelArrayList, new Comparator<AddProductModel>() {
            @Override
            public int compare(AddProductModel o1, AddProductModel o2) {
                return Integer.parseInt(o1.getPrice()) - Integer.parseInt(o2.getPrice());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortAZ() {
        Collections.sort(productModelArrayList, new Comparator<AddProductModel>() {
            @Override
            public int compare(AddProductModel o1, AddProductModel o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortZA() {
        Collections.sort(productModelArrayList, new Comparator<AddProductModel>() {
            @Override
            public int compare(AddProductModel o1, AddProductModel o2) {
                return o2.getName().toLowerCase().compareTo(o1.getName().toLowerCase());
            }
        });
        adapter.notifyDataSetChanged();
    }
}
