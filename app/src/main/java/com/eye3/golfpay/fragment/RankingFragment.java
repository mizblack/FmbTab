package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
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
import com.eye3.golfpay.util.Util;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

//우선순위가 차후로 연기됨 키오스크가 되면 구현될것임.
public class RankingFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();

    private TextView viewRankingText;
    private TextView viewDetailText;
    private View rightLinearLayout;
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
                mParentActivity.getViewMenuFragment().selectMenu(R.id.view_score);
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

        View tabBar = Objects.requireNonNull(getView()).findViewById(R.id.tab_bar);
        LinearLayout ll_lear_long_tab = tabBar.findViewById(R.id.ll_lear_long_tab);
        ll_lear_long_tab.setVisibility(View.GONE);

        LinearLayout ll_ranking_tab = tabBar.findViewById(R.id.ll_ranking_tab);
        viewRankingText = tabBar.findViewById(R.id.viewRankingText);
        viewDetailText = tabBar.findViewById(R.id.viewDetailText);
        TextView rightButtonTextView = tabBar.findViewById(R.id.rightButton);
        ImageView rightButtonIcon = tabBar.findViewById(R.id.rightButtonIcon);

        rightLinearLayout = tabBar.findViewById(R.id.rightLinearLayout);
        ll_ranking_tab.setVisibility(View.VISIBLE);
        rightButtonTextView.setText("스코어");
        rightButtonIcon.setBackgroundResource(R.drawable.score_down);
        viewRankingText.setTextColor(0xff000000);
        viewDetailText.setTextColor(0xffcccccc);

        tabBar.findViewById(R.id.btn_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseFragment courseFragment = new CourseFragment();
                GoNativeScreen(courseFragment, null);
                Global.courseFragment = courseFragment;
                mParentActivity.getViewMenuFragment().selectMenu(R.id.view_gps);
            }
        });

        viewRankingTextOnClick();
        viewDetailTextOnClick();
        rightLinearLayoutOnClick();

        viewRankingText.performClick(); //초기화로 상세보기
    }

    private void initRecyclerView(RecyclerView rankingRecyclerView, List<Player> playerList) {
        LinearLayoutManager mManager;
        if (playerList == null)
            return;

        setRankingColumn();


        mManager = new LinearLayoutManager(mContext);
        rankingRecyclerView.setLayoutManager(mManager);

        mRankingAdapter = new RankingAdapter(mContext, playerList);
        rankingRecyclerView.setAdapter(mRankingAdapter);
        mRankingAdapter.notifyDataSetChanged();
    }

    private void setRankingColumn() {
        View column = Objects.requireNonNull(getView()).findViewById(R.id.ranking_row);
        View ranking_column_detail = Objects.requireNonNull(getView()).findViewById(R.id.ranking_column_detail);

        column.setVisibility(View.VISIBLE);
        ranking_column_detail.setVisibility(View.GONE);

        TextView tvRanking = column.findViewById(R.id.tv_rank);
        TextView tvStart = column.findViewById(R.id.tv_start);
        TextView tvEnd = column.findViewById(R.id.tv_end);
        TextView tvTime = column.findViewById(R.id.tv_time);
        TextView tvHole = column.findViewById(R.id.tv_hole);
        TextView tvPlayer = column.findViewById(R.id.tv_player);
        TextView tvScore = column.findViewById(R.id.tv_score);
        TextView tvTotalParScore = column.findViewById(R.id.tv_total_par_score);

        tvRanking.setText("Rank");
        tvStart.setText("Start");
        tvEnd.setText("End");
        tvTime.setText("Time");
        tvHole.setText("Hole");
        tvPlayer.setText("Player");
        tvScore.setText("Score(Over Par)");
    }

    private void setDetailColumn() {
        View ranking_column_detail = Objects.requireNonNull(getView()).findViewById(R.id.ranking_column_detail);
        View ranking_row = Objects.requireNonNull(getView()).findViewById(R.id.ranking_row);
        ranking_column_detail.setVisibility(View.VISIBLE);
        ranking_row.setVisibility(View.GONE);

        Global.courseInfoList.size();
        TextView beginHole = ranking_column_detail.findViewById(R.id.tv_beginHole);
        TextView endHole = ranking_column_detail.findViewById(R.id.tv_endHole);
        beginHole.setText(Global.courseInfoList.get(0).ctype);
        endHole.setText(Global.courseInfoList.get(1).ctype);

    }

    private void initRecyclerDetailView(RecyclerView rankingRecyclerView, List<Player> playerList) {
        setDetailColumn();

        LinearLayoutManager mManager;
        if (playerList == null)
            return;

        mManager = new LinearLayoutManager(mContext);
        rankingRecyclerView.setLayoutManager(mManager);

        mRankingDetailAdapter = new RankingDetailAdapter(mContext, playerList, playerList.get(0).course);
        rankingRecyclerView.setAdapter(mRankingDetailAdapter);
        mRankingDetailAdapter.notifyDataSetChanged();
    }

    private class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingItemViewHolder> {
        Context context;
        List<Player> playerList;
        LinearLayout container;

        RankingAdapter(Context context, List<Player> playerList) {
            this.context = context;
            this.playerList = playerList;
        }

        @NonNull
        @Override
        public RankingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(mContext).inflate(R.layout.ranking_row, parent, false);
            return new RankingItemViewHolder(view);
        }

        /*
         *  holeScoreView: 각홀의점수를 담는 뷰어레이
         * holes: 각홀의 점수데이터 어레이
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RankingItemViewHolder holder, int position) {
            if (playerList == null)
                return;

            Player player = playerList.get(position);

            if (position % 2 == 0)
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.FMB_Color_33CCD7DB));
            else
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));

            if (player.reserve_id.equals(Global.reserveId)) {
                holder.tvRanking.setTextAppearance(R.style.GlobalTextView_22SP_irisBlue_NotoSans_Bold);
                holder.tvPlayer.setTextAppearance(R.style.GlobalTextView_22SP_irisBlue_NotoSans_Medium);
            } else {
                holder.tvRanking.setTextAppearance(R.style.GlobalTextView_22SP_Black_NotoSans_Bold);
                holder.tvPlayer.setTextAppearance(R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Medium);
            }

            holder.tvPlayer.setText(player.guestName);
            holder.tvRanking.setText(String.valueOf(player.ranking) + "위");
            holder.tvStart.setText(player.course.get(0).ctype);
            holder.tvEnd.setText(player.course.get(1).ctype);
            holder.tvTime.setText(getTeeOffTime(player));
            holder.tvHole.setText(getHoleText(player));
            holder.tvScore.setText(getScoreText(player));
        }

        private SpannableString getHoleText(Player player) {
            String hole = String.format("%dh", player.lastHoleNo);
            return Util.getFontSizeSpannableString(hole, "h", 13);
        }

        private SpannableString getTeeOffTime(Player player) {

            String teeOff = String.format("%s (%d조)", player.teeoff, player.team_no);
            int startIndex, endIndex;
            startIndex = teeOff.indexOf("(");
            endIndex = teeOff.length();
            return Util.getFontSizeSpannableString(teeOff, startIndex, endIndex, 13);
        }

        private SpannableString getScoreText(Player player) {
            String score = String.format("%d  (%d)", player.totalTar, player.totalPar);
            int startIndex, endIndex;
            startIndex = score.indexOf("(");
            endIndex = score.length();

            String color = "#ffffff";
            if (player.totalPar < 0)
                color = "#ed1c24";

            return Util.getColorSpannableString(score, startIndex, endIndex, Color.parseColor(color));
        }

        class RankingItemViewHolder extends RecyclerView.ViewHolder {
            TextView tvRanking = itemView.findViewById(R.id.tv_rank);
            TextView tvStart = itemView.findViewById(R.id.tv_start);
            TextView tvEnd = itemView.findViewById(R.id.tv_end);
            TextView tvTime = itemView.findViewById(R.id.tv_time);
            TextView tvHole = itemView.findViewById(R.id.tv_hole);
            TextView tvPlayer = itemView.findViewById(R.id.tv_player);
            TextView tvScore = itemView.findViewById(R.id.tv_score);
            TextView[][] mHoleScoreView;

            RankingItemViewHolder(View view) {
                super(view);
                mHoleScoreView = new TextView[NUM_OF_COURSE][NUM_OF_HOLE];
                container = view.findViewById(R.id.linear_hole_score_container);

                tvRanking.setTextAppearance(R.style.GlobalTextView_22SP_Black_NotoSans_Bold);
                tvStart.setTextAppearance(R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Medium);
                tvEnd.setTextAppearance(R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Medium);
                tvTime.setTextAppearance(R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Medium);
                tvHole.setTextAppearance(R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Medium);
                tvPlayer.setTextAppearance(R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Medium);
                tvScore.setTextAppearance(R.style.GlobalTextView_22SP_white_NotoSans_Medium);
            }
        }

        @Override
        public int getItemCount() {
            return playerList.size();
        }
    }

    private class RankingDetailAdapter extends RecyclerView.Adapter<RankingDetailAdapter.RankingDetailItemViewHolder> {
        Context context;
        List<Player> playerList;

        RankingDetailAdapter(Context context, List<Player> playerList, List<Course> playingCourse) {
            this.context = context;
            this.playerList = playerList;
        }

        @NonNull
        @Override
        public RankingDetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            view = LayoutInflater.from(mContext).inflate(R.layout.ranking_row_detail, parent, false);
            return new RankingDetailItemViewHolder(view);
        }

        /*
         *  holeScoreView: 각홀의점수를 담는 뷰어레이
         * holes: 각홀의 점수데이터 어레이
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RankingDetailItemViewHolder holder, int position) {
            if (playerList == null)
                return;

            Player player = playerList.get(position);

            if (position % 2 == 0)
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.FMB_Color_33CCD7DB));
            else
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));

            if (player.reserve_id.equals(Global.reserveId)) {
                holder.tvRanking.setTextAppearance(R.style.GlobalTextView_22SP_irisBlue_NotoSans_Bold);
                holder.tvPlayer.setTextAppearance(R.style.GlobalTextView_22SP_irisBlue_NotoSans_Medium);
            } else {
                holder.tvRanking.setTextAppearance(R.style.GlobalTextView_22SP_Black_NotoSans_Bold);
                holder.tvPlayer.setTextAppearance(R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Medium);
            }

            holder.tvPlayer.setText(player.guestName);
            holder.tvRanking.setText(String.valueOf(player.ranking) + "위");
            holder.tvScore.setText(getScoreText(player));

            int total = 0;
            for (int i = 0; i < player.course.get(0).holes.size(); i++) {
                holder.tvBeginHole[i].setText(player.course.get(0).holes.get(i).playedScore.par);
                try {
                    total += Integer.parseInt(player.course.get(0).holes.get(i).playedScore.par);
                } catch (NumberFormatException e) {
                    total += 0;
                }
            }
            holder.tvBeginTotal.setText(String.valueOf(total));

            total = 0;
            for (int i = 0; i < player.course.get(1).holes.size(); i++) {
                holder.tvEndHole[i].setText(player.course.get(1).holes.get(i).playedScore.par);

                try {
                    total += Integer.parseInt(player.course.get(1).holes.get(i).playedScore.par);
                } catch (NumberFormatException e) {
                    total += 0;
                }
            }
            holder.tvEndTotal.setText(String.valueOf(total));
        }

        private SpannableString getScoreText(Player player) {
            String score = String.format("%d (%d)", player.totalTar, player.totalPar);
            int startIndex, endIndex;
            startIndex = score.indexOf("(");
            endIndex = score.length();

            String color = "#ffffff";
            if (player.totalPar < 0)
                color = "#ed1c24";

            return Util.getColorSpannableString(score, startIndex, endIndex, Color.parseColor(color));
        }

        class RankingDetailItemViewHolder extends RecyclerView.ViewHolder {

            TextView tvRanking = itemView.findViewById(R.id.tv_rank);
            TextView tvPlayer = itemView.findViewById(R.id.tv_player);
            TextView tvScore = itemView.findViewById(R.id.tv_score);
            TextView[] tvBeginHole = new TextView[9];
            TextView[] tvEndHole = new TextView[9];
            TextView tvBeginTotal = itemView.findViewById(R.id.tv_beginHole);
            TextView tvEndTotal = itemView.findViewById(R.id.tv_endHole);

            int[] outHoleIds = {R.id.tv_beginHole1, R.id.tv_beginHole2, R.id.tv_beginHole3, R.id.tv_beginHole4, R.id.tv_beginHole5, R.id.tv_beginHole6, R.id.tv_beginHole7, R.id.tv_beginHole8, R.id.tv_beginHole9};
            int[] inHoleIds = {R.id.tv_endHole1, R.id.tv_endHole2, R.id.tv_endHole3, R.id.tv_endHole4, R.id.tv_endHole5, R.id.tv_endHole6, R.id.tv_endHole7, R.id.tv_endHole8, R.id.tv_endHole9};

            RankingDetailItemViewHolder(View view) {
                super(view);

                for (int i = 0; i < 9; i++) {
                    this.tvBeginHole[i] = itemView.findViewById(outHoleIds[i]);
                    this.tvEndHole[i] = itemView.findViewById(inHoleIds[i]);
                }
            }

        }

        @Override
        public int getItemCount() {
            return playerList.size();
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
                    NUM_OF_COURSE = response.getList().get(0).course.size(); //코스수를 지정한다. courseNum 을 요청할것
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
