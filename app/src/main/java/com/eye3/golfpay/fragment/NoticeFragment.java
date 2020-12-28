package com.eye3.golfpay.fragment;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.R;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.notice.ArticleItem;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.net.Uri.withAppendedPath;
import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class NoticeFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    private List<ArticleItem> mNoticeItemList = new ArrayList<>();
    private NoticeAdapter mNoticeAdapter;
    private RecyclerView mRecyclerNotice;
    private TextView mTitleTextView, mContentTextView, mTimeTextView;
    private ImageView mImageView;

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

        mRecyclerNotice = v.findViewById(R.id.recycler_notice);
        mTitleTextView = v.findViewById(R.id.titleTextView);
        mTimeTextView = v.findViewById(R.id.timeTextView);
        mContentTextView = v.findViewById(R.id.contentTextView);
        mImageView = v.findViewById(R.id.iv_img);
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

    private void initRecyclerView(List<ArticleItem> noticeList) {
        LinearLayoutManager mManager;
        if (noticeList == null)
            return;
        mManager = new LinearLayoutManager(mContext);
        mRecyclerNotice.setLayoutManager(mManager);

        mNoticeAdapter = new NoticeAdapter(mContext, noticeList);
        mRecyclerNotice.setAdapter(mNoticeAdapter);
        mNoticeAdapter.notifyDataSetChanged();

        if (!noticeList.isEmpty())
            showNoticeDetail(noticeList.get(0));
    }

    private void showNoticeDetail(ArticleItem item) {
        mTitleTextView.setText(item.title);
        mTimeTextView.setText(item.created_at);
        mContentTextView.setText(item.content);

        Picasso.get()
                .load(Global.HOST_BASE_ADDRESS_AWS + item.file_url)
                .placeholder(mImageView.getDrawable())
                .into(mImageView);
    }

    private class NoticeAdapter extends RecyclerView.Adapter<ViewHolder> {

        Context context;
        List<ArticleItem> articleItems;

        NoticeAdapter(Context context, List<ArticleItem> noticeList) {
            this.context = context;
            this.articleItems = noticeList;
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
            if (articleItems == null)
                return;
            final NoticeItemViewHolder viewHolder = (NoticeItemViewHolder) holder;
            viewHolder.tvTitle.setText(articleItems.get(position).title);
            viewHolder.tvContent.setText(Html.fromHtml(articleItems.get(position).content));
            viewHolder.tvDateTime.setText(articleItems.get(position).created_at);
            viewHolder.itemView.setTag(articleItems.get(position));

//            if(articleItems.get(position).mUseYn.equals("y")) {
//                viewHolder.imgNew.setVisibility(View.GONE);
//                viewHolder.imgArrow.setVisibility(View.VISIBLE);
//            }
//            else {
//                viewHolder.imgNew.setVisibility(View.VISIBLE);
//                viewHolder.imgArrow.setVisibility(View.GONE);
//            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.imgNew.setVisibility(View.INVISIBLE);
                    showNoticeDetail((ArticleItem) v.getTag());
                }
            });
        }

        @Override
        public int getItemCount() {
            return articleItems.size();
        }


        class NoticeItemViewHolder extends ViewHolder {
            TextView tvTitle, tvContent, tvDateTime;
            ImageView imgNew, imgArrow;

            NoticeItemViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tvNoticeTitle);
                tvContent = view.findViewById(R.id.tvNoticeContent);
                tvDateTime = view.findViewById(R.id.tvNoticeDateTime);
                imgNew = view.findViewById(R.id.imgNew);
                imgArrow = view.findViewById(R.id.iv_arrow);
            }
        }
    }

    private void maekeNull(List<ArticleItem> list){
               list = null;
    }
    private void getNoticeList(Context context) {
        showProgress("공지사항 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getNoticeList(context, new DataInterface.ResponseCallback<ResponseData<ArticleItem>>() {
            @Override
            public void onSuccess(ResponseData<ArticleItem> response) {
                hideProgress();
                //나중에 수정할것
                if (response.getResultCode().equals("ok")) {
                    mNoticeItemList = (List<ArticleItem>) response.getList();
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
            public void onError(ResponseData<ArticleItem> response) {
                hideProgress();
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





