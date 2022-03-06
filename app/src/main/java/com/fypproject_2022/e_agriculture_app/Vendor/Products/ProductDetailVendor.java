package com.fypproject_2022.e_agriculture_app.Vendor.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.ReviewAdapter;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Review;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetailVendor extends AppCompatActivity {

    CircleImageView imageView;
    TextView name;
    TextView price;
    TextView category;
    TextView description;
    ImageView editbtn;
    ImageView deletebtn;
    RecyclerView recyclerView;
    ReviewAdapter adapter;
    TextView title;
    ProgressBar progressBar;
    RatingBar ratingBar;

    Store store;
    Intent intent;
    Product product;
    DatabaseHandler databaseHandler;
    List<Review> reviewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_vendor);

        getSupportActionBar().setTitle("Product Detail");
        ratingBar=findViewById(R.id.ratingBar);
        imageView=findViewById(R.id.imageView);
        name=findViewById(R.id.name_val);
        price=findViewById(R.id.price_val);
        description=findViewById(R.id.description_val);
        category=findViewById(R.id.category_val);
        editbtn=findViewById(R.id.edit_button);
        deletebtn=findViewById(R.id.delete_button);
        recyclerView=findViewById(R.id.reviews_recyclerview);
        title=findViewById(R.id.title_reviews);
        progressBar=findViewById(R.id.progress);

        recyclerView.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        ratingBar.setVisibility(View.INVISIBLE);

        intent=getIntent();
        product= (Product) intent.getSerializableExtra(Utilities.intent_product);
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);
        databaseHandler= new DatabaseHandler(this);

        reviewList= new ArrayList<>();
        adapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(product.getImage()).into(imageView);
        name.setText(product.getName());
        category.setText(product.getCategory());
        description.setText(product.getDescription());
        price.setText(Integer.toString(product.getPrice()));

        databaseHandler.getReviewsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList = new ArrayList<>();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Review review = snapShot.getValue(Review.class);
                    if(review.getProductId().equals(product.getId())){
                        reviewList.add(review);
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                setReviewsVisibility();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setReviewsVisibility();


        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailVendor.this, EditProduct.class);
                intent.putExtra(Utilities.intent_product,product);
                intent.putExtra(Utilities.intent_store,store);
                startActivity(intent);
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProductDetailVendor.this)
                        .setTitle("Confirm Operation")
                        .setMessage("Are you sure?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                // Continue with delete operation
                                databaseHandler.getProductsReference().child(product.getCategory()).child(product.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(ProductDetailVendor.this, "DELETION SUCCESSFUL", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(ProductDetailVendor.this, ProductManagementActivity.class));
                                        }
                                        else{
                                            Toast.makeText(ProductDetailVendor.this, "DELETION FAILED", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                dialog.dismiss();
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

    void setReviewsVisibility(){
        if(reviewList.size()!=0){
            recyclerView.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            adapter = new ReviewAdapter(reviewList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProductDetailVendor.this,ProductManagementActivity.class).putExtra(Utilities.intent_store, store));
    }
}