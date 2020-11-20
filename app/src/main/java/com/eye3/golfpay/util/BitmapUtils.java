package com.eye3.golfpay.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BitmapUtils {

	private static final int MAX_IMAGE_SIZE = 1024;

	/**
	 * Resize a bitmap
	 *
	 * @param input
	 * @param destWidth
	 * @param destHeight
	 * @return
	 * @throws OutOfMemoryError
	 */
	public static Bitmap resizeBitmap(final Bitmap input, int destWidth, int destHeight ) throws OutOfMemoryError {
		return resizeBitmap( input, destWidth, destHeight, 0 );
	}

	/**
	 * Resize a bitmap object to fit the passed width and height
	 * 
	 * @param input
	 *           The bitmap to be resized
	 * @param destWidth
	 *           Desired maximum width of the result bitmap
	 * @param destHeight
	 *           Desired maximum height of the result bitmap
	 * @return A new resized bitmap
	 * @throws OutOfMemoryError
	 *            if the operation exceeds the available vm memory
	 */
	public static Bitmap resizeBitmap(final Bitmap input, int destWidth, int destHeight, int rotation ) throws OutOfMemoryError {

		int dstWidth = destWidth;
		int dstHeight = destHeight;
		final int srcWidth = input.getWidth();
		final int srcHeight = input.getHeight();

		if ( rotation == 90 || rotation == 270 ) {
			dstWidth = destHeight;
			dstHeight = destWidth;
		}

		boolean needsResize = false;
		float p;
		if ( ( srcWidth > dstWidth ) || ( srcHeight > dstHeight ) ) {
			needsResize = true;
			if ( ( srcWidth > srcHeight ) && ( srcWidth > dstWidth ) ) {
				p = (float) dstWidth / (float) srcWidth;
				dstHeight = (int) ( srcHeight * p );
			} else {
				p = (float) dstHeight / (float) srcHeight;
				dstWidth = (int) ( srcWidth * p );
			}
		} else {
			dstWidth = srcWidth;
			dstHeight = srcHeight;
		}

		if (input != null && !input.isRecycled() && (needsResize || rotation != 0 )) {
			Bitmap output;

			if ( rotation == 0 ) {
				output = Bitmap.createScaledBitmap( input, dstWidth, dstHeight, true );
			} else {
				Matrix matrix = new Matrix();
				matrix.postScale( (float) dstWidth / srcWidth, (float) dstHeight / srcHeight );
				matrix.postRotate( rotation );
				output = Bitmap.createBitmap( input, 0, 0, srcWidth, srcHeight, matrix, true );
			}
			return output;
		} else
			return input;
	}

	public static Bitmap copy(Bitmap paramBitmap, Bitmap.Config paramConfig)
	{
	    int i = paramBitmap.getWidth();
	    int j = paramBitmap.getHeight();
	    if (paramConfig == null)
	      paramConfig = Bitmap.Config.ARGB_8888;
	    Bitmap localBitmap = Bitmap.createBitmap(i, j, paramConfig);
	    new Canvas(localBitmap).drawBitmap(paramBitmap, new Matrix(), null);
	    return localBitmap;
	}

	public static void resizeFile(Context context, Uri srcUri, String destPath) throws IOException {
		OutputStream output = null;
		try {
			output = new FileOutputStream(destPath);
			Bitmap bitmap = DecodeUtils.decode(context, srcUri, MAX_IMAGE_SIZE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output);
			output.flush();
		} finally {
			output.close();
		}
	}
	public static String getCopyFilePath(Context context, Uri srcUri, String defaultExtension) {
		String type = getMimeType(context, srcUri);
		if (type == null) type = defaultExtension;

		String name = getUUID();
		return getStoragePath(context) + name + "." + type;
	}

	public static String getMimeType(Context context, Uri uri) {
		String extension;

		if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
			final MimeTypeMap mime = MimeTypeMap.getSingleton();
			extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
		} else {
			extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
		}

		return extension;
	}

	/**
	 * 내부 파일 저장경로
	 */
	public static String getStoragePath(Context context) {
		String path = context.getFilesDir().getPath() + File.separator;

		File file = new File(path);

		if (!file.exists()) {
			file.mkdir();
		}

		return path;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
