package com.eye3.golfpay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.ClubAdapter;
import com.eye3.golfpay.adapter.ClubGuestListAdapter;
import com.eye3.golfpay.adapter.NearestLongestAdapter;
import com.eye3.golfpay.adapter.RestaurantListAdapter;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.guest.CaddieInfo;
import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.order.StoreOrder;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-09-22.
 */

public class ClubInfoDialog extends Dialog {

    private IListenerDialog iListenerDialog;
    private RecyclerView guestsRecyclerView;
    private RecyclerView woodRecyclerView;
    private RecyclerView utiltyRecyclerView;
    private RecyclerView ironRecyclerView;
    private RecyclerView wedgeRecyclerView;
    private RecyclerView putterRecyclerView;
    private RecyclerView putterCoverRecyclerView;
    private RecyclerView woodCoverRecyclerView;
    private RecyclerView etcCoverRecyclerView;
    private TextView tv_woodCount, tv_utilityCount,tv_ironCount, tv_wedgeCount, tv_putterCount, tv_putterCoverCount, tv_woodCoverCount, tv_etcCoverCount;
    private TextView tv_save, tv_cancel;
    private CaddieInfo caddieInfo;
    private int currentIdx;
    private int startIdx;
    String[] wood = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] utility = {"1", "2", "3", "4", "5", "6", "7"};
    String[] iron = {"3", "4", "5", "6", "7", "8", "9", "10", "11"};
    String[] putter = {"0개", "1개", "2개", "3개"};
    String[] wedge = {"Pw", "Sw", "PSw", "52w", "53w", "54w", "55w", "56w", "57w", "58w", "60w"};
    String[] putterCover = {"0개", "1개", "2개", "3개", "4개", "5개"};
    String[] woodCover = {"0개", "1개", "2개", "3개", "4개", "5개", "6개", "7개"};
    String[] etcCover = {"0개", "1개", "2개", "3개", "4개", "5개"};

    public enum ClubType {
        eWood,
        eUtility,
        eIron,
        eWedge,
        ePutter,
        ePutterCover,
        eWoodCover,
        eEtcCover
    }

    public interface  IListenerDialog {
        public void onSave(String guestId, ClubInfo clubInfo);
    }

    public ClubInfoDialog() {
        super(null);
    }

    public ClubInfoDialog(Context context, CaddieInfo caddieInfo, int idx)
    {
        super(context);
        this.caddieInfo = caddieInfo;
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
        wedgeRecyclerView = findViewById(R.id.wedgeRecyclerView);
        putterRecyclerView = findViewById(R.id.putterRecyclerView);
        putterCoverRecyclerView = findViewById(R.id.putterCoverRecyclerView);
        woodCoverRecyclerView = findViewById(R.id.woodCoverRecyclerView);
        etcCoverRecyclerView = findViewById(R.id.etcCoverRecyclerView);

        tv_save = findViewById(R.id.tv_save);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_woodCount = findViewById(R.id.tv_woodCount);
        tv_utilityCount = findViewById(R.id.tv_utilityCount);
        tv_ironCount = findViewById(R.id.tv_ironCount);
        tv_wedgeCount = findViewById(R.id.tv_wedgeCount);
        tv_putterCount = findViewById(R.id.tv_putterCount);
        tv_putterCoverCount = findViewById(R.id.tv_putterCoverCount);
        tv_woodCoverCount = findViewById(R.id.tv_woodCoverCount);
        tv_etcCoverCount  = findViewById(R.id.tv_etcCoverCount);
        initClubInfoUI();

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClubInfo clubInfo = getClubInfo();
                caddieInfo.getGuestInfo().get(currentIdx).setClubInfo(clubInfo);
                dismiss();
                caddieInfo.getGuestInfo().get(currentIdx).setClubInfo(clubInfo);
                if (iListenerDialog != null)
                    iListenerDialog.onSave(caddieInfo.getGuestInfo().get(currentIdx).getReserveGuestId(), clubInfo);
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

    private void initClubInfoUI() {
        ClubInfo clubInfo = caddieInfo.getGuestInfo().get(currentIdx).clubInfo;
        initRecyclerViews(woodRecyclerView, wood, true, clubInfo, ClubType.eWood);
        initRecyclerViews(utiltyRecyclerView, utility, true, clubInfo, ClubType.eUtility);
        initRecyclerViews(ironRecyclerView, iron, true, clubInfo, ClubType.eIron);
        initRecyclerViews(putterRecyclerView, putter, false, clubInfo, ClubType.ePutter);
        initRecyclerViews(wedgeRecyclerView, wedge, true, clubInfo, ClubType.eWedge);
        initRecyclerViews(putterCoverRecyclerView, putterCover, false, clubInfo, ClubType.ePutterCover);
        initRecyclerViews(woodCoverRecyclerView, woodCover, false, clubInfo, ClubType.eWoodCover);
        initRecyclerViews(etcCoverRecyclerView, etcCover, false, clubInfo, ClubType.eEtcCover);
    }

    private ClubInfo getClubInfo() {

        ClubAdapter adapter = (ClubAdapter)woodRecyclerView.getAdapter();
        ClubInfo clubInfo = new ClubInfo();
        clubInfo.setWood(adapter.getSelectedItems());

        adapter = (ClubAdapter)utiltyRecyclerView.getAdapter();
        clubInfo.setUtility(adapter.getSelectedItems());

        adapter = (ClubAdapter)ironRecyclerView.getAdapter();
        clubInfo.setIron(adapter.getSelectedItems());

        adapter = (ClubAdapter)wedgeRecyclerView.getAdapter();
        clubInfo.setWedge(adapter.getSelectedItems());

        adapter = (ClubAdapter)putterRecyclerView.getAdapter();
        clubInfo.setPutter(adapter.getSelectedItems());

        adapter = (ClubAdapter)putterCoverRecyclerView.getAdapter();
        clubInfo.setPutter_cover(adapter.getSelectedItems());

        adapter = (ClubAdapter)woodCoverRecyclerView.getAdapter();
        clubInfo.setWood_cover(adapter.getSelectedItems());

        adapter = (ClubAdapter)etcCoverRecyclerView.getAdapter();
        clubInfo.setCover(adapter.getSelectedItems());

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
            public void onAdapterItemClicked(ClubType clubType, int count) {
                switch (clubType) {
                    case eWood: tv_woodCount.setText(count + "개"); break;
                    case eUtility: tv_utilityCount.setText(count + "개"); break;
                    case eIron: tv_ironCount.setText(count + "개"); break;
                    case eWedge: tv_wedgeCount.setText(count + "개"); break;
                    case ePutter: tv_putterCount.setText(count + "개"); break;
                    case ePutterCover: tv_putterCoverCount.setText(count + "개"); break;
                    case eWoodCover: tv_woodCoverCount.setText(count + "개"); break;
                    case eEtcCover: tv_etcCoverCount.setText(count + "개"); break;
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
                    adapter.setSelectItem(clubInfo.getIron());
                    break;
                case eWedge:
                    adapter.setSelectItem(clubInfo.getWedge());
                    break;
                case ePutter:
                    adapter.setSelectItem(clubInfo.getPutter());
                    break;
                case ePutterCover:
                    adapter.setSelectItem(clubInfo.getPutter_cover());
                    break;
                case eWoodCover:
                    adapter.setSelectItem(clubInfo.getWood_cover());
                    break;
                case eEtcCover:
                    adapter.setSelectItem(clubInfo.getCover());
                    break;
            }
        }

        selectCount(recyclerView, clubType);
    }

    private void selectCount(RecyclerView recyclerView, ClubType clubType) {

        ClubAdapter adapter = (ClubAdapter)recyclerView.getAdapter();
        switch (clubType) {
            case eWood: tv_woodCount.setText(adapter.getSelectedItems().size() + "개"); break;
            case eUtility: tv_utilityCount.setText(adapter.getSelectedItems().size() + "개"); break;
            case eIron: tv_ironCount.setText(adapter.getSelectedItems().size() + "개"); break;
            case eWedge: tv_wedgeCount.setText(adapter.getSelectedItems().size() + "개"); break;
            case ePutter: tv_putterCount.setText(adapter.getSelectedCoverCount() + "개"); break;
            case ePutterCover: tv_putterCoverCount.setText(adapter.getSelectedCoverCount() + "개"); break;
            case eWoodCover: tv_woodCoverCount.setText(adapter.getSelectedCoverCount() + "개"); break;
            case eEtcCover: tv_etcCoverCount.setText(adapter.getSelectedCoverCount() + "개"); break;
        }
    }
}