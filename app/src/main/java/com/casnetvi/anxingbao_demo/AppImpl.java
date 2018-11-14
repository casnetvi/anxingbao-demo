package com.casnetvi.anxingbao_demo;

import android.app.Application;

import com.casnetvi.app.sdk.AXBSDK;

/**
 * Created by wzx on 18/4/4.
 */

public class AppImpl extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        AXBSDK.getInstance().init(this, "your_api_key");
        AXBSDK.getInstance().init(this, "bb");

        //监听登录凭证是否过期
        AXBSDK.getInstance().setCallback(new AXBSDK.Callback() {
            @Override
            public void onTokenException() {
                System.out.println("登录凭证过期，请重新登录");
            }
        });
    }
}
