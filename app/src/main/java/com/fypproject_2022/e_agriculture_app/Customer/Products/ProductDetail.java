package com.fypproject_2022.e_agriculture_app.Customer.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.ReviewAdapter;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Models.CartItem;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Review;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductDetail extends AppCompatActivity {


    CircleImageView imageView;
    TextView name;
    TextView price;
    TextView category;
    TextView description;
    TextView title_reviews;
    ImageView addBtn;
    ProgressBar progressBar;
    RatingBar ratingBar;

    Intent intent;
    Product product;
    Store store;
    DatabaseHandler databaseHandler;
    RecyclerView recyclerView;
    ReviewAdapter adapter;
    List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getSupportActionBar().setTitle("Product Detail");

        ratingBar=findViewById(R.id.ratingBar);
        progressBar=findViewById(R.id.progress);
        imageView=findViewById(R.id.imageView);
        name=findViewById(R.id.name_val);
        price=findViewById(R.id.price_val);
        description=findViewById(R.id.description_val);
        category=findViewById(R.id.category_val);
        recyclerView=findViewById(R.id.reviews_recyclerview);
        title_reviews =findViewById(R.id.title_reviews);
        addBtn=findViewById(R.id.add_to_cart_button);
        progressBar.setVisibility(View.VISIBLE);

        intent=getIntent();
        product= (Product) intent.getSerializableExtra(Utilities.intent_product);
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);
        databaseHandler= new DatabaseHandler(this);

        recyclerView.setVisibility(View.INVISIBLE);
        title_reviews.setVisibility(View.INVISIBLE);
        ratingBar.setVisibility(View.INVISIBLE);

        reviewList= new ArrayList<>();
        adapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

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

        Picasso.get().load(product.getImage()).into(imageView);
        name.setText(product.getName());
        category.setText(product.getCategory());
        description.setText(product.getDescription());
        price.setText(Integer.toString(product.getPrice()));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartItem cartItem = new CartItem("0",product.getId(), MyCustomerPreferences.getCustomer().getId(), product.getStoreId());
                databaseHandler.getCartReference().push().setValue(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseHandler.getCartReference().addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                CartItem cartItem1=snapshot.getValue(CartItem.class);
                                cartItem1.setId(snapshot.getKey());
                                databaseHandler.getCartReference().child(cartItem1.getId()).setValue(cartItem1);
                                Toast.makeText(ProductDetail.this, "Item added to cart", Toast.LENGTH_SHORT).show();
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
                                Utilities.createAlertDialog(ProductDetail.this, "Error", error.getMessage());
                            }
                        });
                    }
                });
            }
        });
    }

    void setReviewsVisibility(){
        if(reviewList.size()!=0){
            recyclerView.setVisibility(View.VISIBLE);
            title_reviews.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            adapter = new ReviewAdapter(reviewList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProductDetail.this,ViewProducts.class));
    }
}