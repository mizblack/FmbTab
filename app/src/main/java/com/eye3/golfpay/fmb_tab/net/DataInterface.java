package com.eye3.golfpay.fmb_tab.net;

import android.content.Context;
import android.view.View;

import com.eye3.golfpay.fmb_tab.model.field.Course;
import com.eye3.golfpay.fmb_tab.model.login.Login;
import com.eye3.golfpay.fmb_tab.model.teeup.TeeUpTime;
import com.eye3.golfpay.fmb_tab.util.FmbCustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public DataInterface() {
        super();
    }

    public DataInterface(String url) {
        super(url);
    }

    public static boolean isCallSuccess(Response response) {
        return response.isSuccessful();
    }

//    private void showDialog(Context context, String title, String msg) {
//        dialog = new FmbCustomDialog(context, title, msg, "확인", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        try {
//            dialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //ResponseData를 사용하지 않을때 필요없슴.
    private void solveCommonError(Context context, ResponseCallback callback, Response response) {
        solveCommonError(context, callback, response, true);
    }

    //ResponseData를 사용하지 않을때 필요없슴.
    private void solveCommonError(Context context, ResponseCallback callback, Response response, boolean isCommonError) {
        if (callback == null) {
            return;
        }

        ResponseData data = (ResponseData)response.body();

        if (response.isSuccessful()) {
            if (data != null) {
                if (isCommonError && !data.getResultCode().equals("S000")) {
                    showDialog(context, null, data.getError());
                }
                callback.onSuccess(data);
            } else {
                callback.onError(null);
            }
        } else {
            if(isCommonError) {
                if (data != null) {
                    showDialog(context, null, data.getError());
                } else {
                    showDialog(context, null, "네트웍상태를 확인해주세요.");
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

    public void getTodayReservesForCaddy(final Context context, String caddy_id, final ResponseCallback<TeeUpTime> callback) {
        try {
            Call<TeeUpTime> call = service.getTodayReservesForCaddy(caddy_id);
            call.enqueue(new Callback<TeeUpTime>() {
                @Override
                public void onResponse(Call<TeeUpTime> call, Response<TeeUpTime> response) {
                    TeeUpTime teeUpTime =  response.body();
                    // solveCommonError(context, callback, response, false);
                    if(response == null){
                        callback.onError(teeUpTime);
                        return;
                    }else{

                        callback.onSuccess(teeUpTime);
                    }
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

    public void login(final Context context, String id, String pwd, final ResponseCallback<Login> callback) {
        try {
            Call<Login> call = service.doCaddyLogin(id, pwd);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                  Login login =  response.body();
                   // solveCommonError(context, callback, response, false);
                    if(response == null){
                    callback.onError(login);
                    return;
                }else{

                    callback.onSuccess(login);
                }
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

    public void getCourseInfo(final Context context, String cc_id, final ResponseCallback<Object> callback) {
        try {
            Call<Object> call = service.getCourseInfo();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Object data =  (ArrayList<Course>) response.body();
                    //ArrayList<Course> courseList = data.
                    // solveCommonError(context, callback, response, false);
                    if(response == null){
                        callback.onError(data);
                        return;
                    }else{

                        callback.onSuccess(data);
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public void sendConfirm(final Context context, HashMap<String, Object> params, final DataInterface.ResponseCallback callback){
//        Call<Map<String, String>> call = service.sendConfirm(params);
//        call.enqueue(new Callback<Map<String, String>>() {
//            @Override
//            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
//                Map<String, String> list =   response.body();
//                if(response == null){
//                    callback.onError(response.body());
//                    return;
//                }
//                if(list.size() > 0){
//                    callback.onSuccess(list);
//                }
////                  Map<String, Model> map = new HashMap<String, Model>();
////                  map = response.body().datas;
//
////                  for (String keys: map.keySet()) {
////                      // myCode;
////                  }
//
//            }
//
//            @Override
//            public void onFailure(Call<Map<String, String>> call, Throwable t) {
//
//            }
//        });
//    }



//
//    public void  ApiAcceptCheckImageSearch(final Context context, HashMap<String, Object> params, final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<DownloadPhoto>> call = service.apiAcceptCheckImageSearch(params);
//
//            call.enqueue(new Callback<ResponseData<DownloadPhoto>>() {
//                @Override
//                public void onResponse(Call<ResponseData<DownloadPhoto>> call, Response<ResponseData<DownloadPhoto>> response) {
//                    solveCommonError(context, callback, response);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<DownloadPhoto>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
////                    callback.onFailure(t);
//                    //              showDialog(context, null, "네트웍상태를 확인해주세요.");
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void  ApiAcceptCheckImageInsert(final Context context, RequestBody deviceId, RequestBody acceptCheckItemOrder, RequestBody itemCode, MultipartBody.Part uploadFile, RequestBody imageFile, RequestBody abbre, RequestBody extension, final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<Object>> call = service.apiUploadImage(deviceId, acceptCheckItemOrder, itemCode, uploadFile, imageFile,  abbre, extension);
//
//            call.enqueue(new Callback<ResponseData<Object>>() {
//                @Override
//                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
//                    solveCommonError(context, callback, response);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
////                    callback.onFailure(t);
//                    //              showDialog(context, null, "네트웍상태를 확인해주세요.");
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//    public void ApiAcceptCheckImageDelete(final Context context, HashMap<String, Object> params, final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<Object>> call = service.apiAcceptCheckImageDelete(params);
//
//            call.enqueue(new Callback<ResponseData<Object>>() {
//                @Override
//                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
//                    solveCommonError(context, callback, response);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
////                    callback.onFailure(t);
//                    //              showDialog(context, null, "네트웍상태를 확인해주세요.");
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


}
