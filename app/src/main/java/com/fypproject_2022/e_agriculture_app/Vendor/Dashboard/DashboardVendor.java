package com.fypproject_2022.e_agriculture_app.Vendor.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Login;
import com.fypproject_2022.e_agriculture_app.Common.SelectUserType;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.Models.Vendor;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.fypproject_2022.e_agriculture_app.Vendor.Products.AddProduct;
import com.fypproject_2022.e_agriculture_app.Vendor.Products.ProductManagementActivity;
import com.fypproject_2022.e_agriculture_app.Vendor.Stores.RegisterStore;
import com.fypproject_2022.e_agriculture_app.Vendor.Stores.StoreAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardVendor extends AppCompatActivity {

    FloatingActionButton fab;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navView;
    CircleImageView headerImage;
    View header;
    TextView headerEmail;
    TextView headerName;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    ImageView addBtn;


    RecyclerView recyclerView;
    StoreAdapter adapter;
    List<Store> storeList;

    MyVendorPreferences mvp;
    Vendor vendor;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_vendor);

        mvp= new MyVendorPreferences(this);
        vendor= mvp.getVendor();
        databaseHandler = new DatabaseHandler(this);

        fab =findViewById(R.id.fab);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navView);
        header= navView.getHeaderView(0);
        headerName=header.findViewById(R.id.header_nav_name);
        headerEmail=header.findViewById(R.id.header_nav_email);
        headerImage=header.findViewById(R.id.header_nav_image);
        recyclerView=findViewById(R.id.stores_recycler_view);
        relativeLayout=findViewById(R.id.add_rl);
        addBtn=findViewById(R.id.add_btn);
        Picasso.get().load(vendor.getImage()).into(headerImage);
        headerName.setText(vendor.getName());
        headerEmail.setText(vendor.getEmail());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();

        storeList= new ArrayList<>();
        adapter = new StoreAdapter(this,storeList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);
        databaseHandler.getStoresReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot:dataSnapshot.getChildren()) {
                    Store store = snapShot.getValue(Store.class);
                    if(store.getVendorId().equals(vendor.getId())){
                        storeList.add(store);
                    }
                }
                if(storeList.size()==0){
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                else{
                    relativeLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardVendor.this, RegisterStore.class));
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardVendor.this, RegisterStore.class));
            }
        });

        if (drawer.isDrawerOpen(navView)) {
            drawer.closeDrawer(navView);
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                if (menuItem.getItemId() == R.id.home) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        finish();
                        startActivity(new Intent(DashboardVendor.this, DashboardVendor.class));
                    }
                }

                if (menuItem.getItemId() == R.id.profile) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
//                        startActivity(new Intent(RegisterUserDashboard.this, RegisteredUserProfileActivity.class));
                    }
                }

                if (menuItem.getItemId() == R.id.orders) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
//                        startActivity(new Intent(RegisterUserDashboard.this, ViewOrdersActivity.class));
                    }
                }


                if (menuItem.getItemId() == R.id.logout) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        MyVendorPreferences.setLogin(false);
                        Intent intent = new Intent(DashboardVendor.this, SelectUserType.class);
                        intent.putExtra(Utilities.intent_user_category, Utilities.user_vendor);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}