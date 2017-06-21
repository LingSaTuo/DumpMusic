package com.lingsatuo.AudioSynthesis;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.lingsatuo.Dialog.SavaAS;
import com.lingsatuo.Service.Player;
import com.lingsatuo.Utils.Util;
import com.lingsatuo.dumpmusic.MainActivity;
import com.lingsatuo.soundfile.CheapSoundFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

public class SynthesisMP3 extends AudioSynthesis {
    public static String type = "MP3";

    private static String fenLiData(Context context,String path) throws IOException {
        File file = new File(path);// 原文件
        File file1 = new File(Util.getWorkPath(context)+"/缓存/" + "01");// 分离ID3V2后的文件,这是个中间文件，最后要被删除
        File file2 = new File(Util.getWorkPath(context)+"/缓存/"+new File(path).getName() + "001");// 分离id3v1后的文件
        RandomAccessFile rf = new RandomAccessFile(file, "rw");// 随机读取文件
        FileOutputStream fos = new FileOutputStream(file1);
        byte ID3[] = new byte[3];
        rf.read(ID3);
        String ID3str = new String(ID3);
        // 分离ID3v2
        if (ID3str.equals("ID3")) {
            rf.seek(6);
            byte[] ID3size = new byte[4];
            rf.read(ID3size);
            int size1 = (ID3size[0] & 0x7f) << 21;
            int size2 = (ID3size[1] & 0x7f) << 14;
            int size3 = (ID3size[2] & 0x7f) << 7;
            int size4 = (ID3size[3] & 0x7f);
            int size = size1 + size2 + size3 + size4 + 10;
            rf.seek(size);
            int lens = 0;
            byte[] bs = new byte[1024*4];
            while ((lens = rf.read(bs)) != -1) {
                fos.write(bs, 0, lens);
            }
            fos.close();
            rf.close();
        } else {// 否则完全复制文件
            int lens = 0;
            rf.seek(0);
            byte[] bs = new byte[1024*4];
            while ((lens = rf.read(bs)) != -1) {
                fos.write(bs, 0, lens);
            }
            fos.close();
            rf.close();
        }
        RandomAccessFile raf = new RandomAccessFile(file1, "rw");
        byte TAG[] = new byte[3];
        raf.seek(raf.length() - 128);
        raf.read(TAG);
        String tagstr = new String(TAG);
        if (tagstr.equals("TAG")) {
            FileOutputStream fs = new FileOutputStream(file2);
            raf.seek(0);
            byte[] bs=new byte[(int)(raf.length()-128)];
            raf.read(bs);
            fs.write(bs);
            raf.close();
            fs.close();
        } else {// 否则完全复制内容至file2
            FileOutputStream fs = new FileOutputStream(file2);
            raf.seek(0);
            byte[] bs = new byte[1024*4];
            int len = 0;
            while ((len = raf.read(bs)) != -1) {
                fs.write(bs, 0, len);
            }
            raf.close();
            fs.close();
        }
        if (file1.exists())// 删除中间文件
        {
            file1.delete();

        }
        return file2.getAbsolutePath();
    }
static int aa  = 0;
    public static void Systhesis(final Activity context, final List<String> paths, final String name){
        aa = 0;
        Util.show(context);
        final List<String> newpaths = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CheapSoundFile mSoundFile ;
                    List<String> newpaths = new ArrayList<String>();
                    for(int a  = 0 ;a <paths.size();a++){
                        aa=a;
                        mSoundFile = CheapSoundFile.create(paths.get(a),null);
                        File file = new File(Util.getWorkPath(context) + "/缓存/" + a+".log");
                        mSoundFile.WriteFile(file, Util.getFrame(mSoundFile,25), (int)(Util.getFrame(mSoundFile, MainActivity.dua.get(a)) - Util.getFrame(mSoundFile,25)));
                        newpaths.add(file.getAbsolutePath());
                    }
                    heBingMp3(context,newpaths,name);
                    File[] files = new File(Util.getWorkPath(context) + "/缓存/").listFiles();
                    for (File f : files){
                        f.delete();
                    }
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"完成",Toast.LENGTH_LONG).show();
                            new SavaAS(context,new File(Util.getWorkPath(context)+"/合成/"+name+"."+type.toLowerCase()));
                        }
                    });
                } catch (IOException e) {
                    Util.show(context,"失败","发生一个错误导致合成中断 ,发生错误的音频 \n位置(0开始)  :"+ aa +"\n名称 :"+new File(MainActivity.addlist.get(aa)).getName()+"\n"+e.toString());
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









    public static String heBingMp3(Context context,List<String> paths, String name) throws IOException{
        String luJing= Util.getWorkPath(context)+"/合成/"+name+"."+type;
        File file2=new File(luJing);
        FileOutputStream out;
        FileInputStream in = null;
        out=new FileOutputStream(file2);
        List<String> fili = new ArrayList<String>();
        List<File> files = new ArrayList<File>();
        //字节数组
        for(int a  = 0 ;a < paths.size();a++) {
            String fenLiData = fenLiData(context,paths.get(a));
            fili.add(fenLiData);
            File file=new File(fenLiData);
            files.add(file);
        }

        //输入流对象一个
        byte bs[]=new byte[1024*4];
        for (File file : files) {
            in = new FileInputStream(file);
            int len = 0;
            while ((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
            in.close();
        }
        out.close();
        out = new FileOutputStream(file2, true);//在文件尾打开输出流
        in.close();
        out.close();
        return file2.getAbsolutePath();
    }
}
