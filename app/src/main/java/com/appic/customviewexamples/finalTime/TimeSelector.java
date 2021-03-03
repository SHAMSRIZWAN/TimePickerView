package com.appic.customviewexamples.finalTime;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.appic.customviewexamples.R;

import java.util.ArrayList;

public class TimeSelector extends View {

    //230
    private int height = 230, width = LinearLayout.LayoutParams.MATCH_PARENT;
    private int fontSize = 0;
    private int mActiveSelection = 12;
    int minutePos = 0;
    private int[] hourNumbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private int[] minuteNumbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59};
    private int[] numbers = hourNumbers;

    //color
    private int timeBgColor = getResources().getColor(R.color.colorGrey);
    private int colonColor = getResources().getColor(R.color.colorDarkGrey);
    private int timeMarkerColor = getResources().getColor(R.color.colorAccent);
    private int timeHourColor = Color.BLACK;
    private int timeSelectedHourColor = Color.WHITE;
    private int mtransParentColor = getResources().getColor(android.R.color.transparent);


    private boolean isInit;
    private boolean isHourSelected = false;
    private boolean shouldNumberDraw = true;

    private float paddingTop = 0;
    private float radius = 0;
    private final float[] mTempResult = new float[2];
    private float markx;
    private float marky;
    private float markerRadius = 0;
    private float endX = 0, endY = 0;
    private float clockCenterX;
    private float clockCenterY;

    private ArrayList<Rect> rects = new ArrayList<>();
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<RectF> rectFS = new ArrayList<>();

    private Paint mHandpaint, mClockCirclepaint, mCenterCirclepaint, mrectPaint, mDigitPaint, mMarkerPaint, mdigitalClockRectPaint;

    final Path handPath = new Path();
    Rect mDigitalHourRect = new Rect();
    Rect mDigitalMinuteRect = new Rect();
    Rect mAmRect = new Rect();
    Rect mPmRect = new Rect();

    private String mDigitalItemSelected = "HOUR", mTimeSlot = "Am";
    private String digitHour, digitMin, amString, pmString;

//    private Calendar calendar = Calendar.getInstance();

    private onTimeSelectionListener timeCallback;
    private TimeSelectorHelper helper;

    public TimeSelector(Context context) {
        super(context);

    }

    public TimeSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    public TimeSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);

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

    @Override
    protected void onDraw(final Canvas canvas) {
        if (!isInit) {
            width = getWidth();
            height = getHeight();

            helper = new TimeSelectorHelper(width, height);
            float[] clockCenter = helper.calculateClockCenter();
            clockCenterX = clockCenter[0];
            clockCenterY = clockCenter[1];

            initClock();
            initPaints();
            handPath.moveTo(clockCenterX, clockCenterY);
            endX = clockCenterX;
            endY = clockCenterY;
            isInit = true;
        }

        if (shouldNumberDraw) {

            drawDigitalClock(canvas);

            drawClockCircle(canvas);
            drawCenterCircle(canvas);
            drawCircularMarkerAndHand(canvas);
            drawNumeral(canvas);

        }

//
//        if (shouldAnimate) {
//            shouldAnimate = false;
//
//            ValueAnimator pathAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
//            handPath.lineTo(endX, endY);
//
//            pathMeasure = new PathMeasure(handPath, true);
//            pathLength = pathMeasure.getLength();
//
//            final float[] position = new float[2];
//
//            pathAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    final float distance = animation.getAnimatedFraction() * pathLength;
//                    pathMeasure.getPosTan(distance, position, null);
//                    handPath.lineTo(position[0], position[1]);
//                    canvas.drawPath(handPath, mHandpaint);
//
//                    invalidate();
//
//                }
//            });
//            pathAnimator.start();
//
//        }
//-------------------------------
//        handPath.lineTo(endX, endY);
//        canvas.drawPath(handPath, mHandpaint);
//
//        float endXPosValue = computeXYForHourPosition(mActiveSelection, radius)[0];
//        float endYPosValue = computeXYForHourPosition(mActiveSelection, radius)[1];
//
//        if (endY >= marky) {
//            postInvalidateDelayed(50);
//
//        }
////        endX++;
//        endY--;


    }

    private void initClock() {

        mActiveSelection = helper.getCurrentHour();
        digitHour = helper.getCurrentHour() + "";
        digitMin = helper.getCurrentMinute() + "";
        mTimeSlot = helper.getTimeSlot();

        paddingTop = helper.measurePaddinginPercent();
        radius = helper.getRadius();
        markerRadius = radius / 4.3f;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, markerRadius / 3,
                getResources().getDisplayMetrics());

    }


    private void initPaints() {
        initClockPaints();
        initDigitalClockPaint();
    }

    private void initClockPaints(){
        mClockCirclepaint = new Paint();
        mClockCirclepaint.setColor(timeBgColor);
        mCenterCirclepaint = new Paint();
        mCenterCirclepaint.setStyle(Paint.Style.FILL);
        mCenterCirclepaint.setColor(timeMarkerColor);

        mHandpaint = new Paint();
        mHandpaint.setColor(timeMarkerColor);
        mHandpaint.setStrokeWidth(5);
        mHandpaint.setStyle(Paint.Style.STROKE);
        mHandpaint.setAntiAlias(true);

        mMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkerPaint.setColor(timeMarkerColor);


    }

    private void initDigitalClockPaint(){
        mrectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mrectPaint.setStyle(Paint.Style.STROKE);
        mrectPaint.setColor(mtransParentColor);

        mDigitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDigitPaint.setColor(timeHourColor);
        mDigitPaint.setTextSize(fontSize);

        mdigitalClockRectPaint = new Paint();
        mdigitalClockRectPaint.setStyle(Paint.Style.STROKE);
        mdigitalClockRectPaint.setColor(mtransParentColor);

//        mDigitPaint.setTypeface(Typeface.create("Aerial", Typeface.NORMAL));
    }


    private void drawDigitalClock(Canvas canvas) {
        drawTopRectangle(canvas);
        drawDigitalHourDigit(canvas);
        drawColon(canvas);
        drawDigitalMinDigit(canvas);
        drawAmRect(canvas);
        drawPmRect(canvas);
    }


    private void drawTopRectangle(Canvas canvas) {
        float rectHeight = height / 7f;

        Paint TopRectPaint = new Paint();
        TopRectPaint.setStyle(Paint.Style.FILL);
        TopRectPaint.setColor(timeMarkerColor);

        canvas.drawRect(0, 0, width, rectHeight, TopRectPaint);

    }

    private void drawDigitalHourDigit(Canvas canvas) {

//        if (!isHourSelected) {
//            digitHour = mActiveSelection + "";
//
//        }

        Paint mdigitalHourPaint = new Paint();
        mdigitalHourPaint.setTextSize(height / 11f);
        if (mDigitalItemSelected.equals("HOUR")) {
            mdigitalHourPaint.setColor(Color.WHITE);
        } else {
            mdigitalHourPaint.setColor(timeBgColor);

        }
        mdigitalHourPaint.getTextBounds(digitHour, 0, digitHour.length(), mDigitalHourRect);

        float measureText = (width / 2.1f) - mdigitalHourPaint.measureText(digitHour);
        float textSize = mdigitalHourPaint.getTextSize();
        float boundsWidth = textSize + (textSize / 6);

        canvas.drawText(digitHour, measureText, boundsWidth, mdigitalHourPaint);

        mDigitalHourRect.offset((int) measureText, (int) boundsWidth);

        canvas.drawRect(mDigitalHourRect, mdigitalClockRectPaint);

    }


    private void drawColon(Canvas canvas) {

        Paint digitalHourPaint = new Paint();
        digitalHourPaint.setColor(colonColor);
        digitalHourPaint.setTextSize(height / 11f);
        String digitHour = ":";

        float textX = (width / 2.1f);
        float textSize = digitalHourPaint.getTextSize();
        float textY = textSize + (textSize / 6);

        canvas.drawText(digitHour, textX, textY, digitalHourPaint);

    }


    private void drawDigitalMinDigit(Canvas canvas) {
//
        if (digitMin.length() == 1) {
            digitMin = "0" + digitMin;

        }

        Paint digitalMinPaint = new Paint();

        if (mDigitalItemSelected.equals("MIN")) {
            digitalMinPaint.setColor(Color.WHITE);
        } else {
            digitalMinPaint.setColor(timeBgColor);

        }

        digitalMinPaint.setTextSize(height / 11f);

        digitalMinPaint.getTextBounds(digitMin, 0, digitMin.length(), mDigitalMinuteRect);


        float textX = (width / 1.95f);
        float textSize = digitalMinPaint.getTextSize();
        float textY = textSize + (textSize / 6);

        canvas.drawText(digitMin, textX, textY, digitalMinPaint);

        mDigitalMinuteRect.offset((int) textX, (int) textY);


        canvas.drawRect(mDigitalMinuteRect, mdigitalClockRectPaint);

    }

    private void drawAmRect(Canvas canvas) {


        amString = "Am";

        Paint amPaint = new Paint();
        amPaint.setTextSize(height / 45f);
        amPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        if (mTimeSlot.equals("Am")) {
            amPaint.setColor(Color.WHITE);
        } else {
            amPaint.setColor(timeBgColor);

        }
        float textX = (width / 1.4f);
        float textSize = amPaint.getTextSize();
        float textY = textSize * 3;

        canvas.drawText(amString, textX, textY, amPaint);

        amPaint.getTextBounds(amString, 0, amString.length(), mAmRect);

        mAmRect.left = (int) textX - 10;
        mAmRect.top = (int) ((int) textY - amPaint.measureText(amString) / 1.3);
        mAmRect.right = (int) ((int) textX + amPaint.measureText(amString) + 10);
        mAmRect.bottom = (int) ((int) textY + amPaint.measureText(amString) / 3.1);


        canvas.drawRect(mAmRect, mdigitalClockRectPaint);

    }

    private void drawPmRect(Canvas canvas) {

        pmString = "Pm";
        Paint pmPaint = new Paint();
        pmPaint.setTextSize(height / 45f);
        pmPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        if (mTimeSlot.equals("Pm")) {
            pmPaint.setColor(Color.WHITE);
        } else {
            pmPaint.setColor(timeBgColor);

        }

        float textX = (width / 1.4f);
        float textSize = pmPaint.getTextSize();
        float textY = textSize * 4.5f;

        canvas.drawText(pmString, textX, textY, pmPaint);

        pmPaint.getTextBounds(pmString, 0, pmString.length(), mPmRect);

        mPmRect.left = (int) textX - 10;
        mPmRect.top = (int) ((int) textY - pmPaint.measureText(pmString) / 1.4);
        mPmRect.right = (int) ((int) textX + pmPaint.measureText(pmString) + 10);
        mPmRect.bottom = (int) ((int) textY + pmPaint.measureText(pmString) / 3);


        canvas.drawRect(mPmRect, mdigitalClockRectPaint);
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
        float clockCenterX = helper.getCenterCircleX();
        float clockCenterY = helper.getCenterCircleY();
        float clockRadius=helper.getClockRadius();

        canvas.drawCircle(clockCenterX, clockCenterY, clockRadius, mClockCirclepaint);

    }


    private void drawCenterCircle(Canvas canvas) {
        canvas.drawCircle(clockCenterX, clockCenterY, 12, mCenterCirclepaint);
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
        canvas.drawLine(clockCenterX, clockCenterY, markx, marky, mHandpaint);


    }


    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{pathLength, pathLength},
                Math.max(phase * pathLength, offset));
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
            minutePos = hourPos;
            Point point = decideNumbersToDrawAndReturnXY(canvas, hourPos, rectBounds);

            points.add(point);
            rects.add(rectBounds);


        }


    }


    private Point decideNumbersToDrawAndReturnXY(Canvas canvas, int number, Rect rectBounds) {
        Point point = null;
        int centerx = width / 2;
        int centery = height / 3;


        if (isHourSelected) {

            point = calculateXYforMinuteText(number, rectBounds);
            int x = point.x;
            int y = point.y;
            decideColorForText(number);

            if (number % 10 == 0 || number % 10 == 5) {

//                String strNo=number+"";
//                if(strNo.length()==1)
//                    strNo="0"+number;
                canvas.drawText(number + "", x, y, mDigitPaint);

                Path path = new Path();
                path.moveTo(centerx, centery);
                path.lineTo(x + rectBounds.centerX(), y + rectBounds.centerY());
                canvas.drawPath(path, mrectPaint);

                RectF rectF = new RectF();
                path.computeBounds(rectF, true);
                rectFS.add(rectF);

            } else {
                canvas.drawRect(x, y,
                        x + (rectBounds.width()), y - (rectBounds.height()), mrectPaint);
                if (mActiveSelection == number) {
                    mDigitPaint.setColor(Color.WHITE);
                    canvas.drawCircle(x + rectBounds.centerX(), y + rectBounds.centerY(), 7, mDigitPaint);

                }

                //this is for testing invisible path
                Path path = new Path();
                path.moveTo(centerx, centery);
                path.lineTo(x + rectBounds.centerX(), y + rectBounds.centerY());
                canvas.drawPath(path, mrectPaint);

                RectF rectF = new RectF();
                path.computeBounds(rectF, true);
                rectFS.add(rectF);
//                canvas.drawLine(centerx, centery, x + rectBounds.centerX(), y + rectBounds.centerY(), mrectPaint);

            }


        } else {


            point = calculateXYforHour(number, rectBounds);
            int x = point.x;
            int y = point.y;
            decideColorForText(number);

            canvas.drawText(number + "", x, y, mDigitPaint);
            canvas.drawRect(x - (rectBounds.width()), y + (rectBounds.height()),
                    x + (rectBounds.width() + rectBounds.width()), y - (rectBounds.height() + rectBounds.height()), mrectPaint);


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

        int x = (int) (clockCenterX + Math.cos(angle) * radius - rectBounds.width() / 2);
        int y = (int) (clockCenterY + Math.sin(angle) * radius + rectBounds.height() / 2);

        return new Point(x, y);
    }

    private Point calculateXYforMinuteText(int number, Rect rectBounds) {
        double angle = Math.PI / 30 * (number - 15);//starting digit of angle radiants

        int x = (int) (clockCenterX + Math.cos(angle) * radius - rectBounds.width() / 2);
        int y = (int) (clockCenterY + Math.sin(angle) * radius + rectBounds.height() / 2);

        return new Point(x, y);
    }

    private float[] computeXYForHourPosition
            (final int pos, final float radius) {
        float[] result = mTempResult;
//        Double startAngle = Math.PI * (9 / 8d);   // Angles are in radians.
        double startAngle = Math.PI * (12 / 8d);   // Angles are in radians.
        double angle = startAngle + (pos * (Math.PI / 6));
        result[0] = (float) (radius * Math.cos(angle)) + clockCenterX;
        result[1] = (float) (radius * Math.sin(angle)) + clockCenterY;
        return result;
    }

    private float[] computeXYForMinPosition(final int pos, final float radius) {
        float[] result = mTempResult;
//        Double startAngle = Math.PI * (9 / 8d);   // Angles are in radians.
        double startAngle = Math.PI * (12 / 8d);   // Angles are in radians.
        double angle = startAngle + (pos * (Math.PI / 30));
        result[0] = (float) (radius * Math.cos(angle)) + clockCenterX;
        result[1] = (float) (radius * Math.sin(angle)) + clockCenterY;
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

        }
        else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (isTouchInsideRectCordinates(x, y)) {
//
                if (!isHourSelected) {
                    numbers = minuteNumbers;
                    isHourSelected = true;
                    mDigitalItemSelected = "MIN";

                }

                handPath.reset();
                handPath.moveTo(width / 2, height / 2);

                endX = width / 2;
                endY = height / 2;


                invalidate();
                return true;

            } else if (mDigitalHourRect.contains(x, y)) {
                mDigitalItemSelected = "HOUR";
                numbers = hourNumbers;
                isHourSelected = false;
                mActiveSelection = Integer.parseInt(digitHour);
                invalidate();

                Log.wtf("mDigitalHourRect", "text");
            }
            else if (mDigitalMinuteRect.contains(x, y)) {

                mDigitalItemSelected = "MIN";
                numbers = minuteNumbers;
                isHourSelected = true;
//                mActiveSelection = calendar.get(Calendar.MINUTE);
                mActiveSelection = Integer.parseInt(digitMin);

                invalidate();

                Log.wtf("mDigitalMinuteRect", "text");

            }
            else if (mAmRect.contains(x, y)) {
                mTimeSlot = "Am";
                invalidate();

                Log.wtf("mAmRect", "text");
            } else if (mPmRect.contains(x, y)) {
                mTimeSlot = "Pm";
                invalidate();

                Log.wtf("mPmRect", "text");
            }
        }

        performClick();

        return true;
    }


    private boolean isTouchInsideRectCordinates(int x, int y) {

        for (int i = 0; i < numbers.length; i++) {

            Point point = points.get(i);
            Rect rect = rects.get(i);

            if (calculateTouchIfInsideRect(rect, point, x, y, i)) {

                mActiveSelection = numbers[i];

                if (!isHourSelected) {
                    digitHour = mActiveSelection + "";
                } else {
                    digitMin = mActiveSelection + "";

                }
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


    private boolean calculateTouchIfInsideRect(Rect rect, Point point, int x, int y, int pos) {

        if (isHourSelected) {
            float startingXofLeftWidth = point.x + rect.centerX() - ((float) rect.width() / 2);

            float endingYofTopRectHeight = point.y + rect.centerY() - ((float) rect.height() / 2);
            float endingXofRightRectWidth = point.x + rect.centerX() + ((float) rect.width() / 2);

            float endingYofBottomHeight = point.y + rect.centerY() + ((float) rect.height() / 2);


            if (x >= startingXofLeftWidth && x <= endingXofRightRectWidth
                    && y >= endingYofTopRectHeight && y <= endingYofBottomHeight
                    || rectFS.get(pos).contains(x, y)) {

                Log.wtf("touch is in bound", x + " " + y);

                return true;
            }

        } else {
            int rectCenterOfWidth = rect.width() / 2;
            float startingXofLeftWidth = point.x - rectCenterOfWidth;

            float endingXofRightRectWidth = point.x + (rect.width() + rect.width());
            float endingYofTopRectHeight = point.y - (rect.height() + rect.height());

            float endingYofBottomHeight = point.y + (rect.height() + rect.height());

            if (x >= startingXofLeftWidth && x <= endingXofRightRectWidth
                    && y >= endingYofTopRectHeight && y <= endingYofBottomHeight) {

                Log.wtf("touch is in bound", x + " " + y);

                return true;
            }
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


    public interface onTimeSelectionListener {

        void onHourTimeSelected(String hour);

        void onMinuteTimeSelected(String minute);

    }
}