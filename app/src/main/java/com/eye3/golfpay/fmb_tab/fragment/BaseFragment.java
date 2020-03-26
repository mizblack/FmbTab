package com.eye3.golfpay.fmb_tab.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.BaseActivity;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.listener.OnKeyBackPressedListener;
import com.eye3.golfpay.fmb_tab.listener.OnTitleListener;
import com.eye3.golfpay.fmb_tab.util.Logger;

import java.util.Objects;


//layout(content mMenuView)을 갖고 있지 않음
public class BaseFragment extends Fragment implements OnKeyBackPressedListener {

    protected String TAG;
    protected Context mContext;
    // Parent Activity 핸들
    protected BaseActivity mParentActivity;

    // 타이틀 선택 Listener
    protected OnTitleListener mOnTitleListener;

    private TextView tvTitle;
    private Button btnDrawerOpen;
    private Button btnBackArrow;
    private ImageView ivDivider;
    private String arrowBack;
    protected ProgressDialog pd; // 프로그레스바 선언

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        TAG = getClass().getSimpleName();

        this.mContext = context;
        this.mParentActivity = (BaseActivity) getActivity();
        (mParentActivity).setOnKeyBackPressedListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UIThread.initializeHandler();
        pd = new ProgressDialog(getActivity()); // 생성한다.
        pd.setCancelable(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            arrowBack = bundle.getString("arrowBack");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        tvTitle = mParentActivity.findViewById(R.id.tvTitle);
//        ivDivider = mParentActivity.findViewById(R.id.ivDivider);
//        btnDrawerOpen = mParentActivity.findViewById(R.id.btnDrawerOpen);
        btnBackArrow = mParentActivity.findViewById(R.id.btnTitleBack);
//
//        if ("y".equals(arrowBack)) {
//            btnBackArrow.setVisibility(View.VISIBLE);
//            btnDrawerOpen.setVisibility(View.GONE);
//        } else {
//            btnBackArrow.setVisibility(View.GONE);
//            btnDrawerOpen.setVisibility(View.VISIBLE);
//        }
        if (btnBackArrow != null) {
            btnBackArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBack();
                }
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Native 메인화면으로 이동
     */
    public void GoHomeScreen() {
        if (mParentActivity != null) {
            mParentActivity.GoHomeScreen();
        }
    }

    /**
     * Native 메인화면으로 이동
     *
     * @param bundle Parameter
     */
    public void GoHomeScreen(Bundle bundle) {
        if (mParentActivity != null) {
            mParentActivity.GoHomeScreen(bundle);
        }
    }

    /**
     * 전달받은 Native 화면으로 이동
     *
     * @param fragment Native 화면 개체
     * @param bundle   Parameter 번들
     */
    public void GoNativeScreen(BaseFragment fragment, Bundle bundle) {
        if (mParentActivity != null)
            mParentActivity.GoNativeScreen(fragment, bundle);
    }

    public void GoNavigationDrawer(BaseFragment fragment, Bundle bundle){
        if (mParentActivity != null)
            mParentActivity.GoNavigationDrawer(fragment, bundle);
    }

    public void GoNativeScreenAdd(BaseFragment fragment, Bundle bundle) {
        if (mParentActivity != null) {
            mParentActivity.GoNativeScreenAdd(fragment, bundle);
        }
    }

    /**
     * 타이틀 선택한 정보 전달받을 Listener 등록
     *
     * @param listener 리스너
     */
    public void SetTitleListener(OnTitleListener listener) {
        this.mOnTitleListener = listener;
    }

    /**
     * 화면 타이틀 설정
     *
     * @param label 타이틀
     */
    public void SetTitle(final String label) {
        Logger.i(TAG, "title start......");
        if (tvTitle != null) {
            tvTitle.setText(label);
        }
    }

//    public BaseFragment getVisibleFragment() {
//        for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
//            if (fragment.isVisible()) {
//                return ((BaseFragment) fragment);
//            }
//        }
//        return null;
//    }



    public void GoOrderLeftBoard(BaseFragment fragment, Bundle bundle) {
        if (mParentActivity != null)
            mParentActivity.GoOrderLeftBoard(fragment, bundle);
    }

    /**
     * 네비게이션과 화면 사이의 Divider 출력 처리
     *
     * @param isVisible true: 보이기, false: 숨김
     */
    public void SetDividerVisibility(final boolean isVisible) {
        if (ivDivider != null) ivDivider.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

//    public void setDrawerLayoutEnable(final boolean isEnable) {
//        if (mParentActivity != null) {
//            if(mParentActivity.getDrawerLayout() != null) {
//                if(isEnable) {
//                    mParentActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                } else {
//                    mParentActivity.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//                }
//            } else {
//                Log.e("<PHD>", "## NavigationView Null..!!!!!!!");
//            }
//        }
//    }


//    private FmbCustomDialog commonDialog;
//
//    public void showCarStatusErrorDialog(Context context, String msg) {
//        commonDialog = new FmbCustomDialog(context, null, msg, "확인", new View.OnClickListener() {
//            @Override
//            public void onClick(View mMenuView) {
//                commonDialog.dismiss();
//             //   GoNativeScreenAdd(new DrivingFragment(), null);
//            }
//        });
//
//        try {
//            commonDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    protected void showProgress(final String msg) {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null) {
                    // 객체를 1회만 생성한다.
                    pd = new ProgressDialog(getActivity()); // 생성한다.
                    pd.setCancelable(true);
                    // 백키로 닫는 기능을 제거한다.
                }
                pd.setMessage(msg);
                // 원하는 메시지를 세팅한다.
                pd.show();
                // 화면에 띠워라
            }
        });

    }


    // 프로그레스 다이얼로그 숨기기
    protected void hideProgress() {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd != null && pd.isShowing()) {
                    // 닫는다 : 객체가 존재하고, 보일때만
                    pd.dismiss();
                }
            }
        });

    }

    @Override
    public void onBack() {
//        if ( mParentActivity instanceof MainWorkFragment) {
//            ( (MainActivity_old) mParentActivity).onBackPressed();
//        } else {
        mParentActivity.GoNativeBackStack();
//        }
    }

    public void systemUIHide() {
           if(getActivity() == null)
               return;
        if (Objects.requireNonNull(getActivity()).getWindow().getDecorView() == null)
            return;
        View decorView = Objects.requireNonNull(getActivity()).getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
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
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        systemUIHide();
    }

    public void closeDrawer(){
        mParentActivity.hideDrawer();
    }

}
