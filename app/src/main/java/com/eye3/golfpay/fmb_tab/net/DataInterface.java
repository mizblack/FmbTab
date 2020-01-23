package com.eye3.golfpay.fmb_tab.net;

import android.content.Context;

import com.eye3.golfpay.fmb_tab.model.Token;
import com.eye3.golfpay.fmb_tab.model.login.Login;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataInterface extends BasicDataInterface {
    private static DataInterface instance;
    protected String TAG = getClass().getSimpleName();
//    private FmbCustomDialog dialog;

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

    private void processCommonError(Context context, ResponseCallback callback, Response response) {
        processCommonError(context, callback, response, true);
    }

    private void processCommonError(Context context, ResponseCallback callback, Response response, boolean isCommonError) {
        if (callback == null) {
            return;
        }

        ResponseData data = (ResponseData) response.body();

        if (response.isSuccessful()) {
            if (data != null) {
                if (isCommonError && !data.getResultCode().equals("S1000")) {
                    //   showDialog(context, null, data.getError());
                }
                callback.onSuccess(data);
            } else {
                callback.onError(null);
            }
        } else {
            if (isCommonError) {
                if (data != null) {
                    //    showDialog(context, null, data.getError());
                } else {
                    //     showDialog(context, null, "네트웍상태를 확인해주세요.");
                }
            } else {
                callback.onError(null);
            }
        }
    }


    public void login(String id, String pwd, final ResponseCallback<ResponseData<Login>> callback) {
        try {
            Call<ResponseData<Login>> call = service.doCaddyLogin(id, pwd);
            call.enqueue(new Callback<ResponseData<Login>>() {
                @Override
                public void onResponse(Call<ResponseData<Login>> call, Response<ResponseData<Login>> response) {
                }

                @Override
                public void onFailure(Call<ResponseData<Login>> call, Throwable t) {
                    if (callback == null) return;
                    t.printStackTrace();
                    callback.onFailure(t);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//
//    public void getUserInfo(final Context context, final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<UserInfo>> call = service.getUserInfo();
//
//            call.enqueue(new Callback<ResponseData<UserInfo>>() {
//                @Override
//                public void onResponse(Call<ResponseData<UserInfo>> call, Response<ResponseData<UserInfo>> response) {
//                    processCommonError(context, callback, response, false);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<UserInfo>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
//                    callback.onFailure(t);
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

//    public void apiAcceptCheckDeviceMasterSearch(final Context context,  HashMap<String, Object> params,final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<Device>> call = service.apiAcceptCheckDeviceMasterSearch(params);
//
//            call.enqueue(new Callback<ResponseData<Device>>() {
//                @Override
//                public void onResponse(Call<ResponseData<Device>> call, Response<ResponseData<Device>> response) {
//                    processCommonError(context, callback, response, false);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<Device>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
//                    callback.onFailure(t);
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//     //인수시험 검색 항목 요청 api
//    public void apiAcceptCheckItemSearch(final Context context,  HashMap<String, Object> params,final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<DeviceTestItem>> call = service.apiAcceptCheckItemSearch(params);
//
//            call.enqueue(new Callback<ResponseData<DeviceTestItem>>() {
//                @Override
//                public void onResponse(Call<ResponseData<DeviceTestItem>> call, Response<ResponseData<DeviceTestItem>> response) {
//                    processCommonError(context, callback, response, false);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<DeviceTestItem>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
//                    callback.onFailure(t);
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void apiMasterDeviceGroupSearch(final Context context,  HashMap<String, Object> params,final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<Device>> call = service.apiMasterDeviceGroupSearch(params);
//
//            call.enqueue(new Callback<ResponseData<Device>>() {
//                @Override
//                public void onResponse(Call<ResponseData<Device>> call, Response<ResponseData<Device>> response) {
//                    processCommonError(context, callback, response, false);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<Device>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
//                    callback.onFailure(t);
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void apiApiAcceptCheckGroupInsert(final Context context,  HashMap<String, Object> params,final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<Object>> call = service.apiAcceptCheckInsert(params);
//
//            call.enqueue(new Callback<ResponseData<Object>>() {
//                @Override
//                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
//                    processCommonError(context, callback, response, false);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
//                    callback.onFailure(t);
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void apiApiAcceptCheckInsert(final Context context,  HashMap<String, Object> params,final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<Object>> call = service.apiAcceptCheckInsert(params);
//
//            call.enqueue(new Callback<ResponseData<Object>>() {
//                @Override
//                public void onResponse(Call<ResponseData<Object>> call, Response<ResponseData<Object>> response) {
//                    processCommonError(context, callback, response, false);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<Object>> call, Throwable t) {
//                    if (callback == null) return;
//
//                    t.printStackTrace();
//                    callback.onFailure(t);
//                }
//            });
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//    public void ApiAcceptCheckMasterSearch(final Context context, HashMap<String, Object> params, final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<DeviceTest>> call = service.apiAcceptCheckMasterSearch(params);
//
//            call.enqueue(new Callback<ResponseData<DeviceTest>>() {
//                @Override
//                public void onResponse(Call<ResponseData<DeviceTest>> call, Response<ResponseData<DeviceTest>> response) {
//                    processCommonError(context, callback, response);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<DeviceTest>> call, Throwable t) {
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
//    public void  ApiAcceptCheckDetailSearch(final Context context, HashMap<String, Object> params, final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<DeviceTestItem>> call = service.apiAcceptCheckDetailSearch(params);
//
//            call.enqueue(new Callback<ResponseData<DeviceTestItem>>() {
//                @Override
//                public void onResponse(Call<ResponseData<DeviceTestItem>> call, Response<ResponseData<DeviceTestItem>> response) {
//                    processCommonError(context, callback, response);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData<DeviceTestItem>> call, Throwable t) {
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
//    public void  ApiAcceptCheckImageSearch(final Context context, HashMap<String, Object> params, final ResponseCallback callback) {
//
//        try {
//            Call<ResponseData<DownloadPhoto>> call = service.apiAcceptCheckImageSearch(params);
//
//            call.enqueue(new Callback<ResponseData<DownloadPhoto>>() {
//                @Override
//                public void onResponse(Call<ResponseData<DownloadPhoto>> call, Response<ResponseData<DownloadPhoto>> response) {
//                    processCommonError(context, callback, response);
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
//                    processCommonError(context, callback, response);
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
//                    processCommonError(context, callback, response);
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
