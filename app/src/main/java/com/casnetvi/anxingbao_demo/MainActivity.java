package com.casnetvi.anxingbao_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.casnetvi.app.frg.AXBFragment;
import com.casnetvi.app.sdk.AXBSDK;
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
        AXBSDK.getInstance().goToBindDeviceActivity(this);
    }

}
