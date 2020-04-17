package com.eye3.golfpay.fmb_tab.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.common.UIThread;
import com.eye3.golfpay.fmb_tab.listener.OnEditorFinishListener;
import com.eye3.golfpay.fmb_tab.listener.OnSignatureFinishListener;
import com.eye3.golfpay.fmb_tab.model.gallery.GalleryPicture;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.model.guest.GuestInfo;
import com.eye3.golfpay.fmb_tab.model.info.GuestInfoResponse;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.util.EditorDialogFragment;
import com.eye3.golfpay.fmb_tab.util.GolfClubDialogFragment;
import com.eye3.golfpay.fmb_tab.util.SignatureDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.eye3.golfpay.fmb_tab.util.Logger.TAG;


public class CaddieViewGuestItem extends RelativeLayout {
    private static int sendCountNum = 0;
    protected static ProgressDialog pd; // 프로그레스바 선언
    Guest mGuest;
    Context mContext;
    TextView mMemberNameTextView, mGuestMemoContentTextView;
    EditText mCarNumEditTextView, mPhoneNumberEditText;
    LinearLayout mGuestMemoLinearLayout;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    public ImageView mClubImageView, mSignatureImageView;

//    public CaddieViewGuestItem(Context context) {
//        super(context);
//
//        init(context);
//    }

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
        View v = inflater.inflate(R.layout.item_caddie_view_guest, this, false);

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
        View v = inflater.inflate(R.layout.item_caddie_view_guest, this, false);

        setTag(guest);

        mClubImageView = v.findViewById(R.id.clubImageCaddieView);
        if (mGuest.getClubUrl() != null)
            setImagewithUri(mClubImageView, Global.HOST_BASE_ADDRESS_AWS + mGuest.getClubUrl());
        onClickClubImageView(mClubImageView);
        mSignatureImageView = v.findViewById(R.id.signatureImageView);
        if (mGuest.getSignUrl() != null)
            setImagewithUri(mSignatureImageView, Global.HOST_BASE_ADDRESS_AWS + mGuest.getSignUrl());
        mMemberNameTextView = v.findViewById(R.id.memberNameTextView);
        mMemberNameTextView.setText(guest.getGuestName());
        mCarNumEditTextView = v.findViewById(R.id.carNumberEditText);
        mCarNumEditTextView.setText(guest.getCarNumber());
        mCarNumEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mGuest.setCarNumber(s.toString());
            }
        });

        mPhoneNumberEditText = v.findViewById(R.id.phoneNumberEditText);
        mPhoneNumberEditText.setText(guest.getPhoneNumber());
        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
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
                GolfClubDialogFragment golfClubDialogFragment = new GolfClubDialogFragment();
                golfClubDialogFragment.setGuest(mGuest);
                showDialogFragment(golfClubDialogFragment);
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
                    .placeholder(R.drawable.ic_noimage)
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
                closeKeyboard();
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();

                guestMemoEditorDialogFragment.setGuestId(mGuest.getGuestName());
                guestMemoEditorDialogFragment.setMemoContent(mGuest.getMemo());
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        closeKeyboard();
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

    private void closeKeyboard() {
        closeKeyboard(mCarNumEditTextView);
        closeKeyboard(mPhoneNumberEditText);
    }

    private void showDialogFragment(DialogFragment dialogFragment) {
        closeKeyboard();
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
        guestInfo.setTeamMemo(guest.getTeamMemo());
        guestInfo.setSignImage(signatureImageFile);
        guestInfo.setClubImage(clubImageFile);

        setReserveGuestInfo(guestInfo);
    }

    private void setReserveGuestInfo(GuestInfo guestInfo) {
        showProgress("데이터를 전송중입니다." );
        RequestBody reserveGuestId = null, carNo = null, hp = null, guestMemo = null, teamMemo = null, requestFile = null, requestFile2 = null;

        MultipartBody.Part signImage = null, clubImage = null;
        if (guestInfo.getReserveGuestId() != null)
            reserveGuestId = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getReserveGuestId());
        if (guestInfo.getCarNo() != null)
            carNo = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getCarNo());
        if (guestInfo.getHp() != null)
            hp = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getHp());
        if (guestInfo.getGuestMemo() != null) {
            guestMemo = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getGuestMemo());
        }
        if (guestInfo.getTeamMemo() != null) {
            teamMemo = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getTeamMemo());
        }
        if (guestInfo.getSignImage() != null) {
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getSignImage());
            signImage = MultipartBody.Part.createFormData("sign_image", guestInfo.getSignImage().getName(), requestFile);
        }
        if (guestInfo.getClubImage() != null) {
            requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getClubImage());
            clubImage = MultipartBody.Part.createFormData("club_image", guestInfo.getClubImage().getName(), requestFile2);
        }

        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).setReserveGuestInfo(reserveGuestId, carNo, hp, guestMemo, teamMemo, signImage, clubImage, new DataInterface.ResponseCallback<GuestInfoResponse>() {
            @Override
            public void onSuccess(GuestInfoResponse response) {
                if ("ok".equals(response.getRetCode())) {
                    if (Global.guestList.size() - 1 > sendCountNum) {
                        sendCountNum++;
                    } else if (sendCountNum == Global.guestList.size() - 1) {
                        Toast.makeText(mContext, "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }
                }

            }

            @Override
            public void onError(GuestInfoResponse response) {
                hideProgress();
                Toast.makeText(mContext, response.getRetMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
               hideProgress();
            }
        });
    }

    protected void showProgress(final String msg) {
        UIThread.executeInUIThread(new Runnable() {
            @Override
            public void run() {
                if (pd == null) {
                    // 객체를 1회만 생성한다.
                    pd = new ProgressDialog(mContext); // 생성한다.
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
