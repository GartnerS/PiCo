package com.nex.blub.PiCo.API;

import com.nex.blub.PiCo.interfaces.HasHistoryData;

public class HistoricAPI extends API {

    // Wenn != null, dann wird nach einem Request die Methode "receiveResult(string)" des Objekts aufgerufen.
    private HasHistoryData callBackObj;


    /**
     * Konstruktor
     *
     * @param callBackObj Device-Objekt dessen Methode "receiveResult(string)"  nach einem Request
     *                    aufgerufen werden soll
     **/
    public HistoricAPI(HasHistoryData callBackObj) {
        this.callBackObj = callBackObj;
        this.setPort(8181);
        this.setTag("HistoricAPI");
    }


    protected void onPostExecute(String result) {
        // Falls ein Callback-Objekt registriert wurde
        if (this.callBackObj != null) {
            this.callBackObj.receiveHistoryResult(result);
        }
    }
}
