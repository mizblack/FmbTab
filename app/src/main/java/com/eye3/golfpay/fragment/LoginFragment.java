package com.eye3.golfpay.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.login.Login;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.util.Security;

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
        String golfId  = pref.getString("golf_id", "");
        String id  = pref.getString("id", "");
        String pwd = pref.getString("password", "");
        boolean check = pref.getBoolean("save", false);
        editGolfId.setText(golfId);
        editId.setText(id);
        editPwd.setText(pwd);
        cbSave.setChecked(check);

//        if (BuildConfig.DEBUG) {
////            mNameEditText.setText("roidcaddy@golfpay.co.kr");
////            mPhoneNumberEditText.setText("@a1234567");
//
//            mNameEditText.setText("aaa@aaa.aaa");
//            mPhoneNumberEditText.setText("aaaa");
//        }

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
        mStartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                editId.setEnabled(false);
                editPwd.setEnabled(false);
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

        DataInterface.getInstance(golfId+"/api/v1/").login(id, pwd, new DataInterface.ResponseCallback<Login>() {
            @Override
            public void onSuccess(Login response) {
                hideProgress();
                systemUIHide();

                if (response.getRetCode() != null && response.getRetCode().equals("ok")) {
                    Global.CaddyNo = String.valueOf(response.getCaddyNo());
                    changeDrawerViewToMenuView();

                    if (cbSave.isChecked()) {
                        saveAccount(golfId, id, editPwd.getText().toString(), true);
                    } else {
                        saveAccount("", "", "", false);
                    }

                    Global.HOST_BASE_ADDRESS_AWS = golfId + "/";
                    Global.HOST_ADDRESS_AWS = golfId + "/api/v1/";
                    Global.tabletLogo = response.getTabletLogo();
                    ((MainActivity)getActivity()).updateUI();

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
}
