package com.fypproject_2022.e_agriculture_app.Customer.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Common.SelectUserType;
import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Customer.Dashboard.Dashboard;
import com.fypproject_2022.e_agriculture_app.Models.Customer;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfile extends AppCompatActivity {

    Button make_changes;
    private CircleImageView profileImage;
    private TextView name, email, email2, phone, cnic, address,city, password, changePassword;

    Customer customer;
    MyCustomerPreferences mrp;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        make_changes=findViewById(R.id.edit_btn);
        profileImage = findViewById(R.id.receiver_profile_image);
        name = findViewById(R.id.name_tv);
        email = findViewById(R.id.email_tv);
        email2 = findViewById(R.id.tv_email_2);
        phone = findViewById(R.id.tv_phone);
        cnic = findViewById(R.id.tv_cnic);
        address = findViewById(R.id.tv_address);
        city = findViewById(R.id.tv_city);
        password = findViewById(R.id.tv_password);
        changePassword = findViewById(R.id.change_password);

        mrp = new MyCustomerPreferences(this);
        customer = mrp.getCustomer();
        auth = FirebaseAuth.getInstance();

        name.setText(customer.getName());
        email.setText(customer.getEmail());
        email2.setText(customer.getEmail());
        address.setText(customer.getAddress());
        cnic.setText(customer.getCnic());
        phone.setText(customer.getPhone());
        password.setText(customer.getPassword());
        city.setText(customer.getCity());
        Picasso.get().load(customer.getImage()).into(profileImage);

        make_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerProfile.this, CustomerEditProfile.class));
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setTransformationMethod(null);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("A password reset link will be sent to "+ customer.getEmail());

                passwordResetDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.sendPasswordResetEmail(customer.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CustomerProfile.this, "Reset Link Sent To Your Email.", Toast.LENGTH_LONG).show();
                                MyCustomerPreferences.setLogin(false);
                                Intent intent = new Intent(CustomerProfile.this, SelectUserType.class);
                                intent.putExtra(Utilities.intent_user_category, Utilities.user_customer);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CustomerProfile.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CustomerProfile.this, Dashboard.class));
        finish();
    }
}