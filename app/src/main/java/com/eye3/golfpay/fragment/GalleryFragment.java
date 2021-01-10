package com.eye3.golfpay.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.R;
import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.databinding.FrGallleryBinding;
import com.eye3.golfpay.model.gallery.GalleryPicture;
import com.eye3.golfpay.model.gallery.ResponseGallery;
import com.eye3.golfpay.model.photo.PhotoResponse;
import com.eye3.golfpay.net.DataInterface;
import com.eye3.golfpay.net.ResponseData;

import java.util.List;

public class GalleryFragment extends BaseFragment {
    private FrGallleryBinding binding;
    GalleryAdapter galleryAdapter;

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
        binding.btnClubPhoto.setOnClickListener(onClickListener);
        binding.btnPhoto.setOnClickListener(onClickListener);
        binding.btnEventPhoto.setOnClickListener(onClickListener);

        requestPhotos();
        return binding.getRoot();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            binding.btnAll.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            binding.btnClubPhoto.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            binding.btnPhoto.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);
            binding.btnPhoto.setTextAppearance(R.style.GlobalTextView_18SP_Gray_NotoSans_Medium);

            switch (view.getId()) {
                case R.id.btn_all :
                    binding.btnAll.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    break;
                case R.id.btn_club_photo :
                    binding.btnClubPhoto.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    break;
                case R.id.btn_photo :
                    binding.btnPhoto.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    break;
                case R.id.btn_event_photo :
                    binding.btnEventPhoto.setTextAppearance(R.style.GlobalTextView_18SP_ebonyBlack_NotoSans_Bold);
                    break;
            }
        }
    };

    private void initRecyclerView(List<GalleryPicture> pictureList) {
        GridLayoutManager mManager = new GridLayoutManager(getActivity(), 2);
        mManager.setOrientation(RecyclerView.HORIZONTAL);
        if (pictureList == null)
            return;
        binding.recyclerGallery.setLayoutManager(mManager);
        galleryAdapter = new GalleryAdapter(mContext, pictureList);
        binding.recyclerGallery.setAdapter(galleryAdapter);
        galleryAdapter.notifyDataSetChanged();
    }

    private void requestPhotos() {
        DataInterface.getInstance(Global.HOST_ADDRESS_AWS).getCaddyPhotos(getContext(), new DataInterface.ResponseCallback<ResponseData<ResponseGallery>>() {
            @Override
            public void onSuccess(ResponseData<ResponseGallery> response) {
                //initRecyclerView();
            }

            @Override
            public void onError(ResponseData<ResponseGallery> response) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
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

        private void setClubImage(ImageView img, String url) {

            if (img != null) {

                Glide.with(mContext)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_noimage)
                        .into(img);
            }
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
    }
}
