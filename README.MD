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
    implementation 'com.github.casnetvi:anxingbao:0.1.6'
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
##### 7.获取设备列表
```java
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
```

```java
public class AXBDevice {
    private String deviceId;//设备id,imei
    private String deviceSn;//设备序列号
    private String phone;

    private String realName;//设备使用者真实姓名
    private String nikeName;//设备使用者昵称
    private String image;//设备使用者头像
    private int battery;//剩余电量
    private boolean online;//是否在线
    private int step;//今天步数

    private String lastLocationAddress;//最后定位 位置
    private double lastLocationLat;//最后定位 纬度
    private double lastLocationLng;//最后定位 经度
    private long lastLocationTime;//最后定位 时间
}
```
