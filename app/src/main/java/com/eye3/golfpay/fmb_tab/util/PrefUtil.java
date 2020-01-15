package com.eye3.golfpay.fmb_tab.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {
    private static String PREF_NAME             = "kt_wmms";

//    private static String FLOATING_LOCATION_X   = "floating_location_x";
//    private static String FLOATING_LOCATION_Y   = "floating_location_y";

    private static String LOGIN_ID              = "login_id";
    private static String IS_CHECKED             = "is_checked";

    public static void setLoginId(Context context, String clicked) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(LOGIN_ID, clicked).apply();
    }

    public static String getLoginId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(LOGIN_ID,"");
    }

    public static void setCheckedSaved(Context context, Boolean isChecked) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(IS_CHECKED, isChecked).apply();
    }

    public static Boolean getCheckedSaved(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(IS_CHECKED, false);
    }


}
