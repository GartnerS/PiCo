package com.nex.blub.PiCo.customViews;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
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

    // Tabelle, die die Werte gruppiert
    private TableLayout innerTable;

    private TableRow row1;
    private TableRow row2;
    private TableRow row3;


    // Schriftgröße der Werte
    private int textSizeofValues = 16;

    // Farbe der Werte
    private int colorOfValues = Color.BLACK;


    /**
     * Konstruktor, wird so benötigt
     *
     * @param context
     * @param attrs
     */
    public DetailTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);

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

        this.row1 = new TableRow(context);
        this.row1.setLayoutDirection(LAYOUT_DIRECTION_RTL);
        this.row2 = new TableRow(context);
        this.row2.setLayoutDirection(LAYOUT_DIRECTION_RTL);
        this.row3 = new TableRow(context);
        this.row3.setLayoutDirection(LAYOUT_DIRECTION_RTL);

        this.row1.addView(this.maxHum);
        this.row1.addView(this.maxTemp);

        this.row2.addView(this.avgHum);
        this.row2.addView(this.avgTemp);

        this.row3.addView(this.minHum);
        this.row3.addView(this.minTemp);

        this.innerTable = new TableLayout(context);

        this.innerTable.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.5f));
        this.innerTable.addView(this.row1);
        this.innerTable.addView(this.row2);
        this.innerTable.addView(this.row3);

        this.addView(date);
        this.addView(this.innerTable);
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
            view.setGravity(Gravity.RIGHT);
            view.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0.075f));
        }
    }


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
        } catch(Exception e) {}
    }
}
