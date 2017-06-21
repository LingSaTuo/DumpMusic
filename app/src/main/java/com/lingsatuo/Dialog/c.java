package com.lingsatuo.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.ApplicationExc.BugActivity;
import com.lingsatuo.Utils.Util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class c {
    public static void show(final Activity activity, String title, final String err, final String pathi){
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(err)
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMessage(activity,err,null);
                    }
                })
                .setNeutralButton("发送文件", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> path = new ArrayList<String>();
                        path.add(pathi);
                        sendMessage(activity,err,path);
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
    public static void sendMessage(final Activity activity, final String mess, final List<String> path){
        Util.show(activity);
        new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    {
                        try {
                            Util.sendEmail("15968011381@163.com","Dump Music反馈",mess,path);
                            show(activity,"提交成功");
                        } catch (MessagingException e) {
                            System.out.println(e.toString());
                            show(activity,"提交失败");
                        } catch (UnsupportedEncodingException e) {
                            System.out.println(e.toString());
                            show(activity,"提交失败");
                        }finally {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.dissmiss();
                                }
                            });
                        }
                    }
                }
            }
        } ).start();
    }

   static void show(final Activity activity, final String mes){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,mes,Toast.LENGTH_LONG).show();
            }
        });
    }
}
