package com.eye3.golfpay.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.dialog.GameHoleDialog;
import com.eye3.golfpay.model.field.NearLong;
import com.eye3.golfpay.model.order.ReserveGameType;
import com.eye3.golfpay.model.score.NearLongScoreBoard;
import com.eye3.golfpay.model.teeup.GuestDatum;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.Util;

import java.util.ArrayList;

public class NearestLongestFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<NearLong> totalPlayerList;
    private final ArrayList<GuestDatum> guestArrayList = Global.selectedReservation.getGuestData();
    private final ArrayList<TextView> nearestRankTextViews = new ArrayList<>();
    private final ArrayList<TextView> longestRankTextViews = new ArrayList<>();
    private final ArrayList<TextView> nearestScoreTextViews = new ArrayList<>();
    private final ArrayList<TextView> longestScoreTextViews = new ArrayList<>();

    private TextView tvNearHolePar;
    private TextView tvLongHolePar;

    public NearestLongestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_nearest_longest, container, false);

        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        view.findViewById(R.id.btn_game_hole).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameHoleDialog dlg = new GameHoleDialog(getContext());
                WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;
                dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getNearLongHole();
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                    }
                });
                dlg.show();
            }
        });

        //loadGuestScore();
        tvNearHolePar = view.findViewById(R.id.tv_near_hole_par);
        tvLongHolePar = view.findViewById(R.id.tv_long_hole_par);
        getNearLongHole();






        initScoreBoard(view);

        return view;
    }


    private void addPlayerList(LinearLayout container, boolean isActiveUser, final int index, int playerCount) {

        int height = 120;

        if (playerCount >= 5) {
            height = 80;
        }

        LinearLayout childView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_nearest_longest, null, false);
        final int heightDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightDP);
        childView.setLayoutParams(params);

        TextView tvName = childView.findViewById(R.id.tv_name);
        TextView tvNearestRank = childView.findViewById(R.id.tv_nearest_rank);
        TextView tvLongestRank = childView.findViewById(R.id.tv_longest_rank);
        TextView tvNearest = childView.findViewById(R.id.tv_nearest);
        TextView tvLongest = childView.findViewById(R.id.tv_longest);
        String guestId = Integer.toString(totalPlayerList.get(index).id);
        if (isActiveUser) {
            tvName.setTextAppearance(R.style.GlobalTextView_22SP_irisBlue_NotoSans_Medium);
        } else {
            tvName.setTextAppearance(R.style.GlobalTextView_22SP_ebonyBlack_NotoSans_Medium);
        }

        if (index % 2 == 0) {
            tvName.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightAliceBlue));
            tvNearest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightAliceBlue));
            tvLongest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightAliceBlue));
        }

        tvNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActiveUser(guestId))
                    showNumberKeypad(tvNearest, index, "Nearest");
                else
                    Toast.makeText(mContext, "현재 진행중인 플레이어만 입력할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        tvLongest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActiveUser(guestId))
                    showNumberKeypad(tvLongest, index, "Longest");
                else
                    Toast.makeText(mContext, "현재 진행중인 플레이어만 입력할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        tvName.setText(totalPlayerList.get(index).guestName);
        nearestRankTextViews.add(tvNearestRank);
        longestRankTextViews.add(tvLongestRank);
        nearestScoreTextViews.add(tvNearest);
        longestScoreTextViews.add(tvLongest);
        container.addView(childView);
    }

    private void showNumberKeypad(TextView textView, final int index, final String type) {
        NumberkeypadDialogFragment numberkeypadDialogFragment = new NumberkeypadDialogFragment();

        FragmentManager supportFragmentManager = ((MainActivity) (getContext())).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        numberkeypadDialogFragment.show(transaction, TAG);
        assert numberkeypadDialogFragment.getFragmentManager() != null;
        numberkeypadDialogFragment.getFragmentManager().executePendingTransactions();
        numberkeypadDialogFragment.setiNumberkeypadListener(new NumberkeypadDialogFragment.INumberkeypadListener() {
            @Override
            public void onConfirm(String number) {

                if (guestArrayList.size() == 0) {
                    Toast.makeText(mContext, "게스트 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int guestId = totalPlayerList.get(index).id;
                if (type.equals("Nearest")) {
                    setGuestScoreFromAPI(guestId, "near", number);
                }
                else {
                    setGuestScoreFromAPI(guestId, "long", number);
                }

                /*
                if (number.isEmpty()) {
                    textView.setText("-");

                    if (type.equals("Nearest")) {
                        guestArrayList.get(index).setNearest(0);
                        setNearestRank();
                    }
                    else {
                        guestArrayList.get(index).setLongest(0);
                        setLongestRank();
                    }

                } else {
                    textView.setText(number + "M");
                    if (type.equals("Nearest")) {
                        guestArrayList.get(index).setNearest(Float.parseFloat(number));
                        setNearestRank();
                    }
                    else {
                        guestArrayList.get(index).setLongest(Float.parseFloat(number));
                        setLongestRank();
                    }
                }
        */
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void close() {
        Bundle bundle = new Bundle();
        bundle.putString("ani_direction", "down");
        GoNativeScreen(new ScoreFragment(), bundle);
    }

    private void updateNearestRank() {
        for (int i = 0; i < nearestRankTextViews.size(); i++) {
            TextView tvRank = nearestRankTextViews.get(i);
            String rankText = getRankText(guestArrayList.get(i).getNearestRank());
            tvRank.setText(rankText);
        }
    }

    private void updateLongestRank() {
        for (int i = 0; i < longestRankTextViews.size(); i++) {
            TextView tvRank = longestRankTextViews.get(i);
            String rankText = getRankText(guestArrayList.get(i).getLongestRank());
            tvRank.setText(rankText);
        }
    }

    private void updateNearestRank(NearLongScoreBoard response) {

        try {
            for (int i = 0; i < response.guest_gametype_ranking.size(); i++) {
                TextView tvRank = nearestRankTextViews.get(i);
                String rankText = getRankText(response.guest_gametype_ranking.get(i).near_ranking);
                tvRank.setText(rankText);
                setScore(response.guest_gametype_ranking.get(i).nearest, nearestScoreTextViews.get(i));
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private boolean isActiveUser(String id) {

        for (int i = 0; i < Global.selectedReservation.getGuestData().size(); i++) {
            GuestDatum guestDatum = Global.selectedReservation.getGuestData().get(i);
            if (guestDatum.getId().equals(id)) {
                return true;
            }

        }
        return false;
    }

    private void updateLongestRank(NearLongScoreBoard response) {
        try {
            for (int i = 0; i < response.guest_gametype_ranking.size(); i++) {
                TextView tvRank = longestRankTextViews.get(i);
                String rankText = getRankText(response.guest_gametype_ranking.get(i).long_ranking);
                tvRank.setText(rankText);
                setScore(response.guest_gametype_ranking.get(i).longest, longestScoreTextViews.get(i));
            }
        }catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void setScore(String score, TextView textView) {
        if (score.isEmpty())
            textView.setText("-");
        else
            textView.setText(score + "M");
    }

    private void initScoreBoard(View view) {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getGameTypeScore(getContext(), new DataInterface.ResponseCallback<NearLongScoreBoard>() {
            @Override
            public void onSuccess(NearLongScoreBoard response) {

                totalPlayerList = response.guest_gametype_ranking;
                for (int i = 0; i < response.guest_gametype_ranking.size(); i++) {
                    NearLong nearLong = response.guest_gametype_ranking.get(i);
                    String id = Integer.toString(response.guest_gametype_ranking.get(i).id);
                    addPlayerList(view.findViewById(R.id.view_list), isActiveUser(id), i, response.guest_gametype_ranking.size());
                }

                loadGuestScoreFromAPI();
            }

            @Override
            public void onError(NearLongScoreBoard response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void loadGuestScoreFromAPI() {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getGameTypeScore(getContext(), new DataInterface.ResponseCallback<NearLongScoreBoard>() {
            @Override
            public void onSuccess(NearLongScoreBoard response) {
                updateNearestRank(response);
                updateLongestRank(response);
            }

            @Override
            public void onError(NearLongScoreBoard response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getNearLongHole() {

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGameType(getContext(), new DataInterface.ResponseCallback<ReserveGameType>() {
            @Override
            public void onSuccess(ReserveGameType response) {
                if (response.ret_code.equals("ok")) {
                    int holeNear = response.hole_no_near;
                    int holeLong = response.hole_no_long;
                    int parNear = response.par_near;
                    int parLong = response.par_long;

                    tvNearHolePar.setText(String.format("Hole %d / Par %d", holeNear, parNear));
                    tvLongHolePar.setText(String.format("Hole %d / Par %d", holeLong, parLong));
                }
            }

            @Override
            public void onError(ReserveGameType response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setGuestScoreFromAPI(int guest_id, String game_type, String distance) {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setGameTypeScore(getContext(), guest_id, game_type, distance, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                loadGuestScoreFromAPI();
            }

            @Override
            public void onError(ResponseData<Object> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private String getRankText(int rank) {
        if (rank == 0) {
            return "-";
        }

        return String.format("%d위", rank);
    }
}