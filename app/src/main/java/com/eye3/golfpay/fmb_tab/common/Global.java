package com.eye3.golfpay.fmb_tab.common;


import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class Global {

    public static String DEV_SERVER_IP = "";
    public static String DEV_SERVER_PORT = "";
    public static String SavedDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FMB_TAB";
    //==  public static String   photoPath = "/storage/emulated/0/WMMS/pic.jpg"; //위와 동일한 경로
    //내부 저장소 sd카드없어도 저장됨
//    public static String SavedDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/WMMS"  ;  // .getAbsolutePath();
    public static String SavedPictureName = "";
    public static String SavedPicturePath = SavedDir + File.separator + SavedPictureName;

  //  public static UserInfo userInfo = new UserInfo();
  //  public static ArrayList<UploadPhoto> photoList = new ArrayList<>();
    public static String Token = "";
  //  public static String[] searchItem = new String[7];

    /**
     * 운영용으로 컴파일 할시에 true 로 세팅해준다. 호스트 접속페이지 처리.
     */
    //  public static final boolean RealSetting = BuildConfig.IS_REAL;

    /**
     * 접속 호스트 주소
     */
    //  public static String HOST_ADDRESS = (RealSetting ? HOST_ADDRESS_REAL : HOST_ADDRESS_DEV);

    /**
     * 접속 호스트 주소. 운영
     */
    public static final String HOST_ADDRESS_AWS = "http://testerp.golfpay.co.kr/api/v1/";
    /**
     * 접속 호스트 주소. 개발계
     */
    public static final String HOST_ADDRESS_DEV = "http://10.50.21.62:8000/api/v1/";

    public static String appToken;

    public static String imageBaseDir = HOST_ADDRESS_DEV  + "/attachments";

    public interface NotiAlarmChannelID {
        String CHANNEL_LOC = "macaron_loc";
        String CHANNEL_FLOATING = "macaron_floating";
        String CHANNEL_DRIVE = "macaron_drive";
        String CHANNEL_PUSH = "macaron_push";
    }


}
