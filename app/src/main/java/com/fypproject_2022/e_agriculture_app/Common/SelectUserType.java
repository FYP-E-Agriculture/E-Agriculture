package com.fypproject_2022.e_agriculture_app.Common;

import static com.fypproject_2022.e_agriculture_app.Common.Login.getAdminLoginData;
import static com.fypproject_2022.e_agriculture_app.Common.Login.getCustomerLoginData;
import static com.fypproject_2022.e_agriculture_app.Common.Login.getVendorLoginData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fypproject_2022.e_agriculture_app.Admin.Dashboard.DashboardAdmin;
import com.fypproject_2022.e_agriculture_app.Admin.MyAdminPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.Customer.Common.Register;
import com.fypproject_2022.e_agriculture_app.Customer.Dashboard.Dashboard;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.RegisterVendor;
import com.fypproject_2022.e_agriculture_app.Vendor.Dashboard.DashboardVendor;
import com.google.android.material.textfield.TextInputEditText;

public class SelectUserType extends AppCompatActivity {

    TextInputEditText userCategory;
    Button next;
    TextView register;
    MyAdminPreferences adminPreferences;
    MyVendorPreferences vendorPreferences;
    MyCustomerPreferences customerPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        userCategory= findViewById(R.id.user_category_input);
        next=findViewById(R.id.next);
        register=findViewById(R.id.register);

        adminPreferences= new MyAdminPreferences(this);
        vendorPreferences=new MyVendorPreferences(this);
        customerPreferences= new MyCustomerPreferences(this);

        if(adminPreferences.getLogin()) {
            Intent intent = new Intent(SelectUserType.this, DashboardAdmin.class);
            intent.putExtra(Utilities.intent_admin, adminPreferences.getAdmin());
            startActivity(intent);
            return;
        }
        if(vendorPreferences.getLogin()) {
            Intent intent = new Intent(SelectUserType.this, DashboardVendor.class);
            intent.putExtra(Utilities.intent_vendor, vendorPreferences.getVendor());
            startActivity(intent);
            return;
        }
        if(customerPreferences.getLogin()) {
            Intent intent = new Intent(SelectUserType.this, Dashboard.class);
            intent.putExtra(Utilities.intent_customer, customerPreferences.getCustomer());
            startActivity(intent);
            return;
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userCategory.getText().toString())
                {
                    case "Vendor":
                    {
                        startActivity(new Intent(SelectUserType.this, RegisterVendor.class));
                        break;
                    }
                    case "Customer":
                    {
                        startActivity(new Intent(SelectUserType.this, Register.class));
                        break;
                    }
                    case "Admin":
                    {
                        Toast.makeText(SelectUserType.this, "Cannot Register new Admin", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userCategory.getText().toString())
                {
                    case "Vendor":
                    {
                        if(vendorPreferences.getLogin()){
                            startActivity(new Intent(SelectUserType.this, DashboardVendor.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SelectUserType.this, Login.class);
                            intent.putExtra(Utilities.intent_user_category,userCategory.getText().toString());
                            startActivity(intent);
                        }
                        break;
                    }
                    case "Customer":
                    {
                        if(customerPreferences.getLogin()){
                            startActivity(new Intent(SelectUserType.this, Dashboard.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SelectUserType.this, Login.class);
                            intent.putExtra(Utilities.intent_user_category,userCategory.getText().toString());
                            startActivity(intent);
                        }
                        break;
                    }
                    case "Admin":
                    {
                        if(adminPreferences.getLogin()){
                            startActivity(new Intent(SelectUserType.this, Dashboard.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SelectUserType.this, Login.class);
                            intent.putExtra(Utilities.intent_user_category,userCategory.getText().toString());
                            startActivity(intent);
                        }
                        break;
                    }
                }
            }
        });
    }

    public void showUserCategories(View v){
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                userCategory.setText(item.getTitle().toString());
                return true;
            }
        });
        popup.inflate(R.menu.user_category_popup_menu);
        popup.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}