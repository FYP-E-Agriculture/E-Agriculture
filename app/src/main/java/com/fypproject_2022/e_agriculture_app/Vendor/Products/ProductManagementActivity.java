package com.fypproject_2022.e_agriculture_app.Vendor.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ProductAdapter;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Review;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.fypproject_2022.e_agriculture_app.Vendor.Dashboard.DashboardVendor;
import com.fypproject_2022.e_agriculture_app.Vendor.Stores.StoreDashboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    FloatingActionButton fab;
//    TextView productsFilter;
    TextView filterTextView;
    CardView productsFilterCard;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ProductAdapterVendor adapter;
    RelativeLayout relativeLayout;
    ImageView addBtn;

    Store store;
    Intent intent;
    List<Product> productListAll;
    List<Product> productList;
    DatabaseHandler databaseHandler;
    MyVendorPreferences mvp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        radioGroup =(RadioGroup)findViewById(R.id.radio_group);
        fab =findViewById(R.id.fab);
        filterTextView =findViewById(R.id.filtertext);
//        productsFilter=findViewById(R.id.products_filter);
        productsFilterCard=findViewById(R.id.products_filter_card);
        addBtn=findViewById(R.id.add_btn);
        recyclerView=findViewById(R.id.products_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        relativeLayout=findViewById(R.id.add_rl);

        progressBar.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);

        getSupportActionBar().setTitle("Products");
        intent=getIntent();
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);

        productListAll =new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(ProductManagementActivity.this,2));
        databaseHandler= new DatabaseHandler(this);
        mvp = new MyVendorPreferences(this);
        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapShot : snapShot.getChildren()) {

                        Product product = childSnapShot.getValue(Product.class);
                        if (product.getStoreId().equals(store.getId())) {
                            productListAll.add(product);
                        }
                    }
                }
                if (productListAll.size() != 0) {
                    for (Product product : productListAll) {
                        databaseHandler.getReviewsReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<Review> reviews = new ArrayList<>();
                                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                    Review review = snapShot.getValue(Review.class);
                                    if (review.getProductId().equals(product.getId())) {
                                        reviews.add(review);
                                    }
                                }
                                product.setReviews(reviews);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Utilities.createAlertDialog(ProductManagementActivity.this, Utilities.reviews_failed_utility, error.getMessage());
                            }
                        });
                    }
                    updateRecyclerView();

                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    relativeLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    productsFilterCard.setVisibility(View.GONE);
                    filterTextView.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductManagementActivity.this, AddProduct.class);
                intent.putExtra(Utilities.intent_store,store);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductManagementActivity.this, AddProduct.class);
                intent.putExtra(Utilities.intent_store,store);
                startActivity(intent);
            }
        });
    }
//    public void showProductCategories(View v){
//        PopupMenu popup = new PopupMenu(v.getContext(), v);
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
        adapter= new ProductAdapterVendor(ProductManagementActivity.this, productList, store);
        recyclerView.setAdapter(adapter);

        for (Product product : productListAll) {
            productList.add(product);
        }
        adapter.notifyDataSetChanged();
    }

    public void updateFromRadio(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton= findViewById(radioId);

        productList =new ArrayList<>();
        adapter= new ProductAdapterVendor(ProductManagementActivity.this, productList, store);
        recyclerView.setAdapter(adapter);

        if(radioButton.getText().toString().equals("All")){
            for (Product product : productListAll) {
                productList.add(product);
            }
        }
        else {
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
        startActivity(new Intent(ProductManagementActivity.this, StoreDashboard.class).putExtra(Utilities.intent_store,store));
        finish();
    }
}