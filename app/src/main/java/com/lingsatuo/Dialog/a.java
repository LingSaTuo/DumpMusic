package com.lingsatuo.Dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lingsatuo.Utils.Util;

import java.io.File;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class a {
    public static void SaveSound(final Activity context, final String from, final String mname){
        Util.setWork(context);
        final EditText name = new EditText(context);
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        final TextView tv = new TextView(context);
        tv.setText(Util.getWorkPath(context)+"/提取/"+mname+".aac");
        ll.addView(tv);
        ll.addView(name);
        name.setSingleLine(true);
        name.setText(mname);
        final AlertDialog AD =   new AlertDialog.Builder(context)
                .setTitle("保存")
                .setView(ll)
                .setNegativeButton("取消",null)
                .setPositiveButton("保存",null)
                .show();
        final Button qd = AD.getButton(DialogInterface.BUTTON_POSITIVE);
        {if(new File(Util.getWorkPath(context)+"/提取/"+name.getText().toString()+".aac").exists()){
            qd.setText("覆盖保存");
        }else{
            qd.setText("保存");
        }}
        if(!Util.istruepath(mname)){
            qd.setText("不合法的名字");
            qd.setEnabled(false);
            name.setTextColor(Color.RED);
        }

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                    qd.setEnabled(false);
                else {
                    qd.setEnabled(true);

                    tv.setText(Util.getWorkPath(context)+"/提取/"+name.getText().toString()+".aac");
                    {if(new File(Util.getWorkPath(context)+"/提取/"+name.getText().toString()+".aac").exists()){
                        qd.setText("覆盖保存");
                    }else{
                        qd.setText("保存");
                    }}

                    if(!Util.istruepath(name.getText().toString())){
                        qd.setText("不合法的名字");
                        qd.setEnabled(false);
                        name.setTextColor(Color.RED);
                    }
                }
            }
        });
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AD.dismiss();
                Util.show(context);
                final File file = new File(Util.getWorkPath(context)+"/提取/"+name.getText().toString()+".aac");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Util.muxerAudio(from,file.getAbsolutePath());
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,"完成",Toast.LENGTH_SHORT).show();
                                    new SavaAS(context,file);
                                }
                            });
                        }catch (final Exception e) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.show(context,"失败",e.toString());
                                }
                            });
                        }finally {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.dissmiss();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}
