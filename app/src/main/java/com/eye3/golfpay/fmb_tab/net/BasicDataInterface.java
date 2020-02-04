package com.eye3.golfpay.fmb_tab.net;

import com.eye3.golfpay.fmb_tab.common.Global;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasicDataInterface {
    public HttpService service;

    public BasicDataInterface() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);  //상용으로 올릴때는 false로
        Retrofit retrofit;
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder().baseUrl(Global.HOST_ADDRESS_AWS).addConverterFactory(GsonConverterFactory.create()).client(setHeader()).build();

        service = retrofit.create(HttpService.class);
    }

    public BasicDataInterface(String url) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);  //상용으로 올릴때는 false로

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).client(setHeader()).build();
        service = retrofit.create(HttpService.class);
    }


//	public BasicDataInterface(okhttp3.OkHttpClient a) {
//
//		Retrofit retrofit = new Retrofit.Builder()
//				.baseUrl(Global.HOST_ADDRESS_DEV)
//				.addConverterFactory(GsonConverterFactory.create())
//				//	.client(setHeader("Bearer " + MacaronApp.chauffeur.accessToken))
//				.client(a)
//				.build();
//		service = retrofit.create(HttpService.class);
//	}


    public OkHttpClient setHeader() {

//        Request original = chain.request();
//
//        Request request = original.newBuilder()
//                .header("User-Agent", "Your-App-Name")
//                .header("Accept", "application/vnd.yourapi.v1.full+json")
//                .method(original.method(), original.body())
//                .build();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = null;

                                    Request original = chain.request();
                                    // Request customization: add request headers
                                    Request.Builder requestBuilder = original.newBuilder();
                                    if (Global.Token != null) {  //토큰 설정
                                        requestBuilder.addHeader("Authorization", Global.Token);
                                     //   requestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + "utf-8");

                                     }
                                    request = requestBuilder.build();

                                return chain.proceed(request);
                            }
                        })
                .build();
        return okClient;

    }

}
