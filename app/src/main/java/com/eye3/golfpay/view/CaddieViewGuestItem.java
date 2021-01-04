package com.eye3.golfpay.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.eye3.golfpay.R;
import com.eye3.golfpay.activity.MainActivity;
import com.eye3.golfpay.common.AppDef;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.listener.OnEditorFinishListener;
import com.eye3.golfpay.listener.OnSignatureFinishListener;
import com.eye3.golfpay.model.gallery.GalleryPicture;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.guest.GuestInfo;
import com.eye3.golfpay.util.EditorDialogFragment;
import com.eye3.golfpay.util.SignatureDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static com.eye3.golfpay.util.Logger.TAG;


public class CaddieViewGuestItem extends RelativeLayout {
    private static int sendCountNum = 0;
    Guest mGuest;
    Context mContext;
    TextView mMemberNameTextView, mGuestMemoContentTextView;
    TextView mCarNumTextView, mPhoneNumberText;
    LinearLayout mGuestMemoLinearLayout;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    public ImageView mClubImageView, mSignatureImageView;

    public CaddieViewGuestItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public CaddieViewGuestItem(Context context, Guest guest) {
        super(context);
        this.mContext = context;
        this.mGuest = guest;
        init(context, guest);
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
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                mGuest.getArrClubImageList().add(new GalleryPicture(mGuest.getId(), "", photoUri.toString(),""));
//                takePictureIntent.putExtra("aspectX", 0.1);
//                takePictureIntent.putExtra("aspectY", 0.1);
                //   takePictureIntent.putExtra("scale", true);
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
        View v = inflater.inflate(R.layout.item_caddie_view_basic_guest, this, false);

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 212, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        params.rightMargin = 52;
        v.setLayoutParams(params);
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
        mMemberNameTextView = v.findViewById(R.id.memberNameTextView);
        mMemberNameTextView.setText(guest.getGuestName());
        mCarNumTextView = v.findViewById(R.id.carNumberText);
        mCarNumTextView.setText(guest.getCarNumber());
        mCarNumTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();

                guestMemoEditorDialogFragment.setGuestId(mGuest.getGuestName());
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String text) {

                        mPhoneNumberText.setText(text);
                    }
                });
                showDialogFragment(guestMemoEditorDialogFragment);
            }
        });

        mPhoneNumberText = v.findViewById(R.id.phoneNumberEditText);
        mPhoneNumberText.setText(guest.getPhoneNumber());
        mPhoneNumberText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mGuest.setPhoneNumber(s.toString());
            }
        });

        mGuestMemoLinearLayout = v.findViewById(R.id.guestMemoLinearLayout);
        guestMemoLinearLayoutOnClick(mGuestMemoLinearLayout);
        mGuestMemoContentTextView = v.findViewById(R.id.guestMemoContentTextView);
        mGuestMemoContentTextView.setText(guest.getMemo());
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

        v.findViewById(R.id.club_info_list).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Flags for full-screen mode:
                return false;
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

    private void guestMemoLinearLayoutOnClick(final View guestMemoLinearLayout) {
        guestMemoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();
                guestMemoEditorDialogFragment.setGuestId(mGuest.getGuestName());
                guestMemoEditorDialogFragment.setMemoContent(mGuest.getMemo());
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        mGuest.setMemo(memoContent);
                        mGuestMemoContentTextView.setText(memoContent);
                    }
                });
                showDialogFragment(guestMemoEditorDialogFragment);

            }
        });
    }

    public void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(mContext).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //  systemUIHide();
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

    public void setReserveGuestInfo(Guest guest) {
        File signatureImageFile = null;
        File clubImageFile = null;

        if (mSignatureImageView.getDrawable() != null)
            signatureImageFile = getResizedFilePNG(Objects.requireNonNull(getContext()), ((BitmapDrawable) mSignatureImageView.getDrawable()).getBitmap(), guest.getId() + "_signature", 10);

        if (mClubImageView.getDrawable() != null)
            clubImageFile = getResizedFile(Objects.requireNonNull(getContext()), ((BitmapDrawable) mClubImageView.getDrawable()).getBitmap(), guest.getId() + "_club");

        GuestInfo guestInfo = new GuestInfo();
        guestInfo.setReserveGuestId(guest.getId());
        guestInfo.setCarNo(guest.getCarNumber());
        guestInfo.setHp(guest.getPhoneNumber());
        guestInfo.setGuestMemo(guest.getMemo());
        //guestInfo.setTeamMemo(guest.getTeamMemo());
        guestInfo.setSignImage(signatureImageFile);
        guestInfo.setClubImage(clubImageFile);
    }




    private static File getResizedFile(Context context, Bitmap bitmap, String filename) {
        if (bitmap == null) {
            return null;
        }
        File f = new File(context.getCacheDir(), filename + ".jpg");

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapData = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    private static File getResizedFilePNG(Context context, Bitmap bitmap, String filename, int quality) {
        if (bitmap == null) {
            return null;
        }
        File f = new File(context.getCacheDir(), filename + ".png");

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality /*ignored for PNG*/, bos);
        byte[] bitmapData = bos.toByteArray();

        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }


}
