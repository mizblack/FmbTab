package com.eye3.golfpay.net;

import android.content.Context;
import android.view.View;

import com.eye3.golfpay.common.Global;
import com.eye3.golfpay.model.caddyNote.ResponseCaddyNote;
import com.eye3.golfpay.model.caddyNote.SendSMS;
import com.eye3.golfpay.model.chat.ChatData;
import com.eye3.golfpay.model.chat.ResponseChatMsg;
import com.eye3.golfpay.model.control.ChatHotKey;
import com.eye3.golfpay.model.field.Course;
import com.eye3.golfpay.model.gallery.ResponseGallery;
import com.eye3.golfpay.model.gps.CType;
import com.eye3.golfpay.model.gps.GpsInfo;
import com.eye3.golfpay.model.gps.ResCheckChangeCourse;
import com.eye3.golfpay.model.gps.ResponseCartInfo;
import com.eye3.golfpay.model.guest.ReqClubInfo;
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
import com.eye3.golfpay.util.FmbCustomDialog;

import java.net.URL;

import io.socket.client.Url;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataInterface extends BasicDataInterface {
    private static DataInterface instance;
    protected String TAG = getClass().getSimpleName();
    private FmbCustomDialog dialog;

    public interface ResponseCallback<T> {
        void onSuccess(T response);

        void onError(T response);

        void onFailure(Throwable t);
    }

    public static DataInterface getInstance() {

        if (instance == null) {
            synchronized (DataInterface.class) {
                if (instance == null) {
                    instance = new DataInterface();
                }
            }
        }

        return instance;
    }

    public static DataInterface getInstance(String url) {
        synchronized (DataInterface.class) {
            instance = new DataInterface(url);
        }

        return instance;
    }

    private DataInterface() {
        super();
    }

    private DataInterface(String url) {
        super(url);
    }

    static boolean isCallSuccess(Response response) {
        return response.isSuccessful();
    }

    //ResponseData 를 사용하지 않을때 필요없음.
    private void solveCommonError(Context context, ResponseCallback callback, Response response, boolean isCommonError) {
        if (callback == null) {
            return;
        }

        ResponseData data = (ResponseData) response.body();

        if (response.isSuccessful()) {
            if (data != null) {
                if (isCommonError && !data.getResultCode().equals("ok")) {
                    showDialog(context, null, data.getError());
                }
                callback.onSuccess(data);
            } else {
                callback.onError(null);
            }
        } else {
            if (isCommonError) {
                if (data != null) {
                    showDialog(context, null, data.getError());
                } else {
                  //  showDialog(context, null, "네트웍상태를 확인해주세요. " + "http code: " + response.code());
                }
            } else {
                callback.onError(null);
            }
        }
    }

    private void showDialog(Context context, String title, String msg) {
        dialog = new FmbCustomDialog(context, title, msg, "확인", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTodayReservesForCaddy(String caddy_id, final ResponseCallback<TeeUpTime> callback) {
        try {
            Call<TeeUpTime> call = service.getTodayReservesForCaddy(caddy_id);
            call.enqueue(new Callback<TeeUpTime>() {
                @Override
                public void onResponse(Call<TeeUpTime> call, Response<TeeUpTime> response) {
                    TeeUpTime teeUpTime = response.body();
                    callback.onSuccess(teeUpTime);
                }

                @Override
                public void onFailure(Call<TeeUpTime> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void login(String id, String pwd, final ResponseCallback<Login> callback) {
        try {
            Call<Login> call = service.doCaddyLogin(id, pwd);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    Login login = response.body();
                    callback.onSuccess(login);
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCourseInfo(final Context context, String cc_id, final ResponseCallback<ResponseData<Course>> callback) {
        try {
            int reserve_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
            //int reserve_id = 9430;
            Call<ResponseData<Course>> call = service.getCourseInfo(cc_id, reserve_id);
            call.enqueue(new Callback<ResponseData<Course>>() {
                @Override
                public void onResponse(Call<ResponseData<Course>> call, Response<ResponseData<Course>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Course>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                     callback.onFailure(t);
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getReserveScore(final Context context, String reserve_id, String type, final ResponseCallback<ResponseData<Player>> callback) {
        try {
            Call<ResponseData<Player>> call = service.getReserveScore(reserve_id, type);
            call.enqueue(new Callback<ResponseData<Player>>() {
                @Override
                public void onResponse(Call<ResponseData<Player>> call, Response<ResponseData<Player>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Player>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
               //     showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getRestaurantMenu(final Context context, final ResponseCallback<ResponseData<Restaurant>> callback) {
        try {
            Call<ResponseData<Restaurant>> call = service.storeCategory();
            call.enqueue(new Callback<ResponseData<Restaurant>>() {
                @Override
                public void onResponse(Call<ResponseData<Restaurant>> call, Response<ResponseData<Restaurant>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Restaurant>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getStoreMenu(final Context context, String restaurantId, String categoryId, final ResponseCallback<ResponseData<RestaurantMenu>> callback) {
        try {
            Call<ResponseData<RestaurantMenu>> call = service.storeMenu(restaurantId, categoryId);
            call.enqueue(new Callback<ResponseData<RestaurantMenu>>() {
                @Override
                public void onResponse(Call<ResponseData<RestaurantMenu>> call, Response<ResponseData<RestaurantMenu>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<RestaurantMenu>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getStoreOrder(final Context context, String reserve_no, final ResponseCallback<ResponseData<StoreOrder>> callback) {
        try {

            Call<ResponseData<StoreOrder>> call = service.getStoreOrder( reserve_no);
            call.enqueue(new Callback<ResponseData<StoreOrder>>() {
                @Override
                public void onResponse(Call<ResponseData<StoreOrder>> call, Response<ResponseData<StoreOrder>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<StoreOrder>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cancelOrder(final Context context, CancelOrder cancelOrder, final ResponseCallback<ResponseData<StoreOrder>> callback) {

        try {
            Call<ResponseData<Object>> call = service.cancelOrderShade( cancelOrder);
            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setScore(final Context context, ReserveScore reserveScore, final ResponseCallback<ResponseData<Object>> callback) {
        try {
            Call<ResponseData<Object>> call = service.sendScore(reserveScore);
            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getReserveGuestList(int reserveId, final ResponseCallback<ReserveGuestList> callback) {
        try {
            Call<ReserveGuestList> call = service.getReserveGuestList(reserveId);
            call.enqueue(new Callback<ReserveGuestList>() {
                @Override
                public void onResponse(Call<ReserveGuestList> call, Response<ReserveGuestList> response) {
                    ReserveGuestList reserveGuestList = response.body();
                    callback.onSuccess(reserveGuestList);
                }

                @Override
                public void onFailure(Call<ReserveGuestList> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendGpsInfo(final Context context, String caddy_num, double lat, double lng, String reserve_id, final ResponseCallback<ResponseData<ResponseCartInfo>> callback) {
        try {

            Call<ResponseData<ResponseCartInfo>> call = service.sendGpsInfo(caddy_num, lat, lng, reserve_id);
            call.enqueue(new Callback<ResponseData<ResponseCartInfo>>() {
                @Override
                public void onResponse(Call<ResponseData<ResponseCartInfo>> call, Response<ResponseData<ResponseCartInfo>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<ResponseCartInfo>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public void getNoticeList(final Context context, final ResponseCallback<ResponseData<ArticleItem>> callback) {
        try {
            Call<ResponseData<ArticleItem>> call = service.getCaddyBoard(Global.CaddyNo, "caddy");
            call.enqueue(new Callback<ResponseData<ArticleItem>>() {

                @Override
                public void onResponse(Call<ResponseData<ArticleItem>> call, Response<ResponseData<ArticleItem>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<ArticleItem>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void caddyBoardCheck(final Context context, int articleId,final ResponseCallback<ResponseData<ArticleItem>> callback) {
        try {
            Call<ResponseData<Object>> call = service.caddyBoardCheck(Global.CaddyNo, articleId);
            call.enqueue(new Callback<ResponseData<Object>>() {

                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public void setReserveGuestInfo(RequestBody reserveGuestId, RequestBody carNo, RequestBody hp, RequestBody guestMemo, RequestBody teamMemo, MultipartBody.Part signImage, MultipartBody.Part clubImage, final ResponseCallback<GuestInfoResponse> callback) {
        try {
            Call<GuestInfoResponse> call = service.setReserveGuestInfo(reserveGuestId, carNo, hp, guestMemo, teamMemo, signImage, clubImage);
            call.enqueue(new Callback<GuestInfoResponse>() {
                @Override
                public void onResponse(Call<GuestInfoResponse> call, Response<GuestInfoResponse> response) {
                    callback.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<GuestInfoResponse> call, Throwable t) {

                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendShadeOrders(final Context context, ShadeOrder shadeOrder, final ResponseCallback<ResponseData<Object>> callback) {
        try {
            Call<ResponseData<Object>> call = service.sendShadeOrder(shadeOrder);
            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPlayStatus(final Context context, int reserve_id, String play_status, final ResponseCallback<ResponseData<Object>> callback) {
        try {

            Call<ResponseData<Object>> call = service.setPlayStatus(new PlayStatus(reserve_id, play_status));
            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    callback.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void requestHotKeyList(final ResponseCallback<ChatHotKey> callback) {
        try {
            Call<ChatHotKey> call = service.getChatHotkey();
            call.enqueue(new Callback<ChatHotKey>() {
                @Override
                public void onResponse(Call<ChatHotKey> call, Response<ChatHotKey> response) {
                    ChatHotKey reserveGuestList = response.body();
                    callback.onSuccess(reserveGuestList);
                }

                @Override
                public void onFailure(Call<ChatHotKey> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendChatMessage(ChatData chatData, final ResponseCallback<ResponseChatMsg> callback) {
        try {
            Call<ResponseChatMsg> call = service.sendChatMessage(chatData.type, chatData.sender_id, chatData.sender_name, chatData.receiver_id,
                    chatData.receiver_name, chatData.group_id, chatData.group_name, chatData.course_id, chatData.course_name, chatData.title, chatData.message, chatData.emergency);
            call.enqueue(new Callback<ResponseChatMsg>() {
                @Override
                public void onResponse(Call<ResponseChatMsg> call, Response<ResponseChatMsg> response) {
                    ResponseChatMsg reserveGuestList = response.body();
                    callback.onSuccess(reserveGuestList);
                }

                @Override
                public void onFailure(Call<ResponseChatMsg> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getGameTypeScore(final Context context, final ResponseCallback<NearLongScoreBoard> callback) {
        try {
            int reserve_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
            //int reserve_id = 9430;

            Call<NearLongScoreBoard> call = service.getGameTypeScore(reserve_id);
            call.enqueue(new Callback<NearLongScoreBoard>() {
                @Override
                public void onResponse(Call<NearLongScoreBoard> call, Response<NearLongScoreBoard> response) {
                    NearLongScoreBoard nearLongScoreBoard = response.body();
                    callback.onSuccess(nearLongScoreBoard);
                }

                @Override
                public void onFailure(Call<NearLongScoreBoard> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setGameTypeScore(final Context context, int guest_id, String game_type, String distance, final ResponseCallback<ResponseData<Object>> callback) {
        try {
            int res_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
            //int reserve_id = 9430;

            Call<ResponseData<Object>> call = service.setGameTypeScore(res_id, guest_id, game_type, distance);
            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    ResponseData<Object> nearLongScoreBoard = response.body();
                    callback.onSuccess(nearLongScoreBoard);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setReserveGameType(final Context context, ReserveGameType request, final ResponseCallback<ResponseData<Object>> callback) {
        try {
            Call<ResponseData<Object>> call = service.setReserveGameType(request);
            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    ResponseData<Object> res = response.body();
                    callback.onSuccess(res);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getReserveGameType(final Context context, final ResponseCallback<ReserveGameType> callback) {
        try {

            int res_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
            Call<ReserveGameType> call = service.getReserveGameType(res_id);

            call.enqueue(new RetryableCallback<ReserveGameType>(call, context) {
                @Override
                public void onResponse(Call<ReserveGameType> call, Response<ReserveGameType> response) {
                    ReserveGameType res = response.body();
                    callback.onSuccess(res);
                }

                @Override
                public void onFailure(Call<ReserveGameType> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setGuestPhotos(RequestBody reserveId, RequestBody reserveGuestId,
                               RequestBody photo_type, RequestBody photo_time, RequestBody caddy_id, MultipartBody.Part part, final ResponseCallback<PhotoResponse> callback) {

        try {
            Call<PhotoResponse> call = service.setGuestPhotos(reserveId, reserveGuestId, photo_type, photo_time, caddy_id, part);
            call.enqueue(new Callback<PhotoResponse>() {
                @Override
                public void onResponse(Call<PhotoResponse> call, Response<PhotoResponse> response) {
                    callback.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<PhotoResponse> call, Throwable t) {

                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCaddyNoteInfo(Context context, String caddy_id, String reserve_id, final ResponseCallback<ResponseCaddyNote> callback) {

        try {
            Call<ResponseCaddyNote> call = service.getCaddyNote(caddy_id, reserve_id);

            call.enqueue(new Callback<ResponseCaddyNote>() {
                @Override
                public void onResponse(Call<ResponseCaddyNote> call, Response<ResponseCaddyNote> response) {
                    callback.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<ResponseCaddyNote> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setClubInfo(Context context, String reserve_guest_id, ReqClubInfo reqClubInfo, final ResponseCallback<ResponseData<Object>> callback) {

        try {

            Call<ResponseData<Object>> call = service.setClubInfo( reserve_guest_id,
                    reqClubInfo.wood,
                    reqClubInfo.putter,
                    reqClubInfo.wedge,
                    reqClubInfo.iron,
                    reqClubInfo.utility,
                    reqClubInfo.wood_cover, reqClubInfo.putter_cover, reqClubInfo.wedge_cover, reqClubInfo.iron_cover, reqClubInfo.utility_cover);

            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPersonalInfo(Context context, String reserve_no, String guestId, String carNumber,
                            String phoneNumber, String memo, final ResponseCallback<ResponseData<Object>> callback) {

        try {
            Call<ResponseData<Object>> call = service.setPersonalInfo( reserve_no, guestId, carNumber, phoneNumber, memo);

            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTeamMemo(Context context, String reserve_no, String memo, final ResponseCallback<ResponseData<Object>> callback) {

        try {
            Call<ResponseData<Object>> call = service.setTeamMemo( reserve_no, memo);

            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCaddyPhotos(Context context, final ResponseCallback<ResponseData<ResponseGallery>> callback) {

        try {
            Call<ResponseData<ResponseGallery>> call = service.getCaddyPhotos(Global.reserveId);

            call.enqueue(new Callback<ResponseData<ResponseGallery>>() {
                @Override
                public void onResponse(Call<ResponseData<ResponseGallery>> call, Response<ResponseData<ResponseGallery>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<ResponseGallery>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deletePhoto(Context context, int photo_id, final ResponseCallback<ResponseData<ResponseGallery>> callback) {

        try {
            Call<ResponseData<Object>> call = service.delGuestPhotos(photo_id);

            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void checkChangeCourse(final Context context, final ResponseCallback<ResCheckChangeCourse> callback) {
        try {
            int reserve_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
            Call<ResCheckChangeCourse> call = service.checkChangeCourse(reserve_id);
            URL url = call.request().url().url();
            call.enqueue(new Callback<ResCheckChangeCourse>() {
                @Override
                public void onResponse(Call<ResCheckChangeCourse> call, Response<ResCheckChangeCourse> response) {

                    callback.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<ResCheckChangeCourse> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void changeCourse(final Context context, String before_course, String after_course, ResponseCallback<ResponseData<Object>> callback) {
        try {
            int reserve_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
            Call<ResponseData<Object>> call = service.setChangeCourse( reserve_id, before_course, after_course);

            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void changeCourseList(final Context context, ResponseCallback<ResponseData<CType>> callback) {
        try {
            int reserve_id = Global.teeUpTime.getTodayReserveList().get(Global.selectedTeeUpIndex).getId();
            Call<ResponseData<CType>> call = service.getChangeCourseList(reserve_id);

            call.enqueue(new Callback<ResponseData<CType>>() {
                @Override
                public void onResponse(Call<ResponseData<CType>> call, Response<ResponseData<CType>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<CType>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendSMS(final Context context, SendSMS sendSMS, ResponseCallback<ResponseData<CType>> callback) {
        try {
            Call<ResponseData<Object>> call = service.sendSmsScore(sendSMS);

            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setCeoImage(Context context, int photoId, final ResponseCallback<ResponseData<ResponseGallery>> callback) {

        try {
            Call<ResponseData<Object>> call = service.setCeoImage(photoId);

            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initSetMessage(Context context, int cc_id, String sender_id, String date, final ResponseCallback<ResponseData<ChatData>> callback) {

        try {
            Call<ResponseData<ChatData>> call = service.initSetMessage(cc_id, sender_id, date);

            call.enqueue(new Callback<ResponseData<ChatData>>() {
                @Override
                public void onResponse(Call<ResponseData<ChatData>> call, Response<ResponseData<ChatData>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<ChatData>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void tabletChattingNameList(Context context, final ResponseCallback<ResponseChatNameList> callback) {

        try {
            Call<ResponseChatNameList> call = service.tabletChattingNameList();

            call.enqueue(new Callback<ResponseChatNameList>() {
                @Override
                public void onResponse(Call<ResponseChatNameList> call, Response<ResponseChatNameList> response) {
                    callback.onSuccess(response.body());
                }

                @Override
                public void onFailure(Call<ResponseChatNameList> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void logout(Context context, final ResponseCallback<ResponseData<Object>> callback) {

        try {
            Call<ResponseData<Object>> call = service.logout(Global.CaddyNo);

            call.enqueue(new Callback<ResponseData<Object>>() {
                @Override
                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
