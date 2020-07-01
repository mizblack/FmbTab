package com.eye3.golfpay.fmb_tab.net;

import android.content.Context;
import android.view.View;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.guest.ReserveGuestList;
import com.eye3.golfpay.fmb_tab.model.info.GuestInfoResponse;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.model.notice.NoticeItem;
import com.eye3.golfpay.fmb_tab.model.order.PlayStatus;
import com.eye3.golfpay.fmb_tab.model.order.Restaurant;
import com.eye3.golfpay.fmb_tab.model.order.ShadeOrder;
import com.eye3.golfpay.fmb_tab.model.order.StoreOrder;
import com.eye3.golfpay.fmb_tab.model.score.ReserveScore;
import com.eye3.golfpay.fmb_tab.model.teeup.Player;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;

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
            Call<ResponseData<Course>> call = service.getCourseInfo(cc_id);
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

    public void getRestaurantMenu(final Context context, String caddyId, String reserve_no, final ResponseCallback<ResponseData<Restaurant>> callback) {
        try {
            Call<ResponseData<Restaurant>> call = service.getRestaurantMenu(caddyId, reserve_no);
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






    public void getNoticeList(final Context context, final ResponseCallback<ResponseData<NoticeItem>> callback) {
        try {
            Call<ResponseData<NoticeItem>> call = service.getNoticeList();
            call.enqueue(new Callback<ResponseData<NoticeItem>>() {

                @Override
                public void onResponse(Call<ResponseData<NoticeItem>> call, Response<ResponseData<NoticeItem>> response) {
                    solveCommonError(context, callback, response, false);
                }

                @Override
                public void onFailure(Call<ResponseData<NoticeItem>> call, Throwable t) {
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

}
