package com.eye3.golfpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.eye3.golfpay.R;

import java.util.Objects;

public class GuestSettingActivity extends AppCompatActivity {
    static public final int Id = 0x8834;
    private EditText editText;
    private String type;
    private int index;
    private int viewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_setting);
        editText = findViewById(R.id.edit);
        TextView textView = findViewById(R.id.tv_input_guide);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        index = intent.getIntExtra("index", 0);
        String value = intent.getStringExtra("value");
        viewId = intent.getIntExtra("id", 0);

        if (type.equals("name")) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            textView.setText("이름을 입력하세요");
        } else if (type.equals("car")) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            textView.setText("차량번호를 입력하세요");
        } else if (type.equals("phone")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            textView.setText("연락처를 입력하세요");
        } else if (type.equals("memo")) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            textView.setText("메모를 입력하세요");
        } else if (type.equals("club")) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);

            switch (viewId) {
                case R.id.view_wood_memo : {
                    textView.setText("우드의 메모를 입력하세요");
                    break;
                }
                case R.id.view_utility_memo : {
                    textView.setText("유틸리티의 메모를 입력하세요");
                    break;
                }
                case R.id.view_iron_memo : {
                    textView.setText("아이언의 메모를 입력하세요");
                    break;
                }
                case R.id.view_wedge_memo : {
                textView.setText("웨지의 메모를 입력하세요");
                    break;
                }
                case R.id.view_putter_memo : {
                    textView.setText("퍼터의 메모를 입력하세요");
                    break;
                }
            }
        }

        editText.setHint(value);

        if (type.equals("memo") || type.equals("club") || type.equals("name")) {
            assert value != null;
            if (!value.isEmpty()) {
                editText.setText(value);
            }
        }

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
                Intent i = new Intent();
                i.putExtra("index", index);
                i.putExtra("type", type);
                i.putExtra("value", editText.getText().toString());

                if (type.equals("club")) {
                    i.putExtra("id", viewId);
                }

                setResult(100, i);
                closeKeyboard(editText);
                finish();
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