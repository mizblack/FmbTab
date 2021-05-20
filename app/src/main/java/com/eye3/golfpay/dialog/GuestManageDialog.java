package com.eye3.golfpay.dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.GuestSettingActivity;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.fragment.BaseFragment;
import com.eye3.golfpay.model.caddyNote.SendSMS;
import com.eye3.golfpay.model.gps.CType;
import com.eye3.golfpay.model.guest.GuestInfo;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by Administrator on 2017-09-22.
 */

public class GuestManageDialog extends Dialog {

    static public final int Id = 0x8924;
    BaseFragment baseFragment;
    ArrayList<GuestInfo> guestInfoArrayList;
    int uploadCount = 0; //고객 한명씩 api호출할 때마다 증가 마지막 ap함 i호출을 찾기 위함

    public GuestManageDialog() {
        super(null);
    }

    public GuestManageDialog(Context context, ArrayList<GuestInfo> guestInfo, BaseFragment fragment) {
        super(context);
        this.guestInfoArrayList = guestInfo;
        baseFragment = fragment;
    }

    private LinearLayout memberContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.frd_guest_manage);
        memberContainer = findViewById(R.id.view_member_container);

        int index = 0;
        for (GuestInfo guest : guestInfoArrayList) {
            boolean isLast = false;
            if (index == guestInfoArrayList.size()-1)
                isLast = true;

            addView(index++, guest.getReserveGuestId(), guest.getGuestName(), "", guest.getCarNo(), guest.getHp(), isLast);
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
                uploadCount = 0;
                setPersonalInfo(getGuestInfo(0), uploadCount);

                //순서 바뀌는 부분
                if (Global.guestOrdering == null) {
                    Global.guestOrdering = new ArrayList<>();
                } else {
                    Global.guestOrdering.clear();
                }

                ordering();
            }
        });
    }

    private GuestInfo getGuestInfo(int index) {
        View view = memberContainer.getChildAt(index);
        GuestInfo guestInfo = findGuestInfo((String)view.getTag());
        TextView tvName = view.findViewById(R.id.tv_name);
        //TextView tvBackName = view.findViewById(R.id.tv_backname);
        TextView tvCarNumber = view.findViewById(R.id.tv_car_number);
        TextView tvPhoneNumber = view.findViewById(R.id.tv_phone_number);

        guestInfo.setGuestName(tvName.getText().toString());
        guestInfo.setCarNo(tvCarNumber.getText().toString());
        guestInfo.setHp(tvPhoneNumber.getText().toString());
        return guestInfo;
    }

    private GuestInfo findGuestInfo(String id) {
        for (GuestInfo guestInfo: guestInfoArrayList) {
            if (guestInfo.getReserveGuestId().equals(id))
                return guestInfo;
        }

        return null;
    }

    private void ordering() {
        for (int i = 0; i <  memberContainer.getChildCount(); i++) {
            View view = memberContainer.getChildAt(i);
            String id = (String)view.getTag();
            Global.guestOrdering.add(id);
        }
    }

    private void setPersonalInfo(GuestInfo guestInfo, int index) {

        String guest_id = guestInfo.getReserveGuestId();
        String carNumber = guestInfo.getCarNo();
        String phoneNumber = guestInfo.getHp();
        String memo = guestInfo.getGuestMemo();
        String tabletName = guestInfo.getGuestName();

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setPersonalInfo(getContext(), Global.reserveId, guest_id, carNumber, phoneNumber, memo, tabletName,
                new DataInterface.ResponseCallback<ResponseData<Object>>() {
                    @Override
                    public void onSuccess(ResponseData<Object> response) {

                        if (guestInfoArrayList.size() == ++uploadCount) {
                            dismiss();
                            return;
                        }

                        setPersonalInfo(getGuestInfo(uploadCount), uploadCount);
                    }

                    @Override
                    public void onError(ResponseData<Object> response) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    private void addView(int index, String id, String name, String backName, String carNumber, String phoneNumber, boolean isLast) {
        ////////////////////////////////////////////////////////////////////////////////////////////
        //* 팀 클럽 뷰
        View childView = LayoutInflater.from(getContext()).inflate(R.layout.guest_manage_list_item, null, false);
        childView.setTag(id);
        ImageButton btnUp = childView.findViewById(R.id.btn_up);
        ImageButton btnDown = childView.findViewById(R.id.btn_down);
        TextView tvName = childView.findViewById(R.id.tv_name);
        TextView tvBackName = childView.findViewById(R.id.tv_backname);
        TextView tvCarNumber = childView.findViewById(R.id.tv_car_number);
        TextView tvPhoneNumber = childView.findViewById(R.id.tv_phone_number);
        tvName.setText(name);
        tvBackName.setText(backName);
        tvCarNumber.setText(carNumber);
        tvPhoneNumber.setText(phoneNumber);

        if (index == 0) {
            btnUp.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_down_button_disable));
            btnUp.setRotation(0);
            btnUp.setEnabled(false);
        } else if (isLast) {
            btnDown.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_down_button_disable));
            btnDown.setRotation(180);
            btnDown.setEnabled(false);
        }

        childView.findViewById(R.id.btn_edit_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GuestSettingActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("type", "name");
                intent.putExtra("value", tvName.getText().toString());
                baseFragment.startActivityForResult(intent, GuestSettingActivity.Id);
            }
        });
        childView.findViewById(R.id.btn_edit_car_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GuestSettingActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("type", "car");
                intent.putExtra("value", tvCarNumber.getText().toString());
                baseFragment.startActivityForResult(intent, GuestSettingActivity.Id);
            }
        });
        childView.findViewById(R.id.btn_edit_phone_number).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GuestSettingActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("type", "phone");
                intent.putExtra("value", tvPhoneNumber.getText().toString());
                baseFragment.startActivityForResult(intent, GuestSettingActivity.Id);
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               swapView(index, index-1);
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapView(index, index+1);
            }
        });

        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 90);
        childView.setLayoutParams(lllp);
        memberContainer.addView(childView);
    }

    public void onInput(int index, String type, String value) {
        View view = memberContainer.getChildAt(index);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvBackName = view.findViewById(R.id.tv_backname);
        TextView tvCarNumber = view.findViewById(R.id.tv_car_number);
        TextView tvPhoneNumber = view.findViewById(R.id.tv_phone_number);

        if (type.equals("name")) {
            tvName.setText(value);
        } else if (type.equals("car")) {
            tvCarNumber.setText(value);
        } else if (type.equals("phone")) {
            tvPhoneNumber.setText(value);
        }
    }

    //화면에서 조원 순서를 변경한다.
    private void swapView(int a, int b) {
        ArrayList<View> temp = new ArrayList<>();
        for (int i = 0; i <  memberContainer.getChildCount(); i++) {
            temp.add(memberContainer.getChildAt(i));
        }

        memberContainer.removeAllViews();
        Collections.swap(temp, a, b);

        for (int i = 0; i <  temp.size(); i++) {

            String id = (String)temp.get(i).getTag();
            TextView tvName = temp.get(i).findViewById(R.id.tv_name);
            TextView tvBackName = temp.get(i).findViewById(R.id.tv_backname);
            TextView tvCarNumber = temp.get(i).findViewById(R.id.tv_car_number);
            TextView tvPhoneNumber = temp.get(i).findViewById(R.id.tv_phone_number);

            boolean isLast = false;
            if (i == guestInfoArrayList.size()-1)
                isLast = true;

            addView(i, id, tvName.getText().toString(), tvBackName.getText().toString(), tvCarNumber.getText().toString(), tvPhoneNumber.getText().toString(), isLast);
        }
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