package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.adapter.ChatMessageAdapter;
import com.eye3.golfpay.fmb_tab.dialog.ControlShortCutDialog;
import com.eye3.golfpay.fmb_tab.dialog.MenuCategoryDialog;
import com.eye3.golfpay.fmb_tab.model.chat.MemberData;
import com.eye3.golfpay.fmb_tab.model.chat.Message;
import com.eye3.golfpay.fmb_tab.util.Util;
import com.eye3.golfpay.fmb_tab.view.ControlPanelView;

import java.util.ArrayList;


public class ControlFragment extends BaseFragment {


    public class MenuItem {

        public MenuItem(String title, boolean isActive, boolean isDisable) {
            this.title = title; this.isActive = isActive; this.isDisable = isDisable;
        }

        public String title;
        public boolean isActive;
        public boolean isDisable;
        View view;
    }

    protected String TAG = getClass().getSimpleName();
    private ConstraintLayout view_caddie_list;
    private ControlPanelView controlPanelView;
    private LinearLayout ll_space;
    private boolean isCaddieVisible = false;
    private LinearLayout ll_menu;
    private LinearLayout ll_caddie_list;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private ArrayList<String> caddies = new ArrayList<>();

    private ListView messages_view;
    private ChatMessageAdapter chatMessageAdapter;
    private ImageButton send_message;
    private EditText edit_chat;
    private MemberData memberData;
    private TextView tvTo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_control, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        SetTitle("KT WMMS");
//        SetDividerVisibility(false);
     //   setDrawerLayoutEnable(true);

        view_caddie_list = view.findViewById(R.id.view_caddie_list);
        ll_menu = view.findViewById(R.id.ll_menu);
        tvTo = view.findViewById(R.id.tv_to);
        ll_caddie_list = view.findViewById(R.id.ll_caddie_list);
        addMenuItem();
        createCaddieList();


        controlPanelView = view.findViewById(R.id.view_control_panel);
        controlPanelView.init(getContext());
        controlPanelView.setOnClickListener(new ControlPanelView.OnClickListener() {
            @Override
            public void onShortcutMessage(String msg) {
                edit_chat.setText(msg);
            }

            @Override
            public void onShowShortcutDlg() {
                showControlShortDialog();
            }
        });


                edit_chat = view.findViewById(R.id.edit_chat);
        send_message = view.findViewById(R.id.send_message);
        chatMessageAdapter = new ChatMessageAdapter(mContext);

        messages_view = view.findViewById(R.id.messages_view);
        messages_view.setAdapter(chatMessageAdapter);
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = edit_chat.getText().toString();
                memberData = new MemberData(tvTo.getText().toString(), "#434343");

                boolean my = true;
                if (s.indexOf("!") >= 0) {
                    my = false;
                    memberData = new MemberData("상봉이", "#434343");
                }

                Message msg = new Message(edit_chat.getText().toString(), memberData, my);
                chatMessageAdapter.add(msg);
                //messages_view.setSelection(messages_view.getCount() - 1);
                messages_view.smoothScrollToPosition(messages_view.getCount() - 1);
                edit_chat.setText("");
            }
        });

        messages_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view_caddie_list.setVisibility(View.GONE);
                isCaddieVisible = false;
                return false;
            }
        });
    }

    private void showControlShortDialog() {
        ControlShortCutDialog dlg = new ControlShortCutDialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dlg.setListener(new ControlShortCutDialog.IListenerApplyShortcut() {
            @Override
            public void onApplyShortcut(String shortcut) {
                edit_chat.setText(shortcut);
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

        for (MenuItem mi : menuItems) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mi.view = inflater.inflate(R.layout.item_control_menu, null, false);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 92);
            mi.view.setLayoutParams(lllp);

            TextView tvTitle = mi.view.findViewById(R.id.tv_title);
            View bar = mi.view.findViewById(R.id.view_bar);
            ImageView checkIn = mi.view.findViewById(R.id.iv_checkin);
            tvTitle.setText(mi.title);

            if (mi.isDisable == true) {
                tvTitle.setTextAppearance(getContext(), R.style.GlobalTextView_18SP_martini_NotoSans_Medium);
                bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.martini));
                checkIn.setVisibility(View.GONE);
            }

            mi.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mi.isDisable == true) {
                        return;
                    }

                    if (mi.title.equals("캐디선택")) {

                        if (isCaddieVisible == false)
                            view_caddie_list.setVisibility(View.VISIBLE);
                        else
                            view_caddie_list.setVisibility(View.GONE);

                        isCaddieVisible ^= true;
                        return;
                    }

                    unSelectAll();
                    tvTitle.setTextAppearance(getContext(), R.style.GlobalTextView_18SP_irisBlue_NotoSans_Medium);
                    bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.irisBlue));
                    checkIn.setVisibility(View.VISIBLE);
                    mi.isActive = true;

                    tvTo.setText("To." + mi.title);
                }
            });

            ll_menu.addView(mi.view);
        }

        menuItems.get(0).view.performClick();
    }

    private void unSelectAll() {
        for (MenuItem mi : menuItems) {

            if (mi.isDisable == true) {
                continue;
            }

            TextView tvTitle = mi.view.findViewById(R.id.tv_title);
            View bar = mi.view.findViewById(R.id.view_bar);
            ImageView checkIn = mi.view.findViewById(R.id.iv_checkin);
            ImageView iv_arrow = mi.view.findViewById(R.id.iv_arrow);

            tvTitle.setTextAppearance(getContext(), R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Medium);
            bar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ebonyBlack));
            checkIn.setVisibility(View.GONE);
            mi.isActive = false;

            if (mi.title.equals("캐디선택")) {
                iv_arrow.setVisibility(View.VISIBLE);
            }
        }
    }

    private void createCaddieList() {

        caddies.add("1.유재석");
        caddies.add("2.조세호");
        caddies.add("3.강호동");
        caddies.add("4.이수근");
        caddies.add("5.김희철");
        caddies.add("6.박나래");
        caddies.add("7.장도연");
        caddies.add("8.이상민");
        caddies.add("9.기안84");
        caddies.add("10.헨리");
        caddies.add("11.이시언");
        caddies.add("12.전현무");
        caddies.add("13.한혜진");
        caddies.add("14.샛별이");
        caddies.add("15.점장님");
        caddies.add("16.은별이");
        caddies.add("16.한선화");
        caddies.add("17.공승연");
        caddies.add("18.사나");
        caddies.add("19.다현");
        caddies.add("20.혜리");
        caddies.add("21.유라");
        caddies.add("22.줄리엔강");
        caddies.add("23.장혁");
        caddies.add("24.수현");
        caddies.add("25.이하이");
        caddies.add("26.이소라");
        caddies.add("27.하림");
        caddies.add("28.소향");
        caddies.add("29.정승환");
        caddies.add("30.아이유");

        for (int i = 0; i < caddies.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_control_caddie, null, false);

            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 92);
            view.setLayoutParams(lllp);

            TextView tvTitle = view.findViewById(R.id.tv_title);
            tvTitle.setText(caddies.get(i));

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), tvTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                    tvTo.setText("To." + tvTitle.getText().toString());
                    view_caddie_list.setVisibility(View.GONE);
                    isCaddieVisible = false;
                }
            });

            TextView tvTitle2 = view.findViewById(R.id.tv_title2);
            if (i+1 < caddies.size()) {
                tvTitle2.setText(caddies.get(++i));

                tvTitle2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), tvTitle2.getText().toString(), Toast.LENGTH_SHORT).show();
                        tvTo.setText("To." + tvTitle2.getText().toString());
                        view_caddie_list.setVisibility(View.GONE);
                        isCaddieVisible = false;
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

}


