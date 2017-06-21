package com.lingsatuo.dumpmusic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.ApplicationExc.AllActivity;
import com.lingsatuo.Adapter.InfoAdapter;
import com.lingsatuo.Dialog.help;
import com.lingsatuo.Dialog.i;
import com.lingsatuo.Service.Player;
import com.lingsatuo.Utils.AnimateUtil;
import com.lingsatuo.Utils.FastBlur;
import com.lingsatuo.Utils.UriUtils;
import com.lingsatuo.Utils.Util;
import com.lingsatuo.Utils.a;
import com.lingsatuo.View.BackgroundProgress;
import com.lingsatuo.View.floatingactionbutton.FloatingActionButton;
import com.lingsatuo.View.floatingactionbutton.FloatingActionsMenu;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener,SearchView.OnQueryTextListener {
    Player musicService;
    public static BackgroundProgress bp;
    public static List<a> all;
    TimerTask timerTask;
    Timer timer;
    Handler han;
    public static InfoAdapter adapter;
    public static SeekBar sb_0;
    public static List<String> addlist = new ArrayList<String>();
    public static List<Long> dua;
    public  static Toolbar toolbar;
    private FloatingActionsMenu captureButton;
    private FloatingActionButton camTrash;
    private FloatingActionButton camAccept;
    public static MainActivity inthis;
    public static CoordinatorLayout cl;
    public static MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    List<a> list;
    String prompt = "If you really need the source code, please contact me at 15960811381@163.com";
    Intent intent,intent2;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((Player.MyBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    public static MediaPlayer mp ;
    ListView lv;
    private void bindServiceConnection() {
        Intent intent = new Intent(MainActivity.this, Player.class);
        startService(intent);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AllActivity.getInstance().addActivity(this);
        lv = (ListView) findViewById(R.id.listview);
        inthis = this;
        sb_0 = (SeekBar) findViewById(R.id.seekbar_0);
        cl = (CoordinatorLayout) findViewById(R.id.background);
        camTrash = (FloatingActionButton) findViewById(R.id.btn_trash);

        camAccept = (FloatingActionButton) findViewById(R.id.btn_accept);

        captureButton = (FloatingActionsMenu) findViewById(R.id.btn_capture);
        try {
            Util.readDataFromFile(this,"i");
        } catch (IOException e) {
            new help(this);
            try {
                Util.saveDataToFile(this,"i","");
            } catch (IOException e1) { }
        }
        //启动音乐服务
        Util.setWork(this);
        musicService = new Player();
        bindServiceConnection();
        mp = musicService.mp;
        SR();
        intent = new Intent(this,SecondActivity.class);

        intent2 = new Intent(this,ThirdActivity.class);







        adapter = new InfoAdapter(this);
        list = Util.getSongList(this.getContentResolver());
        all = list;
        addlist = new ArrayList<String>();
        dua = new ArrayList<>();
        musicService.list = list;
        adapter.setDate(list);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();




        lv.setOnItemClickListener(this);
        setUpListenerForCamButtons();
        try {
            UriUtils.up(this,sb_0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    void SR(){
        timer = new Timer();
        han = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(mp!=null&&bp!=null){
                    int postion = mp.getCurrentPosition();
                    bp.setProgress(postion);
                    sb_0.setProgress(postion);
                    lv.postInvalidate();
                }else{
                    sb_0.setProgress(0);
                }
            }
        };
        timerTask = new TimerTask() {
            @Override
            public void run() {
                han.sendEmptyMessage(1);
            }
        };
        timer.schedule(timerTask, 0, 30);
    }
















    private void setUpListenerForCamButtons() {

        camAccept.setOnClickListener(this);

        camTrash.setOnClickListener(this);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimateUtil.hideFab(captureButton.getAddButton());
                captureButton.expand();
            }
        });

    }














    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position!=Player.list.size()-1) {
            a fi = (a) parent.getItemAtPosition(position);
            musicService.preMusic(fi, position);
        }else{
            try {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 10002);
            }catch (Exception e){
                Util.show(this,"错误","您的移动设备中没有可用的文件选择器");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_trash:
                AnimateUtil.showFab(captureButton.getAddButton());
                captureButton.collapse();
                if(Player.mp!=null&&Player.mp.isPlaying())
                startActivity(intent);
                else{
                    Toast.makeText(this,"你应该播放一个音频",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_accept:
                AnimateUtil.showFab(captureButton.getAddButton());
                captureButton.collapse();
                String path = " ";
                if(Player.mp!=null&&Player.mp.isPlaying()) {
                    path = Player.list.get(Player.pos).h;
                }
                intent2.putExtra("Path",path);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (captureButton.isExpanded()){
            AnimateUtil.showFab(captureButton.getAddButton());
            captureButton.collapse();
            return;
        }
        AllActivity.getInstance().exit();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK&&requestCode==10002){
            File file;
            try {

                file = new File(UriUtils.getPath(this,data.getData()));
            }catch (Exception e){
                Util.show(this,"错误","路径解析失败，请重选");
                return;
            }
            String path = file.getAbsolutePath();
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            try {
            mmr.setDataSource(path);
            }catch (Exception e){
                Util.show(this,"错误","不是有效的媒体文件");
                return;
            }
            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (title==null||title.equals("")){
                title = file.getName();
            }
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            com.lingsatuo.Utils.a song = new a();
            song.a = path;
            song.b = title;
            song.c = Long.parseLong(duration);
            song.d = artist;
            song.e = album;
            song.h = path;
            song.pb = Player.list.get(Player.list.size()-1).pb;
            Player.list.set(Player.list.size()-1,song);
            if(musicService==null)musicService = new Player();
            musicService.preMusic(song, Player.list.size() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item =  menu.findItem(R.id.search);

        SearchView sv = (SearchView) item.getActionView();
        ImageView iv = (ImageView) sv.findViewById(android.support.v7.appcompat.R.id.search_button);
//        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
//        layoutParams.width = Util.dip2px(this,28);
//        layoutParams.height = Util.dip2px(this,28);
//        iv.setLayoutParams(layoutParams);

        iv.setImageResource(R.mipmap.search);
        sv.setSubmitButtonEnabled(true);
        sv.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.help:
                final Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://v.youku.com/v_show/id_XMjc3OTc5NzU5Ng==.html?x&sharefrom=android");
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.feedback:
                new i(this).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (Player.mp.isPlaying())
            Player.mp.pause();
        if (newText.equals("")){
            list = Util.getSongList(this.getContentResolver());
        }else {
            list = Util.SeacrhDate(newText);
        }
        musicService.list = list;
        adapter.setDate(list);
        adapter.notifyDataSetChanged();
        return false;
    }


    @SuppressLint("NewApi")
    public static void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 2;//图片缩放比例；
        float radius = 80;//模糊程度

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()/ scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(inthis.getResources(), overlay));
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
    }
}
