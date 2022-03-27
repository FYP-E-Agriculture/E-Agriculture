package com.fypproject_2022.e_agriculture_app.Customer.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Customer.Dashboard.Dashboard;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewStores extends AppCompatActivity {

    TextView title;
    ProgressBar progressBar;
    RecyclerView recyclerViewStores;
    StoresCustomerAdapter adapterStores;
    List<Store> storeList;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stores);

        title = findViewById(R.id.no_stores_title);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        title.setVisibility(View.INVISIBLE);
        getSupportActionBar().setTitle("Stores");

        recyclerViewStores =findViewById(R.id.stores_recycler_view);
        storeList= new ArrayList<>();
        adapterStores = new StoresCustomerAdapter(this,storeList);
        recyclerViewStores.setAdapter(adapterStores);
        recyclerViewStores.setLayoutManager(new GridLayoutManager(this, 2));

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ViewStores.this, Dashboard.class));
    }
}