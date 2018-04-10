## 配置
##### 1：在项目顶层的 build.gradle 添加
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

##### 2：在App Module中的 build.gradle 添加
```

android {
    defaultConfig {
        multiDexEnabled true
    }

    dataBinding {
        enabled true
    }
}

dependencies {
    implementation 'com.github.casnetvi:anxingbao:0.1.0'
}
```

##### 3：创建AppImpl，继承自App，指定到manifest中。
```
public class AppImpl extends AXBApp {
}
```

```
<manifest>
    <application
        android:name=".AppImpl">
    </application>
</manifest>
```


#####  完成以上步骤则配置完成。
---

## 使用
##### 1.监听登录凭证是否过期
```
AXBSDK.getInstance().setCallback(new AXBSDK.Callback() {
    @Override
    public void onTokenException() {
        System.out.println("登录凭证过期，请重新登录");
    }
});
```
##### 2.登录
```
AXBSDK.getInstance()
        .login(username, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.<User>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
           }
           @Override
            public void onError(Throwable e) {
                System.out.println(AXBErrorCode.getErrorText(LoginActivity.this, e));
            }
            @Override
            public void onNext(User user) {
                loginSuccess();
            }
        });
```
##### 3.退出登录
```
AXBSDK.getInstance().logout(this);
```
##### 4.获取已登录用户
```
AXBSDK.getInstance()
        .getCurrUser()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.<User>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(User user) {
                loginSuccess();
            }
        });
```
##### 5.嵌套安行宝列表
```
//须继承自 RxAppCompatActivity
public class MainActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new AXBFragment()).commit();
    }
}
```
##### 6.跳转进入绑定安行宝界面（扫描二维码）
```
AXBSDK.getInstance().goToBindDeviceActivity(this);
```
