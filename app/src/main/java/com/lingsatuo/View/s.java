package com.lingsatuo.View;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class s {
    public static int a(float f,Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return (int)((displayMetrics.density*f)+0.5f);
    }
}
