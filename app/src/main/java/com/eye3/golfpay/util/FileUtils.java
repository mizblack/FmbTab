package com.eye3.golfpay.util;

import android.content.Context;
import android.net.Uri;

import com.eye3.golfpay.common.Global;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by st_park on 2017-05-15.
 */

public class FileUtils {


    private static FileUtils fileUtils;

    public static FileUtils getInstance() {
        if (fileUtils == null) {
            fileUtils = new FileUtils();
        }
        return fileUtils;
    }

    public void copyFile(Context context, Uri srcUri, String destPath) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = DecodeUtils.openInputStream(context, srcUri);
            output = new FileOutputStream(destPath);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }


    public void copyFileUsingStream(String source, String dest) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            String parentPath = new File(dest).getParent();
            File parentDirectory = new File(parentPath);
            if (!parentDirectory.exists()) {
                if (!parentDirectory.mkdirs()) {
                    return;
                }
            }

            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) > 0) {
                output.write(buffer, 0, bytesRead);
            }
        } finally {
            if (input != null) input.close();
            if (output != null) output.close();
        }
    }

    public String getTempPath(Context context, String fileName) {
        return getStoragePath(context, Global.STORAGE_PATH.TEMP) + fileName;
    }

    /**
     * '/temp' 경로 파일 리스트 로그 출력.
     * @param context
     */
    public void showLogTempFileList(Context context) {
        // '/temp' 경로 파일 리스트
        File tempPath = new File(FileUtils.getInstance().getStoragePath(context, Global.STORAGE_PATH.TEMP));
        if (tempPath.isDirectory()) {
            File fileList[] = tempPath.listFiles();
            for (File file : fileList) {

            }
        }
    }

    /**
     * '/temp' 경로의 파일 모두 삭제.
     * @param context
     * @throws IOException
     */
    public void deleteAllTempFiles(Context context) throws IOException {
        File tempPath = new File(getStoragePath(context, Global.STORAGE_PATH.TEMP));

        if (tempPath.exists() && tempPath.isDirectory()) {
            File[] files = tempPath.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    /**
     * 내부 파일 저장경로
     */
    public String getStoragePath(Context context) {
        String path = context.getFilesDir().getPath() + File.separator;

        File file = new File(path);

        if (!file.exists()) {
            file.mkdir();
        }

        return path;
    }

    /**
     * 내부 파일 저장 경로
     *
     * @param storage_path : 추가 경로
     */
    public String getStoragePath(Context context, String storage_path) {

        String path = getStoragePath(context) + storage_path + File.separator;

        File file = new File(path);

        if (!file.exists()) {
            file.mkdir();
        }

        return path;
    }
}
