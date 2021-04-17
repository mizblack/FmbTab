package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.ChatMessageAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.dialog.ControlShortCutDialog;
import com.eye3.golfpay.model.chat.ChatData;
import com.eye3.golfpay.model.chat.MemberData;
import com.eye3.golfpay.model.chat.Message;
import com.eye3.golfpay.model.chat.ResponseChatMsg;
import com.eye3.golfpay.model.control.ChatHotKey;
import com.eye3.golfpay.model.control.ChatHotKeyItem;
import com.eye3.golfpay.model.control.ChatHotKeyOption;
import com.eye3.golfpay.model.info.BasicInfo;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseChatNameList;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.Util;
import com.eye3.golfpay.view.ControlPanelView;

import java.util.ArrayList;
import java.util.Objects;


public class ControlFragment extends BaseFragment {


    public class MenuItem {

        public MenuItem(String title, boolean isActive, boolean isDisable) {
            this.title = title;
            this.isActive = isActive;
            this.isDisable = isDisable;
        }

        public String title;
        public boolean isActive;
        public boolean isDisable;
        View view;
    }

    private View mView;
    protected String TAG = getClass().getSimpleName();
    private ConstraintLayout view_caddie_list;
    private ConstraintLayout view_group_list;
    private ControlPanelView controlPanelView;
    private LinearLayout ll_space;
    private boolean isCaddieVisible = false;
    private boolean isGroupVisible = false;
    private LinearLayout ll_menu;
    private LinearLayout ll_caddie_list;
    private LinearLayout ll_group_list;
    private TextView tvEmergencyMessage;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private ArrayList<BasicInfo> caddies = new ArrayList<>();
    private ArrayList<BasicInfo> groupList = new ArrayList<>();

    private ListView messages_view;
    private ChatMessageAdapter chatMessageAdapter;
    private ImageButton send_message;
    private EditText edit_chat;
    private MemberData memberData;
    private final int itemHeight = 92;
    private String to;
    private boolean emergencyOn = false;
    private BasicInfo selectedCaddy = null;
    private BasicInfo selectedGroup = null;

    private ChatMode chatMode = ChatMode.eAll;
    public enum ChatMode {
        eMonitor,
        eAll,
        eBefore,
        eAfter,
        eCaddy,
        eGroup
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //itemHeight = 42;
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_control, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        SetTitle("KT WMMS");
//        SetDividerVisibility(false);
        //   setDrawerLayoutEnable(true);

        mView = view;
        view_caddie_list = view.findViewById(R.id.view_caddie_list);
        view_group_list = view.findViewById(R.id.view_group_list);
        ll_menu = view.findViewById(R.id.ll_menu);
        ll_caddie_list = view.findViewById(R.id.ll_caddie_list);
        ll_group_list = view.findViewById(R.id.ll_group_list);
        tvEmergencyMessage = view.findViewById(R.id.tv_emergency_msg);
        addMenuItem();
        tabletChattingNameList();
        requestHotKeyList();


        edit_chat = view.findViewById(R.id.edit_chat);
        send_message = view.findViewById(R.id.send_message);
        chatMessageAdapter = new ChatMessageAdapter(mContext);

        messages_view = view.findViewById(R.id.messages_view);
        messages_view.setAdapter(chatMessageAdapter);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        messages_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view_caddie_list.setVisibility(View.GONE);
                view_group_list.setVisibility(View.GONE);
                isCaddieVisible = false;
                isGroupVisible = false;

                closeKeyboard(edit_chat);
                //edit_chat.setEnabled(true);
                return false;
            }
        });

        tvEmergencyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergencyOn ^= true;

                if (emergencyOn) {
                    tvEmergencyMessage.setTextAppearance(R.style.GlobalTextView_Emergency_on);
                    tvEmergencyMessage.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.shape_emergency_on_bg));
                } else {
                    tvEmergencyMessage.setTextAppearance(R.style.GlobalTextView_Emergency_off);
                    tvEmergencyMessage.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.shape_emergency_off_bg));
                }
            }
        });

        String day = Util.getDay(System.currentTimeMillis(), "yyyy-MM-dd");
        initSetMessage(getContext(), 1, Global.CaddyNo, day);
    }

    private void showControlShortDialog(ArrayList<ChatHotKeyOption> getOptions) {
        ControlShortCutDialog dlg = new ControlShortCutDialog(getContext(), getOptions, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dlg.setListener(new ControlShortCutDialog.IListenerApplyShortcut() {
            @Override
            public void onApplyShortcut(String shortcut) {
                edit_chat.setText(shortcut);
            }

            @Override
            public void onClose() {
                edit_chat.setText("");
                controlPanelView.unSelectAll();
            }
        });

        //dlg.setData(mRestaurantList.get(mSelectedRestaurantTabIdx).categoryList);
        dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
        dlg.getWindow().
                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dlg.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void addMenuItem() {

        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("통제소", true, false));
        menuItems.add(new MenuItem("전체", true, false));
        menuItems.add(new MenuItem("전반코스", true, false));
        menuItems.add(new MenuItem("후반코스", true, false));
        menuItems.add(new MenuItem("앞카트", true, true));
        menuItems.add(new MenuItem("뒤카트", true, true));
        menuItems.add(new MenuItem("캐디선택", true, false));
        menuItems.add(new MenuItem("그룹선택", true, false));

        for (MenuItem mi : menuItems) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mi.view = inflater.inflate(R.layout.item_control_menu, null, false);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
            mi.view.setLayoutParams(lllp);

            TextView tvTitle = mi.view.findViewById(R.id.tv_title);
            View bar = mi.view.findViewById(R.id.view_bar);
            ImageView checkIn = mi.view.findViewById(R.id.iv_checkin);
            tvTitle.setText(mi.title);

            if (mi.isDisable) {
                tvTitle.setTextAppearance(R.style.GlobalTextView_18SP_martini_NotoSans_Medium);
                bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.martini));
                checkIn.setVisibility(View.GONE);
            }

            mi.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    unSelectAll();

                    if (mi.isDisable) {
                        return;
                    }

                    if (mi.title.equals("통제소")) {

                        to = "To." + mi.title;
                        chatMode = ChatMode.eMonitor;
                    }

                    else if (mi.title.equals("전체")) {

                        to = "To." + mi.title;
                        chatMode = ChatMode.eAll;
                    }

                    else if (mi.title.equals("캐디선택")) {

                        if (!isCaddieVisible) {
                            view_caddie_list.setVisibility(View.VISIBLE);
                            view_group_list.setVisibility(View.GONE);
                        }
                        else
                            view_caddie_list.setVisibility(View.GONE);

                        isCaddieVisible ^= true;
                        chatMode = ChatMode.eCaddy;
                        closeKeyboard(edit_chat);
                        return;
                    }

                    else if (mi.title.equals("그룹선택")) {

                        if (!isGroupVisible) {
                            view_group_list.setVisibility(View.VISIBLE);
                            view_caddie_list.setVisibility(View.GONE);
                        }
                        else {
                            view_group_list.setVisibility(View.GONE);
                        }

                        isGroupVisible ^= true;
                        chatMode = ChatMode.eGroup;
                        closeKeyboard(edit_chat);
                        return;
                    }

                    else if (mi.title.equals("전반코스")) {
                        String course_id = Global.CurrentCourseId;
                        if (course_id == null)
                            return;

                        to = "To." + mi.title;
                        chatMode = ChatMode.eBefore;

                    }
                    else if (mi.title.equals("후반코스")) {
                        String course_id = Global.CurrentCourseId;
                        if (course_id == null)
                            return;

                        to = "To." + mi.title;
                        chatMode = ChatMode.eAfter;
                    }

                    tvTitle.setTextAppearance(R.style.GlobalTextView_18SP_irisBlue_NotoSans_Medium);
                    bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.irisBlue));
                    checkIn.setVisibility(View.VISIBLE);
                    mi.isActive = true;
                    to = "To." + tvTitle.getText().toString();
                }
            });

            ll_menu.addView(mi.view);
        }

        menuItems.get(0).view.performClick();
    }

    private void unSelectAll() {
        for (MenuItem mi : menuItems) {

            if (mi.isDisable) {
                continue;
            }

            TextView tvTitle = mi.view.findViewById(R.id.tv_title);
            View bar = mi.view.findViewById(R.id.view_bar);
            ImageView checkIn = mi.view.findViewById(R.id.iv_checkin);
            ImageView iv_arrow = mi.view.findViewById(R.id.iv_arrow);

            tvTitle.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Medium);
            bar.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.ebonyBlack));
            checkIn.setVisibility(View.GONE);
            mi.isActive = false;

            if (mi.title.equals("캐디선택")) {
                iv_arrow.setVisibility(View.VISIBLE);
                tvTitle.setText("캐디선택");
            }

            if (mi.title.equals("그룹선택")) {
                iv_arrow.setVisibility(View.VISIBLE);
                tvTitle.setText("그룹선택");
            }
        }
    }

    private void setToCaddie() {

        unSelectAll();

        for (MenuItem mi : menuItems) {

            if (mi.title.equals("캐디선택")) {
                TextView tvTitle = mi.view.findViewById(R.id.tv_title);
                tvTitle.setText(String.format("캐디선택(%s)", selectedCaddy.getName()));
                to = "To." + selectedCaddy.getName();
                View bar = mi.view.findViewById(R.id.view_bar);
                tvTitle.setTextAppearance(R.style.GlobalTextView_18SP_irisBlue_NotoSans_Medium);
                bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.irisBlue));
            }
        }

        selectedGroup = null;
    }

    private void setToGroup() {

        unSelectAll();

        for (MenuItem mi : menuItems) {

            if (mi.title.equals("그룹선택")) {
                TextView tvTitle = mi.view.findViewById(R.id.tv_title);
                tvTitle.setText(String.format("그룹선택(%s)", selectedGroup.getName()));
                to = "To." + selectedGroup.getName();
                View bar = mi.view.findViewById(R.id.view_bar);
                tvTitle.setTextAppearance(R.style.GlobalTextView_18SP_irisBlue_NotoSans_Medium);
                bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.irisBlue));
            }
        }

        selectedCaddy = null;
    }

    private void createCaddieList() {

        for (int i = 0; i < caddies.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_control_caddie, null, false);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
            view.setLayoutParams(lllp);

            TextView tvTitle = view.findViewById(R.id.tv_title);
            BasicInfo basicInfo = caddies.get(i);
            tvTitle.setText(basicInfo.getId() + "." + basicInfo.getName());
            tvTitle.setTag(basicInfo.getId());

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedCaddy = findSelectedCaddy((String)tvTitle.getTag());
                    view_caddie_list.setVisibility(View.GONE);
                    isCaddieVisible = false;
                    setToCaddie();
                }
            });

            TextView tvTitle2 = view.findViewById(R.id.tv_title2);
            if (i + 1 < caddies.size()) {
                basicInfo = caddies.get(++i);
                tvTitle2.setText(basicInfo.getId() + "." + basicInfo.getName());
                tvTitle2.setTag(basicInfo.getId());
                tvTitle2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectedCaddy = findSelectedCaddy((String)tvTitle2.getTag());
                        view_caddie_list.setVisibility(View.GONE);
                        isCaddieVisible = false;
                        setToCaddie();
                    }
                });
            } else {
                tvTitle2.setText("");
                View bar = view.findViewById(R.id.view_bar2);
                bar.setVisibility(View.GONE);
            }

            ll_caddie_list.addView(view);
        }
    }

    private void createGroupList() {

        for (int i = 0; i < groupList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_control_caddie, null, false);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
            view.setLayoutParams(lllp);

            TextView tvTitle = view.findViewById(R.id.tv_title);
            BasicInfo basicInfo = groupList.get(i);
            tvTitle.setText(basicInfo.getId() + "." + basicInfo.getName());
            tvTitle.setTag(basicInfo.getId());

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedGroup = findSelectedGroup((String)tvTitle.getTag());
                    view_group_list.setVisibility(View.GONE);
                    isGroupVisible = false;
                    setToGroup();
                }
            });

            TextView tvTitle2 = view.findViewById(R.id.tv_title2);
            if (i + 1 < groupList.size()) {
                basicInfo = groupList.get(++i);
                tvTitle2.setText(basicInfo.getId() + "." + basicInfo.getName());
                tvTitle2.setTag(basicInfo.getId());
                tvTitle2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedGroup = findSelectedGroup((String)tvTitle2.getTag());
                        view_group_list.setVisibility(View.GONE);
                        isGroupVisible = false;
                        setToGroup();
                    }
                });
            } else {
                tvTitle2.setText("");
                View bar = view.findViewById(R.id.view_bar2);
                bar.setVisibility(View.GONE);
            }

            ll_group_list.addView(view);
        }
    }

    private BasicInfo findSelectedCaddy(String id) {
        for (BasicInfo caddie : caddies) {
            if (caddie.getId().equals(id))
                return caddie;
        }

        return null;
    }

    private BasicInfo findSelectedGroup(String id) {
        for (BasicInfo group : groupList) {
            if (group.getId().equals(id))
                return group;
        }

        return null;
    }

    public void requestHotKeyList() {

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).requestHotKeyList(new DataInterface.ResponseCallback<ChatHotKey>() {
            @Override
            public void onSuccess(ChatHotKey response) {
                if (response == null) {
                    Log.d(TAG, "requestHotKeyList is null");
                    return;
                }

                controlPanelView = mView.findViewById(R.id.view_control_panel);
                controlPanelView.init(getContext());
                controlPanelView.createHotkeyHeader(response.getHeader());
                controlPanelView.createHotKey(response.getBody());
                controlPanelView.setOnClickListener(new ControlPanelView.OnClickListener() {
                    @Override
                    public void onShortcutMessage(String msg) {
                        edit_chat.setText(msg);
                    }

                    @Override
                    public void onShowShortcutDlg(String option) {
                        ArrayList<ChatHotKeyOption> getOptions = getOptions(option, response.getOptions());
                        showControlShortDialog(getOptions);
                    }
                });
            }

            @Override
            public void onError(ChatHotKey response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private ArrayList<ChatHotKeyOption> getOptions(String option, ArrayList<ChatHotKeyOption> chatHotKeyOptions) {
        ArrayList<ChatHotKeyOption> options = new ArrayList<>();

        String[] split = option.split(",");

        for (String str : split) {
            options.add(findHotkeyOption(str, chatHotKeyOptions));
        }

        return options;
    }

    private ChatHotKeyOption findHotkeyOption(String option, ArrayList<ChatHotKeyOption> chatHotKeyOptions) {
        for (ChatHotKeyOption item : chatHotKeyOptions) {
            if (item.getTitle().equals(option)) {
                return item;
            }
        }

        return null;
    }

    private void sendMessage() {

        if (edit_chat.getText().toString().isEmpty()) {
            return;
        }

        final String chatMessage = edit_chat.getText().toString();
        ChatData chatData = new ChatData();
        chatData.type = getChatType();
        if (chatData.type.equals("course")) {
            chatData.course_id = getReceiverId();
        }
        chatData.receiver_id = getReceiverId();
        chatData.receiver_name = to;
        chatData.message = chatMessage;
        chatData.sender_id = Global.CaddyNo;
        chatData.sender_name = Global.caddieName;
        closeKeyboard(edit_chat);
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).sendChatMessage(chatData,
                new DataInterface.ResponseCallback<ResponseChatMsg>() {
            @Override
            public void onSuccess(ResponseChatMsg response) {
                if (response == null) {
                    Log.d(TAG, "ResponseChatMsg is null");
                    return;
                }

                memberData = new MemberData(to, "#434343");

                //boolean my = true;
//                if (chatMessage.indexOf("!") >= 0) {
//                    my = false;
//                    memberData = new MemberData("상봉이", "#434343");
//                }

                Message msg = new Message(response.getMsg(), memberData, response.getTimestamp(), true, emergencyOn);
                chatMessageAdapter.add(msg);
                //messages_view.setSelection(messages_view.getCount() - 1);
                messages_view.smoothScrollToPosition(messages_view.getCount() - 1);
                edit_chat.setText("");
            }

            @Override
            public void onError(ResponseChatMsg response) {
                Toast.makeText(getContext(), "메시지 전송 오류", Toast.LENGTH_SHORT).show();
                edit_chat.setText("");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private String getChatType() {

        switch (chatMode) {
            case eMonitor: return "monitor";
            case eAll: return "all";
            case eCaddy: return "caddy";
            case eGroup: return "group";
            case eBefore: return "course";
            case eAfter: return "course";
        }

        return "all";
    }

    private String getReceiverId() {

        switch (chatMode) {
            case eCaddy: return selectedCaddy.getId();
            case eGroup: return selectedGroup.getId();
            case eBefore: return Global.courseInfoList.get(0).id;
            case eAfter: return Global.courseInfoList.get(1).id;
        }

        return "";
    }

    public void receiveMessage(String sender, String message, long timestamp) {

        if (Global.CaddyNo.equals(sender))
            return;

        memberData = new MemberData(sender, "#434343");
        Message msg = new Message(message, memberData, timestamp, false, false);
        chatMessageAdapter.add(msg);
        //messages_view.setSelection(messages_view.getCount() - 1);
        messages_view.smoothScrollToPosition(messages_view.getCount() - 1);
        edit_chat.setText("");
    }

    private void initSetMessage(Context context, int cc_id, String sender_id, String date) {

        DataInterface.getInstance().initSetMessage(context, cc_id, sender_id, date, new DataInterface.ResponseCallback<ResponseData<ChatData>>() {
            @Override
            public void onSuccess(ResponseData<ChatData> response) {
                 for (ChatData cd: response.getList()) {

                     boolean itsMe = cd.sender_id.equals(Global.CaddyNo);
                     if (itsMe) {
                         memberData = new MemberData(cd.receiver_name, "#434343");
                     } else {
                         memberData = new MemberData(cd.sender_name, "#434343");
                     }

                     Message msg = new Message(cd.message, memberData, cd.timestamp, itsMe, false);
                     chatMessageAdapter.add(msg);
                 }

                chatMessageAdapter.notifyDataSetChanged();
                messages_view.setSelection(messages_view.getCount() - 1);
            }

            @Override
            public void onError(ResponseData<ChatData> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void tabletChattingNameList() {

        DataInterface.getInstance().tabletChattingNameList(getContext(), new DataInterface.ResponseCallback<ResponseChatNameList>() {
            @Override
            public void onSuccess(ResponseChatNameList response) {

                if (response.getList() == null)
                    return;

                for (BasicInfo caddy : response.getList().caddy_list) {
                    caddies.add(caddy);
                }
                for (BasicInfo group : response.getList().group_list) {
                    groupList.add(group);
                }

                createCaddieList();
                createGroupList();
            }

            @Override
            public void onError(ResponseChatNameList response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}


