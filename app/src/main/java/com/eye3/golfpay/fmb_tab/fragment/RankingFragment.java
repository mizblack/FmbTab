package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.databinding.FrRankingBinding;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.HoleInfoLinear;

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

    static int NUM_OF_COURSE;
    static int NUM_OF_HOLE = 10;
    RecyclerView mRankingRecyclerView;
    RankingAdapter mRankingAdapter;
    FrRankingBinding binding;
    ArrayList<Course> mCourseList = new ArrayList<>();
    TextView[][] mHoleScoreView;
    ArrayList<Player> mPlayerList;
    LinearLayout mLinearHoleNoContainer  = null;
    //홀정보레이아웃 어레이
    TextView[][] holeInfoLinear  ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }

        getReserveScore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fr_ranking, container, false);
        View view = binding.getRoot();
        mRankingRecyclerView = view.findViewById(R.id.player_ranking_recycler);
        mLinearHoleNoContainer = view.findViewById(R.id.hole_no_container) ;
        mParentActivity.showMainBottomBar();

        getReserveScore();
        return view;
    }

    /*
     *  최상단 홀정보 보여주는 뷰생성
     *
     */
    private void createHoleInfoLinear() {

        for(int j = 0; holeInfoLinear.length > j; j++) {
            for (int k = 0;  holeInfoLinear[j].length-1 > k; k++) {
                holeInfoLinear[j][k] = new TextView(mContext);
                holeInfoLinear[j][k].setText(mCourseList.get(j).holes[k].id);
                holeInfoLinear[j][k].setLayoutParams(new ViewGroup.LayoutParams(60, ViewGroup.LayoutParams.MATCH_PARENT));
                holeInfoLinear[j][k].setTextAppearance(R.style.RankColumnHoleTextView);
            //    mLinearHoleNoContainer = new LinearLayout(mContext);
             //   mLinearHoleNoContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mLinearHoleNoContainer.addView(holeInfoLinear[j][k]);

            }
            holeInfoLinear[j][holeInfoLinear[j].length-1] = new TextView(mContext);
           //넓이 계산할것
            holeInfoLinear[j][holeInfoLinear[j].length-1].setLayoutParams(new ViewGroup.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT));
            holeInfoLinear[j][holeInfoLinear[j].length-1].setTextAppearance(R.style.RankColumnHoleTextView);
            holeInfoLinear[j][holeInfoLinear[j].length-1].setText(mCourseList.get(j).courseName);
            mLinearHoleNoContainer.addView( holeInfoLinear[j][holeInfoLinear[j].length-1]);
        }

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

    private void initRecyclerView(RecyclerView recyclerView, ArrayList<Player> playerList) {
        LinearLayoutManager mManager ;

       // mRankingRecyclerView.setHasFixedSize(true);
        mManager = new LinearLayoutManager(mContext);
        mRankingRecyclerView.setLayoutManager(mManager);
        mRankingAdapter = new RankingAdapter(mContext, playerList, playerList.get(0).playingCourse);
        mRankingRecyclerView.setAdapter(mRankingAdapter);
        mRankingAdapter.notifyDataSetChanged();
    }

    private class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;

        Context context;
        ArrayList<Player> playerList;
        ArrayList<Course> playingCourse;
        LinearLayout container;
        public RankingAdapter(Context context, ArrayList<Player> playerList, ArrayList<Course> playingCourse) {
            this.context = context;
            this.playerList = playerList;
            this.playingCourse = playingCourse;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder viewHolder ;

            if (viewType == TYPE_HEADER) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_column, parent, false);
                viewHolder = new HeaderViewHolder(view);
                mLinearHoleNoContainer = view.findViewById(R.id.hole_no_container) ;
                createHoleInfoLinear();
            }  else {
                 view = LayoutInflater.from(mContext).inflate(R.layout.ranking_row, parent, false);
                 viewHolder = new RankingItemViewHolder(view);
            }


            return  viewHolder;

        }


        /*
         *  holeScoreView: 각홀의점수를 담는 뷰어레이
         * holes: 각홀의 점수데이터 어레이
         */
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position ) {
            RankingItemViewHolder holder1 = null;
            if (holder instanceof HeaderViewHolder) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            }else {
                position = position -1;
                // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
                holder1 = (RankingItemViewHolder) holder;

                if (position % 2 == 0) {
                    holder1.itemView.setBackgroundColor(Color.parseColor("#EBEFF1"));
                } else {
                    holder1.itemView.setBackgroundColor(Color.parseColor("#F5F7F8"));

                }
                holder1.tvRank.setText(playerList.get(position).Ranking);
                holder1.tvName.setText(playerList.get(position).name);
                //동적 코스뷰 생성
                for (int i = 0; mHoleScoreView.length > i; i++) {
                    Hole[] holes = playerList.get(position).playingCourse.get(i).holes;
                    for (int j = 0; mHoleScoreView[i].length - 2 > j; j++) {
                        mHoleScoreView[i][j].setGravity(Gravity.CENTER);
                        mHoleScoreView[i][j].setText(holes[j].playedScore.tar);

                    }
                    // 스코어 마지막뷰에 총합을 넣는다
                    mHoleScoreView[i][mHoleScoreView[i].length - 1].setText(playerList.get(position).totalTar);

                }
                holder1.tvTarTotalScore.setText(playerList.get(position).totalTar);
                holder1.tvTarTotalScore.setTextColor(Color.WHITE);
                holder1.tvParTotalScore.setText(displayTotalParScore(playerList.get(position).totalPar, holder1.tvParTotalScore));
                holder1.tvParTotalScore.setVisibility(View.VISIBLE);

            }
        }

        private String displayTotalParScore(String parTotalScore, TextView tv) {
            int parScoreInt = Integer.valueOf(parTotalScore);
            if (parScoreInt >= 0) {
                tv.setTextColor(Color.RED);
            } else
                tv.setTextColor(Color.CYAN);
            return "(" + parTotalScore + ")";
        }

        public class RankingItemViewHolder extends RecyclerView.ViewHolder {
            protected TextView tvRank, tvName, tvTarTotalScore, tvParTotalScore;

            public RankingItemViewHolder(View view) {
                super(view);

                container = view.findViewById(R.id.linear_hole_score_container);

                for (int i = 0; mHoleScoreView.length > i; i++) {

                    for (int j = 0; mHoleScoreView[i].length > j; j++) {
                        mHoleScoreView[i][j] = new TextView(getActivity());
                        mHoleScoreView[i][j].setTextAppearance(R.style.RankColumnHoleTextView);
                        mHoleScoreView[i][j].setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.ranking_linear_width), ViewGroup.LayoutParams.MATCH_PARENT));
                        container.addView( mHoleScoreView[i][j]);
                    }

                }
                tvRank = view.findViewById(R.id.tv_rank);
                tvName = view.findViewById(R.id.tv_player_name);
                tvTarTotalScore = view.findViewById(R.id.tv_total_tar_score); //점수 보여주는곳 api에서 내려줌
                tvParTotalScore = view.findViewById(R.id.tv_total_par_score);
            }


        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {

            HeaderViewHolder(View headerView) {
                super(headerView);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return TYPE_HEADER;
            else
                return TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return playerList.size() +1;
        }
    }

    private void getReserveScore() {
        showProgress("랭킹 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getReserveScore(getActivity(), Global.reserveId, "group" ,new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    mPlayerList = (ArrayList<Player>) response.getList();
                    mCourseList = getCourse(mPlayerList);
                    NUM_OF_COURSE = response.getList().get(0).playingCourse.size(); //코스수를 지정한다. courseNum을 요청할것
                    holeInfoLinear = new TextView[NUM_OF_COURSE][NUM_OF_HOLE];
                    mHoleScoreView = new TextView[NUM_OF_COURSE][NUM_OF_HOLE];

                    initRecyclerView(mRankingRecyclerView, mPlayerList);
                    //createHoleInfoLinear();



                } else if (response.getResultCode().equals("fail")) {
                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ResponseData<Player> response) {
                hideProgress();
                response.getError();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });

    }


    private ArrayList<Course> getCourse(ArrayList<Player> playerList) {
        //첫번째 플레이어 코스가 전체코스임.
        return playerList.get(0).playingCourse;

    }


}


