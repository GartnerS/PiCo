package com.nex.blub.PiCo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
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
import com.nex.blub.PiCo.interfaces.Notifiable;
import com.nex.blub.PiCo.interfaces.PiCoActivity;
import com.nex.blub.PiCo.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends Activity implements PiCoActivity, SwipeRefreshLayout.OnRefreshListener, Notifiable {

    private static boolean activityInitDone = false;

    private boolean isActivityInForeground = false;

    private static Map<Device, List<View>> devices;

    private DeviceUpdater deviceUpdater;

    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        if (!activityInitDone) {
            Log.i("Main", "Devices ist leer");
            this.initDevices();
            this.deviceUpdater = new DeviceUpdater(this, 60000);
            this.deviceUpdater.update();

            this.initShortCuts();

            swipeRefreshLayout = findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setOnRefreshListener(this);
        }

        String action = getIntent() != null ? getIntent().getAction() : null;
        if ("TOGGLE_SHORTCUT_WOHNZIMMER".equals(action)) {
            ((Light) this.getDeviceByName("WohnzimmerLicht")).toggle();
            MainActivity.this.moveTaskToBack(true);
        }

        if ("TOGGLE_SHORTCUT_ESSZIMMER".equals(action)) {
            ((Light) this.getDeviceByName("EsszimmerLicht")).toggle();
            MainActivity.this.moveTaskToBack(true);
        }
    }


    private void initShortCuts() {
        ShortcutInfo wohnzimmer = new ShortcutInfo.Builder(this, "shortcutWohnzimmer")
                .setShortLabel("Wohnzimmerlicht")
                .setLongLabel("Wohnzimmerlicht")
                .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.light_on))
                .setIntent(new Intent(this, MainActivity.class).setAction("TOGGLE_SHORTCUT_WOHNZIMMER"))
                .build();

        ShortcutInfo esszimmer = new ShortcutInfo.Builder(this, "shortcutEsszimmer")
                .setShortLabel("Esszimmerlicht")
                .setLongLabel("Esszimmerlicht")
                .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.light_on))
                .setIntent(new Intent(this, MainActivity.class).setAction("TOGGLE_SHORTCUT_ESSZIMMER"))
                .build();

        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        shortcutManager.setDynamicShortcuts(Arrays.asList(wohnzimmer, esszimmer));
    }


    /**
     * Wird aufgerufen, wenn in der View "nach unten" gezogen wird.
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


    /**
     * Wird aufgerufen, wenn die View wieder in der Vordergrund kommt
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.isActivityInForeground = true;
    }


    /**
     * Wird aufgerufen, wenn die View nicht mehr im Vordergrund ist
     */
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
        String deviceName = (view.getId() == R.id.button_licht) ? "WohnzimmerLicht" : "EsszimmerLicht";

        Light light = ((Light) this.getDeviceByName(deviceName));
        if (light != null) {
            light.toggle();
        }
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


    /**
     * OnClick-Methode für die Temperaturgeräte
     * Startet eine neue Detail-View
     *
     * @param view View, dessen OnClick-Event diese Methode referenziert
     */
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
        Set<Map.Entry<Device, List<View>>> entries = devices.entrySet();

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


    /**
     * Getter für 'isActivityInForeground'
     *
     * @return Gibt an, ob die View im Vordergrund ist
     */
    public boolean isInForeground() {
        return this.isActivityInForeground;
    }


    /**
     * Getter für den Context
     *
     * @return aktueller Context
     */
    public Context getContext() {
        return this.getBaseContext();
    }


    /**
     * Startet eine neue Settings-View
     */
    private void showSettings() {
        Intent settingsAcitivity = new Intent(this, Settings.class);
        startActivity(settingsAcitivity);
    }


    private void initDevices() {

        if (devices == null) {
            devices = new HashMap<>();
        }

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

        activityInitDone = true;
    }


    /**
     * Liefert zu einem Gerätename eine entsprechende Referenz auf ein Device-Objekt
     *
     * @param name Name des Geräts
     * @return Referenz auf ein Device-Objekts
     */
    @Nullable
    private Device getDeviceByName(String name) {
        for (Device device : devices.keySet()) {
            if (device.getName().equals(name)) {
                return device;
            }
        }

        return null;
    }
}