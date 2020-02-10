package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.databinding.FrRankingBinding;
import com.eye3.golfpay.fmb_tab.databinding.RankingRowBinding;
import com.eye3.golfpay.fmb_tab.listener.ScoreInputFinishListener;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.util.ScoreDialog;
import com.eye3.golfpay.fmb_tab.view.TabCourseLinear;

import java.util.ArrayList;
import java.util.Objects;

//우선순위가 차후로 연기됨 키오스크가 되면 구현될것임.
public class RankingFragment extends BaseFragment {


    protected String TAG = getClass().getSimpleName();

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

    FrRankingBinding binding;
    ArrayList<Course> mCourseList = new ArrayList<>();

    TextView[] mHoleScoreView = new TextView[10];
    ArrayList<TextView[]> CourseViewList = new ArrayList<TextView[]>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fr_ranking, container, false);
        View view = binding.getRoot();

        return view;
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
    }

    private class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingItemViewHolder> {
        ArrayList<Player> playerList;


        public RankingAdapter(Context context, ArrayList<Player> playerList) {
            this.playerList = playerList;

        }


        @NonNull
        @Override
        public RankingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.ranking_row, parent, false);
            RankingItemViewHolder viewHolder = new RankingItemViewHolder(view);
            //동적 코스뷰 생성
            for(int i =0; CourseViewList.size() > i ; i++){
                TextView[] holeScoreView = new TextView[10];
                for(int j =0; holeScoreView.length > j ; j++) {
                    holeScoreView[j].setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.MATCH_PARENT));
                    holeScoreView[j].setTextAppearance(R.style.RankRowTextView);
                }
                CourseViewList.add(i, holeScoreView);
            }




            return viewHolder;

        }


        /*
        *  holeScoreView: 각홀의점수를 담는 뷰어레리
        * holes: 각홀의 점수데이터 어레이
       */
        @Override
        public void onBindViewHolder(@NonNull RankingItemViewHolder holder, int position) {

          //  holder.tvRank.setText()
          //  holder.tvName.setText();
            for(int i =0; CourseViewList.size() > i ; i++){
                TextView[] holeScoreView = CourseViewList.get(i);
                Hole[] holes = playerList.get(position).playingCourse.get(i).holes;
                for(int j =0; mHoleScoreView.length > j ; j++) {
                //*****    holeScoreView[j].setText(holes[j].par);
                }
             //   holeScoreView[j] = total;
                CourseViewList.add(i, mHoleScoreView);
            }


        }


        public class RankingItemViewHolder extends RecyclerView.ViewHolder {
            protected TextView tvRank, tvName, tvScore;
            ArrayList<TextView> tvHoleScores = new ArrayList<TextView>();
            //            protected LinearLayout[] holeScoreLayout = new LinearLayout[9];
//            protected LinearLayout[] courseTotal = new LinearLayout[2]; //동적생성전 임시처리
//            protected TextView wholeTotal;
//            protected LinearLayout ll_score_row;
            LinearLayout container; //스코어을 넣는 레이아웃

            public RankingItemViewHolder(View view) {
                super(view);

                container = view.findViewById(R.id.linear_hole_score_container);
                tvRank = view.findViewById(R.id.tv_rank);
                tvName = view.findViewById(R.id.tv_player_name);
                tvScore = view.findViewById(R.id.tv_score); //점수 보여주는곳 api에서 내려줌

            }


        }


//        @Override
//        public void onBindViewHolder(@NonNull TabCourseLinear.ScoreAdapter.ScoreItemViewHolder scoreItemViewHolder, int i) {
//            final int pos = i;                            //course tab index
//            Course course = mPlayerList.get(i).playingCourse.get(mCourseId - 1);
//            if (i % 2 == 0) {
//                scoreItemViewHolder.ll_score_row.setBackgroundColor(Color.parseColor("#EBEFF1"));
//                for (int k = 0; scoreItemViewHolder.holeScoreLayout.length > k; k++)
//
//                    scoreItemViewHolder.holeScoreLayout[k].setBackgroundColor(Color.parseColor("#EBEFF1"));
//            } else {
////                scoreItemViewHolder.ll_score_row.setBackgroundColor(Color.parseColor("#F5F7F8"));
////                for (int k = 0; scoreItemViewHolder.holeScoreLayout.length > k; k++)
////                    scoreItemViewHolder.holeScoreLayout[k].setBackgroundColor(Color.parseColor("#F5F7F8"));
//            }
//
//            scoreItemViewHolder.tvRank.setText("lst");
//            scoreItemViewHolder.tvName.setText(playerList.get(i).name);
//
//            for (int j = 0; scoreItemViewHolder.holeScoreLayout.length > j; j++) {
//                if (j == mHoleScoreLayoutIdx)
//                    scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.GRAY);
//                else
//                    scoreItemViewHolder.holeScoreLayout[j].setBackgroundColor(Color.WHITE);
//            }
//
//
//          //  ((TextView) scoreItemViewHolder.holeScoreLayout[0].findViewById(R.id.hole1_hit)).setText(AppDef.Par_Tar(course.holes[0].playedScore, AppDef.isTar));
//         //   ((TextView) scoreItemViewHolder.holeScoreLayout[0].findViewById(R.id.hole1_putt)).setText(course.holes[0].playedScore.putting);
//
//
//
//        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }
    }


}


