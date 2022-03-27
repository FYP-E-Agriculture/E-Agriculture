package com.fypproject_2022.e_agriculture_app.Vendor.Products;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapterVendor extends RecyclerView.Adapter<ProductAdapterVendor.MyViewHolder> {

    Context context;
    Store store;
    List<Product> productList;

    public ProductAdapterVendor(Context context, List<Product> productList, Store store) {
        this.context=context;
        this.store=store;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapterVendor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterVendor.MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.storeName.setText(product.getStoreName());
        Picasso.get().load(product.getImage()).into(holder.imageView);
        holder.price.setText("Rs."+Integer.toString(product.getPrice()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(context, ProductDetailVendor.class);
                    intent.putExtra(Utilities.intent_product, product);
                    intent.putExtra(Utilities.intent_store, store);
                    context.startActivity(intent);
                }
                catch (Exception e){
                    Utilities.createAlertDialog(context,"Error", e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView price;
        TextView storeName;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.product_name);
            storeName=itemView.findViewById(R.id.product_store_name);
            imageView=itemView.findViewById(R.id.image);
            price=itemView.findViewById(R.id.product_price);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
