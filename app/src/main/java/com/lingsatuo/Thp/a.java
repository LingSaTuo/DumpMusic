package com.lingsatuo.Thp;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lingsatuo.AudioSynthesis.SynthesisMP3;
import com.lingsatuo.Service.CompositeService;
import com.lingsatuo.Utils.ACA;
import com.lingsatuo.Utils.Util;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public class a {
    public static void SaveSound(final Activity context, final String path, final ACA aca, final int kbps){
        final String type = "AAC";
        Util.setWork(context);
        final EditText name = new EditText(context);
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        final TextView tv = new TextView(context);
        tv.setText(Util.getWorkPath(context)+"/转码/"+"."+type);
        ll.addView(tv);
        ll.addView(name);
        name.setSingleLine(true);
        final AlertDialog AD =   new AlertDialog.Builder(context)
                .setTitle("保存")
                .setView(ll)
                .setNegativeButton("取消",null)
                .setPositiveButton("保存",null)
                .show();
        final Button qd = AD.getButton(DialogInterface.BUTTON_POSITIVE);
        {if(new File(Util.getWorkPath(context)+"/转码/"+name.getText().toString()+"."+type).exists()){
            qd.setText("覆盖保存");
        }else{
            qd.setText("保存");
        }}
        if(name.getText().length()<1)
            qd.setEnabled(false);
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

                    tv.setText(Util.getWorkPath(context)+"/转码/"+s.toString()+"."+type);
                    {if(new File(Util.getWorkPath(context)+"/转码/"+name.getText().toString()+"."+type).exists()){
                        qd.setText("覆盖保存");
                    }else{
                        qd.setText("保存");
                    }}

                }
            }
        });
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AD.dismiss();
                try {
                    CompositeService.setAudioC(Util.getWorkPath(context)+"/转码/"+name.getText().toString()+"."+type,path,aca,kbps);
                } catch (Exception e) {
                    Util.show(context,"失败",e.toString());
                }
            }
        });
    }
}
