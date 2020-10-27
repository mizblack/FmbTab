package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import androidx.viewpager.widget.ViewPager;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.adapter.TeeUpViewPagerAdapter;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.dialog.LogoutDialog;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.guest.ReserveGuestList;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.model.teeup.TodayReserveList;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.net.ResponseData;
import com.eye3.golfpay.fmb_tab.service.CartLocationService;
import com.eye3.golfpay.fmb_tab.util.SettingsCustomDialog;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMenuFragment extends BaseFragment {

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
        View v = inflater.inflate(R.layout.fr_view_menu, container, false);

        init(v);
        return v;
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

    private void init(View v) {

        drawer_layout = (mParentActivity).findViewById(R.id.drawer_layout);

        v.findViewById(R.id.btn_menu_gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFragment courseFragment = new CourseFragment();
                GoNativeScreen(courseFragment, null);
                Global.courseFragment = courseFragment;
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_ranking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new RankingFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_near_long).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ScoreFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
                NearestLongestDialogFragment nearestLongestDialogFragment = new NearestLongestDialogFragment();
                showDialogFragment(nearestLongestDialogFragment);
            }
        });

        v.findViewById(R.id.btn_menu_near_caddie_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new CaddieFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new GalleryFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_control).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new ControlFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_restaurant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new OrderFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        v.findViewById(R.id.btn_menu_beto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new TopDressingFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoNativeScreen(new NoticeFragment(), null);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });

        v.findViewById(R.id.btn_menu_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsCustomDialog = new SettingsCustomDialog(getActivity());
                settingsCustomDialog.show();
            }
        });
    }
}
