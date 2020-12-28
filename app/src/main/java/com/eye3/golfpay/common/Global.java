package com.eye3.golfpay.common;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;

import com.eye3.golfpay.fragment.CourseFragment;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.field.Hole;
import com.eye3.golfpay.model.guest.Guest;
import com.eye3.golfpay.model.notice.ArticleItem;
import com.eye3.golfpay.model.teeup.TeeUpTime;
import com.eye3.golfpay.model.teeup.TodayReserveList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Global {


    public static TeeUpTime teeUpTime ;
    public static List<Guest> guestList;
    public static ArrayList<Bitmap> signatureBitmapArrayList = new ArrayList<Bitmap>();
    public static String caddieName ;
    public static String DEV_SERVER_IP = "";
    public static String DEV_SERVER_PORT = "";
    public static String SavedDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GolfPay";
    //==  public static String   photoPath = "/storage/emulated/0/WMMS/pic.jpg"; //위와 동일한 경로
    //내부 저장소 sd카드없어도 저장됨
//    public static String SavedDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/WMMS"  ;  // .getAbsolutePath();
    public static String SavedPictureName = "";
    public static String SavedPicturePath = SavedDir + File.separator + SavedPictureName;

    public static String Token = "";
//    public static int saveIdx = -1;


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
    //public static final String HOST_ADDRESS_AWS = "http://deverp.golfpay.co.kr/api/v1/" ;
    public static final String HOST_BASE_ADDRESS_AWS = "http://testerp.golfpay.co.kr/";
  //  public static final String HOST_BASE_ADDRESS_AWS = "http://deverp.golfpay.co.kr/";
    public static final String HOST_BASE_ADDRESS_STORAGE = HOST_BASE_ADDRESS_AWS ;

    public static final String HOST_ADDRESS_DEV_AWS = "http://deverp.golfpay.co.kr/api/v1/";
    /**
     * 접속 호스트 주소. 개발계
     */
    public static final String HOST_BASE_ADDRESS_DEV = "http://10.50.21.62:8000/";

    public static final String HOST_ADDRESS_DEV = HOST_BASE_ADDRESS_DEV + "api/v1/";

    public static String appToken;

    public static String imageBaseDir = HOST_ADDRESS_DEV + "/attachments";

    public static int selectedTeeUpIndex = -1;
    public static String reserveId = "0";
    public static String CaddyNo = "";
    public static TodayReserveList selectedReservation;

    public interface NotiAlarmChannelID {
        String CHANNEL_LOC = "macaron_loc";
        String CHANNEL_FLOATING = "macaron_floating";
        String CHANNEL_DRIVE = "macaron_drive";
        String CHANNEL_PUSH = "macaron_push";
    }

    public static List<Course> courseInfoList = new ArrayList<Course>();

    //현재 경기진행중인 코스 정보
    public static Course CurrentCourse;
    public static Hole CurrentHole;
    public static List<ArticleItem> noticeItemArrayList = new ArrayList<>();
  //  public static ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
  //  public static ArrayList<RestaurantOrder> restaurantOrderArrayList = new ArrayList<>();

    //    public static Location CurrentLocation ;
    @SuppressLint("StaticFieldLeak")
    public static CourseFragment courseFragment;
    public static int viewPagerPosition = 0;


    public static boolean wifi = false;

    public static boolean gps = false;

    public static boolean isYard = false;

    public static int mBeforeSelectedMenuIdx = -1;


    // 파일 저장 위치
    public class STORAGE_PATH {
        public static final String TEMP = "temp";
    }
}
