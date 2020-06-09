package com.sk.appupdate.custom.ui;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.appupdate.R;
import com.sk.appupdate.SkAppUpdater;
import com.sk.appupdate.baseui.BaseUpdateDialog;
import com.sk.appupdate.net.INetDownloadCallBack;
import com.sk.appupdate.utils.AppUtil;
import com.sk.appupdate.view.CircleImageView;

import java.io.File;

/**
 * Created by smark on 2020/6/9.
 * 邮箱：smarkwzp@163.com
 */
public class SimpleNotifyDialog extends BaseUpdateDialog {
    private static final String TAG = "SimpleNotifyDialog";


    @Override
    protected int getLayoutId() {
        return R.layout.simple_dialog_layout;
    }

    @Override
    protected BaseUpdateDialog getCustomDialog() {
        return new SimpleNotifyDialog();
    }


    @Override
    protected void bindView(View view) {

        TextView new_version_name = view.findViewById(R.id.new_version_name);
        TextView size = view.findViewById(R.id.size);
        TextView content = view.findViewById(R.id.content);
        final TextView cancer = view.findViewById(R.id.cancer);
        final TextView update = view.findViewById(R.id.update);

        if (mUpdateInfoBean != null) {
            new_version_name.setText(String.format("最新版本：V%s", mUpdateInfoBean.getNew_version()));
            size.setText(String.format("更新包大小：%s", mUpdateInfoBean.getTarget_size()));
            content.setText(mUpdateInfoBean.getUpdate_log());
            if (mUpdateInfoBean.isConstraint()) {
                cancer.setVisibility(View.GONE);
            } else {
                cancer.setVisibility(View.VISIBLE);
            }
        }
        cancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                update.setText("更新中...");
                cancer.setVisibility(View.GONE);
                File apkFile = new File(getActivity().getCacheDir(), "target.apk");
                SkAppUpdater.getINetManager().downloadApk(mUpdateInfoBean.getApk_file_url(), apkFile, new INetDownloadCallBack() {
                    @Override
                    public void success(File apkFile) {
                        v.setEnabled(true);
                        dismiss();
                        String fileMd5 = AppUtil.getApkMd5(apkFile);
                        Log.e(TAG, "newApkMd5: " + fileMd5);
                        if (fileMd5 != null && fileMd5.equalsIgnoreCase(mUpdateInfoBean.getNew_md5())) {
                            AppUtil.installApp(getActivity(), apkFile);
                        } else {
                            Toast.makeText(getActivity(), "安装包校验未通过，更新失败!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void progress(int progress) {
                        update.setText(String.format("%d%%", progress));
                    }

                    @Override
                    public void failed(Throwable e) {
                        v.setEnabled(true);
                        e.printStackTrace();
                        dismiss();
                        Toast.makeText(getActivity(), "更新失败!", Toast.LENGTH_SHORT).show();
                    }
                }, SimpleNotifyDialog.this);
            }
        });

    }


}
