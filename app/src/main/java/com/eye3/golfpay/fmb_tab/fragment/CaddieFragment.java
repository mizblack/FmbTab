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

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.model.guest.GuestInfo;
import com.eye3.golfpay.fmb_tab.model.info.GuestInfoResponse;
import com.eye3.golfpay.fmb_tab.net.DataInterface;
import com.eye3.golfpay.fmb_tab.util.ClubImageDialogFragment;
import com.eye3.golfpay.fmb_tab.util.EditorDialogFragment;
import com.eye3.golfpay.fmb_tab.util.SignatureDialogFragment;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CaddieFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    View v;
    private ArrayList<Guest> guestArrayList = Global.guestArrayList;
    private LinearLayout memberLinearLayout;
    private ArrayList<CaddieViewGuestItem> caddieViewGuestItemArrayList = new ArrayList<>();
    private ArrayList<Bitmap> signatureBitmapArrayList = new ArrayList<>();
    private ArrayList<Bitmap> clubBitmapArrayList = new ArrayList<>();
    private String guestId = "";
    private Thread getShadeMenuBitmapThread;
    private Thread getSignImageBitmapThread;
    private Bitmap clubImageBitmap;
    private Bitmap signImageBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getSignImageBitmap(final String imageUrl) {
        getSignImageBitmapThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Global.HOST_BASE_ADDRESS_DEV + imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    clubImageBitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        getSignImageBitmapThread.start();
    }

    private void setSignImageBitmap(int i, View caddieViewGuestItem, Bitmap signImageBitmap) {
        caddieViewGuestItem.findViewById(R.id.signatureTextView).setVisibility(View.GONE);
        try {
            getShadeMenuBitmapThread.join();
            ImageView signatureImageView = caddieViewGuestItem.findViewById(R.id.signatureImageView);
            signatureBitmapArrayList.set(i, signImageBitmap);
            signatureImageView.setImageBitmap(signImageBitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void getClubImageBitmap(final String imageUrl) {
        getShadeMenuBitmapThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Global.HOST_BASE_ADDRESS_DEV + imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    signImageBitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        getShadeMenuBitmapThread.start();
    }

    private void setClubImageBitmap(int i, View caddieViewGuestItem, Bitmap clubImageBitmap) {
        try {
            getSignImageBitmapThread.join();
            ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);
            clubBitmapArrayList.set(i, clubImageBitmap);
            clubImageView.setImageBitmap(clubImageBitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void setGuestData() {
        guestArrayList = Global.guestArrayList;
        if (caddieViewGuestItemArrayList != null && caddieViewGuestItemArrayList.size() != 0) {
            for (int i = 0; i < caddieViewGuestItemArrayList.size(); i++) {
                View caddieViewGuestItem = caddieViewGuestItemArrayList.get(i);

                TextView memberNameTextView = caddieViewGuestItem.findViewById(R.id.memberNameTextView);
                memberNameTextView.setText(Global.guestArrayList.get(i).getGuestName());

                EditText carNumberEditText = caddieViewGuestItem.findViewById(R.id.carNumberEditText);
                carNumberEditText.setText(Global.guestArrayList.get(i).getCarNumber());

                EditText phoneNumberEditText = caddieViewGuestItem.findViewById(R.id.phoneNumberEditText);
                phoneNumberEditText.setText(Global.guestArrayList.get(i).getPhoneNumber());

                TextView guestMemoContentTextView = caddieViewGuestItem.findViewById(R.id.guestMemoContentTextView);
                guestMemoContentTextView.setText(Global.guestArrayList.get(i).getMemo());

                if (guestArrayList.get(i).getSignUrl() != null) {
                    getSignImageBitmap(guestArrayList.get(i).getSignUrl());
                    if (signImageBitmap != null) {
                        setSignImageBitmap(i, caddieViewGuestItem, signImageBitmap);
                    }
                }

                if (guestArrayList.get(i).getClubUrl() != null) {
                    getClubImageBitmap(guestArrayList.get(i).getClubUrl());
                    if (clubImageBitmap != null) {
                        setClubImageBitmap(i, caddieViewGuestItem, clubImageBitmap);
                    }
                }

                setImageSignatureImageView();
                setImageClubImageView();

            }
        }
    }

    private void setReserveGuestInfo(GuestInfo guestInfo) {

        showProgress("메모 전송중입니다.");
        RequestBody requestFile = null, requestFile2 = null;
        MultipartBody.Part signImage = null, clubImage = null;
        if (guestInfo.signImage != null) {
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.signImage);
            signImage = MultipartBody.Part.createFormData("sign_image", guestInfo.signImage.getName(), requestFile);
        }
        if (guestInfo.clubImage != null) {
            requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.clubImage);
            clubImage = MultipartBody.Part.createFormData("club_image", guestInfo.clubImage.getName(), requestFile2);
        }
        RequestBody reserveGuestId = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.reserveGuestId);
        RequestBody carNo = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.carNo);
        RequestBody hp = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.hp);
        RequestBody guestMemo = null;
        if (guestInfo.guestMemo != null) {
            guestMemo = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.guestMemo);
        }
        RequestBody teamMemo = RequestBody.create(MediaType.parse("multipart/form-data"), guestInfo.teamMemo);


        DataInterface.getInstance(Global.HOST_ADDRESS_DEV).setReserveGuestInfo(reserveGuestId, carNo, hp, guestMemo, teamMemo, signImage, clubImage, new DataInterface.ResponseCallback<GuestInfoResponse>() {
            @Override
            public void onSuccess(GuestInfoResponse response) {
                hideProgress();
                if (response.getRetCode().equals("ok")) {
                    Toast.makeText(getActivity(), "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(GuestInfoResponse response) {
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });
    }

    private void setReserveGuestInfo() {
        showProgress("서버로 데이터를 전송중입니다....");
        for (int i = 0; i < Global.guestArrayList.size(); i++) {
            Guest guest = Global.guestArrayList.get(i);
            File signatureImageFile = null;
            File clubImageFile = null;


            if (signatureBitmapArrayList.get(i) != null) {
                signatureImageFile = getResizedFile(Objects.requireNonNull(getContext()), signatureBitmapArrayList.get(i), guestId + "_signature");
            }

            if (clubBitmapArrayList.get(i) != null) {
                clubImageFile = getResizedFile(Objects.requireNonNull(getContext()), clubBitmapArrayList.get(i), guestId + "_club");
            }

            GuestInfo guestInfo = new GuestInfo(guest.getId()
                    , guest.getCarNumber()
                    , guest.getPhoneNumber()
                    , guest.getMemo()
                    , guest.getTeamMemo()
                    , signatureImageFile
                    , clubImageFile
            );

            setReserveGuestInfo(guestInfo);
            if (i == Global.guestArrayList.size() - 1) {
                hideProgress();
            }
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
        setGuestData();
        v.findViewById(R.id.saveTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReserveGuestInfo();
            }
        });

    }

    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentManager supportFragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        dialogFragment.show(transaction, TAG);
        assert dialogFragment.getFragmentManager() != null;
        dialogFragment.getFragmentManager().executePendingTransactions();
        systemUIHide();
    }

    private void memoEditorDialogFragmentOnDismissListener(EditorDialogFragment editorDialogFragment) {
        editorDialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setDataTeamMemo();
                setGuestData();
            }
        });
    }

    private void setImageSignatureImageView() {
        for (int i = 0; i < caddieViewGuestItemArrayList.size(); i++) {
            CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(i);
            ImageView signatureImageView = caddieViewGuestItem.findViewById(R.id.signatureImageView);
            if (signatureBitmapArrayList.get(i) != null) {
                signatureImageView.setImageBitmap(signatureBitmapArrayList.get(i));

            }
        }
    }

    private void setImageClubImageView() {
        for (int i = 0; i < caddieViewGuestItemArrayList.size(); i++) {
            CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(i);
            ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);
            if (clubBitmapArrayList.get(i) != null) {
                clubImageView.setImageBitmap(clubBitmapArrayList.get(i));

            }
        }
    }

    private void signatureDialogFragmentOnDismissListener(SignatureDialogFragment signatureDialogFragment) {
        signatureDialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                signatureBitmapArrayList = Global.signatureBitmapArrayList;
                setImageSignatureImageView();
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
                EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();
                guestMemoEditorDialogFragment.setGuestId(Global.guestArrayList.get(i).getId());
                guestMemoEditorDialogFragment.setMemoContent(Global.guestArrayList.get(i).getMemo());
                showDialogFragment(guestMemoEditorDialogFragment);
                memoEditorDialogFragmentOnDismissListener(guestMemoEditorDialogFragment);
                systemUIHide();
            }
        });
    }

    private void signatureRelativeLayoutOnClick(View signatureRelativeLayout, final int position) {
        signatureRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.signatureBitmapArrayList = signatureBitmapArrayList;
                SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();
                signatureDialogFragment.setGuestId(guestArrayList.get(position).getId());
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

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void clubImageViewOnClick(final ImageView clubImageView, final int i) {
        clubImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guestId = guestArrayList.get(i).getId();
                if (clubImageView.getDrawable() == null) {
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA},
                                0);
                    } else {
                        sendTakePhotoIntent();
                    }
                } else {
                    ClubImageDialogFragment clubImageDialogFragment = new ClubImageDialogFragment();
                    clubImageDialogFragment.setGuestId(guestId);
                    clubImageDialogFragment.setClubBitmapArrayList(clubBitmapArrayList);
                    assert getFragmentManager() != null;
                    clubImageDialogFragment.show(getFragmentManager(), TAG);
                    systemUIHide();
                }
            }
        });
    }

    private void createGuestList() {
        if (guestArrayList != null && guestArrayList.size() != 0) {
            for (int i = 0; i < guestArrayList.size(); i++) {
                final int finalI = i;
                final CaddieViewGuestItem caddieViewGuestItem = new CaddieViewGuestItem(mContext);
                memberLinearLayout.addView(caddieViewGuestItem);
                caddieViewGuestItemArrayList.add(caddieViewGuestItem);
                signatureBitmapArrayList.add(null);
                clubBitmapArrayList.add(null);
                guestListOnClick(caddieViewGuestItem);

                TextView memberNameTextView = caddieViewGuestItem.findViewById(R.id.memberNameTextView);
                memberNameTextView.setText(guestArrayList.get(i).getGuestName());

                final EditText carNumberEditText = caddieViewGuestItem.findViewById(R.id.carNumberEditText);
                carNumberEditText.setText(guestArrayList.get(i).getCarNumber());
                carNumberEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Global.guestArrayList.get(finalI).setCarNumber(s.toString());
                    }
                });

                final EditText phoneNumberEditText = caddieViewGuestItem.findViewById(R.id.phoneNumberEditText);
                phoneNumberEditText.setText(guestArrayList.get(i).getPhoneNumber());
                phoneNumberEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Global.guestArrayList.get(finalI).setPhoneNumber(s.toString());
                    }
                });

                View guestMemoLinearLayout = caddieViewGuestItem.findViewById(R.id.guestMemoLinearLayout);
                guestMemoLinearLayoutOnClick(guestMemoLinearLayout, i);

                TextView guestMemoContentTextView = caddieViewGuestItem.findViewById(R.id.guestMemoContentTextView);
                guestMemoContentTextView.setText(guestArrayList.get(i).getMemo());

                View signatureRelativeLayout = caddieViewGuestItem.findViewById(R.id.signatureRelativeLayout);
                signatureRelativeLayoutOnClick(signatureRelativeLayout, i);

                ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);
                clubImageViewOnClick(clubImageView, i);
            }
        }
    }

    private void setDataTeamMemo() {
        TextView teamMemoContentTextView = v.findViewById(R.id.teamMemoContentTextView);
        teamMemoContentTextView.setText(Global.guestArrayList.get(0).getTeamMemo());
    }

    private void teamMemoOnClick() {
        View teamMemoLinearLayout = v.findViewById(R.id.teamMemoLinearLayout);
        final TextView teamMemoContentTextView = v.findViewById(R.id.teamMemoContentTextView);
        teamMemoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditorDialogFragment teamMemoEditorDialogFragment = new EditorDialogFragment();
                teamMemoEditorDialogFragment.setMemoContent(teamMemoContentTextView.getText().toString());
                showDialogFragment(teamMemoEditorDialogFragment);
                memoEditorDialogFragmentOnDismissListener(teamMemoEditorDialogFragment);
                systemUIHide();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
        for (int i = 0; i < guestArrayList.size(); i++) {
            if (guestId.equals(guestArrayList.get(i).getId())) {
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

            CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(traversalByGuestId());
            ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);

            Bitmap clubImageBitmap = rotate(bitmap, exifDegree);
            clubBitmapArrayList.set(traversalByGuestId(), clubImageBitmap);
            clubImageView.setImageBitmap(clubImageBitmap);

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            ((MainActivity) mParentActivity).changeDrawerViewToMenuView();

        }
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static File getResizedFile(Context context, Bitmap bitmap, String filename) {

        File f = new File(context.getCacheDir(), filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10 /*ignored for PNG*/, bos);
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

//    public static File getAlbumStorageDir(Context context, String albumName) {
//        // Get the directory for the app's private pictures directory.
//        File file = new File(context.getExternalFilesDir(
//                Environment.DIRECTORY_PICTURES), albumName);
//        if (!file.mkdirs()) {
//            Log.e("FileUtil", "Directory not created");
//        }
//        return file;
//    }

}
