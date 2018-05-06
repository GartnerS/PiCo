package com.nex.blub.PiCo.interfaces;

import com.nex.blub.PiCo.utils.HistoryData;

import java.util.List;


public interface HasHistoryData {

    // Diese Methode wird nach einem API-Request aufgerufen, der die historischen Daten abfragt
    void receiveHistoryResult(String result);

    // Abfrage der historischen Werte für das Gerät
    void getHistory(int days);

    // Rückgabe der Liste mit den historischen Daten
    List<HistoryData> getHistoryData();

    // View registrieren, die die historischen Daten anzeigt
    void registerViewToShowData(ShowHistoryData callbackView);
}
