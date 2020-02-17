package com.eye3.golfpay.fmb_tab.net;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.guest.ReserveGuestList;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.score.ReserveScore;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpService {

    @Headers("Authorization: abc")

    @POST("caddyLogin")
    Call<Login> doCaddyLogin(@Query("id") String id, @Query("pwd") String pwd);

    @POST("getTodayReservesForCaddy")
    Call<TeeUpTime> getTodayReservesForCaddy(@Query("caddy_id") String caddy_id);

    @GET("getAllCourse?cc_id=1")
    Call<ResponseData<Course>> getCourseInfo();

    @GET("getReserveScore?")
    Call<ResponseData<Player>> getReserveScore(@Query("reserve_id") String reserveId, @Query("type") String type);

    @POST("setReserveScore")
    Call<ResponseData<Object>> sendScore(@Body ReserveScore reserveScore);

    @GET("shadeMenu")
    Call<ResponseData<Restaurant>> getRestaurantMenu(@Query("caddy_id") String caddyId, @Query("reserve_no") String reserveNo);

    @POST("getReserveList")
    Call<ReserveGuestList> getReserveGuestList(@Query("reserve_id") int reserveId);

}
