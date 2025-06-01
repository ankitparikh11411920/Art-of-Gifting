package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Upload_Image extends AppCompatActivity {
    //views
    Toolbar toolbar;
    CircleImageView userimage;
    Button skip, next;
    TextView addphototextview;
    ProgressDialog dialog;

    //vars
    public static final int OPEN_GALLERY = 1;
    private Uri image_uri;

    //firebase
    private StorageReference mStorageRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    final FirebaseUser user = mAuth.getCurrentUser();


    @Override
    protected void onStart() {
        super.onStart();
//         SharedPreferences sharedPreferences=getSharedPreferences("ImageData",MODE_PRIVATE);
//        String imagedata=sharedPreferences.getString(IMAGE_DATA,"0");
//        if(imagedata.equals("1")){
//            startActivity(new Intent(Upload_Image.this,HomeActivity.class));
//            finish();
//        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        // Write a message to the database


        toolbar = findViewById(R.id.user_uploadimage_actionbar);
        userimage = findViewById(R.id.user_upload_image_insertimg);
        skip = findViewById(R.id.user_btn_skip_insertimg);
        addphototextview = findViewById(R.id.user_tv_addphoto_insertimg);
        next = findViewById(R.id.user_btn_next_insertimg);
        dialog = new ProgressDialog(this);

        mStorageRef = FirebaseStorage.getInstance().getReference("User_Image");


        setSupportActionBar(toolbar);
        toolbar.setTitle("Upload Image");

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Wait...");
                dialog.show();
                final StorageReference riversRef = mStorageRef.child("default.jpg");

                riversRef.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String upload_url = String.valueOf(uri);

                                myRef.child(user.getUid()).child("image_url").setValue(upload_url);
                                Toast.makeText(Upload_Image.this, "Upload Successfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Upload_Image.this, HomeActivity.class));
                                dialog.dismiss();
                                finish();
                            }
                        });
            }
        });


        userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_PICK);
                startActivityForResult(i, OPEN_GALLERY);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Uploading...");
                dialog.show();
                final StorageReference riversRef = mStorageRef.child(user.getUid() + ".jpg");

                riversRef.putFile(image_uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                riversRef.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String upload_url = String.valueOf(uri);
                                                dialog.dismiss();
                                                myRef.child(user.getUid()).child("image_url").setValue(upload_url);
                                                Toast.makeText(Upload_Image.this, "Upload Successfull", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Upload_Image.this, HomeActivity.class));
                                                finish();


                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(Upload_Image.this, "Try Again, Upload Failed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_GALLERY) {
            if (data == null) {
                Toast.makeText(this, "Pick a image", Toast.LENGTH_SHORT).show();
            } else {
                addphototextview.setVisibility(View.INVISIBLE);
                image_uri = data.getData();
                userimage.setImageURI(image_uri);
                userimage.setBackground(null);
                skip.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
            }
        }
    }
}
