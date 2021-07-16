package com.eye3.golfpay.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.GuestSettingActivity;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.common.UIThread;
import com.eye3.golfpay.dialog.ClubInfoDialog;
import com.eye3.golfpay.dialog.GuestManageDialog;
import com.eye3.golfpay.dialog.SmsScoreDialog;
import com.eye3.golfpay.listener.ICaddyNoteListener;
import com.eye3.golfpay.listener.ITakePhotoListener;
import com.eye3.golfpay.model.caddyNote.CaddyNoteInfo;
import com.eye3.golfpay.model.caddyNote.ResponseCaddyNote;
import com.eye3.golfpay.model.guest.CaddieInfo;
import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.guest.GuestInfo;
import com.eye3.golfpay.model.guest.ReqClubInfo;
import com.eye3.golfpay.model.guest.ReserveGuestList;
import com.eye3.golfpay.model.photo.PhotoResponse;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.eye3.golfpay.util.Util;
import com.eye3.golfpay.view.CaddieGuestItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CaddieFragment extends BaseFragment implements ICaddyNoteListener  {

    protected String TAG = getClass().getSimpleName();
    View v;

    private CaddieInfo caddieInfo;
    private LinearLayout viewMain;
    private GuestManageDialog guestManageDialog;
    private ClubInfoDialog clubInfoDialog;
    private CaddieFragment me;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getReserveGuestList(Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId());

        me = this;
        caddieInfo = new CaddieInfo();

        for (Guest guest: Global.guestList) {
            GuestInfo gi = new GuestInfo();
            gi.setGuestName(guest.getGuestName());
            gi.setBagName(guest.getBagName());
            gi.setReserveGuestId(guest.getId());
            gi.setCarNo(guest.getCarNumber());
            gi.setHp(guest.getPhoneNumber());
            caddieInfo.getGuestInfo().add(gi);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fr_caddie, container, false);
        mParentActivity.showMainBottomBar();
        viewMain = v.findViewById(R.id.view_main);
        createGuestItem();
        getCaddyNote();

        v.findViewById(R.id.btn_temp_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "btn_temp_group", Toast.LENGTH_SHORT).show();
            }
        });

        v.findViewById(R.id.btn_guest_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<GuestInfo> guestInfo = caddieInfo.getGuestInfo();
                guestManageDialog = new GuestManageDialog(getContext(), guestInfo, me);
                WindowManager.LayoutParams wmlp = guestManageDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;
                guestManageDialog.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                guestManageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //화면을 갱신함
                       getCaddyNote();
                        //부모 화면을 갱신함
                       getReserveGuestList(Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId());
                    }
                });
                guestManageDialog.show();
            }
        });

        v.findViewById(R.id.btn_team_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeTeamPhoto();
            }
        });

        v.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
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

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getReserveGuestList(int reserveId) {
        showProgress("게스트의 정보를 받아오는 중입니다....");
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getReserveGuestList(reserveId, new DataInterface.ResponseCallback<ReserveGuestList>() {
            @Override
            public void onSuccess(ReserveGuestList response) {
                if (response.getRetMsg().equals("성공")) {
                    Global.guestList = response.getList();

                    if (Global.guestOrdering != null) {
                        for (int i = 0; i < Global.guestOrdering.size(); i++) {
                            String flag = Global.guestOrdering.get(i);
                            for (int j = i; j < Global.guestList.size(); j++) {
                                if (flag.equals(Global.guestList.get(j).getId())) {
                                    if (i == j) {
                                        break;
                                    }

                                    Collections.swap(Global.guestList, i, j);
                                }
                            }
                        }
                    }

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

    private void takeTeamPhoto() {
        ((MainActivity)mParentActivity).startCamera(AppDef.GuestPhoto, new ITakePhotoListener() {
            @Override
            public void onTakePhoto(String path) {
                sendPhoto("", path, "club");
            }
        });
    }

    private void sendPhoto(String guestId, String path, String photoType) {
        setProgressMessage("클럽사진을 저장하는 중입니다.");

        RequestBody reserveId = RequestBody.create(MediaType.parse("text/plain"), Global.reserveId);
        RequestBody reserveGuestId = RequestBody.create(MediaType.parse("text/plain"), guestId);
        RequestBody photo_type = RequestBody.create(MediaType.parse("text/plain"), photoType);
        RequestBody photo_time = RequestBody.create(MediaType.parse("text/plain"), "before");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GuestSettingActivity.Id && resultCode == 100) {
            int index = data.getIntExtra("index", 0);
            String type = data.getStringExtra("type");
            String value = data.getStringExtra("value");
            guestManageDialog.onInput(index, type, value);
        } else if (requestCode == GuestSettingActivity.Id+10 && resultCode == 100) {
            int index = data.getIntExtra("index", 0);
            String type = data.getStringExtra("type");
            String value = data.getStringExtra("value");

            CaddieGuestItem guestItemView = (CaddieGuestItem)viewMain.getChildAt(index);
            if (type.equals("phone")) {
                guestItemView.setPhoneNumber(value);
            } else if (type.equals("memo")) {
                guestItemView.setMemo(value);
            }
        } else if (requestCode == GuestSettingActivity.Id+20 && resultCode == 100) {
            int index = data.getIntExtra("index", 0);
            String value = data.getStringExtra("value");
            int id = data.getIntExtra("id", 0);
            clubInfoDialog.onInputMemo(index, value, id);
        }
    }

    private void createGuestItem() {

        for (int i = 0; caddieInfo.getGuestInfo().size() > i; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            viewMain.addView(createGuestItemView(caddieInfo.getGuestInfo().get(i), i), layoutParams);
        }
    }

    private View createGuestItemView(GuestInfo guest, int index) {
        CaddieGuestItem guestItemView = new CaddieGuestItem(getActivity(), this, index, guest, caddieInfo.getGuestInfo().size(), this);

        return guestItemView;
    }

    @Override
    public void onShowClubInfoDlg(String id) {

        clubInfoDialog = new ClubInfoDialog(getContext(), this, caddieInfo, findGuestId(id));
        WindowManager.LayoutParams wmlp = clubInfoDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        clubInfoDialog.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
        clubInfoDialog.show();

        clubInfoDialog.setiListenerDialog(new ClubInfoDialog.IListenerDialog() {
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
        for (Guest guest: Global.guestList) {
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

                //swap
                if (Global.guestOrdering != null) {
                    for (int i = 0; i < Global.guestOrdering.size(); i++) {
                        String flag = Global.guestOrdering.get(i);
                        for (int j = i; j < response.getData().size(); j++) {
                            if (flag.equals(response.getData().get(j).getId())) {
                                if (i == j) {
                                    break;
                                }

                                Collections.swap(response.getData(), i, j);
                            }
                        }
                    }
                }

                //swap
                if (Global.guestOrdering != null) {
                    for (int i = 0; i < Global.guestOrdering.size(); i++) {
                        String flag = Global.guestOrdering.get(i);
                        for (int j = i; j < caddieInfo.getGuestInfo().size(); j++) {
                            if (flag.equals(caddieInfo.getGuestInfo().get(j).getReserveGuestId())) {
                                if (i == j) {
                                    break;
                                }

                                Collections.swap(caddieInfo.getGuestInfo(), i, j);
                            }
                        }
                    }
                }

                for (int i = 0; i < response.getData().size(); i++) {
                    CaddyNoteInfo caddyNoteInfo = response.getData().get(i);
                    CaddieGuestItem view = (CaddieGuestItem)viewMain.getChildAt(i);
                    ClubInfo clubInfo = makeClubInfo(caddyNoteInfo);

                    if (caddieInfo.getGuestInfo().size() <= i) {
                        break;
                    }

                    caddieInfo.getGuestInfo().get(i).clubInfo = clubInfo;
                    view.drawName(caddieInfo.getGuestInfo().get(i).getGuestName(), caddieInfo.getGuestInfo().get(i).getBagName());
                    view.drawClubInfo(clubInfo);
                    if (!caddyNoteInfo.getSign_before().isEmpty())
                        view.drawSignImage(caddyNoteInfo.getSign_before().get(0).photo_url);

                    view.drawClubImage(caddyNoteInfo);
                }
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

        clubInfo.wood_memo = caddyNoteInfo.wood_memo;
        clubInfo.utility_memo = caddyNoteInfo.utility_memo;
        clubInfo.iron_memo = caddyNoteInfo.iron_memo;
        clubInfo.wedge_memo = caddyNoteInfo.wedge_memo;
        clubInfo.putter_memo = caddyNoteInfo.putter_memo;
        return clubInfo;
    }

    private void setClubInfo(String guestId, ClubInfo clubInfo) {
        setProgressMessage("클럽 정보를 저장하는 중입니다.");

        ReqClubInfo reqClubInfo = caddieInfo.getGuestInfo().get(findGuestId(guestId)).getReqClubInfo();

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setClubInfo(getActivity(), guestId,
                reqClubInfo, new DataInterface.ResponseCallback<ResponseData<Object>>() {
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
        String tabletName = guestInfo.getGuestName();

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setPersonalInfo(getActivity(), Global.reserveId, guest_id, carNumber, phoneNumber, memo, tabletName,
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
