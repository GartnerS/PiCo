package com.nex.blub.PiCo.charts;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AxisValueFormatter implements IAxisValueFormatter
{
    // Log-Tag für diese Klasse
    private static final String TAG = "AxisValueFormatter";

    private DateFormat mDataFormat;
    private Date mDate;


    public AxisValueFormatter() {
        this.mDataFormat = new SimpleDateFormat("dd.MM. HH:mm", Locale.GERMAN);
        this.mDate = new Date();
    }


    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return formatiertes Label
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return format((long) value * 1000);
    }


    private String format(long timestamp){
        try {
            mDate.setTime(timestamp);
            return mDataFormat.format(mDate);
        } catch(Exception ex){
            Log.e(TAG, ex.getMessage());
            return "0";
        }
    }
}

