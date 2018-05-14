package com.nex.blub.PiCo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.nex.blub.PiCo.devices.Light;
import com.nex.blub.PiCo.devices.Temperatur;
import com.nex.blub.PiCo.interfaces.Device;
import com.nex.blub.PiCo.interfaces.PimaticActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class MainActivity extends Activity implements PimaticActivity, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainAcivity";

    private static final int TIMER_INTERVAL = 60000; // = 1min

    private boolean isActivityInForeground = false;

    private final Map<Device, List<View>> Devices = new HashMap<>();

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        // Alle Geräte der Map hinzufügen
        Devices.put(new Light("WohnzimmerLicht"), new ArrayList<View>() {{
            add(findViewById(R.id.button_licht));
        }});
        Devices.put(new Light("EsszimmerLicht"), new ArrayList<View>() {{
            add(findViewById(R.id.button_licht_esszimmer));
        }});
        Devices.put(new Temperatur("Wohnzimmer"), new ArrayList<View>() {{
            add(findViewById(R.id.Wohnzimmer_Temp));
            add(findViewById(R.id.Wohnzimmer_Hum));
        }});
        Devices.put(new Temperatur("Arbeitszimmer"), new ArrayList<View>() {{
            add(findViewById(R.id.Arbeitszimmer_Temp));
        }});
        Devices.put(new Temperatur("Draussen"), new ArrayList<View>() {{
            add(findViewById(R.id.Draussen_Temp));
            add(findViewById(R.id.Draussen_Hum));
        }});
        Devices.put(new Temperatur("Kueche"), new ArrayList<View>() {{
            add(findViewById(R.id.Kueche_Temp));
            add(findViewById(R.id.Kueche_Hum));
        }});
        Devices.put(new Temperatur("Bad"), new ArrayList<View>() {{
            add(findViewById(R.id.Bad_Temp));
            add(findViewById(R.id.Bad_Hum));
        }});
        Devices.put(new Temperatur("Schlafzimmer"), new ArrayList<View>() {{
            add(findViewById(R.id.Schlafzimmer_Temp));
            add(findViewById(R.id.Schlafzimmer_Hum));
        }});

        this.setDeviceUpdater();

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    /**
     * Initialisiert einen Timer, um die Werte der Geräte zu aktualisieren.
     *
     * @param interval Intervall in Millisekunden bis der Timer abläuft
     * @param restart  Flag, ob der Timer nach Ablauf wieder gestartet werden soll
     */
    private void registerUpdateTimer(int interval, final boolean restart) {
        new CountDownTimer(interval, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Log.d(TAG, "Timer abgelaufen");
                updateAllDevices();
                if (restart) {
                    this.start();
                }
            }
        }.start();
    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        this.updateAllDevices();
        swipeRefreshLayout.setRefreshing(false);
    }


    /**
     * Methode wird beim Start der Activity ausgeführt
     * Übernimmt das Menu für die ActionBar
     *
     * @param menu Menu
     * @return bool
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        this.isActivityInForeground = true;
        this.updateAllDevices();
    }


    @Override
    protected void onPause() {
        super.onPause();
        this.isActivityInForeground = false;
    }


    /**
     * Wohnzimmer-Lampe wird ein-/ausgeschaltet
     *
     * @param view View, die das toggleEvent aufgerufen hat
     */
    public void toggleLight(View view) {
        Light light;
        switch (view.getId()) {
            case R.id.button_licht:
                light = ((Light) this.getDeviceByName("WohnzimmerLicht"));
                if (light != null) {
                    light.toggle();
                }
                break;
            case R.id.button_licht_esszimmer:
                light = ((Light) this.getDeviceByName("EsszimmerLicht"));
                if (light != null) {
                    light.toggle();
                }
                break;
            default:
                break;
        }
    }


    public boolean isInForeground() {
        return this.isActivityInForeground;
    }


    public Context getContext() {
        return this.getBaseContext();
    }


    /**
     * Ruft bei allen Devices die update()-Methode auf
     */
    private void updateAllDevices() {

        if (!this.checkWifiOnAndConnected()) {
            Log.e(TAG, "Abfrage der Device-Werte nicht möglich, da nicht mit WLAN verbunden oder PI nicht verfügbar");
            return;
        }

        for (Device d : this.Devices.keySet()) {
            d.update();
        }

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        Set<Map.Entry<Device, List<View>>> entries = this.Devices.entrySet();

        for (Map.Entry<Device, List<View>> entry : entries) {
            Device device = entry.getKey();
            List<View> views = entry.getValue();

            if (device instanceof Light) {
                ((Switch) views.get(0)).setChecked(((Light) device).getStatus());
            }

            if (device instanceof Temperatur) {
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
                formatter.applyPattern("##.#");

                String temp = String.format(getResources().getString(R.string.temp), formatter.format(((Temperatur) device).getTemperature()));

                if (views.get(0) == null) {
                    continue;
                }
                ((TextView) views.get(0)).setText(temp);

                if (views.size() > 1) {
                    String hum = String.format(getResources().getString(R.string.hum), formatter.format(((Temperatur) device).getHumidity()));
                    ((TextView) views.get(1)).setText(hum);
                }
            }
        }

        Notifier.showToast(this, R.string.values_refreshed);
    }


    /**
     * Button in ActionBar wird ausgewählt
     *
     * @param item Item das ausgewählt wurde
     * @return bool
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateAllDevices();
                break;
            case R.id.action_show_settings:
                showSettings();
                break;
            default:
                break;
        }
        return true;
    }


    private void showSettings() {
        Intent settingsAcitivity = new Intent(this, Settings.class);
        startActivity(settingsAcitivity);
    }


    public void showDetails(View view) {
        Intent detailActivity = new Intent(this, Details.class);

        switch (view.getId()) {
            case R.id.Wohnzimmer:
                detailActivity.putExtra("Device_Name", "Wohnzimmer");
                detailActivity.putExtra("Device_ID", "WohnzimmerTemp");
                break;
            case R.id.Kueche:
                detailActivity.putExtra("Device_Name", "Kueche");
                detailActivity.putExtra("Device_ID", "Kueche");
                break;
            case R.id.Schlafzimmer:
                detailActivity.putExtra("Device_Name", "Schlafzimmer");
                detailActivity.putExtra("Device_ID", "Schlafzimmer");
                break;
            case R.id.Bad:
                detailActivity.putExtra("Device_Name", "Bad");
                detailActivity.putExtra("Device_ID", "Bad");
                break;
            case R.id.Arbeitszimmer:
                detailActivity.putExtra("Device_Name", "Arbeitszimmer");
                detailActivity.putExtra("Device_ID", "Arbeitszimmer");
                break;
            case R.id.Draussen:
                detailActivity.putExtra("Device_Name", "Draussen");
                detailActivity.putExtra("Device_ID", "Draussen");
                break;
            default:
                return;
        }

        startActivity(detailActivity);
    }


    private void setDeviceUpdater() {
         //&& API.isPiAvailable()
        // Timer nur setzen, wenn mit WLAN verbunden und Pi verfügbar ist
        if (this.checkWifiOnAndConnected()) {
            // normaler Timer zum Aktualisieren der Geräte
            this.registerUpdateTimer(TIMER_INTERVAL, true);
            // Timer um nach dem Laden der View die Werte der Geräte zu laden
            this.registerUpdateTimer(1000, false);
        }
    }


    @Nullable
    private Device getDeviceByName(String name) {
        for (Device device : this.Devices.keySet()) {
            if (device.getName().equals(name)) {
                return device;
            }
        }

        return null;
    }


    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr != null && wifiMgr.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            return (wifiInfo.getNetworkId() != -1);
        }

        return false;
    }
}