package com.eye3.golfpay.camera;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.eye3.golfpay.util.BitmapUtils;
import com.eye3.golfpay.util.FileUtils;

import java.io.IOException;

public class StoreUriAsTempFileAsyncTask extends AsyncTask<Object, Void, String[]> {

    Context context;
    ICompleteFileSace listener;

    public interface ICompleteFileSace {
        void onComplete(String path);
    }

    public StoreUriAsTempFileAsyncTask(Context context, ICompleteFileSace listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String[] doInBackground(Object... objects) {
        // /temp 경로 파일 모두 삭제.
        try {
            FileUtils.getInstance().deleteAllTempFiles(context);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String[] tmpFileSPath = new String[objects.length];

        for (int i = 0; i < objects.length; i++) {
            Uri mediaUri = (Uri) objects[i];

            // 저장할 파일명 생성.
            String tmpFileName = System.currentTimeMillis() + ".jpg";
            // 저장 경로.
            tmpFileSPath[i] = FileUtils.getInstance().getTempPath(context, tmpFileName);

            try {
                BitmapUtils.resizeFile(context, mediaUri, tmpFileSPath[i], 100);
                // '/temp' 경로에 파일 복사.
                //FileUtils.getInstance().copyFile(context, mediaUri, tmpFileSPath[i]);
                // '/temp' 경로 파일 리스트 로그 출력.
                FileUtils.getInstance().showLogTempFileList(context);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        listener.onComplete(tmpFileSPath[0]);
        return tmpFileSPath;
    }
}
