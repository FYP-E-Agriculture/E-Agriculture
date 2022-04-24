package com.fypproject_2022.e_agriculture_app.Common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;

import com.fypproject_2022.e_agriculture_app.Customer.Common.MyCustomerPreferences;
import com.fypproject_2022.e_agriculture_app.R;
import com.fypproject_2022.e_agriculture_app.Vendor.Common.MyVendorPreferences;
import com.fypproject_2022.e_agriculture_app.Vendor.Dashboard.DashboardVendor;

import java.util.regex.Pattern;

public class Utilities {

    /////--------------------DATE-TIME-FORMAT---------------------/////
    public static final String DATE_TIME_FORMAT="dd-MM-yyyy";

    /////--------------------SHARED PREFERENCES------------------////////
    public static final String admin_preferences = "ADMIN_PREFERENCES";
    public static final String vendor_preferences = "VENDOR_PREFERENCES";
    public static final String customer_preferences = "CUSTOMER_PREFERENCES";


    /////--------------------INTENTS------------------////////
    public static final String intent_product = "PRODUCT_OBJECT";
    public static final String intent_order = "ORDER_OBJECT";
    public static final String intent_admin = "ADMIN_OBJECT";
    public static final String intent_vendor = "VENDOR_OBJECT";
    public static final String intent_customer = "CUSTOMER_OBJECT";
    public static final String intent_store = "STORE_OBJECT";
    public static final String intent_product_category = "PRODUCT_CATEGORY";
    public static final String intent_user_category = "USER_CATEGORY";


    /////--------------------USERS------------------////////
    public static final String user_customer = "Customer";
    public static final String user_vendor = "Vendor";
    public static final String user_admin = "Admin";


    /////--------------------KEYS------------------////////
    public static final String key_id = "ID";
    public static final String key_name = "NAME";
    public static final String key_username = "USERNAME";
    public static final String key_email = "EMAIL";
    public static final String key_phone = "PHONE";
    public static final String key_password = "PASSWORD";
    public static final String key_city = "CITY";
    public static final String key_image = "IMAGE";
    public static final String key_cnic = "CNIC";
    public static final String key_address = "ADDRESS";



    /////--------------------ORDER STATUS------------------////////
    public static final String order_complete = "Complete";
    public static final String order_pending = "Pending";
    public static final String order_dispatched = "Dispatched";
    public static final String order_new = "New";

    /////--------------------UTILITY STRINGS------------------////////
    public static final String LOGIN = "LOGIN";
    public static final String login_failed_utility = "LOGIN FAILED";
    public static final String customers_failed_utility = "GET CUSTOMERS FAILED";
    public static final String vendors_failed_utility = "GET VENDORS FAILED";
    public static final String products_failed_utility = "GET PRODUCTS FAILED";
    public static final String reviews_failed_utility = "GET REVIEWS FAILED";
    public static final String save_customer_failed_utility = "SAVE CUSTOMER FAILED";
    public static final String save_vendor_failed_utility = "SAVE VENDOR FAILED";
    public static final String null_object_utility = "Null Object Passed";
    public static final String pending_utility = "Pending";
    public static final String complete_utility = "Complete";

    /////////------------------PRODUCT CATEGORIES---------------------///////////
    public static final String FERTILIZER="Fertilizer";
    public static final String INSECT_KILLER="Insect Killer";

    public static void createAlertDialog(Context context, String title, String text){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void createDismissableAlertDialog(Context context, String title, String text){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void createOrderReceivedConfirmationDialog(Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Order Confirmation")
                .setMessage("Confirm Order Receival?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void createLogoutCustomerDialog(Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Logout")
                .setMessage("Continue logging out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        MyCustomerPreferences.setLogin(false);
                        Intent intent = new Intent(context, SelectUserType.class);
                        intent.putExtra(Utilities.intent_user_category, Utilities.user_customer);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void createLogoutVendorDialog(Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Logout")
                .setMessage("Continue logging out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        MyVendorPreferences.setLogin(false);
                        Intent intent = new Intent(context, SelectUserType.class);
                        intent.putExtra(Utilities.intent_user_category, Utilities.user_vendor);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static final Pattern PASSWORD_PATTERN=
            Pattern.compile("^"
                    +"(?=.*[0-9])"          //minimum 0ne number
                    +"(?=.*[a-z])"          //minimum 0ne lower-case character
                    +"(?=.*[A-Z])"          //minimum 0ne Upper-case character
                    +"(?=.*[a-zA-Z])"       //any character
                    +"(?=\\S+$)"            //no white spaces
                    +".{8,}"                //minimum length 8 characters
                    +"$");

    public static final Pattern PASSWORD_UPPERCASE_PATTERN=Pattern.compile("(?=.*[A-Z])"+".{1,}");
    public static final Pattern PASSWORD_LOWERCASE_PATTERN=Pattern.compile("(?=.*[a-z])"+".{1,}");
    public static final Pattern PASSWORD_NUMBER_PATTERN=Pattern.compile("(?=.*[0-9])"+".{1,}");
    public static final Pattern PASSWORD_SPECIALCHARACTER_PATTERN=Pattern.compile("(?=.*[@#$%^&+=])"+".{1,}");


    public static boolean isInteger(String str){
        if(str.matches("-?\\d+")){
            return true;
        }
        return false;
    }
}
