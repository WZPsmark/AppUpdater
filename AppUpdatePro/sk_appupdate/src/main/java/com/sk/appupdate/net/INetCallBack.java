package com.sk.appupdate.net;

import com.sk.appupdate.bean.UpdateInfoBean;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 * 更新信息结果返回CallBack
 */
public interface INetCallBack {

    void onSuccess(String data);

    void failed(Throwable e);

}
