package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.Model.AddProductModel;
import com.example.admin.Model.CategoryModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class Add_Product extends AppCompatActivity {
    //views
    TextInputLayout prod_name, prod_price, prod_quantity, prod_description;
    ProgressDialog progressDialog;
    Button add;
    Spinner category_name, subcategory_name;
    public static final String FIELD_ERROR = "Field Required";
    public static final int OPEN_GALLERY = 1;
    ImageView prod_img;
    //vars
    String name, price, category, subcategory, quantity, description;
    private Uri image_uri;
    String categories[];
    String subcategories[];
    //firebase
    private StorageReference mStorageRef;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        prod_img = findViewById(R.id.admin_img_prod_img_add_product);
        prod_name = findViewById(R.id.admin_edt_product_name_add_product);
        prod_quantity = findViewById(R.id.admin_edt_product_quantity_add_product);
        prod_price = findViewById(R.id.admin_edt_product_price_add_product);
        prod_description = findViewById(R.id.admin_edt_product_description_add_product);
        category_name = findViewById(R.id.admin_spinner_category_name_add_product);
        subcategory_name = findViewById(R.id.admin_spinner_sub_category_name_add_product);
        add = findViewById(R.id.btn_add_prod);

        categorydata();
        checkcatspinner();

        prod_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent();
                i1.setType("image/*");
                i1.setAction(Intent.ACTION_PICK);
                startActivityForResult(i1, OPEN_GALLERY);

            }
        });
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!subcategory_name.getSelectedItem().toString().equals("--Select Sub-Category--") && !subcategory_name.getSelectedItem().toString().equals("Add New Sub Category")){
                    if (image_uri == null) {
                        Toast.makeText(Add_Product.this, "Image Is Required", Toast.LENGTH_SHORT).show();
                        checkfields();
                    } else {
                        checkfields();
                    }
                }else if(subcategory_name.getSelectedItem().toString().equals("Add New Sub Category")){
                    startActivity(new Intent(Add_Product.this,AddSubCategoryActivity.class));
                    subcategory_name.setSelection(0);
                }else {
                    Toast.makeText(Add_Product.this, "Select any option", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void categorydata() {
        final ArrayList<String> catnamearraylist = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                catnamearraylist.clear();
                catnamearraylist.add("--Select Category--");
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").getChildren()) {
                    String catname = dataSnapshot1.child("catname").getValue(String.class);
                    catnamearraylist.add(catname);
                }
                catnamearraylist.add("Add New Category");
                ArrayAdapter<String> arrayAdaptercatname = new ArrayAdapter<>(Add_Product.this, android.R.layout.simple_list_item_1, catnamearraylist);
                category_name.setAdapter(arrayAdaptercatname);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Add_Product.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkcatspinner() {
        category_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String catname = category_name.getSelectedItem().toString();
                if (!catname.equals("--Select Category--") && !catname.equals("Add New Category")) {
                    subcategorydata(catname);
                    subcategory_name.setEnabled(true);

                } else if (catname.equals("Add New Category")) {
                    startActivity(new Intent(Add_Product.this, AddNewCategoryActivity.class));
                    category_name.setSelection(0, true);
                } else {
                    subcategory_name.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void subcategorydata(final String catname) {

        final ArrayList<String> subcatnamearraylist = new ArrayList<>();
        final ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference("Categories")
                .orderByChild("catname")
                .equalTo(catname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    categoryModelArrayList.clear();
                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                        CategoryModel categoryModel = dataSnapshot1.getValue(CategoryModel.class);
                        categoryModelArrayList.add(categoryModel);
                    }
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (int i = 0; i < categoryModelArrayList.size(); i++) {
                                subcatnamearraylist.clear();
                                subcatnamearraylist.add("--Select Sub-Category--");
                                for (DataSnapshot dataSnapshot1 : snapshot.child("Categories").child(categoryModelArrayList.get(i).getCatkey()).getChildren()) {
                                    if (dataSnapshot1.hasChildren()) {
                                        String subcatname = dataSnapshot1.child("subcatname").getValue(String.class);
                                        subcatnamearraylist.add(subcatname);
                                    }
                                }

                            }
                            subcatnamearraylist.add("Add New Sub Category");
                            ArrayAdapter<String> arrayAdaptersubcatname = new ArrayAdapter<>(Add_Product.this, android.R.layout.simple_list_item_1, subcatnamearraylist);
                            subcategory_name.setAdapter(arrayAdaptersubcatname);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!catkey.isEmpty()) {
//                    int size = (int) dataSnapshot.child("Categories").child(catkey).getChildrenCount();
//                    subcategories = new String[size - 3];
//                    final int[] j = {1};
//                    subcategories[0] = "--Select Sub Category--";
//                    final ArrayList<String> subcategorykeyarraylist=new ArrayList<>();
//                    subcategorykeyarraylist.clear();
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").child(catkey).getChildren()) {
//                        if(dataSnapshot1.hasChildren()){
//                            String key=dataSnapshot1.getKey();
//                            subcategorykeyarraylist.add(key);
//                        }
//                    }
//                    for(int i=0;i<subcategorykeyarraylist.size();i++){
//                        final int finalI = i;
//                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                subcategories[j[0]]=dataSnapshot.child("Categories").child(catkey).child(subcategorykeyarraylist.get(finalI)).child("subcatname").getValue(String.class);
//                                j[0]++;
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//                    subcategories[j[0]] = "Add New Sub-Category";
//
//
//                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Add_Product.this,
//                            android.R.layout.simple_spinner_dropdown_item, subcategories);
//                    subcategory_name.setAdapter(arrayAdapter);
//
//                    subcategory_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            if (position != 0 && position != subcategories.length - 1) {
//                                subcategory_name.setSelection(position, true);
//                                subcategory_name.getDropDownVerticalOffset();
//                            }
//                            if (position == subcategories.length - 1) {
//                                startActivity(new Intent(Add_Product.this, AddSubCategoryActivity.class));
//                                subcategory_name.setSelection(0, true);
//                            }
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }


    /*private void categorysuggestions() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").getChildren()) {
                    int size=(int)dataSnapshot1.getChildrenCount();
                    cat=new String[size+2];

                    for(int i=0;i<cat.length;i++){
                        cat[0]="--Select Category--";
                        String kye = dataSnapshot1.getKey();

                        cat[i]=kye;

                        cat[cat.length]="Add New Category";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Add_Product.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
         ArrayAdapter<String> cat_adapter = new ArrayAdapter<String>(Add_Product.this, android.R.layout.simple_list_item_1,cat);

        category_name.setAdapter(cat_adapter);
        categorynameonitemclick();
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_GALLERY) {
            if (data == null) {
                Toast.makeText(this, "Pick a image", Toast.LENGTH_SHORT).show();
            } else {
                image_uri = data.getData();
                prod_img.setImageURI(image_uri);
            }
        }
    }

    private void checkfields() {
        name = prod_name.getEditText().getText().toString().trim();
        quantity = prod_quantity.getEditText().getText().toString().trim();
        price = prod_price.getEditText().getText().toString().trim();
        category = category_name.getSelectedItem().toString().trim();
        subcategory = subcategory_name.getSelectedItem().toString().trim();
        description = prod_description.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            prod_name.setError(FIELD_ERROR);
            prod_name.requestFocus();
        }
        if (quantity.isEmpty()) {
            prod_quantity.setError(FIELD_ERROR);
            prod_name.requestFocus();
        }
        if (price.isEmpty()) {
            prod_price.setError(FIELD_ERROR);
            prod_name.requestFocus();
        }
        /*if (category.isEmpty()) {
            category_name.setError(FIELD_ERROR);
            prod_name.requestFocus();
        }
        if (subcategory.isEmpty()) {
            subcategory_name.setError(FIELD_ERROR);
            prod_name.requestFocus();
        }*/
                else {
            adddata(name, price, quantity, description, category, subcategory);
            finish();
        }
    }

    private void adddata(final String name, final String price, final String quantity, String description, final String category, final String subcategory) {

        final String[] url = new String[1];


        if (description.isEmpty()) {
            description = name;
        }
        final String key = myRef.push().getKey();
        final StorageReference riversRef = mStorageRef.child("Product_Image").child(key + ".jpg");

        final String finalDescription = description;
        riversRef.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        url=String.valueOf(uri);
                        url[0] = String.valueOf(uri);
                        AddProductModel addProductModel = new AddProductModel();
                        addProductModel.setKey(key);
                        addProductModel.setName(name);
                        addProductModel.setDescription(finalDescription);
                        addProductModel.setCategory(category);
                        addProductModel.setSubcategory(subcategory);
                        addProductModel.setPrice(price);
                        addProductModel.setQuantity(quantity);
                        addProductModel.setImageurl(url[0]);
                        myRef.child("AllProducts").child(key).setValue(addProductModel);
//                        myRef.child("Categories").child(category).child(subcategory).child("Products").child(key).setValue(key);
                    }
                });
            }

        });

        Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show();
    }
}
