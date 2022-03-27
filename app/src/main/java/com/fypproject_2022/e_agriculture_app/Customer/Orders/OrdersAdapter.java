package com.fypproject_2022.e_agriculture_app.Customer.Orders;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ProductDetail;
import com.fypproject_2022.e_agriculture_app.Models.Order;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Products.ProductDetailVendor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    Context context;
    List<Product> productList;
    List<Order> orderList;
    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    public OrdersAdapter(Context context, List<Order> orderList, List<Product> productList) {
        this.context=context;
        this.productList = productList;
        this.orderList = orderList;
        this.databaseHandler= new DatabaseHandler(context);
        this.mcp= new MyCustomerPreferences(context);
    }

    @NonNull
    @Override
    public OrdersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
        OrdersAdapter.MyViewHolder myViewHolder = new OrdersAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.MyViewHolder holder, int position) {

        if(orderList.size() > 0 && productList.size() > 0) {
            Order order = orderList.get(position);
            Product product = productList.get(position);
            holder.name.setText(product.getName());
            holder.price.setText("Rs." + Integer.toString(product.getPrice()));
            holder.store.setText(product.getStoreName());
            holder.date.setText(order.getDateTime());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderDetail.class);
                    intent.putExtra(Utilities.intent_product, product);
                    intent.putExtra(Utilities.intent_order, order);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView price;
        TextView store;
        TextView date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.product_name);
            price=itemView.findViewById(R.id.product_price);
            date=itemView.findViewById(R.id.order_date);
            store=itemView.findViewById(R.id.product_store);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
