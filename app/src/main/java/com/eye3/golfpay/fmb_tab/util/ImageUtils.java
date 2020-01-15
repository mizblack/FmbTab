package com.eye3.golfpay.fmb_tab.util;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Color.WHITE;
import static android.graphics.PorterDuff.Mode.DST_IN;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ImageUtils {
    private static final String TAG = "ImageUtils";

    /**
     * Get a bitmap from the image path
     *
     * @param imagePath
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final String imagePath) {
        return getBitmap(imagePath, 1);
    }

    /**
     * Get a bitmap from the image path
     *
     * @param imagePath
     * @param sampleSize
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final String imagePath, int sampleSize) {
        final Options options = new Options();
        options.inDither = false;
        options.inSampleSize = sampleSize;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imagePath, "r");
            return BitmapFactory.decodeFileDescriptor(file.getFD(), null,
                    options);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage(), e);
                }
        }
    }

    /**
     * Get a bitmap from the image
     *
     * @param image
     * @param sampleSize
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final byte[] image, int sampleSize) {
        final Options options = new Options();
        options.inDither = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }

    /**
     * Get scale for image of size and max height/width
     *
     * @param size
     * @param width
     * @param height
     * @return scale
     */
    public static int getScale(Point size, int width, int height) {
        if (size.x > width || size.y > height)
            return Math.max(Math.round((float) size.y / (float) height),
                    Math.round((float) size.x / (float) width));
        else
            return 1;
    }

    /**
     * Get size of image
     *
     * @param imagePath
     * @return size
     */
    public static Point getSize(final String imagePath) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;

        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(imagePath, "r");
            BitmapFactory.decodeFileDescriptor(file.getFD(), null, options);
            return new Point(options.outWidth, options.outHeight);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage(), e);
            return null;
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage(), e);
                }
        }
    }

    /**
     * Get size of image
     *
     * @param image
     * @return size
     */
    public static Point getSize(final byte[] image) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(image, 0, image.length, options);
        return new Point(options.outWidth, options.outHeight);
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param imagePath
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final String imagePath, int width, int height) {
        Point size = getSize(imagePath);
        return getBitmap(imagePath, getScale(size, width, height));
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param image
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final byte[] image, int width, int height) {
        Point size = getSize(image);
        return getBitmap(image, getScale(size, width, height));
    }

    /**
     * Get bitmap with maximum height or width
     *
     * @param image
     * @param width
     * @param height
     * @return image
     */
    public static Bitmap getBitmap(final File image, int width, int height) {
        return getBitmap(image.getAbsolutePath(), width, height);
    }

    /**
     * Get a bitmap from the image file
     *
     * @param image
     * @return bitmap or null if read fails
     */
    public static Bitmap getBitmap(final File image) {
        return getBitmap(image.getAbsolutePath());
    }

    /**
     * Load a {@link Bitmap} from the given path and set it on the given
     * {@link ImageView}
     *
     * @param imagePath
     * @param view
     */
    public static void setImage(final String imagePath, final ImageView view) {
        setImage(new File(imagePath), view);
    }

    /**
     * Load a {@link Bitmap} from the given {@link File} and set it on the given
     * {@link ImageView}
     *
     * @param image
     * @param view
     */
    public static void setImage(final File image, final ImageView view) {
        Bitmap bitmap = getBitmap(image);
        if (bitmap != null)
            view.setImageBitmap(bitmap);
    }

    /**
     * Round the corners of a {@link Bitmap}
     *
     * @param source
     * @param radius
     * @return rounded corner bitmap
     */
    public static Bitmap roundCorners(final Bitmap source, final float radius) {
        int width = source.getWidth();
        int height = source.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(WHITE);

        Bitmap clipped = Bitmap.createBitmap(width, height, ARGB_8888);
        Canvas canvas = new Canvas(clipped);
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius,
                paint);
        paint.setXfermode(new PorterDuffXfermode(DST_IN));

        Bitmap rounded = Bitmap.createBitmap(width, height, ARGB_8888);
        canvas = new Canvas(rounded);
        canvas.drawBitmap(source, 0, 0, null);
        canvas.drawBitmap(clipped, 0, 0, paint);

        source.recycle();
        clipped.recycle();

        return rounded;
    }

    @Deprecated
    public static File putTextintoPicture(Bitmap srcBitmap, File file) {
//        if (file.exists())
//                file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            //paletBmp이 ""로 넘어옴 오류 해결요.
            Bitmap paletBmp = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            // NEWLY ADDED CODE STARTS HERE [

            Canvas canvas = new Canvas(paletBmp);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE); // Text Color
            paint.setTextSize(16); // Text Size
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
            // some more settings...

            canvas.drawBitmap(paletBmp, 0, 0, paint);
            canvas.drawText("Testing...Testingfdksjfldsjfldjflsjdfljlfjljljfljfslfj", 30, 30, paint);
            // NEWLY ADDED CODE ENDS HERE ]

            paletBmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /*
     * byte[] datas  사진 이미지 데이터 bitmap
     *  View skinView 사진 이미지에 입힐 뷰
     * datas: 원본 이미지데이터
     * skinView : 스킨뷰
    */
    public static Bitmap makeSkinImage(byte[] datas, View skinView) {

        //카메라 사진데이터를 이용해 비트맵이미지를 만든다
        Bitmap cameraimage;
        try {
            cameraimage = BitmapFactory.decodeByteArray(datas, 0, datas.length);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;  //이미지 사이즈 half로 설정됨.

            cameraimage = BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
        } catch (OutOfMemoryError e) {
            //보통 사진데이터가 크기 때문에 메모리오류가 나면 크기를 줄인다
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            cameraimage = BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
        }

        if (cameraimage != null) {
            //이미지나 글자가 포함된 레이아웃의 갭쳐이미지를 불려온다
            skinView.setDrawingCacheEnabled(true);
            Bitmap skinimage = skinView.getDrawingCache();

            //세로모드일때는 카메라 사진을 회전시켜준다(기종에 따라 다를수 있다)
            Matrix matrix = new Matrix();
            matrix.setRotate(90);

            cameraimage = Bitmap.createBitmap(cameraimage, 0, 0, cameraimage.getWidth(), cameraimage.getHeight(), matrix, false);

            //카메라 사진위에 레이아웃 갭쳐이미지를 덮어 그린다
            Canvas canvas = new Canvas(cameraimage);

            canvas.drawBitmap(skinimage, 0, 0, null);
          //  skinimage.recycle();  //안드로이드에서는 recycle을 허용하지 않음.

            return cameraimage;

        }

        return null;
    }

}
