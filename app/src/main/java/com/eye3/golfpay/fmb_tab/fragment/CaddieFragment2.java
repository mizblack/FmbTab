package com.eye3.golfpay.fmb_tab.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.listener.OnEditorFinishListener;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.model.guest.GuestInfo;
import com.eye3.golfpay.fmb_tab.model.info.GuestInfoResponse;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.util.EditorDialogFragment;
import com.eye3.golfpay.fmb_tab.util.SignatureDialogFragment;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CaddieFragment2 extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    View v;
    private List<Guest> guestList = Global.guestList;
    private LinearLayout memberLinearLayout;
    private ArrayList<CaddieViewGuestItem> caddieViewGuestItemArrayList = new ArrayList<>();
    private String guestId = "";
    private ArrayList<String> retCodeArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setSignImage(int i, View caddieViewGuestItem, String url) {
        ImageView signatureImageView = caddieViewGuestItem.findViewById(R.id.signatureImageView);
        setImagewithUri(signatureImageView, url);

    }

    private void setClubImage(int i, View caddieViewGuestItem, String url) {
        ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);
        setImagewithUri(clubImageView, url);

    }

    void setImagewithUri(ImageView img, String uri) {
        if (img != null) {
            Glide.with(getActivity())
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_noimage)
                    .into(img);
        }
    }

    void setImagewithBitmap(ImageView img, Bitmap bitmap) {
        if (img != null) {
            Glide.with(getActivity())
                    .asBitmap()
                    .load(bitmap)
                    //    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.ic_noimage)
                    .into(img);
        }
    }

    private void setGuestData() {
        guestList = Global.guestList;
        if (caddieViewGuestItemArrayList != null && caddieViewGuestItemArrayList.size() != 0) {
            for (int i = 0; i < caddieViewGuestItemArrayList.size(); i++) {
                View caddieViewGuestItem = caddieViewGuestItemArrayList.get(i);

                TextView memberNameTextView = caddieViewGuestItem.findViewById(R.id.memberNameTextView);
                memberNameTextView.setText(Global.guestList.get(i).getGuestName());

                EditText carNumberEditText = caddieViewGuestItem.findViewById(R.id.carNumberEditText);
                carNumberEditText.setText(Global.guestList.get(i).getCarNumber());

                EditText phoneNumberEditText = caddieViewGuestItem.findViewById(R.id.phoneNumberEditText);
                phoneNumberEditText.setText(Global.guestList.get(i).getPhoneNumber());

                TextView guestMemoContentTextView = caddieViewGuestItem.findViewById(R.id.guestMemoContentTextView);
                guestMemoContentTextView.setText(Global.guestList.get(i).getMemo());

                if (guestList.get(i).getSignUrl() != null) {

                    setSignImage(i, caddieViewGuestItem, Global.HOST_BASE_ADDRESS_AWS + guestList.get(i).getSignUrl());
                }

                if (guestList.get(i).getClubUrl() != null) {
                    setClubImage(i, caddieViewGuestItem, Global.HOST_BASE_ADDRESS_AWS + guestList.get(i).getClubUrl());
                }
            }
        }
    }

    private int countOk() {
        int count = 0;
        for (int i = 0; i < retCodeArrayList.size(); i++) {
            if (retCodeArrayList.get(i) != null) {
                if (retCodeArrayList.get(i).equals("ok")) {
                    count++;
                }
            }
        }
        return count;
    }

    private void setReserveGuestInfo(final int i, final GuestInfo guestInfo) {

        RequestBody reserveGuestId, carNo, hp, guestMemo = null, teamMemo = null, requestFile = null, requestFile2 = null;
        MultipartBody.Part signImage = null, clubImage = null;

        reserveGuestId = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getReserveGuestId());
        carNo = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.getCarNo());
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
                hideProgress();

                //여기 에러남
                retCodeArrayList.set(i, response.getRetCode());
                if (i == Global.guestList.size() - 1 && Global.guestList.size() - 1 == countOk()) {
                    Toast.makeText(getActivity(), "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    hideProgress();
                }
                if (i == Global.guestList.size() - 1 && Global.guestList.size() - 1 > countOk()) {
                    setReserveGuestInfo();
                }

            }

            @Override
            public void onError(GuestInfoResponse response) {
//                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
//                hideProgress();
            }
        });
    }

//    private void setReserveGuestInfo() {
//        showProgress("서버로 데이터를 전송중입니다....");
//        for (int i = 0; i < Global.guestList.size(); i++) {
//            Guest guest = Global.guestList.get(i);
//            File signatureImageFile = null;
//            File clubImageFile = null;
//
//
//            if (signatureBitmapArrayList.get(i) != null) {
//                signatureImageFile = getResizedFile(Objects.requireNonNull(getContext()), signatureBitmapArrayList.get(i), guestId + "_signature");
//            }
//
//            if (clubBitmapArrayList.get(i) != null) {
//                clubImageFile = getResizedFile(Objects.requireNonNull(getContext()), clubBitmapArrayList.get(i), guestId + "_club");
//            }
//
//            GuestInfo guestInfo = new GuestInfo();
//            guestInfo.setReserveGuestId(guest.getId());
//            guestInfo.setCarNo(guest.getCarNumber());
//            guestInfo.setHp(guest.getPhoneNumber());
//            guestInfo.setGuestMemo(guest.getMemo());
//            guestInfo.setTeamMemo(guest.getTeamMemo());
//            guestInfo.setSignImage(signatureImageFile);
//            guestInfo.setClubImage(clubImageFile);
//
//            setReserveGuestInfo(i, guestInfo);
//        }
//    }

    private void setReserveGuestInfo() {
        showProgress("서버로 데이터를 전송중입니다....");
        for (int i = 0; caddieViewGuestItemArrayList.size() > i; i++) {
            Guest guest = Global.guestList.get(i);
            File signatureImageFile = null;
            File clubImageFile = null;

            Bitmap signatureBitmap = null;
            ImageView signImgView = caddieViewGuestItemArrayList.get(i).findViewById(R.id.signatureImageView);
            signatureBitmap = ((BitmapDrawable) signImgView.getDrawable()).getBitmap();

            //    if (signatureBitmapArrayList.get(i) != null) {
            signatureImageFile = getResizedFilePNG(Objects.requireNonNull(getContext()), signatureBitmap, guestId + "_signature", 100);
            //   }

            Bitmap clubBitmap = null;
            ImageView clubImgView = caddieViewGuestItemArrayList.get(i).findViewById(R.id.clubImageView);
            clubBitmap = ((BitmapDrawable) clubImgView.getDrawable()).getBitmap();
            //    if (clubBitmapArrayList.get(i) != null) {
            clubImageFile = getResizedFile(Objects.requireNonNull(getContext()), clubBitmap, guestId + "_club", 10);
            //   }

            GuestInfo guestInfo = new GuestInfo();
            guestInfo.setReserveGuestId(guest.getId());
            guestInfo.setCarNo(guest.getCarNumber());
            guestInfo.setHp(guest.getPhoneNumber());
            guestInfo.setGuestMemo(guest.getMemo());
            guestInfo.setTeamMemo(guest.getTeamMemo());
            guestInfo.setSignImage(signatureImageFile);
            guestInfo.setClubImage(clubImageFile);

            setReserveGuestInfo(i, guestInfo);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        setDataTeamMemo();
        setGuestData();
        closeKeyboard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fr_caddie, container, false);
        memberLinearLayout = v.findViewById(R.id.memberLinearLayout);
        mParentActivity.showMainBottomBar();
        setDataTeamMemo();
        closeKeyboard();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createGuestList();
        setDataTeamMemo();
        teamMemoOnClick();
        v.findViewById(R.id.saveTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReserveGuestInfo();
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

        systemUIHide();
    }

//    private void memoEditorDialogFragmentOnDismissListener(EditorDialogFragment editorDialogFragment) {
////        setDataTeamMemo();
////        setGuestData();
//        editorDialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                setDataTeamMemo();
//                setGuestData();
//            }
//        });
//    }


    private void signatureDialogFragmentOnDismissListener(SignatureDialogFragment signatureDialogFragment) {
        signatureDialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //  signatureBitmapArrayList = Global.signatureBitmapArrayList;
                // setImageSignatureImageView();
            }
        });
    }

    private void closeKeyboard() {
        for (int i = 0; i < caddieViewGuestItemArrayList.size(); i++) {
            CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(i);
            closeKeyboard(caddieViewGuestItem.findViewById(R.id.carNumberEditText));
            closeKeyboard(caddieViewGuestItem.findViewById(R.id.phoneNumberEditText));
        }
    }

    private void guestListOnClick(final CaddieViewGuestItem caddieViewGuestItem) {
        caddieViewGuestItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
            }
        });
    }

    private void guestMemoLinearLayoutOnClick(final View guestMemoLinearLayout, final int i) {
        guestMemoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();
                guestMemoEditorDialogFragment.setGuestId(Global.guestList.get(i).getId());
                guestMemoEditorDialogFragment.setMemoContent(Global.guestList.get(i).getMemo());
                guestMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        closeKeyboard();
                        setDataTeamMemo();
                        setGuestData();
                    }
                });
                showDialogFragment(guestMemoEditorDialogFragment);
                systemUIHide();
            }
        });
    }

    private void signatureRelativeLayoutOnClick(View signatureRelativeLayout, final int position) {
        signatureRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();
                signatureDialogFragment.setGuestId(guestList.get(position).getId());
                signatureDialogFragment.setCaddieViewGuestItemArrayList(caddieViewGuestItemArrayList);
                showDialogFragment(signatureDialogFragment);
                signatureDialogFragmentOnDismissListener(signatureDialogFragment);
                systemUIHide();
            }
        });
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                takePictureIntent.putExtra("aspectX", 0.1);
//                takePictureIntent.putExtra("aspectY", 0.1);
                //   takePictureIntent.putExtra("scale", true);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void clubImageViewOnClick(final ImageView clubImageView, final int i) {
        clubImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guestId = guestList.get(i).getId();
                if (clubImageView.getDrawable() == null) {
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA},
                                0);
                    } else {
                        sendTakePhotoIntent();
                    }
                } else {
                    sendTakePhotoIntent();
//                    ClubImageDialogFragment clubImageDialogFragment = new ClubImageDialogFragment();
//                    clubImageDialogFragment.setGuestId(guestId);
//                    clubImageDialogFragment.setClubBitmapArrayList(clubBitmapArrayList);
//                    assert getFragmentManager() != null;
//                    clubImageDialogFragment.show(getFragmentManager(), TAG);
//                    systemUIHide();
                }
            }
        });
    }

    private void createGuestList() {
        if (guestList != null && guestList.size() != 0) {
            for (int i = 0; i < guestList.size(); i++) {
                final int finalI = i;
                final CaddieViewGuestItem caddieViewGuestItem = new CaddieViewGuestItem(mContext);

                memberLinearLayout.addView(caddieViewGuestItem);
                caddieViewGuestItemArrayList.add(i, caddieViewGuestItem);
                retCodeArrayList.add(null);
                guestListOnClick(caddieViewGuestItem);

                TextView memberNameTextView = caddieViewGuestItem.findViewById(R.id.memberNameTextView);
                memberNameTextView.setText(guestList.get(i).getGuestName());

                final EditText carNumberEditText = caddieViewGuestItem.findViewById(R.id.carNumberEditText);
                carNumberEditText.setText(guestList.get(i).getCarNumber());
                carNumberEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Global.guestList.get(finalI).setCarNumber(s.toString());
                    }
                });

                final EditText phoneNumberEditText = caddieViewGuestItem.findViewById(R.id.phoneNumberEditText);
                phoneNumberEditText.setText(guestList.get(i).getPhoneNumber());
                phoneNumberEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Global.guestList.get(finalI).setPhoneNumber(s.toString());
                    }
                });

                View guestMemoLinearLayout = caddieViewGuestItem.findViewById(R.id.guestMemoLinearLayout);
                guestMemoLinearLayoutOnClick(guestMemoLinearLayout, i);

                TextView guestMemoContentTextView = caddieViewGuestItem.findViewById(R.id.guestMemoContentTextView);
                guestMemoContentTextView.setText(guestList.get(i).getMemo());

                View signatureRelativeLayout = caddieViewGuestItem.findViewById(R.id.signatureRelativeLayout);
                signatureRelativeLayoutOnClick(signatureRelativeLayout, i);

                ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);
                clubImageViewOnClick(clubImageView, i);
            }

        }

    }

    private void setDataTeamMemo() {
        TextView teamMemoContentTextView = v.findViewById(R.id.teamMemoContentTextView);
        teamMemoContentTextView.setText(Global.guestList.get(0).getTeamMemo());
    }

    private void teamMemoOnClick() {
        View teamMemoLinearLayout = v.findViewById(R.id.teamMemoLinearLayout);
        final TextView teamMemoContentTextView = v.findViewById(R.id.teamMemoContentTextView);
        teamMemoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                EditorDialogFragment teamMemoEditorDialogFragment = new EditorDialogFragment();
                teamMemoEditorDialogFragment.setMemoContent(teamMemoContentTextView.getText().toString());
                showDialogFragment(teamMemoEditorDialogFragment);
                teamMemoEditorDialogFragment.setOnEditorFinishListener(new OnEditorFinishListener() {
                    @Override
                    public void OnEditorInputFinished(String memoContent) {
                        closeKeyboard();
                        setDataTeamMemo();
                        setGuestData();
                    }
                });
              //  memoEditorDialogFragmentOnDismissListener(teamMemoEditorDialogFragment);
                systemUIHide();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendTakePhotoIntent();
            } else {
                Toast.makeText(getActivity(), "카메라 사용 권한이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private int traversalByGuestId() {
        int index = 0;
        for (int i = 0; i < guestList.size(); i++) {
            if (guestId.equals(guestList.get(i).getId())) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            //    CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(traversalByGuestId());
            CaddieViewGuestItem caddieViewGuestItem = (CaddieViewGuestItem) memberLinearLayout.getChildAt(traversalByGuestId());
            ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);

            Bitmap clubImageBitmap = rotate(bitmap, exifDegree);
            //        clubBitmapArrayList.set(traversalByGuestId(), clubImageBitmap);
            //******
            //     clubImageView.setImageBitmap(clubImageBitmap);
            setImagewithBitmap(clubImageView, clubImageBitmap);

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
         //   ((MainActivity) mParentActivity).changeDrawerViewToMenuView();

        }
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static File getResizedFile(Context context, Bitmap bitmap, String filename, int quality) {

        File f = new File(context.getCacheDir(), filename + ".jpg");

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality /*ignored for PNG*/, bos);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}