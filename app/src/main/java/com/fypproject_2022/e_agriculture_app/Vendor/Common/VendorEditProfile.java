package com.fypproject_2022.e_agriculture_app.Vendor.Common;

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
import com.fypproject_2022.e_agriculture_app.Models.Vendor;
import com.fypproject_2022.e_agriculture_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class VendorEditProfile extends AppCompatActivity {

    private TextInputLayout
            mName,
            mPhone,
            mAddress,
            mCity,
            mCnic;

    String name,address,phone,cnic,city;

    DatabaseHandler databaseHandler;
    ProgressBar progressBar;
    MyVendorPreferences mvp;
    Button editButton;
    Vendor vendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit_profile);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        getSupportActionBar().setTitle("Edit Profile");
        editButton = findViewById(R.id.edit_btn);
        databaseHandler= new DatabaseHandler(this);
        mvp = new MyVendorPreferences(this);
        vendor = mvp.getVendor();

        mName = findViewById(R.id.tx_name);
        mPhone = findViewById(R.id.tx_phone);
        mAddress = findViewById(R.id.tx_address);
        mCnic = findViewById(R.id.tx_cnic);
        mCity = findViewById(R.id.tx_city);

        mName.getEditText().setText(vendor.getName());
        mPhone.getEditText().setText(vendor.getPhone());
        mCnic.getEditText().setText(vendor.getCnic());
        mAddress.getEditText().setText(vendor.getAddress());
        mCity.getEditText().setText(vendor.getCity());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(     !validName() |
                        !validPhone() |
                        !(validCnic(mCnic.getEditText().getText().toString())))
                {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                name = mName.getEditText().getText().toString();
                address = mAddress.getEditText().getText().toString();
                phone = mPhone.getEditText().getText().toString();
                cnic = mCnic.getEditText().getText().toString();
                city = mCity.getEditText().getText().toString();

                vendor.setName(name);
                vendor.setPhone(phone);
                vendor.setCnic(cnic);
                vendor.setAddress(address);
                vendor.setCity(city);
                vendor.setPassword(mvp.getVendor().getPassword());

                databaseHandler.getVendorsReference().child(vendor.getId()).setValue(vendor)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(VendorEditProfile.this, "EDITED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                    mvp.saveVendor(vendor);
                                    startActivity(new Intent(VendorEditProfile.this, VendorProfile.class));
                                }
                            }
                        });
            }
        });
    }

    private boolean validName()
    {
        String name= mName.getEditText().getText().toString().trim();
        if(name.isEmpty())
        {
            mName.setError("Name is required. Can't be empty");
            return false;
        }
        return true;
    }

    private boolean validPhone()
    {
        String phone= mPhone.getEditText().getText().toString().trim();
        if(phone.isEmpty())
        {
            mPhone.setError("Phone Number is required. Can't be empty");
            return false;
        }else if(phone.length()!=11)
        {
            mPhone.setError("Enter a valid 11 digit phone number");
            return false;
        }
        mPhone.setErrorEnabled(false);
        return true;
    }

    public boolean validCnic(String cnic){
        if(Utilities.isInteger(cnic)){
            if(cnic.length()==13){
                return true;
            }
            else{
                mCnic.getEditText().setError("length must be 13");
                return false;
            }
        }
        else{
            mCnic.getEditText().setError("Write integers only");
            return false;
        }
    }

    public void showCityPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Islamabad:
                    case R.id.Rawalpindi:
                        mCity.getEditText().setText(item.getTitle().toString());
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        popup.inflate(R.menu.city_popup_menu);
        popup.show();
    }
}