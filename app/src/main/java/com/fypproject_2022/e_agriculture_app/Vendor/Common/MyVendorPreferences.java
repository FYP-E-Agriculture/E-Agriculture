package com.fypproject_2022.e_agriculture_app.Vendor.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Vendor;
import com.fypproject_2022.e_agriculture_app.R;

public class MyVendorPreferences {

    static Context context;
    static SharedPreferences vendorPreferences;
    static SharedPreferences.Editor vendorEditor;

    static String undefined="Undefined";

    public MyVendorPreferences(Context context) {
        this.context = context;
        this.vendorPreferences= context.getSharedPreferences(Utilities.vendor_preferences, Context.MODE_PRIVATE);
        this.vendorEditor= vendorPreferences.edit();
    }

    public static void setLogin(boolean login){

        vendorEditor.putBoolean(Utilities.login_utility,login);
        vendorEditor.commit();
    }

    public static boolean getLogin(){
        return vendorPreferences.getBoolean("LOGIN",false);
    }

    public static void saveVendor(Vendor vendor) {

        if (!(vendor == null)) {
            vendorEditor.putString(Utilities.key_id, vendor.getId());
            vendorEditor.putString(Utilities.key_name, vendor.getName());
            vendorEditor.putString(Utilities.key_username, vendor.getUsername());
            vendorEditor.putString(Utilities.key_email, vendor.getEmail());
            vendorEditor.putString(Utilities.key_address, vendor.getAddress());
            vendorEditor.putString(Utilities.key_phone, vendor.getPhone());
            vendorEditor.putString(Utilities.key_cnic, vendor.getCnic());
            vendorEditor.putString(Utilities.key_image, vendor.getImage());
            vendorEditor.putString(Utilities.key_password, vendor.getPassword());

            setLogin(true);

            vendorEditor.commit();
        } else {
            Utilities.createAlertDialog(context, Utilities.save_vendor_failed_utility, Utilities.null_object_utility);
        }
    }
    public static Vendor getVendor(){

        Vendor vendor = new Vendor();
        vendor.setId(vendorPreferences.getString(Utilities.key_id, undefined ));
        vendor.setName(vendorPreferences.getString(Utilities.key_name, undefined));
        vendor.setUsername(vendorPreferences.getString(Utilities.key_username,undefined));
        vendor.setEmail(vendorPreferences.getString(Utilities.key_email, undefined));
        vendor.setAddress(vendorPreferences.getString(Utilities.key_address, undefined));
        vendor.setPhone(vendorPreferences.getString(Utilities.key_phone, undefined));
        vendor.setCnic(vendorPreferences.getString(Utilities.key_cnic, undefined));
        vendor.setImage(vendorPreferences.getString(Utilities.key_image, undefined));

        return vendor;
    }
}
