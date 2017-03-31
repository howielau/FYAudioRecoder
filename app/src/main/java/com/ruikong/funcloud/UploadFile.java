package com.ruikong.funcloud;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.spdy.FrameReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by ruikong on 2017/3/30.
 */

public class UploadFile implements Callback {

    static private final String uploadUrl = "http://rkmobile.cn/upload.php";

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private UploadInterface uploadImp;

    /**
     * @param mediaType MediaType
     * @param localPath 本地文件路径
     * @return 响应的结果 和 HTTP status code
     * @throws IOException
     */
    public void put(MediaType mediaType, String localPath) throws IOException {
        File file = new File(localPath);
        //30秒超时
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"fileupload\";filename=\"fileupload.amr\""), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(this);

    }

    public void upload(String filePath, UploadInterface imp) throws IOException {
        MediaType type = MediaType.parse("multipart/form-data; charset=utf-8; name=\"fileupload\"");
        callBack(imp);
        put(type, filePath);
    }

    public void callBack(UploadInterface imp){
        uploadImp = imp;
    }

    //上传JPG图片
    public void putImg(String Url, String localPath) throws IOException {
        MediaType Image = MediaType.parse("image/jpeg; charset=utf-8");
        put(Image, localPath);
    }

    @Override
    public void onFailure(Request request, final IOException e) {
        if (uploadImp!=null){


            Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    uploadImp.onUploadCallBack(null, new Error(e.getMessage()));
                    return true;
                }
            });
            handler.sendEmptyMessage(1);

        }

    }

    @Override
    public void onResponse(final Response response) throws IOException {
        if (uploadImp!=null){

            Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    uploadImp.onUploadCallBack(response, null);
                    return true;
                }
            });
            handler.sendEmptyMessage(1);
        }
    }
}
