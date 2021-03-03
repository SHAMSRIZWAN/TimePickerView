package com.appic.customviewexamples;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TimeSelectorPath extends View {

    private int height = 230, width = LinearLayout.LayoutParams.MATCH_PARENT;
    private int fontSize = 0;

    private int[] hourNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private int[] minuteNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60};
    private int[] numbers = hourNumbers;
    private int timeBgColor = getResources().getColor(R.color.colorGrey);
    private int timeMarkerColor = getResources().getColor(R.color.colorAccent);
    private int timeHourColor = Color.BLACK;
    private int timeSelectedHourColor = Color.WHITE;
    private int mActiveSelection = 12;
    int minutePos = 0;

    private boolean isInit;
    private boolean isHourSelected = false;

    private float padding = 0;
    private float radius = 0;
    private final float[] mTempResult = new float[2];

    float markx;
    float marky;
    float markerRadius = 0;

    private ArrayList<Rect> rects = new ArrayList<>();
    private ArrayList<Point> points = new ArrayList<>();

    private Paint mHandpaint, mClockCirclepaint, mCenterCirclepaint, mrectPaint, mDigitPaint, mMarkerPaint;


    private onTimeSelectionListener timeCallback;


    public TimeSelectorPath(Context context) {
        super(context);
    }

    public TimeSelectorPath(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TimeSelectorPath(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initClock() {
        height = getHeight();
        width = getWidth();

        int min = Math.min(height, width);
        padding = (int) (min / 2) / 4.29f;
        Log.wtf("padding", padding + "");
        radius = min / 2 - padding;
        markerRadius = radius / 4.3f;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, markerRadius / 3,
                getResources().getDisplayMetrics());

    }

    private void initAttr(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.TimeSelector,
                    0, 0);

            timeBgColor = typedArray.getColor(R.styleable.TimeSelector_timeBgColor,
                    timeBgColor);

            timeHourColor = typedArray.getColor(R.styleable.TimeSelector_timeHourColor,
                    timeHourColor);

            timeSelectedHourColor = typedArray.getColor(R.styleable.TimeSelector_timeSelectedHourColor,
                    timeSelectedHourColor);

            timeMarkerColor = typedArray.getColor(R.styleable.TimeSelector_timeMarkerColor,
                    timeMarkerColor);


            typedArray.recycle();
        }
    }

    private void initPaints() {
        mHandpaint = new Paint();
        mHandpaint.setColor(timeMarkerColor);
        mHandpaint.setStrokeWidth(5);
        mHandpaint.setStyle(Paint.Style.STROKE);
        mHandpaint.setAntiAlias(true);

        mClockCirclepaint = new Paint();
        mClockCirclepaint.setColor(timeBgColor);

        mCenterCirclepaint = new Paint();
        mCenterCirclepaint.setStyle(Paint.Style.FILL);
        mCenterCirclepaint.setColor(timeMarkerColor);


        mMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkerPaint.setColor(timeMarkerColor);

        mrectPaint = new Paint();
        mrectPaint.setStyle(Paint.Style.STROKE);
//        mrectPaint.setColor(getResources().getColor(android.R.color.transparent));

        mDigitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDigitPaint.setTextAlign(Paint.Align.CENTER);

        mDigitPaint.setColor(timeHourColor);
        mDigitPaint.setTextSize(fontSize);


//        mDigitPaint.setTypeface(Typeface.create("Aerial", Typeface.NORMAL));


    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initClock();
            initPaints();
            isInit = true;
        }

        drawClockCircle(canvas);
        drawCenterCircle(canvas);
        drawCircularMarkerAndHand(canvas);
        drawNumeral(canvas);
    }

    public void setMarkx(float markx) {
        this.markx = markx;
    }

    public void setMarky(float marky) {
        this.marky = marky;
    }

    public void setMarkerRadius(float markerRadius) {
        this.markerRadius = markerRadius;
    }


    private void drawClockCircle(Canvas canvas) {

//        canvas.drawCircle(width / 2, height / 2, radius + padding - 10, mClockCirclepaint);
        canvas.drawCircle((float)width / 2, (float)height / 2, radius+fontSize, mClockCirclepaint);

    }

    private void drawCenterCircle(Canvas canvas) {
        canvas.drawCircle((float)width / 2, (float)height / 2, 12, mCenterCirclepaint);
    }


    private void drawCircularMarkerAndHand(Canvas canvas) {

        float[] markerxyData = null;

        if (isHourSelected) {
            markerxyData = computeXYForMinPosition(mActiveSelection, radius);
        } else {
            markerxyData = computeXYForHourPosition(mActiveSelection, radius);
        }

        markx = markerxyData[0];
        marky = markerxyData[1];

        canvas.drawCircle(markx, marky, markerRadius, mMarkerPaint);
        canvas.drawLine(markx, marky, width / 2, height / 2, mHandpaint);
//        path=new Path();
//        path.moveTo((float) width / 2, (float) height / 2);
//        path.lineTo(markx,marky);
//        canvas.drawPath(path,mHandpaint);
//                ObjectAnimator animator = ObjectAnimator.ofFloat(this,"radius", 0, 50);
//                animator.setInterpolator(new AccelerateInterpolator());
//                animator.setDuration(400);
//                animator.start();

    }

    private void drawNumeral(Canvas canvas) {
        points.clear();
        rects.clear();
        minutePos = 0;

        for (int hourPos : numbers) {

            String strNo = String.valueOf(hourPos);
            final Rect rectBounds = new Rect();


            mDigitPaint.getTextBounds(strNo, 0, strNo.length(), rectBounds);
//            float textWidth = mDigitPaint.measureText(strNo);

            Point point = decideNumbersToDrawAndReturnXY(canvas, hourPos, rectBounds);

            points.add(point);
            rects.add(rectBounds);

            minutePos = hourPos;


        }


    }

    private Point decideNumbersToDrawAndReturnXY(Canvas canvas, int number, Rect rectBounds) {
        Point point = null;

        if (isHourSelected) {

            point = calculateXYforMinuteText(number, rectBounds);
            int x = point.x;
            int y = point.y;
            decideColorForText(number);

            if (number % 10 == 0 || number % 10 == 5) {


//                canvas.drawRect(x - ((float)rectBounds.width()/2), y + ((float)rectBounds.height()/2),
//                        x + ((float)rectBounds.width()/2), y - ((float)rectBounds.height()), mrectPaint);

                canvas.drawCircle(x, y , 3, mDigitPaint);

                canvas.drawText(number + "", x, y, mDigitPaint);


            } else {

                if (mActiveSelection == number) {
                    canvas.drawCircle(x + rectBounds.centerX(), y + rectBounds.centerY(), 3, mDigitPaint);

                }

                canvas.drawRect((x + rectBounds.centerX()) - 8, (y + rectBounds.centerY()) - 8,
                        (x + rectBounds.centerX()) + 8, (y + rectBounds.centerY()) + 8, mrectPaint);
//                canvas.drawCircle(x + rectBounds.centerX(), y + rectBounds.centerY(), 3, mDigitPaint);


            }
        } else {


            point = calculateXYforHour(number, rectBounds);
            int x = point.x;
            int y = point.y;
            decideColorForText(number);

            canvas.drawCircle(x, y , 5, mDigitPaint);

            canvas.drawText(number + "", x, y, mDigitPaint);

//            canvas.drawRect(x - (rectBounds.width()), y + ((float)rectBounds.height()/2),
//                    x + (rectBounds.width()), y - (rectBounds.height()+(float)rectBounds.height()/2), mrectPaint);
//           canvas.drawRect(x - (rectBounds.width()), y + (rectBounds.height()),
//                    x + (rectBounds.width() + rectBounds.width()), y - (rectBounds.height() + rectBounds.height()), mrectPaint);


        }

        return point;
    }

    private void decideColorForText(int number) {
        if (number == mActiveSelection) {
            mDigitPaint.setColor(timeSelectedHourColor);
        } else {
            mDigitPaint.setColor(timeHourColor);
        }

    }

    private Point calculateXYforHour(int number, Rect rectBounds) {
        double angle = Math.PI / 6 * (number - 3);//starting digit of angle radiants

        int x = (int) (width / 2 + Math.cos(angle) * radius - rectBounds.width() / 2);
        int y = (int) (height / 2 + Math.sin(angle) * radius + rectBounds.height() / 2);

        return new Point(x, y);
    }

    private Point calculateXYforMinuteText(int number, Rect rectBounds) {
        double angle = Math.PI / 30 * (number - 15);//starting digit of angle radiants

        int x = (int) (width / 2 + Math.cos(angle) * radius - rectBounds.width() / 2);
        int y = (int) (height / 2 + Math.sin(angle) * radius + rectBounds.height() / 2);

        return new Point(x, y);
    }

    private float[] computeXYForHourPosition
            (final int pos, final float radius) {
        float[] result = mTempResult;
//        Double startAngle = Math.PI * (9 / 8d);   // Angles are in radians.
        double startAngle = Math.PI * (12 / 8d);   // Angles are in radians.
        double angle = startAngle + (pos * (Math.PI / 6));
        result[0] = (float) (radius * Math.cos(angle)) + ((float) width / 2);
        result[1] = (float) (radius * Math.sin(angle)) + ((float) height / 2);
        return result;
    }

    private float[] computeXYForMinPosition(final int pos, final float radius) {
        float[] result = mTempResult;
//        Double startAngle = Math.PI * (9 / 8d);   // Angles are in radians.
        double startAngle = Math.PI * (12 / 8d);   // Angles are in radians.
        double angle = startAngle + (pos * (Math.PI / 30));
        result[0] = (float) (radius * Math.cos(angle)) + (width / 2);
        result[1] = (float) (radius * Math.sin(angle)) + (height / 2);
        return result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

            if (isTouchInsideRectCordinates(x, y)) {

                invalidate();

            }
            return true;

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

            if (isTouchInsideRectCordinates(x, y)) {

//                if (!isHourSelected) {
//                    numbers = minuteNumbers;
//                    isHourSelected = true;
//                    mActiveSelection = 0;
//
//                }
                invalidate();

                return true;

            }
        }
        performClick();

        return true;
    }

    private boolean isTouchInsideRectCordinates(int x, int y) {

        for (int i = 0; i < numbers.length; i++) {

            Point point = points.get(i);
            Rect rect = rects.get(i);

            if (calculateTouchIfInsideRect(rect, point, x, y)) {

//                mActiveSelection = i+1;
                mActiveSelection = numbers[i];
                Log.wtf("activeSelection", mActiveSelection + "");

                if (timeCallback != null) {
                    if (!isHourSelected) {

                        timeCallback.onHourTimeSelected(String.valueOf(mActiveSelection));
                    } else {

                        timeCallback.onMinuteTimeSelected(String.valueOf(mActiveSelection));
                    }
                }

                return true;

            }

        }

        return false;
    }

    private boolean calculateTouchIfInsideRect(Rect rect, Point point, int x, int y) {


//        if (!isHourSelected) {

        int rectCenterOfWidth = rect.width();
        float startingXofLeftWidth = point.x - rectCenterOfWidth;

        float endingXofRightRectWidth = point.x + (rect.width() + rect.width());
        float endingYofTopRectHeight = point.y - (rect.height() + rect.height());

        float endingYofBottomHeight = point.y + (rect.height() + rect.height());

//        if (i == 0) {
//
//            endingXofRightRectWidth = point.x;
//            endingYofBottomHeight = point.y;
//
//
//        }


        if (x >= startingXofLeftWidth && x <= endingXofRightRectWidth
                && y >= endingYofTopRectHeight && y <= endingYofBottomHeight) {

            Log.wtf("touch is in bound", x + " " + y);

            return true;
        }


        return false;
    }


    @Override
    public boolean performClick() {
        super.performClick();


        return true;
    }

    public void setItemSelectedListener(onTimeSelectionListener itemSelectedListener) {

        timeCallback = itemSelectedListener;

    }

    interface onTimeSelectionListener {

        void onHourTimeSelected(String hour);

        void onMinuteTimeSelected(String minute);

    }
}