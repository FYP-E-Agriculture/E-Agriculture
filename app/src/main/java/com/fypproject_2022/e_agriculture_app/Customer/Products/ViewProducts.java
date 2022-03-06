package com.fypproject_2022.e_agriculture_app.Customer.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Dashboard.Dashboard;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewProducts extends AppCompatActivity {

    Toolbar toolbar;
    TextView compare_btn;
    TextView productsFilter;
    CardView productsFilterCard;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productListAll;
    List<Product> productList;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products);
        productsFilter=findViewById(R.id.products_filter);
        productsFilterCard=findViewById(R.id.products_filter_card);
        recyclerView=findViewById(R.id.products_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        compare_btn =findViewById(R.id.compare_btn);
        progressBar.setVisibility(View.VISIBLE);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);

        productListAll =new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        databaseHandler= new DatabaseHandler(this);
        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()){
                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {
                        Product product = childSnapShot.getValue(Product.class);
                        if(databaseHandler.getReviews(product.getId())!=null){
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


    public void showProductCategories(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                productsFilter.setText(item.getTitle().toString());
                updateRecyclerView();
                return true;
            }
        });
        popup.inflate(R.menu.product_category_popup_menu);
        popup.show();
    }
    void updateRecyclerView(){
        productList =new ArrayList<>();
        adapter= new ProductAdapter(ViewProducts.this, productList);
        recyclerView.setAdapter(adapter);

        for (Product product : productListAll) {
            if (product.getCategory().equals(productsFilter.getText().toString())) {
                productList.add(product);
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