package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.admin.Fragment.DashBoard_fragment;
import com.example.admin.Fragment.Product_Fragment;
import com.example.admin.Fragment.Tools_fragment;
import com.example.admin.Fragment.User_fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    FrameLayout frameLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    //firebase
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        myRef.child("Admin").child("token").setValue(refreshToken);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.admin_home_toolbar);
        frameLayout = findViewById(R.id.admin_frame_container);
        drawerLayout = findViewById(R.id.admin_drawer_layout);
        navigationView = findViewById(R.id.admin_nav_view);
        setSupportActionBar(toolbar);
        toolbar.setTitle("DashBoard");
        getSupportFragmentManager().beginTransaction().add(R.id.admin_frame_container, new DashBoard_fragment()).commit();

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.syncState();

        drawerLayout.addDrawerListener(drawerToggle);

        navigationView.setCheckedItem(R.id.nav_dashboard);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_dashboard:

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_frame_container, new DashBoard_fragment())
                        .addToBackStack(null)
                        .commit();
                toolbar.setTitle("DashBoard");
                break;
            case R.id.nav_user:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_frame_container, new User_fragment())
                        .addToBackStack(null)
                        .commit();
                toolbar.setTitle("Users");
                break;

            case R.id.nav_product:
                startActivity(new Intent(HomeActivity.this,ShowAllProductsActivity.class));
                break;

            case R.id.nav_manage_product:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_frame_container,new Product_Fragment())
                        .commit();
                toolbar.setTitle("Manage Products");
                break;

            case R.id.nav_tools:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.admin_frame_container, new Tools_fragment())
                        .commit();
                toolbar.setTitle("Tools");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
