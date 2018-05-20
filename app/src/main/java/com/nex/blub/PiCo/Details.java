package com.nex.blub.PiCo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.nex.blub.PiCo.charts.HistoryChart;
import com.nex.blub.PiCo.devices.Temperatur;
import com.nex.blub.PiCo.interfaces.HasHistoryData;
import com.nex.blub.PiCo.interfaces.ShowHistoryData;

import java.util.Locale;


public class Details extends Activity implements ShowHistoryData {

    // Log-Tag für diese Klasse
    private static final String TAG = "Details";

    // Name des Devices und Titel der View
    private String title;

    // Referenz auf das Device, dessen historische Daten angezeigt werden
    private HasHistoryData device;

    // Referenz auf die Chart, die die Daten visualisiert
    private HistoryChart chart;

    // Referenz auf die Eingabemaske für die Anzahl der Tage
    private EditText daysRangeSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.title = (String) bundle.get("Device_Name");
        }

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.daysRangeSelect = findViewById(R.id.daysRange);
        int days = Integer.valueOf(this.daysRangeSelect.getText().toString());

        this.chart = new HistoryChart((LineChart) findViewById(R.id.chart), this);


        this.device = new Temperatur(this.title);
        this.device.registerViewToShowData(this);
        this.requestData(days);

        this.daysRangeSelect.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                Log.i(TAG, value);

                if (value.isEmpty()) {
                    Log.i(TAG, "Eingegebener Wert ist leer.");
                    return;
                }

                device.getHistory(Integer.valueOf(s.toString()));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }


    /**
     * Abfrage der Daten
     *
     * @param days Anzahl der Tage, die abgefragt werden sollen
     */
    private void requestData(int days) {
        this.device.getHistory(days);
    }


    /**
     * Abgefragte Werte im Diagramm anzeigen
     */
    public void show() {
        if (this.device == null || this.device.getHistoryData() == null) {
            Log.i(TAG, "Kein Device angegeben.");
            return;
        }

        this.chart.setData(this.device.getHistoryData());
    }


    /**
     * Zurück-Button im Menu
     *
     * @param item Eintrag, der angeklickt wurde
     * @return true
     */
    public boolean onOptionsItemSelected(MenuItem item){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(mainIntent, 0);
        return true;
    }


    /**
     * Setzt die statistischen Werte für Temperatur
     *
     * @param minimum Minimumwert
     * @param maximum Maximalwert
     * @param average Durchschnittswert
     */
    public void setStatisticValuesForTemperature(double minimum, double maximum, double average) {
        String minimumStr = String.format(getResources().getString(R.string.value_min), minimum);
        String maximumStr = String.format(getResources().getString(R.string.value_max), maximum);
        String avgStr     = String.format(getResources().getString(R.string.value_avg), average);

        ((TextView) findViewById(R.id.minimum)).setText(minimumStr);
        ((TextView) findViewById(R.id.maximum)).setText(maximumStr);
        ((TextView) findViewById(R.id.average)).setText(avgStr);
    }


    /**
     * OnClick-Handler zum Herunterzählen der Tage
     *
     * @param view View, die diesen Handler referenziert
     */
    public void decreaseDays(View view) {
        this.changeSelectedDays(false);
    }



    /**
     * OnClick-Handler zum Hochzählen der Tage
     *
     * @param view View, die diesen Handler referenziert
     */
    public void increaseDays(View view) {
        this.changeSelectedDays(true);
    }


    /**
     * Aktualisiert die Anzeige der Tage, wenn die Buttons zum Hoch-/Herunterzählen geklickt wurden
     *
     * @param increase Flag, das angibt, ob die Anzahl hoch- oder heruntergezählt wurde
     */
    private void changeSelectedDays(boolean increase) {
        if (this.daysRangeSelect == null) {
            return;
        }

        int days = Integer.valueOf(this.daysRangeSelect.getText().toString());

        if (!increase && days == 1) {
            return;
        }

        days = (increase) ? days + 1 : days - 1;
        this.daysRangeSelect.setText(String.format(Locale.GERMAN, "%d", days));
    }
}
