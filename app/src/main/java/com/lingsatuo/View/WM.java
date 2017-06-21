package com.lingsatuo.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class WM extends View {

    private int width = 1;
    private int color = 0x6623EA2D;
    private long max = 0,min = 0,left = 0;
    private boolean isinvalidate = false;
    private long right = 0 ;
    private WaveformView2 waveformView2;
    WM bp;
    Paint paint = new Paint();
    Paint paint2 = new Paint();
    public WM(Context context, AttributeSet attributeSet , int id){
        super(context,attributeSet,id);
    }
    public WM(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVisibility(View.GONE);
        String prompt = "If you really need the source code, please contact me at 15960811381@163.com";
        bp = this;
        setWillNotDraw(false);
        paint.setTextSize(20);
        paint.setColor(color);
        paint2.setTextSize(20);
        paint2.setColor(0xB4333333);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        float dun = getWidth()*((float)width/max);
        canvas.drawRect(getWidth()*((float)min/max),0,dun,height,paint);//选中区
        canvas.drawRect(0,0,getWidth()*((float)left/max),height,paint2);//左侧未选
        canvas.drawRect(getWidth()*((float)right/max),0,getWidth(),height,paint2);//右侧未选
    }
    public void setProgress(long progress){
        if (waveformView2==null)
            return;
        int st = (int)((double)waveformView2.getWaveStart()/(waveformView2.getWaveStart()*2+waveformView2.getWaveWidth())*getMax());
        double cur = ((double) progress/getMax()*(getMax()-2*st));
        this.width = (int) cur+st;
        isinvalidate = false;
        bp.invalidate();
    }
    public boolean getInvalidated(){
        return isinvalidate;
    }
    public int getProgress(){
        return this.width;
    }
   public long getMin(){
       return this.min;
   }
   public void setWaveformView(WaveformView2 waveformView2){
       this.waveformView2 = waveformView2;
   }
   public WaveformView2 getWaveformView2(){
       return this.waveformView2;
   }
    public void setMax(long max){
        this.max = max;
        right = (long) get(max);
    }
    public long getMax(){
        return this.max;
    }
    public void setMin(long min){
        this.min = get(min);
        this.left = this.min;
    }
    public void setRightm(long max){
        right = get(max);
    }
    public void setLeftm(long min){
        setMin(min);
    }
    private long get(long index){
        int st = (int)((double)waveformView2.getWaveStart()/(waveformView2.getWaveStart()*2+waveformView2.getWaveWidth())*getMax());
        double cur = ((double) index/getMax()*(getMax()-2*st));
        return (long) (cur+st);
    }
}
