package com.sk.appupdate.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 */
public class UpdateInfoBean implements Parcelable {


    /**
     * new_version : 1.0.30
     * apk_file_url :
     * update_log : 1、优化用户体验
     * target_size : 10M
     * new_md5 : 295687E756F569C7159974DD493489A5
     * constraint : false
     */

    private String new_version;
    private String new_version_code;
    private String apk_file_url;
    private String update_log;
    private String target_size;
    private String new_md5;
    private boolean constraint;

    public static UpdateInfoBean parse(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            UpdateInfoBean bean = new UpdateInfoBean();
            bean.setNew_version(jsonObject.optString("new_version"));
            bean.setNew_version_code(jsonObject.optString("new_version_code"));
            bean.setApk_file_url(jsonObject.optString("apk_file_url"));
            bean.setUpdate_log(jsonObject.optString("update_log"));
            bean.setTarget_size(jsonObject.optString("target_size"));
            bean.setNew_md5(jsonObject.optString("new_md5"));
            bean.setConstraint(jsonObject.optBoolean("constraint"));
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getNew_version_code() {
        return new_version_code;
    }

    public void setNew_version_code(String new_version_code) {
        this.new_version_code = new_version_code;
    }

    public String getNew_version() {
        return new_version;
    }

    public void setNew_version(String new_version) {
        this.new_version = new_version;
    }

    public String getApk_file_url() {
        return apk_file_url;
    }

    public void setApk_file_url(String apk_file_url) {
        this.apk_file_url = apk_file_url;
    }

    public String getUpdate_log() {
        return update_log;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }

    public String getTarget_size() {
        return target_size;
    }

    public void setTarget_size(String target_size) {
        this.target_size = target_size;
    }

    public String getNew_md5() {
        return new_md5;
    }

    public void setNew_md5(String new_md5) {
        this.new_md5 = new_md5;
    }

    public boolean isConstraint() {
        return constraint;
    }

    public void setConstraint(boolean constraint) {
        this.constraint = constraint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.new_version);
        dest.writeString(this.new_version_code);
        dest.writeString(this.apk_file_url);
        dest.writeString(this.update_log);
        dest.writeString(this.target_size);
        dest.writeString(this.new_md5);
        dest.writeByte(this.constraint ? (byte) 1 : (byte) 0);
    }

    public UpdateInfoBean() {
    }

    protected UpdateInfoBean(Parcel in) {
        this.new_version = in.readString();
        this.new_version_code = in.readString();
        this.apk_file_url = in.readString();
        this.update_log = in.readString();
        this.target_size = in.readString();
        this.new_md5 = in.readString();
        this.constraint = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UpdateInfoBean> CREATOR = new Parcelable.Creator<UpdateInfoBean>() {
        @Override
        public UpdateInfoBean createFromParcel(Parcel source) {
            return new UpdateInfoBean(source);
        }

        @Override
        public UpdateInfoBean[] newArray(int size) {
            return new UpdateInfoBean[size];
        }
    };
}
