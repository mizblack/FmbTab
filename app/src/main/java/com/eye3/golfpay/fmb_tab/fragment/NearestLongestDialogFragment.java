package com.eye3.golfpay.fmb_tab.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.view.NearestLongestInputItem;

import java.util.ArrayList;

public class NearestLongestDialogFragment extends DialogFragment {

    protected String TAG = getClass().getSimpleName();
    private ArrayList<GuestDatum> guestArrayList = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getGuestData();
    private LinearLayout guestItemLinearLayout;
    private ArrayList<NearestLongestInputItem> nearestLongestInputItemArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_nearest_longest, container, false);
        guestItemLinearLayout = view.findViewById(R.id.guestItemLinearLayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (guestArrayList.size() != 0) {
            for (int i = 0; i < guestArrayList.size(); i++) {
                final NearestLongestInputItem nearestLongestInputItem = new NearestLongestInputItem(getContext());
                nearestLongestInputItemArrayList = new ArrayList<>();
                nearestLongestInputItemArrayList.add(nearestLongestInputItem);
                guestItemLinearLayout.addView(nearestLongestInputItem);
                final View selectorLinearLayout = nearestLongestInputItem.findViewById(R.id.selectorLinearLayout);
                selectorLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < nearestLongestInputItemArrayList.size(); i++) {
                            View selectorLinearLayoutTemp = nearestLongestInputItemArrayList.get(i).findViewById(R.id.selectorLinearLayout);
                            selectorLinearLayoutTemp.setBackgroundColor(0xffffffff);
                            View modifyDividerTemp = nearestLongestInputItemArrayList.get(i).findViewById(R.id.modifyDivider);
                            modifyDividerTemp.setVisibility(View.GONE);
                        }
                        selectorLinearLayout.setBackgroundResource(R.drawable.shape_black_edge);
                        View modifyDivider = nearestLongestInputItem.findViewById(R.id.modifyDivider);
                        modifyDivider.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

    }

}
