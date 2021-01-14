package com.kt.wmms.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

public class TestItemTextWatcher implements TextWatcher {

    private final EditText et;
    private String mValue;

    public TestItemTextWatcher(EditText et, String value)
    {
        this.et = et;
        this.mValue = value;
    }

    @SuppressWarnings("unused")
    private static final String TAG = "TestItemTextWatcher";

    @Override
    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);
        mValue =  et.getText().toString().trim();

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    public String getmValue(){
        return mValue;
    }

}