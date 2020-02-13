package com.example.attendclasstpad.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.attendclasstpad.R;
import com.example.attendclasstpad.application.CustomApplication;
import com.example.attendclasstpad.model.DrawPath;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 自定义控件
 */

public class CustomSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback, Runnable {
    /**
     * 画笔尺寸，画线粗细
     */
    private float paintSize = 8;

    /**
     * 画笔颜色，默认为红色
     */
    private int paintColor = R.color.red;

    Thread thread;
    // SurfaceHolder实例
    private SurfaceHolder mSurfaceHolder;
    // Canvas对象
    private Canvas mCanvas;
    // 控制子线程是否运行
    private boolean startDraw = true;
    // Path实例
    private Path mPath = new Path();
    // Paint实例
    private Paint mPaint;

    private Bitmap mBitmap;
    private float mX, mY;// 临时点坐标

    // 保存Path路径的集合,用List集合来模拟栈
    private static List<DrawPath> savePathList;
    private static List<DrawPath> redoPathList;
    // 记录Path路径的对象
    private DrawPath drawPath;

    private static int width;
    private static int height;
    private boolean isFirstShow = true;

    // 记录Path路径的对象
    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
//        thread = new Thread(this);
        savePathList = new ArrayList<DrawPath>();
        redoPathList = new ArrayList<DrawPath>();

        width = CustomApplication.Screen_Width;
        height = CustomApplication.Screen_Height;

        initView(); // 初始化

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 保存一次一次绘制出来的图形
        mCanvas = new Canvas(mBitmap);
    }

    private void initView() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        // 设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置常亮
        this.setKeepScreenOn(true);

    }

    @Override
    public void run() {
        // 如果不停止就一直绘制
        // while (startDraw) {
        // // 绘制
        // draw();
        // }

        while (startDraw) {
            draw();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 创建
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDraw = true;

        thread = new Thread(this);
        thread.start();

//        if (isFirstShow) {
//            thread.start();
//        } else {
//            if (thread ==Thread.currentThread())  {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                }
//            }
//            thread.run();
//        }

//        isFirstShow = false;
    }

    /*
     * 改变
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        System.out.println("surfaceChanged");
    }

    /*
     * 销毁
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        startDraw = false;

        thread = null;
    }

    private void draw() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();

            // 设置背景透明
            mCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
            // mCanvas.drawColor(Color.WHITE);

            // 保存一次一次绘制出来的图形
            // mCanvas = new Canvas(mBitmap);
            // 将前面已经画过得显示出来
            // mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

            // mPaint.setStyle(Paint.Style.STROKE);
            // mpaint.setStrokeWidth(DensityUtil.px2dip(getContext(), 30));
            // mPaint.setStrokeWidth(paintSize);
            // mPaint.setColor(paintColor);

            // 保存之前绘制出来的内容
            if (savePathList != null && savePathList.size() > 0) {
                for (int i = 0; i < savePathList.size(); i++) {
                    mCanvas.drawPath(savePathList.get(i).getPath(),
                            savePathList.get(i).getPaint());
                }
            }

            if (mPath != null) {
                // 实时的显示
                mCanvas.drawPath(mPath, mPaint);
            } else {
                mPath = new Path();
                mCanvas.drawPath(mPath, mPaint);
            }

        } catch (Exception e) {

        } finally {
            // 对画布内容进行提交
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX(); // 获取手指移动的x坐标
        int y = (int) event.getY(); // 获取手指移动的y坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // 每次down下去重新new一个Path
                mPath = new Path();

                drawPath = new DrawPath();
                drawPath.path = mPath;
                drawPath.paint = mPaint;

                mPath.moveTo(x, y);
                mX = x;
                mY = y;

                invalidate();

                break;

            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                mX = x;
                mY = y;

                invalidate();

                break;

            case MotionEvent.ACTION_UP:
                mPath.lineTo(mX, mY);
                mCanvas.drawPath(mPath, mPaint);

                // 将一条完整的路径保存下来(相当于入栈操作)
                savePathList.add(drawPath);
                mPath = null;// 重新置空
//                mPath=new Path();

                invalidate();

                break;
        }
        return true;
    }

    /**
     * 清空笔画列表
     */
    public void resetPathList() {
        if (savePathList != null && savePathList.size() > 0) {
            savePathList.clear();
        }

        if (redoPathList != null && redoPathList.size() > 0) {
            redoPathList.clear();
        }

        if (mPath != null) {
            mPath.reset();
        }
    }

    /**
     * 重置画布，相当于清空画布
     */
    public void resetCanvas() {
        // 重新设置画布，相当于清空画布
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
    }

    /**
     * 撤销的核心思想就是将画布清空， 将保存下来的Path路径最后一个移除掉， 重新将路径画在画布上面。
     */
    public void undo() {
        // 清空画布，但是如果图片有背景的话，则使用上面的重新初始化的方法，用该方法会将背景清空掉...
        if (savePathList != null && savePathList.size() > 0) {
            // 移除最后一个path,相当于出栈操作
            DrawPath path = savePathList.get(savePathList.size() - 1);
            savePathList.remove(path);

            // 重做（恢复）的笔画列表
            redoPathList.add(path);
            // Collections.reverse(redoPathList);

            Iterator<DrawPath> iter = savePathList.iterator();
            while (iter.hasNext()) {
                DrawPath drawPath = iter.next();
                mCanvas.drawPath(drawPath.path, drawPath.paint);
            }
            invalidate();// 刷新
        }
    }

    /**
     * 重做（恢复）的核心思想就是将撤销的路径保存到另外一个集合里面(栈)， 然后从redo的集合里面取出最顶端对象， 画在画布上面即可。
     */
    public void redo() {
        if (savePathList != null && savePathList.size() >= 0
                && redoPathList != null && redoPathList.size() > 0) {
            // 获取最近撤销的笔画，即撤销列表中最后一个数据
            DrawPath drawPathRemoved = redoPathList
                    .get(redoPathList.size() - 1);
            // 将恢复的笔画从撤销列表中去除
            redoPathList.remove(drawPathRemoved);
            // 将恢复的笔画添加到保存列表中
            savePathList.add(drawPathRemoved);
        }

        // 重新绘制
        Iterator<DrawPath> iter01 = savePathList.iterator();
        while (iter01.hasNext()) {
            DrawPath drawPath = iter01.next();
            mCanvas.drawPath(drawPath.path, drawPath.paint);
        }

        invalidate();// 刷新
    }

    /**
     * 画笔
     */
    @SuppressWarnings("deprecation")
    public void setPaintBrush() {
        mPaint = new Paint();
        mPaint.setStyle(Style.STROKE);
        // mpaint.setStrokeWidth(DensityUtil.px2dip(getContext(), 30));
        mPaint.setStrokeWidth(getPaintSize());// 设置空心边框宽（线的粗细）
        // mPaint.setColor(getPaintColor());
        mPaint.setColor(getResources().getColor(getPaintColor()));

        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 消除拉动，使画面圓滑
        mPaint.setStyle(Style.STROKE); // 设置画笔为空心，否则会是首尾连起来多边形内一块为透明。
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 结合方式，平滑
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 圆头
    }

    /**
     * 橡皮擦
     */
    public void setEraser() {
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(getPaintSize());
        // mPaint.setColor(mPaintColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 圆头
        mPaint.setDither(true);// 消除拉动，使画面圆滑
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 结合方式，平滑
        mPaint.setAlpha(0); // //
        mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    }

    /**
     * 设置画笔颜色
     */
    public void setPaintColor(int color) {
        paintColor = color;
    }

    public int getPaintColor() {
        return this.paintColor;
    }

    public void setPaintSize(float f) {
        paintSize = f;
    }

    public float getPaintSize() {
        return this.paintSize;
    }
}