package com.eye3.golfpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.GuestSettingActivity;
import com.eye3.golfpay.adapter.ClubAdapter;
import com.eye3.golfpay.adapter.ClubGuestListAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.fragment.BaseFragment;
import com.eye3.golfpay.model.caddyNote.CaddyNoteInfo;
import com.eye3.golfpay.model.guest.CaddieInfo;
import com.eye3.golfpay.model.guest.ClubInfo;

/**
 * Created by Administrator on 2017-09-22.
 */

public class ClubInfoDialog extends Dialog {

    BaseFragment baseFragment;
    private IListenerDialog iListenerDialog;
    private RecyclerView guestsRecyclerView;
    private RecyclerView woodRecyclerView;
    private RecyclerView utiltyRecyclerView;
    private RecyclerView ironRecyclerView, ironRecyclerView2;
    private RecyclerView wedgeRecyclerView;
    private RecyclerView putterRecyclerView;
    private TextView tv_woodCount, tv_utilityCount,tv_ironCount, tv_wedgeCount, tv_putterCount;
    private TextView tv_putterCoverCount, tv_woodCoverCount, tv_utilityCoverCount, tv_wedgeCoverCount, tv_ironCoverCount;
    private TextView tv_woodMemo, tv_utilityMemo, tv_ironMemo, tv_wedgeMemo, tv_putterMemo;
    private ImageView tv_cancel;
    private LinearLayout tv_save;
    private CaddieInfo caddieInfo;
    private int currentIdx;
    private int startIdx;
    String[] wood = {"1", "2", "3", "4", "5", "7", "9"};
    String[] utility = {"3", "4", "5", "7", "9", "11"};
    String[] iron = {"3", "4", "5", "6", "7", "8", "9"};
    String[] iron2 = {"10", "11", "12"};
    String[] putter = {"1", "2"};
    String[] wedge = {"P", "A", "G", "S"};

    public enum ClubType {
        eWood,
        eUtility,
        eIron,
        eIron2,
        eWedge,
        ePutter,
    }

    public interface  IListenerDialog {
        void onSave(String guestId, ClubInfo clubInfo);
    }

    public ClubInfoDialog() {
        super(null);
    }

    public ClubInfoDialog(Context context, BaseFragment fragment, CaddieInfo caddieInfo, int idx)
    {
        super(context);
        this.caddieInfo = caddieInfo;
        this.baseFragment = fragment;
        this.startIdx = idx;
        currentIdx = startIdx;
    }

    public ClubInfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setData(String imgscr, String target, String idx) {
        setContentView(R.layout.fr_golf_club_list);
    }

    public void setiListenerDialog(IListenerDialog iListenerDialog) {
        this.iListenerDialog = iListenerDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fr_golf_club_list);
        guestsRecyclerView = findViewById(R.id.rv_guests);
        woodRecyclerView = findViewById(R.id.woodRecyclerView);
        utiltyRecyclerView = findViewById(R.id.utiltyRecyclerView);
        ironRecyclerView = findViewById(R.id.ironRecyclerView);
        ironRecyclerView2 = findViewById(R.id.ironRecyclerView2);
        wedgeRecyclerView = findViewById(R.id.wedgeRecyclerView);
        putterRecyclerView = findViewById(R.id.putterRecyclerView);

        tv_save = findViewById(R.id.btn_save);
        tv_cancel = findViewById(R.id.btn_cancel);
        tv_woodCount = findViewById(R.id.tv_woodCount);
        tv_utilityCount = findViewById(R.id.tv_utilityCount);
        tv_ironCount = findViewById(R.id.tv_ironCount);
        tv_wedgeCount = findViewById(R.id.tv_wedgeCount);
        tv_putterCount = findViewById(R.id.tv_putterCount);

        tv_putterCoverCount = findViewById(R.id.tv_putterCoverCount);
        tv_woodCoverCount = findViewById(R.id.tv_woodCoverCount);
        tv_utilityCoverCount = findViewById(R.id.tv_utilityCoverCount);
        tv_ironCoverCount = findViewById(R.id.tv_ironCoverCount);
        tv_wedgeCoverCount = findViewById(R.id.tv_wedgeCoverCount);

        tv_woodMemo = findViewById(R.id.tv_wood_memo);
        tv_utilityMemo = findViewById(R.id.tv_utility_memo);
        tv_ironMemo = findViewById(R.id.tv_iron_memo);
        tv_wedgeMemo = findViewById(R.id.tv_wedge_memo);
        tv_putterMemo= findViewById(R.id.tv_putter_memo);

        findViewById(R.id.view_wood_memo).setOnClickListener(this::onMemoClick);
        findViewById(R.id.view_utility_memo).setOnClickListener(this::onMemoClick);
        findViewById(R.id.view_iron_memo).setOnClickListener(this::onMemoClick);
        findViewById(R.id.view_wedge_memo).setOnClickListener(this::onMemoClick);
        findViewById(R.id.view_putter_memo).setOnClickListener(this::onMemoClick);

        initClubInfoUI();

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iListenerDialog == null)
                    return;

                ClubInfo clubInfo = getClubInfo();
                caddieInfo.getGuestInfo().get(currentIdx).setClubInfo(clubInfo);

                for (int i = 0; i < caddieInfo.getGuestInfo().size(); i++) {
                    iListenerDialog.onSave(caddieInfo.getGuestInfo().get(i).getReserveGuestId(), caddieInfo.getGuestInfo().get(i).getClubInfo());
                }

                dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        createGuestList();
    }

    public void onMemoClick(View view){

        String text = "";
        switch (view.getId()) {
            case R.id.view_wood_memo : {

                text = tv_woodMemo.getText().toString();
                break;
            }
            case R.id.view_utility_memo : {
                text = tv_utilityMemo.getText().toString();
                break;
            }
            case R.id.view_iron_memo : {
                text = tv_ironMemo.getText().toString();
                break;
            }
            case R.id.view_wedge_memo : {
                text = tv_wedgeMemo.getText().toString();
                break;
            }
            case R.id.view_putter_memo : {
                text = tv_putterMemo.getText().toString();
                break;
            }
        }

        Intent intent = new Intent(getContext(), GuestSettingActivity.class);
        intent.putExtra("type", "club");
        intent.putExtra("id", view.getId());
        intent.putExtra("value", text);
        baseFragment.startActivityForResult(intent, GuestSettingActivity.Id+20);
    }

    private void initClubInfoUI() {
        ClubInfo clubInfo = caddieInfo.getGuestInfo().get(currentIdx).clubInfo;
        initRecyclerViews(woodRecyclerView, wood, true, clubInfo, ClubType.eWood);
        initRecyclerViews(utiltyRecyclerView, utility, true, clubInfo, ClubType.eUtility);
        initRecyclerViews(ironRecyclerView, iron, true, clubInfo, ClubType.eIron);
        initRecyclerViews(ironRecyclerView2, iron2, true, clubInfo, ClubType.eIron2);
        initRecyclerViews(putterRecyclerView, putter, false, clubInfo, ClubType.ePutter);
        initRecyclerViews(wedgeRecyclerView, wedge, true, clubInfo, ClubType.eWedge);
    }

    private ClubInfo getClubInfo() {

        ClubAdapter adapter = (ClubAdapter)woodRecyclerView.getAdapter();
        ClubInfo clubInfo = new ClubInfo();
        clubInfo.setWood(adapter.getSelectedItems());

        adapter = (ClubAdapter)utiltyRecyclerView.getAdapter();
        clubInfo.setUtility(adapter.getSelectedItems());

        adapter = (ClubAdapter)ironRecyclerView.getAdapter();
        clubInfo.setIron(adapter.getSelectedItems());

        adapter = (ClubAdapter)ironRecyclerView2.getAdapter();
        clubInfo.addIron(adapter.getSelectedItems());

        adapter = (ClubAdapter)wedgeRecyclerView.getAdapter();
        clubInfo.setWedge(adapter.getSelectedItems());

        adapter = (ClubAdapter)putterRecyclerView.getAdapter();
        clubInfo.setPutter(adapter.getSelectedItems());

        clubInfo.wood_memo = tv_woodMemo.getText().toString();
        clubInfo.utility_memo = tv_utilityMemo.getText().toString();
        clubInfo.iron_memo = tv_ironMemo.getText().toString();
        clubInfo.wedge_memo = tv_wedgeMemo.getText().toString();
        clubInfo.putter_memo = tv_putterMemo.getText().toString();

        return clubInfo;
    }

    private void createGuestList() {

        LinearLayoutManager mManager;

        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        guestsRecyclerView.setLayoutManager(mManager);

        ClubGuestListAdapter adapter = new ClubGuestListAdapter(getContext(), new ClubGuestListAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(Integer id) {
                caddieInfo.getGuestInfo().get(currentIdx).clubInfo = getClubInfo();
                currentIdx = id;
                initClubInfoUI();
            }
        });

        guestsRecyclerView.setAdapter(adapter);
        adapter.setData(Global.guestList);
        adapter.select(startIdx);
    }

    private void initRecyclerViews(RecyclerView recyclerView, String[] items, boolean isMultiSelect, ClubInfo clubInfo, ClubType clubType) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ClubAdapter adapter = new ClubAdapter(getContext(), clubType, isMultiSelect, new ClubAdapter.IOnClickAdapter() {

            @Override
            public void onAdapterItemClicked(ClubType clubType, int count, int coverCount) {
                switch (clubType) {
                    case eWood: {
                        tv_woodCount.setText(count + "개");
                        tv_woodCoverCount.setText(String.format("(커버 %d개)", coverCount));
                    } break;
                    case eUtility: {
                        tv_utilityCount.setText(count + "개");
                        tv_utilityCoverCount.setText(String.format("(커버 %d개)", coverCount));
                    } break;
                    case eIron:
                    case eIron2:
                    {
                        ClubAdapter clubAdapter = (ClubAdapter)ironRecyclerView.getAdapter();
                        ClubAdapter clubAdapter2 = (ClubAdapter)ironRecyclerView2.getAdapter();
                        count = clubAdapter.getSelectedCount() + clubAdapter2.getSelectedCount();
                        coverCount = clubAdapter.getSelectedCoverCount() + clubAdapter2.getSelectedCoverCount();
                        tv_ironCount.setText(count + "개");
                        tv_ironCoverCount.setText(String.format("(커버 %d개)", coverCount));
                    } break;
                    case eWedge: {
                        tv_wedgeCount.setText(count + "개");
                        tv_wedgeCoverCount.setText(String.format("(커버 %d개)", coverCount));
                    } break;
                    case ePutter: {
                        tv_putterCount.setText(count + "개");
                        tv_putterCoverCount.setText(String.format("(커버 %d개)", coverCount));
                    } break;
                }
            }
        });
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        linearLayoutManager.scrollToPositionWithOffset(0, 0);

        for (String item: items) {
            adapter.addItem(item);
        }

        if (clubInfo != null) {
            switch (clubType) {
                case eWood:
                    adapter.setSelectItem(clubInfo.getWood());
                    break;
                case eUtility:
                    adapter.setSelectItem(clubInfo.getUtility());
                    break;
                case eIron:
                    clubInfo.getIron2().clear();
                    for (CaddyNoteInfo.ClubInfo ci: clubInfo.iron) {
                        if (ci.club.equals("10") || ci.club.equals("11") || ci.club.equals("12")) {
                            int club2 = Integer.parseInt(ci.club)-10;
                            ci.club = Integer.toString(club2);
                            clubInfo.getIron2().add(ci);
                            clubInfo.iron.remove(ci);
                        }
                    }

                    adapter.setSelectItem(clubInfo.getIron());

                    break;
                case eIron2:
                    adapter.setSelectItem(clubInfo.getIron2());
                    break;
                case eWedge:
                    adapter.setSelectItem(clubInfo.getWedge());
                    break;
                case ePutter:
                    adapter.setSelectItem(clubInfo.getPutter());
                    break;
            }
        }

        selectCount(recyclerView, clubType);
    }

    private void selectCount(RecyclerView recyclerView, ClubType clubType) {

        ClubAdapter adapter = (ClubAdapter)recyclerView.getAdapter();
        switch (clubType) {
            case eWood:
                tv_woodCount.setText(adapter.getSelectedItems().size() + "개");
                tv_woodCoverCount.setText(String.format("(커버 %d개)", adapter.getSelectedCoverCount()));
                break;
            case eUtility:
                tv_utilityCount.setText(adapter.getSelectedItems().size() + "개");
                tv_utilityCoverCount.setText(String.format("(커버 %d개)", adapter.getSelectedCoverCount()));
                break;
            case eIron2:
                ClubAdapter clubAdapter = (ClubAdapter)ironRecyclerView.getAdapter();
                ClubAdapter clubAdapter2 = (ClubAdapter)ironRecyclerView2.getAdapter();
                int count = clubAdapter.getSelectedCount() + clubAdapter2.getSelectedCount();
                int coverCount = clubAdapter.getSelectedCoverCount() + clubAdapter2.getSelectedCoverCount();
                tv_ironCount.setText(count + "개");
                tv_ironCoverCount.setText(String.format("(커버 %d개)", coverCount));
                break;
            case eWedge:
                tv_wedgeCount.setText(adapter.getSelectedItems().size() + "개");
                tv_wedgeCoverCount.setText(String.format("(커버 %d개)", adapter.getSelectedCoverCount()));
                break;
            case ePutter:
                tv_putterCount.setText(adapter.getSelectedItems().size() + "개");
                tv_putterCoverCount.setText(String.format("(커버 %d개)", adapter.getSelectedCoverCount()));
                break;
        }
    }

    public void onInputMemo(int index, String value, int id) {

        switch (id) {
            case R.id.view_wood_memo : {
                tv_woodMemo.setText(value);
                caddieInfo.getGuestInfo().get(currentIdx).getClubInfo().wood_memo = value;
                break;
            }
            case R.id.view_utility_memo : {
                tv_utilityMemo.setText(value);
                caddieInfo.getGuestInfo().get(currentIdx).getClubInfo().utility_memo = value;
                break;
            }
            case R.id.view_iron_memo : {
                tv_ironMemo.setText(value);
                caddieInfo.getGuestInfo().get(currentIdx).getClubInfo().iron_memo = value;
                break;
            }
            case R.id.view_wedge_memo : {
                tv_wedgeMemo.setText(value);
                caddieInfo.getGuestInfo().get(currentIdx).getClubInfo().wedge_memo = value;
                break;
            }
            case R.id.view_putter_memo : {
                tv_putterMemo.setText(value);
                caddieInfo.getGuestInfo().get(currentIdx).getClubInfo().putter_memo = value;
                break;
            }
        }
    }
}