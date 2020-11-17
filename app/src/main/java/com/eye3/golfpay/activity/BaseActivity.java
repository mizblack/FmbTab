package com.eye3.golfpay.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.R;
import com.eye3.golfpay.fragment.BaseFragment;
import com.eye3.golfpay.fragment.MainWorkFragment;
import com.eye3.golfpay.fragment.ViewMenuFragment;
import com.eye3.golfpay.listener.OnKeyBackPressedListener;
import com.eye3.golfpay.util.BackPressCloseHandler;


public class BaseActivity<T extends ViewDataBinding> extends FragmentActivity {
    protected String TAG = getClass().getSimpleName();
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 2000;

    private T mVd;
    protected BaseFragment mBaseFragment;
    protected BaseFragment mPreviousBaseFragment;
    private ProgressDialog pd; // 프로그레스바 선언
    protected BackPressCloseHandler backPressCloseHandler;
    public DrawerLayout drawer;
    private boolean isForward = true;
    protected OnKeyBackPressedListener mOnKeyBackPressedListener;
    RelativeLayout mContentRelativeLayout;
    ConstraintLayout mBottomBarLayout;

    public void setOnKeyBackPressedListener(BaseFragment listener) {
        mOnKeyBackPressedListener = listener;

    }

    public DrawerLayout getDrawer() {
        drawer = ((MainActivity) this).drawer_layout;
        return drawer;
    }

    public BaseFragment getPreviousBaseFragment() {
        return mPreviousBaseFragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void showMainBottomBar() {
        mContentRelativeLayout = findViewById(R.id.vw_NativeContent);
        mContentRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.content_view_height)));
        mBottomBarLayout = findViewById(R.id.main_bottom_bar);
        mBottomBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideMainBottomBar() {
        mContentRelativeLayout = findViewById(R.id.vw_NativeContent);
        mContentRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        mBottomBarLayout = findViewById(R.id.main_bottom_bar);
        mBottomBarLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemUIHide();

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
        String direction;
        if (fragment == null) {
            return;
        }
        //동일한 fragment를 replace할경우 skip!
        if (mPreviousBaseFragment != null) {
            if (fragment.getClass().getName().equals(mPreviousBaseFragment.getClass().getName()))
                return;
        }

        mBaseFragment = fragment;
        mPreviousBaseFragment = fragment;
        if (getVisibleFragment() instanceof ViewMenuFragment) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (bundle != null) {
            mBaseFragment.setArguments(bundle);
            direction = bundle.getString("ani_direction");
            if (direction != null) {
                if (direction.equals("up")) {
                    transaction.setCustomAnimations(R.anim.pull_in_top, R.anim.push_out_down);
                } else if (direction.equals("down"))
                    transaction.setCustomAnimations(R.anim.slide_down_to_enter, R.anim.slide_down_to_enter);
            }
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        //replace시 새로운 fragment가 생성되면서 올라오면 , 현 fragment는 onDestroy를 호출한다.(메모리에서 사라지게됨)
        transaction.replace(R.id.vw_NativeContent, mBaseFragment).commitAllowingStateLoss();

    }

    public void GoNativeScreenBetweenParTar(BaseFragment fragment, Bundle bundle) {
        String direction;
        if (fragment == null) {
            return;
        }

        mBaseFragment = fragment;
        if (getVisibleFragment() instanceof ViewMenuFragment) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (bundle != null) {
//            mBaseFragment.setArguments(bundle);
//            direction = bundle.getString("ani_direction");
//            if (direction.equals("up")) {
//                transaction.setCustomAnimations(R.anim.pull_in_top, R.anim.push_out_down);
//            } else if (direction.equals("down"))
//                transaction.setCustomAnimations(R.anim.slide_down_to_enter, R.anim.slide_down_to_enter);
//
//        } else {
//            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//        }
        //replace시 새로운 fragment가 생성되면서 올라오면 , 현 fragment는 onDestroy를 호출한다.(메모리에서 사라지게됨)
        transaction.replace(R.id.vw_NativeContent, mBaseFragment).commitAllowingStateLoss();

    }

    public void GoNavigationDrawer(BaseFragment fragment, Bundle bundle) {
        if (fragment == null) {
            return;
        }

        mBaseFragment = fragment;
        if (bundle != null) {
            mBaseFragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.nav_view, mBaseFragment).commitAllowingStateLoss();
    }

    public void GoOrderLeftBoard(BaseFragment fragment, Bundle bundle) {
        if (fragment == null) {
            return;
        }

        mBaseFragment = fragment;
        if (bundle != null) {
            mBaseFragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.applyFragmentLinear, mBaseFragment).commitAllowingStateLoss();

    }

    public void removeFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.push_out_down, R.anim.pull_in_top);

        transaction.remove(fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    public void GoNativeScreenAdd(BaseFragment fragment, Bundle bundle) {
        if (fragment == null) {
            return;
        }

        mBaseFragment = fragment;
        if (bundle != null) {
            mBaseFragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.vw_NativeContent, mBaseFragment).addToBackStack(null).commitAllowingStateLoss();
    }

    public void GoNativeScreenAdd(BaseFragment fragment, Bundle bundle, String backStack) {
        if (fragment == null)
            return;

        mBaseFragment = fragment;
        if (bundle != null) {
            mBaseFragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isForward) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(R.id.vw_NativeContent, mBaseFragment).commitAllowingStateLoss();


//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.vw_NativeContent, mBaseFragment).addToBackStack(null).commitAllowingStateLoss();
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

    public ViewMenuFragment getViewMenuFragment() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof ViewMenuFragment) {
                return ((ViewMenuFragment) fragment);
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

    public void GoRootScreenAdd(Bundle bundle) {
        GoNativeScreenAdd(new MainWorkFragment(), null);
        //   GoNativeScreenAdd(new FrTakeoverTestmain(), null);
//        if (bundle != null) {
//            mBaseFragment.setArguments(bundle);
//        }
    }

    /**
     * Native 메인화면으로 이동
     *
     * @param bundle Parameter 번들
     */
    protected void GoRootScreen(Bundle bundle) {
        mBaseFragment = getRootFragment();


//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.vw_NativeContent, mBaseFragment).commit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isForward) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(R.id.vw_NativeContent, mBaseFragment).commit();

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
//                if (mBaseFragment != null) {
//                  //  mBaseFragment.checkMyMenuPosition();
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
    public void hideDrawer() {
        ((MainActivity) this).HideMenuMap();
    }

    public void openDrawer() {
        ((MainActivity) this).OpenMenuMap();
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

    public void systemUIHide() {
        View decorView = getWindow().getDecorView();
        final int uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // TODO: The system bars are visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls.
                    systemUIHide();
                } else {
                    // TODO: The system bars are NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.
                }
            }
        });
    }

    public void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        systemUIHide();
    }


    protected void onViewStateRestored(Bundle savedInstanceState) {
    }


}
