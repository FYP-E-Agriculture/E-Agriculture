package com.fypproject_2022.e_agriculture_app.Customer.Cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Models.CartItem;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    TextView empty;
    ProgressBar progressBar;

    public static TextView total;
    Button completeBtn;

    DatabaseHandler databaseHandler;
    MyCustomerPreferences mcp;

    CartItemAdapter adapter;
    List<Product> productList;
    List<CartItem> cartItemList;

    RecyclerView recyclerView;

    int totalAmount=0;
    public int itemCount=0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle("My Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        recyclerView=findViewById(R.id.cart_recycler_view);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        empty=findViewById(R.id.empty);
        total=findViewById(R.id.total_val);
        completeBtn =findViewById(R.id.complete_sale_button);
        
        databaseHandler=new DatabaseHandler(this);
        mcp = new MyCustomerPreferences(this);

        productList = new ArrayList<>();
        cartItemList = new ArrayList<>();

        adapter = new CartItemAdapter(this, productList, cartItemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        progressBar.setVisibility(View.VISIBLE);
        empty.setVisibility(View.INVISIBLE);

        databaseHandler.getCartReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empty.setVisibility(View.INVISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if(cartItem.getCustomerId().equals(mcp.getCustomer().getId())){
                        databaseHandler.getProductsReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                    for (DataSnapshot snapshot2: snapshot1.getChildren()) {
                                        Product product = snapshot2.getValue(Product.class);
                                        if(product.getId().equals(cartItem.getProductId())){
                                            productList.add(product);
                                            cartItemList.add(cartItem);

                                            itemCount++;
                                            totalAmount += product.getPrice();
                                            total.setText(Integer.toString(totalAmount));
                                            adapter.notifyDataSetChanged();
                                            if(productList.size()==0){
                                                empty.setVisibility(View.INVISIBLE);
                                            }

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Utilities.createAlertDialog(CartActivity.this, "Error", error.getMessage().toString());
                            }
                        });
                    }
                }

//                if(cartItemList.size()==0){
//                    empty.setVisibility(View.VISIBLE);
//                }else{
//                    empty.setVisibility(View.INVISIBLE);
//                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Utilities.createAlertDialog(CartActivity.this, "Error", error.getMessage().toString());
            }
        });


        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}