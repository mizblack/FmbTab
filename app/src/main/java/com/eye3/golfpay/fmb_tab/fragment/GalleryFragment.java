package com.eye3.golfpay.fmb_tab.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.common.Global;
import com.eye3.golfpay.fmb_tab.model.gallery.GalleryPicture;
import com.eye3.golfpay.fmb_tab.model.guest.Guest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends BaseFragment {

    LinearLayout mLinearGuestTab;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    Button mBtnTakePicture;
    List<GalleryPicture> mPictureList;
    ImageView mPictureImage;
    ImageView mPictureClose;
    RecyclerView mGalleryRecycler;
    GalleryAdapter mGalleryAdatper;
    FrameLayout mFlPicture;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPictureList = new ArrayList<>();
        if(Global.guestList != null) {
            for (int i = 0; Global.guestList.size() > i; i++) {
                Guest a_guest = Global.guestList.get(i);
                for (int j = 0; a_guest.getArrClubImageList().size() > j; j++)
                    if(a_guest.getArrClubImageList().get(j).uri == null && a_guest.getArrClubImageList().get(j).file_url != null )
                        mPictureList.add(new GalleryPicture("0", "1234", Global.HOST_BASE_ADDRESS_AWS + a_guest.getArrClubImageList().get(j).file_url, ""));
                    else
                        mPictureList.add(new GalleryPicture("0", "1234", a_guest.getArrClubImageList().get(j).uri, ""));
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_galllery, container, false);
        mLinearGuestTab = v.findViewById(R.id.guestTabLinearLayout);
        mPictureClose = v.findViewById(R.id.pic_gallery_close);
        mFlPicture = v.findViewById(R.id.fl_pic_gallery);
        mPictureClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParentActivity.showMainBottomBar();
                mFlPicture.setVisibility(View.GONE);
                mGalleryRecycler.setVisibility(View.VISIBLE);

            }
        });
        createGuestList(mLinearGuestTab);
        mGalleryRecycler = v.findViewById(R.id.recycler_gallery);
        initRecyclerView(mPictureList);
        mBtnTakePicture = v.findViewById(R.id.bt_take_picture);
        mBtnTakePicture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA},
                            0);
                } else {
                    sendTakePhotoIntent();
                }
            }
        });
        mPictureImage = v.findViewById(R.id.pic_gallery);
        mParentActivity.showMainBottomBar();
        return v;
    }


    private void initRecyclerView(List<GalleryPicture> pictureList) {
        GridLayoutManager mManager = new GridLayoutManager(getActivity(), 2);
        mManager.setOrientation(RecyclerView.HORIZONTAL);
//        LinearLayoutManager horizonalLayoutManager
//                = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        if (pictureList == null)
            return;
        //    mManager = new LinearLayoutManager(mContext);
        // mGalleryRecycler.setHasFixedSize(true);
        mGalleryRecycler.setLayoutManager(mManager);

        mGalleryAdatper = new GalleryAdapter(mContext, pictureList);
        mGalleryRecycler.setAdapter(mGalleryAdatper);
        mGalleryAdatper.notifyDataSetChanged();
    }

    private void createGuestList(LinearLayout container) {

        for (int i = 0; Global.selectedReservation.getGuestData().size() > i; i++) {
            final int idx = i;
            TextView tv = new TextView(new ContextThemeWrapper(getActivity(), R.style.ShadeGuestNameTextView), null, 0);
            final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
            final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            tv.setLayoutParams(param);
            tv.setTag(Global.selectedReservation.getGuestData().get(idx).getId());
            tv.setText(Global.selectedReservation.getGuestData().get(idx).getGuestName());
            //  tv.setBackgroundResource(R.drawable.shape_gray_edge);

            tv.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    //
                }
            });
            container.addView(tv);
        }
    }

    private class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ImageView mImage;
        Context mContext;
        List<GalleryPicture> mPictureList;

        public GalleryAdapter(Context context, List<GalleryPicture> pictureList) {
            mContext = context;
            mPictureList = pictureList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            //  RecyclerView.ViewHolder viewHolder = null;
            view = LayoutInflater.from(mContext).inflate(R.layout.gallery_picture_item, parent, false);
            return new GalleryItemViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            GalleryItemViewHolder viewHolder = (GalleryItemViewHolder) holder;
            viewHolder.imageView.setTag(position);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GalleryViewFragment galleryViewFragment = new GalleryViewFragment(mPictureList, (int) v.getTag());
                    assert getFragmentManager() != null;
                    galleryViewFragment.show(getFragmentManager(), TAG);
                    systemUIHide();

                }
            });
            setClubImage(viewHolder.imageView, mPictureList.get(position).uri);
        }

        @Override
        public int getItemCount() {
            return mPictureList.size();
        }

        public class GalleryItemViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public GalleryItemViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.img_galllery);
            }
        }

        private void setClubImage(ImageView img, String url) {

            if (img != null) {

                Glide.with(mContext)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_noimage)
                        .into(img);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
            Bitmap bitmap1 = rotate(bitmap, exifDegree);

            mPictureList.add(0, new GalleryPicture("567", "1234", imageFilePath, ""));
            mGalleryAdatper.notifyDataSetChanged();
            //   mPictureImage.setImageBitmap(bitmap);

//            CaddieViewGuestItem caddieViewGuestItem = caddieViewGuestItemArrayList.get(traversalByGuestId());
//            ImageView clubImageView = caddieViewGuestItem.findViewById(R.id.clubImageView);
//
//            Bitmap clubImageBitmap = rotate(bitmap, exifDegree);
//            clubBitmapArrayList.set(traversalByGuestId(), clubImageBitmap);
//            clubImageView.setImageBitmap(clubImageBitmap);

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {

            ;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static File getResizedFile(Context context, Bitmap bitmap, String filename) {

        File f = new File(context.getCacheDir(), filename + ".jpg");

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10 /*ignored for PNG*/, bos);
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
    public void onResume() {
        super.onResume();
        mParentActivity.showMainBottomBar();
    }
}
