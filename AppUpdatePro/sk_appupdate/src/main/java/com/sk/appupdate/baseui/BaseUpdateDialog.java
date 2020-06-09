package com.sk.appupdate.baseui;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.sk.appupdate.SkAppUpdater;
import com.sk.appupdate.bean.UpdateInfoBean;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 */
public abstract class BaseUpdateDialog extends DialogFragment {

    private static final String TAG = "UpdateNotifyDialog";

    public static final String KEY_UPDATE_INFO = "update_info";

    protected UpdateInfoBean mUpdateInfoBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mUpdateInfoBean = bundle.getParcelable(KEY_UPDATE_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        bindView(view);
        return view;
    }

    protected abstract void bindView(View view);

    @LayoutRes
    protected abstract int getLayoutId();


    protected abstract BaseUpdateDialog getCustomDialog();


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(mUpdateInfoBean!=null){
            if(mUpdateInfoBean.isConstraint()){
                getDialog().setCanceledOnTouchOutside(false);
                getDialog().setCancelable(false);
                getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return true;
                        }
                        return false;
                    }
                });
            }else{
                getDialog().setCanceledOnTouchOutside(true);
                getDialog().setCancelable(true);
            }
        }
    }


    public void show(FragmentActivity activity, UpdateInfoBean updateInfoBean) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_UPDATE_INFO, updateInfoBean);
        BaseUpdateDialog dialog = getCustomDialog();
        dialog.setArguments(bundle);
        dialog.show(activity.getSupportFragmentManager(), getCustomDialog().getClass().getSimpleName());
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.i(TAG, "updateDialog onDismiss");
        SkAppUpdater.getINetManager().cancer(this);
    }


}
