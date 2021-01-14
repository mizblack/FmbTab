package com.eye3.golfpay.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.databinding.FrGallleryBinding;
import com.eye3.golfpay.model.gallery.PersnoalPhotoData;
import com.eye3.golfpay.model.gallery.PhotoData;
import com.eye3.golfpay.model.gallery.ResponseGallery;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends BaseFragment {

    public interface IGalleryListener {
        void onShowImages(ArrayList<String> images, int position);
        void onLongClick(View v, int position, int photoId);
        void onEmptyImage();
    }
    enum GalleryType { All, ClubTeamBefore, ClubTeamAfter, Normal, Event  }

    private FrGallleryBinding binding;
    GalleryAdapter galleryAdapter;
    GalleryType galleryType = GalleryType.All;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fr_galllery, container, false);
        binding.btnAll.setOnClickListener(onClickListener);
        binding.btnClubPhotoBefore.setOnClickListener(onClickListener);
        binding.btnClubPhotoAfter.setOnClickListener(onClickListener);
        binding.btnPhoto.setOnClickListener(onClickListener);
        binding.btnEventPhoto.setOnClickListener(onClickListener);
        requestPhotos();
        return binding.getRoot();
    }

    public void update() {
        requestPhotos();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {

            binding.btnAll.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            binding.btnClubPhotoAfter.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            binding.btnClubPhotoBefore.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            binding.btnPhoto.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            binding.btnEventPhoto.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);

            switch (view.getId()) {
                case R.id.btn_all :
                    binding.btnAll.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    galleryType = GalleryType.All;
                    break;
                case R.id.btn_club_photo_before :
                    binding.btnClubPhotoBefore.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    galleryType = GalleryType.ClubTeamBefore;
                    break;
                case R.id.btn_club_photo_after :
                    binding.btnClubPhotoAfter.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    galleryType = GalleryType.ClubTeamAfter;
                    break;
                case R.id.btn_photo :
                    binding.btnPhoto.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    galleryType = GalleryType.Normal;
                    break;
                case R.id.btn_event_photo :
                    binding.btnEventPhoto.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    galleryType = GalleryType.Event;
                    break;
            }
            requestPhotos();
        }
    };

    private void initRecyclerView(RecyclerView rv, TextView tvEmpty, int orientation, List<PhotoData> pictureList, int spanCount) {
        GridLayoutManager mManager = new GridLayoutManager(getActivity(), spanCount);
        int spacing = 0; // 50px

        mManager.setOrientation(orientation);
        if (pictureList == null)
            return;

        tvEmpty.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
        rv.setLayoutManager(mManager);
        rv.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
        galleryAdapter = new GalleryAdapter(mContext, pictureList, galleryType,new IGalleryListener() {
            @Override
            public void onShowImages(ArrayList<String> images, int position) {

                new StfalconImageViewer.Builder<String>(mContext, images, new ImageLoader<String>() {
                    @Override
                    public void loadImage(ImageView imageView, String image) {
                        Glide.with(mContext).load(image).into(imageView);
                    }
                }).withStartPosition(position).show();
            }

            @Override
            public void onLongClick(View v, int position, int photoId) {
                ActionSheet.createBuilder(mContext, getFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("삭제")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                deletePhoto(photoId);
                            }
                        }).show();
            }

            @Override
            public void onEmptyImage() {
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
        rv.setAdapter(galleryAdapter);
        galleryAdapter.notifyDataSetChanged();
    }

    private void deletePhoto(int photoId) {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).deletePhoto(getContext(), photoId, new DataInterface.ResponseCallback<ResponseData<ResponseGallery>>() {
            @Override
            public void onSuccess(ResponseData<ResponseGallery> response) {
                requestPhotos();
            }

            @Override
            public void onError(ResponseData<ResponseGallery> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void requestPhotos() {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getCaddyPhotos(getContext(), new DataInterface.ResponseCallback<ResponseData<ResponseGallery>>() {
            @Override
            public void onSuccess(ResponseData<ResponseGallery> response) {
                switch (galleryType) {
                    case All:
                        List<PhotoData> allPhotos = getAllPhotos(response);
                        binding.scrollView.setVisibility(View.GONE);
                        initRecyclerView(binding.recyclerGallery, binding.tvEmpty, RecyclerView.VERTICAL, allPhotos, 4);
                        break;
                    case ClubTeamBefore:
                    case ClubTeamAfter:
                        initPersonalGallery(response.getData().club_photo_team, response.getData().club_photo_personal);
                        break;
                    case Normal:
                        binding.scrollView.setVisibility(View.GONE);
                        initRecyclerView(binding.recyclerGallery, binding.tvEmpty, RecyclerView.VERTICAL, response.getData().normal_photo, 4);
                        break;
                    case Event:
                        binding.scrollView.setVisibility(View.GONE);
                        initRecyclerView(binding.recyclerGallery, binding.tvEmpty, RecyclerView.VERTICAL, response.getData().event_photo, 4);
                        break;
                }
            }

            @Override
            public void onError(ResponseData<ResponseGallery> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void initPersonalGallery(List<PhotoData> teamList, List<PersnoalPhotoData> pictureList) {
        binding.recyclerGallery.setVisibility(View.GONE);
        binding.scrollView.setVisibility(View.VISIBLE);
        binding.viewPersonalGallery.removeAllViews();

        ////////////////////////////////////////////////////////////////////////////////////////////
        //* 팀 클럽 뷰
        View childView = LayoutInflater.from(mContext).inflate(R.layout.view_personal_galllery, null, false);
        TextView tvName = childView.findViewById(R.id.tv_name);
        tvName.setText("팀 클럽사진");
        RecyclerView rc = childView.findViewById(R.id.recycler_gallery);
        TextView tvEmpty = childView.findViewById(R.id.tv_empty);
        initRecyclerView(rc, tvEmpty, RecyclerView.HORIZONTAL, teamList, 1);
        binding.viewPersonalGallery.addView(childView);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //* 게스트 이미지 뷰
        int guestCount = pictureList.size();
        for (int i = 0; i < guestCount; i++) {
            childView = LayoutInflater.from(mContext).inflate(R.layout.view_personal_galllery, null, false);
            tvName = childView.findViewById(R.id.tv_name);
            tvName.setText(pictureList.get(i).guest_name);
            rc = childView.findViewById(R.id.recycler_gallery);
            tvEmpty = childView.findViewById(R.id.tv_empty);
            initRecyclerView(rc, tvEmpty, RecyclerView.HORIZONTAL, pictureList.get(i).list, 1);
            binding.viewPersonalGallery.addView(childView);
        }
    }

    private List<PhotoData> getAllPhotos(ResponseData<ResponseGallery> response) {
        List<PhotoData> allPhotos = new ArrayList<>();

        if (response.getData().club_photo_personal != null) {
            for (PersnoalPhotoData item : response.getData().club_photo_personal) {
                allPhotos.addAll(item.list);
            }
        }

        if (response.getData().normal_photo != null) {
            allPhotos.addAll(response.getData().normal_photo);
        }

        if (response.getData().club_photo_team != null) {
            allPhotos.addAll(response.getData().club_photo_team);
        }

        if (response.getData().event_photo != null) {
            allPhotos.addAll(response.getData().event_photo);
        }

        return allPhotos;
    }

    static public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@Nullable Rect outRect, @NonNull View view, RecyclerView parent, @Nullable RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                //outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                //outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                //outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                //outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    static private class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        Context mContext;
        List<PhotoData> mPictureList;
        IGalleryListener iGalleryListener;

        public GalleryAdapter(Context context, List<PhotoData> pictureList, GalleryType type, IGalleryListener listener) {
            mContext = context;
            this.iGalleryListener = listener;
            if (type == GalleryType.ClubTeamBefore) {
                mPictureList = new ArrayList<>();
                for (PhotoData data: pictureList) {
                    if (data.photo_type.equals("before"))
                        mPictureList.add(data);
                }
            } else if (type == GalleryType.ClubTeamAfter) {
                mPictureList = new ArrayList<>();
                for (PhotoData data: pictureList) {
                    if (data.photo_type.equals("after"))
                        mPictureList.add(data);
                }
            } else {
                mPictureList = pictureList;
            }

            if (mPictureList.size() == 0) {
                iGalleryListener.onEmptyImage();
            }
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
                    ArrayList<String> arr = new ArrayList<>();
                    for (PhotoData data : mPictureList) {
                        arr.add(Global.HOST_BASE_ADDRESS_AWS + data.photo_url);
                    }
                    iGalleryListener.onShowImages(arr, position);
                }
            });

            viewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    iGalleryListener.onLongClick(v, position, mPictureList.get(position).photo_id);
                    return true;
                }
            });
            setClubImage(viewHolder.imageView, mPictureList.get(position).photo_url);
        }

        private void setClubImage(ImageView img, String url) {

            if (img != null) {

                //url = url.replace("/1", "");
                Glide.with(mContext)
                        .load(Global.HOST_BASE_ADDRESS_AWS + url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_noimage)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                img.setScaleType(ImageView.ScaleType.CENTER);
                                img.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_noimage));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                img.setScaleType(ImageView.ScaleType.CENTER);
                                return false;
                            }
                        })
                        .into(img);
            }
        }

        @Override
        public int getItemCount() {
            return mPictureList.size();
        }

        static public class GalleryItemViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public GalleryItemViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.img_gallery);
            }
        }
    }
}
