package com.eye3.golfpay.fmb_tab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.eye3.golfpay.fmb_tab.R;


public class MainWorkFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        return inflater.inflate(R.layout.main_fragment, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mParentActivity.openDrawer();
//        SetTitle("KT WMMS");
//        SetDividerVisibility(false);
     //   setDrawerLayoutEnable(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     //   mBtnTaokeoverTest = (Button)getView().findViewById(R.id.btnTakeoverTest);
      //  mBtnTaokeoverTest.setOnClickListener();
//        getView().findViewById(R.id.btnTakeoverTest).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                GoNativeScreenAdd(new FrDeviceSearch(), null);
////                Intent intent = new Intent(getActivity(), DeviceSearchActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}


