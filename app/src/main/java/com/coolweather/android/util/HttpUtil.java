package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/8/29.
 */

public class HttpUtil {

    //发送一条HTTP请求只需要调用sendOkHttpRequest()方法传入请求地址，注册一个回调来处理服务器响应
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }



}
