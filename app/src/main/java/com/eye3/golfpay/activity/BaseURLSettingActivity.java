package com.eye3.golfpay.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eye3.golfpay.R;

import java.util.Objects;

public class BaseURLSettingActivity extends BaseActivity {
    static public final int Id = 0x2838;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_url_setting);
        editText = findViewById(R.id.edit);
        SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
        String golfId = pref.getString("golf_id", "");
        editText.setHint(golfId);

        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(editText);
                finish();
            }
        });

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(editText);
                SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("golf_id", editText.getText().toString());
                editor.apply();
                setResult(100);
                finish();
            }
        });

        findViewById(R.id.btn_http).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("http://");
                editText.setSelection(editText.getText().length());
            }
        });

        findViewById(R.id.btn_https).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("https://");
                editText.setSelection(editText.getText().length());
            }
        });

        findViewById(R.id.btn_golfpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.append(".golfpay");
                editText.setSelection(editText.getText().length());
            }
        });

        findViewById(R.id.btn_co_kr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.append(".co.kr");
                editText.setSelection(editText.getText().length());
            }
        });

        findViewById(R.id.btn_com).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.append(".com");
                editText.setSelection(editText.getText().length());
            }
        });

        findViewById(R.id.btn_silk_real).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.append("http://erp.silkvalleygc.co.kr");
                editText.setSelection(editText.getText().length());
            }
        });

        findViewById(R.id.btn_silk_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.append("http://silktest.golfpay.co.kr");
                editText.setSelection(editText.getText().length());
            }
        });
    }

    @Override
    protected void onStart() {
        editText.requestFocus();
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // im.showSoftInput(searchBox, 0);
        im.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        super.onStart();
    }

    public void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE));
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}