package com.fypproject_2022.e_agriculture_app.Customer.Orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ProductDetail;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Review;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class AddReview extends AppCompatActivity {

    RatingBar ratingBar;

    TextInputLayout reviewText;
    Button submit;
    ProgressBar progressBar;

    Review review;
    DatabaseHandler databaseHandler;
    Intent intent;
    Store store;
    Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        getSupportActionBar().setTitle("Write a review...");

        ratingBar = findViewById(R.id.ratingBar);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        reviewText =findViewById(R.id.pass_top);
        submit =findViewById(R.id.confirm_button);

        databaseHandler= new DatabaseHandler(this);
        intent = getIntent();
        store = (Store) intent.getSerializableExtra(Utilities.intent_store);
        product = (Product) intent.getSerializableExtra(Utilities.intent_product);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ratingBar.getRating()!=0.0f){
                    progressBar.setVisibility(View.VISIBLE);

                    review = new Review();

                    //Converting string value to float
                    String rating = String.valueOf(ratingBar.getRating());
                    float floatRating= Float.parseFloat(rating);
                    Toast.makeText(AddReview.this,rating+" Stars",Toast.LENGTH_SHORT).show();

                    review.setDescription(reviewText.getEditText().getText().toString());
                    review.setProductId(product.getId());
                    review.setRating(floatRating);

                    review.setReviewerName(MyCustomerPreferences.getCustomer().getName());
                    databaseHandler.getReviewsReference().push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddReview.this, "Review Added", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(AddReview.this, OrderDetail.class);
                                intent.putExtra(Utilities.intent_store,store);
                                intent.putExtra(Utilities.intent_product,product);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(AddReview.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(AddReview.this, "Please Add a Rating!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}