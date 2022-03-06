package com.fypproject_2022.e_agriculture_app.Customer.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Review;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class AddReview extends AppCompatActivity {

    TextInputLayout reviewText;
    Button add;
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

        progressBar = (ProgressBar)findViewById(R.id.progress);
        reviewText =findViewById(R.id.pass_top);
        add=findViewById(R.id.confirm_button);

        databaseHandler= new DatabaseHandler(this);
        intent = getIntent();
        store = (Store) intent.getSerializableExtra(Utilities.intent_store);
        product = (Product) intent.getSerializableExtra(Utilities.intent_product);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                review = new Review();
                review.setDescription(reviewText.getEditText().getText().toString());
                review.setProductId(product.getId());
                review.setReviewerName(MyCustomerPreferences.getCustomer().getName());
                databaseHandler.getReviewsReference().push().setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddReview.this, "Review Added", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(AddReview.this, ProductDetail.class);
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
        });
    }
}