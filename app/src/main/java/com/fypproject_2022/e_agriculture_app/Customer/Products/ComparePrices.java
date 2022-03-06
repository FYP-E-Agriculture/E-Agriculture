package com.fypproject_2022.e_agriculture_app.Customer.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComparePrices extends AppCompatActivity {

    CardView cardView;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    SearchProductsAdapter adapter;
    Intent intent;

    String productCategory;
    DatabaseHandler databaseHandler;
    List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        intent=getIntent();
        productCategory=intent.getStringExtra(Utilities.intent_product_category);
        databaseHandler= new DatabaseHandler(this);

        cardView = findViewById(R.id.price_filter_card);
        recyclerView = findViewById(R.id.search_products_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        getSupportActionBar().setTitle(productCategory);

        productList= new ArrayList<>();
        adapter= new SearchProductsAdapter(this,productList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()){
                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {
                        Product product = childSnapShot.getValue(Product.class);
                        if(product.getCategory().equals(productCategory)){
                            if(databaseHandler.getReviews(product.getId())!=null){
                                product.setReviews(databaseHandler.getReviews(product.getId()));
                            }
                            productList.add(product);
                        }
                    }
                }
                sortProductsByPrice(0);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    List<Product> sortProductsByPrice(int option){
        for(int i=0; i<productList.size();i++){
            for (int j=0;j<productList.size();j++){

                switch (option)
                {
                    case 0:
                    {
                        if(productList.get(i).getPrice() < productList.get(j).getPrice()){
                            Product temp = productList.get(i);
                            productList.set(i,productList.get(j));
                            productList.set(j,temp);
                        }

                        break;
                    }
                    case 1:
                    {
                        if(productList.get(i).getPrice() > productList.get(j).getPrice()){
                            Product temp = productList.get(i);
                            productList.set(i,productList.get(j));
                            productList.set(j,temp);
                        }

                        break;
                    }
                }

            }
        }
        return productList;
    }

    public void showPriceOption(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.low_to_high:
                    {
                        sortProductsByPrice(0);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    case R.id.high_to_low:
                    {
                        sortProductsByPrice(1);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

                return true;
            }
        });
        popup.inflate(R.menu.price_menu);
        popup.show();
    }
}