package com.fypproject_2022.e_agriculture_app.Common;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fypproject_2022.e_agriculture_app.Admin.MyAdminPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Products.ProductDetail;
import com.fypproject_2022.e_agriculture_app.Models.Admin;
import com.fypproject_2022.e_agriculture_app.Models.Customer;
import com.fypproject_2022.e_agriculture_app.Models.Product;
import com.fypproject_2022.e_agriculture_app.Models.Review;
import com.fypproject_2022.e_agriculture_app.Models.Store;
import com.fypproject_2022.e_agriculture_app.Models.Vendor;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.fypproject_2022.e_agriculture_app.Vendor.Products.ProductManagementActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    Context context;
    MyCustomerPreferences customerPreferences;
    MyAdminPreferences adminPreferences;
    MyVendorPreferences vendorPreferences;

    Admin admin;
    Customer customer;
    Vendor vendor;
    Product product;
    Store store;

    List<Customer> customers;
    List<Vendor> vendors;
    List<Product> products;
    List<Review> reviews;
    List<Store> stores;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseUser user;

    Resources resources;

    public DatabaseHandler(Context context) {

        this.customers= new ArrayList<>();
        this.vendors= new ArrayList<>();
        this.products= new ArrayList<>();

        this.context=context;
        this.database= FirebaseDatabase.getInstance();
        this.reference= database.getReference();
        this.storage= FirebaseStorage.getInstance();
        this.auth= FirebaseAuth.getInstance();

        this.adminPreferences= new MyAdminPreferences(context);
        this.customerPreferences= new MyCustomerPreferences(context);
        this.vendorPreferences = new MyVendorPreferences(context);

        this.resources=context.getResources();
    }

//    public void save_to_level_1(String child1){
//        if(!child1.isEmpty()) {
//            reference.child(child1).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(context, "SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
//                    } else if (task.isCanceled()) {
//                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }else{
//            Toast.makeText(context, "INVALID NODE PATH", Toast.LENGTH_SHORT).show();
//        }
//    }
//    public void save_to_level_2(String child1, String child2){
//        if(!child1.isEmpty() && !child2.isEmpty()){
//            reference
//                    .child(child1)
//                    .child(child2)
//                    .setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()){
//                        Toast.makeText(context, "SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
//                    }
//                    else if(task.isCanceled()){
//                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }else{
//            Toast.makeText(context, "INVALID NODE PATH", Toast.LENGTH_SHORT).show();
//        }
//    }
//
    public DatabaseReference get_reference_level_1(String child1){
        return reference.child(child1);
    }
    public DatabaseReference get_reference_level_2(String child1, String child2){
        return reference.child(child1).child(child2);
    }
    public DatabaseReference get_reference_level_3(String child1, String child2, String child3){
        return reference.child(child1).child(child2).child(child3);
    }
    public DatabaseReference get_reference_level_4(String child1, String child2, String child3, String child4){
        return reference.child(child1).child(child2).child(child3).child(child4);
    }

    public DatabaseReference getVendorsReference(){
        return get_reference_level_1(context.getResources().getString(R.string.node_vendors));
    }
    public DatabaseReference getStoresReference(){
        return get_reference_level_1(context.getResources().getString(R.string.node_stores));
    }
    public DatabaseReference getProductsReference(){
        return get_reference_level_1(context.getResources().getString(R.string.node_products));
    }
    public DatabaseReference getReviewsReference(){
        return get_reference_level_1(context.getResources().getString(R.string.node_reviews));
    }
    public DatabaseReference getCustomersReference(){
        return get_reference_level_1(context.getResources().getString(R.string.node_customers));
    }public DatabaseReference getAdminReference(){
        return get_reference_level_1(context.getResources().getString(R.string.node_admin));
    }
    public DatabaseReference getOrdersReference(){
        return get_reference_level_1(context.getResources().getString(R.string.node_orders));
    }
    public DatabaseReference getCartReference(){
        return get_reference_level_1(context.getResources().getString(R.string.node_cart));
    }

    public List<Customer> getCustomers(){
        getCustomersReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customers= new ArrayList<>();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    customers.add(snapShot.getValue(Customer.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utilities.createAlertDialog(context, Utilities.customers_failed_utility,error.getMessage());
            }
        });
        return this.customers;
    }
    public List<Vendor> getVendors(){
        getVendorsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vendors= new ArrayList<>();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    vendors.add(snapShot.getValue(Vendor.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utilities.createAlertDialog(context, Utilities.vendors_failed_utility,error.getMessage());
            }
        });
        return this.vendors;
    }
    public List<Product> getProducts(){
        getProductsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products= new ArrayList<>();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    products.add(snapShot.getValue(Product.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utilities.createAlertDialog(context, Utilities.products_failed_utility,error.getMessage());
            }
        });
        return this.products;
    }

//    public List<Product> getProductsByCategory(String productCategory){
//        getProductsReference().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                products= new ArrayList<>();
//                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
//                    Product product = snapShot.getValue(Product.class);
//                    if(product.getCategory().equals(productCategory)){
//                        products.add(product);
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Utilities.createAlertDialog(context, resources.getString(R.string.products_failed_utility),error.getMessage());
//            }
//        });
//        return this.products;
//    }

    public List<Review> getReviews(String productId){
        getReviewsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviews= new ArrayList<>();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Review review = snapShot.getValue(Review.class);
                    if(review.getProductId().equals(productId)){
                        reviews.add(review);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utilities.createAlertDialog(context, Utilities.reviews_failed_utility,error.getMessage());
            }
        });
        return this.reviews;
    }


    public Customer loginAdmin(String email, String password){
        admin = new Admin();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    getAdminReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            admin = dataSnapshot.getValue(Admin.class);
                            adminPreferences.saveAdmin(admin);
                            adminPreferences.setLogin(true);
                            Login.progressBar.setVisibility(View.INVISIBLE);
                            Login.getAdminLoginData();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Login.progressBar.setVisibility(View.INVISIBLE);
                    Utilities.createAlertDialog(context, Utilities.login_failed_utility,task.getException().getMessage());

                }
            }
        });

        return customer;
    }
    public Customer loginCustomer(String email, String password){
        customer = new Customer();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    user = auth.getCurrentUser();
                    if (user.isEmailVerified()) {

                        getCustomersReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                    customer = snapShot.getValue(Customer.class);
                                    customer.setId(snapShot.getKey());
                                    if (customer.getId().equals(auth.getCurrentUser().getUid())) {
                                        customerPreferences.saveCustomer(customer);
                                        customerPreferences.setLogin(true);
                                        Login.getCustomerLoginData();
                                        Login.progressBar.setVisibility(View.INVISIBLE);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Utilities.createAlertDialog(context, "Error", "Please verify this email first");
                        Login.progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Login.progressBar.setVisibility(View.INVISIBLE);
                    Utilities.createAlertDialog(context, Utilities.login_failed_utility, task.getException().getLocalizedMessage());

                }
            }
        });

        return customer;
    }

    public List<Store> getStores(String vendorId){
        stores = new ArrayList<>();
        getStoresReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    store = snapshot.getValue(Store.class);
                    if(store.getId().equals(vendorId)){
                        stores.add(store);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return stores;
    }

    public Vendor loginVendor(String email, String password){
        vendor = new Vendor();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    user = auth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        Log.d("LOGIN", "EMAIL VERIFIED");
                        getVendorsReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                                    Log.d("LOGIN", "VENDOR OBJECT FOUND");
                                    vendor = snapShot.getValue(Vendor.class);
                                    Log.d("LOGIN", "VENDOR NAME\t"+vendor.getName());
                                    Log.d("LOGIN", "VENDOR ID\t"+vendor.getId());
                                    if (vendor.getId().equals(auth.getCurrentUser().getUid())) {
                                        Log.d("LOGIN", "VENDOR MATCHED");
                                        List<Store> stores1 = getStores(vendor.getId());
                                        if (stores1 != null) {
                                            vendor.setStoreList(stores1);
                                        }

                                        vendorPreferences.saveVendor(vendor);
                                        vendorPreferences.setLogin(true);
                                        Log.d("LOGIN", "VENDOR SAVED");
                                        Log.d("LOGIN", "VENDOR NAME: "+vendor.getName());
                                        Login.getVendorLoginData();
                                        Login.progressBar.setVisibility(View.INVISIBLE);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Utilities.createAlertDialog(context,"Error","Please verify this email first");
                        Login.progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                else{
                    Utilities.createAlertDialog(context, Utilities.login_failed_utility,task.getException().getLocalizedMessage());
                    Login.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        return vendor;
    }

}
