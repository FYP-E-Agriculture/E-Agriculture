package com.fypproject_2022.e_agriculture_app.Customer.Stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ProductAdapter;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ViewProducts;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreDetail extends AppCompatActivity {

    TextView name;
    TextView city;
    TextView phone;
    CircleImageView imageView;
    ProgressBar progressBar;

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
        setContentView(R.layout.activity_store_detail);

        name=findViewById(R.id.name_val);
        city=findViewById(R.id.city_val);
        phone=findViewById(R.id.phone_val);
        imageView=findViewById(R.id.image);
        recyclerViewProducts=findViewById(R.id.products_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        productListAll =new ArrayList<>();
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        intent = getIntent();
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);
        getSupportActionBar().setTitle(store.getName());
        databaseHandler= new DatabaseHandler(this);

        name.setText(store.getName());
        city.setText(store.getCity());
        phone.setText(store.getPhone());
        Picasso.get().load(store.getImage()).into(imageView);
        progressBar.setVisibility(View.VISIBLE);

        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {
                        Product product = childSnapShot.getValue(Product.class);

                        if (product.getStoreId().equals(store.getId())) {
                            if (databaseHandler.getReviews(product.getId()) != null) {
                                product.setReviews(databaseHandler.getReviews(product.getId()));
                            }
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
    }

    void updateRecyclerView(){
        productList =new ArrayList<>();
        adapter= new ProductAdapter(StoreDetail.this, productList);
        recyclerViewProducts.setAdapter(adapter);

        for (Product product : productListAll) {
            productList.add(product);
        }
        adapter.notifyDataSetChanged();
    }
}