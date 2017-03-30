package com.iigt.myscoleftview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.iigt.myscoleftview.R;

/**
 * Created by zhouheng on 2017/3/28.
 */

public class MyScrollView extends View {
    private static final String TAG = "TouchProgressView";

    private Paint linePaint;
    private Paint pointPaint;

    private int pointRadius = 10;//圆点默认半径,单位px
    private int pointColor = R.color.gray_dft;//圆点默认颜色

    private int lineHeight = 2;//线默认高度,单位px
    private int lineClor = R.color.gray_dft;//线默认颜色

    private int progress = 0;
    private final int PROGRESS_MIN = 0;
    private final int PROGRESS_MAX = 100;

    private OnProgressChangedListener progressChangedListener;
    private int mThisHour;
    private int mThisMin;

    public interface OnProgressChangedListener {
        void onProgressChanged(View view, int progress);
    }

    public MyScrollView(Context context) {
        super(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置百分比
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("progress 不可以小于0 或大于100");
        }
        this.progress = progress;
        invalidate();

        if (progressChangedListener != null) {
            progressChangedListener.onProgressChanged(this, 100-progress);
        }
    }

    /**
     * 设置进度变化监听器
     *
     * @param onProgressChangedListener
     */
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.progressChangedListener = onProgressChangedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getY() < pointRadius) {
            setProgress(PROGRESS_MIN);
            return true;
        } else if (event.getY() > getHeight() - pointRadius) {
            setProgress(PROGRESS_MAX);
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setProgress(calculProgress(event.getY()));
                    return true;
                case MotionEvent.ACTION_MOVE:
                    setProgress(calculProgress(event.getY()));
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "[draw] .. in .. ");
        super.draw(canvas);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeWidth(lineHeight);
        linePaint.setColor(getResources().getColor(lineClor));

        String[] seq = new String[]{"-00", "-01", "-02", "-03", "-04", "-05", "-06", "-07", "-08",
                "-09", "-10", "-11", "-12", "-13", "-14", "-15", "-16", "-17",
                "-18", "-19", "-20", "-21", "-22", "-23", "-24"};


        //因为是以画布Canvas 为draw对象，所以RectF构造函数内的参数是以canvas为边界，而不是屏幕
        //画线以及动画效果的实现
        for(int i = 0; i < seq.length; i++){
            if(i < mThisHour-3 || i > mThisHour+3) {
                linePaint.setTextSize(30);
                canvas.drawText(seq[i], getWidth() / 2, getHeight() / 24 * (24 - i), linePaint);
            }else if(i == mThisHour+3 || i == mThisHour-3){
                linePaint.setTextSize(35);
                canvas.drawText(seq[i], getWidth() / 2 + 10, getHeight() / 24 * (24 - i), linePaint);
            }else if(i == mThisHour+2 || i == mThisHour-2){
                linePaint.setTextSize(40);
                canvas.drawText(seq[i], getWidth() / 2 + 20, getHeight() / 24 * (24 - i), linePaint);
            }else if(i == mThisHour+1 || i == mThisHour-1){
                linePaint.setTextSize(45);
                canvas.drawText(seq[i], getWidth() / 2 + 30, getHeight() / 24 * (24 - i), linePaint);
            }else{
                linePaint.setTextSize(50);
                canvas.drawText(seq[i], getWidth() / 2 + 40, getHeight() / 24 * (24 - i), linePaint);
            }
        }


        //时间效果的实现
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(getResources().getColor(pointColor));
        pointPaint.setTextSize(100);
        canvas.drawText(progressToTime(100 - progress),getWidth()/2+200, getCx()+10, pointPaint);

    }


    /**
     * 将百分比转换成时间
     * @param progress 输入的百分比
     * @return 返回对应的时间
     */
    private String progressToTime(float progress){
        float myMin = (6.0f * 24.0f) / 100;
        float thisTime = progress * myMin;

        mThisHour = ((int)thisTime) / 6;
        mThisMin = ((int)thisTime) % 6;


        return Integer.toString(mThisHour) + ": "+ Integer.toString(mThisMin) + "0";
    }

    /**
     * 获取圆点的x轴坐标
     *
     * @return
     */
    private float getCx() {
        float cx = 0.0f;
        cx = (getHeight() - pointRadius * 2);
        if (cx < 0) {
            throw new IllegalArgumentException("TouchProgressView 宽度不可以小于 2 倍 pointRadius");
        }
        return cx / 100 * (progress) + pointRadius;
    }

    /**
     * 计算触摸点的百分比
     *
     * @param eventX
     * @return
     */
    private int calculProgress(float eventX) {
        float proResult = (eventX - pointRadius) / (getHeight() - pointRadius * 2);
        return (int) (proResult * 100);
    }

}
