package com.eye3.golfpay.fmb_tab.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;
import com.eye3.golfpay.fmb_tab.util.EditorDialogFragment;
import com.eye3.golfpay.fmb_tab.util.SignatureDialogFragment;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;

import java.util.ArrayList;
import java.util.Objects;

public class CaddieFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    //    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
//    private static final int REQUEST_IMAGE_CAPTURE = 672;
//    private String imageFilePath;
//    private Uri photoUri;
    View v;
    private ArrayList<Guest> guestArrayList = Global.guestArrayList;
    private LinearLayout memberLinearLayout;
    private ArrayList<CaddieViewGuestItem> caddieViewGuestItemArrayList = new ArrayList<>();
    private EditorDialogFragment teamMemoEditorDialogFragment = new EditorDialogFragment();
    private EditorDialogFragment guestMemoEditorDialogFragment = new EditorDialogFragment();
    private SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();

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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                sendTakePhotoIntent();
//            } else {
//                Toast.makeText(getActivity(), "카메라 사용 권한이 없습니다.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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
    }

    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        dialogFragment.show(transaction, "dialogFragment");
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

    private void closeKeyboard() {
        for (int i = 0; i < caddieViewGuestItemArrayList.size(); i++) {
            CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(i);
            closeKeyboard(caddieViewGuestItem.findViewById(R.id.carNumberEditText));
            closeKeyboard(caddieViewGuestItem.findViewById(R.id.phoneNumberEditText));
        }
    }

    private void guestListOnClick(final CaddieViewGuestItem caddieViewGuestItem, int position) {
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
                guestMemoEditorDialogFragment.setGuestId(guestArrayList.get(i).getId());
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
                signatureDialogFragment.setGuestId(guestArrayList.get(position).getId());
                showDialogFragment(signatureDialogFragment);
                systemUIHide();
            }
        });
    }

    private void createGuestList() {
        if (guestArrayList != null && guestArrayList.size() != 0) {
            for (int i = 0; i < guestArrayList.size(); i++) {
                final CaddieViewGuestItem caddieViewGuestItem = new CaddieViewGuestItem(mContext);
                memberLinearLayout.addView(caddieViewGuestItem);
                caddieViewGuestItemArrayList.add(caddieViewGuestItem);
                guestListOnClick(caddieViewGuestItem, i);

                TextView memberNameTextView = caddieViewGuestItem.findViewById(R.id.memberNameTextView);
                memberNameTextView.setText(guestArrayList.get(i).getGuestName());

                EditText carNumberEditText = caddieViewGuestItem.findViewById(R.id.carNumberEditText);
                carNumberEditText.setText(guestArrayList.get(i).getCarNumber());

                EditText phoneNumberEditText = caddieViewGuestItem.findViewById(R.id.phoneNumberEditText);
                phoneNumberEditText.setText(guestArrayList.get(i).getPhoneNumber());

                View guestMemoLinearLayout = caddieViewGuestItem.findViewById(R.id.guestMemoLinearLayout);
                guestMemoLinearLayoutOnClick(guestMemoLinearLayout, i);

                TextView guestMemoContentTextView = caddieViewGuestItem.findViewById(R.id.guestMemoContentTextView);
                guestMemoContentTextView.setText(guestArrayList.get(i).getMemo());

                View signatureRelativeLayout = caddieViewGuestItem.findViewById(R.id.signatureRelativeLayout);
                signatureRelativeLayoutOnClick(signatureRelativeLayout, i);

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
                teamMemoEditorDialogFragment.setMemoContent(teamMemoContentTextView.getText().toString());
                showDialogFragment(teamMemoEditorDialogFragment);
                memoEditorDialogFragmentOnDismissListener(teamMemoEditorDialogFragment);
                systemUIHide();
            }
        });
    }

//    private void sendTakePhotoIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//
//            if (photoFile != null) {
//                photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
//            ExifInterface exif = null;
//
//            try {
//                exif = new ExifInterface(imageFilePath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            int exifOrientation;
//            int exifDegree;
//
//            if (exif != null) {
//                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                exifDegree = exifOrientationToDegrees(exifOrientation);
//            } else {
//                exifDegree = 0;
//            }
//
////            ((ImageView) v.findViewById(R.id.caddieImageView)).setImageBitmap(rotate(bitmap, exifDegree));
//        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
//            ((MainActivity) mParentActivity).changeDrawerViewToMenuView();
//
//        }
//    }

//    private int exifOrientationToDegrees(int exifOrientation) {
//        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
//            return 90;
//        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
//            return 180;
//        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
//            return 270;
//        }
//        return 0;
//    }

//    private Bitmap rotate(Bitmap bitmap, float degree) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degree);
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }
//
//    private File createImageFile() throws IOException {
//        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "TEST_" + timeStamp + "_";
//        File storageDir = Objects.requireNonNull(getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,      /* prefix */
//                ".jpg",         /* suffix */
//                storageDir          /* directory */
//        );
//        imageFilePath = image.getAbsolutePath();
//        return image;
//    }

//    private static File getResizedFile(Context context, Bitmap bitmap, String filename) {
//        //create a file to write bitmap data
//        File f = new File(context.getCacheDir(), filename);
//        try {
//            f.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //Convert bitmap to byte array
//        //  Bitmap bitmap = bitmap;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 10 /*ignored for PNG*/, bos);
//        byte[] bitmapData = bos.toByteArray();
//
//        //write the bytes in file
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(f);
//            fos.write(bitmapData);
//            fos.flush();
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
