package com.fypproject_2022.e_agriculture_app.Customer.Orders;

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
import com.fypproject_2022.e_agriculture_app.Models.Order;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReceivedOrdersFragment extends Fragment {

    ProgressBar progressBar;
    TextView empty;
    int rowCount=0;

    RecyclerView recyclerView;
    List<Order> orderList;
    List<Product> productList ;
    OrdersAdapter adapter;

    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    public ReceivedOrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler= new DatabaseHandler(this.getContext());
        mcp= new MyCustomerPreferences(this.getContext());
        orderList= new ArrayList<>();
        productList= new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recieved_orders, container, false);
        progressBar=view.findViewById(R.id.progress);
        empty=view.findViewById(R.id.empty);

        recyclerView=view.findViewById(R.id.received_orders_recycler_view);
        adapter= new OrdersAdapter(this.getContext(), orderList, productList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL, false));

        databaseHandler.getOrdersReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if(order.getCustomerId().equals(mcp.getCustomer().getId())
                            && order.getStatus().equals(Utilities.order_complete)){
                        orderList.add(order);
                        rowCount++;
                        adapter.notifyDataSetChanged();
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
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