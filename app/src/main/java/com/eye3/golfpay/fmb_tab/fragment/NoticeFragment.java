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
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.notice.NoticeItem;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;

import java.util.ArrayList;
import java.util.List;

public class NoticeFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    private List<NoticeItem> mNoticeItemList = new ArrayList<>();
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

    private void initRecyclerView(List<NoticeItem> noticeList) {
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

        if(item.mUseYn.equals("Y") ) {
            item.mUseYn = "y";

        }
        imageLayout.setVisibility(View.GONE);
        mTitleTextView.setText(item.mTitle);
        mTimeTextView.setText("작성일 " + item.mCreatedAt);
        mContentTextView.setText(Html.fromHtml(item.mContents));
    }


    private class NoticeAdapter extends RecyclerView.Adapter<ViewHolder> {

        Context context;
        List<NoticeItem> noticeList;


        NoticeAdapter(Context context, List<NoticeItem> noticeList) {
            this.context = context;
            this.noticeList = noticeList;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            ViewHolder viewHolder = null;
            view = LayoutInflater.from(mContext).inflate(R.layout.notice_row, parent, false);
            viewHolder = new NoticeAdapter.NoticeItemViewHolder(view);

            return viewHolder;

        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (noticeList == null)
                return;
            final NoticeItemViewHolder viewHolder = (NoticeItemViewHolder) holder;
            viewHolder.tvTitle.setText(noticeList.get(position).mTitle);
            viewHolder.tvContent.setText(Html.fromHtml(noticeList.get(position).mContents));
            viewHolder.tvDateTime.setText(noticeList.get(position).mCreatedAt);
            viewHolder.itemView.setTag(noticeList.get(position));
            if(noticeList.get(position).mUseYn.equals("y"))
                viewHolder.imgNew.setVisibility(View.INVISIBLE);
            else
                viewHolder.imgNew.setVisibility(View.VISIBLE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.imgNew.setVisibility(View.INVISIBLE);
                    showNoticeDetail((NoticeItem) v.getTag());
                }
            });
        }

        @Override
        public int getItemCount() {
            return noticeList.size();
        }


        class NoticeItemViewHolder extends ViewHolder {
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

    private void maekeNull(List<NoticeItem> list){
               list = null;
    }
    private void getNoticeList(Context context) {
        showProgress("공지사항 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getNoticeList(context, new DataInterface.ResponseCallback<ResponseData<NoticeItem>>() {
            @Override
            public void onSuccess(ResponseData<NoticeItem> response) {
                hideProgress();
                //나중에 수정할것
                if (response.getResultCode().equals("ok")) {
                    mNoticeItemList = (List<NoticeItem>) response.getList();
                    AppDef.previousCntOfNotice =  Global.noticeItemArrayList.size();
                    if(Global.noticeItemArrayList.size() == 0)
                        Global.noticeItemArrayList = mNoticeItemList;
                    if(Global.noticeItemArrayList.size() < mNoticeItemList.size()) {
                        for(int i = Global.noticeItemArrayList.size() -1; mNoticeItemList.size() > i ;i++) {
                            Global.noticeItemArrayList.add(mNoticeItemList.get(i));
                        }
                    }

                    initRecyclerView(Global.noticeItemArrayList);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNoticeItemList = null;
    }
}





