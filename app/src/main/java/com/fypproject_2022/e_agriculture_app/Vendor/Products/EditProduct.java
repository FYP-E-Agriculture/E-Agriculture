package com.fypproject_2022.e_agriculture_app.Vendor.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class EditProduct extends AppCompatActivity {

    TextInputEditText nameET;
    TextInputEditText descriptionET;
    TextInputEditText priceET;
    Button submit;
    ProgressBar progressBar;

    DatabaseHandler databaseHandler;
    Intent intent;
    Product product;
    Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        databaseHandler= new DatabaseHandler(this);
        intent=getIntent();
        product = (Product) intent.getSerializableExtra(Utilities.intent_product);
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        nameET=findViewById(R.id.nameET);
        descriptionET=findViewById(R.id.descriptionET);
        priceET=findViewById(R.id.priceET);
        submit=findViewById(R.id.submit_btn);

        nameET.setText(product.getName());
        descriptionET.setText(product.getDescription());
        priceET.setText(Integer.toString(product.getPrice()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameET.getText().toString().isEmpty() ||
                        descriptionET.getText().toString().isEmpty() ||
                        priceET.getText().toString().isEmpty())
                {
                    Toast.makeText(EditProduct.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);

                    product.setName(nameET.getText().toString());
                    product.setPrice(Integer.parseInt(priceET.getText().toString()));
                    product.setDescription(descriptionET.getText().toString());
                    System.out.println(product.getCategory());
                    System.out.println(product.getId());
                    try {
                        databaseHandler.getProductsReference().child(product.getCategory()).child(product.getId()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(EditProduct.this, ProductManagementActivity.class).putExtra(Utilities.intent_store,store));
                                    Toast.makeText(EditProduct.this, "Product updated successfully", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(EditProduct.this, "FAILED TO UPDATE", Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    catch (Exception exception){
                        Utilities.createAlertDialog(EditProduct.this, "FAILED", exception.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}