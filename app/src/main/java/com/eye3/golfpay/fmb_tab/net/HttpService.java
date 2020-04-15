package com.eye3.golfpay.fmb_tab.net;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.guest.ReserveGuestList;
import com.eye3.golfpay.fmb_tab.model.info.GuestInfoResponse;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.model.notice.NoticeItem;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.order.ShadeOrder;
import com.eye3.golfpay.fmb_tab.model.score.ReserveScore;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface HttpService {

    @Headers("Authorization: abc")

    @FormUrlEncoded
    @POST("caddyLogin")
    Call<Login> doCaddyLogin(@Field("id") String id, @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("getTodayReservesForCaddy")
    Call<TeeUpTime> getTodayReservesForCaddy(@Field("caddy_id") String caddy_id);

    @GET("getAllCourse")
    Call<ResponseData<Course>> getCourseInfo(@Query("cc_id") String ccId);

    @GET("getReserveScore?")
    Call<ResponseData<Player>> getReserveScore(@Query("reserve_id") String reserveId, @Query("type") String type);


    @POST("setReserveScore")
    Call<ResponseData<Object>> sendScore(@Body ReserveScore reserveScore);

    @GET("shadeMenu")
    Call<ResponseData<Restaurant>> getRestaurantMenu(@Query("caddy_id") String caddyId, @Query("reserve_no") String reserveNo);

    @FormUrlEncoded
    @POST("getReserveList")
    Call<ReserveGuestList> getReserveGuestList(@Field("reserve_id") int reserveId);


    @POST("orderShade")
    Call<ResponseData<Object>> sendShadeOrder(@Body ShadeOrder shadeOrder);
    //캐디수첩 저장 api
    @Multipart
    @POST("setReserveGuestInfo")
    Call<GuestInfoResponse> setReserveGuestInfo(@Part("reserve_guest_id") RequestBody reserveGuestId,
                                                @Part("car_no") RequestBody carNo,
                                                @Part("hp") RequestBody hp,
                                                @Part("guest_memo") RequestBody guestMemo,
                                                @Part("team_memo") RequestBody teamMemo,
                                                @Part MultipartBody.Part signImage,
                                                @Part MultipartBody.Part clubImage);


    @POST("getNoticeList")
    Call<ResponseData<NoticeItem>> getNoticeList();
}
