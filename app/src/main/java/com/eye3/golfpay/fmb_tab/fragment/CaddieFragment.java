package com.eye3.golfpay.fmb_tab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.listener.OnEditorFinishListener;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.model.guest.ReserveGuestList;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.util.EditorDialogFragment;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;

import java.util.List;
import java.util.Objects;

public class CaddieFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    View v;
    private List<Guest> guestList = Global.guestList;
    public static LinearLayout mGuestViewContainerLinearLayout;
    private LinearLayout mTeamMemoLinear;
    TextView mTeamMemoContentTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getReserveGuestList(Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId());

    }


    private void setDataTeamMemo(String teamMemo) {
        mTeamMemoContentTextView.setText(teamMemo);
    }

    private void teamMemoOnClick(LinearLayout teamMemoLinear) {

        teamMemoLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                EditorDialogFragment teamMemoEditorDialogFragment = new EditorDialogFragment();
                teamMemoEditorDialogFragment.setMemoContent(mTeamMemoContentTextView.getText().toString());
                showDialogFragment(teamMemoEditorDialogFragment);
                teamMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        setDataTeamMemo(memoContent);

                    }
                });
                systemUIHide();
            }
        });
    }

    private void closeKeyboard() {
        for (int i = 0; i < mGuestViewContainerLinearLayout.getChildCount(); i++) {
            CaddieViewGuestItem caddieViewGuestItem = (CaddieViewGuestItem) mGuestViewContainerLinearLayout.getChildAt(i);
            closeKeyboard(caddieViewGuestItem.findViewById(R.id.carNumberEditText));
            closeKeyboard(caddieViewGuestItem.findViewById(R.id.phoneNumberEditText));
        }
    }


    private View createGuestItemView(Guest guest) {
        CaddieViewGuestItem guestItemView = new CaddieViewGuestItem(getActivity(), guest);

        return guestItemView;
    }

    private void createCaddieGuestViews() {
        guestList = Global.guestList;
        for (int i = 0; guestList.size() > i; i++) {
            mGuestViewContainerLinearLayout.addView(createGuestItemView(guestList.get(i)));

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        closeKeyboard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fr_caddie, container, false);
        mGuestViewContainerLinearLayout = v.findViewById(R.id.guestViewContainerLinearLayout);
        createCaddieGuestViews();
        mTeamMemoLinear = v.findViewById(R.id.teamMemoLinearLayout);
        teamMemoOnClick(mTeamMemoLinear);
        mTeamMemoContentTextView = v.findViewById(R.id.teamMemoContentTextView);
        mTeamMemoContentTextView.setText(Global.guestList.get(0).getTeamMemo());
        mParentActivity.showMainBottomBar();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //최종 전송시 클릭버튼
        v.findViewById(R.id.saveCadieTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; mGuestViewContainerLinearLayout.getChildCount() > i; i++)
                    ((CaddieViewGuestItem) mGuestViewContainerLinearLayout.getChildAt(i)).setReserveGuestInfo(guestList.get(i));
            }
        });

    }

    private void showDialogFragment(DialogFragment dialogFragment) {
          closeKeyboard();
        FragmentManager supportFragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();
    }

    public void getReserveGuestList(int reserveId) {
        showProgress("게스트의 정보를 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGuestList(reserveId, new DataInterface.ResponseCallback<ReserveGuestList>() {
            @Override
            public void onSuccess(ReserveGuestList response) {
                if (response.getRetMsg().equals("성공")) {
                    Global.guestList = response.getList();
                    hideProgress();
                }
            }

            @Override
            public void onError(ReserveGuestList response) {
                Toast.makeText(getContext(), "정보를 받아오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
