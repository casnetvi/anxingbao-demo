package com.casnetvi.anxingbao_demo;

import com.casnetvi.app.sdk.AXBApp;
import com.casnetvi.app.sdk.AXBSDK;

/**
 * Created by wzx on 18/4/4.
 */

public class AppImpl extends AXBApp {
    @Override
    public void onCreate() {
        super.onCreate();

        //监听登录凭证是否过期
        AXBSDK.getInstance().setCallback(new AXBSDK.Callback() {
            @Override
            public void onTokenException() {
                System.out.println("登录凭证过期，请重新登录");
            }
        });
    }
}
