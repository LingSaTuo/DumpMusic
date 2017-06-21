package com.lingsatuo.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.ApplicationExc.AllActivity;
import com.lingsatuo.Utils.Util;
import com.lingsatuo.Utils.a;
import com.lingsatuo.View.BackgroundProgress;
import com.lingsatuo.dumpmusic.MainActivity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by Administrator on 2017/4/3.
 */

public class Player extends Service implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    public final IBinder binder = (IBinder) new MyBinder();
    public static List<a> list ;
    public static int pos = 0;
    public static int playtype = 0;
    String prompt = "If you really need the source code, please contact me at 15960811381@163.com";


    @Override
    public void onCreate() {
        mp.setOnCompletionListener(this);
        if(MainActivity.sb_0!=null) {
            MainActivity.sb_0.setOnSeekBarChangeListener(this);
        }
        super.onCreate();
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        Log.i("Service", "ExampleService-onDestroy");
        AllActivity.getInstance().exit();
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(MainActivity.bp!=null){
            MainActivity.bp.showDown();
            MainActivity.bp = null;
            if(list!=null) {
                switch (playtype){
                    case 0:
                        next();
                        break;
                    case 1:
                        //preMusic(list.get(pos),pos);
                        break;
                }
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(mp!=null)
        mp.seekTo(seekBar.getProgress());
    }

    public class MyBinder extends Binder {
        public Player getService() {
            return Player.this;
        }
    }

    public static MediaPlayer mp = new MediaPlayer();


    public void playOrPause() {
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }
    public void stop() {
        if(mp != null) {
            mp.stop();
        }
    }
    public void preMusic(String path) {
        stop();
        try {
            mp.reset();
            mp.setDataSource(path);
            mp.prepare();
            mp.seekTo(0);
            mp.start();
            MainActivity.sb_0.setMax(mp.getDuration());
            MainActivity.mmr.setDataSource(path);
//                    byte[] bytes = MainActivity.mmr.getEmbeddedPicture();
//                    if (bytes==null){
//                        MainActivity.cl.setBackgroundColor(0xffffffff);
//                    }else{
//                        Bitmap bit = Util.byteToBitmap(bytes);
//                        MainActivity.blur(bit,MainActivity.cl);
//                    }
        } catch (Exception e) {
            System.out.println(getErr(e));
        }
    }
    public void preMusic(a fi,int pos){
        if(MainActivity.bp!=null&&pos!=this.pos){
            MainActivity.bp.showDown();
            MainActivity.bp = null;
        }
        this.pos = pos;
        if(MainActivity.toolbar!=null){
            MainActivity.toolbar.setSubtitle(fi.b);
        }
        this.preMusic(fi.h);
        MainActivity.bp = fi.pb;
        if (MainActivity.bp!=null) {
            MainActivity.bp.setVisibility(View.VISIBLE);
            MainActivity.bp.setMax(mp.getDuration());
        }else{

        }
       MainActivity.adapter.notifyDataSetChanged();
    }
    public void next(){
        pos++;
      if(pos>=list.size())
          pos = 0;
        preMusic(list.get(pos),pos);
    }
    public void up(){
        pos--;
        if(pos<0)
            pos = list.size()-1;
        preMusic(list.get(pos),pos);
    }
    static StringWriter sw= new StringWriter();
    static PrintWriter pw=new PrintWriter(sw);
    public static String getErr(Exception e){
        sw= new StringWriter();
        pw=new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }


}
