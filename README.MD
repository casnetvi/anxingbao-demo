## 配置
##### 1：在项目顶层的 build.gradle 添加
```java
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

##### 2：在App Module中的 build.gradle 添加
```java
android {
    defaultConfig {
        multiDexEnabled true
    }
    dataBinding {
        enabled true
    }
}

dependencies {
    implementation 'com.github.casnetvi:anxingbao:0.1.5'
}
```
---
## 初始化
```java
AXBSDK.getInstance().init(this, "your_api_key");
```
---
## 使用
##### 1.监听登录凭证是否过期
```java
AXBSDK.getInstance().setCallback(new AXBSDK.Callback() {
    @Override
    public void onTokenException() {
        System.out.println("登录凭证过期，请重新登录");
    }
});
```
##### 2.登录
```java
AXBSDK.getInstance()
        .login(username, password)
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
                System.out.println(AXBErrorCode.getErrorText(LoginActivity.this, e));
            }
            @Override
            public void onNext(Object o) {
            }
        });
```
##### 3.退出登录
```java
AXBSDK.getInstance().logout(this);
```
##### 4.检查是否已经登录
```java
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
```
##### 5.嵌套安行宝列表
```java
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
```java
AXBSDK.getInstance().goToBindDeviceActivity(this);
```
