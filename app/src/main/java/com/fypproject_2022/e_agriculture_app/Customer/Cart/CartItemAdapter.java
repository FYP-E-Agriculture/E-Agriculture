package com.fypproject_2022.e_agriculture_app.Customer.Cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ProductDetail;
import com.fypproject_2022.e_agriculture_app.Models.CartItem;
import com.fypproject_2022.e_agriculture_app.Models.Order;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder>{
    Context context;
    List<Product> productList;
    List<CartItem> cartItemList;
    DatabaseHandler databaseHandler;
    static MyCustomerPreferences mcp;

    public CartItemAdapter(Context context, List<Product> productList, List<CartItem> cartItemList) {
        this.context=context;
        this.productList = productList;
        this.cartItemList = cartItemList;
        databaseHandler= new DatabaseHandler(context);
        mcp= new MyCustomerPreferences(context);
    }

    @NonNull
    @Override
    public CartItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        CartItemAdapter.MyViewHolder myViewHolder = new CartItemAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = productList.get(position);
        CartItem cartItem = cartItemList.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("Rs."+Integer.toString(product.getPrice()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetail.class);
                intent.putExtra(Utilities.intent_product, product);
                context.startActivity(intent);
            }
        });

        holder.yes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Order order = new Order();
                order.setCustomerId(mcp.getCustomer().getId().toString());
                order.setProductId(product.getId());
                order.setAmount(product.getPrice());

                //FORMATTING DATE PROPERLY
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utilities.DATE_TIME_FORMAT, Locale.ENGLISH);
                String currentDateTime = LocalDateTime.now().format(formatter);
//                LocalDateTime dateTime = LocalDateTime.parse(currentDateTime, formatter);
                order.setDateTime(currentDateTime);

                order.setStoreId(product.getStoreId());
                order.setStatus(Utilities.pending_utility);
                databaseHandler.getOrdersReference().push().setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseHandler.getOrdersReference().addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                Order order = snapshot.getValue(Order.class);
                                order.setId(snapshot.getKey());
                                databaseHandler.getOrdersReference().child(order.getId()).setValue(order);

                                databaseHandler.getCartReference().child(cartItem.getId()).removeValue();
                                cartItemList.remove(position);
                                productList.remove(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

            }
        });
        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseHandler.getCartReference().child(cartItemList.get(position).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            databaseHandler.getCartReference().child(cartItemList.get(position).getId()).removeValue();
                            notifyDataSetChanged();
                            Toast.makeText(context, "Product removed from cart", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Utilities.createAlertDialog(context,"Error","Something went wrong. Please try again!");
                        }
                    }
                });
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
        ImageView yes;
        ImageView no;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.product_name);
            price=itemView.findViewById(R.id.product_price);
            yes=itemView.findViewById(R.id.yes);
            no=itemView.findViewById(R.id.no);
            cardView=itemView.findViewById(R.id.cardView);

        }
    }
}
