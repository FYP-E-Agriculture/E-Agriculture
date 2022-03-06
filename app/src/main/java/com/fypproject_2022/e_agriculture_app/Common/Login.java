package com.fypproject_2022.e_agriculture_app.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class Login extends AppCompatActivity {

    static Context context;

    Intent intent;
    String userType;

    TextInputEditText email;
    TextInputEditText password;

    Button login;
    TextView signUp;
    TextView signUpText;
    public static ProgressBar progressBar;

    DatabaseHandler databaseHandler;
    static MyCustomerPreferences mcp;
    static MyAdminPreferences map;
    static MyVendorPreferences mvp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        email=findViewById(R.id.emailET);
        password=findViewById(R.id.passwordET);
        login=findViewById(R.id.loginBTN);
        signUp=findViewById(R.id.signUpText);
        signUpText=findViewById(R.id.signUpText1);

        context=this;
        intent=getIntent();
        userType=intent.getStringExtra(Utilities.intent_user_category);
        databaseHandler= new DatabaseHandler(this);
        mcp= new MyCustomerPreferences(this);
        map= new MyAdminPreferences(this);
        mvp = new MyVendorPreferences(this);


        if(userType.equals("Admin")){
            signUpText.setVisibility(View.INVISIBLE);
            signUp.setVisibility(View.INVISIBLE);
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (userType)
                {
                    case "Customer":
                    {
                        startActivity(new Intent(Login.this, Register.class));
                        break;
                    }
                    case "Vendor":
                    {
                        startActivity(new Intent(Login.this, RegisterVendor.class));
                        break;
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    switch (userType) {
                        case "Admin": {
                            databaseHandler.loginAdmin(email.getText().toString(), password.getText().toString());
                            break;
                        }
                        case "Customer": {
                            databaseHandler.loginCustomer(email.getText().toString(), password.getText().toString());

                            break;
                        }
                        case "Vendor": {
                            databaseHandler.loginVendor(email.getText().toString(), password.getText().toString());

                            break;
                        }
                    }
                }
                else{
                    Toast.makeText(Login.this, "PLEASE FILL ALL FIELDS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void getCustomerLoginData(){
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(context, Dashboard.class);
        intent.putExtra(Utilities.intent_customer, mcp.getCustomer());
        context.startActivity(intent);
        if (MyCustomerPreferences.getLogin()){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public static void getVendorLoginData(){
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(context, DashboardVendor.class);
        intent.putExtra(Utilities.intent_vendor, mvp.getVendor());
        context.startActivity(intent);
        if (MyVendorPreferences.getLogin()){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    public static void getAdminLoginData(){
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(context, DashboardAdmin.class);
        intent.putExtra(Utilities.intent_admin, map.getAdmin());
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, SelectUserType.class));
    }
}