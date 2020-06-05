package com.eye3.golfpay.fmb_tab.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.adapter.ClubAdapter;
import com.eye3.golfpay.fmb_tab.adapter.ClubGuestListAdapter;
import com.eye3.golfpay.fmb_tab.adapter.RestaurantListAdapter;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;

/**
 * Created by Administrator on 2017-09-22.
 */

public class ClubInfoDialog extends Dialog {

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

    String[] wood = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    String[] utility = {"1", "2", "3", "4", "5", "6", "7"};
    String[] iron = {"3", "4", "5", "6", "7", "8", "9", "10", "11"};
    String[] wedge = {"Pw", "Sw", "PSw", "52w", "53w", "54w", "55w", "56w", "57w", "58w", "60w"};
    String[] putter = {"0", "1", "2", "3"};
    String[] putterCover = {"0", "1", "2", "3", "4", "5"};
    String[] woodCover = {"0", "1", "2", "3", "4", "5", "6", "7"};
    String[] etcCover = {"0", "1", "2", "3", "4", "5"};

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

    public interface  IListenerDialogTouch {
        public void onTouch();
    }

    public ClubInfoDialog() {
        super(null);
    }

    public ClubInfoDialog(Context context) {
        super(context);
    }

    public ClubInfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void setData(String imgscr, String target, String idx) {

        setContentView(R.layout.fr_golf_club_list);
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

        initRecyclerViews(woodRecyclerView, wood, ClubType.eWood);
        initRecyclerViews(utiltyRecyclerView, utility, ClubType.eUtility);
        initRecyclerViews(ironRecyclerView, iron, ClubType.eIron);
        initRecyclerViews(wedgeRecyclerView, wedge, ClubType.eWedge);
        initRecyclerViews(putterRecyclerView, putter, ClubType.ePutter);
        initRecyclerViews(putterCoverRecyclerView, putterCover, ClubType.ePutterCover);
        initRecyclerViews(woodCoverRecyclerView, woodCover, ClubType.eWoodCover);
        initRecyclerViews(etcCoverRecyclerView, etcCover, ClubType.eEtcCover);

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "저장~", Toast.LENGTH_SHORT).show();
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

    private void createGuestList() {

        LinearLayoutManager mManager;

        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        guestsRecyclerView.setLayoutManager(mManager);

        ClubGuestListAdapter adapter = new ClubGuestListAdapter(getContext(), new ClubGuestListAdapter.IOnClickAdapter() {
            @Override
            public void onAdapterItemClicked(Integer id) {
            }
        });

        guestsRecyclerView.setAdapter(adapter);
        adapter.setData(Global.guestList);
        adapter.select(0);
    }

    private void initRecyclerViews(RecyclerView recyclerView, String[] items, ClubType clubType) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        ClubAdapter adapter = new ClubAdapter(getContext(), clubType, new ClubAdapter.IOnClickAdapter() {
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
    }
}
