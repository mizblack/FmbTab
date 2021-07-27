package com.eye3.golfpay.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.CaddyAdapter;
import com.eye3.golfpay.common.SingleClickListener;
import com.eye3.golfpay.model.teeup.Caddy;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

public class PopupActivity extends BaseActivity {
    static public final int Id = 0x2234;
    private ConstraintLayout view;
    private TextView tvMessage;
    private TextView tvSender;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        String msg = getIntent().getStringExtra("message");
        String sender = getIntent().getStringExtra("sender");
        String time = getIntent().getStringExtra("time");

        tvMessage = findViewById(R.id.tv_message);
        tvSender = findViewById(R.id.tv_sender);
        tvTime = findViewById(R.id.tv_time);

        tvMessage.setText(msg);
        tvSender.setText(sender);
        tvTime.setText(time);

        view = findViewById(R.id.layout_container);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.tv_reply).setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                setResult(100);
                finish();
            }
        });
    }
}