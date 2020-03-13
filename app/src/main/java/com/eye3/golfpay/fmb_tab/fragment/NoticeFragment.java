package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.notice.NoticeItem;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;

import java.util.ArrayList;

public class NoticeFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    private ArrayList<NoticeItem> mNoticeItemList = new ArrayList<>();
    private NoticeAdapter mNoticeAdapter;
    private RecyclerView mRecyclerNotice;
    private TextView mTitleTextView, mContentTextView, mTimeTextView;
    private LinearLayout imageLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_notice, container, false);

        imageLayout = v.findViewById(R.id.imageLinearLayout);
        mRecyclerNotice = v.findViewById(R.id.recycler_notice);
        mTitleTextView = v.findViewById(R.id.titleTextView);
        mTimeTextView = v.findViewById(R.id.timeTextView);
        mContentTextView = v.findViewById(R.id.contentTextView);
        getNoticeList(getActivity());

        mParentActivity.showMainBottomBar();
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

    }

    private void initRecyclerView(ArrayList<NoticeItem> noticeList) {
        LinearLayoutManager mManager;
        if (noticeList == null)
            return;
        mManager = new LinearLayoutManager(mContext);
        mRecyclerNotice.setLayoutManager(mManager);

        mNoticeAdapter = new NoticeAdapter(mContext, noticeList);
        mRecyclerNotice.setAdapter(mNoticeAdapter);
        mNoticeAdapter.notifyDataSetChanged();
    }

    private void showNoticeDetail(NoticeItem item) {
        imageLayout.setVisibility(View.GONE);
        mTitleTextView.setText(item.mTitle);
        mTimeTextView.setText("작성일 " + item.mCreatedAt);
        mContentTextView.setText(Html.fromHtml(item.mContents));
    }


    private class NoticeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context context;
        ArrayList<NoticeItem> noticeList;


        NoticeAdapter(Context context, ArrayList<NoticeItem> noticeList) {
            this.context = context;
            this.noticeList = noticeList;

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder viewHolder = null;
            view = LayoutInflater.from(mContext).inflate(R.layout.notice_row, parent, false);
            viewHolder = new NoticeAdapter.NoticeItemViewHolder(view);

            return viewHolder;

        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (noticeList == null)
                return;
            NoticeItemViewHolder viewHolder = (NoticeItemViewHolder) holder;
            viewHolder.tvTitle.setText(noticeList.get(position).mTitle);
            viewHolder.tvContent.setText(Html.fromHtml(noticeList.get(position).mContents));
            viewHolder.tvDateTime.setText(noticeList.get(position).mCreatedAt);
            viewHolder.itemView.setTag(noticeList.get(position));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNoticeDetail((NoticeItem) v.getTag());
                }
            });
        }

        @Override
        public int getItemCount() {
            return noticeList.size();
        }


        class NoticeItemViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvContent, tvDateTime;
            ImageView imgNew;

            NoticeItemViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tvNoticeTitle);
                tvContent = view.findViewById(R.id.tvNoticeContent);
                tvDateTime = view.findViewById(R.id.tvNoticeDateTime);
                imgNew = view.findViewById(R.id.imgNew);


            }

        }
    }

    private void getNoticeList(Context context) {
        showProgress("랭킹 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getNoticeList(context, new DataInterface.ResponseCallback<ResponseData<NoticeItem>>() {
            @Override
            public void onSuccess(ResponseData<NoticeItem> response) {
                hideProgress();
                //나중에 수정할것
                if (response.getResultCode().equals("ok")) {
                    mNoticeItemList = (ArrayList<NoticeItem>) response.getList();
                    AppDef.previousCntOfNotice = mNoticeItemList.size();
                    initRecyclerView(mNoticeItemList);

                } else if (response.getResultCode().equals("fail")) {
                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(ResponseData<NoticeItem> response) {
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





