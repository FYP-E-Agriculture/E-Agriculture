package com.fypproject_2022.e_agriculture_app.Vendor.Orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.material.tabs.TabLayout;

public class ViewOrdersVendor extends AppCompatActivity {

    Store store;
    ViewPager viewPager;
    TabLayout tabLayout;
    MyVendorOrderedFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_vendor);

        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        store = (Store) intent.getSerializableExtra(Utilities.intent_store);

        tabLayout = findViewById(R.id.ordered_items_tab_layout);
        viewPager = findViewById(R.id.ordered_items_view_pager);

        adapter = new MyVendorOrderedFragmentPagerAdapter(getSupportFragmentManager(), store, this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        // Define listeners
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener()
                {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab)
                    {
                        viewPager.setCurrentItem(tab.getPosition());
                    }
                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}