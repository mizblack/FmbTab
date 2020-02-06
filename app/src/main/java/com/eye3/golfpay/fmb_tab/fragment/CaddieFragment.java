package com.eye3.golfpay.fmb_tab.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.activity.MainActivity;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.teeup.GuestDatum;
import com.eye3.golfpay.fmb_tab.view.CaddieViewGuestItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class CaddieFragment extends BaseFragment {

    protected String TAG = getClass().getSimpleName();
    LinearLayout mImgClubPic;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    ImageView mPhoto;
    View v;
    RecyclerView guestRecyclerView;
    private int position;
    LinearLayout memberLinearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt("selectedTeeUpIndex");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fr_caddie, container, false);
        memberLinearLayout = v.findViewById(R.id.memberLinearLayout);
//        mImgClubPic = v.findViewById(R.id.club_pic);
//        mImgClubPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
//                        Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    ActivityCompat.requestPermissions(getActivity(),
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//
//                } else {
//                    sendTakePhotoIntent();
//                }
//            }
//        });
        mParentActivity.showMainBottomBar();
//        mPhoto = v.findViewById(R.id.caddieImageView);
        return v;
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


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createGuestList(Global.teeUpTime.getTodayReserveList().get(position).getGuestData());
    }

    private void guestListOnClick(CaddieViewGuestItem caddieViewGuestItem, int position) {
        caddieViewGuestItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void signatureRelativeLayoutOnClick(View signatureRelativeLayout, int position) {
        signatureRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void createGuestList(ArrayList<GuestDatum> guestList) {
        if (guestList != null && guestList.size() != 0) {
            for (int i = 0; i < guestList.size(); i++) {
                final CaddieViewGuestItem caddieViewGuestItem = new CaddieViewGuestItem(mContext);
                memberLinearLayout.addView(caddieViewGuestItem);
                guestListOnClick(caddieViewGuestItem, i);

                TextView memberNameTextView = caddieViewGuestItem.findViewById(R.id.memberNameTextView);
                memberNameTextView.setText(guestList.get(i).getGuestName());

                View signatureRelativeLayout = caddieViewGuestItem.findViewById(R.id.signatureRelativeLayout);
                signatureRelativeLayoutOnClick(signatureRelativeLayout, i);


            }
        }
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

//            ((ImageView) v.findViewById(R.id.caddieImageView)).setImageBitmap(rotate(bitmap, exifDegree));
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            ((MainActivity) mParentActivity).changeDrawerViewToMenuView();

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

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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

//    public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestItemViewHolder> {
//
//        ArrayList<GuestDatum> guestList;
//        TextView memberNameTextView;
//        View memberDivider;
//
//        GuestAdapter(Context context, ArrayList<GuestDatum> todayReserveList) {
//            this.guestList = todayReserveList;
//        }
//
//        class GuestItemViewHolder extends RecyclerView.ViewHolder {
//
//            GuestItemViewHolder(View view) {
//                super(view);
//
//                memberNameTextView = view.findViewById(R.id.memberNameTextView);
//                memberDivider = view.findViewById(R.id.memberDivider);
//
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        int position = getAdapterPosition();
//                        memberNameTextView.setTextColor(0xff000000);
//                        memberDivider.setBackgroundColor(0xff000000);
//
//                        // Todo 선택된 포지션을 제외한 모든 아이템을 아래의 색상으로 변경
//                        //  memberNameTextView.setTextColor(0xffcccccc);
//                        //  memberDivider.setBackgroundColor(0xffcccccc);
//
//                    }
//                });
//
//            }
//
//        }
//
//        @NonNull
//        @Override
//        public GuestItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_guest, viewGroup, false);
//            return new GuestItemViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull GuestItemViewHolder scoreItemViewHolder, int position) {
//            memberNameTextView.setText(guestList.get(position).getGuestName());
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return guestList.size();
//        }
//
//    }

}
