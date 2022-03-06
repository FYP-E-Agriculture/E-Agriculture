package com.fypproject_2022.e_agriculture_app.Vendor.Stores;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.DatabaseHandler;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.Models.Vendor;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.fypproject_2022.e_agriculture_app.Vendor.Dashboard.DashboardVendor;
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

public class RegisterStore extends AppCompatActivity {

    Button register;
    TextInputEditText nameET, cityET,addressET, phoneNoET, openingYearET;
    ImageView camera;
    ImageView mStoreImage;
    Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    Store store;
    StorageReference storageReference;
    DatabaseHandler databaseHandler;
    ProgressDialog loadingBar;
//    ProgressBar progressBar;

    MyVendorPreferences mvp;
    Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser_store);
        storageReference = FirebaseStorage.getInstance().getReference(Utilities.user_vendor);
        databaseHandler = new DatabaseHandler(this);
        store = new Store();
        mvp = new MyVendorPreferences(this);
        vendor = mvp.getVendor();

//        progressBar=findViewById(R.id.progress);
        camera = findViewById(R.id.camera_icon);
        mStoreImage = findViewById(R.id.imageView);
        register = findViewById(R.id.register_btn);
        nameET = (TextInputEditText) findViewById(R.id.nameET);
        cityET = findViewById(R.id.cityET);
        addressET = (TextInputEditText) findViewById(R.id.addressET);
        phoneNoET = (TextInputEditText) findViewById(R.id.phoneNoET);
        openingYearET = (TextInputEditText) findViewById(R.id.openingYearET);


        loadingBar = new ProgressDialog(this);
//        progressBar.setVisibility(View.INVISIBLE);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileChooser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emptyCheck()) {
                    Toast.makeText(RegisterStore.this, "PLEASE FILL ALL FIELDS", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (validName() && validPhone())
                    if (mImageUri != null) {
                        loadingBar.setTitle("STORE REGISTRATION");
                        loadingBar.setMessage("Please wait while we register your request!");
                        loadingBar.show();
                        uploadStore();
                    } else {
                        Toast.makeText(RegisterStore.this, "No file Selected", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    private void uploadStore() {
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

                                Store store = new Store();
                                store.setId("0");
                                store.setVendorId(vendor.getId());
                                store.setName(nameET.getText().toString());
                                store.setPhone(phoneNoET.getText().toString());
                                store.setCity(cityET.getText().toString());
                                store.setAddress(addressET.getText().toString());
                                store.setImage(downloadUrl);

                                databaseHandler
                                        .getStoresReference()
                                        .push()
                                        .setValue(store)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    databaseHandler.getStoresReference().addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            Store store = snapshot.getValue(Store.class);
                                                            store.setId(snapshot.getKey());
                                                            databaseHandler
                                                                    .getStoresReference()
                                                                    .child(snapshot.getKey())
                                                                    .setValue(store);
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

                                                    Toast.makeText(RegisterStore.this, "Your request will be reviewed now!", Toast.LENGTH_LONG).show();
                                                    loadingBar.dismiss();
                                                    Intent intent = new Intent(RegisterStore.this, DashboardVendor.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Utilities.createAlertDialog(RegisterStore.this,"Alert",task.getException().getMessage().toString());
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });



                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterStore.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void openProfileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mStoreImage);
        }
    }

    public void showCityPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                cityET.setText(item.getTitle().toString());
                return true;
            }
        });
        popup.inflate(R.menu.city_popup_menu);
        popup.show();
    }

    private boolean emptyCheck() {
        if (openingYearET.getText().toString().isEmpty()
                || nameET.getText().toString().isEmpty()
                || phoneNoET.getText().toString().isEmpty()
                || cityET.getText().toString().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validName() {
        String name = nameET.getText().toString().trim();
        if (name.isEmpty()) {
            nameET.setError("Name is required. Can't be empty");
            return false;
        }
        return true;
    }

    private boolean validPhone() {
        String phone = phoneNoET.getText().toString().trim();
        if (phone.isEmpty()) {
            phoneNoET.setError("Phone Number is required. Can't be empty");
            return false;
        } else if (phone.length() != 11) {
            phoneNoET.setError("Enter a valid 11 digit phone number");
            return false;
        }
        return true;
    }
}