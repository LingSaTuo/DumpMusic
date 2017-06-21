package com.ApplicationExc;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class ApplictionLoader extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationExc.getInstance().init(this);
        PgyCrashManager.register(this);
    }
}