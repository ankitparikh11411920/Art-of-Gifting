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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.Model.CategoryModel;
import com.example.admin.Model.SubCategoryModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddNewCategoryActivity extends AppCompatActivity {
    //views
    Toolbar toolbar;
    TextView textViewsubcat,textViewcat;
    TextInputLayout categoryname;
    TextInputLayout subcategoryname;
    Button submit;
    ImageView subcatimage,catimage;
    ProgressDialog progressDialog;

    //vars
    Uri cat_image_uri;
    Uri subcat_image_uri;
    public static final int OPEN_GALLERY_SUBCATEGORY = 1;
    public static final int OPEN_GALLERY_CATEGORY = 2;

    //firebase
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);
        categoryname = findViewById(R.id.admin_edt_category_name_addnew_category);
        subcategoryname = findViewById(R.id.admin_edt_sub_category_name_addnew_category);
        submit = findViewById(R.id.admin_btn_submit_addnew_category);
        textViewsubcat = findViewById(R.id.pickimagesubtext_addnew_category);
        textViewcat=findViewById(R.id.pickimagecattext_addnew_category);
        catimage=findViewById(R.id.admin_img_cat_image_addnew_category);
        subcatimage = findViewById(R.id.admin_img_subcat_image_addnew_category);
        progressDialog = new ProgressDialog(this);

        subcatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_PICK);
                startActivityForResult(i, OPEN_GALLERY_SUBCATEGORY);
            }
        });
        catimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_PICK);
                startActivityForResult(i, OPEN_GALLERY_CATEGORY);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (cat_image_uri == null) {
                        Toast.makeText(AddNewCategoryActivity.this, "Category Image is required", Toast.LENGTH_SHORT).show();
                        checkfields();
                    if(subcat_image_uri==null){

                        Toast.makeText(AddNewCategoryActivity.this, "Sub-Category Image is required", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        checkfields();
                    }
                } else if(subcat_image_uri==null){
                    Toast.makeText(AddNewCategoryActivity.this, "Sub-category Image is required", Toast.LENGTH_SHORT).show();
                    checkfields();
                }*/
                if(catimage!=null && subcat_image_uri!=null){

                    checkfields();
                }
                else if(cat_image_uri==null||subcat_image_uri==null){
                    Toast.makeText(AddNewCategoryActivity.this, "Image is Required", Toast.LENGTH_SHORT).show();
                    checkfields();
                }
            }
        });

        inittoolbar();
    }

    private void checkfields() {
        if (categoryname.getEditText().getText().toString().trim().isEmpty()) {
            categoryname.setError("Field can't be Empty");
            categoryname.requestFocus();
            if (subcategoryname.getEditText().getText().toString().trim().isEmpty()) {
                subcategoryname.setError("Field can't be Empty");
                subcategoryname.requestFocus();
                refresherror();
            } else {
                subcategoryname.setError(null);
            }
        } else {
            categoryname.setError(null);
            if (subcategoryname.getEditText().getText().toString().trim().isEmpty()) {
                subcategoryname.setError("Field can't be Empty");
                subcategoryname.requestFocus();
                refresherror();
            } else {
                subcategoryname.setError(null);
                createcategory();
            }
        }
    }

    private void refresherror() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                categoryname.setError(null);
                subcategoryname.setError(null);
                textViewsubcat.setTextColor(Color.BLACK);
                categoryname.clearFocus();
                subcategoryname.clearFocus();
            }
        }, 2000);
    }

    private void createcategory() {

        progressDialog.show();
        progressDialog.setMessage("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        String catname = categoryname.getEditText().getText().toString().trim();
        final String finalcatname = catname.toUpperCase().charAt(0) + catname.toLowerCase().substring(1);
        String subcatname = subcategoryname.getEditText().getText().toString().trim();
        final String finalsubcatname = subcatname.toUpperCase().charAt(0) + subcatname.toLowerCase().substring(1);
        final String catkey=myRef.push().getKey();
        final String subcatkey=myRef.push().getKey();

        final StorageReference riversRef = mStorageRef.child("Category_Image").child(catkey + ".jpg");
        riversRef.putFile(cat_image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        final String subcaturl = String.valueOf(uri);
                        final StorageReference subcatref=mStorageRef.child("Sub_Category_Image").child(subcatkey+".jpg");
                        subcatref.putFile(subcat_image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               subcatref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       String caturl=String.valueOf(uri);

                                       SubCategoryModel subCategoryModel = new SubCategoryModel();
                                       subCategoryModel.setSubcatname(finalsubcatname);
                                       subCategoryModel.setSubcatimageurl(subcaturl);
                                       subCategoryModel.setSubcatkey(subcatkey);
                                       CategoryModel categoryModel=new CategoryModel();
                                       categoryModel.setCatname(finalcatname);
                                       categoryModel.setCatimageurl(caturl);
                                       categoryModel.setCatkey(catkey);
                                       myRef.child("Categories").child(catkey).setValue(categoryModel);
                                       myRef.child("Categories").child(catkey).child(subcatkey).setValue(subCategoryModel);

                                       categoryname.getEditText().setText(null);
                                       subcategoryname.getEditText().setText(null);
                                       subcatimage.setImageResource(R.drawable.ic_add_a_photo);
                                       catimage.setImageResource(R.drawable.ic_add_a_photo);
                                       textViewcat.setVisibility(View.VISIBLE);
                                       textViewsubcat.setVisibility(View.VISIBLE);
                                       subcategoryname.clearFocus();
                                       categoryname.clearFocus();
                                       progressDialog.dismiss();
                                       Toast.makeText(AddNewCategoryActivity.this, "Category Added", Toast.LENGTH_SHORT).show();

                                   }
                               });
                            }
                        });

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddNewCategoryActivity.this, "Uploading Failed, Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void inittoolbar() {
        toolbar = findViewById(R.id.admin_addnewcategory_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("New Category");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_GALLERY_SUBCATEGORY) {
            if (data == null) {
                textViewsubcat.setTextColor(Color.RED);
                refresherror();
                Toast.makeText(this, "Pick a image", Toast.LENGTH_SHORT).show();
            } else {
                textViewsubcat.setVisibility(View.GONE);
                subcat_image_uri = data.getData();
                subcatimage.setImageURI(subcat_image_uri);
            }

        }
        else if(requestCode==OPEN_GALLERY_CATEGORY){
            if (data == null) {
                textViewcat.setTextColor(Color.RED);
                refresherror();
                Toast.makeText(this, "Pick a image", Toast.LENGTH_SHORT).show();
            } else {
                textViewcat.setVisibility(View.GONE);
                cat_image_uri = data.getData();
                catimage.setImageURI(cat_image_uri);
            }

        }
    }
}
