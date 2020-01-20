package com.eye3.golfpay.fmb_tab.activity;


import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.fragment.MainWorkFragment;
import com.eye3.golfpay.fmb_tab.fragment.BaseFragment;
import com.eye3.golfpay.fmb_tab.listener.OnKeyBackPressedListener;
import com.eye3.golfpay.fmb_tab.util.BackPressCloseHandler;


public class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    private T mVd;
    protected BaseFragment mNativeFragment;
    protected String TAG = getClass().getSimpleName();
    private ProgressDialog pd; // 프로그레스바 선언
    protected BackPressCloseHandler backPressCloseHandler;
    protected DrawerLayout drawer;
    private boolean isForward = true;
    protected OnKeyBackPressedListener mOnKeyBackPressedListener;
    // protected Realm mRealm;

    public void setOnKeyBackPressedListener(BaseFragment listener) {
        mOnKeyBackPressedListener = listener;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressCloseHandler = new BackPressCloseHandler(this);
//        Realm.init(this);
//        RealmConfiguration configuration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
//        Realm.setDefaultConfiguration(configuration);
//        Realm.deleteRealm(Realm.getDefaultConfiguration());
//        mRealm = Realm.getInstance(Realm.getDefaultConfiguration());

    }

    protected void setBind(@LayoutRes int layId) {
        if (mVd == null) {
            mVd = DataBindingUtil.setContentView(this, layId);
        }
    }

    protected T getBind() {
        return mVd;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public DrawerLayout getDrawerLayout() {
        if (drawer != null) return drawer;
        else return null;
    }


    /**
     * 전달받은 Native 화면으로 이동
     *
     * @para fragment  Native 화면 개체
     * @para bundle    Parameter 번들
     * @para isForward 다음 화면으로 이동인지에 대한 여부
     */

    protected void hideSoftkey() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off. ");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        //    newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        newUiOptions ^= View.SYSTEM_UI_LAYOUT_FLAGS;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

    }

    // parameter1 : activity 내에서 fragment 를 삽입할 Layout id
    // parameter2 : 삽입할 fragment
    public void GoNativeScreen(BaseFragment fragment, Bundle bundle) {
        if (fragment == null) {
            return;
        }

        mNativeFragment = fragment;
        if (bundle != null) {
            mNativeFragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.vw_NativeContent, mNativeFragment).commitAllowingStateLoss();
    }

    public void GoNativeScreenAdd(BaseFragment fragment, Bundle bundle) {
        if (fragment == null) {
            return;
        }

        mNativeFragment = fragment;
        if (bundle != null) {
            mNativeFragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.vw_NativeContent, mNativeFragment).addToBackStack(null).commitAllowingStateLoss();
    }

    public void GoNativeScreenAdd(BaseFragment fragment, Bundle bundle, String backStack) {
        if (fragment == null)
            return;

        mNativeFragment = fragment;
        if (bundle != null) {
            mNativeFragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isForward) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(R.id.vw_NativeContent, mNativeFragment).commitAllowingStateLoss();


//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.vw_NativeContent, mNativeFragment).addToBackStack(null).commitAllowingStateLoss();
    }

    /**
     * 가장 최상위의 frgamant를 리턴한다.
     *
     * @return
     */
    public BaseFragment getVisibleFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.isVisible()) {
                return ((BaseFragment) fragment);
            }
        }
        return null;
    }

    /**
     * 메인 화면 구성하는 Fragment 반환
     *
     * @return Fragment
     */
    public BaseFragment getRootFragment() {
        return new MainWorkFragment();

    }

    protected void GoRootScreenAdd(Bundle bundle) {
        GoNativeScreenAdd(new MainWorkFragment(), null);
        //   GoNativeScreenAdd(new FrTakeoverTestmain(), null);
//        if (bundle != null) {
//            mNativeFragment.setArguments(bundle);
//        }
    }

    /**
     * Native 메인화면으로 이동
     *
     * @param bundle Parameter 번들
     */
    protected void GoRootScreen(Bundle bundle) {
        mNativeFragment = getRootFragment();


//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.vw_NativeContent, mNativeFragment).commit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isForward) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(R.id.vw_NativeContent, mNativeFragment).commit();

    }


    /**
     * Native 메인화면으로 이동
     */
    public void GoHomeScreen() {
        GoHomeScreen(null);
    }

    /**
     * Native 메인화면으로 이동 (계좌 조회)
     *
     * @param bundle Parameter
     */
    public void GoHomeScreen(Bundle bundle) {
        //GoRootScreen(bundle);
        GoRootScreenAdd(null);

    }

    /**
     * 전달받은 Native 화면으로 이동
     */
    public void GoNativeBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStack();

        }

    }

    /**
     * 메뉴 View 초기화
     */
//    protected void SetMenuView() {
//   //     mDlHomeView = (androidx.drawerlayout.widget.DrawerLayout) this.findViewById(R.id.drawer_layout);
//
//        getBind()..setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        mDlHomeView.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                if (mNativeFragment != null) {
//                  //  mNativeFragment.checkMyMenuPosition();
//                }
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                mDlHomeView.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//
//
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//
//                mDlHomeView.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//            }
//        });


    //   }
    private void openDrawer() {
    }

    public void showProgress(String msg) {
        if (pd == null) {
            // 객체를 1회만 생성한다.
            pd = new ProgressDialog(this); // 생성한다.
            pd.setCancelable(true);
            // 백키로 닫는 기능을 제거한다.
        }
        pd.setMessage(msg);
        // 원하는 메시지를 세팅한다.
        pd.show();
        // 화면에 띠워라
    }

    // 프로그레스 다이얼로그 숨기기
    public void hideProgress() {
        if (pd != null && pd.isShowing()) {
            // 닫는다 : 객체가 존재하고, 보일때만
            pd.dismiss();
        }
    }


    @Override
    public void onBackPressed() {

        if (mOnKeyBackPressedListener == null)
            return;

        BaseFragment visibleFragment = getVisibleFragment();
        if (visibleFragment != null) {

            if (visibleFragment instanceof MainWorkFragment)
                backPressCloseHandler.onBackPressed();
            else
                mOnKeyBackPressedListener.onBack();
        }
    }

    /**
     * Navigation 메뉴들과 타이틀 View 핸들 설정
     */
    protected void EventForTitleView(final View view) {
//        switch (view.getId()) {
//            case R.id.btnTitleHome:
//                GoRootScreen(null);
//                break;
//
//            case R.id.btnDrawerOpen:
//                ( (MainActivity) this).OpenMenuMap();
//                break;
//        }
    }

}
