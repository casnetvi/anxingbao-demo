## 配置
##### 1：在项目顶层的 build.gradle 添加
```
allprojects {
    repositories {
        //axb sdk need  :steps1
        maven { url 'https://jitpack.io' }
    }
}
```

##### 2：在App Module中的 build.gradle 添加
```
dependencies {
    //axb sdk need  :steps2
    implementation 'com.github.casnetvi:anxingbao:0.1.0'
}
```

##### 3：在App Module中的 build.gradle 添加
```
defaultConfig {
    //axb sdk need  :steps3
    multiDexEnabled true
}
```

##### 4：在App Module中的 build.gradle 添加
```
android {
    //axb sdk need  :steps4
    dataBinding {
        enabled true
    }
}
```

##### 5：创建AppImpl，继承自App，指定到manifest中。
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
##### 1.登录
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

##### 2.获取已登录用户
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
##### 3.退出登录
```
AXBSDK.getInstance().logout(this);
```
##### 4.嵌套安行宝列表
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
##### 5.进入绑定安行宝界面
```
AXBSDK.getInstance().goToBindDeviceActivity(this);
```




