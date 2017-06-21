package com.ApplicationExc;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
public class AllActivity extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static AllActivity instance;

    private AllActivity() {
    }
    public synchronized static AllActivity getInstance() {
        if (null == instance) {
            instance = new AllActivity();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}