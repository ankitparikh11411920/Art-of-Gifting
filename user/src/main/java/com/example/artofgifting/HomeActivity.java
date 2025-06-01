package com.example.artofgifting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artofgifting.Fragment.Home;
import com.example.artofgifting.Fragment.MyReviews;
import com.example.artofgifting.Fragment.Myorder;
import com.example.artofgifting.Fragment.Shop_by_Cat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CircleImageView cir_profile;
    TextView textView;

    private Uri imguri;
    private Intent intent;
    Picasso picasso;

    //firebase
    private StorageReference mStorageRef;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    final FirebaseUser user = mAuth.getCurrentUser();


    @Override
    protected void onStart() {
        super.onStart();
        if (isNetworkConnectionAvailable()) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String refreshToken = FirebaseInstanceId.getInstance().getToken();
            myRef.child(user.getUid()).child("token").setValue(refreshToken);
            onResume();
        } else {
            startActivity(new Intent(HomeActivity.this, InternetConnectionActivity.class));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("Users");
        final FirebaseUser user = mAuth.getCurrentUser();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        cir_profile = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        textView = navigationView.getHeaderView(0).findViewById(R.id.user_name_display_homeactivity);

        /*textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                View rootview = LayoutInflater.from(HomeActivity.this)
                        .inflate(R.layout.raw_dialog_password, null);
                builder.setView(rootview);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText edt_name = rootview.findViewById(R.id.edt_forgotpasswordemail);
                Button button = rootview.findViewById(R.id.btn_forgot);
                edt_name.setHint("New Name");
                button.setText("Submit");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edt_name.getText().toString();
                        myRef.child(user.getUid()).child("fname").setValue(name);
                        textView.setText(name);
                        alertDialog.dismiss();
                    }
                });
            }
        });*/

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.child(user.getUid()).child("image_url").getValue(String.class);
                String name = dataSnapshot.child(user.getUid()).child("fullname").getValue(String.class);

                textView.setText(name);
                picasso.get().load(url).fit().into(cir_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");

        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, new Home())
                .addToBackStack(null)
                .commit();
        cir_profile.setOnClickListener(this);
    }


    //    }
//    public void checkNetworkStatus() {
//
//        final ConnectivityManager connMgr = (ConnectivityManager)
//                this.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        final android.net.NetworkInfo wifi =
//                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        final android.net.NetworkInfo mobile =
//                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//        if (wifi.isAvailable()) {
//            main();
//        } else if (mobile.isAvailable()) {
//            main();
//        } else {
//            intent = new Intent(HomeActivity.this, InternetConnectionActivity.class);
//            startActivity(intent);
//        }
//
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_image: {
                startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
            }
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_toolbar_menu, menu);
        return true;
    }*/


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment = null;
        if (id == R.id.home) {

            fragment = new Home();
            /*getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, new Home())
                    .addToBackStack(null)
                    .commit();*/
            toolbar.setTitle("Home");

        } else if (id == R.id.shopbycat) {
            fragment = new Shop_by_Cat();
            toolbar.setTitle("Categories");
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    onBackPressed();
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            });*/
        } /*else if (id == R.id.tracking) {
            fragment = new Trackingorder();
            toolbar.setTitle("Track Order");

        }*/ else if (id == R.id.myorder) {
            fragment = new Myorder();
            toolbar.setTitle("My Order");
        } /*else if (id == R.id.helpcenter) {

            fragment = new Helpcenter();
            toolbar.setTitle("Help Center");
        } else if (id == R.id.rating) {
            ratingfeedback();

        } */ else if (id == R.id.share) {
            share();
        } else if (id == R.id.logout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.myreview) {
            fragment = new MyReviews();
            toolbar.setTitle("My Reviews");
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .addToBackStack(null)
                    .commit();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void share() {
        intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.Clairvoyant.artofgifting");
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private void ratingfeedback() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View MyView = layoutInflater.inflate(R.layout.raw_rating_dialog, null);
        Button btnfeedback = MyView.findViewById(R.id.user_btn_submit_app_rating);
        Button btnnothanks = MyView.findViewById(R.id.user_btn_nothanks_app_rating);
        RatingBar ratingBar = MyView.findViewById(R.id.user_app_rating);
        final EditText edtfeedback = MyView.findViewById(R.id.user_edt_feedback_app_rating);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(MyView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        btnfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strfeedback = edtfeedback.getText().toString();
                if (strfeedback.isEmpty()) {
                    StyleableToast.makeText(HomeActivity.this, "Please give feedback then press submit", R.style.exampletoast).show();
                } else {
                    alertDialog.dismiss();
                }
            }
        });
        btnnothanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleableToast.makeText(HomeActivity.this, "Rate later", R.style.exampletoast).show();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                imguri = data.getData();
                cir_profile.setImageURI(imguri);
                changeprofileimage(imguri);
            }
        }
    }

    private void changeprofileimage(Uri imguri) {
        mStorageRef = FirebaseStorage.getInstance().getReference("User_Image");
        final StorageReference riversRef = mStorageRef.child(user.getUid() + ".jpg");

        riversRef.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        riversRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String upload_url = String.valueOf(uri);
                                        myRef.child(user.getUid()).child("image_url").setValue(upload_url);
                                        Toast.makeText(HomeActivity.this, "Upload Successfull", Toast.LENGTH_SHORT).show();


                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(HomeActivity.this, "Try Again, Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //    public void checkNetworkConnection() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//        builder.setTitle("No internet Connection");
//        builder.setMessage("Please turn on internet connection to continue");
//        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//
    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            intent = new Intent(HomeActivity.this, InternetConnectionActivity.class);
            startActivity(intent);
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (checkNavigationMenuItem() != 0) {
                navigationView.setCheckedItem(R.id.home);
                Fragment fragment = new Home();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle(" Exit ");
                builder.setMessage(" Are you sure you want to exit the app ? ");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        }

    }

    private int checkNavigationMenuItem() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked())
                return i;
        }
        return -1;
    }
}

