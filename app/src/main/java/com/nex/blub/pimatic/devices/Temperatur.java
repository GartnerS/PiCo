package com.nex.blub.pimatic.devices;

import android.util.Log;
import com.nex.blub.pimatic.API;
import com.nex.blub.pimatic.interfaces.Device;
import com.nex.blub.pimatic.utils.JSONUtils;
import org.json.JSONObject;


/**
 *  Klasse für ein Device, das Temperatur und Luftfeuchtigkeit misst.
 */
public class Temperatur implements Device {

    // Log-Tag für diese Klasse
    private static final String TAG = "Temperatur";

    // Name des Device, dient als ID für die Abfrage über die API
    private String name;

    // Aktueller Temperatur-Wert
    private double temperature;

    // Aktueller Luftfeuchtigkeitswert
    private double humidity;


    /**
     * Konstruktor
     *
     * @param name Name des Device
     */
    public Temperatur(String name) {
        this.name = name;
    }


    /**
     *  Abfrage der aktuellen Werte des Devices
     */
    public void update() {
        new API(this).execute("values");
    }


    /*
     * Methode, die das Ergebnis einer API-Abfrage verarbeitet
     * Wird durch die API-Instanz aufgerufen.
     */
    public void receiveResult(String result) {
        JSONObject object = JSONUtils.getValuesByDeviceName(result, this.name);
        if (object != null) {
            this.setCurrentValues(object);
        }
    }


    /**
     * Aktuelle Daten des Device anzeigen
     *
     * @param data JSONObject, das die aktuellen Werte des Device beinhaltet
     */
    private void setCurrentValues(JSONObject data) {
        try {
            this.temperature    = data.getDouble("temperature");
            this.humidity       = data.getDouble("humidity");
        } catch(Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    /**
     * Getter für den Namen des Device
     *
     * @return den Namen des Device
     */
    public String getName() {
        return this.name;
    }


    /**
     * Getter für Termperatur
     *
     * @return der aktuelle Temperatur-Wert
     */
    public double getTemperature() {
        return this.temperature;
    }


    /**
     * Getter für Luftfeuchtigkeit
     *
     * @return der aktuelle Luftfeuchtigkeitswert
     */
    public double getHumidity() {
        return this.humidity;
    }
}
