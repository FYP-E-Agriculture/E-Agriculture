package com.fypproject_2022.e_agriculture_app.Vendor.Stores;

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
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.SelectUserType;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.Models.Vendor;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.fypproject_2022.e_agriculture_app.Vendor.Dashboard.DashboardVendor;
import com.fypproject_2022.e_agriculture_app.Vendor.Orders.ViewOrdersVendor;
import com.fypproject_2022.e_agriculture_app.Vendor.Products.ProductManagementActivity;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreDashboard extends AppCompatActivity {

    CardView productCard;
    CardView ordersCard;
    CardView reportCard;
    CardView customersCard;
    Intent intent;

    Store store;
    MyVendorPreferences mvp;
    Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_dashboard);

        mvp= new MyVendorPreferences(this);
        vendor= mvp.getVendor();
        intent=getIntent();
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);

        productCard=findViewById(R.id.productCard);
        ordersCard=findViewById(R.id.ordersCard);
        reportCard=findViewById(R.id.salesReportCard);
        customersCard=findViewById(R.id.customerCard);

        getSupportActionBar().setTitle(store.getName());
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreDashboard.this, ProductManagementActivity.class);
                intent.putExtra(Utilities.intent_store,store);
                startActivity(intent);
            }
        });

        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreDashboard.this, ViewOrdersVendor.class);
                intent.putExtra(Utilities.intent_store,store);
                startActivity(intent);
            }
        });

        customersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StoreDashboard.this, "FEATURE UNAVAILABLE", Toast.LENGTH_LONG).show();
            }
        });

        reportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StoreDashboard.this, "FEATURE UNAVAILABLE", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(StoreDashboard.this, DashboardVendor.class));
    }
}