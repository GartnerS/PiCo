package com.nex.blub.PiCo.charts;

import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.nex.blub.PiCo.utils.HistoryData;

import java.util.ArrayList;
import java.util.List;


public class HistoryChart  {

    private final static int UPPER_LIMIT    = 100;
    private final static int COLOR_TEMP     = Color.rgb(53, 198, 255);
    private final static int COLOR_HUM      = Color.rgb(107, 191, 109);
    private final static String LABEL_TEMP  = "Temperatur";
    private final static String LABEL_HUM   = "Luftfeuchtigkeit";
    private final static String LABEL_AVG   = "Durchschnitt";

    private LineChart chart;

    private List<Entry> entriesTemp = new ArrayList<>();
    private List<Entry> entriesHum  = new ArrayList<>();

    private LineData lineData = new LineData();

    private LineDataSet dataSetTemp = new LineDataSet(null, LABEL_TEMP);
    private LineDataSet dataSetHum  = new LineDataSet(null, LABEL_HUM);

    // Referenzen auf die TextViews, die die statistischen Werte anzeigen
    private TextView minimumView;
    private TextView maximumView;
    private TextView averageView;


    /**
     * Konstruktor
     *
     * @param chart Instanz einer Chart, die für das Anzeigen verantwortlich ist
     */
    public HistoryChart(LineChart chart) {
        this.chart = chart;
        this.chart.getXAxis().setValueFormatter( new AxisValueFormatter());

        this.dataSetTemp.setCircleColor(COLOR_TEMP);
        this.dataSetHum.setCircleColor(COLOR_HUM);

        LegendEntry legendTemp = new LegendEntry();
        legendTemp.formColor = COLOR_TEMP;
        legendTemp.label = LABEL_TEMP;

        LegendEntry legendHum = new LegendEntry();
        legendHum.formColor = COLOR_HUM;
        legendHum.label = LABEL_HUM;

        List<LegendEntry> list = new ArrayList<>();
        list.add(legendTemp);
        list.add(legendHum);

        Legend legend = chart.getLegend();
        legend.setCustom(list);
    }


    /**
     * Zeigt anhand einer Liste von Daten die Chart an
     *
     * @param dataList Liste mit Einzelwerten
     */
    public void setData(List<HistoryData> dataList) {
        this.reset();

        float avgTemp = 0;
        for(HistoryData data : dataList) {
            if (data.getTemperature() < UPPER_LIMIT) {
                this.entriesTemp.add(new Entry(data.getTimestamp().floatValue(), data.getTemperature().floatValue()));
                avgTemp += data.getTemperature().floatValue();
            }

            if (data.getHumidity() < UPPER_LIMIT) {
                this.entriesHum.add(new Entry(data.getTimestamp().floatValue(), data.getHumidity().floatValue()));
            }
        }

        if (this.entriesTemp.size() > 0) {
            avgTemp = avgTemp / this.entriesTemp.size();
        }

        this.dataSetTemp.setValues(entriesTemp);
        this.dataSetHum.setValues(entriesHum);

        this.lineData.addDataSet(dataSetTemp);
        this.lineData.addDataSet(dataSetHum);
        this.chart.setData(this.lineData);

        // Chart neuzeichnen
        this.updateChart();

        if (this.minimumView != null) {
            this.minimumView.setText(LABEL_TEMP + ": " + String.valueOf(this.dataSetTemp.getYMin()));
        }

        if (this.maximumView != null) {
            this.maximumView.setText(LABEL_HUM + ": " + String.valueOf(this.dataSetTemp.getYMax()));
        }

        if (this.averageView != null) {
            this.averageView.setText(LABEL_AVG + ": " + String.valueOf(String.format("%.1f", avgTemp)));
        }
    }


    /**
     * Setzt alle Werte zurück
     */
    private void reset() {
        this.entriesTemp.clear();
        this.entriesHum.clear();
        this.lineData.clearValues();
        this.dataSetTemp.clear();
        this.dataSetHum.clear();
    }


    /**
     * Setzt die Chart als invalide => Chart wird neugezeichnet
     */
    private void updateChart() {
        this.chart.invalidate();
    }


    /**
     * Setzt die TextViews, die die statistischen Werte anzeigen
     *
     * @param minimumView TextView für den Minimalwert
     * @param maximumView TextView für den Maximalwert
     * @param averageView TextView für den Durchschnittswert
     */
    public void setStatisticViews(TextView minimumView, TextView maximumView, TextView averageView) {
        this.minimumView = minimumView;
        this.maximumView = maximumView;
        this.averageView = averageView;
    }
}
