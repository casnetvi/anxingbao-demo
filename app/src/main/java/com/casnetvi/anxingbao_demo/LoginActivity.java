package com.casnetvi.anxingbao_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.casnetvi.app.sdk.AXBErrorCode;
import com.casnetvi.app.sdk.AXBSDK;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzx.datamove.realm.entry.User;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class LoginActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        checkIsLogin();
    }

    /**
     * 检查是否已经登录
     */
    private void checkIsLogin() {
        AXBSDK.getInstance()
                .isLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Object>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        loginSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
    }

    /**
     * 登录
     */
    private void login() {
        String username = ((EditText) findViewById(R.id.etUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.etPassword)).getText().toString();

        AXBSDK.getInstance()
                .login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Object>bindUntilEvent(ActivityEvent.DESTROY))
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        findViewById(R.id.progress).setVisibility(View.VISIBLE);
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        findViewById(R.id.progress).setVisibility(View.GONE);
                    }
                })
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        loginSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(AXBErrorCode.getErrorText(LoginActivity.this, e));
                        Toast.makeText(LoginActivity.this, AXBErrorCode.getErrorText(LoginActivity.this, e), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
    }

    private void loginSuccess() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
