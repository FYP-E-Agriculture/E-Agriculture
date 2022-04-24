package com.fypproject_2022.e_agriculture_app.Vendor.Common;

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
import com.fypproject_2022.e_agriculture_app.Models.Vendor;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Dashboard.DashboardVendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class VendorProfile extends AppCompatActivity {

    Button make_changes;
    private CircleImageView profileImage;
    private TextView name, email, email2, phone, cnic, address,city, password, changePassword;

    Vendor vendor;
    MyVendorPreferences mrp;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);
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

        mrp = new MyVendorPreferences(this);
        vendor = mrp.getVendor();
        auth = FirebaseAuth.getInstance();

        name.setText(vendor.getName());
        email.setText(vendor.getEmail());
        email2.setText(vendor.getEmail());
        address.setText(vendor.getAddress());
        cnic.setText(vendor.getCnic());
        phone.setText(vendor.getPhone());
        password.setText(vendor.getPassword());
        city.setText(vendor.getCity());
        Picasso.get().load(vendor.getImage()).into(profileImage);

        make_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VendorProfile.this, VendorEditProfile.class));
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
                passwordResetDialog.setMessage("A password reset link will be sent to "+ vendor.getEmail());

                passwordResetDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.sendPasswordResetEmail(vendor.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(VendorProfile.this, "Reset Link Sent To Your Email.", Toast.LENGTH_LONG).show();
                                MyVendorPreferences.setLogin(false);
                                Intent intent = new Intent(VendorProfile.this, SelectUserType.class);
                                intent.putExtra(Utilities.intent_user_category, Utilities.user_customer);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(VendorProfile.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(VendorProfile.this, DashboardVendor.class));
        finish();
    }
}