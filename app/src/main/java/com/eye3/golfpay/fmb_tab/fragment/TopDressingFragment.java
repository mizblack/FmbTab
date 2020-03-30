package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.gallery.GalleryPicture;
import com.eye3.golfpay.fmb_tab.model.top_dressing.TopDressing;

import java.util.List;
// 아이템뷰 처음 시작누르면  -> 종료 -> 시작종료시간 (togle 형식으로)
public class TopDressingFragment extends BaseFragment {
    List<TopDressing> mTopDressingScheduleList;
    RecyclerView mTopDressingRecycler;
    TopDressingScheduleAdapter mTopDressingAdatper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_top_dressing, container, false);

        //initRecyclerView();
        mParentActivity.showMainBottomBar();
        return v;
    }


    private void initRecyclerView(List<GalleryPicture> pictureList) {
        GridLayoutManager mManager = new GridLayoutManager(getActivity(), 2);
        mManager.setOrientation(RecyclerView.HORIZONTAL);
//        LinearLayoutManager horizonalLayoutManager
//                = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        if (pictureList == null)
            return;
        //    mManager = new LinearLayoutManager(mContext);
        // mGalleryRecycler.setHasFixedSize(true);
//        mGalleryRecycler.setLayoutManager(mManager);
//
//        mGalleryAdatper = new GalleryFragment.TopDressingScheduleAdapter(mContext, pictureList);
//        mGalleryRecycler.setAdapter(mGalleryAdatper);
//        mGalleryAdatper.notifyDataSetChanged();
    }

    private class TopDressingScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        List<GalleryPicture> mPictureList;

        public TopDressingScheduleAdapter(Context context, List<GalleryPicture> pictureList) {
            mContext = context;
            mPictureList = pictureList;
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
//            viewHolder.imageView.setTag(position);
//            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    GalleryViewFragment galleryViewFragment = new GalleryViewFragment(mPictureList, (int) v.getTag());
//                    assert getFragmentManager() != null;
//                    galleryViewFragment.show(getFragmentManager(), TAG);
//                    systemUIHide();
//
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return mPictureList.size();
        }

        public class TopDressingViewHolder extends RecyclerView.ViewHolder {
        //    ImageView imageView;

            public TopDressingViewHolder(@NonNull View itemView) {
                super(itemView);
               // imageView = itemView.findViewById(R.id.img_galllery);
            }
        }

    }

}
