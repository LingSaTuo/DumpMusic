package com.lingsatuo.Dialog;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.lingsatuo.Utils.Util;
import com.lingsatuo.soundfile.CheapSoundFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class h {
    public static void PreView(final Activity context, final CheapSoundFile mSoundFile, final double startms, final double endms){
        Util.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final File file = new File(Util.getWorkPath(context) + "/缓存/" + "log" + ".dm" );
                    final int startFrame = Util.getFrame(mSoundFile,startms);
                    final int endFrame = Util.getFrame(mSoundFile,endms);
                    mSoundFile.WriteFile(file, startFrame,(endFrame - startFrame));
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                g.AudioListen(context,file.getAbsolutePath(),mSoundFile,startms,endms);
                            } catch (final IOException e) {
                                context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Util.show(context,"失败","预览错误 \n"+ e.toString());
                                }
                            });
                            }
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
}
