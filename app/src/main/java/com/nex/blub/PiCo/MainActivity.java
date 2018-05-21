package com.nex.blub.PiCo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.nex.blub.PiCo.devices.Light;
import com.nex.blub.PiCo.devices.Temperatur;
import com.nex.blub.PiCo.interfaces.Device;
import com.nex.blub.PiCo.interfaces.Notifiable;
import com.nex.blub.PiCo.interfaces.PimaticActivity;
import com.nex.blub.PiCo.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends Activity implements PimaticActivity, SwipeRefreshLayout.OnRefreshListener, Notifiable {

    private boolean isActivityInForeground = false;

    private final Map<Device, List<View>> devices = new HashMap<>();

    private DeviceUpdater deviceUpdater;

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        // Alle Geräte der Map hinzufügen
        devices.put(new Light("WohnzimmerLicht"), new ArrayList<View>() {{
            add(findViewById(R.id.button_licht));
        }});
        devices.put(new Light("EsszimmerLicht"), new ArrayList<View>() {{
            add(findViewById(R.id.button_licht_esszimmer));
        }});
        devices.put(new Temperatur("Wohnzimmer"), new ArrayList<View>() {{
            add(findViewById(R.id.Wohnzimmer_Temp));
            add(findViewById(R.id.Wohnzimmer_Hum));
        }});
        devices.put(new Temperatur("Arbeitszimmer"), new ArrayList<View>() {{
            add(findViewById(R.id.Arbeitszimmer_Temp));
        }});
        devices.put(new Temperatur("Draussen"), new ArrayList<View>() {{
            add(findViewById(R.id.Draussen_Temp));
            add(findViewById(R.id.Draussen_Hum));
        }});
        devices.put(new Temperatur("Kueche"), new ArrayList<View>() {{
            add(findViewById(R.id.Kueche_Temp));
            add(findViewById(R.id.Kueche_Hum));
        }});
        devices.put(new Temperatur("Bad"), new ArrayList<View>() {{
            add(findViewById(R.id.Bad_Temp));
            add(findViewById(R.id.Bad_Hum));
        }});
        devices.put(new Temperatur("Schlafzimmer"), new ArrayList<View>() {{
            add(findViewById(R.id.Schlafzimmer_Temp));
            add(findViewById(R.id.Schlafzimmer_Hum));
        }});

        this.deviceUpdater = new DeviceUpdater(this, 60000);
        this.deviceUpdater.update();

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        this.deviceUpdater.update();
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
     * Button in ActionBar wird ausgewählt
     *
     * @param item Item das ausgewählt wurde
     * @return bool
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                this.deviceUpdater.update();
                break;
            case R.id.action_show_settings:
                showSettings();
                break;
            default:
                break;
        }
        return true;
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


    /**
     * Callback-Methode, die von DeviceUpdater aufgerufen wird
     * Ruft die Methoden zum Aktualisieren der Werte der Geräte auf.
     *
     * @param result Resultat der API-Abfrage
     */
    public void getNotification(String result) {
        Set<Map.Entry<Device, List<View>>> entries = this.devices.entrySet();

        for (Map.Entry<Device, List<View>> entry : entries) {
            Device device = entry.getKey();
            List<View> views = entry.getValue();

            device.updateValues(result);

            if (views.get(0) == null) {
                continue;
            }

            if (device instanceof Light) {
                ((Switch) views.get(0)).setChecked(((Light) device).getStatus());
            }

            if (device instanceof Temperatur) {
                Temperatur temperatur = (Temperatur) device;

                String temp = String.format(getResources().getString(R.string.temp), StringUtils.formatDoubleToDecimal(temperatur.getTemperature()));
                ((TextView) views.get(0)).setText(temp);

                if (views.size() > 1) {
                    String hum = String.format(getResources().getString(R.string.hum), StringUtils.formatDoubleToDecimal(temperatur.getHumidity()));
                    ((TextView) views.get(1)).setText(hum);
                }
            }
        }

        Notifier.showToast(this, R.string.values_refreshed);
    }


    private void showSettings() {
        Intent settingsAcitivity = new Intent(this, Settings.class);
        startActivity(settingsAcitivity);
    }


    @Nullable
    private Device getDeviceByName(String name) {
        for (Device device : this.devices.keySet()) {
            if (device.getName().equals(name)) {
                return device;
            }
        }

        return null;
    }
}