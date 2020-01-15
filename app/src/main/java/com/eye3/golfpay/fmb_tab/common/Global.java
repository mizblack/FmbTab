package com.eye3.golfpay.fmb_tab.common;


import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class Global {

    public static String DEV_SERVER_IP = "";
    public static String DEV_SERVER_PORT = "";
    public static String SavedDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WMMS";
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
    public static final String HOST_ADDRESS_REAL = "https://";
    /**
     * 접속 호스트 주소. 개발계
     */
    public static final String HOST_ADDRESS_DEV = "http://49.247.3.140:8080";   //cloud
  //  public static final String HOST_ADDRESS_DEV = "http://10.10.169.151:8080";
    //   public static final String HOST_ADDRESS_DEV     = "http://10.10.169.248:8080"; //mr Bae
    //  public static final String HOST_ADDRESS_DEV = "http://210.116.118.194:8080"; //unovous server

    public static String appToken;

    public static final String ru = "RU";
    public static final String du = "DU";
    public static String imageBaseDir = HOST_ADDRESS_DEV  + "/attachments";
//    public static String[] searchItem = new String[7];
    public interface NotiAlarmChannelID {
        String CHANNEL_LOC = "macaron_loc";
        String CHANNEL_FLOATING = "macaron_floating";
        String CHANNEL_DRIVE = "macaron_drive";
        String CHANNEL_PUSH = "macaron_push";
    }

    public interface AppVersionCheck {
        String CHAUFFEUR_SYSTEM = "CHAUFFEUR_SYSTEM";
        String CHAUFFEUR_VERSION = "CHAUFFEUR_VERSION";
        String VERSION = "VERSION";
        String MESSAGE = "MESSAGE";
        String ACTION = "ACTION";
    }

    public interface ErrorCode {
        String EC902 = "EC902";  // 운행중일때 고객이 예약취소시
        String EC105 = "EC105";  // 다른사람이 해당 차량 사용시
    }

}
