package com.eye3.golfpay.fmb_tab.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.dialog.LogoutDialog;
import com.eye3.golfpay.fmb_tab.listener.ScoreInputFinishListener;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;
import com.eye3.golfpay.fmb_tab.util.ScoreDialog;
import com.eye3.golfpay.fmb_tab.util.SettingsCustomDialog;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.util.List;
import java.util.Objects;

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

    private void setLogout() {
        Objects.requireNonNull(getActivity()).stopService((new Intent(getActivity(), CartLocationService.class)));
        getActivity().finish();
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

        mView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                NearestLongestDialogFragment nearestLongestDialogFragment = new NearestLongestDialogFragment();
                showDialogFragment(nearestLongestDialogFragment);
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
                ScoreDialog sDialog = new ScoreDialog(mContext, "저장", "취소");
                WindowManager.LayoutParams wmlp = sDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER_VERTICAL;

                sDialog.getWindow().setAttributes(wmlp);

                sDialog.setOnScoreInputFinishListener(new ScoreInputFinishListener() {
                    @Override
                    public void OnScoreInputFinished(List<Player> playerList) {

                    }
                });

                sDialog.getWindow().
                        setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                sDialog.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                sDialog.show();
            }
        });

        mView.findViewById(R.id.btn_menu_beto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new TopDressingFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                selectMenu(R.id.view_beto);
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
                settingsCustomDialog = new SettingsCustomDialog(getActivity());
                settingsCustomDialog.show();
            }
        });
    }

    private void selectMenu(int id) {

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
    }

    private void getAllCourseInfo(Context context) {
        showProgress("코스 정보를 가져오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getCourseInfo(context, "1", new DataInterface.ResponseCallback<ResponseData<Course>>() {

            @Override
            public void onSuccess(ResponseData<Course> response) {
                hideProgress();
                if (response.getResultCode().equals("ok")) {
                    Global.courseInfoList = response.getList();
                    init();

                } else if (response.getResultCode().equals("fail")) {
                    // Toast.makeText(getAct, response.getResultMessage(), Toast.LENGTH_SHORT).show();
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
                if (drawer_layout != null)
                    drawer_layout.closeDrawer(GravityCompat.END);
                setLogout();
            }
        });
    }
}
