package com.lingsatuo.Dialog;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lingsatuo.Service.Player;
import com.lingsatuo.dumpmusic.R;
import com.lingsatuo.dumpmusic.SecondActivity;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class SavaAS{
    AlertDialog ad ;
    TextView runas;
    Spinner sr;
    String[] type = new String[]{"不操作","铃声","通知音","闹铃"};
   int pos = 0;
    AppCompatCheckBox gx;
    public SavaAS(final Context context, final File f){
        ad = new AlertDialog.Builder(context)
                .setView(R.layout.save_as)
                .setIcon(R.mipmap.settings)
                .setTitle("设置为 ")
                .setPositiveButton("确定",null)
                .setNegativeButton("试听",null)
                .setNeutralButton("取消",null)
                .show();
        ad.setCanceledOnTouchOutside(true);
        runas = (TextView) ad.getWindow().findViewById(R.id.runas);
        gx = (AppCompatCheckBox) ad.getWindow().findViewById(R.id.gx);
        sr = (Spinner) ad.getWindow().findViewById(R.id.run_type);
        sr.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,type));
        sr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ad.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(Player.mp!=null&&Player.mp.isPlaying())
                    Player.mp.pause();
                    if(SecondActivity.video!=null&&SecondActivity.video.isPlaying()){
                        SecondActivity.video.pause();
                    }
                    g.AudioListen(context,f.getAbsolutePath());
                } catch (IOException e) {
                    Toast.makeText(context,"试听出现错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ad.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pos){
                    case 0:
                        ad.dismiss();
                        break;
                    case 1:
                        try {
                            setMyRingtone(context,f.getAbsolutePath());
                            Toast.makeText(context,"完成",Toast.LENGTH_SHORT).show();
                            ad.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(context,"失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        try {
                            setMyNotification(context,f.getAbsolutePath());
                            Toast.makeText(context,"完成",Toast.LENGTH_SHORT).show();
                            ad.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(context,"失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        try {
                            setMyAlarm(context,f.getAbsolutePath());
                            Toast.makeText(context,"完成",Toast.LENGTH_SHORT).show();
                            ad.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(context,"失败",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                if(gx.isChecked()){
                    setVoice(context,f.getAbsolutePath());
                }
            }
        });
    }


    public static void setMyRingtone(Context context,String path) throws Exception {
        try {
            File sdfile = new File(path);
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_MUSIC, false);

            Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
            Uri newUri = context.getContentResolver().insert(uri, values);
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
        }catch(Exception e){
                 throw new Exception(e);
        }
    }

    //设置--提示音的具体实现方法
    public void setMyNotification(Context context,String path) throws Exception {
            try{
        File sdfile = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
        Uri newUri = context.getContentResolver().insert(uri, values);

        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, newUri);
            }catch(Exception e){
                throw new Exception(e);
            }
    }
    //设置--闹铃音的具体实现方法
    public void setMyAlarm(Context context,String path) throws Exception {
        try{
        File sdfile = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, true);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
        Uri newUri = context.getContentResolver().insert(uri, values);
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM, newUri);
        }catch(Exception e){
            throw new Exception(e);
        }
    }
    private void setVoice(Context context,String path2) {

        ContentValues cv = new ContentValues();
        Uri newUri = null;
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(path2);
        Cursor cursor = context.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{path2}, null);

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);

            cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);

            cv.put(MediaStore.Audio.Media.IS_ALARM, true);
            cv.put(MediaStore.Audio.Media.IS_MUSIC, false);
            String _id = cursor.getString(0);
            context.getContentResolver().update(uri, cv, MediaStore.MediaColumns.DATA + "=?", new String[]{path2});

            newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));
            RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALL, newUri);
        }
        //播放铃声
               // Ringtone rt = RingtoneManager.getRingtone(this, newUri);
                //rt.play();
    }
}
