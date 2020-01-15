package com.eye3.golfpay.fmb_tab.net;

public interface ChangeStatusInterface {
    public void onSuccess();
    public void onErrorCode(ResponseData<Object> response);
    public void onError();
    public void onFailed();
}
