package com.eye3.golfpay.net;

import com.eye3.golfpay.model.caddyNote.ResponseCaddyNote;
import com.eye3.golfpay.model.chat.ResponseChatMsg;
import com.eye3.golfpay.model.control.ChatHotKey;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.gallery.ResponseGallery;
import com.eye3.golfpay.model.gps.GpsInfo;
import com.eye3.golfpay.model.gps.ResponseCartInfo;
import com.eye3.golfpay.model.guest.ReserveGuestList;
import com.eye3.golfpay.model.info.GuestInfoResponse;
import com.eye3.golfpay.model.login.Login;
import com.eye3.golfpay.model.notice.ArticleItem;
import com.eye3.golfpay.model.order.CancelOrder;
import com.eye3.golfpay.model.order.PlayStatus;
import com.eye3.golfpay.model.order.ReserveGameType;
import com.eye3.golfpay.model.order.Restaurant;
import com.eye3.golfpay.model.order.RestaurantMenu;
import com.eye3.golfpay.model.order.ShadeOrder;
import com.eye3.golfpay.model.order.StoreOrder;
import com.eye3.golfpay.model.photo.PhotoResponse;
import com.eye3.golfpay.model.score.NearLongScoreBoard;
import com.eye3.golfpay.model.score.ReserveScore;
import com.eye3.golfpay.model.teeup.Player;
import com.eye3.golfpay.model.teeup.TeeUpTime;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpService {

    @Headers("Authorization: {access_token}")

    @FormUrlEncoded
    @POST("caddyLogin")
    Call<Login> doCaddyLogin(@Field("id") String id, @Field("pwd") String pwd);

    @FormUrlEncoded
    @POST("getTodayReservesForCaddy")
    Call<TeeUpTime> getTodayReservesForCaddy(@Field("caddy_id") String caddy_id);

    @GET("getAllCourse")
    Call<ResponseData<Course>> getCourseInfo(@Query("cc_id") String ccId, @Query("reserve_id") int reserveId);

    @GET("getReserveScore?")
    Call<ResponseData<Player>> getReserveScore(@Query("reserve_id") String reserveId, @Query("type") String type);

    @POST("setReserveScore")
    Call<ResponseData<Object>> sendScore(@Body ReserveScore reserveScore);

    @GET("shadeMenu")
    Call<ResponseData<Restaurant>> getRestaurantMenu(@Query("caddy_id") String caddyId, @Query("reserve_no") String reserveNo);

    @GET("storeCategory")
    Call<ResponseData<Restaurant>> storeCategory();

    @GET("storeMenu/{restaurant_id}/{category_id}")
    Call<ResponseData<RestaurantMenu>> storeMenu(@Path("restaurant_id") String restaurantId, @Path("category_id") String categoryId);

    @GET("orderShadeHistory")
    Call<ResponseData<StoreOrder>> getStoreOrder(@Query("reserve_no") String reserveNo);


    @POST("cancelOrderShade")
    Call<ResponseData<Object>> cancelOrderShade(@Body CancelOrder cancelOrder);

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


    @FormUrlEncoded
    @POST("getCaddyBoard")
    Call<ResponseData<ArticleItem>> getCaddyBoard(@Field("caddy_id") String caddy_id, @Field("table_name") String table_name);

    @FormUrlEncoded
    @POST("caddyBoardCheck")
    Call<ResponseData<Object>> caddyBoardCheck(@Field("caddy_id") String caddy_id, @Field("article_id") int article_id);

    @POST("setPlayStatus")
    Call<ResponseData<Object>> setPlayStatus(@Body PlayStatus shadeOrder);

    @GET("setCartPos")
    Call<ResponseData<ResponseCartInfo>> sendGpsInfo(@Query("caddy_id") String caddy_num,
                                                     @Query("lat") double lat,
                                                     @Query("lng") double lng,
                                                     @Query("reserve_id") String reserve_id);

    @GET("http://deverp.golfpay.co.kr/api/v1/getChatHotkey")
    Call<ChatHotKey> getChatHotkey();

    @GET("http://deverp.golfpay.co.kr/api/v1/sendmsg")
    Call<ResponseChatMsg> sendChatMessage(@Query("sender") String sender,
                                          @Query("sender_type") String sender_type,
                                          @Query("msg") String msg,
                                          @Query("receiver_type") String receiver_type);

    @FormUrlEncoded
    @POST("getGameTypeScore")
    Call<NearLongScoreBoard> getGameTypeScore(@Field("res_id") int res_id);

    @FormUrlEncoded
    @POST("setGameTypeScore")
    Call<ResponseData<Object>> setGameTypeScore(@Field("res_id") int res_id,
                                              @Field("guest_id") int guest_id,
                                              @Field("game_type") String game_type,
                                              @Field("distance") String distance);

    @POST("setReserveGameType")
    Call<ResponseData<Object>> setReserveGameType(@Body ReserveGameType request);

    @FormUrlEncoded
    @POST("getReserveGameType")
    Call<ReserveGameType> getReserveGameType(@Field("res_id") int res_id);

    @Multipart
    @POST("setGuestPhotos")
    Call<PhotoResponse> setGuestPhotos(@Part("reserve_id") RequestBody reserve_id,
                                        @Part("reserve_guest_id") RequestBody reserveGuestId,
                                        @Part("photo_type") RequestBody photo_type,
                                        @Part("photo_time") RequestBody photo_time,
                                        @Part("caddy_id") RequestBody caddy_id,
                                        @Part MultipartBody.Part img_file);

    @FormUrlEncoded
    @POST("getCaddyNotebook")
    Call<ResponseCaddyNote> getCaddyNote(@Field("caddy_id") String caddy_id,
                                         @Field("reserve_id") String reserve_id);

    @FormUrlEncoded
    @POST("setClubInfo")
    Call<ResponseData<Object>> setClubInfo(@Field("reserve_guest_id") String reserve_guest_id,
                                      @Field("wood") String wood,
                                      @Field("utility") String utility,
                                      @Field("iron") String iron,
                                      @Field("wedge") String wedge,
                                      @Field("putter") String putter,
                                      @Field("wood_cover") String wood_cover,
                                      @Field("putter_cover") String putter_cover,
                                      @Field("etc_cover") String etc_cover);

    @FormUrlEncoded
    @POST("setPersonalInfo")
    Call<ResponseData<Object>> setPersonalInfo(@Field("reserve_id") String reserve_id,
                                               @Field("guest_id") String guest_id,
                                               @Field("carNumber") String carNumber,
                                               @Field("phoneNumber") String phoneNumber,
                                               @Field("memo") String memo);

    @FormUrlEncoded
    @POST("setTeamMemo")
    Call<ResponseData<Object>> setTeamMemo(@Field("reserve_id") String reserve_id, @Field("team_memo") String team_memo);

    @FormUrlEncoded
    @POST("getCaddyPhotos")
    Call<ResponseData<ResponseGallery>> getCaddyPhotos(@Field("reserve_id") String reserve_id);

    @FormUrlEncoded
    @POST("delguestPhotos")
    Call<ResponseData<Object>> delGuestPhotos(@Field("photo_id") int photo_id);
}
