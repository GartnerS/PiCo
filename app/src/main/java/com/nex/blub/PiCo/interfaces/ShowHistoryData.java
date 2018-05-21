package com.nex.blub.PiCo.interfaces;

public interface ShowHistoryData {

    // Zeigt die historischen Daten an
    void show();

    // Setzt statistische Werte
    void setStatisticValuesForTemperature(double minimum, double maximum, double average);
}
