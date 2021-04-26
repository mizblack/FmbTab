package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.SingleClickListener;
import com.eye3.golfpay.dialog.YesNoDialog;
import com.eye3.golfpay.model.login.Login;
import com.eye3.golfpay.model.teeup.TeeUpTime;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.util.Security;
import com.eye3.golfpay.util.Util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends BaseFragment {
    TextView mStartTextView;
    EditText editGolfId, editId, editPwd;
    ImageView mCancelIcon;
    CheckBox cbSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fr_drawer_login, container, false);
        editGolfId = v.findViewById(R.id.edit_GolfId);
        editPwd = v.findViewById(R.id.phoneNumberEditText);
        editId = v.findViewById(R.id.nameEditText);
        mStartTextView = v.findViewById(R.id.startTextView);
        mCancelIcon = v.findViewById(R.id.cancelIcon);
        cbSave = v.findViewById(R.id.cb_save);
        init(v);

        SharedPreferences pref = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        String golfId = pref.getString("golf_id", "");
        String id = pref.getString("id", "");
        String pwd = pref.getString("password", "");
        boolean check = pref.getBoolean("save", false);

        if (golfId.isEmpty()) {
            golfId = "http://silkv.golfpay.co.kr";
        }

        editGolfId.setText(golfId);
        editId.setText(id);
        editPwd.setText(pwd);
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
            dlg.setContentTitle("다른 기기에서 로그인하였습니다.");
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

        //매번 로그인 하기 귀찮아서 자동 로그인
//        if(BuildConfig.DEBUG) {
//            UIThread.executeInUIThread(new Runnable() {
//                @Override
//                public void run() {
//                    mStartTextView.performClick();
//                }
//            });
//        }

        // 메뉴뷰 이벤트처리
        mStartTextView.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (editGolfId.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "골프장 ID를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    login(editGolfId.getText().toString(), editId.getText().toString().trim(), Security.encrypt(editPwd.getText().toString()));
                } catch (NoSuchPaddingException
                        | NoSuchAlgorithmException
                        | InvalidAlgorithmParameterException
                        | InvalidKeyException
                        | BadPaddingException
                        | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                closeKeyboard(editId);
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

    private void login(String golfId, String id, String pwd) {
        //   showProgress("로그인 중입니다....");

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
                    Global.tabletLogo = response.getTabletLogo();
                    Global.loginToken = response.getLoginToken();
                    getTodayReservesForCaddy(getContext(), Global.CaddyNo);

                    if (cbSave.isChecked()) {
                        saveAccount(golfId, id, editPwd.getText().toString(), true);
                    } else {
                        saveAccount("", "", "", false);
                    }

                } else {
                    Toast.makeText(getActivity(), "ID와 패스워드를 확인해주세요", Toast.LENGTH_SHORT).show();
                    editId.setEnabled(true);
                    editPwd.setEnabled(true);
                    editId.setText("");
                    editPwd.setText("");
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

    private void saveAccount(String golfId, String id, String pwd, boolean check) {
        try {
            SharedPreferences pref = getActivity().getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("golf_id", golfId);
            editor.putString("id", id);
            editor.putString("password", pwd);
            editor.putBoolean("save", check);
            editor.apply();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void changeDrawerViewToMenuView() {
        //메뉴fragment활성화 시킨다.
        GoNavigationDrawer(new TeeUpFragment(), null);
    }

    private void getTodayReservesForCaddy(final Context context, String caddy_id) {
        //   showProgress("티업시간을 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getTodayReservesForCaddy(caddy_id, new DataInterface.ResponseCallback<TeeUpTime>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TeeUpTime response) {
                hideProgress();
                systemUIHide();

                if (response == null || response.getTodayReserveList() == null || response.getTodayReserveList().size() == 0) {
                    Toast.makeText(context, "배정된 예약이 없습니다.", Toast.LENGTH_SHORT).show();
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
}
