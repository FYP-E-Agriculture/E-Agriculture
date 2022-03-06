package com.fypproject_2022.e_agriculture_app.Vendor.Products;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.Models.Vendor;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProduct extends AppCompatActivity {

    TextInputEditText nameET;
    TextInputEditText descriptionET;
    TextInputEditText priceET;
    TextInputEditText categoryET;
    CircleImageView imageView;
    Button submit;

    Store store;
    Intent intent;

    StorageReference storageReference;
    DatabaseHandler databaseHandler;
    MyVendorPreferences mvp;
    Vendor vendor;
    ImageView camera;
    ImageView productImage;
    Uri mImageUri;

    ProgressDialog loadingBar;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        databaseHandler= new DatabaseHandler(this);
        mvp = new MyVendorPreferences(this);
        vendor=mvp.getVendor();
        storageReference = FirebaseStorage.getInstance().getReference(getString(R.string.node_products));
        camera=findViewById(R.id.camera_icon);
        nameET=findViewById(R.id.nameET);
        categoryET=findViewById(R.id.categoryET);
        descriptionET=findViewById(R.id.descriptionET);
        priceET=findViewById(R.id.priceET);
        imageView=findViewById(R.id.imageView);
        submit=findViewById(R.id.submit_btn);
        productImage=findViewById(R.id.imageView);

        intent=getIntent();
        store= (Store) intent.getSerializableExtra(Utilities.intent_store);

        loadingBar = new ProgressDialog(this);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileChooser();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mImageUri !=null) {
                    if(emptyCheck()) {
                        Toast.makeText(AddProduct.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        loadingBar.hide();
                    }
                    else {
                        loadingBar.setTitle("UPLOADING PRODUCT");
                        loadingBar.setMessage("Please wait while your product is uploading");
                        loadingBar.show();
                        uploadProduct();
                    }
                }
                else
                {
                    Toast.makeText(AddProduct.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void uploadProduct()
    {
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));
        fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String downloadUrl = uri.toString();

                                Product product = new Product();
                                product.setName(nameET.getText().toString());
                                product.setCategory(categoryET.getText().toString());
                                product.setPrice(Integer.parseInt(priceET.getText().toString()));
                                product.setDescription(descriptionET.getText().toString());
                                product.setName(nameET.getText().toString());
                                product.setImage(downloadUrl);
                                product.setStoreId(store.getId());
                                product.setStoreName(store.getName());

                                databaseHandler.getProductsReference().child(product.getCategory())
                                        .push()
                                        .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            Toast.makeText(AddProduct.this, "PRODUCT UPLOADED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                            loadingBar.dismiss();
                                            databaseHandler.getProductsReference().child(product.getCategory())
                                                    .addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            Product product1 = snapshot.getValue(Product.class);
                                                            System.out.println("PRODUCT NAME:"+product1.getName());
                                                            product1.setId(snapshot.getKey());
                                                            System.out.println("PRODUCT ID:"+product1.getId());
                                                            databaseHandler
                                                                    .getProductsReference()
                                                                    .child(product1.getCategory())
                                                                    .child(product1.getId())
                                                                    .setValue(product1)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            Intent intent = new Intent(AddProduct.this, ProductManagementActivity.class);
                                                                            intent.putExtra(Utilities.intent_store,store);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                    });
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
                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void openProfileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null)
        {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(productImage);
        }
    }

    public void showProductCategoryPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                categoryET.setText(item.getTitle().toString());
                return true;
            }
        });
        popup.inflate(R.menu.product_category_popup_menu);
        popup.show();
    }
    public boolean emptyCheck(){
        if(nameET.getText().toString().isEmpty() ||
                priceET.getText().toString().isEmpty() ||
                categoryET.getText().toString().isEmpty() ||
                descriptionET.getText().toString().isEmpty())
        {
            return true;
        }
        return false;
    }
}