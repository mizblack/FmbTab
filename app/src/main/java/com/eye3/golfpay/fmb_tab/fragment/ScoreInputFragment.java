package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;

import java.util.ArrayList;
//사용안함.
public class ScoreInputFragment extends BaseFragment {


    protected String TAG = getClass().getSimpleName();
    ArrayList parScoreIntegerArrayList;
    ArrayList strokesScoreIntegerArrayList;
    ArrayList puttIntegerArrayList;
    ArrayList nearestIntegerArrayList;
    ArrayList longestIntegerArrayList;
    RecyclerView playersRecyclerView;
    ScoreInputAdapter mScoreInputAdapter;
    LinearLayoutManager mManager;
//    ArrayList<Player> mPlayerList = new ArrayList<>();
    ArrayList<GuestDatum> playerList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }

        playerList =   Global.teeUpTime.getTodayReserveList().get(0).getGuestData();

        TextView[] tvHit = new TextView[15];
        TextView[] tvPutt = new TextView[15];

        createIntegerArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fr_score_input, container, false);
        playersRecyclerView = v.findViewById(R.id.player_score_list);
        playersRecyclerView.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        playersRecyclerView.setLayoutManager(mManager);
        mScoreInputAdapter =  new ScoreInputAdapter(getActivity(), playerList);
        playersRecyclerView.setAdapter(mScoreInputAdapter);
        mScoreInputAdapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        SetTitle("KT WMMS");
//        SetDividerVisibility(false);
        //   setDrawerLayoutEnable(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    ArrayList<Integer> incrementsLoop(int a, int b, int increments) {
        //  singleton.log("incrementsLoop(" + a + ", " + b + ", " + increments + ")");
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for (int i = a; i <= b; ) {
            integerArrayList.add(i);
            i = i + increments;
        }
        return integerArrayList;
    }

    void createIntegerArrayList() {
        parScoreIntegerArrayList = incrementsLoop(2 * (-1), 10, 1);
        strokesScoreIntegerArrayList = incrementsLoop(1, 10, 1);
        puttIntegerArrayList = incrementsLoop(0, 10, 1);
        nearestIntegerArrayList = incrementsLoop(0, 20, 1);
        longestIntegerArrayList = incrementsLoop(100, 300, 10);
    }

    private class ScoreInputAdapter extends RecyclerView.Adapter<ScoreInputAdapter.ScoreInputItemViewHolder> {
        ArrayList<GuestDatum> playerList;
        protected LinearLayout ll_item;
        FmbCustomDialog fmbDialog;

        public ScoreInputAdapter(Context context, ArrayList<GuestDatum> playerList) {
            this.playerList = playerList;
        }

        public class ScoreInputItemViewHolder extends RecyclerView.ViewHolder {
            protected TextView[] tvPar = new TextView[9];
            protected TextView tvRank, tvName;

            public ScoreInputItemViewHolder(View view) {
                super(view);
//                tvRank = view.findViewById(R.id.rank);
//                tvName = view.findViewById(R.id.name);
//
//                tvPar[0] = view.findViewById(R.id.hole1);
//                tvPar[1] = view.findViewById(R.id.hole2);
//                tvPar[2] = view.findViewById(R.id.hole3);
//                tvPar[3] = view.findViewById(R.id.hole4);
//                tvPar[4] = view.findViewById(R.id.hole5);



            }

            private View.OnClickListener leftListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fmbDialog.dismiss();
                }
            };

            private View.OnClickListener rightListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            };
        }


        @NonNull
        @Override
        public ScoreInputItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.player_score_item, viewGroup, false);
            ScoreInputItemViewHolder viewHolder = new ScoreInputItemViewHolder(view);
            //  ll_item = view.findViewById(R.id.ll_schedule_item);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreInputItemViewHolder holder, int position) {
         //   scoreItemViewHolder.tvRank.setText(list[0]);
        }



        @Override
        public int getItemCount() {
            return playerList.size();
        }


    }


}


