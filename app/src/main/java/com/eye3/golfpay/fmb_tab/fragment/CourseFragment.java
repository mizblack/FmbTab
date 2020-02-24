package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.field.Hole;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;

import java.util.ArrayList;

public class CourseFragment extends BaseFragment {
    protected String TAG = getClass().getSimpleName();

    private ViewPager mMapPager;
    private ArrayList<Course> mCourseInfoList;
    private CoursePagerAdapter mCoursePagerAdapter;
//    private TextView mTvHoleNo, mTvHolePar, mTvCourseName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//        }

        getAllCourseInfo(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_course, container, false);
//        mTvHoleNo = v.findViewById(R.id.holeNo);
//        mTvHolePar = v.findViewById(R.id.holePar);
//        mTvCourseName = v.findViewById(R.id.courseName);

        mMapPager = v.findViewById(R.id.map_pager);
        (mParentActivity).hideMainBottomBar();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    class CoursePagerAdapter extends PagerAdapter {
        Context mContext;
        ArrayList<Hole> mHoleList;
        ImageView mIvMap;

        CoursePagerAdapter(Context context, ArrayList<Hole> mHoleList) {
            this.mContext = context;
            this.mHoleList = mHoleList;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.map_pager_page, container, false);
            mIvMap = view.findViewById(R.id.iv_map);
//            mTvHoleNo.setText(mHoleList.get(position).hole_no);
//            mTvHolePar.setText(mHoleList.get(position).par);
            if (mHoleList.get(position).img_2_file_url != null) {
                Glide.with(mContext)
                        .load("http://10.50.21.62:8000/" + mHoleList.get(position).img_2_file_url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_noimage)
                        .into(mIvMap);

            } else {
                mIvMap.setImageDrawable(getResources().getDrawable(R.drawable.ic_noimage, null));
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            // 뷰페이저에서 삭제.
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mHoleList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == object);
        }

        @Override
        public int getItemPosition(@NonNull Object item) {
            return POSITION_NONE;
        }

    }

    private void getAllCourseInfo(Context context) {
        showProgress("코스 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getCourseInfo(context, "1", new DataInterface.ResponseCallback<ResponseData<Course>>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(ResponseData<Course> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    mCourseInfoList = (ArrayList<Course>) response.getList();
                    Global.courseInfoList = mCourseInfoList;

                    mCoursePagerAdapter = new CoursePagerAdapter(getActivity(), mCourseInfoList.get(0).holes);
                    mMapPager.setAdapter(mCoursePagerAdapter);
//                    mMapPager.setCurrentItem(0);
//                    mTvCourseName.setText(mCourseInfoList.get(0).courseName + " COURSE");
//                    mTvHoleNo.setText(mCourseInfoList.get(0).holes.get(0).hole_no);
//                    mTvHolePar.setText(mCourseInfoList.get(0).holes.get(0).par);
                } else if (response.getResultCode().equals("fail")) {
                    Toast.makeText(getActivity(), response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(ResponseData<Course> response) {
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });
    }

}


