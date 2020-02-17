package com.eye3.golfpay.fmb_tab.common;

import android.text.Html;

import com.eye3.golfpay.fmb_tab.model.score.Score;

public class AppDef {
//    public final static int ACTIVITY_CLOSE = 9000;
//    public final static String TAKEOVER_TEST_BEFORE  = "10";
   public static  boolean wifi = false;

   public static boolean gps = false;

   public static boolean isTar = false;

   //스코어 점수를 par로 보여줄지 타수로 보여줄지 결정하는 함수
   /*   Score : 스코어
   //   istar : 설정화면시 타수 스윗치값
    */
   public static String Par_Tar(Score score, boolean istar) {
      if (istar)
         return score.tar;
      else
         return score.par;
   }


   public interface CType{
      String OUT = "OUT";
      String IN = "IN";
   }

   public static int MeterToYard(int meter){
      double yard = meter * 1.0936;

      return (int) yard;
   }


}
