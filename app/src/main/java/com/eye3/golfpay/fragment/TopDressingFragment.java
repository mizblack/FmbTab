package com.eye3.golfpay.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.model.top_dressing.TopDressing;

import java.util.ArrayList;
import java.util.List;

// 아이템뷰 처음 시작누르면  -> 종료 -> 시작종료시간 (togle 형식으로)
public class TopDressingFragment extends BaseFragment {
    List<TopDressing> mTopDressingScheduleList = new ArrayList<>();
    RecyclerView mTopDressingRecycler;
    TopDressingScheduleAdapter mTopDressingAdatper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopDressingScheduleList.add(new TopDressing("08:15", "08:30", "OUT", "3"));
        mTopDressingScheduleList.add(new TopDressing("08:15", "08:30", "OUT", "3"));
        mTopDressingScheduleList.add(new TopDressing("08:15", "08:30", "OUT", "5"));
        mTopDressingScheduleList.add(new TopDressing("08:15", "08:30", "OUT", "3"));
        mTopDressingScheduleList.add(new TopDressing("08:15", "08:30", "OUT", "5"));
        mTopDressingScheduleList.add(new TopDressing("08:15", "08:30", "OUT", "3"));
        mTopDressingScheduleList.add(new TopDressing("08:15", "08:30", "OUT", "4"));


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_top_dressing, container, false);
        mTopDressingRecycler = v.findViewById(R.id.top_dressing_recycler);
        initRecyclerView(mTopDressingScheduleList);
        mParentActivity.showMainBottomBar();
        return v;
    }


    private void initRecyclerView(List<TopDressing> topDressingList) {

        if (topDressingList == null)
            return;
        LinearLayoutManager mManager = new LinearLayoutManager(mContext);
        mTopDressingRecycler.setHasFixedSize(true);
        mTopDressingRecycler.setLayoutManager(mManager);

        mTopDressingAdatper = new TopDressingScheduleAdapter(mContext, topDressingList);
        mTopDressingRecycler.setAdapter(mTopDressingAdatper);
        mTopDressingAdatper.notifyDataSetChanged();
    }

    private class TopDressingScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        List<TopDressing> mTopDressingList;

        public TopDressingScheduleAdapter(Context context, List<TopDressing> topDressingList) {
            mContext = context;
            mTopDressingList = topDressingList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            //  RecyclerView.ViewHolder viewHolder = null;
            view = LayoutInflater.from(mContext).inflate(R.layout.top_dressing_schedule_item, parent, false);
            return new TopDressingScheduleAdapter.TopDressingViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            TopDressingViewHolder viewHolder = (TopDressingViewHolder) holder;
            viewHolder.startTime.setText(mTopDressingList.get(position).start_time + " 시작");
            viewHolder.finishTime.setText(mTopDressingList.get(position).finish_time + " 종료");
        }

        @Override
        public int getItemCount() {
            return mTopDressingList.size();
        }

        public class TopDressingViewHolder extends RecyclerView.ViewHolder {
            Button startStop;
            TextView hole, course, startTime, finishTime;

            public TopDressingViewHolder(@NonNull View itemView) {
                super(itemView);
                startStop = itemView.findViewById(R.id.btn_top_dressing);
                hole = itemView.findViewById(R.id.hole);
                course = itemView.findViewById(R.id.course);
                startTime = itemView.findViewById(R.id.tv_top_dressing_start_time);
                finishTime = itemView.findViewById(R.id.tv_top_dressing_finish_time);
                startStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (startStop.getText().toString()) {
                            case "시작":
                                startStop.setText("종료");
                                startStop.setBackgroundColor(Color.RED);
                                itemView.setBackgroundColor(Color.BLACK);
                                course.setTextColor(Color.WHITE);
                                hole.setTextColor(Color.WHITE);
                                break;
                            case "종료":
                                startStop.setVisibility(View.GONE);
                                hole.setTextColor(Color.BLACK);
                                course.setTextColor(Color.BLACK);

                                itemView.setBackgroundColor(Color.WHITE);
                                break;
                        }
                    }
                });
            }
        }

    }


}
