package com.lingsatuo.Dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class help {
    public help(final Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle("教程")
                        .setMessage("这是您第一次使用，强烈建议您先转到优酷观看样例视频\n\n视频包括了 裁剪  导出  导出专辑  视频提取音频  合成的样例\n\n可以帮助您更快的了解Dump Music")
                        .setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse("http://v.youku.com/v_show/id_XMjc3OTc5NzU5Ng==.html?x&sharefrom=android");
                                intent.setData(content_url);
                                activity.startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消",null).show();
            }
        });
    }
}
