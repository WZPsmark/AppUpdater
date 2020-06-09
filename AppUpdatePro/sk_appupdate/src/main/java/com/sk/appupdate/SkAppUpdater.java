package com.sk.appupdate;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.sk.appupdate.annotation.CheckType;
import com.sk.appupdate.baseui.BaseUpdateDialog;
import com.sk.appupdate.bean.UpdateInfoBean;
import com.sk.appupdate.custom.net.OkHttpNetManager;
import com.sk.appupdate.custom.ui.CustomNotifyDialog;
import com.sk.appupdate.net.INetCallBack;
import com.sk.appupdate.net.INetManager;
import com.sk.appupdate.utils.AppUtil;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 */
public class SkAppUpdater {

    private static final String TAG = "SkAppUpdater";

    private static SkAppUpdater mInstance = null;

    private static Application mAppContext;

    private static INetManager mINetManager = new OkHttpNetManager();

    private static String mUpdateUrl = "";

    private static BaseUpdateDialog sNotifyDialog;

    public final static int SILENT_CHECK = 0;//静默检查

    public final static int INITIATIVE_CHECK = 1;//手动检查


    public static SkAppUpdater init(Application application) {
        if (mInstance != null) {
            return mInstance;
        }
        mAppContext = application;
        mInstance = new SkAppUpdater();
        sNotifyDialog = new CustomNotifyDialog();
        return mInstance;
    }


    public SkAppUpdater setINetManager(INetManager INetManager) {
        checkInit();
        mINetManager = INetManager;
        return mInstance;
    }

    public static INetManager getINetManager() {
        checkInit();
        return mINetManager;
    }

    public SkAppUpdater setUpdateUrl(String updateUrl) {
        checkInit();
        mUpdateUrl = updateUrl;
        return mInstance;
    }


    public SkAppUpdater setCustomDialog(BaseUpdateDialog dialog) {
        checkInit();
        sNotifyDialog = dialog;
        return mInstance;
    }


    public static void checkUpdate(final FragmentActivity tag, @CheckType final int type) {
        checkInit();
        if ("".equals(mUpdateUrl)) {
            throw new RuntimeException("SkAppUpdater：请先设置更新信息url");
        }
        mINetManager.get(mUpdateUrl, new INetCallBack() {
            @Override
            public void onSuccess(String data) {
                Log.i(TAG, "onSuccess: " + data);
                UpdateInfoBean bean = UpdateInfoBean.parse(data);
                if (null == bean) {
                    return;
                }
                try {
                    long versionCode = Long.parseLong(bean.getNew_version_code());
                    if (versionCode <= AppUtil.getVersionCode(mAppContext)) {
                        if (type == INITIATIVE_CHECK) {
                            Toast.makeText(mAppContext, "App当前已是最新版本", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onSuccess: 版本号不符合规则");
                    return;
                }
                sNotifyDialog.show(tag, bean);
            }

            @Override
            public void failed(Throwable e) {
                e.printStackTrace();
            }
        }, tag);

    }


    private static void checkInit() {
        if (mInstance == null) {
            throw new RuntimeException("SkAppUpdater:请先初始化! init()");
        }
    }

}
