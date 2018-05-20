package com.nex.blub.PiCo.devices;

import android.util.Log;

import com.nex.blub.PiCo.API;
import com.nex.blub.PiCo.interfaces.Device;
import com.nex.blub.PiCo.utils.JSONUtils;

import org.json.JSONObject;


/**
 *  Klasse, die eine Lampe darstellt
 */
public class Light implements Device {

    // Log-Tag für diese Klasse
    private static final String TAG = "Light";

    // Gibt ab, ob die Lampe ein- oder ausgeschaltet ist
    private boolean isOn = false;

    // Name des Geräts. Entspricht der Device-ID bei Pimatic
    private final String name;


    /**
     * Konstruktor
     *
     * @param deviceName Name des Geräts (entspricht der ID bei Pimatic)
     */
    public Light(String deviceName) {
        this.name = deviceName;
    }


    /**
     * Die Lampe wird je nach aktuellem Zustand ein- oder ausgeschaltet
     */
    public void toggle() {
        if (!this.isOn) {
            this.on();
        } else {
            this.off();
        }

        this.isOn = !this.isOn;
    }


    /**
     * Lampe soll eingeschaltet werden => entsprechender API-Request wird gesendet
     */
    private void on() {
        new API().execute("control?state=on&device=" + this.name);
    }


    /**
     * Lampe soll ausgeschaltet werden => entsprechender API-Request wird gesendet
     */
    private void off() {
        new API().execute("control?state=off&device=" + this.name);
    }


    /**
     * Wird nach einem API-Request automatisch aufgerufen, sofern die Instanz als Callback-Objekt
     * bei der API registriert ist.
     *
     * @param result Das Ergebnis des API-Request (Antwort der API)
     */
    public void updateValues(String result) {
        JSONObject object = JSONUtils.getValuesByDeviceName(result, this.name);
        if (object != null) {
            boolean isLightOn = false;
            try {
                isLightOn = object.getString("state").equals("on");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            this.isOn = isLightOn;
            Log.d(TAG, "Status des Lichts '" + this.name + "' ist: " + this.isOn);
        }
    }


    /**
     * Abfragen des aktuellen Status des Lichts
     *
     * @return aktueller Status, ob das Licht an ist
     */
    public boolean getStatus() {
        return this.isOn;
    }


    /**
     * Getter für den Namen des Device
     *
     * @return der Name des Device
     */
    public String getName() {
        return this.name;
    }
}
