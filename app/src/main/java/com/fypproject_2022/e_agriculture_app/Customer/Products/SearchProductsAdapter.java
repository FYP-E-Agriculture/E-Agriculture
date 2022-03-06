package com.fypproject_2022.e_agriculture_app.Customer.Products;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.MyViewHolder>{

    Context context;
    List<Product> productList;
    DatabaseHandler databaseHandler;

    public SearchProductsAdapter(Context context, List<Product> productList) {
        this.context=context;
        this.productList = productList;
        this.databaseHandler= new DatabaseHandler(context);
    }

    @NonNull
    @Override
    public SearchProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        SearchProductsAdapter.MyViewHolder myViewHolder = new SearchProductsAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductsAdapter.MyViewHolder holder, int position) {
        Product product = productList.get(position);

        Picasso.get().load(product.getImage()).into(holder.imageView);
        holder.name.setText(product.getName());
        holder.price.setText("Rs."+Integer.toString(product.getPrice()));
        holder.storeName.setText(product.getStoreName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra(Utilities.intent_product, product);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CircleImageView imageView;
        TextView name;
        TextView storeName;
        TextView price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            storeName=itemView.findViewById(R.id.store_name);
            name=itemView.findViewById(R.id.product_name);
            price=itemView.findViewById(R.id.product_price);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
