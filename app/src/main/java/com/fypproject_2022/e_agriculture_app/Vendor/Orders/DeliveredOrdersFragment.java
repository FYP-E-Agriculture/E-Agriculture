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

public class DeliveredOrdersFragment extends Fragment {

    ProgressBar progressBar;
    TextView empty;
    int rowCount=0;

    Context context;
    Store store;
    RecyclerView recyclerView;
    List<Order> orderList;
    List<Product> productList;
    OrdersVendorAdapter adapter;

    DatabaseHandler databaseHandler;
    public DeliveredOrdersFragment(Store store, Context context) {
        this.store=store;
        this.context=context;
        this.orderList= new ArrayList<>();
        this.productList= new ArrayList<>();
        this.databaseHandler= new DatabaseHandler(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delivered_orders, container, false);

        progressBar=view.findViewById(R.id.progress);
        empty=view.findViewById(R.id.empty);

        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.INVISIBLE);

        recyclerView=view.findViewById(R.id.delivered_orders_recycler_view);
        adapter= new OrdersVendorAdapter(this.getContext(), store, orderList, productList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));

        databaseHandler.getOrdersReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    System.out.println("Store ID 1:"+order.getStoreId());
                    System.out.println("Store ID 2:"+store.getId());
                    if(order.getStoreId().equals(store.getId()) && order.getStatus().equals(Utilities.order_complete)){
                        System.out.println("FOUND ONE");
                        orderList.add(order);
                        rowCount++;
                        adapter.notifyDataSetChanged();

                        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                    for (DataSnapshot snapshot2: snapshot.getChildren()) {
                                        Product product = snapshot2.getValue(Product.class);
                                        System.out.println("PRODUCT ID:"+product.getId());
                                        if(product.getId().equals(order.getProductId())
                                                && order.getStoreId().equals(store.getId())
                                                && (order.getStatus().equals(Utilities.order_complete))) {
                                            productList.add(product);
                                            System.out.println("MATCHED");
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
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