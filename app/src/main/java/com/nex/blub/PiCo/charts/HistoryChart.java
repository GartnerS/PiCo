package com.nex.blub.PiCo.charts;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.nex.blub.PiCo.interfaces.ShowHistoryData;
import com.nex.blub.PiCo.utils.HistoryData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HistoryChart  {

    private final static int UPPER_LIMIT    = 100;
    private final static int BOTTOM_LIMIT   = -20;
    private final static int COLOR_TEMP     = Color.rgb(53, 198, 255);
    private final static int COLOR_HUM      = Color.rgb(107, 191, 109);
    private final static String LABEL_TEMP  = "Temperatur";
    private final static String LABEL_HUM   = "Luftfeuchtigkeit";

    private final LineChart chart;
    private final ShowHistoryData view;

    private final List<Entry> entriesTemp = new ArrayList<>();
    private final List<Entry> entriesHum  = new ArrayList<>();

    private final LineData lineData = new LineData();

    private final LineDataSet dataSetTemp = new LineDataSet(null, LABEL_TEMP);
    private final LineDataSet dataSetHum  = new LineDataSet(null, LABEL_HUM);


    /**
     * Konstruktor
     *
     * @param chart Instanz einer Chart, die für das Anzeigen verantwortlich ist
     * @param view Instanz einer ShowHistroyData-View, die für das Anzeigen der statistischen Werte verantwortlich ist
     */
    public HistoryChart(LineChart chart, ShowHistoryData view) {
        this.view   = view;
        this.chart  = chart;
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

        dataList
            .stream()
            .filter(entry -> entry.getHumidity() < UPPER_LIMIT && entry.getTemperature() < UPPER_LIMIT)
            .filter(entry -> entry.getHumidity() > BOTTOM_LIMIT && entry.getTemperature() > BOTTOM_LIMIT)
            .forEach(entry -> {
                this.entriesHum.add(new Entry(entry.getTimestamp().floatValue(), entry.getHumidity().floatValue()));
                this.entriesTemp.add(new Entry(entry.getTimestamp().floatValue(), entry.getTemperature().floatValue()));
            });

        double avgTemp = dataList
                .stream()
                .collect(Collectors.averagingDouble(HistoryData::getTemperature));

        this.dataSetTemp.setValues(entriesTemp);
        this.dataSetHum.setValues(entriesHum);

        this.lineData.addDataSet(dataSetTemp);
        this.lineData.addDataSet(dataSetHum);
        this.chart.setData(this.lineData);

        // Chart neuzeichnen
        this.updateChart();

        if (this.view != null) {
            this.view.setStatisticValuesForTemperature(this.dataSetTemp.getYMin(), this.dataSetTemp.getYMax(), avgTemp);
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
}
