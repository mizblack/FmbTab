package com.eye3.golfpay.fmb_tab.common;

import com.eye3.golfpay.fmb_tab.model.field.Club;
import com.eye3.golfpay.fmb_tab.model.score.Score;

import java.util.ArrayList;
import java.util.List;

public class AppDef {
    //    public final static int ACTIVITY_CLOSE = 9000;
//    public final static String TAKEOVER_TEST_BEFORE  = "10";
    public static boolean wifi = false;

    public static boolean gps = false;

    public static boolean isTar = false;

    //스코어 점수를 par로 보여줄지 타수로 보여줄지 결정하는 함수
   /*   Score : 스코어
   //   istar : 설정화면시 타수 스윗치값
    */
    public static String Par_Tar(Score score, boolean istar) {
        if(score == null)
            return null;
        if (istar)
            return score.tar;
        else
            return score.par;
    }


    public interface CType {
        String OUT = "OUT";
        String IN = "IN";
    }

    public static int MeterToYard(int meter) {
        double yard = meter * 1.0936;

        return (int) yard;
    }

    public interface ScoreType {
        String Par = "par";
        String Tar = "tar";
        String Putt = "putt";
    }

    public static String priceMapper(int price) {
        String priceToString = "" + price;
        if (price >= 1000) {
            int length = priceToString.length();
            String string00 = priceToString.substring(0, priceToString.length() - 3);
            String string01 = priceToString.substring(priceToString.length() - 3, length);
            priceToString = string00 + "," + string01;
        }
        return priceToString;
    }

    public static int  previousCntOfNotice = 0;
    public static boolean  nextMainActivityOnCreate = false;

    public static String imageFilePath = "";
    public static String guestid = "";

    public static final String NEAREST = "니어리스트";
    public static final String LONGEST = "롱기스트";

    public static List<Club> clubList = new ArrayList<>();


}
