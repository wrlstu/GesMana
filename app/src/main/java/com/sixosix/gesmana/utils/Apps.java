package com.sixosix.gesmana.utils;

import android.graphics.drawable.Drawable;

import org.litepal.crud.DataSupport;

public class Apps extends DataSupport {
    private String appName;
    private String packageName;
    private Drawable mImage;

    public Apps() {
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable mImage) {
        this.mImage = mImage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
