package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.Model.CategoryModel;
import com.example.admin.Model.SubCategoryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class AddSubCategoryActivity extends AppCompatActivity {
    //view
    Toolbar toolbar;
    Spinner categoryspinner;
    ImageView subcatimage;
    TextView subcatimagetext;
    Button submit;
    AutoCompleteTextView subcategoryname;
    ProgressDialog progressDialog;

    ///firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    //vars
    String categories[];
    private Uri image_uri;
    public static final int OPEN_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_category);
        toolbar = findViewById(R.id.admin_addsubcategory_toolbar);
        categoryspinner = findViewById(R.id.admin_spinner_category_name_add_subcategory);
        subcatimagetext = findViewById(R.id.textpickimage_addnew_subcategory);
        subcatimage = findViewById(R.id.admin_img_subcat_image_addnew_subcategory);
        submit = findViewById(R.id.admin_btn_submit_addnew_subcategory);
        subcategoryname = findViewById(R.id.admin_actv_subcatname_addsub_category);
        progressDialog = new ProgressDialog(this);

        subcatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_PICK);
                startActivityForResult(i, OPEN_GALLERY);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_uri == null) {
                    Toast.makeText(AddSubCategoryActivity.this, "Sub-category Image is required", Toast.LENGTH_SHORT).show();
                    subcatimagetext.setTextColor(Color.RED);
                    checkfields();
                } else {
                    subcatimagetext.setVisibility(View.GONE);
                    checkfields();
                }
            }
        });

        inittoolbar();
        spinnerdata();


    }

    private void checkfields() {
        if (categoryspinner.getSelectedItem().toString().equals("--Select Category--")) {
            Toast.makeText(this, "Select a Category", Toast.LENGTH_SHORT).show();
            if (subcategoryname.getText().toString().trim().isEmpty()) {
                subcategoryname.setError("Field can't be Empty");
                subcategoryname.requestFocus();
                refresherror();
            }

        } else {
            subcategoryname.setEnabled(true);
            if (subcategoryname.getText().toString().trim().isEmpty()) {
                subcategoryname.setError("Field can't be Empty");
                refresherror();
            } else {
                createsubcategory();
            }
        }
    }

    private void refresherror() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                subcategoryname.setError(null);
                subcategoryname.clearFocus();
                subcatimagetext.setTextColor(Color.BLACK);
            }
        }, 1500);
    }

    private void createsubcategory() {
        progressDialog.show();
        progressDialog.setMessage("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        String subcatname = subcategoryname.getText().toString();
        final String subcatkey = myRef.push().getKey();
        final String finalsubcatname = subcatname.toUpperCase().charAt(0) + subcatname.substring(1);
        final StorageReference riversRef = mStorageRef.child("Sub_Category_Image").child(subcatkey + ".jpg");

        riversRef.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = String.valueOf(uri);

                        final ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
                        final SubCategoryModel subCategoryModel = new SubCategoryModel();
                        subCategoryModel.setSubcatname(finalsubcatname);
                        subCategoryModel.setSubcatkey(subcatkey);
                        subCategoryModel.setSubcatimageurl(url);
                        Query query = myRef.child("Categories").orderByChild("catname").equalTo(categoryspinner.getSelectedItem().toString());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                categoryModelArrayList.clear();
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        CategoryModel categoryModel = dataSnapshot1.getValue(CategoryModel.class);
                                        categoryModelArrayList.add(categoryModel);
                                    }
                                    myRef.child("Categories").child(categoryModelArrayList.get(0).getCatkey()).child(subcatkey).setValue(subCategoryModel);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        progressDialog.dismiss();
                        subcategoryname.setText(null);
                        subcategoryname.clearFocus();
                        subcatimage.setImageResource(R.drawable.ic_add_a_photo);
                        categoryspinner.setSelection(0, true);
                        subcatimagetext.setVisibility(View.VISIBLE);
                        Toast.makeText(AddSubCategoryActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSubCategoryActivity.this, "Uploading Image Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void spinnerdata() {
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.child("Categories").getChildrenCount();
                categories = new String[size + 2];
                int i = 1;
                categories[0] = "--Select Category--";
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").getChildren()) {
                    categories[i] = dataSnapshot1.child("catname").getValue(String.class);
                    i++;
                }
                progressDialog.dismiss();
                categories[i] = "Add New Category";
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddSubCategoryActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, categories);
                categoryspinner.setAdapter(arrayAdapter);

                categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0 && position != categories.length - 1) {
                            categoryspinner.setSelection(position, true);
                            subcategoryname.setEnabled(true);
                            subcategorysuggestions();
                            categoryspinner.getDropDownVerticalOffset();
                        }
                        if (position == categories.length - 1) {
                            startActivity(new Intent(AddSubCategoryActivity.this, AddNewCategoryActivity.class));
                            categoryspinner.setSelection(0, true);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void inittoolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Sub-Category");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void subcategorysuggestions() {
        final String catname = categoryspinner.getSelectedItem().toString();
        final ArrayAdapter<String> cat_adapter = new ArrayAdapter<String>(AddSubCategoryActivity.this, android.R.layout.simple_list_item_1);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").child(catname).getChildren()) {
                    String kye = dataSnapshot1.getKey();
                    cat_adapter.add(kye);
                    cat_adapter.remove("catimageurl");
                    cat_adapter.remove("catname");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddSubCategoryActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        subcategoryname.setAdapter(cat_adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_GALLERY) {
            if (data == null) {
                subcatimagetext.setTextColor(Color.RED);
                refresherror();
                Toast.makeText(this, "Pick a image", Toast.LENGTH_SHORT).show();
            } else {
                subcatimagetext.setVisibility(View.GONE);
                image_uri = data.getData();
                subcatimage.setImageURI(image_uri);
            }
        }
    }
}
