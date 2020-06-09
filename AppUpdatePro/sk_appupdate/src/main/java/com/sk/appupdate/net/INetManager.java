package com.sk.appupdate.net;

import java.io.File;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 * 网络请求待实现接口
 */
public interface INetManager {

    void get(String updateUrl, INetCallBack callBack, Object tag);

    void downloadApk(String apkUrl, File apkFile, INetDownloadCallBack callBack, Object tag);

    void cancer(Object tag);
}
