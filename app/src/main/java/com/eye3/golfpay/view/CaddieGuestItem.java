package com.eye3.golfpay.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.eye3.golfpay.activity.GuestSettingActivity;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.fragment.CaddieFragment;
import com.eye3.golfpay.listener.ICaddyNoteListener;
import com.eye3.golfpay.listener.OnSignatureFinishListener;
import com.eye3.golfpay.model.caddyNote.CaddyImage;
import com.eye3.golfpay.model.caddyNote.CaddyNoteInfo;
import com.eye3.golfpay.model.guest.ClubInfo;
import com.eye3.golfpay.model.guest.GuestInfo;
import com.eye3.golfpay.util.SignatureDialogFragment;
import com.eye3.golfpay.util.Util;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.eye3.golfpay.util.Logger.TAG;


public class CaddieGuestItem extends RelativeLayout {

    private static final int sendCountNum = 0;
    protected static ProgressDialog pd; // 프로그레스바 선언

    private int index = 0;
    Context mContext;
    CaddieFragment caddieFragment;
    TextView mGuestMemoContentTextView;
    TextView mGuestMemoInputTextView;
    TextView mPhoneNumberText;
    TextView mNameTextView;
    TextView mBagNameTextView;
    TextView mBigShow;
    ConstraintLayout memoView;
    GuestInfo guestInfo;
    TextView tv_clubInfo;
    List<CaddyImage> clubImages;

    private int guestTotalCount = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private ImageView mClubImageView;
    private ImageView mSignatureImageView;
    private ICaddyNoteListener listener;

    public CaddieGuestItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CaddieGuestItem(Context context, CaddieFragment fragment, int index, GuestInfo guestInfo, int guestTotalCount, ICaddyNoteListener listener) {
        super(context);
        this.mContext = context;
        this.caddieFragment = fragment;
        this.index = index;
        this.guestInfo = guestInfo;
        this.guestTotalCount = guestTotalCount;
        this.listener = listener;
        init(context, guestInfo);
    }

    public void init(Context context, GuestInfo guest) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_caddie_guest, this, false);

        if (guestTotalCount == 5) {
            v = inflater.inflate(R.layout.item_caddie_guest_small, this, false);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        if (index > 0)
            layoutParams.leftMargin = 1;

        v.findViewById(R.id.rootView).setBackgroundColor(Color.WHITE);
        v.findViewById(R.id.rootView).setPadding((int)Util.convertDpToPixel(12, mContext), 0, (int)Util.convertDpToPixel(12, mContext), 0);
        setTag(guest);

        mClubImageView = v.findViewById(R.id.clubImage);
        mSignatureImageView = v.findViewById(R.id.signatureImageView);
        v.findViewById(R.id.btn_shot).setOnClickListener(this::onClickShot);
        mBigShow = v.findViewById(R.id.btn_big_show);
        mBigShow.setOnClickListener(this::onClickBigShow);

        mNameTextView = v.findViewById(R.id.tv_name);
        mBagNameTextView = v.findViewById(R.id.tv_bag_name);

        mNameTextView.setText(guestInfo.getGuestName());
        mBagNameTextView.setText(guestInfo.getBagName());

        mSignatureImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();
                signatureDialogFragment.setGuestId(guestInfo.getReserveGuestId());
                showDialogFragment(signatureDialogFragment);
                signatureDialogFragment.setOnSignatureFinishListener(new OnSignatureFinishListener() {
                    @Override
                    public void OnSignatureInputFinished(Bitmap bitmap) {

                        //mSignatureImageView.setImageBitmap(bitmap);

                        //create a file to write bitmap data
                        try {
                            File f = new File(context.getCacheDir(), "temp.png");
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

                    @Override
                    public void OnSignatureInputFinished(String svg) {

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
        mGuestMemoContentTextView = v.findViewById(R.id.guestMemoContentTextView);
        mGuestMemoInputTextView = v.findViewById(R.id.tv_memo_input);
        mPhoneNumberText = v.findViewById(R.id.tv_phone_input);

        v.findViewById(R.id.view_phone).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), GuestSettingActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("type", "phone");
                intent.putExtra("value", guestInfo.clubInfo.phoneNumber);
                caddieFragment.startActivityForResult(intent, GuestSettingActivity.Id+10);
            }
        });

        tv_clubInfo = v.findViewById(R.id.tv_input_club);

        drawClubInfo(guestInfo.clubInfo);

        memoView = v.findViewById(R.id.view_memo);
        memoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GuestSettingActivity.class);
                intent.putExtra("index", index);
                intent.putExtra("type", "memo");
                intent.putExtra("value", guestInfo.clubInfo.memo);
                caddieFragment.startActivityForResult(intent, GuestSettingActivity.Id+10);
            }
        });

        addView(v, layoutParams);
    }

    public void setPhoneNumber(String text) {
        guestInfo.setHp(text);
        guestInfo.clubInfo.phoneNumber = text;
        listener.onInputPhoneNumber(guestInfo.getReserveGuestId(), text);
    }

    public void setMemo(String text) {
        guestInfo.setGuestMemo(text);
        guestInfo.clubInfo.memo = text;
        listener.onInputMemo(guestInfo.getReserveGuestId(), text);
    }

    public void onClickShot(View view) {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull((MainActivity) mContext), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((MainActivity) mContext, new String[]{Manifest.permission.CAMERA},
                    0);
        } else {
            listener.onTakeClubPhoto(guestInfo.getReserveGuestId());
        }
    }

    public void onClickBigShow(View view) {

        List<String> images = new ArrayList<>();
        for (CaddyImage ci : clubImages) {
            images.add(Global.HOST_BASE_ADDRESS_AWS + ci.photo_url);
        }

        new StfalconImageViewer.Builder<String>(mContext, images, new ImageLoader<String>() {
            @Override
            public void loadImage(ImageView imageView, String image) {
                Glide.with(mContext).load(image).into(imageView);
            }
        }).withStartPosition(images.size()-1).show();
    }

    public void drawClubImage(CaddyNoteInfo caddyNoteInfo) {

        clubImages = caddyNoteInfo.getClub_before();
        if (caddyNoteInfo.getClub_before().isEmpty()) {
            mBigShow.setTextAppearance(R.style.GlobalTextView_16SP_white_NotoSans_Bold);
            mBigShow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ripple_gray_bg));
            mBigShow.setEnabled(false);
            drawClubImage("");
            return;
        }

        CaddyImage caddyImage = caddyNoteInfo.getClub_before().get(caddyNoteInfo.getClub_before().size()-1);
        String uri = caddyImage.photo_url;
        drawClubImage(uri);

        mBigShow.setTextAppearance(R.style.GlobalTextView_16SP_white_NotoSans_Bold);
        mBigShow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ripple_black_bg));
        mBigShow.setEnabled(true);
    }

    private void drawClubImage(String uri) {
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

    public void drawSignImage(String uri) {

        Glide.with(mContext)
                .load(Global.HOST_BASE_ADDRESS_AWS + uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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
        if (!clubInfo.phoneNumber.isEmpty()) {
            mPhoneNumberText.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Medium);
            mPhoneNumberText.setText(clubInfo.phoneNumber);
        } else {
            mPhoneNumberText.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            mPhoneNumberText.setText("연락처 입력");
        }

        mGuestMemoContentTextView.setText(clubInfo.memo);
        if (clubInfo.memo.isEmpty()) {
            mGuestMemoInputTextView.setVisibility(View.VISIBLE);
        } else {
            mGuestMemoInputTextView.setVisibility(View.GONE);
        }

        if (clubInfo == null || clubInfo.totalCount() == 0) {
            tv_clubInfo.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            tv_clubInfo.setText("클럽정보 입력");
            return;
        }

        tv_clubInfo.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Medium);
        tv_clubInfo.setText("클럽" + clubInfo.totalCount() + "개");
    }

    public void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void closeKeyboard() {
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

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;

            //빈이미지로 간주
            int height = ((BitmapDrawable) drawable).getBitmap().getHeight();
            if (((BitmapDrawable) drawable).getBitmap().getHeight() == 30) {
                return false;
            }
        }

        return hasImage;
    }


    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentManager supportFragmentManager = ((MainActivity) (mContext)).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();
    }
}
