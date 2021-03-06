package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.BaseURLSettingActivity;
import com.eye3.golfpay.activity.CaddySelectActivity;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.SingleClickListener;
import com.eye3.golfpay.dialog.YesNoDialog;
import com.eye3.golfpay.model.info.VersionInfo;
import com.eye3.golfpay.model.login.Login;
import com.eye3.golfpay.model.teeup.Caddy;
import com.eye3.golfpay.model.teeup.TeeUpTime;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.Security;
import com.eye3.golfpay.util.Util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends BaseFragment {
    TextView mStartTextView;
    EditText editPwd;
    EditText Id;
    TextView caddyName;
    ImageView mCancelIcon;
    CheckBox cbSave;
    String golfId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fr_drawer_login, container, false);
        caddyName = v.findViewById(R.id.tv_caddy_name);
        editPwd = v.findViewById(R.id.phoneNumberEditText);
        Id = v.findViewById(R.id.nameText);
        mStartTextView = v.findViewById(R.id.startTextView);
        mCancelIcon = v.findViewById(R.id.cancelIcon);
        cbSave = v.findViewById(R.id.cb_save);
        init(v);
        checkUpdate();

        SharedPreferences pref = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String id = pref.getString("id", "");
        String pwd = pref.getString("password", "");
        boolean check = pref.getBoolean("save", false);

//        if (BuildConfig.DEBUG) {
//            golfId = "http://erp.silkvalleygc.co.kr";
//            id = "c@c";
//            pwd = "1111";
//        }

        //editGolfId.setText(golfId);
        caddyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaddySelectActivity.class);
                startActivityForResult(intent, CaddySelectActivity.Id);
            }
        });

        Id.setText(id);
        editPwd.setText(pwd);
        caddyName.setText(name);
        cbSave.setChecked(check);

        Bundle bundle = getArguments();
        if (bundle == null)
            return v;

        boolean isTokenError = bundle.getBoolean("TokenError", false);
        if (isTokenError) {
            YesNoDialog dlg = new YesNoDialog(getContext(), R.style.DialogTheme);
            WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
            wmlp.gravity = Gravity.CENTER;

            // Set alertDialog "not focusable" so nav bar still hiding:
            dlg.getWindow().
                    setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
            dlg.show();
            dlg.setContentTitle("?????? ???????????? ????????????????????????.");
            dlg.setDisableCancelButton();
            dlg.setListener(new YesNoDialog.IListenerYesNo() {
                @Override
                public void onConfirm() {

                }
            });
        }

        return v;
    }

    private void init(View v) {

        //?????? ????????? ?????? ???????????? ?????? ?????????
//        if(BuildConfig.DEBUG) {
//            UIThread.executeInUIThread(new Runnable() {
//                @Override
//                public void run() {
//                    mStartTextView.performClick();
//                }
//            });
//        }

        // ????????? ???????????????
        mStartTextView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                try {
                    login(Id.getText().toString().trim(), Security.encrypt(editPwd.getText().toString()));
                } catch (NoSuchPaddingException
                        | NoSuchAlgorithmException
                        | InvalidAlgorithmParameterException
                        | InvalidKeyException
                        | BadPaddingException
                        | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                closeKeyboard(Id);
                closeKeyboard(editPwd);
            }
        });

        mCancelIcon.findViewById(R.id.cancelIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void login(String id, String pwd) {
        //   showProgress("????????? ????????????....");

        if (id.equals("abcdefg")) {
            Intent intent = new Intent(getActivity(), BaseURLSettingActivity.class);
            startActivityForResult(intent, BaseURLSettingActivity.Id);
            return;
        }

        SharedPreferences pref = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        golfId = pref.getString("golf_id", "");
        if (golfId.isEmpty()) {
            golfId = "http://erp.silkvalleygc.co.kr/";
        }

        Global.loginToken = null;
        DataInterface.getInstance(golfId + "/api/v1/").login(id, pwd, new DataInterface.ResponseCallback<Login>() {
            @Override
            public void onSuccess(Login response) {
                hideProgress();
                systemUIHide();

                if (response.getRetCode() != null && response.getRetCode().equals("ok")) {
                    Global.CaddyNo = String.valueOf(response.getCaddyNo());

                    Global.HOST_BASE_ADDRESS_AWS = golfId + "/";
                    Global.HOST_ADDRESS_AWS = golfId + "/api/v1/";
                    Global.HOST_LARAVEL_ADDRESS = golfId + ":6001";
                    Global.tabletLogo = response.getTabletLogo();
                    Global.loginToken = response.getLoginToken();
                    getTodayReservesForCaddy(getContext(), Global.CaddyNo);

                    if (cbSave.isChecked()) {
                        saveAccount(golfId, id, caddyName.getText().toString(), editPwd.getText().toString(), true);
                    } else {
                        saveAccount("", "", "","", false);
                    }

                } else {
                    Toast.makeText(getActivity(), "ID??? ??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
                    Id.setEnabled(true);
                    editPwd.setEnabled(true);
                    Id.setText("");
                    editPwd.setText("");
                    caddyName.setText("");
                }
            }

            @Override
            public void onError(Login response) {
                hideProgress();
                systemUIHide();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
                systemUIHide();
            }
        });
    }

    private void saveAccount(String golfId, String id, String name, String pwd, boolean check) {
        try {
            SharedPreferences pref = getActivity().getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("golf_id", golfId);
            editor.putString("id", id);
            editor.putString("name", name);
            editor.putString("password", pwd);
            editor.putBoolean("save", check);
            editor.apply();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void changeDrawerViewToMenuView() {
        //??????fragment????????? ?????????.
        GoNavigationDrawer(new TeeUpFragment(), null);
    }

    private void getTodayReservesForCaddy(final Context context, String caddy_id) {
        //   showProgress("??????????????? ???????????? ????????????....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getTodayReservesForCaddy(caddy_id, new DataInterface.ResponseCallback<TeeUpTime>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TeeUpTime response) {
                hideProgress();
                systemUIHide();

                if (response == null || response.getTodayReserveList() == null || response.getTodayReserveList().size() == 0) {
                    Toast.makeText(context, "????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                changeDrawerViewToMenuView();
                ((MainActivity) getActivity()).updateUI();
            }

            @Override
            public void onError(TeeUpTime response) {
                hideProgress();
                systemUIHide();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
                systemUIHide();
            }
        });
    }

    private void checkUpdate() {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).checkUpdate(new DataInterface.ResponseCallback<VersionInfo>() {
            @Override
            public void onSuccess(VersionInfo response) {

                try {
                    PackageInfo packageInfo = getContext().getApplicationContext()
                            .getPackageManager()
                            .getPackageInfo(getContext().getApplicationContext().getPackageName(), 0);
                    String currentVersion = packageInfo.versionName;

                    if (compareVersion(response.getVersion(), currentVersion))
                        update(response.getVersion());

                } catch (PackageManager.NameNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VersionInfo response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void update(String version) {
        Intent intent = Objects.requireNonNull(getActivity()).getPackageManager().getLaunchIntentForPackage("com.machaboo.apkinstaller");
        intent.putExtra("update", 0x34);
        intent.putExtra("version", version);
        startActivity(intent);

        new Handler().postDelayed(new Runnable() {// 1 ??? ?????? ??????
            @Override
            public void run() {
                // ????????? ?????? ??????

                getActivity().moveTaskToBack(true);						// ???????????? ?????????????????? ??????
                getActivity().finishAndRemoveTask();						// ???????????? ?????? + ????????? ??????????????? ?????????
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 1000);
    }

    public static boolean compareVersion(String appVersion, String compareVersion) {
        boolean isNeedUpdate = false;
        String[] arrX = appVersion.split("[.]");
        String[] arrY = compareVersion.split("[.]");

        int length = Math.max(arrX.length, arrY.length);

        for(int i = 0; i < length; i++){
            int x, y;
            try {
                x = Integer.parseInt(arrX[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                x = 0;
            }
            try {
                y = Integer.parseInt(arrY[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                y = 0;
            }

            if(x > y) {
                // ??? ????????? ???
                isNeedUpdate = true;
                break;
            }else if(x < y){
                // ?????? ????????? ???
                isNeedUpdate = false;
                break;
            } else {
                // ?????? ??????
                isNeedUpdate = false;
            }
        }
        return isNeedUpdate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BaseURLSettingActivity.Id && resultCode == 100) {
            SharedPreferences pref = getActivity().getSharedPreferences("config", MODE_PRIVATE);
            String id = pref.getString("id", "");
            Id.setText(id);
        } else if (requestCode == CaddySelectActivity.Id && resultCode == 100) { //get All Caddy
            String id = data.getStringExtra("email");
            String name = data.getStringExtra("name");
            caddyName.setText(name);
            Id.setText(id);
            editPwd.setText("");
        }
    }
}
