package com.ApplicationExc;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lingsatuo.Dialog.c;
import com.lingsatuo.Utils.Util;
import com.lingsatuo.dumpmusic.MainActivity;
import com.lingsatuo.dumpmusic.R;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

/**
 * Created by Administrator on 2016/12/15.
 */

public class BugActivity extends AppCompatActivity implements View.OnClickListener {
    AppCompatButton senderr,newversion;

    final Intent intent = new Intent();
    TextView err;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bugactivity);
        senderr = (AppCompatButton) findViewById(R.id.senderr);
        newversion = (AppCompatButton) findViewById(R.id.newversion);
        newversion.setOnClickListener(this);
        senderr.setOnClickListener(this);
        AllActivity.getInstance().addActivity(this);
        err = (TextView) findViewById(R.id.bug);
        setSupportActionBar((Toolbar) findViewById(R.id.bug_toolbar));
        err.setText(getIntent().getStringExtra("BUG"));
        PgyUpdateManager.register(this, null,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        final AppBean appBean = getAppBeanFromString(result);
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("http://www.coolapk.com/apk/com.lingsatuo.dumpmusic");
                        intent.setData(content_url);
                        Toast.makeText(BugActivity.this,"新版或许修了了呢",Toast.LENGTH_LONG).show();
                        newversion.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNoUpdateAvailable() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.senderr:
                c.sendMessage(this,getSystemVersion()+"\n"+getAPPVersion()+"\n"+err.getText().toString()+"\n\n"+getIntent().getStringExtra("BUG").toString(),null);
                break;
            case R.id.newversion:
                startActivity(intent);
                break;
        }
    }
    private String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;//获取系统版本，如4.0.2
    }
    /**
     * 获取软件版本号
     */
    private int  getAPPVersion() {
        PackageManager pm = this.getPackageManager();//得到PackageManager对象

        try {
            PackageInfo pi = pm.getPackageInfo(this.getPackageName(), 0);//得到PackageInfo对象，封装了一些软件包的信息在里面
            return pi.versionCode;//获取清单文件中versionCode节点的值
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        AllActivity.getInstance().exit();
        super.onBackPressed();
    }

}
