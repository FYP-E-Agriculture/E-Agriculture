package com.fypproject_2022.e_agriculture_app.Admin.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Admin.CustomerManagment.CustomerMgmtActivity;
import com.fypproject_2022.e_agriculture_app.Admin.MyAdminPreferences;
import com.fypproject_2022.e_agriculture_app.Admin.VendorManagment.VendorMgmtActivity;
import com.fypproject_2022.e_agriculture_app.Common.SelectUserType;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Models.Admin;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardAdmin extends AppCompatActivity {

    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navView;
    CardView customerCard;
    CardView vendorCard;
    CardView logoutCard;

    MyAdminPreferences map;
    Admin admin;
    View header;
    TextView headerEmail;
    TextView headerName;
    CircleImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        map = new MyAdminPreferences(this);
        admin = map.getAdmin();

        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navView);
        header= navView.getHeaderView(0);
        headerName=header.findViewById(R.id.header_nav_name);
        headerEmail=header.findViewById(R.id.header_nav_email);
        headerImage=header.findViewById(R.id.header_nav_image);
        Picasso.get().load(admin.getImage()).into(headerImage);
        headerName.setText(admin.getName());
        headerEmail.setText(admin.getEmail());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        customerCard =findViewById(R.id.customersCard);
        vendorCard =findViewById(R.id.vendorsCard);
        logoutCard =findViewById(R.id.logoutCard);


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
                        startActivity(new Intent(DashboardAdmin.this, DashboardAdmin.class));
                    }
                }

                if (menuItem.getItemId() == R.id.profile) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
//                        startActivity(new Intent(RegisterUserDashboardAdmin.this, RegisteredUserProfileActivity.class));
                    }
                }

                if (menuItem.getItemId() == R.id.logout) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        map.setLogin(false);
                        Intent intent = new Intent(DashboardAdmin.this, SelectUserType.class);
                        intent.putExtra(Utilities.intent_user_category, Utilities.user_admin);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        customerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdmin.this, CustomerMgmtActivity.class));
            }
        });

        vendorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardAdmin.this, VendorMgmtActivity.class));
            }
        });
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setLogin(false);
                Intent intent = new Intent(DashboardAdmin.this, SelectUserType.class);
                intent.putExtra(Utilities.intent_user_category, Utilities.user_admin);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}