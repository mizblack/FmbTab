package com.eye3.golfpay.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Debug;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.eye3.golfpay.R;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;


public class Util {
    private static SecretKey key;

    public static boolean isHostSelectMode() {
        return false;
    }

    public  static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("MapActivity", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }


    public static String get(String key) {
        try {
            @SuppressLint("PrivateApi")
            Class clazz = Class.forName("android.os.SystemProperties");
            if (clazz == null) {
                return "";
            }

            Method method = clazz.getDeclaredMethod("get", String.class);
            if (method == null) {
                return "";
            }

            return (String) method.invoke(null, key);

        } catch (Exception e) {
            //IMLog.e("WHOOPS", "Exception during reflection: " + e.getMessage());
        }

        return "";
    }


//    public static void callCompany(Activity activity) {
//
//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Global.COMPANY_PHONENO));
//        activity.startActivity(intent);
//        activity.finish();
//    }


    /**
     * Soft Keypad 보이기
     *
     * @param view Current View
     */
    public static void showSoftKey(View view, Activity act) {
        if (view == null) {
            view = act.getCurrentFocus();
        }
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Soft Keypad 가리기
     *
     * @param view Current View
     */
    public static void hideSoftKey(View view, Activity act) {
        if (view == null) {
            view = act.getCurrentFocus();
        }
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * GetDeviceUUID
     *
     * @param context Context
     * @return String
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String GetDeviceUUID(Context context) {

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

//        if (Build.VERSION.SDK_INT < 26)
//            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//        else{
//            Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
//            String params[] = {"android_id"};
//            Cursor c = context.getContentResolver().query(URI, null, null, params, null);
//            if(c != null){
//                if(!c.moveToFirst() || c.getColumnCount() < 2){
//                    if(!c.isClosed())
//                        c.close();
//                    androidId = "";
//                }
//                else{
//                    androidId = c.getString(1);
//                }
//            }
//            else
//                androidId = "";
//        }

//        String gserviceID = "";
//        Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
//        String params[] = {"android_id"};
//        Cursor c = context.getContentResolver().query(URI, null, null, params, null);
//        if(c != null){
//            if(!c.moveToFirst() || c.getColumnCount() < 2){
//                if(!c.isClosed())
//                    c.close();
//                gserviceID = "";
//            }
//            else{
//                gserviceID = c.getString(1);
//            }
//        }
//        else
//            gserviceID = "";

//        //IMLog.d("androidId ANDROID_ID["+android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID)+"]");
//        //IMLog.d("androidId gservicesID["+gserviceID+"]");
//
//        //IMLog.d("androidId gservices["+androidId+"]");
//        //IMLog.d("androidId tmDevice["+tmDevice+"]");
//        //IMLog.d("androidId tmSerial["+tmSerial+"]");

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        //IMLog.d("androidId deviceId[" + deviceId + "]");

//        //IMLog.d("androidId ro.build.date["+ Util.get("ro.build.date")+"]");

        return deviceId;
    }

    private static final String DATE_FORMAT_PRIMARY = "yyyy-MM-dd HH:mm";

    public static String getDateFormat(Long milliSeconds) {
        if (milliSeconds == null || milliSeconds == 0) {
            return "";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PRIMARY, Locale.getDefault());
         formatter.getCalendar().get(Calendar.YEAR);
        return formatter.format(new Date(milliSeconds));
    }

    public static SimpleDateFormat timelogFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US);

    public static void timeLog(String TAG, long time) {
        String message = String.format("TimeCheck %40s [%s]", TAG, timelogFormat.format(time));
        //IMLog.d("TimeCheck", message);
    }

    public static String getDatePrintable(String date) {
        if (TextUtils.isEmpty(date) || date.length() < 8) {
            return date;
        }

        String yyyy = date.substring(0, 4);
        String mm = date.substring(4, 6);
        String dd = date.substring(6, 8);

        // modify by lvforyou : 2018.09.10 - 시간 헝식 추가
        String hh = "", mm1 = "";
        if (date.length() == 12) {
            hh = date.substring(8, 10);
            mm1 = date.substring(10, 12);
            return yyyy + "-" + mm + "-" + dd + " " + hh + ":" + mm1;
        }

        return yyyy + "-" + mm + "-" + dd;
    }

    public static void resetListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalItemsHeight = 0;

        int count = listAdapter.getCount();
        for (int itemPos = 0; itemPos < count; itemPos++) {
            View item = listAdapter.getView(itemPos, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        int totalDividersHeight = 0;// listView.getDividerHeight() *  (numberOfItems - 1);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String PrintLogJson(String jsonString) {
        String result;
        try {
            result = URLDecoder.decode(new JSONObject(jsonString).toString(4), "utf-8");
        } catch (Exception e) {
            result = jsonString;
        }
        //IMLog.d("JSON String = " + result);
        return result;
    }


    /**
     * 모든 쿠키 정보들을 Vector 개체로 변환하여 반환
     *
     * @param cookies 쿠키 목록
     * @return Vector 개체
     */
    private static Vector<String> getCookieAllKeysByCookieString(String cookies) {
        if (TextUtils.isEmpty(cookies)) {
            return null;
        }

        String[] cookieField = cookies.split(";");
        int len = cookieField.length;
        for (int i = 0; i < len; i++) {
            cookieField[i] = cookieField[i].trim();
        }

        Vector<String> allCookieField = new Vector<String>();
        for (String cookie : cookieField) {
            if (TextUtils.isEmpty(cookie) || !cookie.contains("=")) {
                continue;
            }
            String[] singleCookieField = cookie.split("=");
            allCookieField.add(singleCookieField[0]);
        }

        if (allCookieField.isEmpty()) {
            return null;
        } else {
            return allCookieField;
        }
    }


    public static String XmlStringParser(Context context, String data, String tag) {
        String value = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(data));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("pub")) {
                        value = xpp.nextText();
                    }
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    public static String getMACAddress(Context context) {
        try {
            WifiManager mng = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = mng.getConnectionInfo();
            return info.getMacAddress(); // 맥주소
        } catch (Exception e) {
            return "";
        }
    }


    // UUID OREO
    @SuppressLint("MissingPermission")
    public static String getGoogleUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        androidId = getGsfAndroidId(context);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        return deviceUuid.toString();
    }

    public static String getGsfAndroidId(Context context) {
        Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
        String ID_KEY = "android_id";
        String params[] = {ID_KEY};
        Cursor c = context.getContentResolver().query(URI, null, null, params, null);
        if (c != null && (!c.moveToFirst() || c.getColumnCount() < 2)) {
            if (!c.isClosed()) {
                c.close();
            }
            return "";
        }

        try {
            if (c != null) {
                String result = Long.toHexString(Long.parseLong(c.getString(1)));
                if (!c.isClosed()) {
                    c.close();
                }
                return result;
            } else {
                return "";
            }
        } catch (NumberFormatException e) {
            if (!c.isClosed()) {
                c.close();
            }
            return "";
        }
    }

    /**
     * 통화 형태로 출력 형식 변경
     *
     * @param amount 금액
     * @return 통화 출력 형식
     */
    public static String getCurrencyString(String amount) {
        // insert by lvforyou : 2018.09.11 - 빈문자열 처리 추가
        if (amount != null && amount.trim().length() == 0)
            return "0";

        try {
            return getCurrencyString(Long.parseLong(amount.replaceAll("\\D", "")));
        } catch (Exception e) {
            e.printStackTrace();
            return amount;
        }
    }


    /**
     * 통화 형태로 출력 형식 변경
     *
     * @param amount 금액
     * @return 통화 출력 형식
     */
    public static String getCurrencyString(Long amount) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(amount);
    }

    /**
     * 금액 문자열을 숫자형 금액으로 변환하여 반환
     *
     * @param amount 문자열 금액
     * @return 숫자 금액
     */
    public static long getCurrencyValue(String amount) {
        if (!TextUtils.isEmpty(amount)) {
            try {
                return Long.parseLong(amount.replaceAll("\\D", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }

    /**
     * 금액을 한글로 변경
     *
     * @param amount 금액
     * @return String 한글
     */
    public static String convertHangul(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return "";
        }

        String value = amount.replace("\\D", "");

        String[] han1 = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
        String[] han2 = {"", "십", "백", "천"};
        String[] han3 = {"", "만", "억", "조", "경"};

        StringBuilder result = new StringBuilder();

        int len = value.length();
        for (int i = len - 1; i >= 0; i--) {
            result.append(han1[Integer.parseInt(value.substring(len - i - 1, len - i))]);
            if (Integer.parseInt(value.substring(len - i - 1, len - i)) > 0) {
                result.append(han2[i % 4]);
            }
            if (i % 4 == 0) {
                result.append(han3[i / 4]);
            }
        }

        return result.toString();
    }

    /**
     * 전달된 Parameter 를 Decoding 하여 전달
     *
     * @param value Parameter
     * @return String
     */
    public static String getUrlDecode(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }

        String result = "";
        try {
            result = URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //IMLog.e("NativeActivity", "getUrlDecoding:UnsupportedEncodingException - " + e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * 전달된 Parameter 를 Encoding 하여 전달
     *
     * @param value Parameter
     * @return String
     */
    public static String getUrlEncode(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }

        String result = "";
        try {
            result = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //IMLog.e("NativeActivity", "getUrlEneode:UnsupportedEncodingException - " + e.getLocalizedMessage());
        }

        return result;
    }

    /**
     * 다른 색상을 가진 SpannableString 문자열을 만들어서 반환
     *
     * @param message 전체 메시지
     * @param value   색상이 다른 메시지 문구
     * @param color   색상
     * @param isAll   전체 문구에서 검색
     * @return SpannableString 문자열
     */
    public static SpannableString getColorSpannableString(String message, String value, int color, boolean isAll) {
        return getColorSpannableString(new SpannableString(message), value, color, isAll);
    }

    /**
     * 다른 색상을 가진 SpannableString 문자열을 만들어서 반환
     *
     * @param message 전체 메시지
     * @param value   색상이 다른 메시지 문구
     * @param color   색상
     * @param isAll   전체 문구에서 검색
     * @return SpannableString 문자열
     */
    public static SpannableString getColorSpannableString(SpannableString message, String value, int color, boolean isAll) {
        try {
            int offset = 0;
            do {
                int startIndex, endIndex;
                startIndex = message.toString().indexOf(value, offset);
                if (startIndex >= 0) {
                    endIndex = startIndex + value.length();
                    message.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    offset = endIndex;
                } else {
                    isAll = false;
                }
            } while (isAll);

        } catch (Exception e) {
            //IMLog.d("UTIL", "getSpannableString " + e.getLocalizedMessage());
        }
        return message;
    }

    //greenSpannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,input.length(), 0);

    /**
     * 다른 폰트사이즈를 가진 SpannableString 문자열을 만들어서 반환
     *
     * @param message  전체 메시지
     * @param value    색상이 다른 메시지 문구
     * @param fontSize 폰트사이즈
     * @return SpannableString 문자열
     */
    public static SpannableString getFontSizeSpannableString(String message, String value, int fontSize) {
        SpannableString spannableString = new SpannableString(message);
        try {
            int startIndex, endIndex;
            startIndex = message.indexOf(value);
            endIndex = startIndex + value.length();

            spannableString.setSpan(new AbsoluteSizeSpan(fontSize, true), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            //IMLog.d("UTIL", "getSpannableString " + e.getLocalizedMessage());
        }
        return spannableString;
    }

    /**
     * 다른 폰트가진 SpannableString 문자열을 만들어서 반환
     *
     * @param message 전체 메시지
     * @param value   색상이 다른 메시지 문구
     * @param font    폰트
     * @return SpannableString 문자열
     */
    public static SpannableString setCustomFontTypeSpan(Context context, String message, String value, int font) {
        SpannableString spannableString = new SpannableString(message);
        try {
            int startIndex, endIndex;
            startIndex = message.indexOf(value);
            endIndex = startIndex + value.length();

            Typeface typeface = ResourcesCompat.getFont(context, font);
            spannableString.setSpan(new StyleSpan(typeface.getStyle()), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            //IMLog.d("UTIL", "getSpannableString " + e.getLocalizedMessage());
        }
        return spannableString;
    }

    /**
     * Json 짤린 문자열 Log 출력
     *
     * @param str 전체 메시지
     */
    public static void logLargeString(String str) {
        if (str.length() > 3000) {
            //IMLog.d("temp", "logLargeString : " + str.substring(0, 3000));
            //Log.i("e", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            //IMLog.d("temp", "logLargeString : " + str);
            //Log.i("e", str); // continuation
        }
    }

    /**
     * Utf8 문자열을 만들어서 반환
     *
     * @param str 전체 메시지
     * @return utf8문자열
     */
    public static String decodeUtf8(String str) {
        String sReturn = "";
        if (str == null || str.equals("")) {
            return sReturn;
        }

        sReturn = str;
        try {
            sReturn = URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return sReturn;
        }

        return sReturn;
    }


    /**
     * 파라미터로 넘어온 문자열에 대하여 메모리상에 0x00 마스킹
     *
     * @param data 문자열
     */
    public static void clearString(String data) {
//        if (data == null) {
//            return;
//        }
//
//        Field sv;
//        try {
//            sv = String.class.getDeclaredField("value");
//            sv.setAccessible(true);
//
//            char value[] = (char[]) sv.get(data);
//            for (int i = 0; i < data.length(); i++) {
//                value[i] = '0';
//            }
//        } catch (NoSuchFieldException e) {
////            e.printStackTrace();
//        } catch (IllegalAccessException e) {
////            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
////            e.printStackTrace();
//        }
    }


    public static void clearString_sumbank(String data) {
        if (data != null && data.length() != 0) {
            try {
                Field sv = String.class.getDeclaredField("value");
                sv.setAccessible(true);
                char[] e = (char[]) sv.get(data);

                for (int i = 0; i < data.length(); ++i) {
                    e[i] = 48;
                }
            } catch (NoSuchFieldException var4) {
//                var4.printStackTrace();
            } catch (IllegalAccessException var5) {
//                var5.printStackTrace();
            } catch (IllegalArgumentException var6) {
//                var6.printStackTrace();
            }
        }
    }

    public static void clearByte(byte[] data) {
        if (data == null) {
            return;
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = 0x00;
        }
    }

    public static int chkPermission(Context ctx, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ctx.checkSelfPermission(permission);
        } else {
            return PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * 전달된 첫번째 Parameter가 null 이면 두번째  Parameter 전달
     *
     * @param parm1 Parameter
     * @param parm2 Parameter
     * @return String
     */
    public static String getSecondWord(String parm1, String parm2) {

        if (TextUtils.isEmpty(parm1) && TextUtils.isEmpty(parm2)) {
            return "";
        }

        String result = parm1;
        if (TextUtils.isEmpty(parm1) || parm1.equals("null")) {
            result = parm2;
        }

        return result;
    }


    public static String getNetworkOperForUsimInfo(Context con) {
        String retOperation;
        String company;
        TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
        company = tm.getSimOperator();
        if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
            retOperation = "NONE";
            return retOperation;
        }

        if (company == null || company.length() < 0) {
            retOperation = "NONE";
            return retOperation;
        }

        if (company.equals("45002") || company.equals("45004") || company.equals("45008")) {
            retOperation = "KT";
        } else if (company.equals("45003") || company.equals("45005") || company.equals("45011")) {
            retOperation = "SKT";
        } else if (company.equals("45006")) {
            retOperation = "LGT";
        } else {
            retOperation = "NONE";
        }

        return retOperation;
    }

    @SuppressLint("MissingPermission")
    public static String getSimSerial(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    public static String getAgencyCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = "0";
        String networkOperator = tm.getNetworkOperatorName();
        //IMLog.d("operator : " + networkOperator);
        if (tm.getNetworkOperatorName() == null) {
            operator = "0";
        } else if (tm.getNetworkOperatorName().toUpperCase().startsWith("K") || tm.getNetworkOperatorName().toUpperCase().startsWith("olleh")) {
            operator = "2";
        } else if (tm.getNetworkOperatorName().toUpperCase().startsWith("S")) {
            operator = "1";
        } else if (tm.getNetworkOperatorName().toUpperCase().startsWith("L")) {
            operator = "3";
        }
        return operator;
    }

//    public static String getLineNumber(Context context) {
//        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
//        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (tm == null || tm.getLine1Number() == null || tm.getLine1Number().isEmpty()) {
////                if (!AppDef.RealSetting)
////                    return AppDef.TEST_PHONE;
////                else
//
//                //IMLog.d("전화번호 추출 오류");
//                return "";
//            } else {
//                String line = tm.getLine1Number().replace("+82", "0");
//                //IMLog.d("line = " + line);
//                return line;
//            }
//        } else {
//            return "";
//        }
//    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        if (file == null) return null;

        long length = file.length();
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        InputStream is = null;

        try {
            is = new FileInputStream(file);

            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
            // Close the input stream and return bytes
        } catch (IOException e) {
            throw new IOException("Could not completely read file " + file.getName());
        } catch (Exception e) {
            throw new IOException("Could not completely read file " + file.getName());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                throw new IOException("Could not close " + file.getName());
            }
        }
        return bytes;
    }


    /**
     * 루팅 체크 관련
     *
     * @return
     */
    public static boolean rootCheck() {
        String[] arrayOfString = {"/sbin/su", "/system/su", "/system/sbin/su", "/system/xbin/su", "/data/data/com.noshufou.android.su", "/system/app/Superuser.apk"};

        int i = 0;
        while (true) {
            if (i >= arrayOfString.length) {
                return false;
            }
            if (new File(arrayOfString[i]).exists()) {
                return true;
            }
            i++;
        }
    }

    public static boolean checkFile() {
        String[] arrayOfString = {"/system/bin/.ext", "/system/xbin/.ext"};

        int i = 0;
        while (true) {
            if (i >= arrayOfString.length) {
                return false;
            }
            if (new File(arrayOfString[i]).exists()) {
                return true;
            }
            i++;
        }
    }

    public static boolean execCmd() {
        boolean flag = false;
        try {
            Runtime.getRuntime().exec("su");
            flag = true;
        } catch (Exception e) {
            flag = false;
        }

        return flag;
    }

    /**
     * 다른 시스템에서 디버그 여부 체크
     *
     * @return
     */
    public static boolean checkDebug() {
        if (Debug.isDebuggerConnected()) {
            return true;
        }
        return false;
    }


    private static String getTime(String type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type, Locale.PRC);
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    public static boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    /**
     * 해당 날짜의 요일 반환
     * 1:수요일, 2:목요일, 3:금요일, 4:토요일, 5:일요일, 6:월요일, 7:화요일
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int getDayOfTheWeek(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);

        return cal.get(Calendar.DAY_OF_WEEK);
    }


    public static String getVersion(Context context) {
        String version = "";
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
            //IMLog.d(SumPerfValue.MAIN_TAG, "버젼 : " + version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 숫자로 된 문자 열인지 체크
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        boolean result = false;
        try {
            Double.parseDouble(str);
            result = true;
        } catch (Exception e) {
        }
        return result;
    }



    public static boolean isIntegerNumber(String str)  {
        boolean result = false;
        try {
            Integer.parseInt(str);
            result = true;
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    public static boolean isInteger(String s)  {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /**
     * null체크
     *
     * @param pStr
     * @return
     */
    public static boolean isNUll(String pStr) {
        if (pStr == null || "".equals(pStr) || "null".equals(pStr) || pStr.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * insert by lvforyou : 2018.09.07 - 카드 관련 함수 추가
     */
    public static String makeCardValidDate(String dateData) {
        if (dateData == null)
            return "";

        if (dateData.length() == 6) {
            return dateData.substring(0, 4) + "-" + dateData.substring(4, 6);
        } else if (dateData.length() == 4) {
            return dateData.substring(0, 2) + "/" + dateData.substring(2, 4);
        }

        return dateData;
    }


    /**
     * 앱이 설치 되어있는지 확인
     *
     * @param packageName
     * @param context
     * @return
     */
    public static boolean isAppInstalled(String packageName, Context context) {

        boolean result = false;

        PackageInfo pi = null;
        PackageManager pm = context.getPackageManager();
        try {
            pi = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            result = true;
        } catch (PackageManager.NameNotFoundException e) {
            result = false;
        }

        return result;
    }


    // param 스트링으로 변경
    public static String convertParam(Map params) {
        String sReturn = "";
        if (params == null) return sReturn;

        Iterator<String> keys = params.keySet().iterator();
        int i = 0;
        while (keys.hasNext()) {
            String key = keys.next();
            if (i > 0) {
                sReturn = sReturn + "&" + key + "=" + params.get(key).toString();
            } else {
                sReturn = key + "=" + params.get(key).toString();
            }
            i++;
        }

        return sReturn;
    }


//    /**
//     * 코드르바 호출
//     */
//    public static String goWebUrl(Context pContext, int pCd, String pMenuId, Map params) {
//        String sReturn = "";
//        if (pMenuId == null || "".equals(pMenuId)) return sReturn;
//
//        String sLinkUrl="";
//        if(pCd==CALLWEB) {                                                                          //웹뷰호출
//            sLinkUrl = SiteMenuParser.getInstance().getLinked(pMenuId);
//            if (sLinkUrl == null || "".equals(sLinkUrl)) return sReturn;
//            sReturn = NetConfig.HostAddress + NetAction.PATH_WEBPREFIX + sLinkUrl;
//        }else{                                                                                      //내부 HTML호출
//            sReturn = IMResourceSync.instance().getInternalPath(pContext, NetAction.WEB_PATH_PREFIX + pMenuId);
//        }
//
//        if (params != null) {
//            if (params.size() > 0)
//                sReturn = sReturn + "?" + convertParam(params);
//        }
//
//        if (sReturn.contains("//")) {
//            sReturn = sReturn.replaceAll("//", "/");
//        }
//
//        return sReturn;
//    }

//    public static ServiceMenu getServiceMenu(String menuId) {
//        return SiteMenuParser.getInstance().getMenu(SiteMenuParser.getInstance().getMenuList().subMenuList, menuId);
//    }

    /**
     * 금액 콤마 제거
     *
     * @param amount 문자열 금액
     * @return 숫자 금액
     */
    public static String getCurrencyNoComma(String amount) {
        if (!TextUtils.isEmpty(amount)) {
            try {
                return amount.replaceAll("\\D", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 스트링 널값 처리
     *
     * @param value
     * @return
     */
    public static String getNullConvert(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }

        return value;
    }

    /**
     * 주말 (토,일)
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean isWeekend(int year, int month, int day) {
        boolean result = false;

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        //SUNDAY:1 SATURDAY:7
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            result = true;
        }

        return result;
    }


//    /**
//     * 언어를 설정한다.
//     *
//     * @param context context
//     * @param langCode 변경할 언어코드
//     */
//    public static void changeLanguage(Context context, String langCode) {
//
//        // made by lvforyou : 2018.10.10 - 다국어 처리.
//        if (langCode == null || langCode.length() == 0)
//            return;
//
//        Resources res = context.getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        android.content.res.Configuration conf = res.getConfiguration();
//        if(langCode.equalsIgnoreCase(AppDef.LANGUAGE_ZH))
//            conf.locale = Locale.CHINA;
//        else
//            conf.locale = new Locale(langCode);
//        res.updateConfiguration(conf, dm);
//    }

    public static final Locale getCurrentLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    /**
     * 시스템언어가 한국어인지 판단
     */
    public static final boolean isDeviceUsingKor() {
        return Locale.getDefault().getLanguage().equals("ko");
    }

    /**
     * 전화번호 포멧
     *
     * @param src
     * @return
     */
    public static String phoneFormat(String src) {
        if (src == null) {
            return "";
        }
        if (src.length() == 8) {
            return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
            return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }


    public static void ShareContent(Context context, String pChooserTitle, String pContent) {
        if (pContent == null) return;

        String sChooserTitle = pChooserTitle;
        String sContent = pContent;
        Intent sendIntent = new Intent();
        sendIntent.setType("text/plain");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sContent);
        //sendIntent.putExtra(Intent.EXTRA_SUBJECT, sSubject);
        sendIntent = Intent.createChooser(sendIntent, sChooserTitle);
        context.startActivity(sendIntent);
    }

    /**
     * 안드로이드 진동
     *
     * @param mContext
     */
    @SuppressLint("MissingPermission")
    public static void vibrator(Context mContext) {
        Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(400);
    }


    /**
     * 화면 캡쳐 방지 설정.
     *
     * @param act 실행하려는 Activity
     */
    public static void diableCaption(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //IMLog.d("diableCaption");
            act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    /**
     * 화면 캡쳐 방지 설정 해제.
     *
     * @param act 실행하려는 Activity
     */
    public static void enableCaption(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //IMLog.d("enableCaption");
            act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    public static String addSingDataDate(String signData) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = sdfNow.format(date);
        signData += "&시간=" + formatDate;
        return signData;
    }

//    public static boolean isSessionDisConnect(long time, long checkTime){
//        long sessionTime = (checkTime - time) / 1000 / 60;
//        if ( sessionTime > Long.parseLong(BSGlobal.instance().session_timeout)){
//            return true;
//        } else {
//            return false;
//        }
//    }

    public static String getNotEmptyString(String input) {
        if (input == null || input.length() == 0) {
            return "-";
        }
        return input;
    }

    public static String makePhoneFormat(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber))
            return "";
        String value = getCurrencyNoComma(phoneNumber);
        if (value.length() == 8) {
            return value.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (value.length() == 12) {
            return value.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return value.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

    /**
     * 입력 값 바이트 체크
     *
     * @param param
     * @return
     */
    public static int checkByte(String param) {
        int len = param.length();
        int cnt = 0;

        for (int i = 0; i < len; i++) {
            //-----------------------
            // 256 이하면 1byte짜리
            // 256이상이면 2byte짜리
            //-----------------------
            if (param.charAt(i) < 256)
                cnt++;
            else
                cnt = cnt + 2;
        }

        return cnt;
    }

    /**
     * 입력 값 바이트 체크
     *
     * @param param
     * @return
     */
    public static String checkByte(String param, int checkCnt) {
        int len = param.length();
        int cnt = 0;
        String result = "";

        for (int i = 0; i < len; i++) {
            //-----------------------
            // 256 이하면 1byte짜리
            // 256이상이면 2byte짜리
            //-----------------------

            if (checkCnt == cnt) {
                break;
            }


            if (param.charAt(i) < 256) {
                cnt++;
                result = result + param.charAt(i);
            } else {
                cnt = cnt + 2;
                result = result + param.charAt(i);
            }
        }

        return result;
    }


    public static String getReplaceStr(String pInput, String pStr) {
        if (pInput == null || pInput.length() == 0) {
            return "";
        }
        String sReturn = "";
        sReturn = pInput.replaceAll(pStr, "");
        return sReturn;

    }

    public static String toPhone(String src) {
        if (src == null) {
            return "";
        }
        if (src.length() == 8) {
            return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
            return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

    public static String securityPhone(String num) {
        num = toPhone(num);
        String[] nums = num.split("-");
        nums[1] = nums[1].substring(0, nums[1].length() - 1) + "*";
        nums[2] = nums[2].substring(0, nums[2].length() - 1) + "*";
        num = nums[0] + "-" + nums[1] + "-" + nums[2];
        return num;
    }

    public static boolean hasConnectivity(Context context, boolean wifiOnly) {
        try {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }

            if (wifiOnly)
                return haveConnectedWifi;
            else
                return haveConnectedWifi || haveConnectedMobile;
        } catch (Exception e) {
            return true; //just in case it fails move on, say yeah! we have Internet connection (hopefully)
        }

    }

    @SuppressLint("TrulyRandom")
    public static byte[] encryptPassword(String passwdString) {
        try {
            byte[] cleartext = passwdString.getBytes("UTF8");

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] clearBytes = cipher.doFinal(cleartext);
            byte[] encryptedPwd = Base64.encode(clearBytes, Base64.DEFAULT);
            return encryptedPwd;
        } catch (Exception e) {
            Logger2.e("Failed encryptPassword", e);
        }
        return null;
    }

    public static String decryptPassword(byte[] passwdBytes) {
        String pw = "";
        try {
            byte[] encrypedPwdBytes = Base64.decode(passwdBytes, Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);  //key값
            byte[] plainTextPwdBytes = cipher.doFinal(encrypedPwdBytes);
            pw = new String(plainTextPwdBytes, "UTF8");
        } catch (Exception e) {
            Logger2.e("Failed decryptPassword", e);
        }
        return pw;
    }

    public static String getHashCode(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

//    @SuppressWarnings("MissingPermission")
//    private void sendAlarm() {
//        PositionProviderFactory.create(this, new PositionProvider.PositionListener() {
//            @Override
//            public void onPositionUpdate(Position position) {
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ShortcutActivity.this);
//                String request = ProtocolFormatter.formatRequest(
//                        preferences.getString(MainFragment.KEY_URL, null), position, ALARM_SOS);
//
//                RequestManager.sendRequestAsync(request, new RequestManager.RequestHandler() {
//                    @Override
//                    public void onComplete(boolean success) {
//                        if (success) {
//                            Toast.makeText(ShortcutActivity.this, R.string.status_send_success, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ShortcutActivity.this, R.string.status_send_fail, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }

//    public static Location getMyLocation(Context context) {
//
//    // LocationListener의 핸들을 얻음
//        LocationManager  locManager =(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//    // GPS로 부터 위치정보를 업데이트 요청
//        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,context);
//    // 기지국으로 부터 위치정보를 업데이트 요청
//    //locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
//
//    // 주소를 확인하기 위한 Geocoder KOREA 와 KOREAN 둘다 가능
//   Geocoder geoCoder =new Geocoder(context,Locale.KOREAN);
//
//     }

    public static String GetLocationAddress(Context context, Location currLocation) {
        StringBuffer juso = new StringBuffer();

        try {
            Geocoder geoCoder = new Geocoder(context, Locale.KOREAN);
            double latPoint = currLocation.getLatitude();
            double lngPoint = currLocation.getLongitude();
            //  speed = (float)(myLocation.getSpeed() * 3.6);

            // 위도,경도를 이용하여 현재 위치의 주소를 가져온다.
            List<Address> addresses;
            addresses = geoCoder.getFromLocation(latPoint, lngPoint, 1);
            for (Address addr : addresses) {
                int index = addr.getMaxAddressLineIndex();
                for (int i = 0; i <= index; i++) {
                    juso.append(addr.getAddressLine(i));
                    juso.append(" ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return juso.toString();
    }

//    public static String GetLocationAddress(Context context, Position position) {
//
//        StringBuffer juso = new StringBuffer();
//        Geocoder geoCoder = new Geocoder(context, Locale.KOREAN);
//        double latPoint = position.getLatitude();
//        double lngPoint = position.getLongitude();
//        //  speed = (float)(myLocation.getSpeed() * 3.6);
//
//        try {
//            // 위도,경도를 이용하여 현재 위치의 주소를 가져온다.
//            Guest<Address> addresses;
//            addresses = geoCoder.getFromLocation(latPoint, lngPoint, 1);
//            for (Address addr : addresses) {
//                int index = addr.getMaxAddressLineIndex();
//                for (int i = 0; i <= index; i++) {
//                    juso.append(addr.getAddressLine(i));
//                    juso.append(" ");
//                }
//                juso.append("\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return juso.toString();
//    }

    public static int getAppVersionCode(Context context){
        PackageInfo packageInfo = null;         //패키지에 대한 전반적인 정보

        //PackageInfo 초기화
        try{
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return -1;
        }

        return packageInfo.versionCode;
    }

    public static String getAppVersionName(Context context){
        PackageInfo packageInfo = null;         //패키지에 대한 전반적인 정보

        //PackageInfo 초기화
        try{
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return "";
        }

        return packageInfo.versionName;
    }

    public static String getManufactuerName() {
        String manufacturer = Build.MANUFACTURER;
 //       String model = Build.MODEL;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        }
        return capitalize(manufacturer) ;
    }

    public static String getModelName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(model);
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static String decimalFormat(long num) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(num);
    }

    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    public static String makeStringComma(String str) {
        try {
            if (str.length() == 0)
                return "";
            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat("#,###,###,###");
            return format.format(value);

        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static int getDisplayHeight(WindowManager windowManager) {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.y;
    }

    /**
     * 오레오 버전 알림채널 대응
     */
    public static String getNotiChId(Context context, String channel_id) {
        // 채널의 ID
        String id = channel_id;
        // 사용자에게 보이는 채널의 이름
        CharSequence name = context.getString(R.string.app_name);
        // 사용자에게 보이는 채널의 설명
        String description = context.getString(R.string.app_name);
        // 푸시 타입
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Sets whether notifications posted to this channel should display notification lights
            mChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            mChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            mChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            mChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        return id;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);

        return px;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);

        return dp;
    }


    public static int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }

    @SuppressLint("MissingPermission")
    public static HashMap<String, Object> getLoginLocationInfo(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        HashMap<String, Object> param = new HashMap<>();

        try {
            final LocationManager lm = (LocationManager) activityWeakReference.get().getSystemService(Context.LOCATION_SERVICE);

            if(lm != null) {
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                Geocoder gcd = new Geocoder(activityWeakReference.get().getBaseContext(), Locale.getDefault());

                List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);

                param.put("lon", longitude);
                param.put("lat", latitude);
                param.put("poi", address);

                return param;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return param;
    }

    public static boolean isValidPhoneNumber(String phoneNum) {
        boolean returnValue = false;

        try {
            String regex = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phoneNum);

            if (m.matches()) {
                returnValue = true;
            }

            return returnValue;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 차량 번호 유효 여부 판단
     * 1번째 패턴 12조1234 =>숫자2,한글1,숫자4
     * 2번째 패턴 서울12치1233 한글2,숫자2,한글1,숫자4
     * @param carNum
     * @return
     */
    public static boolean isValidCarNumber(String carNum) {
        boolean returnValue = false;

        try {
            String regex = "^\\d{2}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}/*$";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(carNum);
            if (m.matches()) {
                returnValue = true;
            } else {
                //2번째 패턴 처리
                regex = "^[서울|부산|대구|인천|대전|광주|울산|제주|경기|강원|충남|전남|전북|경남|경북|세종]{2}\\d{2}[가|나|다|라|마|거|너|더|러|머|버|서|어|저|고|노|도|로|모|보|소|오|조|구|누|두|루|무|부|수|우|주|바|사|아|자|허|배|호|하\\x20]\\d{4}$";
                p = Pattern.compile(regex);
                m = p.matcher(carNum);
                if (m.matches()) {
                    returnValue = true;
                }
            }

            return returnValue;

        } catch (Exception e) {
            return false;
        }
    }

    public static String timeMapper(String time) {
        String timeString;
        String amOrPm;
        if (Integer.parseInt(time.split(":")[0]) >= 12) {
            amOrPm = " PM";
        } else {
            amOrPm = " AM";
        }
//            timeString = time.split(":")[0] + ":" + time.split(":")[1] + amOrPm;
        timeString = time.split(":")[0] + ":" + time.split(":")[1];
        return timeString;
    }

    public static ArrayList<String> stringTokenizer(String str) {
        ArrayList<String> arr = new ArrayList<>();

        if (str == null)
            return arr;

        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        while (tokenizer.hasMoreTokens()) {
            arr.add(tokenizer.nextToken());
        }

        return arr;
    }

    public static int DlgUIFalg =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

}
