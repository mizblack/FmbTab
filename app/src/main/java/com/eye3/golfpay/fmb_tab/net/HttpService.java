package com.eye3.golfpay.fmb_tab.net;

import com.eye3.golfpay.fmb_tab.model.Device;
import com.eye3.golfpay.fmb_tab.model.Token;
import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.model.score.ReserveScore;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpService {

    //   @GET("url")
//    Call<ResponseData<UserVO>> getSystemCheck();

//    @FormUrlEncoded
//    @POST("/touchcash/srv/user/api/exist/cid")
//    Call<ResponseData<UserVO>> getUserInfo(@FieldMap Map<String, String> params);

//    @POST("/api/common/getAppInfo")
//    Call<ResponseData<AppInfo>> getAppInfo(@Body Map<String, Object> params);
//
//    @POST("/api/chauffeur/getChauffeur")
//    Call<ResponseData<ChauffeurInfo>> getChauffeur(@Body Map<String, Object> params);
//

    //    @POST("/api/auth/logout")
//    Call<ResponseData<Object>> logout(@Body Map<String, Object> params, @Header("Authorization") String auth);
    @FormUrlEncoded
    @POST("/wmms/api/user")
    Call<ResponseData<Token>> login(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("/wmms/api/acceptCheckDeviceMasterSearch")
    Call<ResponseData<Device>> apiAcceptCheckDeviceMasterSearch(@FieldMap Map<String, Object> params);

    @POST("caddyLogin")
    Call<Login> doCaddyLogin(@Query("id") String id, @Query("pwd") String pwd);

    @POST("getTodayReservesForCaddy")
    Call<TeeUpTime> getTodayReservesForCaddy(@Query("caddy_id") String caddy_id);

    @GET("getAllCourse?cc_id=1")
    Call<ResponseData<Course>> getCourseInfo();

    @GET("getReserveScore?")
    Call<ResponseData<Player>> getReserveScore(@Query("reserve_id") String reserveId);

    @POST("setReserveScore")
    Call<Object> sendScore(@Body ReserveScore reserveScore);

}
