package com.lingsatuo.View;

/**
 * Created by Administrator on 2017/5/18 0018.
 */
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lingsatuo.dumpmusic.MainActivity;
import com.lingsatuo.dumpmusic.R;

public class DragListView extends ListView {

    private ImageView dragImageView;// 被拖拽的项，其实就是一个ImageView
    private int dropFrom;// 手指拖动项原始在列表中的位置
    private int dropTo;// 手指拖动的时候，当前拖动项在列表中的位置

    private int dragPointX;// 在当前数据项中的X轴上的位置
    private int dragPointY;// 在当前数据项中的Y轴上的位置
    private int dragOffsetX;// 当前视图和屏幕的距离(x方向上)
    private int dragOffsetY;// 当前视图和屏幕的距离(y方向上)

    private WindowManager windowManager;// windows窗口控制类
    private WindowManager.LayoutParams windowParams;// 用于控制拖拽项的显示的参数

    private int scaledTouchSlop;// 判断滑动的距离,就是到底滑动多长距离，才算是 移动 滚动
    private int upScrollBounce;// 拖动的时候，开始向上滚动的上限
    private int downScrollBounce;// 拖动的时候，开始向下滚动的下限

    //交换数据的监听
    private ExchangeDataListener exchangeDataListener = null;

    public DragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
// 获得触发移动事件的最小距离
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    // 拦截touch事件，其实就是加一层控制
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN
                && exchangeDataListener != null) {
// 确定相对于离 listview这个父控件的 距离
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            Log.i("DragListView", "onInterceptTouchEvent : ev.getX() = " + x);
            Log.i("DragListView", "onInterceptTouchEvent : ev.gety() = " + y);

// x,y是相对于listview的距离，可以通过这两个值确定在listview的位置
            dropFrom = dropTo = pointToPosition(x, y);
// 如果不在有效的listview的局域
            if (dropTo == AdapterView.INVALID_POSITION) {
                return super.onInterceptTouchEvent(ev);
            }

// 获得点击的时候，选中的itemView,在ListView里面一个选项可以是几个view视图的，所以用ViewGroup
            ViewGroup itemView = (ViewGroup) getChildAt(dropTo
                    - getFirstVisiblePosition());

// 确定相对于离 listview里面的选中一个的itemview 的位置，
// 和上面的那个不一样的，
// getLeft和getTop是相对于离listview的距离
            dragPointX = x - itemView.getLeft();
            dragPointY = y - itemView.getTop();
            Log.i("DragListView", "onInterceptTouchEvent : dragPointX = "
                    + dragPointX);
            Log.i("DragListView", "onInterceptTouchEvent : dragPointY = "
                    + dragPointY);

// 当前视图和屏幕的距离,getRawX()和getRawY()，指离屏幕的距离
            dragOffsetX = (int) (ev.getRawX() - x);
            dragOffsetY = (int) (ev.getRawY() - y);
            Log.i("DragListView", "onInterceptTouchEvent : ev.getRawX()= "
                    + ev.getRawX());
            Log.i("DragListView", "onInterceptTouchEvent : ev.getRawY() = "
                    + ev.getRawY());
            Log.i("DragListView",
                    "onInterceptTouchEvent : ev.dragOffsetX() = "
                            + dragOffsetX);
            Log.i("DragListView",
                    "onInterceptTouchEvent : ev.dragOffsetY() = "
                            + dragOffsetX);

            View dragger = itemView.findViewById(R.id.move);
// 判断拖动的位置: -20 只是为了增加 横向拖动的空间 ，可以试下调大大的值，再试试
            if (dragger != null && x > dragger.getLeft()-20) {
// 当拖动的时候，假设listview布满屏幕的话，listview界面需要判断是向上滚还是向下滚动，
                upScrollBounce = Math.min(y - scaledTouchSlop, getHeight() / 3);
                downScrollBounce = Math.max(y + scaledTouchSlop,
                        getHeight() * 2 / 3);

// 图片缓存
                itemView.setDrawingCacheEnabled(true);
// 把缓冲中的图片载入去
                Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
// 开始绘图
                startDrag(bm, x, y);
            }
// 返回false表示可以继续向下传递
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (dragImageView != null && dropTo != INVALID_POSITION
                && exchangeDataListener != null) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_UP:
                    if(MainActivity.addlist.size()>1) {
                        int upY = (int) ev.getY();
// 停止绘画，相当于释放内存
                        stopDrag();
// 绘图
                        onDrop(upY);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(MainActivity.addlist.size()>1) {
                        int moveX = (int) ev.getX();
                        int moveY = (int) ev.getY();
//绘画移动的状态
                        onDrag(moveX, moveY);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 准备拖动，初始化拖动项的图像
     *
     * @param bm
     * @param x
     * @param y
     */
    public void startDrag(Bitmap bm, int x, int y) {
// 停止绘画，释放内存
        stopDrag();

        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = x - dragPointX + dragOffsetX;
        windowParams.y = y - dragPointY + dragOffsetY;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager) getContext().getSystemService("window");
        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;
    }

    /**
     * 停止拖动，去除拖动项的头像
     */
    public void stopDrag() {
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    /**
     * 拖动执行，在Move方法中执行
     *
     * @param x
     * @param y
     */
    public void onDrag(int x, int y) {
        if (dragImageView != null) {
// 拖动的过程的 透明度
            windowParams.alpha = 0.8f;
            windowParams.x = x - dragPointX + dragOffsetX;
            windowParams.y = y - dragPointY + dragOffsetY;
// 边拖动 边更新数据 , 注释之后就能看到效果了。。
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
// 为了避免滑动到分割线的时候，返回-1的问题
        int tempPosition = pointToPosition(0, y);
//判断是否无效区域
        if (tempPosition != INVALID_POSITION) {
            dropTo = tempPosition;
        }

// 滚动
        int scrollHeight = 0;
        if (y < upScrollBounce) {
            scrollHeight = 8;// 定义向上滚动8个像素，如果可以向上滚动的话
        } else if (y > downScrollBounce) {
            scrollHeight = -8;// 定义向下滚动8个像素，，如果可以向上滚动的话
        }

        if (scrollHeight != 0) {
// 真正滚动的方法setSelectionFromTop()
            setSelectionFromTop(dropTo,
                    getChildAt(dropTo - getFirstVisiblePosition())
                            .getTop() + scrollHeight);
        }
    }

    /**
     * 拖动放下的时候
     *
     * @param y
     */
    public void onDrop(int y) {

// 为了避免滑动到分割线的时候，返回-1的问题
        int tempPosition = pointToPosition(0, y);
        if (tempPosition != INVALID_POSITION) {
            dropTo = tempPosition;
        }

// 超出边界处理
        if (y < getChildAt(1).getTop()) {
// 超出上边界
            dropTo = 1;
        } else if (y > getChildAt(getChildCount() - 1).getBottom()) {
// 超出下边界
            dropTo = getAdapter().getCount() - 1;
        }

// 数据交换
        if (dropTo > 0 && dropTo < getAdapter().getCount()) {
            if (exchangeDataListener != null)
//让继承者来实现交换
                exchangeDataListener.setExchangeData(dropFrom,
                        dropTo);
        }
    }

    // 交换数据的监听接口
    public interface ExchangeDataListener {
        void setExchangeData(int from, int to);
    }

    // 初始化交换数据的监听
    public void setExchangeDataListener(
            ExchangeDataListener exchangeDataListener) {
        this.exchangeDataListener = exchangeDataListener;
    }

}