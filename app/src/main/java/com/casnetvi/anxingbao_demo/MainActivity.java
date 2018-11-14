package com.casnetvi.anxingbao_demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.casnetvi.app.entity.other.AXBDevice;
import com.casnetvi.app.frg.AXBFragment;
import com.casnetvi.app.sdk.AXBSDK;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println();

        AXBFragment fragment = new AXBFragment();
//        callback == null，点击查看体征按钮，按sdk的逻辑跳转页面
//        callback != null，点击查看体征按钮，回调事件
        fragment.setOnSignsCallback(new AXBFragment.OnSignsCallback() {
            @Override
            public void onDeviceClick(String s, String s1) {
                System.out.println("点击查看体征按钮");
                System.out.println("imei : " + s);
                System.out.println("sn : " + s1);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();


        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        findViewById(R.id.btnBind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBind();
            }
        });
        findViewById(R.id.btnList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceList();
            }
        });
    }

    private void logout() {
        AXBSDK.getInstance().logout(this);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    private void goToBind() {
        AXBSDK.getInstance().goToBindDeviceActivity(this);
    }


    private void getDeviceList() {
        AXBSDK.getInstance()
                .getDeviceList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .compose(this.<List<AXBDevice>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Subscriber<List<AXBDevice>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<AXBDevice> devices) {
                        Toast.makeText(MainActivity.this, "共有：" + devices.size() + "个设备", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IntentIntegrator.REQUEST_CODE) {
                //扫码或输入SN绑定设备后的回调，点击【解绑设备】则isUnbind为true
                boolean isUnbind = data.getBooleanExtra("isUnbind", false);
                String imei = data.getStringExtra("imei");
                String sn = data.getStringExtra("sn");
                String phone = data.getStringExtra("phone");
                if (isUnbind) {
                    System.out.println("绑定设备");
                } else {
                    System.out.println("解除绑定");
                }
                System.out.println(imei);
                System.out.println(sn);
                System.out.println(phone);
            } else if (requestCode == AXBSDK.REQUEST_EDIT_DEVICE) {
                //点击列表项进入详细页面编辑信息后的回调，点击【解绑设备】则isUnbind为true
                boolean isUnbind = data.getBooleanExtra("isUnbind", false);
                if (isUnbind) {
                    String imei = data.getStringExtra("imei");
                    String sn = data.getStringExtra("sn");
                    String phone = data.getStringExtra("phone");

                    System.out.println("解除绑定");
                    System.out.println(imei);
                    System.out.println(sn);
                    System.out.println(phone);
                }
            }

        }
    }
}
