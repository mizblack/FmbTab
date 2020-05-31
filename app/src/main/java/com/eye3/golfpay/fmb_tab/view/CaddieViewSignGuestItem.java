package com.eye3.golfpay.fmb_tab.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraCharacteristics;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.dialog.ClubInfoDialog;
import com.eye3.golfpay.fmb_tab.listener.OnSignatureFinishListener;
import com.eye3.golfpay.fmb_tab.model.gallery.GalleryPicture;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.util.SignatureDialogFragment;
import com.eye3.golfpay.fmb_tab.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static com.eye3.golfpay.fmb_tab.util.Logger.TAG;


public class CaddieViewSignGuestItem extends RelativeLayout {
    Guest mGuest;
    Context mContext;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    public ImageView mClubImageView, mSignatureImageView;

    public CaddieViewSignGuestItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CaddieViewSignGuestItem(Context context, Guest guest) {
        super(context);
        this.mContext = context;
        this.mGuest = guest;
        init(context, guest);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_caddie_view_sign_guest, this, false);
        addView(v);
    }

    private void onClickClubImageView(ImageView imgview) {
        imgview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgview.getDrawable() == null) {
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull((MainActivity)mContext), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((MainActivity)mContext, new String[]{Manifest.permission.CAMERA},
                                0);
                    } else {
                        sendTakePhotoIntent();
                    }
                } else {
                    sendTakePhotoIntent();

                }
            }
        });
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(Objects.requireNonNull(mContext).getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(mContext, mContext.getPackageName(), photoFile);
                //takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                mGuest.getArrClubImageList().add(new GalleryPicture(mGuest.getId(), "", photoUri.toString(),""));
                ((Activity) mContext).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile()  {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
     //   String imageFileName = "Caddie_Pic" + timeStamp + "_";
         String imageFileName = "Caddie_Pic_" + mGuest.getId() + "_";
        File storageDir = Objects.requireNonNull(mContext).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,      /* prefix */
                    ".jpg",         /* suffix */
                    storageDir          /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppDef.imageFilePath = image.getAbsolutePath();
        AppDef.guestid = mGuest.getId();
        return image;
    }


    public void init(Context context, Guest guest) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_caddie_view_sign_guest, this, false);
        setTag(guest);

        mClubImageView = v.findViewById(R.id.clubImageCaddieView);
        if (mGuest.getClubUrl() != null) {
            //setImagewithUri(mClubImageView, Global.HOST_BASE_ADDRESS_AWS + mGuest.getClubUrl());

            Glide.with(mContext)
                    .load(Global.HOST_BASE_ADDRESS_AWS + mGuest.getClubUrl())
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
        onClickClubImageView(mClubImageView);
        mSignatureImageView = v.findViewById(R.id.signatureImageView);
        if (mGuest.getSignUrl() != null)
            setImagewithUri(mSignatureImageView, Global.HOST_BASE_ADDRESS_AWS + mGuest.getSignUrl());

        mSignatureImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();
                signatureDialogFragment.setGuestId(mGuest.getId());
                showDialogFragment(signatureDialogFragment);
                signatureDialogFragment.setOnSignatureFinishListener(new OnSignatureFinishListener() {
                    @Override
                    public void OnSignatureInputFinished(Bitmap bitmap) {
                        mSignatureImageView.setImageBitmap(bitmap);
                    }
                });
            }
        });

        v.findViewById(R.id.club_info_list).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ClubInfoDialog dlg = new ClubInfoDialog(context);
                WindowManager.LayoutParams wmlp = dlg.getWindow().getAttributes();
                wmlp.gravity = Gravity.CENTER;
                dlg.getWindow().getDecorView().setSystemUiVisibility(Util.DlgUIFalg);
                dlg.show();
            }
        });

        addView(v);
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
