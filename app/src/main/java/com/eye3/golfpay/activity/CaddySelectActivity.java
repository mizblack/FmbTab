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
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.CaddyAdapter;
import com.eye3.golfpay.model.teeup.Caddy;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CaddySelectActivity extends BaseActivity {
    static public final int Id = 0x7234;
    private RecyclerView recyclerView;
    String golfId;
    List<Caddy> caddyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caddy_select);

        SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
        golfId = pref.getString("golf_id", "");
        if (golfId.isEmpty()) {
            golfId = "http://erp.silkvalleygc.co.kr/";
        }

        Intent intent = getIntent();
        getAllCaddyList(this);
    }

    private void initRecyclerView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);

        recyclerView = findViewById(R.id.recycler_caddy);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
        String name = pref.getString("name", "");


        CaddyAdapter adapter = new CaddyAdapter(this, findIndexFromName(name), new CaddyAdapter.IAdapterClickListener() {
            @Override
            public void onClickListener(int position) {
                String email = caddyList.get(position).email;
                String name = caddyList.get(position).name;
                Intent intent = new Intent();
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                setResult(100, intent);
                finish();
            }
        });

        adapter.setData(caddyList);
        recyclerView.setAdapter(adapter);
    }

    private int findIndexFromName(String name) {
        int i = 0;
        for (Caddy caddy: caddyList) {
            if (caddy.name.equals(name)) {
                return i;
            }

            i++;
        }

        return -1;
    }

    private void getAllCaddyList(final Context context) {
        //   showProgress("티업시간을 받아오는 중입니다....");
        DataInterface.getInstance(golfId + "/api/v1/").getAllCaddyList(context, new DataInterface.ResponseCallback<ResponseData<Caddy>>() {
            @Override
            public void onSuccess(ResponseData<Caddy> response) {
                caddyList = response.getList();
                initRecyclerView();
            }

            @Override
            public void onError(ResponseData<Caddy> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}