package com.appic.customviewexamples.wavepath;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class WaveView extends View {


    private Path path1;
    private Path path2;

    private Path path3;
    private Path path4;
    private Paint paint1;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;

    private int mWidth;
    private int mHeight;
    float tranlateWidth;
    ValueAnimator mValueAnimator;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }



    public void init(){
        paint1=new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setColor(Color.RED);

        paint2=new Paint();
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(Color.BLACK);

        paint3=new Paint();
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setColor(Color.BLUE);

        paint4=new Paint();
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setColor(Color.GREEN);

        path1=new Path();
        path2=new Path();

        path3=new Path();
        path4=new Path();

    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mValueAnimator = ValueAnimator.ofInt(0,1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tranlateWidth = valueAnimator.getAnimatedFraction() * mWidth;
                invalidate();
                System.out.println("update...");
            }
        });
        mValueAnimator.setDuration(1 << 10);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    public void start() {
        mValueAnimator.start();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getWidth();
        mHeight = getHeight();
        float waveHeight = (float) (mWidth / 5);
//
        path1.moveTo(-mWidth, 0);

        path1.rCubicTo(mWidth / 5 * 1, -waveHeight, mWidth / 5 * 3, waveHeight, mWidth, 0);
        path1.rCubicTo(mWidth / 5 * 1, -waveHeight, mWidth / 5 * 3, waveHeight, mWidth, 0);

        path1.rLineTo(0, mHeight >> 1);
        path1.rLineTo(-mWidth << 1, 0);
        path1.close();


        path2.moveTo(-mWidth, 0);

        path2.rCubicTo(mWidth / 4 * 1, -waveHeight, mWidth / 4 * 3, waveHeight, mWidth, 0);
        path2.rCubicTo(mWidth / 4 * 1, -waveHeight, mWidth / 4 * 3, waveHeight, mWidth, 0);

        path2.rLineTo(0, mHeight >> 1);
        path2.rLineTo(-mWidth << 1, 0);
        path2.close();


        path3.moveTo(mWidth, 0);

        path3.rCubicTo(mWidth / 5 * 1, waveHeight, mWidth / 5 * 3, -waveHeight, mWidth, 0);
        path3.rCubicTo(mWidth / 5 * 1, waveHeight, mWidth / 5 * 3, -waveHeight, mWidth, 0);

        path3.rLineTo(0, mHeight >> 1);
        path3.rLineTo(mWidth >> 1, 0);
        path3.close();


//        path4.moveTo(-mWidth, 0);
//
//        path4.rCubicTo(mWidth / 4 * 1, waveHeight, mWidth / 4 * 3, -waveHeight, mWidth, 0);
//        path4.rCubicTo(mWidth / 4 * 1, waveHeight, mWidth / 4 * 3, -waveHeight, mWidth, 0);
//
//        path4.rLineTo(0, mHeight >> 1);
//        path4.rLineTo(-mWidth << 1, 0);
//        path4.close();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(tranlateWidth, mHeight/2);

        canvas.drawPath(path1,paint1);
        canvas.drawPath(path2,paint2);
        canvas.drawPath(path3,paint3);
        canvas.drawPath(path4,paint4);


    }
}
