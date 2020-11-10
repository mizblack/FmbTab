package com.eye3.golfpay.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.UIThread;

public class FmbAlert {

//    public final static int STYLE_CONFIRM = 0;
//    public final static int STYLE_OK_CANCEL = 1;
//    public final static int STYLE_YES_NO = 2;

//    public static void showConfirmDialog(final Context context, final Bundle bundle, final DialogInterface.OnClickListener listener) {
//        if (bundle == null) {
//            if (listener != null) {
//                listener.onClick(null, DialogInterface.BUTTON_NEGATIVE);
//            }
//            return;
//        }
//
//        String title = context.getResources().getString(R.string.default_alert_title);
//        if (bundle.containsKey("title")) {
//            title = bundle.getString("title");
//        }
//        String message = bundle.containsKey("message") ? bundle.getString("message") : "";
//        String labelConfirm = bundle.containsKey("confirm") ? bundle.getString("confirm") : context.getResources().getString(R.string.alert_confirm);
//
//        showConfirmDialog(context, message, context.getResources().getString(R.string.alert_confirm), listener);
//    }

    /**
     * 공통 alert dialog (버튼 한개)
     *
     * @param context  Context
     * @param message  Alert Message
     * @param listener Callback Listener
     */
    public static void showConfirmDialog(final Context context, final String message, final DialogInterface.OnClickListener listener) {
        showConfirmDialog(context, message, context.getResources().getString(R.string.confirm), listener);
    }

    /**
     * 공통 alert dialog (버튼 한개)
     *
     * @param context  Context
     * @param message  Alert Message
     * @param button   Button Label
     * @param listener Callback Listener
     */
    public static void showConfirmDialog(final Context context, final String message, final String button, final DialogInterface.OnClickListener listener) {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle(context.getResources().getString(R.string.default_alert_title));
                dialog.setMessage(message);
                dialog.setPositiveButton(button, listener);
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

//    /**
//     * 공통 alert dialog (버튼 한개)
//     *
//     * @param context Context
//     * @param title Alert title
//     * @param message Alert Message
//     * @param button Button Label
//     * @param listener Callback Listener
//     */
//    public static void showConfirmDialog(final Context context, final String title, final String message, final String button, final DialogInterface.OnClickListener listener) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        dialog.setTitle(title);
//        dialog.setMessage(message);
//        dialog.setPositiveButton(button, listener);
//        dialog.setCancelable(false);
//        dialog.show();
//    }


    /**
     * 공통 alert dialog (버튼 두개) - 라벨 custom
     *
     * @param context  Context
     * @param message  Alert Message
     * @param confirm  Button Label (Confirm - POSITIVE)
     * @param cancel   Button Label (Cancel - NEGATIVE)
     * @param listener Callback Listener
     */
    public static void showAlertDialog(final Context context, final String message, final String confirm, final String cancel, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.default_alert_title));
        dialog.setMessage(message);
        dialog.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            }
        });
        dialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
