package com.ruikong.funcloud;

import android.os.Handler;

import com.squareup.okhttp.Response;

public interface UploadInterface {
    void onUploadCallBack(Response res, Error error);
}
