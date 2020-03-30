package com.eye3.golfpay.fmb_tab.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eye3.golfpay.fmb_tab.R;
import com.eye3.golfpay.fmb_tab.model.gallery.GalleryPicture;

import java.util.List;

public class GalleryViewFragment extends DialogFragment {
    ViewPager mGalleryViewPager;
    GalleryPagerAdapter mGalleryPagerAdapter;
    List<GalleryPicture> mGalleryPictureList;
    ImageView mIvLeft, mIvRight;
    int mSelectedId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    public GalleryViewFragment(List<GalleryPicture> galleryPictureList, int selectedId) {
        mGalleryPictureList = galleryPictureList;
        mSelectedId = selectedId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_view_dialog, container, false);

        mGalleryViewPager = view.findViewById(R.id.gallery_view_pager);
        mGalleryPagerAdapter = new GalleryPagerAdapter(getActivity(), mGalleryPictureList);
        mGalleryViewPager.setAdapter(mGalleryPagerAdapter);
        mGalleryViewPager.setCurrentItem(mSelectedId);
        mIvLeft = view.findViewById(R.id.arrow_left);
        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryViewPager.arrowScroll(View.FOCUS_LEFT);
            }
        });

        mIvRight = view.findViewById(R.id.arrow_right);
        mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGalleryViewPager.arrowScroll(View.FOCUS_RIGHT);
            }
        });
        mGalleryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mTvHoleNo.setText(Global.CurrentCourse.holes.get(position).hole_no);
//                mTvCourseName.setText(Global.CurrentCourse.courseName);
//                mTvHolePar.setText(Global.CurrentCourse.holes.get(position).par);
//
//                if (Global.CurrentCourse.holes.get(position).tBox != null) {
//                    tournamentTextView.setText(Global.CurrentCourse.holes.get(position).tBox.get(0).getTboxValue());
//                    regularTextView.setText(Global.CurrentCourse.holes.get(position).tBox.get(1).getTboxValue());
//                    ladiesTextView.setText(Global.CurrentCourse.holes.get(position).tBox.get(2).getTboxValue());
//                    championshipTextView.setText(Global.CurrentCourse.holes.get(position).tBox.get(3).getTboxValue());
//                    frontTextView.setText(Global.CurrentCourse.holes.get(position).tBox.get(4).getTboxValue());
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }


    public class GalleryPagerAdapter extends PagerAdapter {
        Context mContext;
        List<GalleryPicture> mPictureList;
        ImageView mIvMap;

        @SuppressLint("MissingPermission")
        GalleryPagerAdapter(Context context, List<GalleryPicture> pictureList) {
            mContext = context;
            mPictureList = pictureList;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.map_pager_page, container, false);

            mIvMap = view.findViewById(R.id.iv_map);

            if (mPictureList.get(position).url != null) {
                Glide.with(mContext)
                       .load(mPictureList.get(position).url)
                        // .load(Uri.parse(mPictureList.get(position).url))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(mIvMap);
            } else {
                mIvMap.setImageDrawable(getResources().getDrawable(R.drawable.ic_noimage, null));
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            // 뷰페이저에서 삭제.
            container.removeView((View) object);
        }

        @Override
        public int getCount() {

            return mPictureList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == object);
        }

        @Override
        public int getItemPosition(@NonNull Object item) {
            return POSITION_NONE;
        }

    }


}
