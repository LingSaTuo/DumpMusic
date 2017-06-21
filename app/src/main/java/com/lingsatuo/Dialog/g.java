package com.lingsatuo.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.widget.SeekBar;

import com.lingsatuo.Service.Player;
import com.lingsatuo.dumpmusic.R;
import com.lingsatuo.soundfile.CheapSoundFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class g {
    static AlertDialog ad;
    static SeekBar sb;
    static Context a;
    static MediaPlayer mp;
    static String path2;
    public static void  AudioListen(Context activity, String path) throws IOException {
        a = activity;
        mp= new MediaPlayer();
        mp.reset();
        mp.setDataSource(path);
        mp.prepare();
        mp.seekTo(0);
        mp.start();
        show();
        Player.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(ad!=null)
                    ad.dismiss();
            }
        });
    }
    public static void  AudioListen(Activity context,String path,CheapSoundFile mSoundFile,double startFrame,double endFrame) throws IOException {
        path2 = path;
        mp= new MediaPlayer();
        mp.reset();
        mp.setDataSource(path);
        mp.prepare();
        mp.seekTo(0);
        if (Player.mp.isPlaying()){
            Player.mp.pause();
        }
        mp.start();
        show2(context,mSoundFile,startFrame,endFrame);
    }
    public static void show(){
        ad = new AlertDialog.Builder(a)
                .setTitle("试听")
                .setView(R.layout.listen)
                .setPositiveButton("关闭",null)
                .show();
        sb = (SeekBar) ad.getWindow().findViewById(R.id.seekbar);
        sb.setMax(mp.getDuration());
        ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mp.stop();
            }
        });
        ad.setCanceledOnTouchOutside(false);
        handler.post(runnable);
    }


    public static void show2(final Activity context, final CheapSoundFile mSoundFile, final double startFrame, final double endFrame){
        ad = new AlertDialog.Builder(context)
                .setTitle("预览")
                .setView(R.layout.listen)
                .setNegativeButton("关闭",null)
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    b.SaveSound(context,"真实预览",mSoundFile,startFrame,endFrame);
                    }
                })
                .show();
        sb = (SeekBar) ad.getWindow().findViewById(R.id.seekbar);
        sb.setMax(mp.getDuration());
        ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mp.stop();
                File f = new File(path2);
                if (f.exists())f.delete();
            }
        });
        ad.setCanceledOnTouchOutside(false);
        handler.post(runnable);
    }


    public static android.os.Handler handler = new android.os.Handler();
    public static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sb.setProgress(mp.getCurrentPosition());
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mp.seekTo(seekBar.getProgress());
                }
            });
            handler.postDelayed(runnable, 20);
        }
    };
}
