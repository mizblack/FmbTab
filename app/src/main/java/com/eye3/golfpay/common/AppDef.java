package com.eye3.golfpay.common;

import com.eye3.golfpay.fragment.OrderFragment;
import com.eye3.golfpay.model.field.Club;
import com.eye3.golfpay.model.order.OrderDetail;
import com.eye3.golfpay.model.order.OrderItemInvoice;
import com.eye3.golfpay.model.order.RestaurantOrder;
import com.eye3.golfpay.model.order.StoreOrder;
import com.eye3.golfpay.model.score.Score;

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

    public interface PlayStatus{
        String Playing = "게임중";
        String Finish = "게임종료";
        String PaymentComplete = "정산완료";
    }

    public static int  previousCntOfNotice = 0;
    public static boolean  nextMainActivityOnCreate = false;

    public static String imageFilePath = "";
    public static String guestid = "";

    public static final String NEAREST = "니어리스트";
    public static final String LONGEST = "롱기스트";

    public static List<Club> clubList = new ArrayList<>();
    public static List<OrderItemInvoice> orderItemInvoiceArrayList = new  ArrayList<>();
    public static List<OrderDetail> orderDetailList = new ArrayList<>();
    public static List<RestaurantOrder> restaurantOrderArrayList = new ArrayList<>();
    public static List<StoreOrder> storeOrderArrayList = new ArrayList<>();

    public static int mTempSaveRestaurantIdx ;

//    public static List<String> getGuestList(){
//        List<String> ArrList = new ArrayList<>();
//        for(int i=0; AppDef.orderDetailList.size() > i ;i++){
//            ArrList.add(getGuestName(AppDef.orderDetailList.get(i).reserve_guest_id));
//        }
//        return ArrList;
//    }

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

}
