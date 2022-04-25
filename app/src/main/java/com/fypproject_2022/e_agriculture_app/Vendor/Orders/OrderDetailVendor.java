package com.fypproject_2022.e_agriculture_app.Vendor.Orders;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Orders.OrderDetail;
import com.fypproject_2022.e_agriculture_app.Models.Order;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetailVendor extends AppCompatActivity {

    CircleImageView imageView;

    TextView date;
    TextView status;
    TextView amount;
    Button confirmDispatch;
//    Button accept;
//    Button reject;

    TextView name;
    TextView price;
    TextView category;
    TextView description;

    ProgressBar progressBar;

    Intent intent;
    Product product;
    Order order;
    Store store;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_vendor);

        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar=findViewById(R.id.progress);
        imageView=findViewById(R.id.imageView);

        confirmDispatch=findViewById(R.id.confirm_dispatch);
        date=findViewById(R.id.date_val);
        amount=findViewById(R.id.amount_val);
        status=findViewById(R.id.status_val);
//        accept=findViewById(R.id.accept_button);
//        reject=findViewById(R.id.reject_button);

        name=findViewById(R.id.name_val);
        price=findViewById(R.id.price_val);
        description=findViewById(R.id.description_val);
        category=findViewById(R.id.category_val);
        progressBar.setVisibility(View.INVISIBLE);

        intent=getIntent();
        product= (Product) intent.getSerializableExtra(Utilities.intent_product);
        order= (Order) intent.getSerializableExtra(Utilities.intent_order);
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);
        databaseHandler= new DatabaseHandler(this);

        date.setText(order.getDateTime());
        amount.setText(Integer.toString(order.getAmount()));
        status.setText(order.getStatus());

        Picasso.get().load(product.getImage()).into(imageView);
        name.setText(product.getName());
        category.setText(product.getCategory());
        description.setText(product.getDescription());
        price.setText(Integer.toString(product.getPrice()));

//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        reject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        if(order.getStatus().equals(Utilities.order_pending)){
            confirmDispatch.setVisibility(View.VISIBLE);
        }else{
            confirmDispatch.setVisibility(View.GONE);
        }
        confirmDispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                AlertDialog alertDialog = new AlertDialog.Builder(OrderDetailVendor.this)
                        .setTitle("Dispatch Confirmation")
                        .setMessage("Confirm Order Dispatch?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                order.setStatus(Utilities.order_dispatched);
                                databaseHandler.getOrdersReference().child(order.getId()).setValue(order);
                                Intent intent = new Intent(OrderDetailVendor.this, OrderDetailVendor.class);
                                intent.putExtra(Utilities.intent_product, product);
                                intent.putExtra(Utilities.intent_order, order);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OrderDetailVendor.this, ViewOrdersVendor.class).putExtra(Utilities.intent_store,store));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(OrderDetailVendor.this, ViewOrdersVendor.class).putExtra(Utilities.intent_store,store));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}