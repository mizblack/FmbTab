package com.eye3.golfpay.fmb_tab.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.AppDef;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.listener.OnEditorFinishListener;
import com.eye3.golfpay.fmb_tab.listener.OnSignatureFinishListener;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.util.EditorDialogFragment;
import com.eye3.golfpay.fmb_tab.util.SignatureDialogFragment;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;



public class CaddieViewGuestItem extends RelativeLayout {
     Guest mGuest;
     Context mContext;
     TextView mMemberNameTextView, mGuestMemoContentTextView;
     EditText mCarNumEditTextView, mPhoneNumberEditText;
    LinearLayout mGuestMemoLinearLayout;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
  public ImageView mClubImageView  ,mSignatureImageView;
    public CaddieViewGuestItem(Context context) {
        super(context);

        init(context);
    }
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

    private void onClickClubImageView(ImageView imgview){
        imgview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTakePhotoIntent();
            }
        });
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("guestId", "2000");
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(mContext).getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(mContext, mContext.getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                takePictureIntent.putExtra("aspectX", 0.1);
//                takePictureIntent.putExtra("aspectY", 0.1);
                //   takePictureIntent.putExtra("scale", true);
                ( (Activity) mContext).startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(mContext).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        AppDef.imageFilePath = image.getAbsolutePath();
        AppDef.guestid = mGuest.getId();
        return image;
    }




    private void init(Context context, Guest guest) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View guestItemView = inflater.inflate(R.layout.item_caddie_view_guest, this, false);

        setTag(guest);
        //여기 널에러
        mClubImageView = guestItemView.findViewById(R.id.clubImageView);
        onClickClubImageView(mClubImageView);
        mSignatureImageView = guestItemView.findViewById(R.id.signatureImageView);
        mMemberNameTextView = guestItemView.findViewById(R.id.memberNameTextView);
        mMemberNameTextView.setText(guest.getGuestName());
        mCarNumEditTextView = guestItemView.findViewById(R.id.carNumberEditText);
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

        mPhoneNumberEditText = guestItemView.findViewById(R.id.phoneNumberEditText);
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

        mGuestMemoLinearLayout = guestItemView.findViewById(R.id.guestMemoLinearLayout);
        guestMemoLinearLayoutOnClick(mGuestMemoLinearLayout);
        mGuestMemoContentTextView = guestItemView.findViewById(R.id.guestMemoContentTextView);
        mGuestMemoContentTextView.setText(guest.getMemo());
        mClubImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();
                signatureDialogFragment.setGuestId(mGuest.getId());
                showDialogFragment(signatureDialogFragment);
                signatureDialogFragment.setGuestId(mGuest.getId());
                showDialogFragment(signatureDialogFragment);
                signatureDialogFragment.setOnSignatureFinishListener(new OnSignatureFinishListener() {
                    @Override
                    public void OnSignatureInputFinished(Bitmap bitmap) {
                          mClubImageView.setImageBitmap(bitmap);
                    }
                });
        }
        });

        if (guest.getSignUrl() != null) {

            setSignImage(guestItemView, Global.HOST_BASE_ADDRESS_AWS + guest.getSignUrl());
        }

        if (guest.getClubUrl() != null) {
            setClubImage(guestItemView, Global.HOST_BASE_ADDRESS_AWS + guest.getClubUrl());
        }


        addView(guestItemView);
    }

    private void setSignImage(View caddieViewGuestItem, String url) {
        ImageView signatureImageView = caddieViewGuestItem.findViewById(R.id.signatureImageView);
        setImagewithUri(signatureImageView, url);

    }

    private void setClubImage(View caddieViewGuestItem, String url) {
        ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);
        setImagewithUri(clubImageView, url);

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

    private void guestMemoLinearLayoutOnClick(final View guestMemoLinearLayout) {
        guestMemoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();
                Guest guest = (Guest) view.getTag();
                String guestId = guest.getId();
                guestMemoEditorDialogFragment.setGuestId(guestId);
                guestMemoEditorDialogFragment.setMemoContent(guest.getMemo());
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        closeKeyboard();
                        mGuest.setMemo(memoContent);

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
        FragmentManager supportFragmentManager = ((Activity) mContext).getFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
      //  dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();

      //  systemUIHide();

    }

    public void setmClubImageView(ImageView mClubImageView) {
        this.mClubImageView = mClubImageView;
    }

    public ImageView getmClubImageView() {
       return this.mClubImageView ;
    }


}
