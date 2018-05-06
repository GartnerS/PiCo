package com.nex.blub.PiCo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.nex.blub.PiCo.charts.HistoryChart;
import com.nex.blub.PiCo.devices.Temperatur;
import com.nex.blub.PiCo.interfaces.HasHistoryData;
import com.nex.blub.PiCo.interfaces.ShowHistoryData;


public class Details extends Activity implements ShowHistoryData {

    // Log-Tag für diese Klasse
    private static final String TAG = "Details";

    private String title;

    private HasHistoryData device;

    private HistoryChart chart;

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

        this.chart = new HistoryChart((LineChart) findViewById(R.id.chart));
        this.chart.setStatisticViews((TextView) findViewById(R.id.minimum), (TextView) findViewById(R.id.maximum), (TextView) findViewById(R.id.average));

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
}
