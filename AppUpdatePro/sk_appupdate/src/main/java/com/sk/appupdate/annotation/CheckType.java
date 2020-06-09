package com.sk.appupdate.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.sk.appupdate.SkAppUpdater.INITIATIVE_CHECK;
import static com.sk.appupdate.SkAppUpdater.SILENT_CHECK;

/**
 * Created by smark on 2020/6/8.
 * 邮箱：smarkwzp@163.com
 */
@IntDef({SILENT_CHECK, INITIATIVE_CHECK})
@Retention(RetentionPolicy.SOURCE)
public @interface CheckType {
}
