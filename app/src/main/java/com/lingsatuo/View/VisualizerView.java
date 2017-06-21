package com.lingsatuo.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class VisualizerView extends View {
    private byte[] bytes;
    private float[] points;
    private Paint paint = new Paint();
    private Rect rect = new Rect();

    String prompt = "If you really need the source code, please contact me at 15960811381@163.com";
    public VisualizerView(Context context) {
        super(context);
    }
    public VisualizerView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        bytes = null;
        paint.setStrokeWidth(2f);
        paint.setAntiAlias(true);
        paint.setColor(0x98ED3EDF);
        paint.setStyle(Paint.Style.FILL);
    }
    public void updateVisualizer(byte[] ftt){
        bytes = ftt;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bytes==null)return;
        rect.set(0,0,getWidth(),getHeight());
        if (points == null || points.length < bytes.length * 4)
        {
            points = new float[bytes.length * 4];
        }
        for (int i = 0; i < bytes.length - 1; i++)
        {
            // 计算第i个点的x坐标
            points[i * 4] = rect.width()*i/(bytes.length - 1);
            // 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
            points[i * 4 + 1] = (rect.height() / 2)
                    + ((byte) (bytes[i] + 128)) * 128
                    / (rect.height() / 2)/5;
            // 计算第i+1个点的x坐标
            points[i * 4 + 2] = rect.width() * (i + 1)
                    / (bytes.length - 1);
            // 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
            points[i * 4 + 3] = (rect.height() / 2)
                    + ((byte) (bytes[i + 1] + 128)) * 128
                    / (rect.height() / 2)/5;
        }
        // 绘制波形曲线
        canvas.drawLines(points, paint);



    }
}
