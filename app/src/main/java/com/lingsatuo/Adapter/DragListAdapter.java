package com.lingsatuo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lingsatuo.Utils.Util;
import com.lingsatuo.Utils.a;
import com.lingsatuo.View.DragListView;
import com.lingsatuo.dumpmusic.R;
import com.lingsatuo.dumpmusic.ThirdActivity;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
        */

public  class DragListAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    public DragListAdapter(Context context, List<String> list){
        this.list = list;
        this.context = context;
    }
    @Override
    public View getView(int item, View view, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_1, null);

        TextView textView = (TextView) view
                .findViewById(R.id.title);
        textView.setText(Util.getFileNameNoEx(new File(list.get(item)).getName()));
        TextView subtitle = (TextView) view.findViewById(R.id.singer);
        subtitle.setText(list.get(item));
        return view;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public Object getItem(int item) {
        // TODO Auto-generated method stub
        return item;
    }
    @Override
    public long getItemId(int id) {
        // TODO Auto-generated method stub
        return id;
    }
}
