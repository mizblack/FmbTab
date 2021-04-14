package com.eye3.golfpay.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.UIThread;
import com.eye3.golfpay.dialog.ClubInfoDialog;
import com.eye3.golfpay.dialog.GameHoleDialog;
import com.eye3.golfpay.dialog.SmsScoreDialog;
import com.eye3.golfpay.listener.ICaddyNoteListener;
import com.eye3.golfpay.listener.ITakePhotoListener;
import com.eye3.golfpay.listener.OnEditorFinishListener;
import com.eye3.golfpay.model.caddyNote.CaddyNoteInfo;
import com.eye3.golfpay.model.caddyNote.ResponseCaddyNote;
import com.eye3.golfpay.model.guest.CaddieInfo;
import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.guest.GuestInfo;
import com.eye3.golfpay.model.photo.PhotoResponse;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.EditorDialogFragment;
import com.eye3.golfpay.util.Util;
import com.eye3.golfpay.view.CaddieViewBasicGuestItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CaddieMainFragment extends BaseFragment implements ICaddyNoteListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "CaddieMainFragment";

    private List<Guest> guestList = Global.guestList;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageView ivGrab;
    private CaddieInfo caddieInfo;
    private LinearLayout viewMain;
    protected static ProgressDialog pd; // 프로그레스바 선언
    public static LinearLayout mGuestViewContainerLinearLayout;
    private TextView tvTeamMemoContent;

    public enum CaddyNoteType{
        Before {
            @NonNull
            @Override
            public String toString() {
                return "before";
            }
        },
        After {
            @NonNull
            @Override
            public String toString() {
                return "after";
            }
        }
    }
    private CaddyNoteType caddyNoteType = CaddyNoteType.Before;

    public CaddieMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        caddieInfo = new CaddieInfo();

        for (Guest guest: guestList) {
            GuestInfo gi = new GuestInfo();
            gi.setGuestName(guest.getGuestName());
            gi.setReserveGuestId(guest.getId());
            gi.setCarNo(guest.getCarNumber());
            gi.setHp(guest.getPhoneNumber());
            caddieInfo.getGuestInfo().add(gi);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fr_caddie_main, container, false);
        viewMain = view.findViewById(R.id.view_main);
        createGuestBasicView();

        tvTeamMemoContent = view.findViewById(R.id.tv_team_memo_content);
        view.findViewById(R.id.tv_team_memo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();

                guestMemoEditorDialogFragment.setMemoContent(tvTeamMemoContent.getText().toString());
                guestMemoEditorDialogFragment.setTextType("팀메모", "메모를 입력하세요");
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        setTeamMemo(memoContent);
                    }
                });

                FragmentManager supportFragmentManager = ((MainActivity) (mContext)).getSupportFragmentManager();
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                guestMemoEditorDialogFragment.show(transaction, TAG);
                assert guestMemoEditorDialogFragment.getFragmentManager() != null;
                guestMemoEditorDialogFragment.getFragmentManager().executePendingTransactions();
            }
        });

        TextView tvStartSign = view.findViewById(R.id.tv_start_sign);
        TextView tvEndSign = view.findViewById(R.id.tv_end_sign);
        tvStartSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStartSign.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                tvEndSign.setTextAppearance(R.style.GlobalTextView_18SP_zumthor_NotoSans_Bold);
                caddyNoteType = CaddyNoteType.Before;
                getCaddyNote();
            }
        });

        tvEndSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvStartSign.setTextAppearance(R.style.GlobalTextView_18SP_zumthor_NotoSans_Bold);
                tvEndSign.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                caddyNoteType = CaddyNoteType.After;
                getCaddyNote();
            }
        });

        view.findViewById(R.id.btn_team_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeTeamPhoto();
            }
        });

        view.findViewById(R.id.btn_sms_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<GuestInfo> guestInfo = caddieInfo.getGuestInfo();
                SmsScoreDialog dlg = new SmsScoreDialog(getContext(), guestInfo);
                WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;
                dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
                dlg.show();
            }
        });

        getCaddyNote();
        return view;
    }

    private void takeTeamPhoto() {
        ((MainActivity)mParentActivity).startCamera(AppDef.GuestPhoto, new ITakePhotoListener() {
            @Override
            public void onTakePhoto(String path) {
                sendPhoto("", path, "club");
            }
        });
    }

    private void createGuestBasicView() {

        mGuestViewContainerLinearLayout = viewMain;
        guestList = Global.guestList;
        for (int i = 0; caddieInfo.getGuestInfo().size() > i; i++) {
            viewMain.addView(createGuestItemView(caddieInfo.getGuestInfo().get(i)));
        }
    }

    private View createGuestItemView(GuestInfo guest) {
        CaddieViewBasicGuestItem guestItemView = new CaddieViewBasicGuestItem(getActivity(), guest, caddieInfo.getGuestInfo().size(), this);

        return guestItemView;
    }

    @Override
    public void onShowClubInfoDlg(String id) {

        ClubInfoDialog dlg = new ClubInfoDialog(getContext(), caddieInfo, findGuestId(id));
        WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
        dlg.show();

        dlg.setiListenerDialog(new ClubInfoDialog.IListenerDialog() {
            @Override
            public void onSave(String guestId, ClubInfo clubInfo) {
                setClubInfo(guestId, clubInfo);
            }
        });
    }

    @Override
    public void onInputCarNumber(String guestId, String text) {
        int guestIndex = findGuestId(guestId);
        caddieInfo.getGuestInfo().get(guestIndex).setCarNo(text);
        setPersonalInfo(caddieInfo.getGuestInfo().get(guestIndex));
    }

    @Override
    public void onInputPhoneNumber(String guestId, String text) {
        int guestIndex = findGuestId(guestId);
        caddieInfo.getGuestInfo().get(guestIndex).setHp(text);
        setPersonalInfo(caddieInfo.getGuestInfo().get(guestIndex));
    }

    @Override
    public void onInputMemo(String guestId, String text) {
        int guestIndex = findGuestId(guestId);
        caddieInfo.getGuestInfo().get(guestIndex).setGuestMemo(text);
        setPersonalInfo(caddieInfo.getGuestInfo().get(guestIndex));
    }

    @Override
    public void onSignatureImage(String guestId, File signatureFile) {
        setGuestPhotos(guestId, signatureFile);
    }

    @Override
    public void onTakeClubPhoto(String guestId) {

        ((MainActivity)mParentActivity).startCamera(AppDef.GuestPhoto, new ITakePhotoListener() {
            @Override
            public void onTakePhoto(String path) {
                sendPhoto(guestId, path, "club");
            }
        });
    }

    private int findGuestId(String id) {

        int i = 0;
        for (Guest guest: guestList) {
            if (guest.getId().equals(id)) {
                return i;
            }

            i++;
        }

        return 0;
    }

    private String getClubInfoText(String str) {
        str = str.replace("[", "");
        str = str.replace("]", "");
        str = str.replace(" ", "");
        return str;
    }

    private void getCaddyNote() {
        setProgressMessage("캐디수첩을 불러오는 중입니다.");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getCaddyNoteInfo(getActivity(), Global.CaddyNo, Global.reserveId, new DataInterface.ResponseCallback<ResponseCaddyNote>() {
            @Override
            public void onSuccess(ResponseCaddyNote response) {
                hideProgress();
                for (int i = 0; i < response.getData().size(); i++) {
                    CaddyNoteInfo caddyNoteInfo = response.getData().get(i);
                    CaddieViewBasicGuestItem view = (CaddieViewBasicGuestItem)viewMain.getChildAt(i);
                    ClubInfo clubInfo = makeClubInfo(caddyNoteInfo);
                    caddieInfo.getGuestInfo().get(i).clubInfo = clubInfo;
                    view.setCaddyNoteType(caddyNoteType);
                    view.drawClubInfo(clubInfo);
                    view.drawSignImage(caddyNoteInfo);
                    view.drawClubImage(caddyNoteInfo);
                }

                tvTeamMemoContent.setText(response.getTeam_memo());
            }

            @Override
            public void onError(ResponseCaddyNote response) {
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });
    }

    private ClubInfo makeClubInfo(CaddyNoteInfo caddyNoteInfo) {
        ClubInfo clubInfo = new ClubInfo();
        clubInfo.wood = caddyNoteInfo.getWood();
        clubInfo.utility = caddyNoteInfo.getUtility();
        clubInfo.iron = caddyNoteInfo.getIron();
        clubInfo.putter = caddyNoteInfo.getPutter();
        clubInfo.wedge = caddyNoteInfo.getWedge();

        clubInfo.phoneNumber = caddyNoteInfo.getPhoneNumber();
        clubInfo.carNumber = caddyNoteInfo.getCarNumber();
        clubInfo.memo = caddyNoteInfo.getGuest_memo();
        return clubInfo;
    }

    private void setClubInfo(String guestId, ClubInfo clubInfo) {
        setProgressMessage("클럽 정보를 저장하는 중입니다.");

        String wood = getClubInfoText(clubInfo.wood.toString());
        String utility = getClubInfoText(clubInfo.utility.toString());
        String iron = getClubInfoText(clubInfo.iron.toString());
        String wedge = getClubInfoText(clubInfo.wedge.toString());
        String putter = getClubInfoText(clubInfo.putter.toString());

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setClubInfo(getActivity(), guestId, wood, utility, iron, wedge,
                putter, new DataInterface.ResponseCallback<ResponseData<Object>>() {
            @Override
            public void onSuccess(ResponseData<Object> response) {
                hideProgress();

                //화면을 갱신한
                getCaddyNote();
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

    private void setPersonalInfo(GuestInfo guestInfo) {
        setProgressMessage("클럽 정보를 저장하는 중입니다.");

        String guest_id = guestInfo.getReserveGuestId();
        String carNumber = guestInfo.getCarNo();
        String phoneNumber = guestInfo.getHp();
        String memo = guestInfo.getGuestMemo();

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setPersonalInfo(getActivity(), Global.reserveId, guest_id, carNumber, phoneNumber, memo,
               new DataInterface.ResponseCallback<ResponseData<Object>>() {
                    @Override
                    public void onSuccess(ResponseData<Object> response) {
                        hideProgress();

                        //화면을 갱신한
                        getCaddyNote();
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

    private void setTeamMemo(String teamMemo) {
        setProgressMessage("팀 메모를 저장하는 중입니다.");

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setTeamMemo(getActivity(), Global.reserveId, teamMemo,
                new DataInterface.ResponseCallback<ResponseData<Object>>() {
                    @Override
                    public void onSuccess(ResponseData<Object> response) {
                        hideProgress();

                        //화면을 갱신한
                        getCaddyNote();
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

    private void setGuestPhotos(String guestId, File file) {
        sendPhoto(guestId, file.getAbsolutePath(), "sign");
    }

    private void sendPhoto(String guestId, String path, String photoType) {
        setProgressMessage("클럽사진을 저장하는 중입니다.");

        RequestBody reserveId = RequestBody.create(MediaType.parse("text/plain"), Global.reserveId);
        RequestBody reserveGuestId = RequestBody.create(MediaType.parse("text/plain"), guestId);
        RequestBody photo_type = RequestBody.create(MediaType.parse("text/plain"), photoType);
        RequestBody photo_time = RequestBody.create(MediaType.parse("text/plain"), caddyNoteType.toString());
        RequestBody caddy_id = RequestBody.create(MediaType.parse("text/plain"), Global.CaddyNo);

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("img_file", path, requestBody);

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setGuestPhotos(reserveId, reserveGuestId,
                photo_type, photo_time, caddy_id, part, new DataInterface.ResponseCallback<PhotoResponse>() {
                    @Override
                    public void onSuccess(PhotoResponse response) {
                        getCaddyNote();
                    }

                    @Override
                    public void onError(PhotoResponse response) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    protected void showProgress(final String msg) {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null) {
                    // 객체를 1회만 생성한다.
                    pd = new ProgressDialog(getContext()); // 생성한다.
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
}
