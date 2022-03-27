package com.fypproject_2022.e_agriculture_app.Customer.Products;

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
import com.fypproject_2022.e_agriculture_app.Customer.Stores.StoreDetail;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoresCustomerAdapter extends RecyclerView.Adapter<StoresCustomerAdapter.MyViewHolder> {

    Context context;
    List<Store> storeList;

    public StoresCustomerAdapter(Context context, List<Store> storeList) {
        this.context=context;
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public StoresCustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_item, parent, false);
        StoresCustomerAdapter.MyViewHolder myViewHolder = new StoresCustomerAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoresCustomerAdapter.MyViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.storeName.setText(store.getName());
        Picasso.get().load(store.getImage()).into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoreDetail.class);
                intent.putExtra(Utilities.intent_store, store);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView storeName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            storeName=itemView.findViewById(R.id.store_name_title);
            cardView=itemView.findViewById(R.id.card_view);
        }
    }
}