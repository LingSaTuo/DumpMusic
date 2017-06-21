package com.lingsatuo.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.ApplicationExc.AllActivity;
import com.lingsatuo.Service.Player;
import com.lingsatuo.dumpmusic.MainActivity;
import com.lingsatuo.dumpmusic.SecondActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class d {
    static String e;
    public static void show(final Activity activity, String title, final Exception err){
        final String pre = err.toString().substring(err.toString().length()-3,err.toString().length());
        System.out.println(pre);
       e = "示波器遇到了状况，导致初始化失败了，希望您可以发送错误信息，以便开发者更好的适配。\n\n"+ Player.getErr(err);
        if ((pre.replaceAll(" ","")).equals("-3")){
            e = "不必担心，只是权限不足而已，您只需要点击授权并给予录音权限即可";
        }
       AlertDialog ad =  new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(e)
                .setPositiveButton("授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "请尝试授权，安卓6.0以下无需任何操作", Toast.LENGTH_LONG).show();
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + activity.getPackageName())); // 根据包名打开对应的设置界面
                            activity.startActivity(intent);
                        } catch (Exception e3) {
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                    AllActivity.getInstance().exit();
                                }
                            }).start();
                        }
                    }
                })
                .setNeutralButton("发送",null)
                .setNegativeButton("取消",null)
                .show();
        ad.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pre.replaceAll(" ","").equals("-3")){
                    Toast.makeText(activity,"这只是权限不足而已，不用发送报告的",Toast.LENGTH_LONG).show();
                    return;
                }
                c.sendMessage(activity,e,null);
            }
        });
    }
}
