package com.fypproject_2022.e_agriculture_app.Customer.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Dashboard.Dashboard;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewProducts extends AppCompatActivity {

//    RadioGroup radioGroup;
//    RadioButton radioButton;

    TextView title;
    TextView cityTitle;
    TextView showAllText;
    TextView nearestBtn;
    DrawerLayout drawerLayout;

    CardView productsFilterCard;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    StoresCustomerAdapter adapter;
    List<Store> storeListAll;
    List<Store> storeList;

//    RecyclerView recyclerViewProducts;
//    ProductAdapter adapter;
//    List<Product> productListAll;
//    List<Product> productList;

    Store store;
    Intent intent;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        title=findViewById(R.id.title);
        cityTitle=findViewById(R.id.city_title);
        showAllText =findViewById(R.id.show_all);
        nearestBtn =findViewById(R.id.nearest);
//        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
        productsFilterCard=findViewById(R.id.products_filter_card);
//        recyclerViewProducts =findViewById(R.id.products_recycler_view);
        recyclerView =findViewById(R.id.stores_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        drawerLayout = findViewById(R.id.drawer_layout);

        getSupportActionBar().setTitle("Stores");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText("No stores in "+MyCustomerPreferences.getCustomer().getCity());
        cityTitle.setText("All Stores");

//        recyclerViewProducts.setVisibility(View.INVISIBLE);


        storeListAll = new ArrayList<>();
        storeList = new ArrayList<>();
        adapter = new StoresCustomerAdapter(this, storeList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


//        productListAll =new ArrayList<>();
//        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        intent = getIntent();
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);
        databaseHandler= new DatabaseHandler(this);


        databaseHandler.getStoresReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Store store = snapShot.getValue(Store.class);
                    storeListAll.add(store);
                    storeList.add(store);
                }
                progressBar.setVisibility(View.GONE);
                if(storeListAll.size()==0){
                    title.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeList= new ArrayList<>();
                for (Store store : storeListAll) {
                    storeList.add(store);
                }
                showAllText.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                cityTitle.setText("All Stores");
                recyclerView.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                nearestBtn.setText("Show Nearest");
            }
        });

        nearestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nearestBtn.getText().toString().equals("Show Nearest")){
                    cityTitle.setText("Stores in "+MyCustomerPreferences.getCustomer().getCity());
                    storeList = new ArrayList<>();
                    for (Store store :storeListAll) {
                        if (store.getCity().equals(MyCustomerPreferences.getCustomer().getCity())) {
                            storeList.add(store);
                        }
                    }
                    if(storeList.size()==0){
                        title.setVisibility(View.VISIBLE);
                        showAllText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    else{
                        adapter = new StoresCustomerAdapter(ViewProducts.this, storeList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new GridLayoutManager(ViewProducts.this, 2));
                        adapter.notifyDataSetChanged();
                    }

                    Snackbar snackbar = Snackbar.make(drawerLayout, "Showing store in "+MyCustomerPreferences.getCustomer().getCity(), Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (Store store : storeListAll) {
                                storeList.add(store);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                    snackbar.show();
                    nearestBtn.setText("Show All");
                }
                else if(nearestBtn.getText().toString().equals("Show All")){
                    storeList= new ArrayList<>();
                    for (Store store : storeListAll) {
                        storeList.add(store);
                    }
                    showAllText.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    cityTitle.setText("All Stores");
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    nearestBtn.setText("Show Nearest");
                }
            }
        });

//        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapShot : dataSnapshot.getChildren()){
//                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {
//                        Product product = childSnapShot.getValue(Product.class);
//                        if (databaseHandler.getReviews(product.getId()) != null) {
//                            product.setReviews(databaseHandler.getReviews(product.getId()));
//                        }
//                        productListAll.add(product);
//                    }
//                }
//                updateRecyclerView();
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.search:
//                    {
//                        Intent intent = new Intent(ViewProducts.this, ComparePrices.class);
//                        intent.putExtra(Utilities.intent_product_category,item.getTitle().toString());
//                        startActivity(intent);
//                        break;
//                    }
//                    case R.id.nearest:
//                    {
//                        storeList = new ArrayList<>();
//                        for (Store store :storeListAll) {
//                            if (store.getCity().equals(MyCustomerPreferences.getCustomer().getCity())) {
//                                storeList.add(store);
//                            }
//                        }
//                        adapterStores = new StoresCustomerAdapter(ViewProducts.this, storeList);
//                        recyclerViewStores.setAdapter(adapterStores);
//                        recyclerViewStores.setLayoutManager(new GridLayoutManager(ViewProducts.this, 2));
//                        adapter.notifyDataSetChanged();
//
//                        Snackbar snackbar = Snackbar.make(drawerLayout, "Showing store in "+MyCustomerPreferences.getCustomer().getCity(), Snackbar.LENGTH_LONG);
//                        snackbar.show();
//                        break;
//                    }
//                }
//                return true;
//            }
//        });

//        compare_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showProductCategoriesOnNavbar(view);
//            }
//        });
    }

//    public void showProductCategoriesOnNavbar(View v){
//        PopupMenu popup = new PopupMenu(this, v);
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(ViewProducts.this, ComparePrices.class);
//                intent.putExtra(Utilities.intent_product_category,item.getTitle().toString());
//                startActivity(intent);
//                return true;
//            }
//        });
//        popup.inflate(R.menu.product_category_popup_menu);
//        popup.show();
//    }


//    public void showProductCategories(View v){
//        PopupMenu popup = new PopupMenu(this, v);
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                productsFilter.setText(item.getTitle().toString());
//                updateRecyclerView();
//                return true;
//            }
//        });
//        popup.inflate(R.menu.product_category_popup_menu);
//        popup.show();
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compare_prices:
            {
                startActivity(new Intent(ViewProducts.this, ComparePrices.class));
                break;
            }
        }
        return true;
    }

    void updateRecyclerView(){
        storeList =new ArrayList<>();
        adapter= new StoresCustomerAdapter(ViewProducts.this, storeList);
        recyclerView.setAdapter(adapter);

        for (Store store : storeListAll) {
            storeList.add(store);
        }
        adapter.notifyDataSetChanged();
    }

//    public void updateFromRadio(View v){
//        int radioId = radioGroup.getCheckedRadioButtonId();
//        radioButton= findViewById(radioId);
//
//        productList =new ArrayList<>();
//        adapter= new ProductAdapter(ViewProducts.this, productList);
//        recyclerViewProducts.setAdapter(adapter);
//
//        if(radioButton.getText().toString().equals("All")){
//            for (Product product : productListAll) {
//                productList.add(product);
//            }
//            recyclerViewProducts.setVisibility(View.INVISIBLE);
//            recyclerViewStores.setVisibility(View.VISIBLE);
//        }
//        else {
//            recyclerViewProducts.setVisibility(View.VISIBLE);
//            recyclerViewStores.setVisibility(View.INVISIBLE);
//            for (Product product : productListAll) {
//                if (product.getCategory().equals(radioButton.getText())) {
//                    productList.add(product);
//                }
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_navbar_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewProducts.this, Dashboard.class));
    }
}