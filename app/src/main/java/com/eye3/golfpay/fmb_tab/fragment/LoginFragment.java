package com.eye3.golfpay.fmb_tab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eye3.golfpay.fmb_tab.BuildConfig;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.util.Security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class LoginFragment extends BaseFragment {
    TextView mStartTextView;
    EditText mPhoneNumberEditText, mNameEditText;
    ImageView mCancelIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fr_drawer_login, container, false);
        mPhoneNumberEditText = v.findViewById(R.id.phoneNumberEditText);
        mNameEditText = v.findViewById(R.id.nameEditText);
        mStartTextView = v.findViewById(R.id.startTextView);
        mCancelIcon = v.findViewById(R.id.cancelIcon);
        init(v);
        return v;
    }

    private void init(View v){

        //매번 로그인 하기 귀찮아서 자동 로그인
        if(BuildConfig.DEBUG == true) {
            UIThread.executeInUIThread(new Runnable() {
                @Override
                public void run() {
                    mStartTextView.performClick();
                }
            });
        }

        // 메뉴뷰 이벤트처리
        mStartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    login(mNameEditText.getText().toString().trim(), Security.encrypt(mPhoneNumberEditText.getText().toString()));
                } catch (NoSuchPaddingException
                        | NoSuchAlgorithmException
                        | InvalidAlgorithmParameterException
                        | InvalidKeyException
                        | BadPaddingException
                        | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                closeKeyboard(mNameEditText);
                closeKeyboard(mPhoneNumberEditText);
                mNameEditText.setEnabled(false);
                mPhoneNumberEditText.setEnabled(false);

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
        //   showProgress("로그인 중입니다....");

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).login(id, pwd, new DataInterface.ResponseCallback<Login>() {
            @Override
            public void onSuccess(Login response) {
                hideProgress();
                systemUIHide();

                if (response.getRetCode() != null && response.getRetCode().equals("ok")) {
                    Global.CaddyNo = String.valueOf(response.getCaddyNo());
                    changeDrawerViewToMenuView();
                } else {
                    Toast.makeText(getActivity(), "ID와 패스워드를 확인해주세요", Toast.LENGTH_SHORT).show();
                    mNameEditText.setEnabled(true);
                    mPhoneNumberEditText.setEnabled(true);
                    mNameEditText.setText("");
                    mPhoneNumberEditText.setText("");

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

    public void changeDrawerViewToMenuView() {
        //메뉴fragment활성화 시킨다.
        GoNavigationDrawer(new ViewMenuFragment(), null);
    }




}
