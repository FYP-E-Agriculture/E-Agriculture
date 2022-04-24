package com.fypproject_2022.e_agriculture_app.Customer.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Customer;

public class MyCustomerPreferences {

    static Context context;
    static SharedPreferences customerPreferences;
    static SharedPreferences.Editor customerEditor;

    static String undefined="Undefined";

    public MyCustomerPreferences(Context context) {
        this.context=context;
        this.customerPreferences= context.getSharedPreferences(Utilities.customer_preferences, Context.MODE_PRIVATE);
        this.customerEditor= customerPreferences.edit();
    }

    public static void setLogin(boolean login){

        customerEditor.putBoolean(Utilities.LOGIN,login);
        customerEditor.commit();
    }

    public static boolean getLogin(){
        return customerPreferences.getBoolean("LOGIN",false);
    }


    public static void saveCustomer(Customer customer){

        if(!(customer ==null)) {

            customerEditor.putString(Utilities.key_id, customer.getId());
            customerEditor.putString(Utilities.key_name, customer.getName());
            customerEditor.putString(Utilities.key_email, customer.getEmail());
            customerEditor.putString(Utilities.key_address, customer.getAddress());
            customerEditor.putString(Utilities.key_phone, customer.getPhone());
            customerEditor.putString(Utilities.key_cnic, customer.getCnic());
            customerEditor.putString(Utilities.key_image, customer.getImage());
            customerEditor.putString(Utilities.key_password, customer.getPassword());
            customerEditor.putString(Utilities.key_city, customer.getCity());

            setLogin(true);

            customerEditor.commit();
        }
        else{
            Utilities.createAlertDialog(context, Utilities.save_customer_failed_utility,Utilities.null_object_utility);
        }
    }
    public static Customer getCustomer(){

        Customer customer = new Customer();
        customer.setId(customerPreferences.getString(Utilities.key_id, undefined ));
        customer.setName(customerPreferences.getString(Utilities.key_name, undefined));
        customer.setEmail(customerPreferences.getString(Utilities.key_email, undefined));
        customer.setAddress(customerPreferences.getString(Utilities.key_address, undefined));
        customer.setPhone(customerPreferences.getString(Utilities.key_phone, undefined));
        customer.setCnic(customerPreferences.getString(Utilities.key_cnic, undefined));
        customer.setImage(customerPreferences.getString(Utilities.key_image, undefined));
        customer.setCity(customerPreferences.getString(Utilities.key_city, undefined));
        customer.setPassword(customerPreferences.getString(Utilities.key_password, undefined));

        return customer;
    }
}
