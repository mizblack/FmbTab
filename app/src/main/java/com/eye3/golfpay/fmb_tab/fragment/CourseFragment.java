package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.view.TabCourseLinear;

import java.util.ArrayList;

public class CourseFragment extends BaseFragment {
    protected String TAG = getClass().getSimpleName();

    ViewPager mMapPager;
    ArrayList<Course> mCourseInfoList;
    CoursePagerAdapter mCoursePagerAdapter;
    TextView mTvHoleNo, mTvHolePar, mTvCourseName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }

        getAllCourseInfo(getActivity(), "1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_course, container, false);
        mTvHoleNo = v.findViewById(R.id.holeNo);
        mTvHolePar = v.findViewById(R.id.holePar);
        mTvCourseName = v.findViewById(R.id.courseName);

        mMapPager = (ViewPager) v.findViewById(R.id.map_pager) ;
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
        Hole[] mHoleList;
        TextView mHoleId;
        ImageView mIvMap;

        public CoursePagerAdapter(Context context, Hole[] mHoleList) {
            this.mContext = context;
            this.mHoleList = mHoleList;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = null;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.map_pager_page, container, false);
            mIvMap =  view.findViewById(R.id.iv_map);
            mTvHoleNo.setText(mHoleList[position].hole_no);
            mTvHolePar.setText(mHoleList[position].par);
            if(mHoleList[position].img_2_file_url != null) {
               Glide.with(mContext)
                       .load("http://10.50.21.62:8000/" + mHoleList[position].img_2_file_url)
                       .diskCacheStrategy(DiskCacheStrategy.NONE)
                       .placeholder(R.drawable.ic_noimage)
                       .into(mIvMap);

           }else{
               mIvMap.setImageDrawable(getResources().getDrawable(R.drawable.ic_noimage));
           }
        //    return super.instantiateItem(container, position);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 뷰페이저에서 삭제.
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mHoleList.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

            return (view == (View) object);
         //   return false;
        }

        @Override
        public int getItemPosition(Object item) {


//        if (ischanged()) {
//            return POSITION_UNCHANGED;
//        } else {
//            // after this, onCreateView() of Fragment is called.
//            return POSITION_NONE;   // notifyDataSetChanged
//        }
           return POSITION_NONE;

        }

    }



    void getAllCourseInfo(Context context, String cc_id){
        showProgress("코스 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getCourseInfo(context, cc_id,  new DataInterface.ResponseCallback<ResponseData<Course>>() {


            @Override
            public void onSuccess(ResponseData<Course> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                   mCourseInfoList = ( ArrayList<Course> ) response.getList();
                    Global.courseInfoList = mCourseInfoList;

                    mCoursePagerAdapter = new CoursePagerAdapter(getActivity(), mCourseInfoList.get(0).holes) ;
                    mMapPager.setAdapter(mCoursePagerAdapter) ;
                    mMapPager.setCurrentItem(0);
                    mTvCourseName.setText( mCourseInfoList.get(0).courseName + " COURSE");
                    mTvHoleNo.setText(mCourseInfoList.get(0).holes[0].hole_no);
                    mTvHolePar.setText(mCourseInfoList.get(0).holes[0].par);
                }else if(response.getResultCode().equals("fail")){
                    Toast.makeText(getActivity(), response.getResultMessage() , Toast.LENGTH_SHORT).show();
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


