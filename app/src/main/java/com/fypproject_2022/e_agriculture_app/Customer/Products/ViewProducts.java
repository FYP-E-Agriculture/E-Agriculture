package com.fypproject_2022.e_agriculture_app.Customer.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Dashboard.Dashboard;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewProducts extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;

    TextView title;
    Toolbar toolbar;
    TextView compare_btn;

    CardView productsFilterCard;
    ProgressBar progressBar;

    RecyclerView recyclerViewStores;
    StoresCustomerAdapter adapterStores;
    List<Store> storeList;

    RecyclerView recyclerViewProducts;
    ProductAdapter adapter;
    List<Product> productListAll;
    List<Product> productList;

    Store store;
    Intent intent;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);

        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
//        productsFilter=findViewById(R.id.products_filter);
        productsFilterCard=findViewById(R.id.products_filter_card);
        recyclerViewProducts =findViewById(R.id.products_recycler_view);
        recyclerViewStores =findViewById(R.id.stores_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        compare_btn =findViewById(R.id.compare_btn);
        progressBar.setVisibility(View.VISIBLE);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);

        recyclerViewProducts.setVisibility(View.INVISIBLE);


        storeList= new ArrayList<>();
        adapterStores = new StoresCustomerAdapter(this,storeList);
        recyclerViewStores.setAdapter(adapterStores);
        recyclerViewStores.setLayoutManager(new GridLayoutManager(this, 2));


        productListAll =new ArrayList<>();
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        intent = getIntent();
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);
        databaseHandler= new DatabaseHandler(this);


        databaseHandler.getStoresReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Store store = snapShot.getValue(Store.class);
                    storeList.add(store);
                }
                progressBar.setVisibility(View.GONE);
                if(storeList.size()==0){
                    title.setVisibility(View.VISIBLE);
                }
                adapterStores.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()){
                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {
                        Product product = childSnapShot.getValue(Product.class);
                        if (databaseHandler.getReviews(product.getId()) != null) {
                            product.setReviews(databaseHandler.getReviews(product.getId()));
                        }
                        productListAll.add(product);
                    }
                }
                updateRecyclerView();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        compare_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductCategoriesOnNavbar(view);
            }
        });
    }


    public void showProductCategoriesOnNavbar(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ViewProducts.this, ComparePrices.class);
                intent.putExtra(Utilities.intent_product_category,item.getTitle().toString());
                startActivity(intent);
                return true;
            }
        });
        popup.inflate(R.menu.product_category_popup_menu);
        popup.show();
    }


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

    void updateRecyclerView(){
        productList =new ArrayList<>();
        adapter= new ProductAdapter(ViewProducts.this, productList);
        recyclerViewProducts.setAdapter(adapter);

        for (Product product : productListAll) {
            productList.add(product);
        }
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        productList =new ArrayList<>();
        adapter= new ProductAdapter(ViewProducts.this, productList);
        recyclerViewProducts.setAdapter(adapter);

        if(radioButton.getText().toString().equals("No Filter")){
            for (Product product : productListAll) {
                productList.add(product);
            }
            recyclerViewProducts.setVisibility(View.INVISIBLE);
            recyclerViewStores.setVisibility(View.VISIBLE);
        }
        else {
            recyclerViewProducts.setVisibility(View.VISIBLE);
            recyclerViewStores.setVisibility(View.INVISIBLE);
            for (Product product : productListAll) {
                if (product.getCategory().equals(radioButton.getText())) {
                    productList.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewProducts.this, Dashboard.class));
    }
}