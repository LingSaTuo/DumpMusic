package com.lingsatuo.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lingsatuo.Service.Player;
import com.lingsatuo.dumpmusic.MainActivity;
import com.lingsatuo.dumpmusic.R;
import com.lingsatuo.soundfile.CheapSoundFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class Util {
//    public static List<a> getSongList(Context context){
//        List<a> songs = null;
//
//        Cursor cursor = context.getContentResolver().query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                new String[] { MediaStore.Audio.Media._ID,
//                        MediaStore.Audio.Media.DISPLAY_NAME,
//                        MediaStore.Audio.Media.TITLE,
//                        MediaStore.Audio.Media.DURATION,
//                        MediaStore.Audio.Media.ARTIST,
//                        MediaStore.Audio.Media.ALBUM,
//                        MediaStore.Audio.Media.YEAR,
//                        MediaStore.Audio.Media.ALBUM_ID,
//                        MediaStore.Audio.Media.MIME_TYPE,
//                        MediaStore.Audio.Media.SIZE,
//                        MediaStore.Audio.Media.DATA },
//                MediaStore.Audio.Media.MIME_TYPE + "=? or "
//                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
//                new String[] { "audio/mpeg", "audio/x-ms-wma" }, null);
//
//        songs = new ArrayList<a>();
//
//        if (cursor.moveToFirst()) {
//
//            a song = null;
//
//            do {
//                song = new a();
//                // 文件名
//                song.a = cursor.getString(1);
//                // 歌曲名
//                song.b = cursor.getString(2);
//                // 时长
//                song.c = (cursor.getInt(3));
//                // 歌手名
//                song.d = (cursor.getString(4));
//                // 专辑名
//                song.e = (cursor.getString(5));
//                //音乐ID
//                song.j = cursor.getLong(cursor
//                        .getColumnIndex(MediaStore.Audio.Media._ID));
//                //专辑ID
//                song.k = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//                // 年代
//                if (cursor.getString(6) != null) {
//                    song.i = (cursor.getString(6));
//                } else {
//                    song.i = ("未知");
//                }
//                // 歌曲格式
//                if ("audio/mpeg".equals(cursor.getString(7).trim())) {
//                    song.f = ("mp3");
//                } else if ("audio/x-ms-wma".equals(cursor.getString(7).trim())) {
//                    song.f = ("wma");
//                }
//                // 文件大小
//                if (cursor.getString(8) != null) {
//                    float size = cursor.getInt(8) / 1024f / 1024f;
//                    song.g = ((size + "").substring(0, 4) + "M");
//                } else {
//                    song.g = ("未知");
//                }
//                // 文件路径
//                if (cursor.getString(9) != null) {
//                    song.h = (cursor.getString(9));
//                }
//                songs.add(song);
//            } while (cursor.moveToNext());
//
//            cursor.close();
//
//        }
//        return songs;
//    }
    public static List<a> getSongList(ContentResolver CR){
        Cursor cursor=CR.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<a> mp3infos=new ArrayList<a>();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        for(int i = 0;i<cursor.getCount();i++){
            a song=new a();
            cursor.moveToNext();
            long id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title =cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long album_id=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if(isMusic!=0&&duration/(1000*60)>=1){
                song.a = cursor.getString(1);
                song.b = (title);
                song.c = (duration);
                song.d = (artist);
                song.e = (album);
                // 歌曲格式
                if ("audio/mpeg".equals(cursor.getString(7).trim())) {
                    song.f = ("mp3");
                } else if ("audio/x-ms-wma".equals(cursor.getString(7).trim())) {
                    song.f = ("wma");
                }
                song.g = (size);
                song.h = (url);
                // 年代
                if (cursor.getString(6) != null) {
                    song.i = (cursor.getString(6));
                } else {
                    song.i = ("未知");
                }
                song.j = (id);
                song.k = (album_id);
                mp3infos.add(song);
            }
        }

        a song=new a();
        song.b = ("");
        song.c = -100;
        mp3infos.add(song);
        return mp3infos;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static Bitmap byteToBitmap(byte[] imgByte) {
        if (imgByte==null)
            return null;
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize =0;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static int getFrame(CheapSoundFile mSoundFile,double startms){
        int mSampleRate;
        int mSamplesPerFrame;

        mSampleRate = mSoundFile.getSampleRate();
        mSamplesPerFrame = mSoundFile.getSamplesPerFrame();

        final int Frame = (int)(1.0 * startms/1000 * mSampleRate / mSamplesPerFrame + 0.5);
        System.out.println(Frame);
        return Frame;
    }
    public static double getErr(CheapSoundFile mSoundFile,double startms){
        int mSampleRate;
        int mSamplesPerFrame;

        mSampleRate = mSoundFile.getSampleRate();
        mSamplesPerFrame = mSoundFile.getSamplesPerFrame();

        final double Frame = (1.0 * startms/1000 * mSampleRate / mSamplesPerFrame + 0.5);
        double err = Frame - getFrame(mSoundFile,startms)*1.0;
        //(err - 0.5)*mSamplesPerFrame*(1000.0/mSampleRate);
        return err-0.5;
    }


    public static void SaveSound(final Activity context,String mname, final CheapSoundFile mSoundFile, double startms, double endms){
        com.lingsatuo.Dialog.b.SaveSound(context,mname,mSoundFile,startms,endms);
    }
    private Context context;
    public static String getWorkPath(Context c){
        return Environment.getExternalStorageDirectory().toString()+"/Dump Music";
    }
    public static void setWork(Context c){
        String[] files = {"",
                "/专辑",
                "/剪切",
                "/合成",
                "/提取",
                "/缓存"};
        for (String file : files){
            File f = new File(getWorkPath(c)+file);
            if(!f.exists()){
                f.mkdirs();
            }
        }
    }
    public static void SaveSound(final Activity context, final String from, String mname){
        com.lingsatuo.Dialog.a.SaveSound(context,from,mname);
    }

    private static ProgressDialog pd;
    public static void show(final Activity context){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd = new ProgressDialog(context);
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setTitle("等待");
                pd.setMessage("操作中.....");
                pd.show();
            }
        });
    }
    public static void dissmiss(){
        if (pd!=null){
            pd.dismiss();
        }
    }
    public static void show(final Activity context, final String title, final String mess){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)
                        .setMessage(mess)
                        .setTitle(title)
                        .setPositiveButton("确定",null).show();
            }
        });
    }

    public static void setRing(Context activity,File file){
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
//    values.put(MediaStore.MediaColumns.TITLE, file.getName());
//    values.put(MediaStore.MediaColumns.SIZE, file.length());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
//    values.put(MediaStore.Audio.Media.ARTIST, "Madonna");
//    values.put(MediaStore.Audio.Media.DURATION, 230);
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_MUSIC, false);
            Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
            Uri newUri = activity.getContentResolver().insert(uri, values);
            RingtoneManager.setActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_RINGTONE, newUri);
    }




    private static int port1 = 25;  //smtp协议使用的是25号端口
    private static String server1 = "smtp.163.com"; // 发件人邮件服务器
    private static String user1 ="15968011381@163.com";   // 使用者账号
    private static String password1 = "f00000000"; //使用者密码

    //构造发送邮件帐户的服务器，端口，帐户，密码
    public static  void MainUtils(String server, int port, String user, String passwd) {
        port1 = port;
        user1 = user;
        password1 = passwd;
        server1 = server;
    }

    public static void sendEmail(String email, String subject, String body,
                                 List<String> paths) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", server1);
        props.put("mail.smtp.port", String.valueOf(port1));
        props.put("mail.smtp.auth", "true");
        Transport transport = null;
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage msg = new MimeMessage(session);
        transport = session.getTransport("smtp");
        transport.connect(server1, user1, password1);    //建立与服务器连接
        msg.setSentDate(new Date());
        InternetAddress fromAddress = null;
        fromAddress = new InternetAddress(user1);
        msg.setFrom(fromAddress);
        InternetAddress[] toAddress = new InternetAddress[1];
        toAddress[0] = new InternetAddress(email);
        msg.setRecipients(Message.RecipientType.TO, toAddress);
        msg.setSubject(subject, "UTF-8");            //设置邮件标题
        MimeMultipart multi = new MimeMultipart();   //代表整个邮件邮件
        BodyPart textBodyPart = new MimeBodyPart();  //设置正文对象
        textBodyPart.setText(body);                  //设置正文
        multi.addBodyPart(textBodyPart);//添加正文到邮件
        if(paths!=null) {
            for (String path : paths) {
                FileDataSource fds = new FileDataSource(path);   //获取磁盘文件
                BodyPart fileBodyPart = new MimeBodyPart();                       //创建BodyPart
                fileBodyPart.setDataHandler(new DataHandler(fds));           //将文件信息封装至BodyPart对象
                String fileNameNew = MimeUtility.encodeText(fds.getName(),
                        "utf-8", null);      //设置文件名称显示编码，解决乱码问题
                fileBodyPart.setFileName(fileNameNew);  //设置邮件中显示的附件文件名
                multi.addBodyPart(fileBodyPart);        //将附件添加到邮件中
            }
        }
        msg.setContent(multi);                      //将整个邮件添加到message中
        msg.saveChanges();
        transport.sendMessage(msg, msg.getAllRecipients());  //发送邮件
        transport.close();
    }


    static MediaExtractor mediaExtractor;
    static MediaMuxer mediaMuxer;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void muxerAudio(String file_in, String file_out) throws Exception {
        String by = "bai du tie ba @ 7eu7d7 ";
        String url = "http://tieba.baidu.com/p/4809716863";
        mediaExtractor = new MediaExtractor();
        MediaPlayer md=new MediaPlayer();
            md.setDataSource(file_in.toString());
            md.prepare();
        int audioIndex = -1;//音频通道
        int count=0;//已转换的时长
        int size=md.getDuration();//获取视频时长
        try {
            mediaExtractor.setDataSource(file_in.toString());//设置视频路径，可以是网络链接
            int trackCount = mediaExtractor.getTrackCount();//获取通道总数
            for (int i = 0; i < trackCount; i++) {
                MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
                if (trackFormat.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                    audioIndex = i;
                }//获取音频通道
            }
            if (audioIndex==-1){
                throw new Exception("声道为0，此视频可能无声音");
            }
            mediaExtractor.selectTrack(audioIndex);//切换到音频通道
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(audioIndex);
            mediaMuxer = new MediaMuxer(file_out.toString(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            int writeAudioIndex = mediaMuxer.addTrack(trackFormat);
            mediaMuxer.start();
            ByteBuffer byteBuffer = ByteBuffer.allocate(trackFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE));
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            long stampTime = 0;
            //获取相邻帧之间的间隔时间
            {
                mediaExtractor.readSampleData(byteBuffer, 0);
                if (mediaExtractor.getSampleFlags() == MediaExtractor.SAMPLE_FLAG_SYNC) {
                    mediaExtractor.advance();
                }
                mediaExtractor.readSampleData(byteBuffer, 0);
                long secondTime = mediaExtractor.getSampleTime();
                mediaExtractor.advance();
                mediaExtractor.readSampleData(byteBuffer, 0);
                long thirdTime = mediaExtractor.getSampleTime();
                stampTime = Math.abs(thirdTime - secondTime);
            }
            mediaExtractor.unselectTrack(audioIndex);
            mediaExtractor.selectTrack(audioIndex);
            //开始提取音频
            while (true) {
                int readSampleSize = mediaExtractor.readSampleData(byteBuffer, 0);
                if (readSampleSize < 0) {
                    break;
                }
                mediaExtractor.advance();//移动到下一帧
                bufferInfo.size = readSampleSize;
                bufferInfo.flags = mediaExtractor.getSampleFlags();
                bufferInfo.offset = 0;
                bufferInfo.presentationTimeUs += stampTime;
                count+=stampTime;
                mediaMuxer.writeSampleData(writeAudioIndex, byteBuffer, bufferInfo);
            }
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaExtractor.release();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
    public static void FileCopy(File s,File t) throws IOException {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        FileChannel in = null;
        FileChannel out = null;
        if(s.isFile()){
            fis = new FileInputStream(s);
            fos = new FileOutputStream(t);
            in = fis.getChannel();
            out = fos.getChannel();
            in.transferTo(0,in.size(),out);
            fis.close();
            in.close();
            fos.close();
            out.close();
        }else{
            if(!t.exists()){
                t.mkdirs();
            }
            for(File file:s.listFiles()){
                FileCopy(file,new File(t.getPath()+"/"+file.getName()));
            }
        }
    }
    public static List<a> SeacrhDate(String key){
        List<a> listnow = new ArrayList<a>();
        for(a a : MainActivity.all){
            if(a.b.toLowerCase().contains(key.toLowerCase())){
                listnow.add(a);
            }
        }
            a song=new a();
            song.b = ("没找到？");
            song.c = -100;
            listnow.add(song);
        return listnow;
    }

    public static void saveDataToFile(Context context,String name,String Str) throws IOException {
        File file = new File(context.getFilesDir().toString()+"/"+name);
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        try {
            fos.write(Str.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    //读取文件内容
    public  static String readDataFromFile(Context context,String name) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        String line=null;
        sb = new StringBuffer();
        fis = context.openFileInput(name);
        isr = new InputStreamReader(fis);
        br = new BufferedReader(isr);
            while((line=br.readLine())!=null){
                sb.append(line+(""));
            }
        br.close();;
        isr.close();
        fis.close();
        return sb.toString();
    }

    public static boolean istruepath(String name){
        String[] strs ={":","\\","/","*","?",",","<",">","|","\""};
        for (String str : strs){
            if(name.contains(str))return false;
        }
        return true;
    }
}
