package com.eye3.golfpay.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.caddyNote.SendSMS;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.gps.CType;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.guest.GuestInfo;
import com.eye3.golfpay.model.order.ReserveGameType;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-09-22.
 */

public class SmsScoreDialog extends Dialog {

    ArrayList<GuestInfo> guestInfo;
    public interface  IListenerDialog {
        void onSelected(int hole);
    }

    public SmsScoreDialog() {
        super(null);
    }

    public SmsScoreDialog(Context context, ArrayList<GuestInfo> guestInfo) {
        super(context);
        this.guestInfo = guestInfo;
    }

    public SmsScoreDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private LinearLayout memberContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frd_sms_score);
        memberContainer = findViewById(R.id.view_member_container);

        for (GuestInfo guest : guestInfo) {
            addView(guest.getReserveGuestId(), guest.getGuestName(), "", guest.getHp());
        }

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        findViewById(R.id.tv_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendSMS sendSMS = new SendSMS();
                sendSMS.reserve_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
                for (int i = 0; i < memberContainer.getChildCount(); i++) {
                    View childView = memberContainer.getChildAt(i);
                    TextView tvName = childView.findViewById(R.id.tv_name);
                    ImageButton btnCheck = childView.findViewById(R.id.btn_check);
                    EditText editPhone = childView.findViewById(R.id.edit_phone_number);
                    String tag = (String)btnCheck.getTag();
                    if (tag.equals("true")) {

                        SendSMS.GuestInfo guest_info = new SendSMS.GuestInfo();
                        guest_info.guest_id = (String)tvName.getTag();
                        guest_info.hp = editPhone.getText().toString();
                        sendSMS.guest_info.add(guest_info);
                    }
                }

                sendSMS(sendSMS);
            }
        });
    }

    private void addView(String id, String name, String nickName, String phoneNumber) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //* 팀 클럽 뷰
        View childView = LayoutInflater.from(getContext()).inflate(R.layout.sms_list_item, null, false);
        ImageButton btnCheck = childView.findViewById(R.id.btn_check);
        TextView tvName = childView.findViewById(R.id.tv_name);
        TextView tvNickName = childView.findViewById(R.id.tv_nickname);
        EditText editPhone = childView.findViewById(R.id.edit_phone_number);
        tvName.setTag(id);
        tvName.setText(name);
        editPhone.setText(phoneNumber);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tag = (String)btnCheck.getTag();
                if (tag.equals("true")) {
                    btnCheck.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.uncheck));
                    btnCheck.setTag("false");
                } else {
                    btnCheck.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.check));
                    btnCheck.setTag("true");
                }
            }
        });


        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 90);
        childView.setLayoutParams(lllp);
        memberContainer.addView(childView);
    }

    private void sendSMS(SendSMS sendSMS) {

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).sendSMS(getContext(), sendSMS, new DataInterface.ResponseCallback<ResponseData<CType>>() {
            @Override
            public void onSuccess(ResponseData<CType> response) {
                dismiss();
            }

            @Override
            public void onError(ResponseData<CType> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}