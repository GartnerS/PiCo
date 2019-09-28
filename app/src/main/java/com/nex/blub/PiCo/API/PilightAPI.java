package com.nex.blub.PiCo.API;

import com.nex.blub.PiCo.DeviceUpdater;

public class PilightAPI extends API {

    //  Referenz, auf einen DeviceUpdater, der mit dem Ergebnis der API-Abfrage benachrichtig wird
    private DeviceUpdater deviceUpdater;


    public PilightAPI() {
        this.setPort(80);
        this.setTag("PilightAPI");
    }


    /**
     * Konstruktor
     *
     * @param deviceUpdater Updater dessen Methode "receiveResult(string)"  nach einem Request
     *                      aufgerufen werden soll
     */
    public PilightAPI(DeviceUpdater deviceUpdater) {
        this();
        this.deviceUpdater = deviceUpdater;
    }


    protected void onPostExecute(String result) {
        // Falls ein Callback-Objekt registriert wurde
        if (this.deviceUpdater != null) {
            this.deviceUpdater.receiveResults(result);
        }
    }


}
