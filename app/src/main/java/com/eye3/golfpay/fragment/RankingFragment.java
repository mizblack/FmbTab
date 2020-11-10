package com.eye3.golfpay.fragment;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

//우선순위가 차후로 연기됨 키오스크가 되면 구현될것임.
public class RankingFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();

    private View tabBar;
    private View pinkNearestOrLinearLayout;
    private View viewRankingViewDetailLinearLayout;
    private TextView viewRankingText;
    private TextView viewDetailText;
    private View rightLinearLayout;
    private TextView rightButtonTextView;
    private ImageView rightButtonIcon;
    static int NUM_OF_COURSE = 0;
    static int NUM_OF_HOLE = 10;
    RecyclerView mRankingRecyclerView;
    RankingAdapter mRankingAdapter;
    RankingDetailAdapter mRankingDetailAdapter;
    List<Course> mCourseList;
    List<Player> mPlayerList;
    LinearLayout mLinearHoleNoContainer = null;
    //홀정보레이아웃 어레이
    TextView[][] holeInfoLinear;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
        String type = "null";
        if (Global.selectedReservation.getGroup() == null) {
            type = "null";
        } else {
            type = "group";
        }
        getReserveScore(type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_ranking, container, false);
        mRankingRecyclerView = view.findViewById(R.id.player_ranking_recycler);
        //mLinearHoleNoContainer = view.findViewById(R.id.hole_no_container);
        mParentActivity.showMainBottomBar();

        return view;
    }

    /*
     *  최상단 홀정보 보여주는 뷰생성
     *
     */
    private void createHoleInfoLinear(LinearLayout mLinearHoleNoContainer) {

        for (int j = 0; holeInfoLinear.length > j; j++) {
            for (int k = 0; holeInfoLinear[j].length - 1 > k; k++) {
                holeInfoLinear[j][k] = new TextView(mContext);
                holeInfoLinear[j][k].setText(mCourseList.get(j).holes.get(k).hole_no);
                holeInfoLinear[j][k].setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.ranking_linear_width), ViewGroup.LayoutParams.MATCH_PARENT));
                holeInfoLinear[j][k].setTextAppearance(R.style.RankColumnHoleTextView);
                holeInfoLinear[j][k].setGravity(Gravity.CENTER);
                mLinearHoleNoContainer.addView(holeInfoLinear[j][k]);

            }
            holeInfoLinear[j][holeInfoLinear[j].length - 1] = new TextView(mContext);
            //넓이 계산할것
            holeInfoLinear[j][holeInfoLinear[j].length - 1].setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.ranking_linear_width), ViewGroup.LayoutParams.MATCH_PARENT));
            holeInfoLinear[j][holeInfoLinear[j].length - 1].setTextAppearance(R.style.RankColumnHoleTextView);
            holeInfoLinear[j][holeInfoLinear[j].length - 1].setText(mCourseList.get(j).courseName);
            holeInfoLinear[j][holeInfoLinear[j].length - 1].setGravity(Gravity.CENTER);
            mLinearHoleNoContainer.addView(holeInfoLinear[j][holeInfoLinear[j].length - 1]);
        }

    }

    private void rightLinearLayoutOnClick() {
        rightLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("ani_direction", "down");
                GoNativeScreen(new ScoreFragment(), bundle);
            }
        });
    }

    private void viewRankingTextOnClick() {
        viewRankingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRankingText.setTextColor(0xff000000);
                viewDetailText.setTextColor(0xffcccccc);
                initRecyclerView(mRankingRecyclerView, mPlayerList);
            }
        });
    }

    private void viewDetailTextOnClick() {
        viewDetailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDetailText.setTextColor(0xff000000);
                viewRankingText.setTextColor(0xffcccccc);
                initRecyclerDetailView(mRankingRecyclerView, mPlayerList);
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabBar = Objects.requireNonNull(getView()).findViewById(R.id.tab_bar);
        pinkNearestOrLinearLayout = tabBar.findViewById(R.id.pinkNearestOrLinearLayout);
        pinkNearestOrLinearLayout.setVisibility(View.GONE);
        viewRankingViewDetailLinearLayout = tabBar.findViewById(R.id.viewRankingViewDetailLinearLayout);
        viewRankingText = tabBar.findViewById(R.id.viewRankingText);
        viewDetailText = tabBar.findViewById(R.id.viewDetailText);
        rightButtonTextView = tabBar.findViewById(R.id.rightButton);
        rightLinearLayout = tabBar.findViewById(R.id.rightLinearLayout);
        rightButtonIcon = tabBar.findViewById(R.id.rightButtonIcon);

        viewRankingViewDetailLinearLayout.setVisibility(View.VISIBLE);
        rightButtonTextView.setText("스코어");
        rightButtonIcon.setBackgroundResource(R.drawable.score_down);
        viewRankingText.setTextColor(0xff000000);
        viewDetailText.setTextColor(0xffcccccc);

        viewRankingTextOnClick();
        viewDetailTextOnClick();
        rightLinearLayoutOnClick();

        viewRankingText.performClick(); //초기화로 상세보기
    }

    private void initRecyclerView(RecyclerView rankingRecyclerView, List<Player> playerList) {
        LinearLayoutManager mManager;
        if (playerList == null)
            return;

        mManager = new LinearLayoutManager(mContext);
        rankingRecyclerView.setLayoutManager(mManager);

        mRankingAdapter = new RankingAdapter(mContext, playerList, playerList.get(0).playingCourse);
        rankingRecyclerView.setAdapter(mRankingAdapter);
        mRankingAdapter.notifyDataSetChanged();
    }

    private void initRecyclerDetailView(RecyclerView rankingRecyclerView, List<Player> playerList) {
        LinearLayoutManager mManager;
        if (playerList == null)
            return;
        mManager = new LinearLayoutManager(mContext);
        rankingRecyclerView.setLayoutManager(mManager);

        mRankingDetailAdapter = new RankingDetailAdapter(mContext, playerList, playerList.get(0).playingCourse);
        rankingRecyclerView.setAdapter(mRankingDetailAdapter);
        mRankingDetailAdapter.notifyDataSetChanged();
    }

    private class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;
        Context context;
        List<Player> playerList;
        List<Course> playingCourse;
        LinearLayout container;

        RankingAdapter(Context context, List<Player> playerList, List<Course> playingCourse) {
            this.context = context;
            this.playerList = playerList;
            this.playingCourse = playingCourse;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder viewHolder = null;

            if (viewType == TYPE_HEADER) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_column, parent, false);
                viewHolder = new HeaderViewHolder(view);
                //mLinearHoleNoContainer = view.findViewById(R.id.hole_no_container);
                //createHoleInfoLinear(mLinearHoleNoContainer);


            } else if (viewType == TYPE_ITEM) {
                view = LayoutInflater.from(mContext).inflate(R.layout.ranking_row, parent, false);
                viewHolder = new RankingItemViewHolder(view);
//                        view.findViewById(R.id.rankingNormalLinearLayout).setVisibility(View.GONE);
//                        view.findViewById(R.id.linear_hole_score_container).setVisibility(View.VISIBLE);
//                        view.findViewById(R.id.tv_total_par_score).setVisibility(View.GONE); //gone으로처리
            }

            assert viewHolder != null;
            return viewHolder;
        }


        /*
         *  holeScoreView: 각홀의점수를 담는 뷰어레이
         * holes: 각홀의 점수데이터 어레이
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (playerList == null)
                return;

            if (position != 0 && position % 2 == 1)
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.FMB_Color_33CCD7DB));
             else
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));

//            RankingItemViewHolder holder1 = null;
//            if (holder instanceof HeaderViewHolder) {
//                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
//            } else {
//                position = position - 1;
//                // Item 을 하나, 하나 보여주는(bind 되는) 함수입니다.
//                holder1 = (RankingItemViewHolder) holder;

//
//                if (position % 2 == 0) {
//                    holder1.itemView.setBackgroundColor(Color.parseColor("#EBEFF1"));
//                } else {
//                    holder1.itemView.setBackgroundColor(Color.parseColor("#F5F7F8"));
//
//                }
//                holder1.tvRank.setText(playerList.get(position).Ranking);
//                //팀명을 api로부터 받아야함
//                if (playerList.get(position).team_name != null || playerList.get(position).team_name == "")
//                    holder1.tvName.setText(playerList.get(position).name + "(" + playerList.get(position).team_name + ")");
//                else
//                    holder1.tvName.setText(playerList.get(position).name);
//
//                //동적 코스점수뷰 생성
//                for (int i = 0; holder1.mHoleScoreView.length > i; i++) {
//                    List<Hole> holes = playerList.get(position).playingCourse.get(i).holes;
//                    //해당 코스의 홀정보가 없으면 바로 리턴
//                    if (holes.size() == 0)
//                        return;
//                    int eachCourseTotal = 0;
//                    for (int j = 0; holder1.mHoleScoreView[i].length - 1 > j; j++) {
//                        holder1.mHoleScoreView[i][j].setGravity(Gravity.CENTER);
//                        holder1.mHoleScoreView[i][j].setTextAppearance(R.style.RankColumnHoleTextView);
//                        //여기서 각홀 타점수를 입력한다.
//                        holder1.mHoleScoreView[i][j].setText(holes.get(j).playedScore.tar);
//
//                        if (Util.isInteger(holes.get(j).playedScore.tar))
//                            eachCourseTotal = eachCourseTotal + Integer.parseInt(holes.get(j).playedScore.tar);
//                    }
//                    // 스코어 마지막뷰에 총합을 넣는다
//                    holder1.mHoleScoreView[i][holder1.mHoleScoreView[i].length - 1].setGravity(Gravity.CENTER);
//                    holder1.mHoleScoreView[i][holder1.mHoleScoreView[i].length - 1].setTextAppearance(R.style.RankColumnHoleTextView);
//                    holder1.mHoleScoreView[i][holder1.mHoleScoreView[i].length - 1].setText(String.valueOf(eachCourseTotal));
//                    holder1.mHoleScoreView[i][holder1.mHoleScoreView[i].length - 1].setTypeface(holder1.mHoleScoreView[i][holder1.mHoleScoreView[i].length - 1].getTypeface(), Typeface.BOLD);
//
//                    holder1.tvTarTotalScore.setText(playerList.get(position).totalTar);
//                    holder1.tvTarTotalScore.setTextColor(Color.WHITE);
//                    holder1.tvParTotalScore.setText(displayTotalParScore(playerList.get(position).totalPar, holder1.tvParTotalScore));
//                    holder1.tvParTotalScore.setVisibility(View.VISIBLE);
//                    holder1.tvNormalScore.setText(playerList.get(position).totalTar);
//                    holder1.tvNormalPar.setText(displayTotalParScore(playerList.get(position).totalPar, holder1.tvNormalPar));
//
//                    holder1.tvPlayingHole.setText(playerList.get(position).lastHoleNo);
//                    holder1.tvGroupName.setText(Global.selectedReservation.getGroup());
//                    holder1.tvGroupName.setVisibility(View.VISIBLE);
//                    //    holder1.tvCourseName.setText(playerList.get(position).playingCourse.get(i).courseName); //현재치고잇는 코스명을 알아야됨
//                    holder1.tvCourseName.setText("IN");
//                    holder1.tvCourseName.setVisibility(View.VISIBLE);
//                }
//            }
        }

        private String displayTotalParScore(String parTotalScore, TextView tv) {
            int parScoreInt = Integer.parseInt(parTotalScore);
            if (parScoreInt >= 0) {
                tv.setTextColor(Color.RED);
            } else
                tv.setTextColor(Color.CYAN);
            return "(" + parTotalScore + ")";
        }

        class RankingItemViewHolder extends RecyclerView.ViewHolder {
            TextView tvRank, tvName, tvTarTotalScore, tvParTotalScore, tvPlayingHole, tvGroupName, tvCourseName, tvNormalScore, tvNormalPar;
            TextView[][] mHoleScoreView;

            RankingItemViewHolder(View view) {
                super(view);
                mHoleScoreView = new TextView[NUM_OF_COURSE][NUM_OF_HOLE];
                container = view.findViewById(R.id.linear_hole_score_container);

                for (int i = 0; mHoleScoreView.length > i; i++) {

                    for (int j = 0; mHoleScoreView[i].length > j; j++) {
                        mHoleScoreView[i][j] = new TextView(getActivity());
                        mHoleScoreView[i][j].setTextAppearance(R.style.RankColumnHoleTextView);
                        mHoleScoreView[i][j].setLayoutParams(new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.ranking_linear_width), ViewGroup.LayoutParams.MATCH_PARENT));
                        container.addView(mHoleScoreView[i][j]);
                    }

                }
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
            return playerList.size() + 1;
        }
    }

    private class RankingDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final int TYPE_HEADER = 0;
        private final int TYPE_ITEM = 1;
        Context context;
        List<Player> playerList;
        List<Course> playingCourse;
        LinearLayout container;

        RankingDetailAdapter(Context context, List<Player> playerList, List<Course> playingCourse) {
            this.context = context;
            this.playerList = playerList;
            this.playingCourse = playingCourse;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder viewHolder = null;

            if (viewType == TYPE_HEADER) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_column_detail, parent, false);
                viewHolder = new HeaderViewHolder(view);
                //mLinearHoleNoContainer = view.findViewById(R.id.hole_no_container);
                //createHoleInfoLinear(mLinearHoleNoContainer);


            } else if (viewType == TYPE_ITEM) {
                view = LayoutInflater.from(mContext).inflate(R.layout.ranking_row_detail, parent, false);
                viewHolder = new RankingDetailItemViewHolder(view);
//                        view.findViewById(R.id.rankingNormalLinearLayout).setVisibility(View.GONE);
//                        view.findViewById(R.id.linear_hole_score_container).setVisibility(View.VISIBLE);
//                        view.findViewById(R.id.tv_total_par_score).setVisibility(View.GONE); //gone으로처리
            }

            assert viewHolder != null;
            return viewHolder;
        }

        /*
         *  holeScoreView: 각홀의점수를 담는 뷰어레이
         * holes: 각홀의 점수데이터 어레이
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (playerList == null)
                return;

            if (position != 0 && position % 2 == 1)
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.FMB_Color_33CCD7DB));
            else
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        }

        private String displayTotalParScore(String parTotalScore, TextView tv) {
            int parScoreInt = Integer.parseInt(parTotalScore);
            if (parScoreInt >= 0) {
                tv.setTextColor(Color.RED);
            } else
                tv.setTextColor(Color.CYAN);
            return "(" + parTotalScore + ")";
        }

        class RankingDetailItemViewHolder extends RecyclerView.ViewHolder {

            RankingDetailItemViewHolder(View view) {
                super(view);
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
            return playerList.size() + 1;
        }
    }

    private void getReserveScore(String type) {
        showProgress("랭킹 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getReserveScore(getActivity(), Global.reserveId, "group", new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    mPlayerList = response.getList();
                    mPlayerList.sort(Comparator.naturalOrder());
                    mCourseList = Global.courseInfoList;
                    NUM_OF_COURSE = response.getList().get(0).playingCourse.size(); //코스수를 지정한다. courseNum 을 요청할것
                    holeInfoLinear = new TextView[NUM_OF_COURSE][NUM_OF_HOLE];

                    initRecyclerView(mRankingRecyclerView, mPlayerList);

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

}
