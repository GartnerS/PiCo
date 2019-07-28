package com.nex.blub.PiCo.API;

import com.nex.blub.PiCo.DeviceUpdater;

public class TradfriAPI extends API {

    //  Referenz, auf einen DeviceUpdater, der mit dem Ergebnis der API-Abfrage benachrichtig wird
    private DeviceUpdater deviceUpdater;


    public TradfriAPI() {}


    public TradfriAPI(DeviceUpdater deviceUpdater) {
        this.deviceUpdater = deviceUpdater;
        this.setPort(8282);
        this.setTag("TradfriAPI");
    }


    protected void onPostExecute(String result) {
        // Falls ein Callback-Objekt registriert wurde
        if (this.deviceUpdater != null) {
            this.deviceUpdater.receiveResults(result);
        }
    }
}
