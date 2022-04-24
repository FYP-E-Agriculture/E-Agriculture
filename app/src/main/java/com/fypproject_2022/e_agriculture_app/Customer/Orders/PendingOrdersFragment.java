package com.fypproject_2022.e_agriculture_app.Customer.Orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ProductAdapter;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ViewProducts;
import com.fypproject_2022.e_agriculture_app.Models.Order;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class PendingOrdersFragment extends Fragment {

    ProgressBar progressBar;
    TextView empty;
    int rowCount=0;

    RecyclerView recyclerView;
    List<Order> orderList;
    List<Product> productList;
    OrdersAdapter adapter;

    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    public PendingOrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHandler= new DatabaseHandler(getActivity().getApplicationContext());
        mcp= new MyCustomerPreferences(getActivity().getApplicationContext());
        orderList= new ArrayList<>();
        productList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);
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
                    System.out.println("ORDER CUST ID:"+order.getCustomerId());
                    System.out.println("CUSTOMER ID:"+mcp.getCustomer().getId());
                    if(order.getCustomerId().equals(mcp.getCustomer().getId()) && order.getStatus().equals(Utilities.order_pending)){
                        orderList.add(order);
                        rowCount++;
                        System.out.println("MATCHED");

                        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                    for (DataSnapshot snapshot2: snapshot.getChildren()) {
                                        Product product = snapshot2.getValue(Product.class);
                                        System.out.println("PRODUCT ID:"+product.getId());
                                        if(product.getId().equals(order.getProductId())
                                                && order.getCustomerId().equals(mcp.getCustomer().getId())
                                                && (order.getStatus().equals(Utilities.order_pending) || order.getStatus().equals(Utilities.order_dispatched))) {
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
                if(rowCount==0){
                    empty.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);

//                databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
//
//                            for (DataSnapshot snapshot2: snapshot.getChildren()) {
//
//                                Product product = snapshot2.getValue(Product.class);
//
//                                for (Order order : orderList) {
//                                    if(product.getId().equals(order.getProductId())
//                                            && order.getCustomerId().equals(mcp.getCustomer().getId())
//                                            && order.getStatus().equals(Utilities.order_pending)){
//                                        productList.add(product);
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                }
//                            }
//                        }
//                        if(rowCount==0){
//                            empty.setVisibility(View.VISIBLE);
//                        }
//
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
//
//    void updateRecyclerView(){
//        orderList =new ArrayList<>();
//        adapter= new OrdersAdapter(getActivity().getApplicationContext(), orderList, productList);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));
//
//        for (Order order : orderList) {
//            orderList.add(order);
//        }
//        adapter.notifyDataSetChanged();
//    }
}