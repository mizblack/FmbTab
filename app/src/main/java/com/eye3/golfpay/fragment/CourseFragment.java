package com.eye3.golfpay.fragment;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.eye3.golfpay.BuildConfig;
import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.UIThread;
import com.eye3.golfpay.dialog.ChangeCourseDialog;
import com.eye3.golfpay.model.chat.ChatData;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.gallery.ResponseGallery;
import com.eye3.golfpay.model.gps.CartPos;
import com.eye3.golfpay.model.gps.GpsInfo;
import com.eye3.golfpay.model.gps.ResCheckChangeCourse;
import com.eye3.golfpay.model.gps.ResponseCartInfo;
import com.eye3.golfpay.model.gps.TimeData;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.Util;
import com.eye3.golfpay.view.CartPosView;
import com.eye3.golfpay.view.GpsView;
import com.eye3.golfpay.view.HoleCupPointView;
import com.eye3.golfpay.view.TeeShotSpotView;
import java.util.ArrayList;
import java.util.List;
import static com.eye3.golfpay.common.Global.GameStatus.eNone;

public class CourseFragment extends BaseFragment {
    protected String TAG = getClass().getSimpleName();

    private ArrayList<Course> mCourseInfoList;
    private TextView mTvCourseName;
    View mainView;

    private TextView tv_hole_par, tv_time, tv_before_time, tv_after_time;
    private TeeShotSpotView tbox1, tbox2, tbox3, tbox4, tbox5;
    private GpsView gpsView;
    private CartPosView cartPosView;
    private TextView tvDistance1;
    private TextView tvDistance2;
    private TextView tvHereToHole;
    private ImageView ivAdvertising1, ivAdvertising2, ivAdvertising3, iv_map, iv_holeCup;
    private HoleCupPointView holeCupPointView;
    private ConstraintLayout btnChangeCourse;
    private int advertisingCount = 0;
    private boolean advertsingContinue = true;
    private ResponseCartInfo cartInfo;
    int mapPosition = 0;
    int currentHoleIndex = -1;

    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllCourseInfo(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advertsingContinue = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fr_course, container, false);

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
        tv_before_time = mainView.findViewById(R.id.tv_before_time);
        tv_after_time = mainView.findViewById(R.id.tv_after_time);

        iv_map = mainView.findViewById(R.id.iv_map);
        iv_holeCup = mainView.findViewById(R.id.iv_holeCup);
        holeCupPointView = mainView.findViewById(R.id.holeCup_point);
        gpsView = mainView.findViewById(R.id.view_gps);
        cartPosView = mainView.findViewById(R.id.view_cartPos);
        tvDistance1 = mainView.findViewById(R.id.textView);
        tvDistance2 = mainView.findViewById(R.id.textView2);
        ivAdvertising1 = mainView.findViewById(R.id.iv_ad1);
        ivAdvertising2 = mainView.findViewById(R.id.iv_ad2);
        ivAdvertising3 = mainView.findViewById(R.id.iv_ad3);
        tvHereToHole = mainView.findViewById(R.id.tv_here_to_hole);
        btnChangeCourse = mainView.findViewById(R.id.btn_change_course);

        if (BuildConfig.DEBUG) {
            //tvHereToHole.setVisibility(View.GONE);
        }

        gpsView.setDistanceListener(new GpsView.IDistanceListener() {
            @Override
            public void onDistance(double distance1, double distance2) {
                String text1 = String.format("%dM", (int) distance1);
                String text2 = String.format("%dM", (int) distance2);

                if (Global.isYard) {
                    int yard1 = (int)distance1;
                    if (Global.isYard) {
                        yard1 = AppDef.MeterToYard(yard1);
                    }

                    int yard2 = (int)distance2;
                    if (Global.isYard) {
                        yard2 = AppDef.MeterToYard(yard2);
                    }

                    text1 = String.format("%dYD", yard1);
                    text2 = String.format("%dYD", yard2);
                }

                tvDistance1.setText(text1);
                tvDistance2.setText(text2);
            }
        });

        mainView.findViewById(R.id.tv_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                (mParentActivity).getViewMenuFragment().selectMenu(R.id.view_score);
            }
        });

        (mParentActivity).showMainBottomBar();
        mainView.findViewById(R.id.iv_goal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mapPosition >= 9)
                    mapPosition = 0;

                setDrawPage(mapPosition);
                drawHoleCup(mapPosition);
                drawMap(mapPosition++);
            }
        });

        mainView.findViewById(R.id.btn_convert_my).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.isYard ^= true;

                try {
                    updateCourse(cartInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnChangeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkChangeCourse(getContext());
            }
        });

        //Global.gameTimeStatus = eBeforeStart;
        ((MainActivity) getActivity()).setGameTimerListener(new MainActivity.IGameTimerListener() {
            @Override
            public void onGameTimer() {
                updateTimer();
            }
        });

        startAdvertisingTimerThread();
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

    public void updateCourse(ResponseCartInfo cartInfo) throws Exception {

        //미터 야드 즉각적인 변환을 위해 멤버변수로 저장해 놓음
        //아니면 5초에 한번씩 갱신될 때마다 변경됨.
        this.cartInfo = cartInfo;

        if (mCourseInfoList == null)
            return;

        int position = 0;
        if (!BuildConfig.DEBUG) {
            Global.CurrentCourse = getCurrentCourse(cartInfo.nearby_hole_list);
            position = getCurrentHoleIndex(cartInfo.nearby_hole_list);
            if (position < 0)
                return;
        } else {
            position = 0;
        }

        Hole currentHole = Global.CurrentCourse.holes.get(position);
        Hole prevHole = null;
        Hole nextHole = null;

        if (position > 0)
            prevHole = Global.CurrentCourse.holes.get(position-1);
        if (position < 8)
            nextHole = Global.CurrentCourse.holes.get(position+1);

        cartPosView.setCurrentHoleInfo(currentHole, prevHole, nextHole);

        setDrawPage(position);
        drawHoleCup(position);
        drawMap(position);

        if (cartInfo.here_to_hole > 1000)
            cartInfo.here_to_hole = 999;

        if (!Global.isYard) {
            tvHereToHole.setText(String.format("%dM", cartInfo.here_to_hole));
        } else {
            int yard = AppDef.MeterToYard(cartInfo.here_to_hole);
            tvHereToHole.setText(String.format("%dYD", yard));
        }

        cartPosView.clearCart();
        for (GpsInfo gpsInfo : cartInfo.nearby_hole_list) {

            CartPos cartPos = new CartPos(
                    gpsInfo.getCaddyName(),
                    gpsInfo.getCart_status(),
                    gpsInfo.getLat(), gpsInfo.getLng(),
                    gpsInfo.getCart_no(),
                    gpsInfo.getGubun(),
                    gpsInfo.getDistance(),
                    gpsInfo.getPercent());

            switch (gpsInfo.getGubun()) {
                case "same_hole":
                case "me":
                    cartPosView.addCurrentCart(cartPos);
                    break;
                case "back":
                    cartPosView.addPrevCart(cartPos);
                    break;
                case "front":
                    cartPosView.addNextCart(cartPos);
                    break;
            }
        }

        cartPosView.invalidate();
        currentHoleIndex = position;

        TimeData td = cartInfo.time_data;
        if (td == null)
            return;

        if (td.before_start_time != null && !td.before_start_time.isEmpty()) {
            if (Global.gameTimeStatus == Global.GameStatus.eEnd) {
                Global.gameSec = 0;
                Global.gameBeforeSec = 0;
                Global.gameAfterSec = 0;
            }
            Global.gameTimeStatus = Global.GameStatus.eBefore;
        }
        if (td.before_end_time != null && !td.before_end_time.isEmpty())
            Global.gameTimeStatus = Global.GameStatus.eWait;
        if (td.after_start_time != null && !td.after_start_time.isEmpty())
            Global.gameTimeStatus = Global.GameStatus.eAfter;
        if (td.after_end_time != null && !td.after_end_time.isEmpty())
            Global.gameTimeStatus = Global.GameStatus.eEnd;
    }

    private Course getCurrentCourse(List<GpsInfo> gpsInfoList) {

        if (mCourseInfoList == null)
            return null;

        if (gpsInfoList.size() == 0)
            return null;

        for (GpsInfo gpsInfo : gpsInfoList) {
            if (gpsInfo.getGubun().equalsIgnoreCase("me")) {

                for (int i = 0; i < mCourseInfoList.size(); i++) {
                    if (mCourseInfoList.get(i).ctype.equalsIgnoreCase(gpsInfo.getCtype())) {
                        return mCourseInfoList.get(i);
                    }
                }
            }
        }

        return null;
    }

    private int getCurrentHoleIndex(List<GpsInfo> gpsInfoList) {
        if (gpsInfoList.size() == 0)
            return -1;

        for (GpsInfo gpsInfo : gpsInfoList) {
            if (gpsInfo.getGubun().equalsIgnoreCase("me")) {

                List<Hole> holeList = getHoleList(gpsInfo.getCtype());
                if (holeList == null || holeList.size() == 0)
                    return -1;

                for (int i = 0; i < holeList.size(); i++) {
                    if (holeList.get(i).id.equalsIgnoreCase(gpsInfo.getHole_id()))
                        return i;
                }
            }
        }

        return -1;
    }

    private List<Hole> getHoleList(String ctype) {
        for (int i = 0; i < mCourseInfoList.size(); i++) {
            if (mCourseInfoList.get(i).ctype.equalsIgnoreCase(ctype)) {
                return mCourseInfoList.get(i).holes;
            }
        }

        return null;
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

                    if (mCourseInfoList == null || mCourseInfoList.size() == 0) {
                        Toast.makeText(mContext, "코스 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //여기서 초기화
                    if (Global.CurrentCourse == null) {
                        if (Global.gameTimeStatus == Global.GameStatus.eBefore || Global.gameTimeStatus == eNone)
                            Global.CurrentCourse = mCourseInfoList.get(0);
                        else {
                            Global.CurrentCourse = mCourseInfoList.get(1);
                        }
                    }
                    else
                        Global.CurrentCourse = findCurrentCourse(Global.CurrentCourse.id, mCourseInfoList);

                    //초기화
                    // mTvHoleNo.setText(Global.CurrentCourse.holes.get(0).hole_no);

                    if (Global.CurrentCourse.courseName == null)
                        return;

                    mTvCourseName.setText(Global.CurrentCourse.courseName);
                    //mTvHolePar.setText(Global.CurrentCourse.holes.get(0).par);
                    drawMap(0);
                    drawHoleCup(0);

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

    private Course findCurrentCourse(String id, ArrayList<Course> courses) {
        for (Course course : courses) {
            if (course.id.equals(id)) {
                return course;
            }
        }

        return null;
    }

    private void drawMap(int position) {

        String mapImageUri = "";
        try {
            if (Global.CurrentCourse.courseName.equalsIgnoreCase("lake")) {
                String fileName = String.format("lake_%d.png", position+1);
                mapImageUri = Global.ASSETS_PATH_LAKE + fileName;
            } else if (Global.CurrentCourse.courseName.equalsIgnoreCase("silk")) {
                String fileName = String.format("silk_%d.png", position+1);
                mapImageUri = Global.ASSETS_PATH_SILK + fileName;
            } else if (Global.CurrentCourse.courseName.equalsIgnoreCase("valley")) {
                String fileName = String.format("valley_%d.png", position+1);
                mapImageUri = Global.ASSETS_PATH_VALLEY + fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(mContext)
                .load(mapImageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv_map)
                .getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(int width, int height) {
                        if (Global.CurrentCourse == null)
                            return;

                        Hole h = Global.CurrentCourse.holes.get(position);
                        gpsView.setHolePos(new Point(h.course_hole_width_per, h.course_hole_height_per), h.hole_image_meter, width);

                        if (currentHoleIndex != position)
                            gpsView.setObject1Pos(new Point(h.course_start_width_per, h.course_start_height_per));
                    }
                });
    }

    private void drawHoleCup(int position) {

        String mapImageUri = "";
        try {
            if (Global.CurrentCourse.courseName.equalsIgnoreCase("lake")) {
                String fileName = String.format("lake_%d_tee.png", position+1);
                mapImageUri = Global.ASSETS_PATH_LAKE + "tee/" + fileName;
            } else if (Global.CurrentCourse.courseName.equalsIgnoreCase("silk")) {
                String fileName = String.format("silk_%d_tee.png", position+1);
                mapImageUri = Global.ASSETS_PATH_SILK + "tee/" + fileName;
            } else if (Global.CurrentCourse.courseName.equalsIgnoreCase("valley")) {
                String fileName = String.format("valley_%d_tee.png", position+1);
                mapImageUri = Global.ASSETS_PATH_VALLEY + "tee/" + fileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(mContext)
                .load(mapImageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(iv_holeCup)
                .getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(int width, int height) {
                        if (Global.CurrentCourse == null)
                            return;

                        Hole h = Global.CurrentCourse.holes.get(position);
                        holeCupPointView.setHolePos(new Point(h.hole_pin_width_per, h.hole_pin_height_per), h.hole_image_meter, width);
                    }
                });
    }

    private void setDrawPage(int position) {
        try {
            mTvCourseName.setText(Global.CurrentCourse.courseName);
            String holeNo = Global.CurrentCourse.holes.get(position).hole_no;
            String par = Global.CurrentCourse.holes.get(position).par;
            tv_hole_par.setText(String.format("%s Hole Par %s", holeNo, par));

            if (Global.CurrentCourse.holes != null && Global.CurrentCourse.holes.get(0).tBox.size() > 0) {

                tbox1.setValue(Global.CurrentCourse.holes.get(position).tBox.get(0).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(0).getTboxValue(), Global.CurrentCourse.holes.get(position).tBox.get(0).getmTboxColor());
                tbox2.setValue(Global.CurrentCourse.holes.get(position).tBox.get(1).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(1).getTboxValue(), Global.CurrentCourse.holes.get(position).tBox.get(1).getmTboxColor());
                tbox3.setValue(Global.CurrentCourse.holes.get(position).tBox.get(2).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(2).getTboxValue(), Global.CurrentCourse.holes.get(position).tBox.get(2).getmTboxColor());
                tbox4.setValue(Global.CurrentCourse.holes.get(position).tBox.get(3).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(3).getTboxValue(), Global.CurrentCourse.holes.get(position).tBox.get(3).getmTboxColor());
                tbox5.setValue(Global.CurrentCourse.holes.get(position).tBox.get(4).getTboxName(), Global.CurrentCourse.holes.get(position).tBox.get(4).getTboxValue(), Global.CurrentCourse.holes.get(position).tBox.get(4).getmTboxColor());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private String showLogOfLocationInfo(Location location) {
        return "onLocationChanged: " + location.toString() +
                "\n각도 : " + location.getBearing() +
                "\n속도 : " + location.getSpeed() +
                "\n정확도 : " + location.getAccuracy() +
                "\n제공프로바이더 : " + location.getProvider();
    }

    private void startAdvertisingTimerThread() {

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (!advertsingContinue)
                    return;

                switch (advertisingCount % 3) {
                    case 0:
                        changeAdvertising(ivAdvertising1, ivAdvertising2);
                        break;
                    case 1:
                        changeAdvertising(ivAdvertising2, ivAdvertising3);
                        break;
                    case 2:
                        changeAdvertising(ivAdvertising3, ivAdvertising1);
                        break;
                }

                advertisingCount++;

            }
        }.start();
    }

    private void updateTimer() {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                updateGameTimer();
                updateBeforeGameTimer();
                updateAfterGameTimer();
            }
        });
    }

    private void updateGameTimer() {
        try {
            int hour = Global.gameSec / 3600;
            int min = (Global.gameSec - (hour * 3600)) / 60;
            int sec = (Global.gameSec - (hour * 3600) - (min * 60));

            if (Global.gameSec < 3600)
                tv_time.setText(String.format("%02d:%02d", min, sec));
            else
                tv_time.setText(String.format("%02d:%02d", hour, min));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBeforeGameTimer() {
        try {
            int hour = Global.gameBeforeSec / 3600;
            int min = (Global.gameBeforeSec - (hour * 3600)) / 60;
            int sec = (Global.gameBeforeSec - (hour * 3600) - (min * 60));

            if (Global.gameBeforeSec < 3600)
                tv_before_time.setText(String.format("%02d:%02d", min, sec));
            else
                tv_before_time.setText(String.format("%02d:%02d", hour, min));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAfterGameTimer() {
        try {
            int hour = Global.gameAfterSec / 3600;
            int min = (Global.gameAfterSec - (hour * 3600)) / 60;
            int sec = (Global.gameAfterSec - (hour * 3600) - (min * 60));

            if (Global.gameAfterSec < 3600)
                tv_after_time.setText(String.format("%02d:%02d", min, sec));
            else
                tv_after_time.setText(String.format("%02d:%02d", hour, min));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeAdvertising(ImageView ivOut, ImageView ivIn) {

        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Animation slowly_appear, slowlyDisappear;
                    slowlyDisappear = AnimationUtils.loadAnimation(getContext(), R.anim.slowly_fadeout);
                    slowly_appear = AnimationUtils.loadAnimation(getContext(), R.anim.slowly_fadein);
                    ivOut.setAnimation(slowlyDisappear);
                    ivOut.setVisibility(View.GONE);
                    ivIn.setAnimation(slowly_appear);
                    ivIn.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        startAdvertisingTimerThread();
    }

    private void checkChangeCourse(Context context) {

        ChangeCourseDialog dlg = new ChangeCourseDialog(getContext(), mCourseInfoList.get(0).courseName,
                mCourseInfoList.get(1).courseName, new ChangeCourseDialog.IListenerDialog() {
            @Override
            public void onChangeCourse() {
                getAllCourseInfo(getActivity());
            }
        });
        WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.show();
    }

}