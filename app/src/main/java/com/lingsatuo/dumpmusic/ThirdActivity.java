package com.lingsatuo.dumpmusic;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.lingsatuo.Adapter.DragListAdapter;
import com.lingsatuo.Dialog.e;
import com.lingsatuo.Service.Player;
import com.lingsatuo.Utils.Util;
import com.lingsatuo.View.DragListView;
import com.lingsatuo.soundfile.CheapSoundFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {
    DragListView cdlv;
    CheapSoundFile mSoundFile = null;
    String type = "MP3";
    static DragListAdapter dla;
    Button inadd;
    static ThirdActivity inthis = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.third_toolbar));

        inthis = this;
        String path = getIntent().getStringExtra("Path");
        try {
            mSoundFile = CheapSoundFile.create(path,null);
        } catch (IOException e) {}
        if(mSoundFile!=null&&mSoundFile.getFiletype().equals("MP3")){
            if(mSoundFile.getAvgBitrateKbps()!=192&&mSoundFile.getAvgBitrateKbps()!=320){
                Toast.makeText(this,"该音频Kbps不是常规值",Toast.LENGTH_LONG).show();
            }
            if(MainActivity.addlist==null)return;
            if(MainActivity.addlist.contains(path)){
                show_0(path);
            }else {
                MainActivity.addlist.add(path);
                MainActivity.dua.add(Player.list.get(Player.pos).c);
            }
            type = mSoundFile.getFiletype();
        }else{
            if (!path.equals(" "))
            Util.show(this,"错误的格式","当前仅支持MP3文件的合成，不支持你所选的格式");
        }
        cdlv = (DragListView) findViewById(R.id.cdlv);
        dla = new DragListAdapter(this,MainActivity.addlist);
        cdlv.setAdapter(dla);
        cdlv.deferNotifyDataSetChanged();
        cdlv.setExchangeDataListener(new DragListView.ExchangeDataListener() {
            //交换数据的操作
            public void setExchangeData(int from, int to) {
                // Log.i("setExchangeData","hello");
                String item = MainActivity.addlist.get(from);
                MainActivity.addlist.remove(item);
                MainActivity.addlist.add(to, item);
                long dua = MainActivity.dua.get(from);
                MainActivity.dua.remove(dua);
                MainActivity.dua.add(to,dua);
                dla.notifyDataSetChanged();
                // list.remove(dragPosition);
            }
        });
        cdlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            show2(position);
            }
        });
        inadd = (Button) findViewById(R.id.inadd);
        inadd.setOnClickListener(this);
    }
    private void show_0(final String path){
        new AlertDialog.Builder(this)
                .setTitle("重复的元素")
                .setMessage("当前已经包含了  "+new File(path).getName()+"  你希望再次添加吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.addlist.add(path);
                        MainActivity.dua.add(Player.list.get(Player.pos).c);
                        dla.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inadd:
                if (MainActivity.addlist==null)return;
                if(MainActivity.addlist.size()<2){
                    Toast.makeText(this,"这根本不用合成，数量必须大于 1",Toast.LENGTH_LONG).show();
                    return;
                }
                e.SaveSound(this,MainActivity.addlist,type.toLowerCase());
                break;
        }
    }

    @Override
    protected void onResume() {
        getSupportActionBar().setSubtitle("点击删除  拖动排序");
        super.onResume();
    }

    public static void show2(final int pos){
        new AlertDialog.Builder(inthis)
                .setTitle("移除")
                .setMessage("需要移除  :"+new File(MainActivity.addlist.get(pos)).getName()+"  吗")
                .setPositiveButton("移除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = MainActivity.addlist.get(pos);
                        MainActivity.addlist.remove(item);
                        long dua = MainActivity.dua.get(pos);
                        MainActivity.dua.remove(dua);
                        dla.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("取消",null).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clearn) {
            if (MainActivity.addlist==null)return true;
            for(int pos = 0; pos<MainActivity.addlist.size();pos++) {
                String item2 = MainActivity.addlist.get(pos);
                MainActivity.addlist.remove(item2);
                long dua = MainActivity.dua.get(pos);
                MainActivity.dua.remove(dua);
            }
            dla.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
