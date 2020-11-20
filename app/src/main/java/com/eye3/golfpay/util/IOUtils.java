package com.eye3.golfpay.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Images.ImageColumns;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {


	/**
	 * Close a {@link Closeable} stream without throwing any exception
	 * 
	 * @param c
	 */
	public static void closeSilently( final Closeable c ) {
		if ( c == null ) return;
		try {
			c.close();
		} catch ( final Throwable t ) {}
	}

	public static void closeSilently( final ParcelFileDescriptor c ) {
		if ( c == null ) return;
		try {
			c.close();
		} catch ( final Throwable t ) {}
	}

	public static void closeSilently( Cursor cursor ) {
		if ( cursor == null ) return;
		try {
			if ( cursor != null ) cursor.close();
		} catch ( Throwable t ) {}
	}

	/**
	 * Try to return the absolute file path from the given Uri
	 * 
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath(final Context context, final Uri uri ) {

		if ( null == uri ) return null;

		final String scheme = uri.getScheme();
		String data = null;

		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}
	
	public static byte[] getBytesFromInputStream(InputStream is) throws IOException
	{
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];

        for (int len; (len = is.read(buffer)) != -1;)
            os.write(buffer, 0, len);

        os.flush();

        return os.toByteArray();
	}
	
	public static String writeTempFile(Context context, byte[] b){
			
		String filePath = null;
    	FileOutputStream outstream = null;
        try {
        	File file = File.createTempFile(String.valueOf(System.currentTimeMillis()), ".tmp", context.getCacheDir());
			filePath = file.getAbsolutePath();
			
	        if(file.exists()) file.delete();
        
        	outstream  = new FileOutputStream(file);
            outstream.write(b, 0, b.length);
            outstream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            
        } finally{
        	IOUtils.closeSilently(outstream);
        }
        
        return filePath;
	}
	
}
