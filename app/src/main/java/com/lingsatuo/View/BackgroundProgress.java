package com.lingsatuo.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class BackgroundProgress extends View {

    private int width = 1;
    private int color = 0xA2361AED;
    private long max = 0;
    private boolean isinvalidate = false;
    BackgroundProgress bp;
    public BackgroundProgress(Context context,AttributeSet attributeSet ,int id){
        super(context,attributeSet,id);
    }
    public BackgroundProgress(Context context,  AttributeSet attrs) {
        super(context, attrs);
        setVisibility(View.GONE);
        String prompt = "If you really need the source code, please contact me at 15960811381@163.com";
        bp = this;
        //setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        isinvalidate = true;
        paint.setTextSize(20);
        paint.setColor(color);
        int height = getHeight();
        float dun = getWidth()*((float)width/max);
        canvas.drawRect(0,0,dun,height,paint);
//
//        Paint paint1 = new Paint();
//        paint1.setAntiAlias(true);
//        paint1.setFilterBitmap(true);
//        paint1.setColor(Color.RED);
//        paint1.setStrokeWidth((float) 6.0);
//        canvas.drawLine(getHeight()/20,getHeight()/20,getWidth()/80,getHeight()/2,paint1);
    }
     public void setProgress(long progress){
         this.width = (int) progress;
         isinvalidate = false;
         bp.invalidate();
     }
     public boolean getInvalidated(){
           return isinvalidate;
     }
     public int getProgress(){
         return this.width;
     }
     public void showDown(){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 try {
                     while(width>20) {
                         Thread.sleep(20);
                         width-=width/20;
                         postInvalidate();
                     }
                     width = 0;
                     postInvalidate();
                 } catch (InterruptedException e) { e.printStackTrace();}
             }
         }).start();
     }
     public void setMax(long max){
        this.max = max;
     }
     public long getMax(){
         return this.max;
     }
}
