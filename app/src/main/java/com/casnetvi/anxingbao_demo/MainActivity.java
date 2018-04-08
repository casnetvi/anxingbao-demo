package com.casnetvi.anxingbao_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.casnetvi.app.frg.AXBFragment;
import com.casnetvi.app.presenter.base.v2.BackV2Activity;
import com.casnetvi.app.presenter.base.v2.BaseV2Activity;
import com.casnetvi.app.presenter.login.vm.LoginV2Activity;
import com.casnetvi.app.sdk.AXBSDK;
import com.casnetvi.app.utils.QRCodeHelper;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

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
    }

    private void logout() {
        AXBSDK.getInstance().logout(this);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    private void goToBind(){
//        AXBSDK.getInstance().goToBindDeviceActivity(this);
//        startActivity(new Intent(this, QRC.class));
        QRCodeHelper.showQRCodeActivity(this);
    }

}
