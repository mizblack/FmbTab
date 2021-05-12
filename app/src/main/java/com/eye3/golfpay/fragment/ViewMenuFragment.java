package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.dialog.LogoutDialog;
import com.eye3.golfpay.dialog.PopupDialog;
import com.eye3.golfpay.listener.ITakePhotoListener;
import com.eye3.golfpay.listener.ScoreInputFinishListener;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.photo.PhotoResponse;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.model.teeup.TeeUpTime;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.service.CartLocationService;
import com.eye3.golfpay.util.FmbCustomDialog;
import com.eye3.golfpay.util.ScoreDialog;
import com.eye3.golfpay.util.SettingsCustomDialog;
import com.eye3.golfpay.util.Util;

import java.io.File;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMenuFragment extends BaseFragment {

    private View mView;
    private TextView caddieNameTextView;
    private DrawerLayout drawer_layout;
    private SettingsCustomDialog settingsCustomDialog;

    public ViewMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fr_view_menu, container, false);
        getAllCourseInfo(mContext);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentManager supportFragmentManager = ((MainActivity) (getContext())).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();
    }

    private void init() {

        drawer_layout = (mParentActivity).findViewById(R.id.drawer_layout);

        caddieNameTextView = mView.findViewById(R.id.tv_caddie);
        caddieNameTextView.setText(Global.caddieName + " 캐디");

        mView.findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        mView.findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTodayReservesForCaddy(getContext(), Global.CaddyNo);
            }
        });

        mView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParentActivity.getPreviousBaseFragment() != null)
                    drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        mView.findViewById(R.id.btn_menu_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFragment courseFragment = new CourseFragment();
                GoNativeScreen(courseFragment, null);
                Global.courseFragment = courseFragment;
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_gps);
            }
        });

        mView.findViewById(R.id.btn_menu_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_score);
            }
        });

        mView.findViewById(R.id.btn_menu_ranking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new RankingFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_ranking);
            }
        });

        mView.findViewById(R.id.btn_menu_near_long).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new NearestLongestFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_near_long);
            }
        });

        mView.findViewById(R.id.btn_menu_near_caddie_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new CaddieFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_caddie_note);
            }
        });

        mView.findViewById(R.id.btn_menu_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new GalleryFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_gallery);
            }
        });

        mView.findViewById(R.id.btn_menu_control).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_control);
            }
        });

        mView.findViewById(R.id.btn_menu_restaurant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new OrderFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_restaurant);
            }
        });

        mView.findViewById(R.id.btn_menu_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mView.findViewById(R.id.btn_menu_beto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                GoNativeScreen(new TopDressingFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_beto);
                */
            }
        });

        mView.findViewById(R.id.btn_menu_notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new NoticeFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_notice);
            }
        });

        mView.findViewById(R.id.btn_menu_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.END);
                settingsCustomDialog = new SettingsCustomDialog(getActivity(), R.style.DialogTheme);
                WindowManager.LayoutParams wmlp = settingsCustomDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;
                // Set alertDialog "not focusable" so nav bar still hiding:
                settingsCustomDialog.getWindow().
                        setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                ;

                settingsCustomDialog.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                settingsCustomDialog.setCanceledOnTouchOutside(false);
                settingsCustomDialog.show();
            }
        });


        mView.findViewById(R.id.btn_menu_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.END);
                ((MainActivity)mParentActivity).startCamera(AppDef.GuestPhoto, new ITakePhotoListener() {
                    @Override
                    public void onTakePhoto(String path) {
                        sendPhoto(path, "normal");
                    }
                });
            }
        });

        mView.findViewById(R.id.btn_menu_special_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawer(GravityCompat.END);
                ((MainActivity)mParentActivity).startCamera(AppDef.GuestPhoto, new ITakePhotoListener() {
                    @Override
                    public void onTakePhoto(String path) {
                        sendPhoto(path, "event");
                    }
                });
            }
        });

        //시작메뉴
        mView.findViewById(R.id.btn_menu_near_caddie_note).performClick();
    }

    public void selectMenu(int id) {

        mView.findViewById(R.id.view_gps).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_score).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_ranking).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_near_long).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_caddie_note).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_gallery).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_control).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_restaurant).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_team).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_beto).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_notice).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_494B4E));
        mView.findViewById(R.id.view_setting).setBackgroundColor(ContextCompat.getColor(mContext, R.color.FMB_Color_3A3D40));
        mView.findViewById(id).setBackgroundColor(ContextCompat.getColor(mContext, R.color.irisBlue));

        mView.findViewById(R.id.btn_close).setVisibility(View.VISIBLE);
    }

    private void getAllCourseInfo(Context context) {
        showProgress("코스 정보를 가져오는 중입니다.");
        DataInterface.getInstance().getCourseInfo(context, "1", new DataInterface.ResponseCallback<ResponseData<Course>>() {

            @Override
            public void onSuccess(ResponseData<Course> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    Global.courseInfoList = response.getList();
                    init();

                } else if (response.getResultCode().equals("fail")) {
                     Toast.makeText(context, response.getResultMessage(), Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(context, "response.getResultCode().equals("else")", Toast.LENGTH_SHORT).show();
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

    private void logout() {
        LogoutDialog dlg = new LogoutDialog(getContext(), R.style.DialogTheme);
        WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;

        // Set alertDialog "not focusable" so nav bar still hiding:
        dlg.getWindow().
                setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
        dlg.show();

        dlg.setListener(new LogoutDialog.IListenerLogout() {
            @Override
            public void onLogout() {
                requestLogout();
            }
        });
    }

    public void requestLogout() {
        showProgress("로그아웃 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).logout(getContext(), new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                hideProgress();
                ((MainActivity)mParentActivity).navigationView.setVisibility(View.VISIBLE);
                GoNavigationDrawer(new LoginFragment(), null);

                mParentActivity.setPreviousBaseFragment(new LoginFragment());
                mParentActivity.GoRootScreenAdd(null);
                mParentActivity.hideMainBottomBar();

                ((MainActivity)mParentActivity).stopGpsTimerTask();
                ((MainActivity)mParentActivity).stopTimerTask();
            }

            @Override
            public void onError(ResponseData<Object> response) {
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });
    }

    private void sendPhoto(String path, String photoType) {
        setProgressMessage("클럽사진을 저장하는 중입니다.");
        closeDrawer();

        RequestBody reserveId = RequestBody.create(MediaType.parse("text/plain"), Global.reserveId);
        RequestBody photo_type = RequestBody.create(MediaType.parse("text/plain"), photoType);
        RequestBody photo_time = RequestBody.create(MediaType.parse("text/plain"), "normal");
        RequestBody caddy_id = RequestBody.create(MediaType.parse("text/plain"), Global.CaddyNo);

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("img_file", path, requestBody);

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setGuestPhotos(reserveId, null,
                photo_type, photo_time, caddy_id, part, new DataInterface.ResponseCallback<PhotoResponse>() {
                    @Override
                    public void onSuccess(PhotoResponse response) {
                        if (mParentActivity.getGalleryFragment() != null)
                            mParentActivity.getGalleryFragment().update();
                    }

                    @Override
                    public void onError(PhotoResponse response) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    private void getTodayReservesForCaddy(final Context context, String caddy_id) {
        //   showProgress("티업시간을 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getTodayReservesForCaddy(caddy_id, new DataInterface.ResponseCallback<TeeUpTime>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(TeeUpTime response) {
                hideProgress();
                systemUIHide();

                if (response == null || response.getTodayReserveList() == null || response.getTodayReserveList().size() == 0) {
                    Toast.makeText(context, "배정된 예약이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ((MainActivity)mParentActivity).navigationView.setVisibility(View.VISIBLE);
                mParentActivity.setPreviousBaseFragment(new LoginFragment());
                mParentActivity.GoRootScreenAdd(null);
                mParentActivity.hideMainBottomBar();

                ((MainActivity)mParentActivity).stopGpsTimerTask();
                ((MainActivity)mParentActivity).stopTimerTask();

                GoNavigationDrawer(new TeeUpFragment(), null);
                ((MainActivity)mParentActivity).updateUI();
            }

            @Override
            public void onError(TeeUpTime response) {
                hideProgress();
                systemUIHide();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
                systemUIHide();
            }
        });
    }
}
