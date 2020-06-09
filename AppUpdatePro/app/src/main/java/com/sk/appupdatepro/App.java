package com.sk.appupdatepro;

import android.app.Application;

import com.sk.appupdate.SkAppUpdater;
import com.sk.appupdate.custom.ui.CustomNotifyDialog;
import com.sk.appupdate.custom.ui.SimpleNotifyDialog;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkAppUpdater.init(this)
                .setUpdateUrl("https://smark-file.oss-cn-shanghai.aliyuncs.com/updateApp/json_test.txt")
//                .setCustomDialog(new CustomNotifyDialog())
                .setCustomDialog(new SimpleNotifyDialog())
//                        .setINetManager(new OkHttpNetManager()
        ;
    }
}
