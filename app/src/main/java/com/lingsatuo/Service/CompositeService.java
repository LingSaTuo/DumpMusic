package com.lingsatuo.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.lingsatuo.Thp.AudioCodec;
import com.lingsatuo.Utils.ACA;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class CompositeService extends Service {
    public static AudioCodec audioCodec=AudioCodec.newInstance();
    public static boolean isfinsh = true;
    @Override
    public void onCreate() {
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
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void setAudioC(String path, String path2, final ACA aca,int kbps) throws Exception {
        if (!isfinsh)
            throw new Exception("有未完成的任务 ");
        isfinsh = false;
        audioCodec.setEncodeType(MediaFormat.MIMETYPE_AUDIO_MPEG);
        audioCodec.setIOPath(path, path2);
        audioCodec.prepare();
        audioCodec.startAsync();
        audioCodec.setOnCompleteListener(new AudioCodec.OnCompleteListener() {
            @Override
            public void completed() {
                audioCodec.release();
                isfinsh = true;
            }
        });
        audioCodec.setOnProgressListener(new AudioCodec.OnProgressListener() {
            @Override
            public void progress(long progress, long maxsize, long retime) {
                aca.pb.setMax((int) maxsize);
                aca.pb.setProgress((int) progress);
                aca.curr.setText(progress+"");
                aca.max.setText(maxsize+"");
                aca.retime.setText(retime+"");
            }
        });
    }
}
