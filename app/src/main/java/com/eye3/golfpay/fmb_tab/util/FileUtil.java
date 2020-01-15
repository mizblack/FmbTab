package com.eye3.golfpay.fmb_tab.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class FileUtil {

    public static File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("FileUtil", "Directory not created");
        }
        return file;
    }

//    public static String getWmmsPictureFileName(Device device){
//
//       StringBuilder sb = new StringBuilder();
//       sb.append("A") ;
//        sb.append("_");
//        sb.append(device.deviceCategoryName);
//        sb.append("_");
//        sb.append(device.deviceTypeName);
//        sb.append("_");
//        sb.append(device.deviceId);
//        sb.append("_");
////        sb.append(Global.userInfo.userName);
////        sb.append("_");
//        sb.append(DateUtils.getCurrentDate(DateUtils.DATE_FORMATTED_1));
//        sb.append("_");
//        sb.append(DateUtils.getCurrentDate(DateUtils.DATE_FORMATTED_2));
//        sb.append(".jpg");
//        return sb.toString();
//    }
//
//    public static String getWmmsPictureAbbreviation(Device device){
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("A") ;
//        sb.append("_");
//        sb.append(device.deviceCategoryName);
//        sb.append("_");
//        sb.append(device.deviceTypeName);
//        sb.append("_");
//        sb.append(device.deviceId);
//        return sb.toString();
//    }

}
