package com.appic.customtimepicker;

import android.util.Log;

import java.util.Calendar;

public class TimeSelectorHelper {

    private float centerCircleX;
    private float centerCircleY;
    private float minValueBetweenXY;

    private int width;
    private int height;
    private int currentHour;
    private int currentMinute;

    private Calendar calendar = Calendar.getInstance();

    public TimeSelectorHelper(int width, int height) {
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        initTime();
    }

    private void initTime() {
        currentHour = calendar.get(Calendar.HOUR);
        currentMinute = calendar.get(Calendar.MINUTE);

    }

    public float[] calculateClockCenter() {
        centerCircleX = width / 2f;
        centerCircleY = height / 3f;
        return new float[]{centerCircleX, centerCircleY};

    }

    public float getCenterCircleX() {
        return centerCircleX;
    }

    public float getCenterCircleY() {
        return centerCircleY;
    }

    public int getCurrentHour() {

        return currentHour;
    }

    public int getCurrentMinute() {
        return currentMinute;
    }

    public String getTimeSlot() {

        if (currentHour < 12) {
            return "Am";
        } else {
            return "Pm";
        }

    }

    public float getRadius() {

        return minValueBetweenXY / 2 - measurePaddinginPercent();
    }

    public float measurePaddinginPercent() {

        minValueBetweenXY = Math.min(width / 1.68f, height / 2f);
        float paddingPercent = (int) (minValueBetweenXY / 2) / 4.29f;
        Log.wtf("paddingPercent", paddingPercent + "");
        return paddingPercent;
    }

    public float getClockRadius(){

     return getRadius()+measurePaddinginPercent()-10;

    }

}
