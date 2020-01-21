package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.eye3.golfpay.fmb_tab.R;

import java.util.Objects;


/*
 *  wmmms 메뉴 화면
 */
public class RankingFragment extends BaseFragment {


    protected String TAG = getClass().getSimpleName();
    Button mBtnTaokeoverTest;

    private View tabBar;
    private View viewRankingViewDetailLinearLayout;
    private View nearestLongestLinearLayout;
    private TextView viewRankingText;
    private TextView viewDetailText;
    private TextView nearest;
    private TextView longest;
    private View rightLinearLayout;
    private TextView rightButtonTextView;
    private ImageView rightButtonIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_ranking, container, false);
    }

    private void rightLinearLayoutOnClick() {
        rightLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoNativeScreen(new ScoreFragment(), null);
            }
        });
    }

    private void viewRankingTextOnClick() {
        viewRankingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRankingText.setTextColor(0xff000000);
                viewDetailText.setTextColor(0xffcccccc);
            }
        });
    }

    private void viewDetailTextOnClick() {
        viewDetailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDetailText.setTextColor(0xff000000);
                viewRankingText.setTextColor(0xffcccccc);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        SetTitle("KT WMMS");
//        SetDividerVisibility(false);
        //   setDrawerLayoutEnable(true);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabBar = Objects.requireNonNull(getView()).findViewById(R.id.tab_bar);
        viewRankingViewDetailLinearLayout = tabBar.findViewById(R.id.viewRankingViewDetailLinearLayout);
        viewRankingText = tabBar.findViewById(R.id.viewRankingText);
        viewDetailText = tabBar.findViewById(R.id.viewDetailText);
        nearest = tabBar.findViewById(R.id.nearest);
        longest = tabBar.findViewById(R.id.longest);
        rightButtonTextView = tabBar.findViewById(R.id.rightButton);
        rightLinearLayout = tabBar.findViewById(R.id.rightLinearLayout);
        rightButtonIcon = tabBar.findViewById(R.id.rightButtonIcon);
        nearestLongestLinearLayout = tabBar.findViewById(R.id.nearestLongestLinearLayout);

        viewRankingViewDetailLinearLayout.setVisibility(View.VISIBLE);
        nearestLongestLinearLayout.setVisibility(View.VISIBLE);
        rightButtonTextView.setText("Score");
        rightButtonIcon.setBackgroundResource(R.drawable.score_down);
        viewRankingText.setTextColor(0xff000000);
        viewDetailText.setTextColor(0xffcccccc);
        nearest.setTextColor(0xffcccccc);
        longest.setTextColor(0xffcccccc);

        viewRankingTextOnClick();
        viewDetailTextOnClick();
        rightLinearLayoutOnClick();

        //   mBtnTaokeoverTest = (Button)getView().findViewById(R.id.btnTakeoverTest);
        //  mBtnTaokeoverTest.setOnClickListener();
//        getView().findViewById(R.id.btnTakeoverTest).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                GoNativeScreenAdd(new FrDeviceSearch(), null);
////                Intent intent = new Intent(getActivity(), DeviceSearchActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
//            }
//        });
    }


}


