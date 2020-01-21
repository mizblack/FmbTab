package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/*
 *  wmmms 메뉴 화면
 */
public class ScoreFragment extends BaseFragment {


    protected String TAG = getClass().getSimpleName();
    Button mBtnTaokeoverTest;

    private View tabBar;
    private TextView course01TextView;
    private TextView course02TextView;
    private TextView course03TextView;
    private View course01Tab;
    private View course02Tab;
    private View course03Tab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_score, container, false);

    }

    private void tabTitleOnClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == course01TextView) {
                    course01TextView.setTextColor(0xff000000);
                    course02TextView.setTextColor(0xffcccccc);
                    course03TextView.setTextColor(0xffcccccc);

                    course01Tab.setVisibility(View.VISIBLE);
                    course02Tab.setVisibility(View.GONE);
                    course03Tab.setVisibility(View.GONE);
                } else if (view == course02TextView) {
                    course01TextView.setTextColor(0xffcccccc);
                    course02TextView.setTextColor(0xff000000);
                    course03TextView.setTextColor(0xffcccccc);

                    course01Tab.setVisibility(View.GONE);
                    course02Tab.setVisibility(View.VISIBLE);
                    course03Tab.setVisibility(View.GONE);
                } else if (view == course03TextView) {
                    course01TextView.setTextColor(0xffcccccc);
                    course02TextView.setTextColor(0xffcccccc);
                    course03TextView.setTextColor(0xff000000);

                    course01Tab.setVisibility(View.GONE);
                    course02Tab.setVisibility(View.GONE);
                    course03Tab.setVisibility(View.VISIBLE);
                }
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabBar = Objects.requireNonNull(getView()).findViewById(R.id.tab_bar);
        course01TextView = tabBar.findViewById(R.id.course01Text);
        course02TextView = tabBar.findViewById(R.id.course02Text);
        course03TextView = tabBar.findViewById(R.id.course03Text);
        course01Tab = getView().findViewById(R.id.course01Tab);
        course02Tab = getView().findViewById(R.id.course02Tab);
        course03Tab = getView().findViewById(R.id.course03Tab);

        tabTitleOnClick(course01TextView);
        tabTitleOnClick(course02TextView);
        tabTitleOnClick(course03TextView);

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

    private class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreItemViewHolder> {

        //     private ArrayList<AllocationSchedule> mList;
        //     private WeakReference<MainActivity> activityWeakReference;
        protected LinearLayout ll_item;
        private boolean allocatedCheck;  // Default: false


        public class ScoreItemViewHolder extends RecyclerView.ViewHolder {
            protected TextView scheduleTime;
            protected TextView depature;
            protected TextView destination;
            protected TextView supplyExist;
            protected TextView imgNum;
            protected TextView beforeSchedule;

            public ScoreItemViewHolder(View view) {
                super(view);
//                this.scheduleTime = view.findViewById(R.id.schedule_time);
//                this.beforeSchedule = view.findViewById(R.id.before_schedule_time);
//                this.depature = view.findViewById(R.id.departure);
//                this.destination = view.findViewById(R.id.destination);
//                this.supplyExist = view.findViewById(R.id.supply);
//                this.imgNum = view.findViewById(R.id.schedule_num);
            }
        }


        public ScoreAdapter(Context context, ArrayList<String> list) {
            // activityWeakReference = new WeakReference<>((MainActivity)context);
            //  this.mList = list;
            //   allocatedCheck = false;
        }


        // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
        @Override
        public ScoreItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.score_row, viewGroup, false);

            ScoreItemViewHolder viewHolder = new ScoreItemViewHolder(view);
            //  ll_item = view.findViewById(R.id.ll_schedule_item);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreItemViewHolder scheduleItem, int i) {
            final int pos = i;
//            scheduleItem.imgNum.setText(String.valueOf(i + 1));
//            scheduleItem.scheduleTime.setText(getReserveTimeIn("HH:mm", mList.get(i).resvDatetime));
//            scheduleItem.beforeSchedule.setText(getDifferenceFromResvTime(mList.get(i).resvDatetime) );
//            scheduleItem.depature.setText( "출발지: " +mList.get(i).resvOrgPoi);
//            scheduleItem.destination.setText( "도착지: " + mList.get(i).resvDstPoi);
//
//            if(mList.get(i).serviceCount > 0) {
//                scheduleItem.supplyExist.setVisibility(View.VISIBLE);
//            } else {
//                scheduleItem.supplyExist.setVisibility(View.INVISIBLE);
//            }
//
//            if(!mList.get(pos).allocationStatus.equalsIgnoreCase("ALLOCATED") && !allocatedCheck) {
//                // 제일 처음 들어오는 항목이고, 배차상태가 ALLOCATED가 아닐경우 Idx를 요청하고나서 각 상태별 화면으로 이동시킴
//                getAllocDetail(mList.get(pos).allocationIdx, mList.get(pos).allocationStatus);
//            }
//
//            ll_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    checkAllocationStatus(mList.get(pos).allocationStatus, pos);
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return 0;
        }


    }


}


