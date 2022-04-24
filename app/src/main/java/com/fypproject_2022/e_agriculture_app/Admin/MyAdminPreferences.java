package com.fypproject_2022.e_agriculture_app.Admin;

import static com.fypproject_2022.e_agriculture_app.Common.Utilities.key_image;

import android.content.Context;
import android.content.SharedPreferences;

import com.fypproject_2022.e_agriculture_app.Common.Utilities;
import com.fypproject_2022.e_agriculture_app.Models.Admin;

public class MyAdminPreferences {
    static Context context;
    static SharedPreferences adminPreferences;
    static SharedPreferences.Editor adminEditor;

    static String undefined="Undefined";

    public MyAdminPreferences(Context context) {
        this.context=context;
        this.adminPreferences = context.getSharedPreferences(Utilities.admin_preferences, Context.MODE_PRIVATE);
        this.adminEditor = adminPreferences.edit();
    }

    public static void setLogin(boolean login){

        adminEditor.putBoolean(Utilities.LOGIN,login);
        adminEditor.commit();
    }

    public static boolean getLogin(){
        return adminPreferences.getBoolean("LOGIN",false);
    }


    public static void saveAdmin(Admin admin){

        if(!(admin ==null)) {
            adminEditor.putString(Utilities.key_username, admin.getUsername());
            adminEditor.putString(Utilities.key_name, admin.getName());
            adminEditor.putString(Utilities.key_email, admin.getEmail());
            adminEditor.putString(Utilities.key_password, admin.getPassword());
            adminEditor.putString(key_image, admin.getImage());

            setLogin(true);

            adminEditor.commit();
        }
        else{
            Utilities.createAlertDialog(context, Utilities.save_customer_failed_utility,Utilities.null_object_utility);
        }
    }
    public static Admin getAdmin(){

        Admin admin = new Admin();
        admin.setUsername(adminPreferences.getString(Utilities.key_username,undefined));
        admin.setName(adminPreferences.getString(Utilities.key_name,undefined));
        admin.setEmail(adminPreferences.getString(Utilities.key_email, undefined));
        admin.setPassword(adminPreferences.getString(Utilities.key_password, undefined));
        admin.setImage(adminPreferences.getString(key_image, undefined));

        return admin;
    }
}
