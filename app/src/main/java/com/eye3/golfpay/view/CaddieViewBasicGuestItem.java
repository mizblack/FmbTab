package com.eye3.golfpay.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.listener.ICaddyNoteListener;
import com.eye3.golfpay.listener.OnEditorFinishListener;
import com.eye3.golfpay.listener.OnSignatureFinishListener;
import com.eye3.golfpay.model.caddyNote.CaddyNoteInfo;
import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.guest.GuestInfo;
import com.eye3.golfpay.util.EditorDialogFragment;
import com.eye3.golfpay.util.SignatureDialogFragment;
import com.eye3.golfpay.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static com.eye3.golfpay.util.Logger.TAG;


public class CaddieViewBasicGuestItem extends RelativeLayout {
    private static int sendCountNum = 0;
    protected static ProgressDialog pd; // 프로그레스바 선언

    Context mContext;
    TextView mGuestMemoContentTextView;
    TextView mCarNumTextView, mPhoneNumberText;
    LinearLayout mGuestMemoLinearLayout;
    GuestInfo guestInfo;

    TextView tv_clubInfo;
    TextView tv_wood;
    TextView tv_utility;
    TextView tv_iron;
    TextView tv_putter;
    TextView tv_wedge;
    TextView tv_cover;

    TextView tv_woodCount;
    TextView tv_utilityCount;
    TextView tv_ironCount;
    TextView tv_putterCount;
    TextView tv_wedgeCount;
    TextView tv_coverCount;


    private int guestTotalCount = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    public ImageView mClubImageView, mSignatureImageView;
    private ICaddyNoteListener listener;

    public CaddieViewBasicGuestItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CaddieViewBasicGuestItem(Context context, GuestInfo guestInfo, int guestTotalCount, ICaddyNoteListener listener) {
        super(context);
        this.mContext = context;
        this.guestInfo = guestInfo;
        this.guestTotalCount = guestTotalCount;
        this.listener = listener;
        init(context, guestInfo);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_caddie_view_basic_guest, this, false);

        mGuestMemoLinearLayout = v.findViewById(R.id.guestMemoLinearLayout);

        addView(v);
    }

    private void onClickClubImageView(ImageView imgview) {
        imgview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgview.getDrawable() == null) {
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull((MainActivity) mContext), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((MainActivity) mContext, new String[]{Manifest.permission.CAMERA},
                                0);
                    } else {
                        listener.onTakeClubPhoto(guestInfo.getReserveGuestId());
                    }
                } else {
                    listener.onTakeClubPhoto(guestInfo.getReserveGuestId());
                }
            }
        });
    }


    public void init(Context context, GuestInfo guest) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_caddie_view_basic_guest, this, false);

        if (guestTotalCount == 5) {
            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.width = (int) Util.convertDpToPixel(244, context);
            v.setLayoutParams(layoutParams);
        }

        setTag(guest);

        mClubImageView = v.findViewById(R.id.clubImageCaddieView);
        onClickClubImageView(mClubImageView);

        mSignatureImageView = v.findViewById(R.id.signatureImageView);

//        if (guestInfo.getSignImage() != null)
//            setImagewithUri(mSignatureImageView, Global.HOST_BASE_ADDRESS_AWS + mGuest.getSignUrl());

        mSignatureImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();
                signatureDialogFragment.setGuestId(guestInfo.getReserveGuestId());
                showDialogFragment(signatureDialogFragment);
                signatureDialogFragment.setOnSignatureFinishListener(new OnSignatureFinishListener() {
                    @Override
                    public void OnSignatureInputFinished(Bitmap bitmap) {
                        mSignatureImageView.setImageBitmap(bitmap);

                        //create a file to write bitmap data
                        try {
                            File f = new File(context.getCacheDir(), "temp.jpg");
                            f.createNewFile();

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
                            byte[] bitmapdata = bos.toByteArray();
                            FileOutputStream fos = new FileOutputStream(f);
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();
                            listener.onSignatureImage(guestInfo.getReserveGuestId(), f);

                        }catch (IOException ex) {

                        }
                    }
                });
            }
        });

        v.findViewById(R.id.club_info_list).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShowClubInfoDlg(guestInfo.getReserveGuestId());
            }
        });

        mCarNumTextView = v.findViewById(R.id.carNumberText);
        mCarNumTextView.setText(guest.getCarNo());
        mCarNumTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();

                guestMemoEditorDialogFragment.setGuestId(guestInfo.getGuestName());
                guestMemoEditorDialogFragment.setTextType("차량번호", "차량번호를 입력하세요");
                guestMemoEditorDialogFragment.setMemoContent(guestInfo.getGuestMemo());
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String text) {
                        closeKeyboard();
                        guestInfo.setCarNo(text);
                        mCarNumTextView.setText(text);
                        listener.onInputCarNumber(guestInfo.getReserveGuestId(), text);
                    }
                });
                showDialogFragment(guestMemoEditorDialogFragment);
            }
        });

        mPhoneNumberText = v.findViewById(R.id.phoneNumberText);

        mPhoneNumberText.setText(guest.getHp());
        mPhoneNumberText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();

                guestMemoEditorDialogFragment.setGuestId(guestInfo.getGuestName());
                guestMemoEditorDialogFragment.setTextType("연락처 입력", "연락처를 입력하세요");
                guestMemoEditorDialogFragment.setMemoContent(guestInfo.getGuestMemo());
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String text) {

                        closeKeyboard();
                        guestInfo.setHp(text);
                        mPhoneNumberText.setText(text);
                        listener.onInputPhoneNumber(guestInfo.getReserveGuestId(), text);
                    }
                });
                showDialogFragment(guestMemoEditorDialogFragment);
            }
        });

        tv_clubInfo = v.findViewById(R.id.tv_club_info);
        tv_wood = v.findViewById(R.id.tv_wood);
        tv_utility = v.findViewById(R.id.tv_utility);
        tv_iron = v.findViewById(R.id.tv_iron);
        tv_putter = v.findViewById(R.id.tv_putter);
        tv_wedge = v.findViewById(R.id.tv_wedge);
        tv_cover = v.findViewById(R.id.tv_cover);
        tv_woodCount = v.findViewById(R.id.tv_woodCount);
        tv_utilityCount = v.findViewById(R.id.tv_utilityCount);
        tv_ironCount = v.findViewById(R.id.tv_ironCount);
        tv_putterCount = v.findViewById(R.id.tv_putterCount);
        tv_wedgeCount = v.findViewById(R.id.tv_wedgeCount);
        tv_coverCount = v.findViewById(R.id.tv_coverCount);

        drawClubInfo(guestInfo.clubInfo);

        mGuestMemoLinearLayout = v.findViewById(R.id.guestMemoLinearLayout);
        guestMemoLinearLayoutOnClick(mGuestMemoLinearLayout);
        mGuestMemoContentTextView = v.findViewById(R.id.guestMemoContentTextView);
        mGuestMemoContentTextView.setText(guest.getGuestMemo());
        addView(v);
    }

    public void drawGuestInfo(GuestInfo guestInfo) {
        mCarNumTextView.setText(guestInfo.getCarNo());
        mPhoneNumberText.setText(guestInfo.getHp());
        mGuestMemoContentTextView.setText(guestInfo.getGuestMemo());
    }

    public void drawClubImage(CaddyNoteInfo caddyNoteInfo) {
        if (caddyNoteInfo.getClub_before().isEmpty())
            return;

        String uri = caddyNoteInfo.getClub_before().get(0).photo_url;

        Glide.with(mContext)
                .load(Global.HOST_BASE_ADDRESS_AWS + uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.camera)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mClubImageView.setScaleType(ImageView.ScaleType.CENTER);
                        //mClubImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.camera));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(mClubImageView);
    }

    public void drawSignImage(CaddyNoteInfo caddyNoteInfo) {

        if (caddyNoteInfo.getSign_before().isEmpty())
            return;

        String uri = caddyNoteInfo.getSign_before().get(0).photo_url;

        Glide.with(mContext)
                .load(Global.HOST_BASE_ADDRESS_AWS + uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.camera)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mSignatureImageView.setScaleType(ImageView.ScaleType.CENTER);
                        //mClubImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.camera));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(mSignatureImageView);
    }

    public void drawClubInfo(ClubInfo clubInfo) {
        guestInfo.setClubInfo(clubInfo);

        if (clubInfo == null || clubInfo.totalCount() == 0) {

            tv_clubInfo.setTextAppearance(R.style.GlobalTextView_12SP_ebonyBlack_NotoSans_Regular);
            tv_wood.setTextAppearance(R.style.GlobalTextView_15SP_lightGray_NotoSans_Regular);
            tv_utility.setTextAppearance(R.style.GlobalTextView_15SP_lightGray_NotoSans_Regular);
            tv_iron.setTextAppearance(R.style.GlobalTextView_15SP_lightGray_NotoSans_Regular);
            tv_putter.setTextAppearance(R.style.GlobalTextView_15SP_lightGray_NotoSans_Regular);
            tv_wedge.setTextAppearance(R.style.GlobalTextView_15SP_lightGray_NotoSans_Regular);
            tv_cover.setTextAppearance(R.style.GlobalTextView_15SP_lightGray_NotoSans_Regular);

            tv_clubInfo.setText("클럽정보");
            tv_woodCount.setText("");
            tv_utilityCount.setText("");
            tv_ironCount.setText("");
            tv_putterCount.setText("");
            tv_wedgeCount.setText("");
            tv_coverCount.setText("");
            return;
        }

        tv_clubInfo.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Medium);
        tv_wood.setTextAppearance(R.style.GlobalTextView_15SP_ebonyBlack_NotoSans_Medium);
        tv_utility.setTextAppearance(R.style.GlobalTextView_15SP_ebonyBlack_NotoSans_Medium);
        tv_iron.setTextAppearance(R.style.GlobalTextView_15SP_ebonyBlack_NotoSans_Medium);
        tv_putter.setTextAppearance(R.style.GlobalTextView_15SP_ebonyBlack_NotoSans_Medium);
        tv_wedge.setTextAppearance(R.style.GlobalTextView_15SP_ebonyBlack_NotoSans_Medium);
        tv_cover.setTextAppearance(R.style.GlobalTextView_15SP_ebonyBlack_NotoSans_Medium);

        tv_clubInfo.setText("클럽" + clubInfo.totalCount() + "개");
        tv_woodCount.setText(clubInfo.wood.size() + "개");
        tv_utilityCount.setText(clubInfo.utility.size() + "개");
        tv_ironCount.setText(clubInfo.iron.size() + "개");
        tv_putterCount.setText(clubInfo.getPutterCount() + "개");
        tv_wedgeCount.setText(clubInfo.wedge.size() + "개");
        tv_coverCount.setText(clubInfo.coverCount() + "개");
    }

    private void guestMemoLinearLayoutOnClick(final View guestMemoLinearLayout) {
        guestMemoLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();

                guestMemoEditorDialogFragment.setGuestId(guestInfo.getGuestName());
                guestMemoEditorDialogFragment.setMemoContent(guestInfo.getGuestMemo());
                guestMemoEditorDialogFragment.setTextType("개인메모", "메모를 입력하세요");
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        closeKeyboard();
                        guestInfo.setGuestMemo(memoContent);
                        mGuestMemoContentTextView.setText(memoContent);
                        listener.onInputMemo(guestInfo.getReserveGuestId(), memoContent);
                    }
                });
                showDialogFragment(guestMemoEditorDialogFragment);
            }
        });
    }

    public void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void closeKeyboard() {
        closeKeyboard(mCarNumTextView);
        closeKeyboard(mPhoneNumberText);
    }

    void setImagewithUri(ImageView img, String uri) {
        if (img != null) {
            Glide.with(mContext)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    //.placeholder(R.drawable.ic_noimage)
                    .into(img);
        }
    }

    void setImagewithBitmap(ImageView img, Bitmap bitmap) {
        if (img != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(bitmap)
                    //    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_noimage)
                    .into(img);
        }
    }


    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentManager supportFragmentManager = ((MainActivity) (mContext)).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();
    }

    public void setmClubImageView(ImageView mClubImageView) {
        this.mClubImageView = mClubImageView;
    }

    public ImageView getmClubImageView() {
        return this.mClubImageView;
    }
}
