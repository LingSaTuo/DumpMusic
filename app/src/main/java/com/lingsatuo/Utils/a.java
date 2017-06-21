package com.lingsatuo.Utils;

import com.lingsatuo.View.BackgroundProgress;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class a {
    public String a ="";//文件名字
    public String b = "";//标题
    public long c = 0;//时长
    public String d = "";//歌唱者
    public String e = "";//album
    public String f  = "";//类型
    public long g = 0;//大小
    public String h = "";//文件路径
    public byte[] icon ;
    public String i = "";//年代
    public long j=0;//音乐ID
    public long k = 0;//专辑ID
    public BackgroundProgress pb;
    public JSONObject toJSONObject() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("Song",a);
        obj.put("Title",b);
        obj.put("Duration",c);
        obj.put("Singer",d);
        obj.put("Album",e);
        obj.put("Year",i);
        obj.put("Type",f);
        obj.put("Size",g);
        obj.put("FileUrl",h);
        obj.put("SongID",j);
        obj.put("AlbumID",k);
        return obj;
    }
}
