package com.eye3.golfpay.net;

public interface ChangeStatusInterface {
    void onSuccess();
    void onErrorCode(ResponseData<Object> response);
    void onError();
    void onFailed();
}
