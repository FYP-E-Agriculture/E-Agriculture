package com.fypproject_2022.e_agriculture_app.Customer.Orders;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Order;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetail extends AppCompatActivity {

    Button writeReviewBtn;
    Button confirmReceivedBtn;
    CircleImageView imageView;

    TextView date;
    TextView status;
    TextView amount;

    TextView name;
    TextView price;
    TextView category;
    TextView description;

//    ProgressBar progressBar;

    Intent intent;
    Product product;
    Order order;
    Store store;
    DatabaseHandler databaseHandler;
    FirebaseUser user;
    FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        getSupportActionBar().setTitle("Order Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        progressBar=findViewById(R.id.progress);
        imageView=findViewById(R.id.imageView);

        date=findViewById(R.id.date_val);
        writeReviewBtn =findViewById(R.id.write_review);
        confirmReceivedBtn =findViewById(R.id.confirm_received);
        amount=findViewById(R.id.amount_val);
        status=findViewById(R.id.status_val);

        name=findViewById(R.id.name_val);
        price=findViewById(R.id.price_val);
        description=findViewById(R.id.description_val);
        category=findViewById(R.id.category_val);
//        progressBar.setVisibility(View.VISIBLE);

        intent=getIntent();
        product= (Product) intent.getSerializableExtra(Utilities.intent_product);
        order= (Order) intent.getSerializableExtra(Utilities.intent_order);
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);
        databaseHandler= new DatabaseHandler(this);
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        //CONFIRM RECEIVED button will only be visible if the customer is yet to receive the product
        //ADD RATING button will only be visible if the customer has received the product
        if(order.getStatus().equals(Utilities.order_pending)){
            writeReviewBtn.setVisibility(View.INVISIBLE);
            confirmReceivedBtn.setVisibility(View.VISIBLE);
        }
        date.setText(order.getDateTime());

        amount.setText(Integer.toString(order.getAmount()));
        status.setText(order.getStatus());

        Picasso.get().load(product.getImage()).into(imageView);
        name.setText(product.getName());
        category.setText(product.getCategory());
        description.setText(product.getDescription());
        price.setText(Integer.toString(product.getPrice()));

        writeReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrderDetail.this, AddReview.class);
                intent.putExtra(Utilities.intent_product, product);
                intent.putExtra(Utilities.intent_store, store);
                startActivity(intent);
            }
        });

        confirmReceivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(OrderDetail.this)
                        .setTitle("Order Confirmation")
                        .setMessage("Confirm Order Receival?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                order.setStatus(Utilities.order_complete);
                                databaseHandler.getOrdersReference().child(order.getId()).setValue(order);
                                startActivity(new Intent(OrderDetail.this, OrderDetail.class));
                                finish();
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
        finish();
//        startActivity(new Intent(ProductDetail.this, ViewProducts.class));
    }
}