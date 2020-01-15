package com.eye3.golfpay.fmb_tab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.BaseActivity;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.Token;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.util.BackPressCloseHandler;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {
    private String mId;
    private String mPw;
    private String mNsType;
    private EditText etId, etPw;
    private CheckBox chkSave;
    private Button btnLogin;
    protected BackPressCloseHandler backPressCloseHandler;
    private RadioGroup rd;
    private String part = "북부";
    private TextView tvPw;
    private boolean checked = false;
    RadioButton btn0, btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressCloseHandler = new BackPressCloseHandler(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_login);
        init();
        setLoginUISetting();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etId.getText().toString().trim() == "") {
                    Toast.makeText(LoginActivity.this, getResources().getText(R.string.id_blank), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etPw.getText().toString().trim() == "") {
                    Toast.makeText(LoginActivity.this, getResources().getText(R.string.pw_blank), Toast.LENGTH_SHORT).show();
                    return;
                }
                mId = etId.getText().toString().trim();
                mPw = etPw.getText().toString().trim();

                login();

            }
        });

//        private void saveToDB(){
//
//        }

    }

    private void init() {

        btnLogin = (Button) findViewById(R.id.btn_login);
        etId = (EditText) findViewById(R.id.ll_id).findViewById(R.id.et_item);
        ((TextView) findViewById(R.id.ll_id).findViewById(R.id.tv_item)).setText("아이디");
        etPw = (EditText) findViewById(R.id.ll_pw).findViewById(R.id.et_item);
            etPw.setText("12345678");
        tvPw = ((TextView) findViewById(R.id.ll_pw).findViewById(R.id.tv_item));
        etPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tvPw.setText("비밀번호");


        rd = (RadioGroup) findViewById(R.id.radio_group);
        btn0 = (RadioButton) findViewById(R.id.northern);
        btn1 = findViewById(R.id.southern);

        chkSave = (CheckBox) findViewById(R.id.chk_save);
        rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.southern)
                    part = "남부";
                else if (checkedId == R.id.northern)
                    part = "북부";

            }
        });
    }

    private void setLoginUISetting() {
//        if (mRealm.where(UserInfo.class).findFirst() != null) {
//            mId = mRealm.where(UserInfo.class).findFirst().getId();
//            mNsType = mRealm.where(UserInfo.class).findFirst().getNsType();
//            checked = mRealm.where(UserInfo.class).findFirst().getSavedChecked();
//        }
        etId.setText(mId);
        if ("북부".equals(mNsType)) {
            btn0.setChecked(true);
        } else if ("남부".equals(mNsType)) {
            btn1.setChecked(true);
        } else {
            btn0.setChecked(true);
        }
        if (checked)
            chkSave.setChecked(true);
        else
            chkSave.setChecked(false);
//        UserInfo userinfo = mRealm.where(UserInfo.class).findFirst();
//        if (userinfo != null) {
//            if (userinfo.savedChecked) {
//                chkSave.setChecked(true);
//                etId.setText(userinfo.userId);
//            }
//        }
    }


//    private void initRealm() {
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .name("wmms.realm")
//                //   .encryptionKey(getKey())
//                .schemaVersion(1)
//                //           .modules(new UserInfo())
//                //        .migration(new MyMigration())
//                .build();
//        // Use the config
//        mRealm = Realm.getInstance(config);
//    }


//    private boolean IsSetUserInfoToDB(UserInfo userInfo) {
//
//        mRealm.beginTransaction();
//        try {
//            mRealm.delete(UserInfo.class);
//            UserInfo user = mRealm.createObject(UserInfo.class); // Create a new object
//            user.setId(userInfo.userId);
//            //   user.setPw(userInfo.password);
//            user.setuserName(userInfo.userName);
//            user.setNsType(userInfo.nsType);
//            if (chkSave.isChecked()) {
//                user.setSavedChecked(true);
//
//            } else {
//                user.setSavedChecked(false);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        mRealm.commitTransaction();
//        return true;
//    }

    private void login() {
        showProgress(getResources().getString(R.string.msg_login_prog));
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", etId.getText().toString().trim());
        params.put("password", etPw.getText().toString().trim());
        params.put("nsType", part); //**북부 남부

        DataInterface.getInstance().logIn(LoginActivity.this, params, new DataInterface.ResponseCallback<ResponseData<Token>>() {

            @Override
            public void onSuccess(ResponseData<Token> response) {
                if ("S1000".equals(response.getResultCode())) {

                    Token AppToken = response.getData();
                    Global.Token = AppToken.token;
                 //   Toast.makeText(LoginActivity.this, mId + "-" + mPw + "-" + part, Toast.LENGTH_LONG).show();
                  //  requestUserInfo();
                }else{
                   hideProgress();
                    Toast.makeText(LoginActivity.this, response.getError(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(ResponseData<Token> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

        });
    }

//    private void requestUserInfo() {
//
//        DataInterface.getInstance().getUserInfo(LoginActivity.this, new DataInterface.ResponseCallback<ResponseData<UserInfo>>() {
//
//
//            @Override
//            public void onSuccess(ResponseData<UserInfo> response) {
//                if ("S1000".equals(response.getResultCode())) {
//                    Global.userInfo = response.getData();
//
//                    if (IsSetUserInfoToDB(Global.userInfo))
//                        goToMainActivity();
//                    hideProgress();
//                }
//            }
//
//            @Override
//            public void onError(ResponseData<UserInfo> response) {
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//
//        });
//    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
