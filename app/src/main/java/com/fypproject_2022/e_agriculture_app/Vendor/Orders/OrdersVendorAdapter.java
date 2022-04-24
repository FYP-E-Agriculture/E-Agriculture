package com.fypproject_2022.e_agriculture_app.Vendor.Orders;

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
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.fypproject_2022.e_agriculture_app.Models.Order;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Products.ProductDetailVendor;

import java.util.List;

public class OrdersVendorAdapter extends RecyclerView.Adapter<OrdersVendorAdapter.MyViewHolder> {

    Context context;
    List<Product> productList;
    List<Order> orderList;
    DatabaseHandler databaseHandler;
    MyVendorPreferences mvp;

    public OrdersVendorAdapter(Context context, List<Order> orderList, List<Product> productList) {
        this.context=context;
        this.productList = productList;
        this.orderList = orderList;
        this.databaseHandler= new DatabaseHandler(context);
        this.mvp = new MyVendorPreferences(context);
    }

    @NonNull
    @Override
    public OrdersVendorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_store_item, parent, false);
        OrdersVendorAdapter.MyViewHolder myViewHolder = new OrdersVendorAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersVendorAdapter.MyViewHolder holder, int position) {
        Order order = orderList.get(position);
        if(productList.size()!=0){
            Product product = productList.get(position);
            holder.productName.setText(product.getName());
            holder.city.setText("Rs." + Integer.toString(product.getPrice()));
            holder.customerName.setText(product.getStoreName());
            holder.customer_address.setText(order.getDateTime());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderDetailVendor.class);
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
        TextView productName;
        TextView city;
        TextView customerName;
        TextView customer_address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName =itemView.findViewById(R.id.product_name);
            city =itemView.findViewById(R.id.city_name);
            customer_address =itemView.findViewById(R.id.customer_address);
            customerName =itemView.findViewById(R.id.customer_name);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
