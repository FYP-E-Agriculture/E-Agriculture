package com.fypproject_2022.e_agriculture_app.Customer.Stores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ComparePrices;
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
    RadioGroup radioGroup;
    RadioButton radioButton;

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

        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
        name=findViewById(R.id.name_val);
        city=findViewById(R.id.city_val);
        phone=findViewById(R.id.phone_val);
        imageView=findViewById(R.id.image);
        recyclerViewProducts=findViewById(R.id.products_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        productListAll =new ArrayList<>();
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(StoreDetail.this,2));

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
                            productListAll.add(product);
                        }
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
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(StoreDetail.this,2));

        for (Product product : productListAll) {
            productList.add(product);
        }
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        productList =new ArrayList<>();
        adapter= new ProductAdapter(StoreDetail.this, productList);
        recyclerViewProducts.setAdapter(adapter);

        if(radioButton.getText().toString().equals("All")){
            for (Product product : productListAll) {
                productList.add(product);
            }
        }
        else {
            for (Product product : productListAll) {
                System.out.println(product.getName()+" "+product.getCategory());
                System.out.println("Radio Button"+" "+radioButton.getText());
                if (product.getCategory().equals(radioButton.getText())) {
                    productList.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}