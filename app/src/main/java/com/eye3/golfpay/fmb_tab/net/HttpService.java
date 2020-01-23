package com.eye3.golfpay.fmb_tab.net;


import androidx.annotation.Nullable;

import com.eye3.golfpay.fmb_tab.model.Device;
import com.eye3.golfpay.fmb_tab.model.Token;
import com.eye3.golfpay.fmb_tab.model.login.Login;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

}
