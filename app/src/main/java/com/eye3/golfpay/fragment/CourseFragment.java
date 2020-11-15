package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.UIThread;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.GPSUtil;
import com.eye3.golfpay.view.TeeShotSpotView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;

public class CourseFragment extends BaseFragment {
    protected String TAG = getClass().getSimpleName();

    public ViewPager mMapPager;
    private ArrayList<Course> mCourseInfoList;
    public CoursePagerAdapter mCoursePagerAdapter;
    private TextView mTvCourseName;
    View mainView;

    private TextView tv_hole_par, tv_time;
    private TeeShotSpotView tbox1, tbox2, tbox3, tbox4, tbox5;

    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getAllCourseInfo(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fr_course, container, false);

        mMapPager = mainView.findViewById(R.id.map_pager);
        mTvCourseName = mainView.findViewById(R.id.courseName);

//        closeLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GoNativeScreen(new ScoreFragment(), null);
//            }
//        });


        tv_hole_par = mainView.findViewById(R.id.tv_hole_par);
        tv_time = mainView.findViewById(R.id.tv_time);
        tbox1 = mainView.findViewById(R.id.tbox1);
        tbox2 = mainView.findViewById(R.id.tbox2);
        tbox3 = mainView.findViewById(R.id.tbox3);
        tbox4 = mainView.findViewById(R.id.tbox4);
        tbox5 = mainView.findViewById(R.id.tbox5);

        mainView.findViewById(R.id.tv_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                (mParentActivity).getViewMenuFragment().selectMenu(R.id.view_score);
            }
        });
        (mParentActivity).showMainBottomBar();

        startTimerThread();
        return mainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getAllCourseInfo(Context context) {
        showProgress("코스 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getCourseInfo(context, "1", new DataInterface.ResponseCallback<ResponseData<Course>>() {

            @Override
            public void onSuccess(ResponseData<Course> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    mCourseInfoList = (ArrayList<Course>) response.getList();
                    Global.courseInfoList = mCourseInfoList;
                    //여기서 초기화
                    Global.CurrentCourse = mCourseInfoList.get(0);
                    if (mCourseInfoList == null || mCourseInfoList.size() == 0) {
                        Toast.makeText(mContext, "코스 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mCoursePagerAdapter = new CoursePagerAdapter(getActivity(), Global.CurrentCourse.holes);
                    mMapPager.setAdapter(mCoursePagerAdapter);
                    mMapPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            setDrawPage(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    //초기화
                    // mTvHoleNo.setText(Global.CurrentCourse.holes.get(0).hole_no);
                    mTvCourseName.setText(Global.CurrentCourse.courseName);
                    //mTvHolePar.setText(Global.CurrentCourse.holes.get(0).par);

                    if (Global.CurrentCourse.holes != null && Global.CurrentCourse.holes.get(0).tBox.size() > 0) {
                        //tournamentTextView.setText(Global.CurrentCourse.holes.get(0).tBox.get(0).getTboxValue());
                        //regularTextView.setText(Global.CurrentCourse.holes.get(0).tBox.get(1).getTboxValue());
                        //ladiesTextView.setText(Global.CurrentCourse.holes.get(0).tBox.get(2).getTboxValue());
                        //championshipTextView.setText(Global.CurrentCourse.holes.get(0).tBox.get(3).getTboxValue());
                        //frontTextView.setText(Global.CurrentCourse.holes.get(0).tBox.get(4).getTboxValue());
                    } else {
                        Toast.makeText(mContext, "Tbox 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }

                    setDrawPage(0);

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

    private void setDrawPage(int position) {
        mTvCourseName.setText(Global.CurrentCourse.courseName);
        String holeNo = Global.CurrentCourse.holes.get(position).hole_no;
        String par = Global.CurrentCourse.holes.get(position).par;
        tv_hole_par.setText(String.format("%s Hole Par %s", holeNo, par));

        if (Global.CurrentCourse.holes != null && Global.CurrentCourse.holes.get(0).tBox.size() > 0) {

            tbox1.setValue(Global.CurrentCourse.holes.get(position).tBox.get(0).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(0).getTboxValue());
            tbox2.setValue(Global.CurrentCourse.holes.get(position).tBox.get(1).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(1).getTboxValue());
            tbox3.setValue(Global.CurrentCourse.holes.get(position).tBox.get(2).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(2).getTboxValue());
            tbox4.setValue(Global.CurrentCourse.holes.get(position).tBox.get(3).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(3).getTboxValue());
            tbox5.setValue(Global.CurrentCourse.holes.get(position).tBox.get(4).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(4).getTboxValue());
        }
    }

    public class CoursePagerAdapter extends PagerAdapter implements LocationListener {
        Context mContext;
        public List<Hole> mHoleList;
        ImageView mIvMap;
        public Location mLocation;

        @SuppressLint("MissingPermission")
        CoursePagerAdapter(Context context, List<Hole> mHoleList) {
            this.mContext = context;
            this.mHoleList = mHoleList;

            LocationManager myManager =
                    (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            mLocation = myManager.getLastKnownLocation("network");
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.map_pager_page, container, false);
//            Global.viewPagerPosition = position;

            mIvMap = view.findViewById(R.id.iv_map);

            if (mLocation != null && mHoleList.get(position).gps_lat != null && mHoleList.get(position).gps_lon != null) {
                //mTvHereToHole.setText(String.valueOf(GPSUtil.DistanceByDegreeAndroid(mLocation.getLatitude(), mLocation.getLongitude(), Double.parseDouble(mHoleList.get(position).gps_lat), Double.parseDouble(mHoleList.get(position).gps_lon))) + "M");
            }

            if (mHoleList.get(position).img_1_file_url != null) {
                Glide.with(mContext)
                        .load(Global.HOST_BASE_ADDRESS_AWS + mHoleList.get(position).img_1_file_url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
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


        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged2: " + showLogOfLocationInfo(location));
            mLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private String showLogOfLocationInfo(Location location) {
        return "onLocationChanged: " + location.toString() +
                "\n각도 : " + location.getBearing() +
                "\n속도 : " + location.getSpeed() +
                "\n정확도 : " + location.getAccuracy() +
                "\n제공프로바이더 : " + location.getProvider();
    }

    private void startTimerThread() {

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                updateTimer();
            }
        }.start();
    }

    private void updateTimer() {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                int hou = Calendar.getInstance(Locale.KOREA).get(Calendar.HOUR);
                int min = Calendar.getInstance(Locale.KOREA).get(Calendar.MINUTE);
                tv_time.setText(String.format("%02d:%02d", hou, min));
            }
        });
        startTimerThread();
    }
}



