package com.casnetvi.anxingbao_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.casnetvi.app.entity.other.AXBDevice;
import com.casnetvi.app.frg.AXBFragment;
import com.casnetvi.app.sdk.AXBSDK;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println();

        getSupportFragmentManager().beginTransaction().add(R.id.container, new AXBFragment()).commit();


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
                .compose(this.<List<AXBDevice>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Subscriber<List<AXBDevice>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<AXBDevice> devices) {
                        Toast.makeText(MainActivity.this, "共有：" + devices.size() +"个设备", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
