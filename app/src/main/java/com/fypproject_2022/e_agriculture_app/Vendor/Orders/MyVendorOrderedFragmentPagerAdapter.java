package com.fypproject_2022.e_agriculture_app.Vendor.Orders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fypproject_2022.e_agriculture_app.Models.Store;

public class MyVendorOrderedFragmentPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    Store store;
    Context context;

    public MyVendorOrderedFragmentPagerAdapter(@NonNull FragmentManager fm, Store store, Context context) {
        super(fm);
        this.store=store;
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewOrdersFragment(store, context);
            case 1:
                return new DispatchedOrdersFragment(store, context);
            case 2:
                return new DeliveredOrdersFragment(store, context);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "New";
            }
            case 1: {
                return "Dispatched";
            }
            case 2: {
                return "Delivered";
            }
        }
        return null;
    }
}
