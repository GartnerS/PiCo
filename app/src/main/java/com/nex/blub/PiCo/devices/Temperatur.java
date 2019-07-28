package com.nex.blub.PiCo.devices;

import android.util.Log;

import com.nex.blub.PiCo.API.API;
import com.nex.blub.PiCo.API.HistoricAPI;
import com.nex.blub.PiCo.interfaces.Device;
import com.nex.blub.PiCo.interfaces.HasHistoryData;
import com.nex.blub.PiCo.interfaces.ShowHistoryData;
import com.nex.blub.PiCo.utils.HistoryData;
import com.nex.blub.PiCo.utils.JSONUtils;

import org.json.JSONObject;

import java.util.List;


/**
 *  Klasse für ein Device, das Temperatur und Luftfeuchtigkeit misst.
 */
@SuppressWarnings("ALL")
public class Temperatur implements Device, HasHistoryData {

    // Log-Tag für diese Klasse
    private static final String TAG = "Temperatur";

    // Name des Device, dient als ID für die Abfrage über die API
    private String name;

    // Aktueller Temperatur-Wert
    private double temperature;

    // Aktueller Luftfeuchtigkeitswert
    private double humidity;

    // Liste mit historischen Werten
    private List<HistoryData> historyData;

    // Referenz zu der View, die dafür zuständig ist, die historischen Daten anzuzeigen
    private ShowHistoryData historyView;


    /**
     * Konstruktor
     *
     * @param name Name des Device
     */
    public Temperatur(String name) {
        this.name = name;
    }


    /*
     * Methode, die das Ergebnis einer API-Abfrage verarbeitet
     * Wird durch die API-Instanz aufgerufen.
     */
    public void updateValues(String result) {
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
    public void setCurrentValues(JSONObject data) {
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


    /**
     * Abfrage der historischen Daten des Geräts
     *
     * @param days Anzahl der Tage, für die die historischen Daten abgefragt werden sollen
     */
    public void getHistory(int days) {
        new HistoricAPI(this).execute("?device=" + this.name + "&days=" + days);
    }


    /**
     * Callback-Methode, die aufgerufen wird, wenn die historischen Daten
     * abgefragt wurde.
     *
     * @param result String im JSON-Format, die die Antwort des API-Anfrage darstellt.
     */
    public void receiveHistoryResult(String result) {
        this.historyData = JSONUtils.convertStringToHistoryData(result);

        if (this.historyView == null) {
            Log.e(TAG, "Keine View zum Anzeigen der historischen Daten registriert");
            return;
        }

        this.historyView.show();
    }


    /**
     *  Getter für die Liste der historischen Daten
     *
     * @return Liste mit den historischen Daten
     */
    public List<HistoryData> getHistoryData() {
        return this.historyData;
    }


    /**
     * Eine View registrieren, die für die Anzeige der historischen Daten
     * zuständig ist.
     *
     * @param callbackView View, zum Anzeigen der Daten
     */
    public void registerViewToShowData(ShowHistoryData callbackView) {
        this.historyView = callbackView;
    }
}
