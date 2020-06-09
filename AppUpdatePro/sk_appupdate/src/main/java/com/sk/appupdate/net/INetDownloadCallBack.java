package com.sk.appupdate.net;

import java.io.File;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 * apk下载回调
 */
public interface INetDownloadCallBack {

    void success(File apkFile);

    void progress(int progress);

    void failed(Throwable e);


}
