package com.eye3.golfpay.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.adapter.NearestLongestAdapter;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.UIThread;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.teeup.GuestDatum;
import com.eye3.golfpay.model.teeup.GuestScoreDB;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class NearestLongestDialogFragment extends DialogFragment {

    protected ProgressDialog pd; // 프로그레스바 선언

    protected String TAG = getClass().getSimpleName();
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private LinearLayout guestItemLinearLayout;
    private View cancelLinearLayout;
    private TextView mTabLongest, mTabNearest;

    private ArrayList<TextView> tvNearestRank = new ArrayList<>();
    private ArrayList<TextView> tvLongestRank = new ArrayList<>();

    private String mNearestOrLongest = AppDef.NEAREST;
    private  Course mCourse;
    private Hole mHole;

    private TextView tv_1st;
    private TextView tv_2nd;
    private TextView tv_2nd_name;
    private TextView tv_2nd_score;
    private TextView tv_3rd;
    private TextView tv_3rd_name;
    private TextView tv_3rd_score;

    private RecyclerView recyclerView;
    LinearLayoutManager learingLayout;
    NearestLongestAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frd_nearest_longest, container, false);

        mNearestOrLongest = AppDef.LONGEST;
        guestItemLinearLayout = view.findViewById(R.id.guestItemLinearLayout);
        cancelLinearLayout = view.findViewById(R.id.cancelLinearLayout);
        mTabLongest = view.findViewById(R.id.longestTextView);
        mTabLongest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mNearestOrLongest.equals(AppDef.NEAREST)) {
                    mTabNearest.setTextAppearance(R.style.GlobalTextView_28SP_gray_NotoSans_Medium);
                    ( (TextView)getView().findViewById(R.id.measurement_unit)).setTextAppearance(R.style.GlobalTextView_16SP_irisBlue_NotoSans_Medium);
                }
                mNearestOrLongest = AppDef.LONGEST;
                ((TextView) v).setTextAppearance(R.style.GlobalTextView_28SP_ebonyBlack_NotoSans_Medium);
                changeScreenTo(AppDef.LONGEST);

            }
        });
        mTabNearest = view.findViewById(R.id.nearestTextView);
        mTabNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNearestOrLongest.equals(AppDef.LONGEST)) {
                    mTabLongest.setTextAppearance(R.style.GlobalTextView_28SP_gray_NotoSans_Medium);
                    ( (TextView)getView().findViewById(R.id.measurement_unit)).setTextAppearance(R.style.GlobalTextView_14SP_irisBlue_NotoSans_Medium);
                }
                mNearestOrLongest = AppDef.NEAREST;
                ((TextView) v).setTextAppearance(R.style.GlobalTextView_28SP_ebonyBlack_NotoSans_Medium);
                changeScreenTo(AppDef.NEAREST);
            }
        });
//        ( (TextView)view.findViewById(R.id.long_near_hole_no)).setText("Hole "+ mHole.hole_no);
//        ( (TextView)view.findViewById(R.id.long_near_par)).setText("Par" + mHole.par);
//        ( (TextView)view.findViewById(R.id.long_near_course)).setText("Course(" + mCourse.courseName + ")");

        tv_1st = view.findViewById(R.id.tv_1st);
        tv_2nd = view.findViewById(R.id.tv_2nd);
        tv_2nd_name = view.findViewById(R.id.tv_2nd_name);
        tv_2nd_score = view.findViewById(R.id.tv_2nd_score);
        tv_3rd = view.findViewById(R.id.tv_3rd);
        tv_3rd_name = view.findViewById(R.id.tv_3rd_name);
        tv_3rd_score = view.findViewById(R.id.tv_3rd_score);
        return view;
    }



    public void setmNearestOrLongest(String NearestLongest) {
        mNearestOrLongest = NearestLongest;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void makeNearest() {

        tvNearestRank = new ArrayList<>();
        guestItemLinearLayout.removeAllViewsInLayout();
        ( (TextView)getView().findViewById(R.id.measurement_unit)).setText("Centimeters");
        if (guestArrayList.size() != 0) {
            for (int i = 0; i < guestArrayList.size(); i++) {

                final int pos = i;
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.item_nearest_longest_input, null, false);

                LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)Util.convertDpToPixel(90.f, getContext()));
                v.setLayoutParams(lllp);

                recyclerView = v.findViewById(R.id.reyclerview);
                learingLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(learingLayout);


                final TextView tvMeters = v.findViewById(R.id.metersTextView);
                TextView nameTextView = v.findViewById(R.id.nameTextView);
                tvNearestRank.add(v.findViewById(R.id.rankingTextView));

                if (i == 0) {
                    recyclerView.setPadding(0, 2, 0, 0);
                    LinearLayout llmetersTextView = v.findViewById(R.id.llmetersTextView);
                    llmetersTextView.setPadding(0, 2, 0, 2);
                }

                adapter = new NearestLongestAdapter(getContext(), NearestLongestAdapter.Unit.Centimeters, new NearestLongestAdapter.IOnClickAdapter() {
                    @Override
                    public void onAdapterItemClicked(final Integer id) {
                        tvMeters.setText(adapter.getItem(id) + "CM");
                        guestArrayList.get(pos).setNearest(adapter.getItem(id));
                        setNearestRank();
                    }
                });

                recyclerView.setAdapter(adapter);
                nameTextView.setText(guestArrayList.get(i).getGuestName());
                guestItemLinearLayout.addView(v);

                adapter.setScore(guestArrayList.get(pos).getNearest());
            }

            setNearestRank();
        }
    }

    private void makeLongest() {

        tvLongestRank = new ArrayList<>();
        guestItemLinearLayout.removeAllViewsInLayout();
        ( (TextView)getView().findViewById(R.id.measurement_unit)).setText("Meters");
        if (guestArrayList.size() != 0) {
            for (int i = 0; i < guestArrayList.size(); i++) {

                final int pos = i;
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.item_nearest_longest_input, null, false);

                LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)Util.convertDpToPixel(90.f, getContext()));
                v.setLayoutParams(lllp);

                recyclerView = v.findViewById(R.id.reyclerview);
                learingLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(learingLayout);

                final TextView tvMeters = v.findViewById(R.id.metersTextView);
                TextView nameTextView = v.findViewById(R.id.nameTextView);
                tvLongestRank.add(v.findViewById(R.id.rankingTextView));

                if (i == 0) {
                    recyclerView.setPadding(0, 2, 0, 0);
                    LinearLayout llmetersTextView = v.findViewById(R.id.llmetersTextView);
                    llmetersTextView.setPadding(0, 2, 0, 2);
                }

                adapter = new NearestLongestAdapter(getContext(), NearestLongestAdapter.Unit.Meters, new NearestLongestAdapter.IOnClickAdapter() {
                    @Override
                    public void onAdapterItemClicked(final Integer id) {
                        tvMeters.setText(adapter.getItem(id) + "M");
                        guestArrayList.get(pos).setLongest(adapter.getItem(id));
                        setLongestRank();
                    }
                });

                recyclerView.setAdapter(adapter);
                nameTextView.setText(guestArrayList.get(i).getGuestName());
                guestItemLinearLayout.addView(v);

                adapter.setScore(guestArrayList.get(pos).getLongest());
            }

            setLongestRank();
        }
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

        for (int i = 0; i < tvLongestRank.size(); i++) {
            tvLongestRank.get(i).setText(getRankText(guestArrayList.get(i).getLongestRank()));

            final GuestScoreDB gsd = new GuestScoreDB();
            gsd.setGuest_id(guestArrayList.get(i).getId());
            gsd.setLongest(guestArrayList.get(i).getLongest());
            gsd.setLongestRank(guestArrayList.get(i).getLongestRank());
        }

        updateLongestRank();
    }

    private void setNearestRank() {
        for (int i = 0; i < guestArrayList.size(); i++) {

            int rank = 1;
            float score = guestArrayList.get(i).getNearest();
            for (int j = 0; j < guestArrayList.size(); j++) {

                if (guestArrayList.get(j).getNearest() < 0)
                    continue;

                if (score > guestArrayList.get(j).getNearest()) {
                    rank++;
                }
            }

            if (guestArrayList.get(i).getNearest() >= 0)
                guestArrayList.get(i).setNearestRank(rank);
        }

        for (int i = 0; i < tvNearestRank.size(); i++) {
            tvNearestRank.get(i).setText(getRankText(guestArrayList.get(i).getNearestRank()));

            final GuestScoreDB gsd = new GuestScoreDB();
            gsd.setGuest_id(guestArrayList.get(i).getId());
            gsd.setNearest(guestArrayList.get(i).getNearest());
            gsd.setNearestRank(guestArrayList.get(i).getNearestRank());


        }

        updateNearestRank();
    }

    private String getRankText(int rank) {
        switch(rank) {
            case 1: return "1st";
            case 2: return "2nd";
            case 3: return "3rd";
            case 4: return "4th";
            case 5: return "5th";
        }

        return "-";
    }

    private void updateLongestRank() {

        ArrayList<GuestDatum> temp = (ArrayList<GuestDatum>)guestArrayList.clone();
        Collections.sort(temp, new Comparator<GuestDatum>() {
            @Override
            public int compare(GuestDatum o1, GuestDatum o2) {
                return o1.getLongestRank() - o2.getLongestRank();
            }
        });

        for (int i = 0; i < temp.size(); i++) {

            if (temp.get(i).getLongest() < 0)
                continue;

            if (i == 0) {
                String rankText = String.format("%s\n%dM", temp.get(i).getGuestName(), temp.get(i).getLongest());
                tv_1st.setText(rankText);
            }
            else if (i == 1) {
                tv_2nd.setText(getRankText(temp.get(i).getLongestRank()));
                tv_2nd_name.setText(temp.get(i).getGuestName());
                tv_2nd_score.setText(temp.get(i).getLongest() + "M");
            }
            else if (i == 2) {
                tv_3rd.setText(getRankText(temp.get(i).getLongestRank()));
                tv_3rd_name.setText(temp.get(i).getGuestName());
                tv_3rd_score.setText(temp.get(i).getLongest() + "M");
            }
        }
    }

    private void updateNearestRank() {

        ArrayList<GuestDatum> temp = (ArrayList<GuestDatum>)guestArrayList.clone();
        Collections.sort(temp, new Comparator<GuestDatum>() {
            @Override
            public int compare(GuestDatum o1, GuestDatum o2) {
                return o1.getNearestRank() - o2.getNearestRank();
            }
        });

        for (int i = 0; i < temp.size(); i++) {

            if (temp.get(i).getNearest() < 0)
                continue;

            if (i == 0) {
                String rankText = String.format("%s\n%dM", temp.get(i).getGuestName(), temp.get(i).getNearest());
                tv_1st.setText(rankText);
            }
            else if (i == 1) {
                tv_2nd.setText(getRankText(temp.get(i).getNearestRank()));
                tv_2nd_name.setText(temp.get(i).getGuestName());
                tv_2nd_score.setText(temp.get(i).getNearest() + "CM");
            }
            else if (i == 2) {
                tv_3rd.setText(getRankText(temp.get(i).getNearestRank()));
                tv_3rd_name.setText(temp.get(i).getGuestName());
                tv_3rd_score.setText(temp.get(i).getNearest() + "CM");
            }
        }
    }

    private void clearRank() {
        tv_1st.setText("-");
        tv_2nd.setText("-");
        tv_2nd_name.setText("");
        tv_2nd_score.setText("");

        tv_3rd.setText("-");
        tv_3rd_name.setText("");
        tv_3rd_score.setText("");
    }

    private void changeScreenTo(String nearestOrLongest) {

        clearRank();
        if (nearestOrLongest.equals(AppDef.NEAREST))
            makeNearest();
        else
            makeLongest();
    }

    public void setHole(Hole hole){
        this.mHole = hole;
    }
    public void setCourse(Course course){
        this.mCourse = course;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mNearestOrLongest.equals(AppDef.NEAREST)){
            mTabNearest.performClick();
            ( (TextView)getView().findViewById(R.id.measurement_unit)).setText("Centimeters");
        } else {
            mTabLongest.performClick();
        }

        cancelLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private void getLongest() {
        showProgress("Longest정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveScore(getActivity(), Global.reserveId, "null", new DataInterface.ResponseCallback<ResponseData<Player>>() {
            @Override
            public void onSuccess(ResponseData<Player> response) {
//                hideProgress();
//                if (response.getResultCode().equals("ok")) {
//                    mPlayerList = (ArrayList<Player>) response.getList();
//                    mCourseList = getCourse(mPlayerList);
//                    NUM_OF_COURSE = response.getList().get(0).playingCourse.size(); //코스수를 지정한다. courseNum 을 요청할것
//                    CourseTabBar = new TextView[NUM_OF_COURSE];
//
//                    createTabBar(CourseTabBar, mCourseList);
//                    initScoreBoard();

//                } else if (response.getResultCode().equals("fail")) {
//                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
//                }
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

    protected void showProgress(final String msg) {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null) {
                    // 객체를 1회만 생성한다.
                    pd = new ProgressDialog(getActivity()); // 생성한다.
                    pd.setCancelable(true);
                    // 백키로 닫는 기능을 제거한다.
                }
                pd.setMessage(msg);
                // 원하는 메시지를 세팅한다.
                pd.show();
                // 화면에 띠워라
            }
        });
    }

    // 프로그레스 다이얼로그 숨기기
    protected void hideProgress() {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd != null && pd.isShowing()) {
                    // 닫는다 : 객체가 존재하고, 보일때만
                    pd.dismiss();
                }
            }
        });
    }
}
