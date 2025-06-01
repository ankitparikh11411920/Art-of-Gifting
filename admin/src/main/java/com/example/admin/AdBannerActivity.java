package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.Adapter.AdAdapterRecycler;
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

import java.util.ArrayList;

public class AdBannerActivity extends AppCompatActivity {
    private ImageView adbanner;
    private Button upload;
    private RecyclerView showads;
    public static final int OPEN_GALLERY = 1;
    //vars
    ArrayList<String> adsarraylist;
    AdAdapterRecycler adAdapterRecycler;
    String adkey;
    //firebase
    private StorageReference mStorageRef= FirebaseStorage.getInstance().getReference();;
    private DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_banner);
        inittoolbar();

        showads=findViewById(R.id.admin_showads_recyclerview_adbanner);
        adbanner=findViewById(R.id.admin_img_create_ad);
        upload=findViewById(R.id.admin_btn_upload_ad);

        initrecyclerviewshowads();
        adbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectimage();
            }
        });


    }

    private void initrecyclerviewshowads() {
        showads.setLayoutManager(new LinearLayoutManager(this));
        showads.setHasFixedSize(true);
        adsarraylist=new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adsarraylist.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.child("Ad Banner").getChildren()){
                    String img_url=dataSnapshot1.getValue(String.class);
                    final String ad_key=dataSnapshot1.getKey();
                    adsarraylist.add(img_url);
                    if(!adsarraylist.isEmpty()){
                        adAdapterRecycler=new AdAdapterRecycler(AdBannerActivity.this,adsarraylist);
                        showads.setAdapter(adAdapterRecycler);
                        adAdapterRecycler.OnRemoveClick(new AdAdapterRecycler.OnItemClick() {
                            @Override
                            public void OnRemoveClick(int position, View v) {
                                adsarraylist.remove(position);
                                myRef.child("Ad Banner").child(ad_key).removeValue();
                                mStorageRef.child("Ad Banner").child(ad_key+".jpg").delete();
                                adAdapterRecycler.notifyItemRemoved(position);
                            }
                        });
                    }else
                    {
                        showads.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void selectimage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
        startActivityForResult(i, OPEN_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_GALLERY) {
            if (data == null) {
                Toast.makeText(this, "Pick a image", Toast.LENGTH_SHORT).show();
            } else {
                final Uri image_uri = data.getData();
                adbanner.setImageURI(image_uri);
                upload.setVisibility(View.VISIBLE);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadimage(image_uri);
                    }
                });
            }
        }
    }

    private void uploadimage(Uri image_uri) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        adkey=myRef.push().getKey();
        final StorageReference reference=mStorageRef.child("Ad Banner").child(adkey+".jpg");
        reference.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url=String.valueOf(uri);
                                        myRef.child("Ad Banner").child(adkey).setValue(url);
                                        upload.setVisibility(View.INVISIBLE);
                                        adbanner.setImageResource(R.drawable.ic_add_a_photo);
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdBannerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    ///toolbar
    private void inittoolbar() {
        Toolbar toolbar = findViewById(R.id.admin_toolbar_createad);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Upload your Ad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    ///toolbar
}
