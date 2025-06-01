package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.Model.AddProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Product_Description extends AppCompatActivity {
    //views
    Toolbar toolbar;
    Picasso picasso;
    ProgressDialog progressDialog;
    EditText name, price, quantity, description;
    TextView category, sub_category;
    ImageView imageView;
    Button update;

    Uri image_uri;
    public static final int OPEN_GALLERY = 1;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();


    //vars
    String pname;
    String pprice;
    String pcat;
    String psub_cat;
    String pdesc;
    String pqty;
    String pimageurl;
    private String[] categories;
    private String[] subcategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__description);
        toolbar = findViewById(R.id.admin_description_toolbar);
        progressDialog=new ProgressDialog(this);
        inittoolbar();


        final String key = getIntent().getStringExtra("key");

        imageView = findViewById(R.id.admin_image_fromdatabase);
        name = findViewById(R.id.admin_name_fromdatabase);
        price = findViewById(R.id.admin_price_fromdatabase);
        category = findViewById(R.id.admin_category_fromdatabase);
        sub_category = findViewById(R.id.admin_subcategory_fromdatabase);
        quantity = findViewById(R.id.admin_qty_fromdatabase);
        update = findViewById(R.id.admin_btn_update);
        description = findViewById(R.id.admin_description_fromdatabase);
//        categorydata();

        setfields(key);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateproduct(key);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_PICK);
                startActivityForResult(i, OPEN_GALLERY);
            }
        });
    }

//    private void categorydata() {
//        final String key = getIntent().getStringExtra("key");
//
//        progressDialog.show();
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int size = (int) dataSnapshot.child("Categories").getChildrenCount();
//                categories = new String[size + 2];
//                int i = 1;
////                categories[0] = "--Select Category--";
//                categories[0] = dataSnapshot.child("AllProducts").child(key).child("category").getValue(String.class);
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").getChildren()) {
////                    if(!categories[0].equals(dataSnapshot1.getKey())){
//                        categories[i] = dataSnapshot1.getKey();
//                        i++;
////                    }
//                }
//                progressDialog.dismiss();
////                categories[i] = "Add New Category";
//                categories[i] = "Add New Category";
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Product_Description.this,
//                        android.R.layout.simple_spinner_dropdown_item, categories);
//                category.setAdapter(arrayAdapter);
//
//                category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        if (position != categories.length - 1) {
//                            category.setSelection(position, true);
//                            sub_category.setEnabled(true);
//                            subcategorydata();
//                            category.getDropDownVerticalOffset();
//                        }
//                        if (position == categories.length - 1) {
//                            startActivity(new Intent(Product_Description.this, AddNewCategoryActivity.class));
//                            category.setSelection(0, true);
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void subcategorydata() {
//
//        final String key = getIntent().getStringExtra("key");
//        final String catname = category.getSelectedItem().toString();
//        final ArrayAdapter<String> cat_adapter = new ArrayAdapter<String>(Product_Description.this, android.R.layout.simple_list_item_1);
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int size = (int) dataSnapshot.child("Categories").child(catname).getChildrenCount();
//                subcategories = new String[size];
//                int j = 1;
////                subcategories[0] = "--Select Sub Category--";
//                subcategories[0] = dataSnapshot.child("AllProducts").child(key).child("subcategory").getValue(String.class);
//
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").child(catname).getChildren()) {
//                    if (dataSnapshot1.getKey().equals("catimageurl") || dataSnapshot1.getKey().equals("catname")) {
//
//                    } else {
//                        subcategories[j] = dataSnapshot1.getKey();
//                        j++;
//                    }
//                }
//                subcategories[j] = "Add New Sub-Category";
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Product_Description.this,
//                        android.R.layout.simple_spinner_dropdown_item, subcategories);
//                sub_category.setAdapter(arrayAdapter);
//
//                sub_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        if (position != 0 && position != subcategories.length -1) {
//                            sub_category.setSelection(position, true);
//                            sub_category.getDropDownVerticalOffset();
//                        }
//                        if (position == subcategories.length - 1) {
//                            startActivity(new Intent(Product_Description.this, AddSubCategoryActivity.class));
//                            sub_category.setSelection(0, true);
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_GALLERY) {
            if (data == null) {
                Toast.makeText(this, "Pick a image", Toast.LENGTH_SHORT).show();
            } else {
                image_uri = data.getData();
                imageView.setImageURI(image_uri);
            }
        }
    }

    private void updateproduct(final String key) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        final StorageReference riversRef = mStorageRef.child("Product_Image").child(key + ".jpg");
        if(image_uri!=null){
        riversRef.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updatefields(uri);
                                progressDialog.dismiss();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Product_Description.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        }else {
            final String[] url = {getIntent().getStringExtra("imageurl")};

            AddProductModel addProductModel = new AddProductModel();
            addProductModel.setKey(key);
            addProductModel.setName(name.getText().toString());
            addProductModel.setPrice(price.getText().toString());
            addProductModel.setCategory(pcat);
            addProductModel.setSubcategory(psub_cat);
            addProductModel.setQuantity(quantity.getText().toString());
            addProductModel.setDescription(description.getText().toString());
            addProductModel.setImageurl(url[0]);
            myRef.child("AllProducts").child(key).setValue(addProductModel);
            progressDialog.dismiss();
            finish();

        }
    }

    private void updatefields(Uri uri) {
        final String[] url = {getIntent().getStringExtra("imageurl")};
        final String key = getIntent().getStringExtra("key");

        AddProductModel addProductModel = new AddProductModel();
        url[0] = String.valueOf(uri);
        addProductModel.setKey(key);
        addProductModel.setName(name.getText().toString());
        addProductModel.setPrice(price.getText().toString());
        addProductModel.setCategory(pcat);
        addProductModel.setSubcategory(psub_cat);
        addProductModel.setQuantity(quantity.getText().toString());
        addProductModel.setDescription(description.getText().toString());
        addProductModel.setImageurl(url[0]);
        myRef.child("AllProducts").child(key).setValue(addProductModel);
    }

    private void inittoolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Update Product");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setfields(final String key) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AddProductModel addProductModel = dataSnapshot.child("AllProducts").child(key).getValue(AddProductModel.class);
                if (addProductModel == null) {
                    finish();
                } else {
                    pname = addProductModel.getName();
                    pprice = addProductModel.getPrice();
                    pcat = addProductModel.getCategory();
                    psub_cat = addProductModel.getSubcategory();
                    pdesc = addProductModel.getDescription();
                    pqty = addProductModel.getQuantity();
                    pimageurl = addProductModel.getImageurl();

                    name.setText(pname);
                    price.setText(pprice);
                    category.setText(pcat);
                    sub_category.setText(psub_cat);
                    description.setText(pdesc);
                    quantity.setText(pqty);
                    picasso.get().load(pimageurl).fit().placeholder(R.drawable.ic_loading).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Product_Description.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.product_description_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                final String key = getIntent().getStringExtra("key");
                if (key != null) {
                    mStorageRef.child("Product_Image").child(key + ".jpg").delete();
                    myRef.child("AllProducts").child(key).removeValue();
                    myRef.child("Category").child(pcat).child(psub_cat).child(key).removeValue();
                    finish();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

