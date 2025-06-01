package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.admin.Adapter.CategoryRecyclerAdapter;
import com.example.admin.Model.CategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ViewCategoryActivity extends AppCompatActivity {

    //views
    Toolbar toolbar;
    RecyclerView catlist;

    //vars
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    private boolean isMultiSelect = false;
    ArrayList<CategoryModel> multi_select_category_list = new ArrayList<>();
    private ActionMode actionMode;

    //firebase
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        catlist = findViewById(R.id.admin_recycler_categorylist_updatecatsubcat);

        inittoolbar();
        initcatlist();
    }

    private void initcatlist() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        catlist.setLayoutManager(new LinearLayoutManager(this));
        catlist.setHasFixedSize(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryModelArrayList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").getChildren()) {
                    CategoryModel categoryModel = dataSnapshot1.getValue(CategoryModel.class);
                    categoryModelArrayList.add(categoryModel);
                }
                categoryRecyclerAdapter = new CategoryRecyclerAdapter(ViewCategoryActivity.this, categoryModelArrayList);
                catlist.setAdapter(categoryRecyclerAdapter);
                OnClick();
                categoryRecyclerAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void OnClick() {


        categoryRecyclerAdapter.setOnItemClickListener(new CategoryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {

                if (isMultiSelect) {
                    multi_select(v, position);
                } else {
                    Toast.makeText(ViewCategoryActivity.this, categoryModelArrayList.get(position).getCatname(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void OnOptionClick(View v, final int position) {

                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.getMenuInflater();
                popupMenu.inflate(R.menu.view_category_menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                deletedialog(position);
                                break;
                            case R.id.rename:

                                AlertDialog.Builder builder = new AlertDialog.Builder(ViewCategoryActivity.this);
                                View rootview = LayoutInflater.from(ViewCategoryActivity.this).inflate(R.layout.raw_dialog_rename, null);
                                builder.setView(rootview);
                                final AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                Button ok = rootview.findViewById(R.id.admin_dialog_btn_ok);
                                Button cancel = rootview.findViewById(R.id.admin_dialog_btn_cancel);
                                final TextInputLayout dialog_categoryname = rootview.findViewById(R.id.admin_dialog_edt_name);
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String newcatname = dialog_categoryname.getEditText().getText().toString().trim();
                                        if (!newcatname.isEmpty()) {
                                            String finalcatname = newcatname.toUpperCase().charAt(0) + newcatname.toLowerCase().substring(1);

                                            renamecategory(position, finalcatname);
                                            alertDialog.dismiss();
                                        } else {
                                            dialog_categoryname.setError("Field is Required");
                                            dialog_categoryname.requestFocus();
                                        }
                                    }

                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });

            }

            @Override
            public void OnLongClick(View v, int position) {
                if (!isMultiSelect) {
                    isMultiSelect = true;
                    if (actionMode == null) {
                        actionMode = startActionMode(mactionmodecallback);
                    }
                }
                multi_select(v, position);
            }
        });
    }

    private void renamecategory(final int position, final String newcatname) {


        final ArrayList<String> renamecategoryarraylist = new ArrayList<>();
        String categoryname = categoryModelArrayList.get(position).getCatname();
        Query rename_category_query = myRef.child("AllProducts").orderByChild("category").equalTo(categoryname);

        rename_category_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                renamecategoryarraylist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String key = dataSnapshot1.getKey();
                    renamecategoryarraylist.add(key);
                }
                for (int i = 0; i < renamecategoryarraylist.size(); i++) {
                    myRef.child("AllProducts").child(renamecategoryarraylist.get(i)).child("category").setValue(newcatname);
                }
                myRef.child("Categories").child(categoryModelArrayList.get(position).getCatkey()).child("catname").setValue(newcatname);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deletedialog(final int position) {
        final int[] prod_count = new int[1];
        final int[] sub_cat_count = new int[1];
        Query productquery = myRef.child("AllProducts").orderByChild("category").equalTo(categoryModelArrayList.get(position).getCatname());
        productquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int prod = (int) dataSnapshot.getChildrenCount();

                prod_count[0] = prod;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sub_cat = (int) dataSnapshot.child("Categories").child(categoryModelArrayList.get(position).getCatkey()).getChildrenCount();
                sub_cat_count[0] = sub_cat - 3;
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewCategoryActivity.this);

                builder.setMessage("This will result in removing " + prod_count[0] + " Products and " + sub_cat_count[0] + " Sub-Categories");
                builder.setTitle("Delete!!");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        multi_select_category_list.clear();
                        multi_select_category_list.add(categoryModelArrayList.get(position));
                        deletecategorydata();
                        dialog.cancel();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void multi_select(View view, int position) {
        if (actionMode != null) {
            if (multi_select_category_list.contains(categoryModelArrayList.get(position))) {
                multi_select_category_list.remove(categoryModelArrayList.get(position));
                view.setBackgroundColor(Color.WHITE);
            } else {
                multi_select_category_list.add(categoryModelArrayList.get(position));
                view.setBackgroundColor(Color.GRAY);
            }
            if (multi_select_category_list.size() > 0) {
                actionMode.setTitle("" + multi_select_category_list.size());
            } else {
                actionMode.finish();
                isMultiSelect = false;
            }
        }
    }

    private ActionMode.Callback mactionmodecallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.product_description_menu, menu);
            toolbar.setVisibility(View.GONE);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    deletecategorydata();
                    mode.finish();
                    toolbar.setVisibility(View.VISIBLE);
                    multi_select_category_list.clear();
                    initcatlist();
                    return true;

                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multi_select_category_list.clear();
            actionMode = null;
            toolbar.setVisibility(View.VISIBLE);
            isMultiSelect = false;
        }
    };

    private void deletecategorydata() {

        for (int i = 0; i < multi_select_category_list.size(); i++) {
            final ArrayList<String> deleteproductkeylist = new ArrayList<>();
            Query query = myRef.child("AllProducts").orderByChild("category").equalTo(multi_select_category_list.get(i).getCatname());
            // delete products
            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String key = dataSnapshot1.getKey();
                        deleteproductkeylist.add(key);
                    }
                    for (int j = 0; j < deleteproductkeylist.size(); j++) {

                        final int finalJ = j;
                        mStorageRef.child("Product_Image").child(deleteproductkeylist.get(j) + ".jpg").delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            myRef.child("AllProducts").child(deleteproductkeylist.get(finalJ)).removeValue();
                                        }
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //to delete subcategory
            for (int a = 0; a < multi_select_category_list.size(); a++) {

                final ArrayList<String> subcategorykeylist = new ArrayList<>();
                final String catkey = multi_select_category_list.get(a).getCatkey();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.child("Categories").child(catkey).getChildren()) {
                            if (dataSnapshot.hasChildren()) {
                                String subcatkey = dataSnapshot.getKey();
                                subcategorykeylist.add(subcatkey);
                            }
                        }
                        for (int s = 0; s < subcategorykeylist.size(); s++) {
                            final int finalS = s;
                            mStorageRef.child("Sub_Category_Image").child(subcategorykeylist.get(finalS) + ".jpg").delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                myRef.child("Categories").child(catkey).child(subcategorykeylist.get(finalS)).removeValue();
                                            } else {
                                                Toast.makeText(ViewCategoryActivity.this, "Removing Sub-Categories failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            //to delete category
            for (int b = 0; b < multi_select_category_list.size(); b++) {
                final String catkey = multi_select_category_list.get(b).getCatkey();
                mStorageRef.child("Category_Image").child(catkey + ".jpg").delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    myRef.child("Categories").child(catkey).removeValue();
                                } else {
                                    Toast.makeText(ViewCategoryActivity.this, "Removing Failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

            initcatlist();
        }

    }


    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_update_sub_category_toolbar);
        toolbar.setTitle("All Categories");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
