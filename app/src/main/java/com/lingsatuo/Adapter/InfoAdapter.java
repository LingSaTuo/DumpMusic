package com.lingsatuo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lingsatuo.Utils.Util;
import com.lingsatuo.Utils.a;
import com.lingsatuo.View.BackgroundProgress;
import com.lingsatuo.dumpmusic.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class InfoAdapter extends BaseAdapter{
    private Context context;
    private List<a> list;
    private LayoutInflater inflater;
    private a item;
    private ArrayList<View> views;
    public InfoAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public void setDate(List<a> list){
        this.list = list;
        views = new ArrayList<>();
        for(int a  = 0 ;a < list.size() ; a ++){
            views.add(null);
        }
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        item = list.get(position);
        View view = views.get(position);
        if(view==null) {
            if(position != list.size()-1) {
                convertView = inflater.inflate(R.layout.item_0, null);
                TextView title = (TextView) convertView.findViewById(R.id.title);
                TextView singer = (TextView) convertView.findViewById(R.id.singer);
                RoundedImageView rvi = (RoundedImageView) convertView.findViewById(R.id.album);
                String path = Environment.getExternalStorageDirectory() + "/abc.png";
                item.pb = (BackgroundProgress) convertView.findViewById(R.id.background_progress);
                title.setText(item.b);
                singer.setText(item.d);
                File f = new File(path);
                if (f.exists()) {
                    f.delete();
                }
            }else{
                convertView = inflater.inflate(R.layout.last_item, null);
                item.pb = (BackgroundProgress) convertView.findViewById(R.id.background_progress_0);
            }
            view = convertView;
            views.set(position, view);
    }
    else{
            view = views.get(position);
        }
        return view;
    }
}
