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
    implementation 'com.github.casnetvi:anxingbao:0.4.0'
}
```
---
## 初始化
##### 1.初始化安行宝SDK Api key
```java
AXBSDK.getInstance().init(this, "your_api_key");
```
##### 2.初始化高德地图SDK Api key（在设置电子围栏时，使用到geo）
```xml
<meta-data
    android:name="com.amap.api.v2.apikey"
    android:value="你的高德地图api_key"/>
<service android:name="com.amap.api.location.APSService" />
```

###### 获取高德地图api key请参考[高德地图开发者官网](http://lbs.amap.com/api/android-sdk/guide/create-project/get-key)
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
AXBFragment fragment = new AXBFragment();
//callback == null，点击查看体征按钮，按sdk的逻辑跳转页面
//callback != null，点击查看体征按钮，回调事件
fragment.setOnSignsCallback(new AXBFragment.OnSignsCallback() {
    @Override
    public void onDeviceClick(String s, String s1) {
        System.out.println("点击查看体征按钮");
        System.out.println("imei : " + s);
        System.out.println("sn : " + s1);
    }
});
getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

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
    private String phone;//手机号码
    private String idCard;//身份证

    private String contact1Name;//亲情号码1
    private String contact1Phone;;
    private String contact2Name;;//亲情号码2
    private String contact2Phone;;
    private String contact3Name;;//亲情号码3
    private String contact3Phone;;

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
##### 7.绑定、解绑设备回调
调用```AXBSDK.getInstance().goToBindDeviceActivity(this);```后，设置```onActivityResult```回调即可
```java
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
```