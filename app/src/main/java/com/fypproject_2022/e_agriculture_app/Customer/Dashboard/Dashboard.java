package com.fypproject_2022.e_agriculture_app.Customer.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.SelectUserType;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Cart.CartActivity;
import com.fypproject_2022.e_agriculture_app.Customer.Orders.ViewOrdersActivity;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ComparePrices;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ViewProducts;
import com.fypproject_2022.e_agriculture_app.Models.Customer;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navView;
    CardView productCard;
    CardView cartCard;
    CardView ordersCard;
    CardView logoutCard;

    MyCustomerPreferences mcp;
    Customer customer;
    View header;
    TextView headerEmail;
    TextView headerName;
    CircleImageView headerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mcp = new MyCustomerPreferences(this);
        customer = mcp.getCustomer();

        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navView);
        header= navView.getHeaderView(0);
        headerName=header.findViewById(R.id.header_nav_name);
        headerEmail=header.findViewById(R.id.header_nav_email);
        headerImage=header.findViewById(R.id.header_nav_image);
        Picasso.get().load(customer.getImage()).into(headerImage);
        headerName.setText(customer.getName());
        headerEmail.setText(customer.getEmail());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
        productCard=findViewById(R.id.productCard);
        ordersCard=findViewById(R.id.ordersCard);
        cartCard=findViewById(R.id.cartCard);
        logoutCard=findViewById(R.id.logoutCard);


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
                        startActivity(new Intent(Dashboard.this, Dashboard.class));
                    }
                }
                if (menuItem.getItemId() == R.id.cart) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        startActivity(new Intent(Dashboard.this, CartActivity.class));
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
                        startActivity(new Intent(Dashboard.this, ViewOrdersActivity.class));
                    }
                }


                if (menuItem.getItemId() == R.id.logout) {

                    if (drawer.isDrawerOpen(navView)) {
                        drawer.closeDrawer(navView);
                        MyCustomerPreferences.setLogin(false);
                        Intent intent = new Intent(Dashboard.this, SelectUserType.class);
                        intent.putExtra(Utilities.intent_user_category, Utilities.user_customer);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewProducts.class));
            }
        });
        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewOrdersActivity.class));
            }
        });
        cartCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, CartActivity.class));
            }
        });
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCustomerPreferences.setLogin(false);
                Intent intent = new Intent(Dashboard.this, SelectUserType.class);
                intent.putExtra(Utilities.intent_user_category, Utilities.user_customer);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void showProductCategoryPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(Dashboard.this, ComparePrices.class);
                intent.putExtra(Utilities.intent_product_category,item.getTitle().toString());
                startActivity(intent);
                return true;
            }
        });
        popup.inflate(R.menu.product_category_popup_menu);
        popup.show();
    }
}