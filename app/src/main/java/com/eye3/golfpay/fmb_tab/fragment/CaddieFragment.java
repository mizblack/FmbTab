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
import android.util.Log;
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
import com.eye3.golfpay.fmb_tab.util.EditorDialogFragment;
import com.eye3.golfpay.fmb_tab.util.SignatureDialogFragment;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CaddieFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    View v;
    private ArrayList<Guest> guestArrayList = Global.guestArrayList;
    private LinearLayout memberLinearLayout;
    private ArrayList<CaddieViewGuestItem> caddieViewGuestItemArrayList = new ArrayList<>();
    private ArrayList<Bitmap> signatureBitmapArrayList = new ArrayList<>();
    private String guestId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


            }
        }
    }

    private void setReserveGuestInfo(GuestInfo guestInfo) {
        DataInterface.getInstance(Global.HOST_ADDRESS_DEV).setReserveGuestInfo(guestInfo, new DataInterface.ResponseCallback<GuestInfoResponse>() {
            @Override
            public void onSuccess(GuestInfoResponse response) {
                if (response.getRetCode().equals("ok")) {
                    Toast.makeText(getContext(), "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
            GuestInfo guestInfo = new GuestInfo(guest.getId()
                    , guest.getCarNumber()
                    , guest.getPhoneNumber()
                    , guest.getMemo()
                    , guest.getTeamMemo()
                    , guest.getSignImage()
                    , guest.getClubImage());
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
        setGuestData();
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
        signatureBitmapArrayList = Global.signatureBitmapArrayList;
        for (int i = 0; i < caddieViewGuestItemArrayList.size(); i++) {
            CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(i);
            ImageView signatureImageView = caddieViewGuestItem.findViewById(R.id.signatureImageView);
            if (signatureBitmapArrayList.get(i) != null) {
                signatureImageView.setImageBitmap(signatureBitmapArrayList.get(i));
            }
        }
    }

    private void signatureDialogFragmentOnDismissListener(SignatureDialogFragment signatureDialogFragment) {
        signatureDialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
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
                photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void imageViewOnClick(View imageView, final int i) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guestId = guestArrayList.get(i).getId();
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA},
                            0);
                } else {
                    sendTakePhotoIntent();
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

                ImageView caddieImageView = caddieViewGuestItem.findViewById(R.id.caddieImageView);
                imageViewOnClick(caddieImageView, i);
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
            ImageView caddieImageView = caddieViewGuestItem.findViewById(R.id.caddieImageView);
            caddieImageView.setImageBitmap(rotate(bitmap, exifDegree));

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
        //create a file to write bitmap data
        File f = new File(context.getCacheDir(), filename);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        //  Bitmap bitmap = bitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10 /*ignored for PNG*/, bos);
        byte[] bitmapData = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    public static File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("FileUtil", "Directory not created");
        }
        return file;
    }

}
