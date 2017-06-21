package com.lingsatuo.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lingsatuo.soundfile.CheapSoundFile;

public class WaveformView2 extends View {
    protected int A;
    protected int B;
//    protected Paint a;
    protected Paint b;
//    protected Paint c;
    protected Paint d;
    protected Paint e;
    protected Paint f;
//    protected Paint g;
//    protected Paint h;
    protected Paint i;
    protected CheapSoundFile j;
    protected int k;
    protected double[] l;
    protected double m = 1.0d;
    protected int[] n;
    protected float o;
    protected float p;
    protected int q;
    protected int r;
    protected int ss;
    protected a t;
    protected boolean u;
    protected Context v;
    protected float w = 1.0f;
    protected int x;
    protected int y;
    protected int z;

    public interface a {
        void waveformDraw2();

        void waveformTouchEnd();

        void waveformTouchMove(float f);

        void waveformTouchStart(float f);
    }
public WaveformView2(Context context,AttributeSet attr,int de){
    super(context,attr,de);
}
    public WaveformView2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.v = context;
        setFocusable(false);
        this.x = s.a(0.3f, context);
        this.y = s.a(23.0f, context);
        this.z = s.a(15.0f, context);
        this.A = s.a(4.0f, context);
        this.B = s.a(15.0f, context);
//        this.a = new Paint();
//        this.a.setAntiAlias(true);
//        this.a.setColor(Color.BLUE);//?
//        this.a.setStrokeWidth((float) this.x);
        this.f = new Paint();
        this.f.setAntiAlias(true);
        this.f.setColor(Color.RED);//波形颜色
        this.f.setStrokeWidth((float) this.x);
//        this.g = new Paint();
//        this.g.setAntiAlias(true);
//        this.g.setColor(Color.BLUE);//？？
        this.b = new Paint();
        this.b.setAntiAlias(true);
        this.b.setColor(Color.GRAY);//中线
        this.b.setStrokeWidth((float) s.a(0.4f, context));
//        this.h = new Paint();
//        this.h.setAntiAlias(true);
//        this.h.setColor(Color.YELLOW);//左边界
//        this.h.setStrokeWidth((float) s.a(0.5f, context));
//        this.c = new Paint();
//        this.c.setAntiAlias(true);
//        this.c.setColor(Color.YELLOW);//????
//        this.c.setStrokeWidth((float) s.a(0.5f, context));
        this.d = new Paint();
        this.d.setAntiAlias(true);
        this.d.setColor(Color.BLUE);//下边界
        this.d.setStrokeWidth((float) s.a(0.4f, context));
        this.e = new Paint();
        this.e.setAntiAlias(true);
        this.e.setColor(Color.BLUE);//上边界
        this.e.setStrokeWidth((float) s.a(0.4f, context));
        this.i = new Paint();
        this.i.setTextSize((float) s.a(10.0f, context));
        this.i.setAntiAlias(true);
        this.i.setColor(Color.BLACK);//时间颜色
        this.j = null;
        this.k = 0;
        this.l = null;
        this.n = null;
        this.ss = -1;
        this.q = 0;
        this.r = 0;
        this.u = false;
    }

    private void c() {
        int i;
        int i2;
        int i3;
        int numFrames = this.j.getNumFrames();
        int[] frameGains = this.j.getFrameGains();
        double[] dArr = new double[numFrames];
        if (numFrames == 1) {
            dArr[0] = (double) frameGains[0];
        } else if (numFrames == 2) {
            dArr[0] = (double) frameGains[0];
            dArr[1] = (double) frameGains[1];
        } else if (numFrames > 2) {
            dArr[0] = (((double) frameGains[0]) / 2.0d) + (((double) frameGains[1]) / 2.0d);
            for (i = 1; i < numFrames - 1; i++) {
                dArr[i] = ((((double) frameGains[i - 1]) / 3.0d) + (((double) frameGains[i]) / 3.0d)) + (((double) frameGains[i + 1]) / 3.0d);
            }
            dArr[numFrames - 1] = (((double) frameGains[numFrames - 2]) / 2.0d) + (((double) frameGains[numFrames - 1]) / 2.0d);
        }
        double d = 1.0d;
        for (i2 = 0; i2 < numFrames; i2++) {
            if (dArr[i2] > d) {
                d = dArr[i2];
            }
        }
        d = d > 255.0d ? 255.0d / d : 1.0d;
        int[] iArr = new int[256];
        double d2 = 0.0d;
        for (int i4 = 0; i4 < numFrames; i4++) {
            i3 = (int) (dArr[i4] * d);
            if (i3 < 0) {
                i3 = 0;
            }
            if (i3 > 255) {
                i3 = 255;
            }
            if (((double) i3) > d2) {
                d2 = (double) i3;
            }
            iArr[i3] = iArr[i3] + 1;
        }
        double d3 = 0.0d;
        i3 = 0;
        while (d3 < 255.0d && i3 < numFrames / 20) {
            i3 += iArr[(int) d3];
            d3 += 1.0d;
        }
        double d4 = d2;
        i2 = 0;
        while (d4 > 2.0d && i2 < numFrames / 100) {
            i2 += iArr[(int) d4];
            d4 -= 1.0d;
        }
        double[] dArr2 = new double[numFrames];
        double d5 = d4 - d3;
        for (i3 = 0; i3 < numFrames; i3++) {
            d2 = ((dArr[i3] * d) - d3) / d5;
            if (d2 < 0.0d) {
                d2 = 0.0d;
            }
            if (d2 > 1.0d) {
                d2 = 1.0d;
            }
            dArr2[i3] = d2 * d2;
        }
        this.k = numFrames;
        this.l = new double[this.k];
        this.m = 1.0d;
        for (i = 0; i < this.k; i++) {
            this.l[i] = dArr2[i];
        }
        this.w = 1.0f;
        float measuredWidth = (float) (getMeasuredWidth() - (this.B * 2));
        if (((float) this.k) > measuredWidth) {
            this.w = ((float) this.k) / measuredWidth;
        }
        this.u = true;
    }

    private void d() {
        int measuredHeight = ((getMeasuredHeight() - this.y) / 2) - 1;
        this.n = new int[this.k];
        for (int i = 0; i < this.k; i++) {
            this.n[i] = (int) ((this.l[i] * ((double) measuredHeight)) * 0.9d);
        }
    }

    public double a(int i) {
        if (this.o == 0.0f) {
            return 0.0d;
        }
        return (((double) (((float) i) * this.w)) * ((double) this.p)) / (this.m * ((double) this.o));
    }

    public int a(double d) {
        return (int) ((((1.0d * d) * ((double) this.o)) / ((double) this.p)) + 0.5d);
    }

    public void a(int i, int i2) {
        this.q = i;
        this.r = i2;
    }

    protected void a(Canvas canvas, int i, int i2, int i3, Paint paint) {
        canvas.drawLine((float) i, (float) i2, (float) i, (float) i3, paint);
    }

    public boolean a() {
        return this.u;
    }

    public int b(double d) {
        return (int) ((((this.m * d) * ((double) this.o)) / ((double) (this.p * this.w))) + 0.5d);
    }

    public int b(int i) {
        return (int) (((this.m * ((((double) i) * 1.0d) * ((double) this.o))) / ((1000.0d * ((double) this.p)) * ((double) this.w))) + 0.5d);
    }

    public void b() {
        this.n = null;
        this.j = null;
        invalidate();
    }

    public int c(int i) {
        if (this.o == 0.0f) {
            return 0;
        }
        return (int) (((((double) (((float) i) * this.w)) * (1000.0d * ((double) this.p))) / (this.m * ((double) this.o))) + 0.5d);
    }

    public String c(double d) {
        int i = (int) d;
        if (((int) ((100.0d * (d - ((double) i))) + 0.5d)) >= 50) {
            i++;
        }
        int i2 = i / 60;
        int i3 = i - (i2 * 60);
        return (i2 < 10 ? "" + i2 : "" + i2) + ":" + (i3 < 10 ? "00" : "" + i3);
    }

    public int getEnd() {
        return this.r;
    }

    public int getFirstEndPos() {
        return this.k > getWidth() - (this.B * 2) ? this.B + (((getWidth() - (this.B * 2)) * 2) / 3) : this.B + this.k;
    }

    public int getFirstStartPos() {
        return this.k > getWidth() - (this.B * 2) ? this.B + ((getWidth() - (this.B * 2)) / 3) : this.B;
    }

    public int getPlayback() {
        return this.ss;
    }

    public int getSelectLineWidth() {
        return this.x;
    }

    public int getStart() {
        return this.q;
    }

    public int getWaveEnd() {
        return this.k > getWidth() - (this.B * 2) ? getWidth() - this.B : this.B + this.k;
    }

    public int getWaveStart() {
        return this.B;
    }

    public int getWaveWidth() {
        return getWidth() - (this.B * 2);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.j != null) {
            if (this.n == null) {
                d();
            }
            if (this.n != null) {
                int measuredWidth = getMeasuredWidth();
                int measuredHeight = getMeasuredHeight();
                int length = this.n.length;
                int i = this.y + ((measuredHeight - this.y) / 2);
                int i2 = measuredWidth - (this.B * 2);
                int i3 = length > i2 ? i2 : length;
                int i4 = 0;
                while (i4 < measuredWidth) {
                    if (i4 >= this.q && i4 <= this.r) {
//                        a(canvas, i4, this.y, measuredHeight, this.g);
                    }
                    i4++;
                }
                canvas.drawLine(0.0f, (float) i, (float) measuredWidth, (float) i, this.b);
                int i5 = this.B;//开头长度
                i4 = i3 + this.B;
                int i6 = i5;
                while (i6 < i4) {
                    Paint paint = (i6 < this.q || i6 >= this.r) ? this.f : this.f;
                    length = (int) (((float) (i6 - this.B)) * this.w);
                    a(canvas, i6, i - this.n[length], (i + 1) + this.n[length], paint);
                    i6++;
                }
//                canvas.drawLine((float) this.ss, (float) this.y, (float) this.ss, (float) measuredHeight, this.c);
                length = this.q;
                if (this.q <= i5) {
                    length = i5;
                }
                int i7 = this.r;
                if (i7 < i4) {
                    i4 = i7;
                }
//                canvas.drawLine((float) length, (float) this.y, (float) length, (float) measuredHeight, this.h);
//                canvas.drawLine((float) i4, (float) this.y, (float) i4, (float) measuredHeight, this.h);
                length = i2 / 10;
                i4 = i2 / 30;
                for (i6 = i5; i6 <= measuredWidth - this.B; i6++) {
                    if ((i6 - i5) % length == 0) {
//                        canvas.drawLine(0.0f, (float) this.y - this.z, (float) measuredWidth, (float) this.y - this.z, this.e);
                        a(canvas, i6, this.y - this.z, this.y, this.e);
                        a(canvas, i6, this.y - this.z, this.y, this.e);
                        a(canvas, i6, this.y - this.z, measuredHeight, this.e);
                        if (i6 != measuredWidth - this.B) {
                            canvas.drawText(c(a(i6 - i5)), (float) (s.a(4.0f, this.v) + i6), (float) s.a(15.0f, this.v), this.i);
                        }
                        if (i6 != i5) {
//                            a(canvas, i6 - i4, this.y - this.A, this.y, this.e);
//                            a(canvas, i6 - (i4 * 2), this.y - this.A, this.y, this.e);
                        }
                    }
                }
                canvas.drawLine(0.0f, (float) this.y, (float) measuredWidth, (float) this.y, this.e);
                canvas.drawLine(0.0f, (float) (measuredHeight - 1), (float) measuredWidth, (float) (measuredHeight - 1), this.d);
                if (this.t != null) {
                    this.t.waveformDraw2();
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
//            case ContactInfo.UNKNOWN /*0*/:
//                if (this.t != null) {
//                    this.t.waveformTouchStart(motionEvent.getX());
//                    break;
//                }
//                break;
//            case ContactInfo.MOBILE /*1*/:
//                if (this.t != null) {
//                    this.t.waveformTouchEnd();
//                    break;
//                }
//                break;
//            case ContactInfo.UNICOM /*2*/:
//                if (this.t != null) {
//                    this.t.waveformTouchMove(motionEvent.getX());
//                    break;
//                }
//                break;
        }
        return true;
    }

    public void setListener(a aVar) {
        this.t = aVar;
    }

    public void setPlayback(int i) {
        this.ss = i;
    }

    public void setSoundFile(CheapSoundFile cheapSoundFile) {
        this.j = cheapSoundFile;
        this.o = (float) this.j.getSampleRate();
        this.p = (float) this.j.getSamplesPerFrame();
        c();
        this.n = null;
    }
}
