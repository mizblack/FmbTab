package com.eye3.golfpay.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.dialog.ClubInfoDialog;
import com.eye3.golfpay.dialog.GameHoleDialog;
import com.eye3.golfpay.model.field.NearLong;
import com.eye3.golfpay.model.login.Login;
import com.eye3.golfpay.model.score.NearLongScoreBoard;
import com.eye3.golfpay.model.teeup.GuestDatum;
import com.eye3.golfpay.model.teeup.GuestScoreDB;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NearestLongestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearestLongestFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private ArrayList<TextView> nearestRankTextViews = new ArrayList<>();
    private ArrayList<TextView> longestRankTextViews = new ArrayList<>();

    public NearestLongestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NearestLongestFragment newInstance(String param1, String param2) {
        NearestLongestFragment fragment = new NearestLongestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            Bundle bundle = getArguments();
            if (bundle != null) {
            }
        }
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
                dlg.show();
            }
        });


        loadGuestScore();
        loadGuestScoreFromAPI();

        for (int i = 0; i < guestArrayList.size(); i++) {
            addPlayerList(view.findViewById(R.id.view_list), i, guestArrayList.size());
        }

        updateNearestRank();
        updateLongestRank();
        return view;
    }


    private void addPlayerList(LinearLayout container, final int index, int playerCount) {

        int height = 120;

        if (playerCount == 5) {
            height = 90;
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

        if (index % 2 == 0) {
            tvName.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightAliceBlue));
            tvNearest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightAliceBlue));
            tvLongest.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightAliceBlue));
        }

        tvNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberKeypad(tvNearest, index, "Nearest");
            }
        });

        tvLongest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberKeypad(tvLongest, index, "Longest");
            }
        });

        tvName.setText(guestArrayList.get(index).getGuestName());

        Float nearest = guestArrayList.get(index).getNearest();
        if (nearest > 0)
            tvNearest.setText(nearest.toString() + "M");

        Float longest = guestArrayList.get(index).getLongest();
        if (longest > 0)
            tvLongest.setText(longest.toString() + "M");

        nearestRankTextViews.add(tvNearestRank);
        longestRankTextViews.add(tvLongestRank);
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

    private void setLongestRank() {

        for (int i = 0; i < guestArrayList.size(); i++) {

            int rank = 1;
            float score = guestArrayList.get(i).getLongest();
            for (int j = 0; j < guestArrayList.size(); j++) {
                if (score < guestArrayList.get(j).getLongest()) {
                    rank++;
                }
            }

            if (guestArrayList.get(i).getLongest() > 0)
                guestArrayList.get(i).setLongestRank(rank);
        }

        for (int i = 0; i < guestArrayList.size(); i++) {
            //tvLongestRank.get(i).setText(getRankText(guestArrayList.get(i).getLongestRank()));

            final GuestScoreDB gsd = new GuestScoreDB();
            gsd.setGuest_id(guestArrayList.get(i).getId());
            gsd.setLongest(guestArrayList.get(i).getLongest());
            gsd.setLongestRank(guestArrayList.get(i).getLongestRank());

            Realm realm = Realm.getDefaultInstance();

            //데이터 넣기(insert)
            realm.executeTransaction(new Realm.Transaction() {
                @Override public void execute(Realm realm) {
                    // 쿼리를 해서 하나를 가져온다.
                    GuestScoreDB data = realm.where(GuestScoreDB.class).equalTo("guest_id", gsd.getGuest_id()).findFirst();

                    // Insert
                    if(data == null) {
                        GuestScoreDB insert = realm.createObject(GuestScoreDB.class);
                        insert.setGuest_id(gsd.getGuest_id());
                        insert.setLongest(gsd.getLongest());
                        insert.setLongestRank(gsd.getLongestRank());
                    }
                    // Update
                    else {
                        data.setGuest_id(gsd.getGuest_id());
                        data.setLongest(gsd.getLongest());
                        data.setLongestRank(gsd.getLongestRank());
                    }
                }
            });
        }

        updateLongestRank();
    }

    private void setNearestRank() {
        for (int i = 0; i < guestArrayList.size(); i++) {

            int rank = 1;
            float score = guestArrayList.get(i).getNearest();
            for (int j = 0; j < guestArrayList.size(); j++) {

                if (guestArrayList.get(j).getNearest() <= 0)
                    continue;

                if (score > guestArrayList.get(j).getNearest()) {
                    rank++;
                }
            }

            if (guestArrayList.get(i).getNearest() > 0)
                guestArrayList.get(i).setNearestRank(rank);
        }

        for (int i = 0; i < guestArrayList.size(); i++) {

            final GuestScoreDB gsd = new GuestScoreDB();
            gsd.setGuest_id(guestArrayList.get(i).getId());
            gsd.setNearest(guestArrayList.get(i).getNearest());
            gsd.setNearestRank(guestArrayList.get(i).getNearestRank());

            Realm realm = Realm.getDefaultInstance();

            //데이터 넣기(insert)
            realm.executeTransaction(new Realm.Transaction() {
                @Override public void execute(Realm realm) {
                    // 쿼리를 해서 하나를 가져온다.
                    GuestScoreDB data = realm.where(GuestScoreDB.class).equalTo("guest_id", gsd.getGuest_id()).findFirst();

                    // Insert
                    if(data == null) {
                        GuestScoreDB insert = realm.createObject(GuestScoreDB.class);
                        insert.setGuest_id(gsd.getGuest_id());
                        insert.setNearest(gsd.getNearest());
                        insert.setNearestRank(gsd.getNearestRank());
                    }
                    // Update
                    else {
                        data.setGuest_id(gsd.getGuest_id());
                        data.setNearest(gsd.getNearest());
                        data.setNearestRank(gsd.getNearestRank());
                    }
                }
            });
        }

        updateNearestRank();
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

    private void loadGuestScoreFromAPI() {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getGameTypeScore(getContext(), new DataInterface.ResponseCallback<NearLongScoreBoard>() {
            @Override
            public void onSuccess(NearLongScoreBoard response) {

            }

            @Override
            public void onError(NearLongScoreBoard response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void loadGuestScore() {
        //데이터 넣기(insert)
        for (GuestDatum guestDatum: guestArrayList) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // 쿼리를 해서 하나를 가져온다.
                    GuestScoreDB data = realm.where(GuestScoreDB.class).equalTo("guest_id", guestDatum.getId()).findFirst();
                    if (data != null) {
                        guestDatum.setLongest(data.getLongest());
                        guestDatum.setLongestRank(data.getLongestRank());
                        guestDatum.setNearest(data.getNearest());
                        guestDatum.setNearestRank(data.getNearestRank());
                    }
                }
            });
        }
    }

    private String getRankText(int rank) {
        switch(rank) {
            case 1: return "1위";
            case 2: return "2위";
            case 3: return "3위";
            case 4: return "4위";
            case 5: return "5위";
        }

        return "-";
    }
}