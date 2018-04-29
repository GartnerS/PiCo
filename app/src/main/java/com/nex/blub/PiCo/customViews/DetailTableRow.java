package com.nex.blub.PiCo.customViews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.nex.blub.PiCo.R;
import org.json.JSONObject;


/**
 * Eigene Erweiterung einer TableRow, die zur Anzeige der Detailwerte eines Device anzeigt.
 */
public class DetailTableRow extends TableRow {

    // TextView für die Anzeige des Datums
    private TextView date;

    // TextViews für die Anzeige der Temperaturwerte
    private TextView minTemp;
    private TextView maxTemp;
    private TextView avgTemp;

    // TextViews für die Anzeige der Luftfeuchtigkeitswerte
    private TextView minHum;
    private TextView maxHum;
    private TextView avgHum;

    // Schriftgröße der Werte
    private int textSizeofValues = 16;


    /**
     * Konstruktor, wird so benötigt
     *
     * @param context der aktulle Context
     * @param attrs Set von aktuellen Werten, die angezeigt werden sollen
     */
    public DetailTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Tabelle, die die Werte gruppiert
        TableLayout innerTable;

        TableRow row1;
        TableRow row2;
        TableRow row3;

        this.setPadding(15, 15, 15, 15);
        this.setBackground(ContextCompat.getDrawable(context, R.drawable.tablerow_border));

        this.date = new TextView(context);

        this.minTemp = new TextView(context);
        this.maxTemp = new TextView(context);
        this.avgTemp = new TextView(context);

        this.minHum = new TextView(context);
        this.maxHum = new TextView(context);
        this.avgHum = new TextView(context);

        this.formatTempTextViews(new TextView[]{
                this.maxTemp,
                this.minTemp,
                this.avgTemp
        });

        this.formatHumTextViews(new TextView[] {
                this.maxHum,
                this.minHum,
                this.avgHum
        });

        row1 = new TableRow(context);
        row1.setLayoutDirection(LAYOUT_DIRECTION_RTL);
        row2 = new TableRow(context);
        row2.setLayoutDirection(LAYOUT_DIRECTION_RTL);
        row3 = new TableRow(context);
        row3.setLayoutDirection(LAYOUT_DIRECTION_RTL);

        row1.addView(this.maxHum);
        row1.addView(this.maxTemp);

        row2.addView(this.avgHum);
        row2.addView(this.avgTemp);

        row3.addView(this.minHum);
        row3.addView(this.minTemp);

        innerTable = new TableLayout(context);

        innerTable.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.5f));
        innerTable.addView(row1);
        innerTable.addView(row2);
        innerTable.addView(row3);

        this.addView(date);
        this.addView(innerTable);
    }


    private void formatTempTextViews(TextView[] tempViews) {

        for (TextView view : tempViews) {
            view.setText("-");
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, this.textSizeofValues);
            view.setGravity(Gravity.CENTER);
        }
    }


    private void formatHumTextViews(TextView[] humViews) {
        for (TextView view : humViews) {
            view.setText("-");
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, this.textSizeofValues);
            view.setGravity(Gravity.END);
            view.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.075f));
        }
    }


    @SuppressWarnings("unused")
    public void setValues(String date, JSONObject temperatur, JSONObject humidity) {
        try {
            this.date.setText(date);

            // Temperatur-Werte setzen
            this.maxTemp.setText(String.valueOf(temperatur.getDouble("max")));
            this.minTemp.setText(String.valueOf(temperatur.getDouble("min")));
            this.avgTemp.setText(String.valueOf(temperatur.getDouble("avg")));

            // Luftfeuchtigkeitswerte setzen
            this.maxHum.setText(String.valueOf(humidity.getDouble("max")));
            this.minHum.setText(String.valueOf(humidity.getDouble("min")));
            this.avgHum.setText(String.valueOf(humidity.getDouble("avg")));
        } catch(Exception e) {
            Log.e("DetailTableRow", e.getMessage());
        }
    }
}
