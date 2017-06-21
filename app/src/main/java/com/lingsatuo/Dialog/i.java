package com.lingsatuo.Dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lingsatuo.Utils.Util;
import com.lingsatuo.dumpmusic.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/25 0025.
 */
public class i {
    Activity activity;
    LinearLayout ll;
    public i(Activity activity){
        this.activity = activity;
        ll  = new LinearLayout(activity);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(Util.dip2px(activity,14),Util.dip2px(activity,14),Util.dip2px(activity,14),Util.dip2px(activity,0));
    }
    public void show(){
        TextView tv = new TextView(activity);
        tv.setText("请您输入您的邮箱，方便联系，您也可以不输入，进行匿名反馈");
        tv.setGravity(Gravity.CENTER_VERTICAL);
        ll.addView(tv);
        final AppCompatEditText email = new AppCompatEditText(activity);
        email.setHint("不输入为 匿名");
        email.setSingleLine(true);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        ll.addView(email);
        TextView tv2 = new TextView(activity);
        tv2.setText("请输入您的反馈内容");
        tv2.setGravity(Gravity.CENTER_VERTICAL);
        ll.addView(tv2);
        final AppCompatEditText str = new AppCompatEditText(activity);
        ll.addView(str);
        final AlertDialog ad =  new AlertDialog.Builder(activity)
                .setTitle("反馈")
                .setView(ll)
                .setIcon(R.mipmap.music)
                .setPositiveButton("发送", null)
                .setNegativeButton("取消",null)
                .show();
        ad.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    String e1 = email.getText().toString();
                    final String memail = e1.equals("")?"nimingfeedback@163.com":e1;
                    final String nr = str.getText().toString();
                    if(isEmail(memail)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                {
                                    c.sendMessage(activity,memail+"\n\n"+nr,null);
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity,"我会认真查看的",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }else{
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"邮箱格式错误哦，请核实",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }
    public static boolean isEmail(String strEmail) {
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(strEmail);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }
}