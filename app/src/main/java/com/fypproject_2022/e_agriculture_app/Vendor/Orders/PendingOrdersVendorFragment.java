package com.fypproject_2022.e_agriculture_app.Vendor.Orders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Orders.OrdersAdapter;
import com.fypproject_2022.e_agriculture_app.Models.Order;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrdersVendorFragment extends Fragment {
    ProgressBar progressBar;
    TextView empty;
    int rowCount=0;
    Context context;

    Store store;
    RecyclerView recyclerView;
    List<Order> orderList;
    List<Product> productList;
    OrdersAdapter adapter;

    DatabaseHandler databaseHandler;

    public PendingOrdersVendorFragment(Store store, Context context) {
        this.store = store;
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler= new DatabaseHandler(context);
        orderList= new ArrayList<>();
        productList= new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pending_orders_vendor, container, false);

        progressBar=view.findViewById(R.id.progress);
        empty=view.findViewById(R.id.empty);

        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.INVISIBLE);

        recyclerView=view.findViewById(R.id.pending_orders_recycler_view);
        adapter= new OrdersAdapter(this.getContext(), orderList, productList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));

        databaseHandler.getOrdersReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    System.out.println("Store ID 1:"+order.getStoreId());
                    System.out.println("Store ID 2:"+store.getId());
                    if(order.getStoreId().equals(store.getId())
                            && order.getStatus().equals(Utilities.pending_utility)){
                        System.out.println("FOUND ONE");
                        orderList.add(order);
                        rowCount++;
                        adapter.notifyDataSetChanged();
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                if(rowCount==0){
                    empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}