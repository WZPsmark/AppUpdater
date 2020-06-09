package com.sk.appupdate.custom.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sk.appupdate.net.INetCallBack;
import com.sk.appupdate.net.INetDownloadCallBack;
import com.sk.appupdate.net.INetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 * 具体网络访问以及文件下载（okhttp）
 */
public class OkHttpNetManager implements INetManager {

    private static final String TAG = "OkHttpNetManager";

    private static OkHttpClient mOkHttpClient;

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
    }

    @Override
    public void get(String updateUrl, final INetCallBack callBack, Object tag) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(updateUrl).get().tag(tag).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (call.isCanceled()) {
                    return;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.failed(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()) {
                    return;
                }
                try {
                    final String result = response.body().string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(result);
                        }
                    });
                } catch (Throwable e) {
                    callBack.failed(e);
                }
            }
        });

    }

    @Override
    public void downloadApk(String apkUrl, final File apkFile, final INetDownloadCallBack callBack, final Object tag) {
        if (!apkFile.exists()) {
            apkFile.getParentFile().mkdirs();
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(apkUrl).get().tag(tag).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.failed(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                OutputStream os = null;
                try {
                    final long total_size = response.body().contentLength();
                    is = response.body().byteStream();
                    os = new FileOutputStream(apkFile);
                    byte[] buffer = new byte[8 * 1024];
                    long curr_len = 0;
                    int buff_len = 0;
                    while (!call.isCanceled() && (buff_len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, buff_len);
                        os.flush();
                        curr_len += buff_len;

                        final long finalCurr_len = curr_len;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.progress((int) (finalCurr_len * 1.0f / total_size * 100));
                            }
                        });
                    }

                    if (call.isCanceled()) {
                        return;
                    }

                    try {
                        apkFile.setExecutable(true, false);
                        apkFile.setReadable(true, false);
                        apkFile.setWritable(true, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.success(apkFile);
                        }
                    });
                } catch (final Throwable e) {
                    if (call.isCanceled()) {
                        return;
                    }
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.failed(e);
                        }
                    });
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                }

            }
        });
    }

    @Override
    public void cancer(Object tag) {
        List<Call> queuedCalls = mOkHttpClient.dispatcher().queuedCalls();
        if (queuedCalls != null) {
            for (Call call : queuedCalls) {
                if (tag.equals(call.request().tag())) {
                    Log.i(TAG, "cancerRequest: " + tag);
                    call.cancel();
                }
            }
        }

        List<Call> runningCalls = mOkHttpClient.dispatcher().runningCalls();
        if (runningCalls != null) {
            for (Call call : runningCalls) {
                if (tag.equals(call.request().tag())) {
                    Log.i(TAG, "cancerRequest: " + tag);
                    call.cancel();
                }
            }
        }
    }
}
