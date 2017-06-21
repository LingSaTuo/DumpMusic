package com.lingsatuo.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class f {
    public static void SavePic(final Activity context, final Bitmap bit, final String mname){
        Util.setWork(context);
        final EditText name = new EditText(context);
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        final TextView tv = new TextView(context);
        tv.setText(Util.getWorkPath(context)+"/专辑/"+mname+".png");
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
        {if(new File(Util.getWorkPath(context)+"/专辑/"+name.getText().toString()+".png").exists()){
            qd.setText("覆盖保存");
        }else{
            qd.setText("保存");
        }
            if(!Util.istruepath(mname)){
                qd.setText("不合法的名字");
                qd.setEnabled(false);
                name.setTextColor(Color.RED);
            }
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

                    tv.setText(Util.getWorkPath(context)+"/专辑/"+name.getText().toString()+".png");
                    {if(new File(Util.getWorkPath(context)+"/专辑/"+name.getText().toString()+".png").exists()){
                        qd.setText("覆盖保存");
                    }else{
                        qd.setText("保存");
                    }
                        if(!Util.istruepath(name.getText().toString())){
                            qd.setText("不合法的名字");
                            qd.setEnabled(false);
                            name.setTextColor(Color.RED);
                        }
                    }

                }
            }
        });
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AD.dismiss();
                Util.show(context);
                final File file = new File(Util.getWorkPath(context)+"/专辑/"+name.getText().toString()+".png");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new f().saveBitmap(context,file.getAbsolutePath(),bit);
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context,"完成",Toast.LENGTH_SHORT).show();
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
    public void saveBitmap(Activity context,String path, Bitmap mBitmap) {
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            Util.show(context,"失败",e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
