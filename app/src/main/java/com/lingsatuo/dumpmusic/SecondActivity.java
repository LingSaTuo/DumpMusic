package com.lingsatuo.dumpmusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ApplicationExc.AllActivity;
import com.lingsatuo.Dialog.d;
import com.lingsatuo.Dialog.f;
import com.lingsatuo.Dialog.h;
import com.lingsatuo.Service.CompositeService;
import com.lingsatuo.Service.Player;
import com.lingsatuo.Utils.ACA;
import com.lingsatuo.Utils.Util;
import com.lingsatuo.Utils.a;
import com.lingsatuo.View.BackgroundProgress;
import com.lingsatuo.View.BaseVisualizerView;
import com.lingsatuo.View.TwoButtonSeekBar;
import com.lingsatuo.View.VisualizerView;
import com.lingsatuo.View.WM;
import com.lingsatuo.View.WaveformView;
import com.lingsatuo.View.WaveformView2;
import com.lingsatuo.soundfile.CheapSoundFile;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class SecondActivity extends AppCompatActivity implements TwoButtonSeekBar.OnSeekBarChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, NavigationView.OnNavigationItemSelectedListener{

    private Visualizer mVisualizer;
    private BaseVisualizerView baseVisualizerView;
    private VisualizerView visualizerview;
    private Equalizer mEqualizer;
    private TextView audioinfo,new_,max,now,error_range;
    private TwoButtonSeekBar twb_0,twb_1;
    private android.support.v7.widget.AppCompatButton play;
    private static android.support.v7.widget.AppCompatButton save;
    private android.support.v7.widget.AppCompatButton left_0,right_1,left_1,right_0;
    private SeekBar sb;
    CheapSoundFile mSoundFile = null;
    TimerTask timerTask;
    Timer timer = null;
    Handler han;
    RoundedImageView iv;
    boolean isrun = false;
    static SecondActivity inthis;
    public static  VideoView video;
    ImageView icon;
    NavigationView navigationView;
    private ACA aca;
    WaveformView2 waveformView2;
    WM bp;
    Bitmap bit;
//    WaveformView wave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_root);
        setSupportActionBar((Toolbar) findViewById(R.id.second_toolbar));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, (Toolbar) findViewById(R.id.second_toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        if(timer==null) {
            inthis = this;
            AllActivity.getInstance().addActivity(this);
            audioinfo = (TextView) navigationView.getHeaderView(0).findViewById(R.id.audio_mess);
            twb_0 = (TwoButtonSeekBar) findViewById(R.id.twoseekbar_0);
            twb_1 = (TwoButtonSeekBar) findViewById(R.id.twoseekbar_1);
            new_ = (TextView) findViewById(R.id.new_);
            max = (TextView) findViewById(R.id.max);
            play = (AppCompatButton) findViewById(R.id.play);
            save = (AppCompatButton) findViewById(R.id.save);
            sb = (SeekBar) findViewById(R.id.seekbar_0);
            sb.setMax(Player.mp.getDuration());
            now = (TextView) findViewById(R.id.now);
            left_0 = (AppCompatButton) findViewById(R.id.left_0);
            left_1 = (AppCompatButton) findViewById(R.id.left_1);
            right_0 = (AppCompatButton) findViewById(R.id.right_0);
            waveformView2 = (WaveformView2) findViewById(R.id.wave2);
            right_1 = (AppCompatButton) findViewById(R.id.right_1);
            error_range = (TextView) findViewById(R.id.wc);
//            wave = (WaveformView) findViewById(R.id.wave);
            bp = (WM) findViewById(R.id.waveback);
            video  = (VideoView) findViewById(R.id.video);
            icon = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.icon);
            iv = (RoundedImageView) findViewById(R.id.adlumicon);
            aca = new ACA();
            aca.curr = (TextView) navigationView.getHeaderView(0).findViewById(R.id.audioc_curr);
            aca.max = (TextView) navigationView.getHeaderView(0).findViewById(R.id.audioc_max);
            aca.retime = (TextView) navigationView.getHeaderView(0).findViewById(R.id.audioc_retime);
            aca.pb = (ProgressBar) navigationView.getHeaderView(0).findViewById(R.id.audioc_pd);
            baseVisualizerView = (BaseVisualizerView) findViewById(R.id.bisualizerview);
            visualizerview = (VisualizerView) findViewById(R.id.bisualizerview2);
            Player.playtype = 1;
            Intent i = new Intent(this, CompositeService.class);
            startService(i);
        }
    }
   private void setListener(){
       left_0.setOnClickListener(this);
       left_1.setOnClickListener(this);
       right_0.setOnClickListener(this);
       right_1.setOnClickListener(this);
       play.setOnClickListener(this);
       save.setOnClickListener(this);
       twb_0.setOnSeekBarChangeListener(this);
       twb_1.setOnSeekBarChangeListener(this);
       sb.setOnSeekBarChangeListener(this);
//       wave.setListener(this);
   }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            if(Player.mp!=null&&!isrun&&Player.list!=null) {
                setupVisualizerFxAndUi(Player.mp.getAudioSessionId());
                setupEqualizeFxAndUi(Player.mp.getAudioSessionId());
                setve();
                if (mVisualizer!=null)
                mVisualizer.setEnabled(true);
                SR();
                isrun = true;
                byte[] bytes = MainActivity.mmr.getEmbeddedPicture();
                bit = Util.byteToBitmap(bytes);
                if (bit!=null) {
                    Snackbar.make(save, "此音频的专辑图可导出", 1000).show();
                    iv.setImageBitmap(bit);
                    icon.setImageBitmap(bit);
                    navigationView.getMenu().findItem(R.id.save_pic).setVisible(true);
                }else{
                    if(mSoundFile!=null) {
                        switch (mSoundFile.getFiletype()) {
                            case "MP3":
                                iv.setImageResource(R.mipmap.mp3);
                                icon.setImageResource(R.mipmap.mp3);
                                break;
                            case "WAV":
                                iv.setImageResource(R.mipmap.wav);
                                icon.setImageResource(R.mipmap.wav);
                                break;
                            default:
                                iv.setImageResource(R.mipmap.unknown);
                                icon.setImageResource(R.mipmap.unknown);
                                break;
                        }
                    }else{
                        iv.setImageResource(R.mipmap.unknown);
                        icon.setImageResource(R.mipmap.unknown);
                    }
                }
            }
        }
    }

    void SR(){
        timer = new Timer();
        han = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int new_2 = 0;
                DecimalFormat df = new DecimalFormat("####000");
                if(video.getVisibility()!=View.VISIBLE) {
                    if(Player.mp==null)return;
                    new_2 = Player.mp.getCurrentPosition();//当前播放时间
                    try {
                        if (new_2 < (twb_1.getCurrHeight())) {//如果小于最终距离{
                            sb.setProgress((int) (new_2 - twb_1.getCurrLow()));

                            int m_1 = (int) (new_2 / 60 / 1000);
                            int s_1 = (int) ((new_2 - m_1 * 60 * 1000) / 1000);
                            int ms_1 = (int) (new_2 - m_1 * 60 * 1000 - s_1 * 1000);
                            now.setText(m_1 + ":" + s_1 + "." + df.format(ms_1));
                        } else {
                            if (twb_1.getCurrHeight() > 1000)
                                if (Player.mp.isPlaying())
                                    Player.mp.pause();
                        }
                    } catch (Exception e) {
                    } if(mSoundFile!=null){
                        bp.setProgress(new_2);
                    }
                }else{
                    new_2 = video.getCurrentPosition();
                    sb.setProgress(new_2);
                    int m_1 = (new_2 / 60 / 1000);
                    int s_1 = ((new_2 - m_1 * 60 * 1000) / 1000);
                    int ms_1 =  (new_2 - m_1 * 60 * 1000 - s_1 * 1000);
                    now.setText(m_1 + ":" + s_1 + "." + df.format(ms_1));
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
    private void setupVisualizerFxAndUi(int AudioSessionId)
    {
        try {
            mVisualizer = new Visualizer(AudioSessionId);
//		mVisualizer = new Visualizer(0);
            // 参数内必须是2的位数
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            // 设置允许波形表示，并且捕获它
            baseVisualizerView.setVisualizer(mVisualizer, visualizerview);
        }catch (Exception e){
            Toast.makeText(this,"示波器初始化失败，可能权限 不足",Toast.LENGTH_LONG).show();
            d.show(this,"失败的初始化",e);
        }
    }

    private void setupEqualizeFxAndUi(int id)
    {
        try{
        mEqualizer = new Equalizer(0, id);
//		mEqualizer = new Equalizer(0, 0);
        mEqualizer.setEnabled(true);// 启用均衡器
    }catch (Exception e){
        Toast.makeText(this,"启用均衡器失败",Toast.LENGTH_LONG).show();
    }
    }

    @Override
    public void onBackPressed() {
        if (mVisualizer!=null)
        mVisualizer.setEnabled(false);
        Player.playtype = 0;
        if (timer!=null&&timerTask!=null) {
            timer.cancel();
            timerTask.cancel();
        }
        super.onBackPressed();
    }

    @Override
    public void onProgressBefore() {

    }
    double curr = 0;
    double heigh= 0;
    @Override
    public void onProgressChanged(TwoButtonSeekBar seekBar, double progressLow, double progressHigh) {
            switch (seekBar.getId()){
                case R.id.twoseekbar_0:
                    if(progressLow!=progressHigh) {
                        curr = twb_1.getCurrLow();
                        heigh = twb_1.getCurrHeight();
                        twb_1.setMin((int) progressLow);
                        twb_1.setMax((int) progressHigh);
                        bp.setLeftm((long) twb_1.getCurrLow());
                        bp.setRightm((long) twb_1.getCurrHeight());
                    }
                    break;
                case R.id.twoseekbar_1:
                    int zssc = (int)(twb_1.getCurrHeight() - twb_1.getCurrLow());//真实截取时长
                    sb.setMax(zssc);
                    new_.setText((int) twb_1.getCurrLow() + "");
                    max.setText((int) twb_1.getCurrHeight() + "");
                    bp.setLeftm((long) twb_1.getCurrLow());
                    bp.setRightm((long) twb_1.getCurrHeight());
                    break;
            }
    }

    @Override
    protected void onResume() {
        if (Player.list!=null)
        getSupportActionBar().setTitle(Player.list.get(Player.pos).b);
        super.onResume();
    }

    @Override
    public void onProgressAfter() {
        twb_1.refresh();
        new_.setText((int)twb_1.getCurrLow()+"");
        max.setText((int)twb_1.getCurrHeight()+"");
        int zssc = (int)(twb_1.getCurrHeight()-twb_1.getCurrLow());//真实截取时长
        sb.setMax(zssc);
        Player.mp.seekTo((int) twb_1.getCurrLow());//跳转到选取的最低刻度
        DecimalFormat df = new DecimalFormat("######0.0000");
        String s = df.format(Util.getErr(mSoundFile,twb_1.getCurrLow()));
        String e = df.format(Util.getErr(mSoundFile,twb_1.getCurrHeight()));
        double st,et;
        try {
            st = Double.valueOf(s);
            et = Double.valueOf(e);
            bp.setLeftm((long) twb_1.getCurrLow());
            bp.setRightm((long) twb_1.getCurrHeight());
            error_range.setText("开始误差约  (Frame) :"+s+"\n结束误差约  (Frame) :"+df.format(et-st)+"\n\n(不定值 198400长度  192kbps下 )\n0.086Frame 约 26ms   ,  0.2121Frame 约 29ms ");
        }catch (NumberFormatException e2){
            error_range.setText("开始误差约  (Frame) :"+"  ∞  "+"\n结束误差约  (Frame) :"+"  ∞  "+"\n\n(不定值 198400长度  192kbps下 )\n0.086Frame 约 26ms   ,  0.2121Frame 约 29ms ");
            save.setEnabled(false);
            save.setText("音频不合规");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if (video.getVisibility()!=View.VISIBLE) {
                    if (Player.mp.isPlaying()) {
                        Player.mp.pause();
                    } else {
                        Player.mp.seekTo((int) twb_1.getCurrLow());
                        Player.mp.start();
                    }
                }else{
                    if(video.isPlaying()){
                        video.pause();
                    }else{
                        video.start();
                    }
                }
                break;
            case R.id.save:
                if (video.getVisibility()==View.GONE) {
                    if (!save.getText().toString().equals("真实预览")) {
                        if (mSoundFile != null) {
                            Util.SaveSound(this, Player.list.get(Player.pos).b, mSoundFile, twb_1.getCurrLow(), twb_1.getCurrHeight());
                            String prompt = "If you really need the source code, please contact me at 15960811381@163.com";
                            prompt = null;
                        }
                    }else{
                        h.PreView(this, mSoundFile, twb_1.getCurrLow(), twb_1.getCurrHeight());
                    }
                } else {
                        Util.SaveSound(this, Player.list.get(Player.pos).h, Util.getFileNameNoEx(new File(Player.list.get(Player.pos).h).getName()));
                    }
                break;

            case R.id.left_0:
                double now_0 = twb_1.getCurrLow()-100;//获取最低值减去100
                if(now_0<twb_1.getMin()){
                    twb_1.setProgressLow(twb_1.getMin());
                }else{
                    twb_1.setProgressLow(now_0);
                }
                break;

            case R.id.left_1:
                double now_1 = twb_1.getCurrLow()+100;
                if(now_1>twb_1.getMax()){
                    twb_1.setProgressLow(twb_1.getMax());
                }else{
                    twb_1.setProgressLow(now_1);
                }
                break;
            case R.id.right_0:

                double now_2 = twb_1.getCurrHeight()-100;
                if(now_2<twb_1.getMin()){
                    twb_1.setProgressHigh(twb_1.getMin());
                }else{
                    twb_1.setProgressHigh(now_2);
                }
                break;
            case R.id.right_1:

                double now_3 = twb_1.getCurrHeight()+100;
                if(now_3>twb_1.getMax()){
                    twb_1.setProgressHigh(twb_1.getMax());
                }else{
                    twb_1.setProgressHigh(now_3);
                }
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(video.getVisibility()==View.VISIBLE){
            video.seekTo(seekBar.getProgress());
            return;
        }
        Player.mp.seekTo((int) (seekBar.getProgress()+twb_1.getCurrLow()));
    }


    private void setve(){
        if(Player.list == null){
            Toast.makeText(this,"服务异常，请重启应用",Toast.LENGTH_LONG).show();
            return;
        }
        a item = Player.list.get(Player.pos);
        try {
            mSoundFile = CheapSoundFile.create(item.h,
                    null);
            if(mSoundFile==null){
                String name  = new File(Player.list.get(Player.pos).h).getName();
                if (name.endsWith("mp4")||name.endsWith("MP4")){
                    icon.setImageResource(R.mipmap.mp4);
                    iv.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    video.setVideoPath(Player.list.get(Player.pos).h);
                    video.start();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        setupVisualizerFxAndUi(video.getAudioSessionId());
                        setupEqualizeFxAndUi(video.getAudioSessionId());
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(Player.list.get(Player.pos).h);
                        String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        sb.setMax(Integer.parseInt(time));
                        sb.setOnSeekBarChangeListener(this);
                        play.setOnClickListener(this);
                        save.setOnClickListener(this);
                        save.setEnabled(true);
                        save.setText("提取音频");
                        new_.setText(0+"");
                        max.setText(Integer.parseInt(time)+"");
                    }else{
                        Util.show(this, "提示", "您的系统版本过低，无法享用视频提取音频，后续版本中会努力适配到您的版本。");
                    }
                    Player.mp.pause();
                }else {
                    Util.show(this, "警告", "不可识别的音频");
                }
                return;
            }
        } catch (IOException e) { Util.show(this,"警告","暂不支持编辑的音频"+"\n"+e.toString());
            return;
        }
        if(mSoundFile!=null) {
//            wave.setSoundFile(mSoundFile);
//            mWidth =  wave.getMeasuredWidth();
//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            wave.recomputeHeights(metrics.density);
            DecimalFormat df = new DecimalFormat("######0.000");
            String infos =
                    "音频标题    : " + Player.list.get(Player.pos).b + " \n\n" +
                    "音频格式    : " + mSoundFile.getFiletype() + "\n\n" +
                    "音频Hz     : " + mSoundFile.getSampleRate() + " Hz\n\n" +
                    "音频Kbps   : " + mSoundFile.getAvgBitrateKbps() + " kbps\n\n" +
                    "音频时长   : " + df.format((double) Player.mp.getDuration() / 1000.0) + " " +
                    "S(秒)" + "\n\n" +
                    "音频路径   : " + Player.list.get(Player.pos).h;
            if (mSoundFile.getAvgBitrateKbps()!=192&&mSoundFile.getAvgBitrateKbps()!=320) {
                Snackbar.make(save, "对于此 " + mSoundFile.getFiletype() + " 音频我有见解，点击右上角查看", 4000).show();
                mi.setVisible(true);
                save.setText("真实预览");
            }
            audioinfo.setText(infos);
            setListener();
            waveformView2.setSoundFile(mSoundFile);
            bp.setWaveformView(waveformView2);
            bp.setMax(Player.mp.getDuration());
            bp.setMin(0);
            System.out.println(bp.getMin()+"");
            bp.setVisibility(View.VISIBLE);
        }
            twb_0.setMax(Player.mp.getDuration());
            twb_0.setMin(25);
            twb_1.setMin(25);
            twb_1.setMax(Player.mp.getDuration());
        if(mSoundFile.getAvgBitrateKbps()!=192&&mSoundFile.getAvgBitrateKbps()<192&&mSoundFile.getAvgBitrateKbps()!=0){
            twb_1.setProgressLow(700);
            save.setText("真实预览");
            Toast.makeText(this,"此音频建议开始不要低于700毫秒",Toast.LENGTH_LONG).show();
        }else if(mSoundFile.getAvgBitrateKbps()!=320&&mSoundFile.getAvgBitrateKbps()>192){
            twb_1.setProgressLow(900);
            save.setText("真实预览");
            Toast.makeText(this,"此音频建议开始不要低于900毫秒",Toast.LENGTH_LONG).show();
        }
            sb.setMax(Player.mp.getDuration());
            new_.setText((int)twb_1.getCurrLow()+"");
            max.setText((int)twb_1.getCurrHeight()+"");
    }
    static boolean twbe = false , aace = false;
    public static void aacerr(){
        if(!twbe)Util.show(inthis, "错误", "出现致命性错误");
        twbe = true;
        save.setEnabled(false);
    }
    public static void aacerr2(String type){
        if(!aace)Util.show(inthis, "错误", "该音频不是"+type +" 格式，或该音频损坏。");
        aace = true;
        save.setEnabled(false);
    }
    MenuItem mi;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.second,menu);
         mi=  menu.findItem(R.id.info);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                Util.show(this,"Dump Music的个人见解","该音频Kbps不是常规值，剪切可能出现长度异常，但剪切内容正常的情况，就是说，音频长度不变，但内容缩短。");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_pic:
//                    com.lingsatuo.Thp.a.SaveSound(this,Player.list.get(Player.pos).h,aca,mSoundFile.getAvgBitrateKbps());
                f.SavePic(this,bit,Player.list.get(Player.pos).b);
                break;
        }
        return false;
    }
////WAVE监听------------------------------------------------------------------------
//    int mTouchInitialOffset;
//    float mTouchStart;
//    int mOffset = 0,mFlingVelocity = 0,mOffsetGoal = 0,mWidth;
//    boolean mTouchDragging = false;
//    long mWaveformTouchStartMsec;
//
//    @Override
//    public void waveformTouchStart(float x) {
//        mTouchDragging = true;
//        mTouchStart = x;
//        mTouchInitialOffset = mOffset;
//        mFlingVelocity = 0;
//        mWaveformTouchStartMsec = System.currentTimeMillis();
//    }
//
//    @Override
//    public void waveformTouchMove(float x) {
//        mOffset = trap((int)(mTouchInitialOffset + (mTouchStart - x)));
//        dothing();
//    }
//
//    @Override
//    public void waveformTouchEnd() {
//        mTouchDragging = false;
//        mOffsetGoal = mOffset;
//    }
//
//    @Override
//    public void waveformFling(float x) {
//        mTouchDragging = false;
//        mOffsetGoal = mOffset;
//        mFlingVelocity = (int)(-x);
//        dothing();
//    }
//
//    @Override
//    public void waveformDraw() {
//        mWidth = wave.getMeasuredWidth();
//        if (mOffsetGoal != mOffset)
//            dothing();
//        if (mFlingVelocity != 0) {
//            dothing();
//        }
//    }
////WAVE监听结束------------------------------------------------------------------------
//
//
//    //WAVE工具
//
//    private int trap(int pos) {
//        if (pos < 0)
//            return 0;
//        if (pos > wave.maxPos())
//            return wave.maxPos();
//        return pos;
//    }
//    private void dothing(){
//        if (!mTouchDragging) {
//            int offsetDelta;
//
//            if (mFlingVelocity != 0) {
//                float saveVel = mFlingVelocity;
//
//                offsetDelta = mFlingVelocity / 30;
//                if (mFlingVelocity > 80) {
//                    mFlingVelocity -= 80;
//                } else if (mFlingVelocity < -80) {
//                    mFlingVelocity += 80;
//                } else {
//                    mFlingVelocity = 0;
//                }
//
//                mOffset += offsetDelta;
//
//                if (mOffset + mWidth / 2 > wave.maxPos()) {
//                    mOffset = wave.maxPos() - mWidth / 2;
//                    mFlingVelocity = 0;
//                }
//                if (mOffset < 0) {
//                    mOffset = 0;
//                    mFlingVelocity = 0;
//                }
//                mOffsetGoal = mOffset;
//            } else {
//                offsetDelta = mOffsetGoal - mOffset;
//
//                if (offsetDelta > 10)
//                    offsetDelta = offsetDelta / 10;
//                else if (offsetDelta > 0)
//                    offsetDelta = 1;
//                else if (offsetDelta < -10)
//                    offsetDelta = offsetDelta / 10;
//                else if (offsetDelta < 0)
//                    offsetDelta = -1;
//                else
//                    offsetDelta = 0;
//
//                mOffset += offsetDelta;
//            }
//            wave.setParameters(0, 0, mOffset);
//            wave.invalidate();
//        }
//    }
}
