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

import com.lingsatuo.Service.Player;
import com.lingsatuo.Utils.Util;
import com.lingsatuo.soundfile.CheapSoundFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class b {
    private String mypath = "/剪切/";
    public static void setPath(){}
    public static void SaveSound(final Activity context, String mname, final CheapSoundFile mSoundFile, double startms, double endms){
    Util.setWork(context);
       final String type  = mSoundFile.getFiletype().toLowerCase();
        final EditText name = new EditText(context);
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        final TextView tv = new TextView(context);
        tv.setText(Util.getWorkPath(context)+"/剪切/"+mname+"."+type);
        ll.addView(tv);
        ll.addView(name);
        name.setSingleLine(true);
    name.setText(Util.getFileNameNoEx(mname));
    final AlertDialog AD =   new AlertDialog.Builder(context)
            .setTitle("保存")
            .setView(ll)
            .setNegativeButton("取消",null)
            .setPositiveButton("保存",null)
            .show();
    final int startFrame = Util.getFrame(mSoundFile,startms);
    final int endFrame = Util.getFrame(mSoundFile,endms);
    final Button qd = AD.getButton(DialogInterface.BUTTON_POSITIVE);
    {
        if(new File(Util.getWorkPath(context)+"/剪切/"+name.getText().toString()+"."+type).exists()){
        qd.setText("覆盖保存");
    }else{
        qd.setText("保存");
    }
        if(Player.list.get(Player.pos).h.equals(Util.getWorkPath(context)+"/剪切/"+name.getText().toString()+"."+type)){
            qd.setText("文件路径冲突");
            qd.setEnabled(false);
        }

        if(!Util.istruepath(mname)){
            qd.setText("不合法的名字");
            qd.setEnabled(false);
            name.setTextColor(Color.RED);
        }
    }
    name.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().equals(""))
                qd.setEnabled(false);
            else {
                qd.setEnabled(true);

                tv.setText(Util.getWorkPath(context)+"/剪切/"+s.toString()+"."+type);
                {
                    if(new File(Util.getWorkPath(context)+"/剪切/"+name.getText().toString()+"."+type).exists()){
                    qd.setText("覆盖保存");
                }else{
                    qd.setText("保存");
                }

                    if((Player.list.get(Player.pos).h.toLowerCase()).equals((Util.getWorkPath(context)+"/剪切/"+name.getText().toString()+"."+type).toLowerCase())){
                        qd.setText("文件路径冲突");
                        qd.setEnabled(false);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final File file = new File(Util.getWorkPath(context) + "/剪切/" + name.getText().toString() + "." + type);
                        mSoundFile.WriteFile(file, startFrame, (int)(endFrame - startFrame));
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show();
                                new SavaAS(context,file);
                            }
                        });
                    } catch (final IOException e) {
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Util.show(context,"错误","这不是他该有的格式，他的拓展名不应该是  "+mSoundFile.getFiletype()+"\n"+e.toString());
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
